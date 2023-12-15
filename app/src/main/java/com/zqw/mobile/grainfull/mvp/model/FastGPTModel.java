package com.zqw.mobile.grainfull.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.mvp.contract.FastGPTContract;
import com.zqw.mobile.grainfull.mvp.model.api.AccountService;
import com.zqw.mobile.grainfull.mvp.model.entity.ChatHistoryResponse;
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
 * Created by MVPArmsTemplate on 2023/12/06 16:12
 * ================================================
 */
@ActivityScope
public class FastGPTModel extends BaseModel implements FastGPTContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public FastGPTModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<ChatHistoryResponse> getChatHistory() {
        boolean isDouYa = Constant.FASTGPT_KEY.equalsIgnoreCase("fastgpt-lEmLoX75QqwHeUmvwbVFkIXwJSsREJ");
        String addItems = "?appId=" + getAppId(isDouYa) + "&chatId=" + getChatId(isDouYa);
        return mRepositoryManager.obtainRetrofitService(AccountService.class).getChatHistory(Constant.FASTGPT_HISTORY_URL + addItems);
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

        return mRepositoryManager.obtainRetrofitService(AccountService.class).voiceToText(Constant.FASTGPT_TRANSCRIPTIONS_URL, multipartBody, requestBodyMap);
    }

    @Override
    public Observable<ResponseBody> textToSpeech(String text) {
        // 转换成Json
        text = "{" +
                "\"model\": \"tts-1-hd\"," +
                "\"input\": \"" + text + "\"," +
                "\"voice\": \"alloy\"," +
                "\"response_format\": \"mp3\"," +
                "\"speed\": 1" +
                "}";
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), text);
        return mRepositoryManager.obtainRetrofitService(AccountService.class).chatCreate(Constant.FASTGPT_SPEECH_URL, requestBodyJson);
    }

    @Override
    public Observable<ResponseBody> chatCreate(String message) {
        boolean isDouYa = Constant.FASTGPT_KEY.equalsIgnoreCase("fastgpt-lEmLoX75QqwHeUmvwbVFkIXwJSsREJ");
        // 转换成Json
        message = "{\"chatId\": \"" + getChatId(isDouYa) + "\", " +
                "\"messages\": [{\"role\": \"user\", \"content\":  \"" + message + "\"}] , " +
                "\"stream\" : true," + "\"detail\" : false}";
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), message);
        return mRepositoryManager.obtainRetrofitService(AccountService.class).chatCreate(Constant.FASTGPT_CHAT_URL, requestBodyJson);
    }

    /**
     * 获取 AppId
     */
    private String getAppId(boolean isDouYa) {
        return isDouYa ? "6571425b3edacb78a123cf0c" : "656fce2d993ca09b160e9ea7";
    }

    /**
     * 获取 ChatId
     */
    private String getChatId(boolean isDouYa) {
        return isDouYa ? "GrainFullDouYa" : "GrainFullApp";
    }
}