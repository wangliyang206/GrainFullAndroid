package com.zqw.mobile.grainfull.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.grainfull.mvp.contract.LayoutForumContract;
import com.zqw.mobile.grainfull.mvp.model.api.AccountService;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeInfoResponse;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/07/05 11:06
 * ================================================
 */
@ActivityScope
public class LayoutForumModel extends BaseModel implements LayoutForumContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public LayoutForumModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<HomeInfoResponse> queryHomePageInfo() {
        return mRepositoryManager.obtainRetrofitService(AccountService.class).queryHomePageInfo("mall/home/queryHomePageInfo");
    }
}