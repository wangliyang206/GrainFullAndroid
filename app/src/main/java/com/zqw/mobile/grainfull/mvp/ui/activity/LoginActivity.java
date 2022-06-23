package com.zqw.mobile.grainfull.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.qiangxi.checkupdatelibrary.CheckUpdateOption;
import com.qiangxi.checkupdatelibrary.Q;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.dialog.CommTipsDialog;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.EventBusTags;
import com.zqw.mobile.grainfull.di.component.DaggerLoginComponent;
import com.zqw.mobile.grainfull.mvp.contract.LoginContract;
import com.zqw.mobile.grainfull.mvp.model.entity.AppUpdate;
import com.zqw.mobile.grainfull.mvp.model.entity.MainEvent;
import com.zqw.mobile.grainfull.mvp.presenter.LoginPresenter;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description: 用户登录
 * <p>
 * Created by MVPArmsTemplate on 09/16/2019 10:32
 * ================================================
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    /*--------------------------------控件信息--------------------------------*/
    @BindView(R.id.input_login_mobile)
    TextInputLayout mTextInputMobile;                                                               // EditText的容器用于手机号

    @BindView(R.id.edit_login_mobile)
    TextInputEditText mEditTextMobile;                                                              // 账号

    @BindView(R.id.input_login_password)
    TextInputLayout mTextInputPassword;                                                             // EditText的容器用于密码

    @BindView(R.id.edit_login_password)
    TextInputEditText mEditTextPassword;                                                            // 密码

    @BindView(R.id.txvi_login_switchlogin)
    TextView mTxviSwitchLogin;                                                                      // 切换登录方式

    @BindView(R.id.btn_login)
    Button mBtnLogin;                                                                               // 登录

    @BindString(R.string.common_exit_app_tips)
    String commonTips;
    /*--------------------------------业务信息--------------------------------*/

    // 用来销毁 手机号 RxBinding
    Disposable disposMobile;
    // 用来销毁 密码 RxBinding
    Disposable disposPassword;

    // 登录对话框
    private MaterialDialog mDialog;
    // 登录方式：true 短信验证码、false 账号密码登录
    private boolean mSwitchLogin = false;
    // 是否退出APP
    private boolean isExitAPP = false;

    @Override
    protected void onDestroy() {
        KeyboardUtils.unregisterSoftInputChangedListener(this);
        if (mDialog != null) {
            this.mDialog.dismiss();
        }

        if (isExitAPP) {
            if (mPresenter != null) {
                mPresenter.stopLocationService();
            }
        }
        super.onDestroy();
        this.mDialog = null;

        this.mTextInputMobile = null;
        this.mEditTextMobile = null;
        this.mTextInputPassword = null;
        this.mEditTextPassword = null;
        this.mTxviSwitchLogin = null;
        this.mBtnLogin = null;

        this.disposMobile = null;
        this.disposPassword = null;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 初始化Loading对话框
        mDialog = new MaterialDialog.Builder(this).content(R.string.loginactivity_login_processtips).progress(true, 0).build();

        // 添加监听事件
        disposMobile = RxTextView.textChanges(mEditTextMobile)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    if (Objects.requireNonNull(mEditTextMobile.getText()).length() == 0) {
                        mTextInputMobile.setErrorEnabled(false);
                    } else {
                        // 如果之前按下登录，已验证过并且有错误提示，则在输入的时候需要先去掉错误
                        mTextInputMobile.setErrorEnabled(false);
                    }
                });

        disposPassword = RxTextView.textChanges(mEditTextPassword)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    int num = Objects.requireNonNull(mEditTextPassword.getText()).length();
                    if (num == 0) {
                        mTextInputPassword.setErrorEnabled(false);
                    } else {
                        // 如果之前按下登录，已验证过并且有错误提示，则在输入的时候需要先去掉错误
                        mTextInputPassword.setErrorEnabled(false);

                        if (num < 6 || num > 20) {
                            mTextInputPassword.setError(getString(R.string.loginactivity_login_pwderror));
                            mTextInputPassword.setErrorEnabled(true);
                        }
                    }
                });
    }

    @OnClick({
            R.id.imvi_login_close,                                                                  // 返回
            R.id.txvi_login_switchlogin,                                                            // 切换登录方式：短信验证码登录、账号密码登录
            R.id.txvi_login_forgotpassword,                                                         // 忘记密码
            R.id.btn_login                                                                          // 登录按钮
    })
    @Override
    public void onClick(View v) {
        hideInput();
        switch (v.getId()) {
            case R.id.imvi_login_close:                                                             // 返回
                onBackPressed();
                break;
            case R.id.txvi_login_switchlogin:                                                       // 切换登录方式：短信验证码登录、账号密码登录
                switchLogin();
                break;
            case R.id.txvi_login_forgotpassword:                                                    // 忘记密码

                break;
            case R.id.btn_login:                                                                    // 登录按钮
                if (checkInput()) {
                    String username = Objects.requireNonNull(mEditTextMobile.getText()).toString().trim();
                    String password = Objects.requireNonNull(mEditTextPassword.getText()).toString().trim();
                    if (mPresenter != null) {
                        mPresenter.btnLoginOnClick(mSwitchLogin, username, password);
                    }
                }
                break;
        }
    }

    /**
     * 切换登录方式
     */
    private void switchLogin() {
        if (mSwitchLogin) {                                                                         // 账号密码登录
            mSwitchLogin = false;
            // 提示可以切换短信验证码
            mTxviSwitchLogin.setText(R.string.loginactitivy_switch_sms);
            mTextInputPassword.setVisibility(View.VISIBLE);
            mBtnLogin.setText("登录");
        } else {                                                                                    // 短信验证码
            mSwitchLogin = true;
            // 提示可以切换账号密码登录
            mTxviSwitchLogin.setText(R.string.loginactitivy_switch_account);
            mTextInputPassword.setVisibility(View.GONE);
            mBtnLogin.setText("获取验证码");
        }
    }

    /**
     * 用户输入有效性验证
     *
     * @return 校验是否通过
     */
    private boolean checkInput() {
        // 用户名和密码不能为空，为空时返回false同时给出提示。
        String username = Objects.requireNonNull(mEditTextMobile.getText()).toString().trim();
        if ("".equals(username)) {
            mTextInputMobile.setError(getString(R.string.loginactivity_login_usercodenotnull));
            mTextInputMobile.setErrorEnabled(true);
            return false;
        }

        if (mTextInputMobile.isErrorEnabled()) {
            return false;
        }

        if (!mSwitchLogin) {
            // 账号密码登录
            String password = Objects.requireNonNull(mEditTextPassword.getText()).toString().trim();
            if ("".equals(password)) {
                mTextInputPassword.setError(getString(R.string.loginactivity_login_passwordnotnull));
                mTextInputPassword.setErrorEnabled(true);
                return false;
            }

            return !mTextInputPassword.isErrorEnabled();
        }

        return true;
    }

    @Override
    public void setUsernName(String usernName) {
        mEditTextMobile.setText(usernName);
        if (!TextUtils.isEmpty(usernName)) {
            // 设置光标
            mEditTextMobile.setSelection(usernName.length());
        }

    }

    @Override
    public void setPassword(String password) {
        mEditTextPassword.setText(password);
        if (!TextUtils.isEmpty(password) && !mSwitchLogin) {
            // 设置光标
            mEditTextPassword.setSelection(password.length());
        }
    }

    /**
     * 监听返回键
     */
    @Override
    public void onBackPressed() {
        CommTipsDialog phoneDialog = new CommTipsDialog(this, "温馨提示", commonTips, isVal -> {
            if (isVal) {
                isExitAPP = true;
                AppUtils.exitApp();
            }
        });
        phoneDialog.show();

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

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {
        if (mDialog != null)
            mDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mDialog != null)
            mDialog.hide();
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

    /**
     * 跳转至登录-验证码页
     */
    @Override
    public void jumbToLoginVerificationCode() {

    }

    /**
     * 跳转至首页
     */
    @Override
    public void jumbToMain() {
        ActivityUtils.startActivity(MainActivity.class);
        killMyself();
    }

    /**
     * 发现新版本提示是否更新
     */
    @Override
    public void mainAskDialog(AppUpdate info) {
        Q.show(this, new CheckUpdateOption.Builder()
                .setAppName(info.getName())
                .setFileName("/" + info.getFileName())
                .setFilePath(Constant.APP_UPDATE_PATH)
//                .setImageUrl("http://imgsrc.baidu.com/imgad/pic/item/6c224f4a20a446233d216c4f9322720e0cf3d730.jpg")
                .setImageResId(R.mipmap.icon_upgrade_logo)
                .setIsForceUpdate(info.getForce() == 1)
                .setNewAppSize(info.getNewAppSize())
                .setNewAppUpdateDesc(info.getNewAppUpdateDesc())
                .setNewAppUrl(info.getFilePath())
                .setNewAppVersionName(info.getVerName())
                .setNotificationSuccessContent("下载成功，点击安装")
                .setNotificationFailureContent("下载失败，点击重新下载")
                .setNotificationIconResId(R.mipmap.ic_launcher)
                .setNotificationTitle(getString(R.string.app_name))
                .build(), (view, imageUrl) -> {
            // 下载图片
//            view.setScaleType(ImageView.ScaleType.FIT_XY);
//            mImageLoader.loadImage(getActivity(),
//                    ImageConfigImpl
//                            .builder()
//                            .url(imageUrl)
//                            .imageView(view)
//                            .build());
        });
    }

    /**
     * 登录成功    回调
     */
    @Subscriber(tag = EventBusTags.HOME_TAG, mode = ThreadMode.POST)
    private void eventBusEvent(MainEvent mainEvent) {
        if (mainEvent.getCode() == EventBusTags.LOGIN_SUCC_TAG)
            killMyself();
    }

    /**
     * 忘记密码    回调
     */
    @Subscriber(tag = "ForgotPassword_tag", mode = ThreadMode.POST)
    private void updateUser(String account) {
        setUsernName(account);

        mEditTextPassword.setText("");
        mEditTextPassword.setFocusable(true);
        mEditTextPassword.setFocusableInTouchMode(true);
        mEditTextPassword.requestFocus();
    }
}
