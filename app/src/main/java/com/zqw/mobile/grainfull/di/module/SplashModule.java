package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.RequestMapper;
import com.zqw.mobile.grainfull.mvp.contract.SplashContract;
import com.zqw.mobile.grainfull.mvp.model.SplashModel;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/05/2019 13:57
 * ================================================
 */
@Module
public abstract class SplashModule {

    @Binds
    abstract SplashContract.Model bindSplashModel(SplashModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(SplashContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(SplashContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }

}