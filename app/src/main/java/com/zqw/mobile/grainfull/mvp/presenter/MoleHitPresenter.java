package com.zqw.mobile.grainfull.mvp.presenter;

import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.mvp.contract.MoleHitContract;

import javax.inject.Inject;


/**
 * ================================================
 * Description: 打地鼠
 * <p>
 * Created by MVPArmsTemplate on 05/31/2019 10:34
 * ================================================
 */
@FragmentScope
public class MoleHitPresenter extends BasePresenter<MoleHitContract.Model, MoleHitContract.View> {

    @Inject
    public MoleHitPresenter(MoleHitContract.Model model, MoleHitContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
