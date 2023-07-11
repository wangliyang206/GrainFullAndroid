package com.zqw.mobile.grainfull.di.module;

import androidx.recyclerview.widget.GridLayoutManager;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.RequestMapper;
import com.zqw.mobile.grainfull.mvp.contract.CardFlippingContract;
import com.zqw.mobile.grainfull.mvp.model.CardFlippingModel;
import com.zqw.mobile.grainfull.mvp.model.entity.CardFlipping;
import com.zqw.mobile.grainfull.mvp.ui.adapter.CardFlippingAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/07/10 17:00
 * ================================================
 */
@Module
//构建CardFlippingModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class CardFlippingModule {

    @Binds
    abstract CardFlippingContract.Model bindCardFlippingModel(CardFlippingModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(CardFlippingContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(CardFlippingContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }

    @ActivityScope
    @Provides
    static List<CardFlipping> provideFinancialGoodsList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static CardFlippingAdapter provideCardFlippingAdapter(List<CardFlipping> list) {
        return new CardFlippingAdapter(list);
    }

    @ActivityScope
    @Provides
    static GridLayoutManager provideRecyGrid(CardFlippingContract.View view) {
        return new GridLayoutManager(view.getActivity(), 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
    }
}