package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerHandheldBulletScreenComponent;
import com.zqw.mobile.grainfull.mvp.contract.HandheldBulletScreenContract;
import com.zqw.mobile.grainfull.mvp.presenter.HandheldBulletScreenPresenter;

import butterknife.OnClick;

/**
 * Description:手持弹幕
 * <p>
 * Created on 2022/12/29 09:59
 *
 * @author 赤槿
 * module name is HandheldBulletScreenActivity
 */
public class HandheldBulletScreenActivity extends BaseActivity<HandheldBulletScreenPresenter> implements HandheldBulletScreenContract.View {

    @Override
    protected void onDestroy() {
        KeyboardUtils.unregisterSoftInputChangedListener(getWindow());
        super.onDestroy();

    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHandheldBulletScreenComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_handheld_bullet_screen;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("手持弹幕");

    }

    @OnClick({
            R.id.btn_handheldbulletscreen_show                                                      // 显示弹幕
    })
    @Override
    public void onClick(View v) {
        hideInput();
        switch (v.getId()) {
            case R.id.btn_handheldbulletscreen_show:                                                // 显示弹幕
                Bundle mBundle = new Bundle();
                mBundle.putString("", "");
                ActivityUtils.startActivity(mBundle, BulletChatActivity.class);
                break;
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideInput() {
        KeyboardUtils.hideSoftInput(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideInput();
        return super.onTouchEvent(event);
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