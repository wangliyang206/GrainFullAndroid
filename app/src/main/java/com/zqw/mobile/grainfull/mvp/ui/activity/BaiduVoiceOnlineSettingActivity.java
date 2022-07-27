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

import com.zqw.mobile.grainfull.di.component.DaggerBaiduVoiceOnlineSettingComponent;
import com.zqw.mobile.grainfull.mvp.contract.BaiduVoiceOnlineSettingContract;
import com.zqw.mobile.grainfull.mvp.presenter.BaiduVoiceOnlineSettingPresenter;
import com.zqw.mobile.grainfull.R;

/**
 * Description:百度AI - 短语音识别 - 设置
 * <p>
 * Created on 2022/07/26 18:34
 *
 * @author 赤槿
 * module name is BaiduVoiceOnlineSettingActivity
 */
public class BaiduVoiceOnlineSettingActivity extends BaseActivity<BaiduVoiceOnlineSettingPresenter> implements BaiduVoiceOnlineSettingContract.View {
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBaiduVoiceOnlineSettingComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_baidu_voice_online_setting;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("语音识别设置");

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