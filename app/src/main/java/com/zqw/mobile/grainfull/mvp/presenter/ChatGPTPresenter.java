package com.zqw.mobile.grainfull.mvp.presenter;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.app.utils.RxUtils;
import com.zqw.mobile.grainfull.mvp.contract.ChatGPTContract;
import com.zqw.mobile.grainfull.mvp.model.entity.ChatCompletionChunk;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

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

    @Inject
    public ChatGPTPresenter(ChatGPTContract.Model model, ChatGPTContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 创建 聊天 会话
     */
    public void chatCreate(String message) {
        mModel.chatCreate(message)
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<ResponseBody>(mErrorHandler) {
                    @Override
                    public void onError(Throwable t) {
                        buffer = new StringBuffer();
                        buffer.append("请求超时，请检查网络并重试");
                        mRootView.onLoadMessage(buffer);
                    }

                    @Override
                    public void onNext(ResponseBody info) {
                        // 流式输出
                        buffer = new StringBuffer();
                        Gson gson = new Gson();
                        // 获取response输入流
                        InputStream inputStream = info.byteStream();
                        // 读取响应数据
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                // 处理每一行数据
                                Timber.d("##### onResponse111: %s", line);
                                // 判断是否返回了数据，去除response前data关键字，不然解析不了
                                if (line.length() > 6) {
                                    Timber.d("##### onResponse222: %s", line.substring(6));
                                    try {
                                        chatCompletionChunk = gson.fromJson(line.substring(5), ChatCompletionChunk.class);
                                        Timber.d("onResponse333: %s", chatCompletionChunk.getChoices().get(0).getDelta().getContent());
                                        if (chatCompletionChunk.getChoices().get(0).getDelta().getContent() != null) {
                                            addNewlineAfterPeriod(chatCompletionChunk.getChoices().get(0).getDelta().getContent());
                                            buffer.append(chatCompletionChunk.getChoices().get(0).getDelta().getContent());

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

                            Timber.d("onResponse444: %s", buffer.toString());
                        } catch (Exception ignored) {

                        }
                    }
                });
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
    }
}