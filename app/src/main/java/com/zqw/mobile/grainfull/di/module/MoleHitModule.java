package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.FragmentScope;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.RequestMapper;
import com.zqw.mobile.grainfull.mvp.contract.MoleHitContract;
import com.zqw.mobile.grainfull.mvp.model.MoleHitModel;

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
public abstract class MoleHitModule {

    @Binds
    abstract MoleHitContract.Model bindHomeModel(MoleHitModel model);

    @FragmentScope
    @Provides
    static AccountManager provideAccountManager(MoleHitContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @FragmentScope
    @Provides
    static IRequestMapper providerRequestMapper(MoleHitContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @FragmentScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }

}