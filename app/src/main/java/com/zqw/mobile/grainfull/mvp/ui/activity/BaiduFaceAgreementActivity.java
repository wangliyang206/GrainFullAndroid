package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerBaiduFaceAgreementComponent;
import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceAgreementContract;
import com.zqw.mobile.grainfull.mvp.presenter.BaiduFaceAgreementPresenter;

import butterknife.OnClick;

/**
 * Description:百度AI - 人脸识别 -人脸采集协议
 * <p>
 * Created on 2022/07/13 16:59
 *
 * @author 赤槿
 * module name is BaiduFaceAgreementActivity
 */
public class BaiduFaceAgreementActivity extends BaseActivity<BaiduFaceAgreementPresenter> implements BaiduFaceAgreementContract.View {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBaiduFaceAgreementComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_baidu_face_agreement;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({
            R.id.agreement_return
    })
    @Override
    public void onClick(View v) {
        killMyself();
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