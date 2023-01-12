package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.VibrateUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.white.progressview.CircleProgressView;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.dialog.CommTipsDialog;
import com.zqw.mobile.grainfull.app.dialog.PopupMetalDetector;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.app.utils.LineCharts;
import com.zqw.mobile.grainfull.di.component.DaggerMetalDetectorComponent;
import com.zqw.mobile.grainfull.mvp.contract.MetalDetectorContract;
import com.zqw.mobile.grainfull.mvp.presenter.MetalDetectorPresenter;

import java.math.BigDecimal;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Description:金属探测器
 * <p>
 * Created on 2023/01/11 16:38
 *
 * @author 赤槿
 * module name is MetalDetectorActivity
 */
public class MetalDetectorActivity extends BaseActivity<MetalDetectorPresenter> implements MetalDetectorContract.View, SensorEventListener {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_metal_detector)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.cpvi_metaldetector_progress)
    CircleProgressView progressView;                                                                // 进度条
    @BindView(R.id.txvi_metaldetector_tips)
    TextView metalState;                                                                            // 结果提示

    @BindView(R.id.txvi_metaldetector_strength)
    TextView txviStrength;                                                                          // 感应强度
    @BindView(R.id.txvi_metaldetector_x)
    TextView txviXz;                                                                                // x轴
    @BindView(R.id.txvi_metaldetector_y)
    TextView txviYz;                                                                                // y轴
    @BindView(R.id.txvi_metaldetector_z)
    TextView txviZz;                                                                                // z轴

    @BindView(R.id.lcvi_metaldetector_chart)
    LineChartView lcviChart;                                                                        // 图表
    /*------------------------------------------------业务区域------------------------------------------------*/
    @Inject
    AccountManager mAccountManager;

    // 传感器管理器
    private SensorManager sensorManager;
    // 折线图
    private LineCharts lineCharts = new LineCharts();

    // 设置界面
    private PopupMetalDetector mPopup;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.lineCharts = null;
        this.sensorManager = null;
        this.mAccountManager = null;
        this.mPopup = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 实例化传感器管理
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //注册磁场传感器监听器
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMetalDetectorComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_metal_detector;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("金属探测器");

        // 初始化
        lineCharts.initView(lcviChart);
        CommTipsDialog mDialog = new CommTipsDialog(this, "OOOOOOOps!", "你的设备没有霍尔传感器！", true, isVal -> {
            // 不需要操作什么
        });

        // 判断设备是否支持霍尔传感器
        if (!CommonUtils.isMagneticSensorAvailable(getApplicationContext())) {
            mDialog.show();
        }

        mPopup = new PopupMetalDetector(getApplicationContext(), (isSucc, val) -> {
            if (isSucc) {
                mAccountManager.setAlarmLimit(val);
            }
        });
    }

    @OnClick({
            R.id.txvi_metaldetector_settings,                                                       // 设置
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txvi_metaldetector_settings:                                                  // 设置
                if (mPopup != null) {
                    String str = mAccountManager.getAlarmLimit();
                    mPopup.setContent(TextUtils.isEmpty(str) ? "80" : str);
                    mPopup.showAtLocation(contentLayout, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Double rawTotal;//未处理的数据
        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // 保持屏幕常亮
            if (mAccountManager.getKeepWake()) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
            // 分别计算三轴磁感应强度
            float X_lateral = sensorEvent.values[0];
            float Y_lateral = sensorEvent.values[1];
            float Z_lateral = sensorEvent.values[2];
            //Log.d(TAG,X_lateral + "");
            // 计算出总磁感应强度
            rawTotal = Math.sqrt(X_lateral * X_lateral + Y_lateral * Y_lateral + Z_lateral * Z_lateral);
            // 初始化BigDecimal类
            BigDecimal total = new BigDecimal(rawTotal);
            double res = total.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            // 实时绘图
            lineCharts.makeCharts(lcviChart, (float) res);
            txviXz.setText(X_lateral + " μT");
            txviYz.setText(Y_lateral + " μT");
            txviZz.setText(Z_lateral + " μT");
            txviStrength.setText(res + " μT");
            String alarmLimStr = mAccountManager.getAlarmLimit();
            // 防止用户非法输入导致软件崩溃影响体验
            // 报警限值
            double alarmLim;
            if (!alarmLimStr.isEmpty()) {
                alarmLim = Double.valueOf(alarmLimStr);
            } else {
                alarmLim = 80;
            }
            if (res < alarmLim) {
                // 设置文字颜色为黑色
                metalState.setTextColor(Color.rgb(0, 0, 0));
                metalState.setText("未探测到金属");
                // 计算进度
                int progress = (int) ((res / alarmLim) * 100);
                progressView.setReachBarColor(Color.rgb(30, 144, 255));
                progressView.setTextColor(Color.rgb(30, 144, 255));
                // 进度条
                progressView.setProgress(progress);
            } else {
                // 红色
                metalState.setTextColor(Color.rgb(255, 0, 0));
                metalState.setText("探测到金属!");
                progressView.setReachBarColor(Color.rgb(255, 0, 0));
                progressView.setTextColor(Color.rgb(255, 0, 0));
                // 进度条满
                progressView.setProgress(100);
                // 震动，100毫秒
                VibrateUtils.vibrate(100);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}