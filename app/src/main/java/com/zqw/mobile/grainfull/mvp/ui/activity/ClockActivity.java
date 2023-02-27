package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.di.component.DaggerClockComponent;
import com.zqw.mobile.grainfull.mvp.contract.ClockContract;
import com.zqw.mobile.grainfull.mvp.presenter.ClockPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.ClockView;
import com.zqw.mobile.grainfull.mvp.ui.widget.NormalClockView;

import butterknife.BindView;
import butterknife.OnClick;

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
    RelativeLayout contentLayout;                                                                   // 总布局
    @BindView(R.id.imvi_clockactivity_switch)
    ImageView imviSwitch;                                                                           // 切换按钮

    @BindView(R.id.rela_clockactivity_clock)
    RelativeLayout relaGreen;                                                                       // 绿色总布局
    @BindView(R.id.txvi_clockactivity_clock_text)
    TextView txviClockTips;                                                                         // 时间提示
    @BindView(R.id.view_clockactivity_clock)
    ClockView viewClock;                                                                            // 绿色表盘

    @BindView(R.id.rela_clockactivity_dial)
    RelativeLayout relaWhite;                                                                       // 白色总布局
    @BindView(R.id.txvi_clockactivity_dial_text)
    TextView txviDialTips;                                                                          // 时间提示
    @BindView(R.id.view_clockactivity_dial)
    NormalClockView viewNormalClockView;

    /*------------------------------------------------业务区域------------------------------------------------*/
    // 当前是否切换到“全”时钟，true显示白底，false显示绿底
    private boolean isClockAll = true;

    @Override
    protected void onDestroy() {
        if (viewNormalClockView != null) {
            viewNormalClockView.stopDrawing();
        }
        super.onDestroy();
    }

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

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "clock");
        // 增加监听
        viewClock.setOnClockMonitorListener((isNoon, hour, minute, second) -> {
            if (isNoon && hour == 0) {
                // PM是下午时间 12点到23点
                hour = 12;
            }
            // AM是上午时间 24点到11点

            if (txviClockTips != null)
                txviClockTips.setText(CommonUtils.format0Right(String.valueOf(hour)) + ":" + CommonUtils.format0Right(String.valueOf(minute)) + ":" + CommonUtils.format0Right(String.valueOf(second)));
        });

        viewNormalClockView.setOnClockMonitorListener((isNoon, hour, minute, second) -> {
            if (isNoon && hour == 0) {
                // PM是下午时间 12点到23点
                hour = 12;
            }
            // AM是上午时间 24点到11点
            if (txviDialTips != null)
                txviDialTips.setText(CommonUtils.format0Right(String.valueOf(hour)) + ":" + CommonUtils.format0Right(String.valueOf(minute)) + ":" + CommonUtils.format0Right(String.valueOf(second)));
        });

        // 开始绘制
        contentLayout.post(() -> {
            viewNormalClockView.startRun();
        });

    }

    @OnClick({
            R.id.imvi_clockactivity_switch,                                                         // 切换视图
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvi_clockactivity_switch:                                                    // 切换试图
                isClockAll = !isClockAll;

                if (isClockAll) {
                    // 显示白色
                    relaGreen.setVisibility(View.GONE);
                    relaWhite.setVisibility(View.VISIBLE);
                    imviSwitch.setImageResource(R.mipmap.icon_switch_front);

                } else {
                    // 显示绿色
                    relaGreen.setVisibility(View.VISIBLE);
                    relaWhite.setVisibility(View.GONE);
                    imviSwitch.setImageResource(R.mipmap.icon_switch_after);

                }
                break;
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