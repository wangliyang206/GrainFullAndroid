package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerUmDataStatisticsComponent;
import com.zqw.mobile.grainfull.mvp.contract.UmDataStatisticsContract;
import com.zqw.mobile.grainfull.mvp.presenter.UmDataStatisticsPresenter;

/**
 * Description:友盟 - 数据统计
 * <p>
 * Created on 2023/01/16 15:22
 *
 * @author 赤槿
 * module name is UmDataStatisticsActivity
 */
public class UmDataStatisticsActivity extends BaseActivity<UmDataStatisticsPresenter> implements UmDataStatisticsContract.View {
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerUmDataStatisticsComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_um_data_statistics;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("数据统计");

        if (mPresenter != null) {
            mPresenter.initDate();
        }
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