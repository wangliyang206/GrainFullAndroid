package com.zqw.mobile.grainfull.mvp.presenter;

import android.text.TextUtils;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.zqw.mobile.grainfull.BuildConfig;
import com.zqw.mobile.grainfull.app.config.CommonRetryWithDelay;
import com.zqw.mobile.grainfull.app.utils.RxUtils;
import com.zqw.mobile.grainfull.mvp.contract.RemoveWatermarkinContract;
import com.zqw.mobile.grainfull.mvp.model.entity.BaiduAiResponse;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/07/05 11:06
 * ================================================
 */
@ActivityScope
public class RemoveWatermarkinPresenter extends BasePresenter<RemoveWatermarkinContract.Model, RemoveWatermarkinContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    // 百度Token
    private String mAccessToken;

    @Inject
    public RemoveWatermarkinPresenter(RemoveWatermarkinContract.Model model, RemoveWatermarkinContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获取百度Token
     */
    public void getBaiduToken() {
        mModel.getBaiduToken(BuildConfig.BAIDU_API_KEY, BuildConfig.BAIDU_SECRET_KEY)
                .compose(RxUtils.applySchedulers(mRootView))                                        // 使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaiduAiResponse>(mErrorHandler) {

                    @Override
                    public void onNext(BaiduAiResponse info) {
                        if (!TextUtils.isEmpty(info.getAccess_token())) {
                            mAccessToken = info.getAccess_token();
                        } else {
                            mRootView.showMessage(info.getError_description());
                        }
                    }
                });
    }

    /**
     * 去掉水印
     */
    public void removeWatermarkin(String path) {
        mModel.removeWatermarkin(mAccessToken, path)
                .subscribeOn(Schedulers.io())
                .retryWhen(new CommonRetryWithDelay(2, 2))                 // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
//                    mRootView.showLoadingSubmit();                                                  // 显示进度条
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoadingSubmit();                                                  // 隐藏进度条
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaiduAiResponse>(mErrorHandler) {

                    @Override
                    public void onNext(BaiduAiResponse info) {
                        if (!TextUtils.isEmpty(info.getResult())) {
                            mRootView.loadImage(info.getResult());
                        } else {
                            mRootView.showMessage(info.getError_msg());
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}