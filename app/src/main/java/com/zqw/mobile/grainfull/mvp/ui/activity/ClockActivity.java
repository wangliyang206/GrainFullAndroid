package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.di.component.DaggerClockComponent;
import com.zqw.mobile.grainfull.mvp.contract.ClockContract;
import com.zqw.mobile.grainfull.mvp.presenter.ClockPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.ClockView;

import butterknife.BindView;

/**
 * Description:时钟
 * <p>
 * Created on 2023/02/21 16:30
 *
 * @author 赤槿
 * module name is ClockActivity
 */
public class ClockActivity extends BaseActivity<ClockPresenter> implements ClockContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_clock)
    LinearLayout contentLayout;                                                                     // 总布局

    @BindView(R.id.view_clockactivity_text)
    TextView txviTips;

    @BindView(R.id.view_clockactivity_clock)
    ClockView viewClock;

    /*------------------------------------------------业务区域------------------------------------------------*/

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerClockComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_clock;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("时钟");

        viewClock.setOnClockMonitorListener((hour, minute, second) -> {
            txviTips.setText(CommonUtils.format0Right(String.valueOf(hour)) + ":" + CommonUtils.format0Right(String.valueOf(minute)) + ":" + CommonUtils.format0Right(String.valueOf(second)));
        });
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