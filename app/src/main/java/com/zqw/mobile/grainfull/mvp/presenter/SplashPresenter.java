package com.zqw.mobile.grainfull.mvp.presenter;

import static com.zqw.mobile.grainfull.BuildConfig.IS_DEBUG_DATA;

import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.zqw.mobile.grainfull.BuildConfig;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.RxUtils;
import com.zqw.mobile.grainfull.mvp.contract.SplashContract;
import com.zqw.mobile.grainfull.mvp.model.entity.LoginResponse;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/05/2019 13:57
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class SplashPresenter extends BasePresenter<SplashContract.Model, SplashContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AccountManager mAccountManager;

    @Inject
    public SplashPresenter(SplashContract.Model model, SplashContract.View rootView) {
        super(model, rootView);
    }

    public void initData() {
        // 是否使用隐私政策待确定，确定使用时在放开
//        mRootView.approved();
        if (mAccountManager.getPrivacyPolicy()) {
            // 已同意 - 获取权限
            mRootView.approved();
        } else {
            // 未同意
            mRootView.disagree();
        }
    }

    /**
     * 控制业务逻辑
     */
    public void initPresenter() {
        // 创建文件夹
        initFile();

        // 定时清理日志
        initLog();

        /**
         *
         * 初始化友盟统计SDK
         *
         * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
         * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
         * UMConfigure.init调用中appkey和channel参数请置为null）。
         */
        UMConfigure.init(mRootView.getActivity(),
                BuildConfig.DEBUG ? mRootView.getActivity().getString(R.string.um_app_key_debug) : mRootView.getActivity().getString(R.string.um_app_key),
                Constant.UM_CHANNEL,
                UMConfigure.DEVICE_TYPE_PHONE,
                "");
        //统计SDK是否支持采集在子进程中打点的自定义事件，默认不支持
        UMConfigure.setProcessEvent(true);//支持多进程打点

        // 页面数据采集模式
        // setPageCollectionMode接口参数说明：
        // 1. MobclickAgent.PageMode.AUTO: 建议大多数用户使用本采集模式，SDK在此模式下自动采集Activity
        // 页面访问路径，开发者不需要针对每一个Activity在onResume/onPause函数中进行手动埋点。在此模式下，
        // 开发者如需针对Fragment、CustomView等自定义页面进行页面统计，直接调用MobclickAgent.onPageStart/
        // MobclickAgent.onPageEnd手动埋点即可。此采集模式简化埋点工作，唯一缺点是在Android 4.0以下设备中
        // 统计不到Activity页面数据和各类基础指标(提示：目前Android 4.0以下设备市场占比已经极小)。

        // 2. MobclickAgent.PageMode.MANUAL：对于要求在Android 4.0以下设备中也能正常采集数据的App,可以使用
        // 本模式，开发者需要在每一个Activity的onResume函数中手动调用MobclickAgent.onResume接口，在Activity的
        // onPause函数中手动调用MobclickAgent.onPause接口。在此模式下，开发者如需针对Fragment、CustomView等
        // 自定义页面进行页面统计，直接调用MobclickAgent.onPageStart/MobclickAgent.onPageEnd手动埋点即可。

        // 如下两种LEGACY模式不建议首次集成友盟统计SDK的新用户选用。
        // 如果您是友盟统计SDK的老用户，App需要从老版本统计SDK升级到8.0.0版本统计SDK，
        // 并且：您的App之前MobclickAgent.onResume/onPause接口埋点分散在所有Activity
        // 中，逐个删除修改工作量很大且易出错。
        // 若您的App符合以上特征，可以选用如下两种LEGACY模式，否则不建议继续使用LEGACY模式。
        // 简单来说，升级SDK的老用户，如果不需要手动统计页面路径，选用LEGACY_AUTO模式。
        // 如果需要手动统计页面路径，选用LEGACY_MANUAL模式。
        // 3. MobclickAgent.PageMode.LEGACY_AUTO: 本模式适合不需要对Fragment、CustomView
        // 等自定义页面进行页面访问统计的开发者，SDK仅对App中所有Activity进行页面统计，开发者需要在
        // 每一个Activity的onResume函数中手动调用MobclickAgent.onResume接口，在Activity的
        // onPause函数中手动调用MobclickAgent.onPause接口。此模式下MobclickAgent.onPageStart
        // ,MobclickAgent.onPageEnd这两个接口无效。

        // 4. MobclickAgent.PageMode.LEGACY_MANUAL: 本模式适合需要对Fragment、CustomView
        // 等自定义页面进行手动页面统计的开发者，开发者如需针对Fragment、CustomView等
        // 自定义页面进行页面统计，直接调用MobclickAgent.onPageStart/MobclickAgent.onPageEnd
        // 手动埋点即可。开发者还需要在每一个Activity的onResume函数中手动调用MobclickAgent.onResume接口，
        // 在Activity的onPause函数中手动调用MobclickAgent.onPause接口。
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        // 初始APP升级提醒数据设置
        mAccountManager.setUpgrade(true);

        // 验证Token
        validToken();
    }


    /**
     * 验证Token
     */
    private void validToken() {
        //1、验证Token和Userid是否存在，存在表示此次并不是第一次登录；
        String userid = mAccountManager.getUserid();
        String token = mAccountManager.getToken();

        if (TextUtils.isEmpty(token) || TextUtils.isEmpty(userid)) {

            // 未登录，跳转首页
            RxUtils.startDelayed(1, mRootView, () -> mRootView.jumbToLogin());
            return;
        }

        if (IS_DEBUG_DATA) {
            // 将账号密码以及常用信息保存到缓存中
            mAccountManager.updateAccountInfo(new LoginResponse("50154b09-14bd-49bf-87a7-d53ce5d29f80", "R000014", "秤砣", "18124027304"));
            // 跳转致首页
            mRootView.jumbToMain();
        } else {
            //2、验证Token的有效性
            mModel.validToken()
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                    .subscribe(new ErrorHandleSubscriber<LoginResponse>(mErrorHandler) {
                        @Override
                        public void onError(Throwable t) {
                            // 跳转到首页
                            mRootView.jumbToLogin();
                        }

                        @Override
                        public void onNext(LoginResponse loginResponse) {
                            if (loginResponse != null) {
                                // 将账号密码以及常用信息保存到缓存中
                                mAccountManager.updateAccountInfo(loginResponse);
                                // 重新保存Token
                                mAccountManager.setToken(loginResponse.getToken());
                            }

                            // 跳转致首页
                            mRootView.jumbToMain();
                        }
                    });
        }
    }

    /**
     * 创建文件夹
     */
    private void initFile() {
        FileUtils.createOrExistsDir(Constant.LOG_PATH);                                             // 创建日志目录
        FileUtils.createOrExistsDir(Constant.APP_UPDATE_PATH);                                      // 创建升级目录
        FileUtils.createOrExistsDir(Constant.AUDIO_PATH);                                           // 设置音频缓存路径

    }

    /**
     * 定时清理日志
     * ps:正式环境下每启动10次清理一次Log日志。
     */
    private void initLog() {
        if (!BuildConfig.DEBUG) {
            int num = mAccountManager.getStartTime();
            if (num >= 10) {
                //清理日志
                ThreadUtils.getFixedPool(3).execute(() -> {
                    try {
                        FileUtils.delete(Constant.LOG_PATH + "log.txt");
                        mAccountManager.setStartTime(0);
                    } catch (Exception ignored) {
                    }
                });
            } else {
                //不到清理日期，暂时先增加APP启动次数
                mAccountManager.setStartTime(++num);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAccountManager = null;
    }
}
