package com.zqw.mobile.grainfull.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.zqw.mobile.grainfull.mvp.contract.AudioWaveformContract;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * ================================================
 * Description:音频波形
 * <p>
 * Created by MVPArmsTemplate on 2022/08/03 14:42
 * ================================================
 */
@ActivityScope
public class AudioWaveformPresenter extends BasePresenter<AudioWaveformContract.Model, AudioWaveformContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    /**
     * 执行时长(秒)
     */
    private final int mPlayTime = 10;

    @Inject
    public AudioWaveformPresenter(AudioWaveformContract.Model model, AudioWaveformContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 开始播放
     */
    public void onPlay() {
        // 开始做倒计时效果
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(mPlayTime + 1)                                                                // 超过多少秒停止执行
                .map(aLong -> {
                    return mPlayTime - aLong;                                                       // 由于是倒计时，需要将倒计时的数字反过来
                })
                .observeOn(AndroidSchedulers.mainThread())                                          // 切换线程
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))                               // 使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long num) {
                        mRootView.loadView();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}