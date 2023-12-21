package com.zqw.mobile.grainfull.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.mvp.contract.ChatGPTContract;
import com.zqw.mobile.grainfull.mvp.model.api.AccountService;
import com.zqw.mobile.grainfull.mvp.model.entity.WhisperResponse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/11/23 15:49
 * ================================================
 */
@ActivityScope
public class ChatGPTModel extends BaseModel implements ChatGPTContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;
    @Inject
    AccountManager mAccountManager;

    @Inject
    public ChatGPTModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
        this.mAccountManager = null;
    }

    @Override
    public Observable<ResponseBody> getTokenBalance(String sk) {
        // 转换成Json
        String json = "{\"api_key\": \"" + sk + "\"}";
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return mRepositoryManager.obtainRetrofitService(AccountService.class).chatCreate(Constant.CHATGPT_TOKEN, requestBodyJson);
    }

    @Override
    public Observable<ResponseBody> chatCreate(String message) {
        // 转换成Json
        message = "{\"model\": \"" + mAccountManager.getChatGptVersion() + "\", " +
                "\"messages\": [{\"role\": \"user\", \"content\":  \"" + message + "\"}] , " +
                "\"stream\" : true}";
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), message);
        return mRepositoryManager.obtainRetrofitService(AccountService.class).chatCreate(Constant.CHATGPT_CHAT_URL, requestBodyJson);
    }

    @Override
    public Observable<ResponseBody> chatImg(String message) {
        // 转换成Json
        message = "{\"model\": \"dall-e-3\", \"prompt\": \"" + message + "\", \"n\": 1,\"quality\": \"hd\", \"size\" : \"1024x1024\"}";
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), message);
        return mRepositoryManager.obtainRetrofitService(AccountService.class).chatCreate(Constant.CHATGPT_IMAGE_URL, requestBodyJson);
    }

    /**
     * 语音转文字
     */
    @Override
    public Observable<WhisperResponse> voiceToText(File file) {
        // 文件
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
        // 自定义参数
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("language", RequestBody.create(MediaType.parse("multipart/form-data"), "zh"));
        requestBodyMap.put("model", RequestBody.create(MediaType.parse("multipart/form-data"), "whisper-1"));
        requestBodyMap.put("response_format", RequestBody.create(MediaType.parse("multipart/form-data"), "json"));

        return mRepositoryManager.obtainRetrofitService(AccountService.class).voiceToText(Constant.CHATGPT_TRANSCRIPTIONS_URL, multipartBody, requestBodyMap);
    }

    /**
     * 文字转语音
     */
    @Override
    public Observable<ResponseBody> textToSpeech(String text) {
        // model:可用的 TTS 模型之一:tts-1 或 tts-1-hd
        // input:要生成音频的文本。最大长度为4096个字符。
        // voice:生成音频时使用的语音。支持的语音有:alloy、echo、fable、onyx、nova 和 shimmer。
        // response_format:默认为 mp3 音频的格式。支持的格式有:mp3、opus、aac 和 flac。
        // speed:默认为 1 生成的音频速度。选择0.25到4.0之间的值。1.0是默认值。

        // 转换成Json
        text = "{" +
                "\"model\": \"tts-1-hd\"," +
                "\"input\": \"" + text + "\"," +
                "\"voice\": \"alloy\"," +
                "\"response_format\": \"mp3\"," +
                "\"speed\": 1" +
                "}";
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), text);
        return mRepositoryManager.obtainRetrofitService(AccountService.class).chatCreate(Constant.CHATGPT_SPEECH_URL, requestBodyJson);
    }
}