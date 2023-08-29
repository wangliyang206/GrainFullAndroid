package com.zqw.mobile.grainfull.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.mvp.contract.GoodsListContract;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/07/05 11:06
 * ================================================
 */
@ActivityScope
public class GoodsListPresenter extends BasePresenter<GoodsListContract.Model, GoodsListContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public GoodsListPresenter(GoodsListContract.Model model, GoodsListContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }

    /**
     * 获取数据
     */
    public void initData() {

    }

}