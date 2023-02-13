package com.zqw.mobile.grainfull.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.integration.cache.IntelligentCache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.DeviceUtils;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.service.LocationService;
import com.zqw.mobile.grainfull.app.utils.RxUtils;
import com.zqw.mobile.grainfull.mvp.contract.MainContract;
import com.zqw.mobile.grainfull.mvp.model.entity.AppUpdate;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import static com.zqw.mobile.grainfull.BuildConfig.AUTO_UPDATES;
import static com.zqw.mobile.grainfull.BuildConfig.IS_DEBUG_DATA;
import static com.zqw.mobile.grainfull.app.service.LocationService.SPAN_NORMAL;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/31/2019 10:34
 * ================================================
 */
@ActivityScope
public class MainPresenter extends BasePresenter<MainContract.Model, MainContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AccountManager mAccountManager;
    @Inject
    Cache<String, Object> extras;

    // 定位对象
    private LocationService locationService;

    @Inject
    public MainPresenter(MainContract.Model model, MainContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 初始化
     */
    public void initPresenter() {
        // 拿到缓存中的定位对象，然后开启定位
        locationService = ((LocationService) extras.get(IntelligentCache.getKeyOfKeep(LocationService.class.getName())));
        if (locationService != null) {
            locationService.setLocationOption(locationService.setScanSpan(SPAN_NORMAL));
            // 开启定位
            locationService.start();
        }

        // APP升级更新
        if (AUTO_UPDATES && mAccountManager.getUpgrade())
            checkUpdateManager();
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

    @Override
    public void onDestroy() {
        // 关闭定位
        if (locationService != null)
            this.locationService.stop();

        super.onDestroy();
        this.mErrorHandler = null;
        this.mAccountManager = null;
        this.locationService = null;
        this.extras = null;
    }
}
