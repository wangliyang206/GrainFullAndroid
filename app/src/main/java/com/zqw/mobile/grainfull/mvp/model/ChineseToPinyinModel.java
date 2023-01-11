package com.zqw.mobile.grainfull.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.zqw.mobile.grainfull.mvp.contract.ChineseToPinyinContract;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/10 16:38
 * ================================================
 */
@ActivityScope
public class ChineseToPinyinModel extends BaseModel implements ChineseToPinyinContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public ChineseToPinyinModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
}