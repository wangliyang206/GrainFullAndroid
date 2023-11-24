package com.zqw.mobile.grainfull.mvp.presenter;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.zqw.mobile.grainfull.app.config.CommonRetryWithDelay;
import com.zqw.mobile.grainfull.mvp.contract.ChatGPTContract;
import com.zqw.mobile.grainfull.mvp.model.entity.ChatCompletionChunk;

import java.io.BufferedReader;
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

    @Inject
    public ChatGPTPresenter(ChatGPTContract.Model model, ChatGPTContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 创建 聊天 会话
     */
    public void chatCreate(String message) {
        mModel.chatCreate(message)
                .subscribeOn(Schedulers.io())
                .retryWhen(new CommonRetryWithDelay(0, 2))                 // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
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
    }

    /**
     * 解析结果
     */
    private void onAnalysis(ResponseBody info) {
        // 流式输出
        buffer = new StringBuffer();
        Gson gson = new Gson();

        // 测试数据
        StringBuffer text = new StringBuffer("当然可以！以下是制作杏仁蛋糕的步骤：\n" +
                "    \n" +
                "    材料：\n" +
                "    - 1杯杏仁粉\n" +
                "    - 4个鸡蛋\n" +
                "    - 1/2杯糖\n" +
                "    - 1/4杯牛奶\n" +
                "    - 1茶匙香草精\n" +
                "    - 1/2茶匙泡打粉\n" +
                "    - 1/4茶匙盐\n" +
                "    \n" +
                "    步骤：\n" +
                "    1. 预热烤箱至摄氏180度（华氏350度）。准备一个9英寸（23厘米）的圆形蛋糕模具，并在底部涂抹一些油或撒上少量面粉，以防粘。\n" +
                "    \n" +
                "    2. 在一个大碗中，先分离蛋黄和蛋白。将蛋黄和糖一起搅拌，直到糖完全溶解。\n" +
                "    \n" +
                "    3. 加入牛奶和香草精，搅拌均匀。\n" +
                "    \n" +
                "    4. 将杏仁粉、泡打粉和盐混合在另一个碗中。\n" +
                "    \n" +
                "    5. 将杏仁粉混合物加入蛋黄糊中，搅拌均匀，直至没有任何颗粒。\n" +
                "    \n" +
                "    6. 在另一个干净的碗中，打发蛋白至硬性发泡。\n" +
                "    \n" +
                "    7. 将1/3的蛋白混合到蛋黄糊中，搅拌均匀，然后再轻轻地将剩余的蛋白混合进去，直到完全融合。\n" +
                "    \n" +
                "    8. 将蛋糕糊倒入预先准备好的蛋糕模具中。\n" +
                "    \n" +
                "    9. 将蛋糕放入预热的烤箱中，烤约25-30分钟，或直到蛋糕表面呈金黄色，用牙签插入蛋糕中心，牙签取出时干净即可。\n" +
                "    \n" +
                "    10. 烤好后，取出蛋糕，待其完全冷却后即可享用。\n" +
                "    \n" +
                "    11. 可以根据个人喜好在蛋糕上撒上一些糖霜或杏仁碎末作为装饰。\n" +
                "    \n" +
                "    祝您制作成功，并且享用美味的杏仁蛋糕！");

        // 开启线程处理
        new Thread(() -> {
            // 获取response输入流
            InputStream inputStream = info.byteStream();
            // 读取响应数据
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // 处理每一行数据：
                    // data: {"id":"chatcmpl-8OGrtFmsVCqDHrBzeS70L4BvfAEoc","object":"chat.completion.chunk","created":1700795405,"model":"gpt-3.5-turbo-0613","choices":[{"index":0,"delta":{"content":"我"},"finish_reason":null}]}
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
                                    // 停顿0.3秒
                                    Thread.sleep(300);
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
    }
}