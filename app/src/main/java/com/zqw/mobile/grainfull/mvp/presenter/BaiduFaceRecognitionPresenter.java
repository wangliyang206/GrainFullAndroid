package com.zqw.mobile.grainfull.mvp.presenter;

import android.content.Intent;
import android.os.Bundle;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceRecognitionContract;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduFaceLivenessExpActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduFaceLivenessVideoExpActivity;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * ================================================
 * Description:百度AI - 人脸识别
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
    public void onJump(boolean isVideo, boolean frameExtraction) {
        if (isVideo) {
            Intent intent = new Intent(mRootView.getActivity(), BaiduFaceLivenessVideoExpActivity.class);
            Bundle mBundle = new Bundle();
//            mBundle.putString("mAccountsReceivable", mAccountsReceivable);
//            mBundle.putString("appSeq", appSeq);
//            mBundle.putString("code", code);
//            mBundle.putString("session_id", session_id);
//            mBundle.putString("pId", pId);
            intent.putExtras(mBundle);
            mRootView.getActivity().startActivity(intent);
        } else {
            Intent intent = new Intent(mRootView.getActivity(), BaiduFaceLivenessExpActivity.class);
            intent.putExtra("frame_extraction", frameExtraction);
            Bundle mBundle = new Bundle();
//            mBundle.putString("mAccountsReceivable", mAccountsReceivable);
//            mBundle.putString("appSeq", appSeq);
//            mBundle.putString("code", code);
//            mBundle.putString("session_id", session_id);
//            mBundle.putString("pId", pId);
            intent.putExtras(mBundle);
            mRootView.getActivity().startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}