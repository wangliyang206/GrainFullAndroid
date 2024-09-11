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
import com.zqw.mobile.grainfull.di.component.DaggerRandomlyDrawComponent;
import com.zqw.mobile.grainfull.mvp.contract.RandomlyDrawContract;
import com.zqw.mobile.grainfull.mvp.presenter.RandomlyDrawPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.ColorTouchView;

import butterknife.BindView;

/**
 * Description:随机抽
 * <p>
 * Created on 2024/09/10 17:04
 *
 * @author 赤槿
 * module name is RandomlyDrawActivity
 */
public class RandomlyDrawActivity extends BaseActivity<RandomlyDrawPresenter> implements RandomlyDrawContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_randomly_draw)
    LinearLayout contentLayout;                                                                     // 总布局

    @BindView(R.id.view_touch_container_layout)
    ColorTouchView multiTouchView;
    /*------------------------------------------------业务区域------------------------------------------------*/

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerRandomlyDrawComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_randomly_draw;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("随机抽");

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