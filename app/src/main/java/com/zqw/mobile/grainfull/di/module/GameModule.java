package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.FragmentScope;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.RequestMapper;
import com.zqw.mobile.grainfull.mvp.contract.GameContract;
import com.zqw.mobile.grainfull.mvp.model.GameModel;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/31/2019 10:34
 * ================================================
 */
@Module
public abstract class GameModule {

    @Binds
    abstract GameContract.Model bindHomeModel(GameModel model);

    @FragmentScope
    @Provides
    static AccountManager provideAccountManager(GameContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @FragmentScope
    @Provides
    static IRequestMapper providerRequestMapper(GameContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @FragmentScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }

}