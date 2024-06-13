package com.zqw.mobile.grainfull.mvp.ui.activity;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.utils.ArmsUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.di.component.DaggerLocalVerificationCodeComponent;
import com.zqw.mobile.grainfull.mvp.contract.LocalVerificationCodeContract;
import com.zqw.mobile.grainfull.mvp.presenter.LocalVerificationCodePresenter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.ui.widget.CheckView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description: 本地验证码
 * <p>
 * Created on 2024/03/07 15:35
 *
 * @author 赤槿
 * module name is LocalVerificationCodeActivity
 */
public class LocalVerificationCodeActivity extends BaseActivity<LocalVerificationCodePresenter> implements LocalVerificationCodeContract.View {
    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.view_localverificationcode_checkView)
    CheckView viewCheckView;

    /*------------------------------------------业务信息------------------------------------------*/

    /**
     * 将状态栏改为浅色、深色模式(状态栏 icon 和字体，false = 浅色，true = 深色)
     */
    public boolean useLightStatusBar() {
        return false;
    }

    /**
     * 根据主题使用不同的颜色。
     * 如果想要纯透明，则需要重写此方法，返回值为 -1 即可。
     */
    public int useStatusBarColor() {
        return getResources().getColor(R.color.colorPrimary);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLocalVerificationCodeComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_local_verification_code;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("本地验证码");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "local_verification_code_open");
    }

    @OnClick({
            R.id.btn_localverificationcode_refresh                                                  // 刷新
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_localverificationcode_refresh:                                            // 刷新
                viewCheckView.invaliChenkCode();
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