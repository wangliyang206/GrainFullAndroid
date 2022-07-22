package com.zqw.mobile.grainfull.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceActivationContract;

/**
 * ================================================
 * Description:百度人脸识别激活
 * <p>
 * Created by MVPArmsTemplate on 2022/07/22 18:29
 * ================================================
 */
@ActivityScope
public class BaiduFaceActivationPresenter extends BasePresenter<BaiduFaceActivationContract.Model, BaiduFaceActivationContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public BaiduFaceActivationPresenter(BaiduFaceActivationContract.Model model, BaiduFaceActivationContract.View rootView) {
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