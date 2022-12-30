package com.zqw.mobile.grainfull.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.utils.ArmsUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zqw.mobile.grainfull.di.component.DaggerBulletChatComponent;
import com.zqw.mobile.grainfull.mvp.contract.BulletChatContract;
import com.zqw.mobile.grainfull.mvp.presenter.BulletChatPresenter;
import com.zqw.mobile.grainfull.R;

/**
 * Description:展示弹幕
 * <p>
 * Created on 2022/12/30 11:18
 *
 * @author 赤槿
 * module name is BulletChatActivity
 */
public class BulletChatActivity extends BaseActivity<BulletChatPresenter> implements BulletChatContract.View {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBulletChatComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_bullet_chat;
    }

    @Override
    protected void setForm() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        } else {
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        getWindow().setAttributes(lp);
        View decorView = getWindow().getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility();
        // 隐藏导航栏 | 隐藏状态栏
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        systemUiVisibility |= flags;
        getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

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