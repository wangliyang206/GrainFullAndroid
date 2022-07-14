package com.zqw.mobile.grainfull.mvp.ui.activity;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.utils.ArmsUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import com.zqw.mobile.grainfull.di.component.DaggerLivenessDetectionComponent;
import com.zqw.mobile.grainfull.mvp.contract.LivenessDetectionContract;
import com.zqw.mobile.grainfull.mvp.presenter.LivenessDetectionPresenter;
import com.zqw.mobile.grainfull.R;

/**
 * Description:活体检测
 * <p>
 * Created on 2022/07/13 15:29
 *
 * @author 赤槿
 * module name is LivenessDetectionActivity
 */
public class LivenessDetectionActivity extends BaseActivity<LivenessDetectionPresenter> implements LivenessDetectionContract.View {
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLivenessDetectionComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_liveness_detection; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("LivenessDetection");

    }

    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}