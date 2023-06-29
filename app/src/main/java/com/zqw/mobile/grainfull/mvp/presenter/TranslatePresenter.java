package com.zqw.mobile.grainfull.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.app.utils.RxUtils;
import com.zqw.mobile.grainfull.mvp.contract.TranslateContract;
import com.zqw.mobile.grainfull.mvp.model.entity.TranslateResponse;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/06/29 09:30
 * ================================================
 */
@ActivityScope
public class TranslatePresenter extends BasePresenter<TranslateContract.Model, TranslateContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public TranslatePresenter(TranslateContract.Model model, TranslateContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 翻译
     */
    public void translate(String text, String from, String to) {
        // "auto", "en"
        mModel.translate(text, from, to)
                .compose(RxUtils.applySchedulers(mRootView))                                        // 使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<TranslateResponse>(mErrorHandler) {

                    @Override
                    public void onNext(TranslateResponse info) {
                        if (info.getError_code() == -1) {
                            // 没有错误
                            if (CommonUtils.isNotEmpty(info.getTrans_result())) {
                                mRootView.loadContent(info.getTrans_result().get(0).getDst());
                            } else {
                                mRootView.showMessage("暂无内容！");
                            }

                        } else {
                            // 当前翻译有错误
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