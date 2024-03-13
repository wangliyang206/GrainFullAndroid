package com.zqw.mobile.grainfull.mvp.ui.activity;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.utils.ArmsUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import com.zqw.mobile.grainfull.di.component.DaggerWaveEffectsComponent;
import com.zqw.mobile.grainfull.mvp.contract.WaveEffectsContract;
import com.zqw.mobile.grainfull.mvp.presenter.WaveEffectsPresenter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.ui.widget.SimpleSpiderView;
import com.zqw.mobile.grainfull.mvp.ui.widget.SimpleWaveView;
import com.zqw.mobile.grainfull.mvp.ui.widget.WaveView;
import com.zqw.mobile.grainfull.mvp.ui.widget.trendchart.SimpleHalfPieChart;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:水波纹效果
 * <p>
 * Created on 2024/03/13 13:51
 *
 * @author 赤槿
 * module name is WaveEffectsActivity
 */
public class WaveEffectsActivity extends BaseActivity<WaveEffectsPresenter> implements WaveEffectsContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.view_waveeffects_square)
    WaveView viewSquare;
    @BindView(R.id.view_waveeffects_rectangle)
    WaveView viewRectangle;
    @BindView(R.id.view_waveeffects_rectangle2)
    WaveView viewRectangle2;
    @BindView(R.id.view_waveeffects_rotundity)
    WaveView viewRotundity;

    @BindView(R.id.view_waveeffects_wave)
    SimpleWaveView mSimpleWaveView;

    /*------------------------------------------------业务区域------------------------------------------------*/


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerWaveEffectsComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_wave_effects;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("水波纹效果");

        // 代码设置相关属性
        viewSquare.setBorderWidth(2)
                .setWaveColor1(Color.RED)
                .setWaveColor2(Color.parseColor("#80ff0000"))
                .setBorderColor(Color.GREEN)
                .setTextColor(Color.BLUE)
                .setShape(WaveView.ShowShape.RECT)
                .setTextSize(36)
                .setPrecent(0.65f)
                .setTime(2);
    }

    @OnClick({
            R.id.bt_waveeffects_start,                                                              // 开始
            R.id.bt_waveeffects_stop,                                                               // 暂停
            R.id.bt_waveeffects_reset,                                                              // 重置
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_waveeffects_start:                                                         // 开始
                viewSquare.start();
                viewRectangle.start();
                viewRectangle2.start();
                viewRotundity.start();
                break;
            case R.id.bt_waveeffects_stop:                                                          // 暂停
                viewSquare.stop();
                viewRectangle.stop();
                viewRectangle2.stop();
                viewRotundity.stop();
                break;
            case R.id.bt_waveeffects_reset:                                                         // 重置
                viewSquare.reset();
                viewRectangle.reset();
                viewRectangle2.reset();
                viewRotundity.reset();
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