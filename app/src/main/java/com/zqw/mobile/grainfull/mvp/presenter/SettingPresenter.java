package com.zqw.mobile.grainfull.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.DeviceUtils;
import com.zqw.mobile.grainfull.app.utils.RxUtils;
import com.zqw.mobile.grainfull.mvp.contract.SettingContract;
import com.zqw.mobile.grainfull.mvp.model.entity.AppUpdate;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import static com.zqw.mobile.grainfull.BuildConfig.IS_DEBUG_DATA;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/28/2020 13:17
 * ================================================
 */
@ActivityScope
public class SettingPresenter extends BasePresenter<SettingContract.Model, SettingContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public SettingPresenter(SettingContract.Model model, SettingContract.View rootView) {
        super(model, rootView);
    }

    /**
     * APP升级更新
     */
    public void checkUpdateManager() {
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
            } else {
                mRootView.showLatestDialog();
            }
        } else {
            mModel.getVersion("android")
                    .compose(RxUtils.applySchedulers(mRootView))                                    // 切换线程
                    .subscribe(new ErrorHandleSubscriber<AppUpdate>(mErrorHandler) {
                        @Override
                        public void onError(Throwable t) {
                            // 不做任何处理
//                            mRootView.showMessage(t.getMessage());
                        }

                        @Override
                        public void onNext(AppUpdate au) {
                            if (haveNew(au)) {
                                // 先提醒升级
                                mRootView.mainAskDialog(au);
                            } else {
                                mRootView.showLatestDialog();
                            }
                        }
                    });
        }
    }

    /**
     * 版本号比较
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
        super.onDestroy();
        this.mErrorHandler = null;
    }
}
