package com.zqw.mobile.grainfull.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.mvp.contract.IdentifyBankCardsContract;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


/**
 * ================================================
 * Description:识别银行卡
 * <p>
 * Created by MVPArmsTemplate on 06/23/2022 19:34
 * ================================================
 */
@ActivityScope
public class IdentifyBankCardsPresenter extends BasePresenter<IdentifyBankCardsContract.Model, IdentifyBankCardsContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public IdentifyBankCardsPresenter(IdentifyBankCardsContract.Model model, IdentifyBankCardsContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}
