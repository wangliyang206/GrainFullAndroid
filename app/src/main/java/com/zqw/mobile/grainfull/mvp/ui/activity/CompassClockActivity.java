package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerCompassClockComponent;
import com.zqw.mobile.grainfull.mvp.contract.CompassClockContract;
import com.zqw.mobile.grainfull.mvp.presenter.CompassClockPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.compassclock.TimeDiskView;

import butterknife.BindView;

/**
 * Description: 罗盘时钟
 * <p>
 * Created on 2023/02/22 09:48
 *
 * @author 赤槿
 * module name is CompassClockActivity
 */
public class CompassClockActivity extends BaseActivity<CompassClockPresenter> implements CompassClockContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_compass_clock)
    LinearLayout contentLayout;                                                                     // 总布局

    @BindView(R.id.view_compassclock_clock)
    TimeDiskView viewClock;

    /*------------------------------------------------业务区域------------------------------------------------*/

    @Override
    protected void onDestroy() {
        viewClock.onDestroy();
        super.onDestroy();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCompassClockComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_compass_clock;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("罗盘时钟");

        // 开始计时
        contentLayout.post(() -> viewClock.start());

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