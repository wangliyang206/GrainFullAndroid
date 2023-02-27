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
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerScaleRulerComponent;
import com.zqw.mobile.grainfull.mvp.contract.ScaleRulerContract;
import com.zqw.mobile.grainfull.mvp.presenter.ScaleRulerPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.ScaleRulerView;

import butterknife.BindView;

/**
 * Description:刻度尺
 * <p>
 * Created on 2023/02/22 11:34
 *
 * @author 赤槿
 * module name is ScaleRulerActivity
 */
public class ScaleRulerActivity extends BaseActivity<ScaleRulerPresenter> implements ScaleRulerContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_scale_ruler)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.txvi_scaleruleractivity_num)
    TextView txviNum;
    @BindView(R.id.view_scaleruleractivity_ruler)
    ScaleRulerView viewScaleRuler;

    /*------------------------------------------------业务区域------------------------------------------------*/

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerScaleRulerComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_scale_ruler;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("刻度尺");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "scale_ruler");
        viewScaleRuler.setOnNumSelectListener(selectedNum -> {
            txviNum.setText(selectedNum + " cm");
            txviNum.setTextColor(viewScaleRuler.getIndicatorColor());
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