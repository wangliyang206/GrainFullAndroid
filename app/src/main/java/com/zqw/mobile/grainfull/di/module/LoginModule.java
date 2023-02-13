package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.RequestMapper;
import com.zqw.mobile.grainfull.mvp.contract.LoginContract;
import com.zqw.mobile.grainfull.mvp.model.LoginModel;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/16/2019 10:32
 * ================================================
 */
@Module
public abstract class LoginModule {

    @Binds
    abstract LoginContract.Model bindLoginModel(LoginModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(LoginContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(LoginContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }

}