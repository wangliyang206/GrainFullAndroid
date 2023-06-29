package com.zqw.mobile.grainfull.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.mvp.contract.PictureCompressionContract;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/15 13:51
 * ================================================
 */
@ActivityScope
public class PictureCompressionPresenter extends BasePresenter<PictureCompressionContract.Model, PictureCompressionContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public PictureCompressionPresenter(PictureCompressionContract.Model model, PictureCompressionContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}