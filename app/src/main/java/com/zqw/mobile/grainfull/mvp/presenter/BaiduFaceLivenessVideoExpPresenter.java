package com.zqw.mobile.grainfull.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceLivenessVideoExpContract;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * ================================================
 * Description:百度AI - 人脸识别 - 面部活力体验视频(包含视频录制)
 * <p>
 * Created by MVPArmsTemplate on 2022/07/12 12:30
 * ================================================
 */
@ActivityScope
public class BaiduFaceLivenessVideoExpPresenter extends BasePresenter<BaiduFaceLivenessVideoExpContract.Model, BaiduFaceLivenessVideoExpContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public BaiduFaceLivenessVideoExpPresenter(BaiduFaceLivenessVideoExpContract.Model model, BaiduFaceLivenessVideoExpContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}