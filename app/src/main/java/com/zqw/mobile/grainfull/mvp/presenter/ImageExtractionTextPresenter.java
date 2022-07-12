package com.zqw.mobile.grainfull.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.mvp.contract.ImageExtractionTextContract;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * ================================================
 * Description:图片提取文字
 * <p>
 * Created by MVPArmsTemplate on 2022/07/08 16:11
 * ================================================
 */
@ActivityScope
public class ImageExtractionTextPresenter extends BasePresenter<ImageExtractionTextContract.Model, ImageExtractionTextContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public ImageExtractionTextPresenter(ImageExtractionTextContract.Model model, ImageExtractionTextContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}