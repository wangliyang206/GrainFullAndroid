package com.zqw.mobile.grainfull.mvp.presenter;

import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.zqw.mobile.grainfull.BuildConfig;
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

import static com.zqw.mobile.grainfull.BuildConfig.IS_DEBUG_DATA;


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

    /**
     * 控制业务逻辑
     */
    public void initPresenter() {
        // 创建文件夹
        initFile();

        // 定时清理日志
        initLog();

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
