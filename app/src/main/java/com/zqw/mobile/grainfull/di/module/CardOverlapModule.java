package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.RequestMapper;
import com.zqw.mobile.grainfull.mvp.contract.CardOverlapContract;
import com.zqw.mobile.grainfull.mvp.model.CardOverlapModel;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeActionBarInfo;
import com.zqw.mobile.grainfull.mvp.ui.adapter.CardAdapter;
import com.zqw.mobile.grainfull.mvp.ui.widget.stackedcards.OverLayCardLayoutManager;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/09/19 11:46
 * ================================================
 */
@Module
//构建CardOverlapModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class CardOverlapModule {

    @Binds
    abstract CardOverlapContract.Model bindCardOverlapModel(CardOverlapModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(CardOverlapContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(CardOverlapContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }

    @ActivityScope
    @Provides
    static OverLayCardLayoutManager provideOverLayCardLayoutManager(CardOverlapContract.View view) {
        return new OverLayCardLayoutManager();
    }

    @ActivityScope
    @Provides
    static List<HomeActionBarInfo> provideHomeActionBarInfo() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static CardAdapter provideCardAdapter(List<HomeActionBarInfo> list) {
        return new CardAdapter(list);
    }
}