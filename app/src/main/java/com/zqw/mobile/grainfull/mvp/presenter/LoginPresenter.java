package com.zqw.mobile.grainfull.mvp.presenter;



import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.integration.cache.IntelligentCache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.DeviceUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.service.LocationService;
import com.zqw.mobile.grainfull.app.utils.RxUtils;
import com.zqw.mobile.grainfull.mvp.contract.LoginContract;
import com.zqw.mobile.grainfull.mvp.model.entity.AppUpdate;
import com.zqw.mobile.grainfull.mvp.model.entity.LoginResponse;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import static com.zqw.mobile.grainfull.BuildConfig.AUTO_UPDATES;
import static com.zqw.mobile.grainfull.BuildConfig.IS_DEBUG_DATA;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/16/2019 10:32
 * ================================================
 */
@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AccountManager mAccountManager;
    @Inject
    Cache<String, Object> extras;

    // 定位对象
    private LocationService locationService;

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 初始化默认值
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        locationService = ((LocationService) extras.get(IntelligentCache.getKeyOfKeep(LocationService.class.getName())));

        if (IS_DEBUG_DATA) {
            mRootView.setUsernName("15032134297");
            mRootView.setPassword("123456");
        } else {
            mRootView.setUsernName(mAccountManager.getAccount());
            mRootView.setPassword(mAccountManager.getPassword());
        }

        // APP升级更新
        if (AUTO_UPDATES && mAccountManager.getUpgrade())
            checkUpdateManager();
    }

    /**
     * 点击发送短信验证码按钮
     */
    public void btnLoginOnClick(boolean mSwitchLogin, String username, String password) {
//        测试Bugly上报
//        CrashReport.testJavaCrash();

        if (mSwitchLogin) {
            if (IS_DEBUG_DATA) {
                mRootView.jumbToLoginVerificationCode();
            } else {

            }
        } else {
            btnLogin(username, password);
        }

    }

    /**
     * 登录
     */
    private void btnLogin(String username, String password) {
        if (IS_DEBUG_DATA) {
            // 将账号密码以及常用信息保存到缓存中
            mAccountManager.saveAccountInfo(username, password, new LoginResponse("50154b09-14bd-49bf-87a7-d53ce5d29f80", "R000014", "秤砣", "18124027304"));
            // 跳转致首页
            mRootView.jumbToMain();
            // 当用户使用自有账号登录时，可以这样统计：
            MobclickAgent.onProfileSignIn("R000014");
        } else {
            mModel.btnLogin(username, password)
                    .compose(RxUtils.applySchedulers(mRootView))                                    // 切换线程
                    .subscribe(new ErrorHandleSubscriber<LoginResponse>(mErrorHandler) {
                        @Override
                        public void onNext(LoginResponse loginResponse) {
                            if (loginResponse != null) {
                                // 将账号密码以及常用信息保存到缓存中
                                mAccountManager.saveAccountInfo(username, password, loginResponse);
                                // 当用户使用自有账号登录时，可以这样统计：
                                MobclickAgent.onProfileSignIn(loginResponse.getUserId());
                            }

                            // 跳转致首页
                            mRootView.jumbToMain();
                        }
                    });
        }
    }

    /**
     * APP升级更新
     */
    private void checkUpdateManager() {
        // 提醒过了
        mAccountManager.setUpgrade(false);
        if (IS_DEBUG_DATA) {
            // 不更新
            AppUpdate appUpdate = new AppUpdate(1, "1.0.0", "小的来了", "小的来了.apk", "http://f4.market.xiaomi.com/download/AppStore/09a8d44bf3f4925837eae2b699d36ce8bcd4169c9/com.tencent.mobileqq.apk", 0, 18, "1.更新了好多东西\n1.更新了好多东西\n1.更新了好多东西");
            // 非强制更新
//            AppUpdate appUpdate = new AppUpdate(112, "1.1.2", "小的来了", "小的来了.apk", "http://f4.market.xiaomi.com/download/AppStore/09a8d44bf3f4925837eae2b699d36ce8bcd4169c9/com.tencent.mobileqq.apk", 0,18,"1.更新了好多东西\n1.更新了好多东西\n1.更新了好多东西");
            // 强制更新
//            AppUpdate appUpdate = new AppUpdate(112, "1.1.2", "小的来了", "小的来了.apk", "http://f4.market.xiaomi.com/download/AppStore/09a8d44bf3f4925837eae2b699d36ce8bcd4169c9/com.tencent.mobileqq.apk", 1,18,"1.更新了好多东西\n1.更新了好多东西\n1.更新了好多东西");
            if (haveNew(appUpdate)) {
                // 先提醒升级
                mRootView.mainAskDialog(appUpdate);
            }
        } else {
            mModel.getVersion("android")
                    .compose(RxUtils.applySchedulers(mRootView, true, true))                                            // 切换线程
                    .subscribe(new ErrorHandleSubscriber<AppUpdate>(mErrorHandler) {
                        @Override
                        public void onError(Throwable t) {
                            // 不做任何处理
                        }

                        @Override
                        public void onNext(AppUpdate au) {
                            if (haveNew(au)) {
                                // 先提醒升级
                                mRootView.mainAskDialog(au);
                            }
                        }
                    });
        }
    }

    /**
     * 版本号比较
     *
     * @return 是否需要升级
     */
    private boolean haveNew(AppUpdate appUpdate) {
        boolean haveNew = false;
        if (appUpdate == null) {
            return false;
        }

        int curVersionCode = DeviceUtils.getVersionCode(mRootView.getActivity().getApplicationContext());
        if (curVersionCode < appUpdate.getVerCode()) {
            haveNew = true;
        }
        return haveNew;
    }

    /**
     * 关闭APP时关闭定位
     */
    public void stopLocationService() {
        if (locationService != null)
            locationService.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAccountManager = null;
        this.extras = null;
        this.locationService = null;
    }
}
