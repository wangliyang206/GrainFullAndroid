package com.zqw.mobile.grainfull.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.mvp.contract.FastGPTContract;
import com.zqw.mobile.grainfull.mvp.model.api.AccountService;

import io.reactivex.Observable;
import okhttp3.MediaType;
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
    public Observable<ResponseBody> chatCreate(String message) {
        // 转换成Json
        message = "{\"chatId\": \"" + "" + "\", " +
                "\"messages\": [{\"role\": \"user\", \"content\":  \"" + message + "\"}] , " +
                "\"stream\" : true," + "\"detail\" : false}";
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), message);
        return mRepositoryManager.obtainRetrofitService(AccountService.class).chatCreate(Constant.FASTGPT_CHAT_URL, requestBodyJson);
    }

    @Override
    public Observable<ResponseBody> chatImg(String message) {
        // 转换成Json
        message = "{\"prompt\": \"" + message + "\", \"n\": 1, \"size\" : \"512x512\"}";
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), message);
        return mRepositoryManager.obtainRetrofitService(AccountService.class).chatCreate(Constant.FASTGPT_IMAGE_URL, requestBodyJson);
    }
}