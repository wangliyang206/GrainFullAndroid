package com.zqw.mobile.grainfull.mvp.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.zqw.mobile.grainfull.app.config.CommonRetryWithDelay;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.mvp.contract.FastGPTContract;
import com.zqw.mobile.grainfull.mvp.model.entity.ChatCompletionChunk;
import com.zqw.mobile.grainfull.mvp.model.entity.ChatHistoryResponse;
import com.zqw.mobile.grainfull.mvp.model.entity.ChatInputs;
import com.zqw.mobile.grainfull.mvp.model.entity.ChatUserGuideModule;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

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
 * Created by MVPArmsTemplate on 2023/12/06 16:12
 * ================================================
 */
@ActivityScope
public class FastGPTPresenter extends BasePresenter<FastGPTContract.Model, FastGPTContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AccountManager mAccountManager;

    // 用于接收消息，流式输出
    private StringBuffer buffer;
    private ChatCompletionChunk chatCompletionChunk;
    private Gson gson = new Gson();
    // 默认提示语
    private String mDefaultTips = "您好，我是AI小助手，请问有什么可以帮助您的吗？";

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.gson = null;
        this.buffer = null;

        this.chatCompletionChunk = null;
    }

    @Inject
    public FastGPTPresenter(FastGPTContract.Model model, FastGPTContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获取对话日志
     */
    public void getChatHistory() {
        mModel.getChatHistory()
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
                .subscribe(new ErrorHandleSubscriber<ChatHistoryResponse>(mErrorHandler) {
                    @Override
                    public void onError(Throwable t) {
                        Timber.i("##### t=%s", t.getMessage());
                        mRootView.onLoadOpeningRemarks(mDefaultTips);
                    }

                    @Override
                    public void onNext(ChatHistoryResponse info) {
                        Timber.i("##### getChatHistory------------【Start】-------------");

                        Timber.i("##### onNext Code=%s", info.getCode());
                        Timber.i("##### onNext History=%s", info.getData().getHistory().size());
                        try {
                            if (info.getCode() == 200) {
                                // 拿到开场白
                                ChatUserGuideModule mChatUserGuideModule = info.getData().getApp().getUserGuideModule();

                                // 判断是否有历史记录
                                if (CommonUtils.isNotEmpty(info.getData().getHistory())) {
                                    // 有历史记录
                                    mRootView.onLoadHistory(getOpeningRemarks(mChatUserGuideModule), info.getData().getHistory());
                                } else {
                                    // 没有历史记录，单独加载 提示语
                                    mRootView.onLoadOpeningRemarks(getOpeningRemarks(mChatUserGuideModule));
                                }

                            } else {
                                mRootView.onLoadOpeningRemarks(mDefaultTips);
                            }
                        } catch (Exception ex) {
                            Timber.i("##### ex=%s", ex.getMessage());
                            mRootView.onLoadOpeningRemarks(mDefaultTips);
                        }

                        Timber.i("##### getChatHistory------------【End】-------------");
                    }
                });
    }

    /**
     * 获取开场白
     */
    private String getOpeningRemarks(ChatUserGuideModule mChatUserGuideModule) {
        String tips = "";
        if (mChatUserGuideModule != null) {
            Timber.i("##### name=%s", mChatUserGuideModule.getName());
            List<ChatInputs> mInputs = mChatUserGuideModule.getInputs();
            if (CommonUtils.isNotEmpty(mInputs)) {
                Timber.i("##### onNext Inputs=%s", mInputs.size());
                tips = mInputs.get(0).getValue().toString();
            }
        }

        return TextUtils.isEmpty(tips) ? mDefaultTips : tips;
    }

    /**
     * 创建会话，类型：聊天、图片
     *
     * @param message 需要内容
     */
    public void chatCreate(String message) {
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
                        showError(1, t.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody info) {
                        onAnalysis(info);
                    }
                });
    }

    /**
     * 解析结果
     */
    private void onAnalysis(ResponseBody info) {
        // 流式输出
        buffer = new StringBuffer();
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
                        }
                        if (chatCompletionChunk.getChoices().get(0).getFinishReason() != null) {
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        buffer.append("请求有误，请检查key或稍后重试，当然，如果你上下文足够长也会出现，请自行找寻原因，嗯，我懒得写 /doge");

                        mRootView.onLoadError(buffer);
                        break;
                    }
                }
            }

            // 流式展示
            mRootView.onLoadMessage(buffer);
            // 语音播报
            mRootView.onVoiceAnnouncements(buffer.toString());
            mRootView.onSucc();

            Timber.d("onResult: %s", buffer.toString());
        } catch (Exception ignored) {

        }
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


    /**
     * 显示错误信息
     *
     * @param type 类型：0代表自定义错误；1代表系统返回的错误；
     * @param str  错误内容
     */
    private void showError(int type, String str) {
        buffer = new StringBuffer();

        if (type == 0) {
            // 自定义错误
            buffer.append(str);
        } else {
            // 系统返回的错误
            if (str.contains("307")) {
                buffer.append("账户额度不足，请及时联系管理员充值！");
            } else if (str.contains("401")) {
                buffer.append("API密钥无效或未提供。你需要检查你的API密钥是否正确使用，拥有权限并且未过期或者提供。");
            } else if (str.contains("403")) {
                buffer.append("你是未授权访客。");
            } else if (str.contains("413")) {
                buffer.append("请求体太大。你可能需要压缩/减小你的请求数据量。");
            } else if (str.contains("429")) {
                buffer.append("由于发送的同类请求太多的话，你已经超过了你的连接限额。");
            } else if (str.contains("500")) {
                buffer.append("服务器内部错误。这可能是由于服务器发生的问题，不是你的问题。");
            } else if (str.contains("503")) {
                buffer.append("服务器暂时不可用。这可能是由于临时服务器维护/停机等服务器原因导致。");
            } else {
                buffer.append("请求超时，请检查网络并重试");
            }
        }
        mRootView.onLoadError(buffer);
        mRootView.onSucc();
    }
}