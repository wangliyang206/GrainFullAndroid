package com.zqw.mobile.grainfull.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.zqw.mobile.grainfull.mvp.contract.BaiduVoiceRecogContract;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/25 17:21
 * ================================================
 */
@ActivityScope
public class BaiduVoiceRecogModel extends BaseModel implements BaiduVoiceRecogContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public BaiduVoiceRecogModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
}