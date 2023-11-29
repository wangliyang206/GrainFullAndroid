package com.zqw.mobile.grainfull.mvp.presenter;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.zqw.mobile.grainfull.app.config.CommonRetryWithDelay;
import com.zqw.mobile.grainfull.mvp.contract.ChatGPTContract;
import com.zqw.mobile.grainfull.mvp.model.entity.ChatCompletionChunk;
import com.zqw.mobile.grainfull.mvp.model.entity.ChatImg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.ResponseBody;
import timber.log.Timber;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/11/23 15:49
 * ================================================
 */
@ActivityScope
public class ChatGPTPresenter extends BasePresenter<ChatGPTContract.Model, ChatGPTContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    // 用于接收消息，流式输出
    private StringBuffer buffer;
    private ChatCompletionChunk chatCompletionChunk;
    private ChatImg chatImg;
    private Gson gson = new Gson();

    @Inject
    public ChatGPTPresenter(ChatGPTContract.Model model, ChatGPTContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 创建会话，类型：聊天、图片
     *
     * @param message 需要内容
     */
    public void chatCreate(String message) {
        // 判断是文字还是图片，这里使用“模糊匹配”
        String condition = ".*[制作|生成].*[图|照][片|像].*";
        if (message.matches(condition)) {
            // 类型：图片会话
            onSmartMessaging(2, message);
        } else {
            // 类型：文字会话
            onSmartMessaging(1, message);
        }
    }

    /**
     * 创建会话
     *
     * @param type    类型：1聊天，2图片
     * @param message 需要内容
     */
    public void onSmartMessaging(int type, String message) {
        if (type == 1) {
            // 创建 聊天 会话
            mModel.chatCreate(message)
                    .subscribeOn(Schedulers.io())
                    .retryWhen(new CommonRetryWithDelay(0, 2))             // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                    .doOnSubscribe(disposable -> {
//                    mRootView.showLoadingSubmit();                                                  // 显示进度条
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(() -> {
//                    mRootView.hideLoadingSubmit();                                                  // 隐藏进度条
                    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                    .subscribe(new ErrorHandleSubscriber<ResponseBody>(mErrorHandler) {
                        @Override
                        public void onError(Throwable t) {
                            Timber.i("##### t=%s", t.getMessage());
                            buffer = new StringBuffer();
                            buffer.append("请求超时，请检查网络并重试");
                            mRootView.onLoadMessage(buffer);
                            mRootView.onSucc();
                        }

                        @Override
                        public void onNext(ResponseBody info) {
                            onAnalysis(info);
                        }
                    });
        } else {
            // 创建 图片 会话
            mModel.chatImg(message)
                    .subscribeOn(Schedulers.io())
                    .retryWhen(new CommonRetryWithDelay(0, 2))             // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                    .doOnSubscribe(disposable -> {
//                    mRootView.showLoadingSubmit();                                                  // 显示进度条
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(() -> {
//                    mRootView.hideLoadingSubmit();                                                  // 隐藏进度条
                    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                    .subscribe(new ErrorHandleSubscriber<ResponseBody>(mErrorHandler) {
                        @Override
                        public void onError(Throwable t) {
                            Timber.i("##### t=%s", t.getMessage());
                            buffer = new StringBuffer();
//                            buffer.append("请求超时，请检查网络并重试");
                            buffer.append("openkey暂不支持图片输入");
                            mRootView.onLoadMessage(buffer);
                            mRootView.onSucc();
                        }

                        @Override
                        public void onNext(ResponseBody info) {
                            try {
                                String respStr = info.string();
                                Timber.d("##### onResponse: %s", respStr);
                                chatImg = gson.fromJson(respStr, ChatImg.class);
                                mRootView.onLoadImages(chatImg.getData().get(0).getUrl());
                                mRootView.onSucc();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    /**
     * 解析结果
     */
    private void onAnalysis(ResponseBody info) {
        // 流式输出
        buffer = new StringBuffer();

        // 开启线程处理
        new Thread(() -> {
            // 获取response输入流
            InputStream inputStream = info.byteStream();
            // 读取响应数据
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // 处理每一行数据：
                    // data: {"id":"chatcmpl-8OLSS8urj19bZa2AnHhK5UnRdyVUa","object":"chat.completion.chunk","created":1700813048,"model":"gpt-3.5-turbo-0613","choices":[{"index":0,"delta":{"content":"中国"},"finish_reason":null}]}
                    // data: {"id":"chatcmpl-8OLSS8urj19bZa2AnHhK5UnRdyVUa","object":"chat.completion.chunk","created":1700813048,"model":"gpt-3.5-turbo-0613","choices":[{"index":0,"delta":{"content":"文"},"finish_reason":null}]}
                    // data: {"id":"chatcmpl-8OLSS8urj19bZa2AnHhK5UnRdyVUa","object":"chat.completion.chunk","created":1700813048,"model":"gpt-3.5-turbo-0613","choices":[{"index":0,"delta":{"content":"化"},"finish_reason":null}]}
                    // 判断是否返回了数据，去除response前data关键字，不然解析不了
                    if (line.length() > 6) {
                        Timber.d("##### onResponse: %s", line.substring(6));
                        try {
                            chatCompletionChunk = gson.fromJson(line.substring(5), ChatCompletionChunk.class);
                            Timber.d("onAnalysis: %s", chatCompletionChunk.getChoices().get(0).getDelta().getContent());
                            if (chatCompletionChunk.getChoices().get(0).getDelta().getContent() != null) {
                                addNewlineAfterPeriod(chatCompletionChunk.getChoices().get(0).getDelta().getContent());
                                buffer.append(chatCompletionChunk.getChoices().get(0).getDelta().getContent());

                                try {
                                    // 停顿0.2秒
                                    Thread.sleep(200);
                                } catch (Exception ignored) {
                                }
                                mRootView.onLoadMessage(buffer);
                            }
                            if (chatCompletionChunk.getChoices().get(0).getFinishReason() != null) {
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            buffer.append("请求有误，请检查key或稍后重试，当然，如果你上下文足够长也会出现，请自行找寻原因，嗯，我懒得写 /doge");

                            mRootView.onLoadMessage(buffer);
                            break;
                        }
                    }
                }

                mRootView.onSucc();

                Timber.d("onResult: %s", buffer.toString());
            } catch (Exception ignored) {

            }
        }).start();
    }

    /**
     * 对字符串进行处理
     */
    public void addNewlineAfterPeriod(String str) {
        StringBuilder sb = new StringBuilder();
        boolean periodFound = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '.' || c == '。') {
                periodFound = true;
                sb.append(c);
                sb.append('\n');
            } else if (c == '\n') {
                continue;
            } else {
                sb.append(c);
            }
        }
        if (!periodFound) {
            return;
        }
        sb.toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.gson = null;
    }
}