package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.commonsdk.UMConfigure;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.dialog.NotPrivacyPolicyDialog;
import com.zqw.mobile.grainfull.app.dialog.PrivacyPolicyDialog;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.di.component.DaggerSplashComponent;
import com.zqw.mobile.grainfull.mvp.contract.SplashContract;
import com.zqw.mobile.grainfull.mvp.presenter.SplashPresenter;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


/**
 * ================================================
 * Description: 欢迎界面
 * ================================================
 */
@RuntimePermissions
public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View {
    @Inject
    AccountManager mAccountManager;

    /**
     * 是否使用StatusBarCompat
     *
     * @return 不使用
     */
    @Override
    public boolean useStatusBar() {
        return false;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSplashComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
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

        return R.layout.activity_splash;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (mPresenter != null) {
            mPresenter.initData();
        }
    }

    @Override
    public void approved() {
        // 已同意 - 获取权限
        SplashActivityPermissionsDispatcher.runAppWithPermissionCheck(this);
        // 友盟统计 - 同意隐私政策
        UMConfigure.submitPolicyGrantResult(getApplicationContext(), true);
    }

    @Override
    public void disagree() {
        // 未同意
        PrivacyPolicyDialog mDialog = new PrivacyPolicyDialog(this,
                isVal -> {
                    if (isVal) {
                        // 设置隐私政策
                        mAccountManager.setPrivacyPolicy(isVal);
                        approved();
                    } else {
                        // 不同意就再问一次
                        notPrivacyPolicyDialog();
                    }
                });
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    /**
     * 不同意就再问一次
     */
    private void notPrivacyPolicyDialog() {
        NotPrivacyPolicyDialog mDialog = new NotPrivacyPolicyDialog(this,
                isVal -> {
                    if (isVal) {
                        // 设置隐私政策
                        mAccountManager.setPrivacyPolicy(isVal);
                        approved();
                    } else {
                        // 关闭APP
                        AppUtils.exitApp();
                    }
                });
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 申请权限成功后的逻辑
     */
    @NeedsPermission({
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CALL_PHONE
    })
    public void runApp() {
        if (mPresenter != null) {
            mPresenter.initPresenter();
        }
    }

    @Override
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
        ArmsUtils.makeText(getApplicationContext(), message);
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

    /**
     * 屏蔽返回按钮
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return false;
    }

    /**
     * 屏蔽返回按钮
     */
    @Override
    public void onBackPressed() {

    }

    /**
     * 跳转到主界面
     */
    @Override
    public void jumbToMain() {
        ActivityUtils.startActivity(MainActivity.class);
        killMyself();
    }

    /**
     * 跳转致登录页
     */
    @Override
    public void jumbToLogin() {
        ActivityUtils.startActivity(LoginActivity.class);
        killMyself();
    }
}
