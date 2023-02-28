package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerTemperatureComponent;
import com.zqw.mobile.grainfull.mvp.contract.TemperatureContract;
import com.zqw.mobile.grainfull.mvp.presenter.TemperaturePresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.particle.ProgressLayout;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:升温效果
 * <p>
 * Created on 2023/02/28 14:10
 *
 * @author 赤槿
 * module name is TemperatureActivity
 */
public class TemperatureActivity extends BaseActivity<TemperaturePresenter> implements TemperatureContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_temperature)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.view_temperatureactivity_progress)
    ProgressLayout viewProgress;

    /*------------------------------------------------业务区域------------------------------------------------*/
    // 当前温度
    private float temperature;
    // 随机数
    private final Random mRandom = new Random();
    // 目标温度
    private final int targetTemperature = 240;

    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 101:
                    temperature = temperature + 1 + mRandom.nextInt(30);
                    //temperature++;
                    if (temperature >= targetTemperature) {
                        temperature = targetTemperature;
                        mHandler.removeCallbacksAndMessages(null);
                    } else {
                        mHandler.sendEmptyMessageDelayed(101, 2000);
                    }
                    if (viewProgress != null) {
                        viewProgress.setTemperature(temperature, targetTemperature);
                    }
                    break;
                default:
            }
            return false;
        }
    });

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTemperatureComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_temperature;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("升温效果");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "temperature");
    }

    @OnClick({
            R.id.btn_temperatureactivity_start,                                                     // 升温效果
            R.id.btn_temperatureactivity_clean,                                                     // 清洁模式
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_temperatureactivity_start:                                                // 升温效果
                // 预热
                temperature = 0;
                mHandler.removeCallbacksAndMessages(null);
                mHandler.sendEmptyMessageDelayed(101, 0);
                break;
            case R.id.btn_temperatureactivity_clean:                                                // 清洁模式
                // 清洁模式
                mHandler.removeCallbacksAndMessages(null);
                viewProgress.setCleanMode(30, () -> showMessage("清洁完成"));
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