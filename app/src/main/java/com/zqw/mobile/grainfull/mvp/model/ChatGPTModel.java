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

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MediaType;
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
}