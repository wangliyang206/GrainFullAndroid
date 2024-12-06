package com.zqw.mobile.grainfull.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.zqw.mobile.grainfull.mvp.contract.HmsPhotoFilterContract;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/12/05 15:44
 * ================================================
 */
@ActivityScope
public class HmsPhotoFilterModel extends BaseModel implements HmsPhotoFilterContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public HmsPhotoFilterModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
}