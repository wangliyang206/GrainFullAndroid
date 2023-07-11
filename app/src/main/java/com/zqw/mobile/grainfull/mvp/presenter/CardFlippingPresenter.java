package com.zqw.mobile.grainfull.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.contract.CardFlippingContract;
import com.zqw.mobile.grainfull.mvp.model.entity.CardFlipping;
import com.zqw.mobile.grainfull.mvp.ui.adapter.CardFlippingAdapter;

import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/07/10 17:00
 * ================================================
 */
@ActivityScope
public class CardFlippingPresenter extends BasePresenter<CardFlippingContract.Model, CardFlippingContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    // 数据源
    @Inject
    List<CardFlipping> mList;
    @Inject
    CardFlippingAdapter mAdapter;                                                                   // 内容适配器

    @Inject
    public CardFlippingPresenter(CardFlippingContract.Model model, CardFlippingContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 初始化游戏
     */
    public void init() {
        mList.add(new CardFlipping(1, 1, R.mipmap.heitao, R.mipmap.black_1));
        mList.add(new CardFlipping(2, 2, R.mipmap.heitao, R.mipmap.black_2));
        mList.add(new CardFlipping(3, 3, R.mipmap.heitao, R.mipmap.black_3));
        mList.add(new CardFlipping(4, 4, R.mipmap.heitao, R.mipmap.black_4));
        mList.add(new CardFlipping(5, 5, R.mipmap.heitao, R.mipmap.black_5));
        mList.add(new CardFlipping(6, 6, R.mipmap.heitao, R.mipmap.black_6));
        mList.add(new CardFlipping(7, 7, R.mipmap.heitao, R.mipmap.black_7));
        mList.add(new CardFlipping(8, 8, R.mipmap.heitao, R.mipmap.black_8));

        mList.add(new CardFlipping(9, 1, R.mipmap.hongtao, R.mipmap.red_1));
        mList.add(new CardFlipping(10, 2, R.mipmap.hongtao, R.mipmap.red_2));
        mList.add(new CardFlipping(11, 3, R.mipmap.hongtao, R.mipmap.red_3));
        mList.add(new CardFlipping(12, 4, R.mipmap.hongtao, R.mipmap.red_4));
        mList.add(new CardFlipping(13, 5, R.mipmap.hongtao, R.mipmap.red_5));
        mList.add(new CardFlipping(14, 6, R.mipmap.hongtao, R.mipmap.red_6));
        mList.add(new CardFlipping(15, 7, R.mipmap.hongtao, R.mipmap.red_7));
        mList.add(new CardFlipping(16, 8, R.mipmap.hongtao, R.mipmap.red_8));

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}