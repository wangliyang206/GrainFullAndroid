package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerRadarEffectComponent;
import com.zqw.mobile.grainfull.mvp.contract.RadarEffectContract;
import com.zqw.mobile.grainfull.mvp.presenter.RadarEffectPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.RadarView;
import com.zqw.mobile.grainfull.mvp.ui.widget.RotatingCircleView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:
 * <p>
 * Created on 2024/02/28 14:33
 *
 * @author 赤槿
 * module name is RadarEffectActivity
 */
public class RadarEffectActivity extends BaseActivity<RadarEffectPresenter> implements RadarEffectContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.view_radareffect_radar)
    RotatingCircleView mRotatingCircleView;                                                                     // 雷达效果
    @BindView(R.id.btn_radareffect_start)
    Button btnStart;

    @BindView(R.id.view_radareffect_radarView)
    RadarView mRadarView;

    /*------------------------------------------------业务区域------------------------------------------------*/

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerRadarEffectComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_radar_effect;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("雷达效果");

        mRadarView.setForwardRotation(true);
    }

    @OnClick({
            R.id.btn_radareffect_start,                                                             // 开始扫描 or 停止扫描
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_radareffect_start:                                                        // 开始扫描 or 停止扫描
                if (mRotatingCircleView.isScanning()) {
                    mRotatingCircleView.stop();
                    btnStart.setText("开始扫描");
                } else {
                    mRotatingCircleView.start();
                    btnStart.setText("停止扫描");
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