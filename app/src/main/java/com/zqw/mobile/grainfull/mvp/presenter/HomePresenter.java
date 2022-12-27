package com.zqw.mobile.grainfull.mvp.presenter;

import android.app.Application;
import android.text.TextUtils;

import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.utils.RxUtils;
import com.zqw.mobile.grainfull.mvp.contract.HomeContract;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import timber.log.Timber;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/31/2019 10:34
 * ================================================
 */
@FragmentScope
public class HomePresenter extends BasePresenter<HomeContract.Model, HomeContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AccountManager mAccountManager;
    @Inject
    Application mApplication;

    // 循环定位倒计时
    private Disposable mDisposable;

    @Inject
    public HomePresenter(HomeContract.Model model, HomeContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 初始化首页
     */
    public void initHome() {
        // 循环检测是否定位成功(每一秒检测一次，10秒后如果没有定位成功，则改成手动设置)
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(10)
                .compose(RxUtils.applySchedulers(mRootView))                                        // 切换线程
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Timber.i("###onNext#address=%s", mAccountManager.getAddress());
                        if (!TextUtils.isEmpty(mAccountManager.getAddress())) {
                            // 获取定位成功
                            getHome();
                            Timber.i("###initHome-Dispose");
                            // 跳转循环
                            mDisposable.dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i("###initHome-onError");
                    }

                    @Override
                    public void onComplete() {
                        Timber.i("###initHome-onComplete");
                        // 未定位成功，弹出定位界面(手动设置或者重试)
                        mRootView.homemPositioningFailure();
                    }
                });
    }

    /**
     * 热门关键字
     */
    private void loadSearchHot() {
        List<String> list = new ArrayList<>();
        list.add("人脸采集");
        list.add("语音识别");
        list.add("识别身份证");
        list.add("交给苍天");
        list.add("取个颜色");
        mRootView.homeSearchHot(list);
    }

    /**
     * 获取首页数据
     */
    public void getHome() {
        // 设置头像
        mRootView.homeSetAvatar(mAccountManager.getPhotoUrl());
        // 拿到定位数据并进行展示
        mRootView.homeSetLocateAdd(mAccountManager.getSelectCity());
        // 加载热门搜索
        loadSearchHot();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAccountManager = null;
        this.mApplication = null;

        this.mDisposable = null;
    }
}
