package com.zqw.mobile.grainfull.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceCollectionSuccessContract;

/**
 * ================================================
 * Description:百度AI - 人脸采集 - 采集成功
 * <p>
 * Created by MVPArmsTemplate on 2022/07/12 12:30
 * ================================================
 */
@ActivityScope
public class BaiduFaceCollectionSuccessPresenter extends BasePresenter<BaiduFaceCollectionSuccessContract.Model, BaiduFaceCollectionSuccessContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public BaiduFaceCollectionSuccessPresenter(BaiduFaceCollectionSuccessContract.Model model, BaiduFaceCollectionSuccessContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}