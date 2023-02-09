package com.zqw.mobile.grainfull.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.zqw.mobile.grainfull.mvp.contract.PictureMosaicContract;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/09 15:00
 * ================================================
 */
@ActivityScope
public class PictureMosaicModel extends BaseModel implements PictureMosaicContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public PictureMosaicModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
}