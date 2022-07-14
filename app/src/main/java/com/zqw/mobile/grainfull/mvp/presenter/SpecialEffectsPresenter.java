package com.zqw.mobile.grainfull.mvp.presenter;

import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.mvp.contract.SpecialEffectsContract;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/31/2019 10:34
 * ================================================
 */
@FragmentScope
public class SpecialEffectsPresenter extends BasePresenter<SpecialEffectsContract.Model, SpecialEffectsContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public SpecialEffectsPresenter(SpecialEffectsContract.Model model, SpecialEffectsContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;

    }
}
