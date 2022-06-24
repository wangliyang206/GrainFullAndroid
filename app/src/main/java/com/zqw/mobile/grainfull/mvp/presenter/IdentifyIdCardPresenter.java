package com.zqw.mobile.grainfull.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.mvp.contract.IdentifyIdCardContract;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


/**
 * ================================================
 * Description:识别身份证
 * <p>
 * Created by MVPArmsTemplate on 06/24/2022 11:00
 * ================================================
 */
@ActivityScope
public class IdentifyIdCardPresenter extends BasePresenter<IdentifyIdCardContract.Model, IdentifyIdCardContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public IdentifyIdCardPresenter(IdentifyIdCardContract.Model model, IdentifyIdCardContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}
