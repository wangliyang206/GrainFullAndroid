package com.zqw.mobile.grainfull.mvp.presenter;

import com.zqw.mobile.grainfull.mvp.contract.AboutContract;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


/**
 * ================================================
 * Description:关于我们
 * <p>
 * Created by MVPArmsTemplate on 03/28/2020 11:19
 * ================================================
 */
@ActivityScope
public class AboutPresenter extends BasePresenter<AboutContract.Model, AboutContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public AboutPresenter(AboutContract.Model model, AboutContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}
