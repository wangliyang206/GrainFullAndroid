package com.zqw.mobile.grainfull.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.zqw.mobile.grainfull.di.component.DaggerIdentifyIdCardComponent;
import com.zqw.mobile.grainfull.mvp.contract.IdentifyIdCardContract;
import com.zqw.mobile.grainfull.mvp.presenter.IdentifyIdCardPresenter;

import com.zqw.mobile.grainfull.R;


import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:识别身份证
 * <p>
 * Created by MVPArmsTemplate on 06/24/2022 11:00
 * ================================================
 */
public class IdentifyIdCardActivity extends BaseActivity<IdentifyIdCardPresenter> implements IdentifyIdCardContract.View {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerIdentifyIdCardComponent 
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_identify_id_card;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }
	
    @Override
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
