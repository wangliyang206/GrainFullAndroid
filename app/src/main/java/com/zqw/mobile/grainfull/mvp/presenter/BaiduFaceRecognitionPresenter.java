package com.zqw.mobile.grainfull.mvp.presenter;

import android.content.Intent;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceRecognitionContract;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduFaceDetectExpActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduFaceLivenessExpActivity;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * ================================================
 * Description:百度AI - 人脸采集
 * <p>
 * Created by MVPArmsTemplate on 2022/07/12 12:30
 * ================================================
 */
@ActivityScope
public class BaiduFaceRecognitionPresenter extends BasePresenter<BaiduFaceRecognitionContract.Model, BaiduFaceRecognitionContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public BaiduFaceRecognitionPresenter(BaiduFaceRecognitionContract.Model model, BaiduFaceRecognitionContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 跳转活体检测
     */
    public void onJump(boolean isVideo) {
        if (isVideo) {
            Intent intent = new Intent(mRootView.getActivity(), BaiduFaceLivenessExpActivity.class);
            mRootView.getActivity().startActivity(intent);
        } else {
            Intent intent = new Intent(mRootView.getActivity(), BaiduFaceDetectExpActivity.class);
            mRootView.getActivity().startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}