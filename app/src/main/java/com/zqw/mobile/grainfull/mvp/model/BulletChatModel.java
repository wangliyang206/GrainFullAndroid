package com.zqw.mobile.grainfull.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.grainfull.mvp.contract.BulletChatContract;
import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/12/29 09:59
 * ================================================
 */
@ActivityScope
public class BulletChatModel extends BaseModel implements BulletChatContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public BulletChatModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
}