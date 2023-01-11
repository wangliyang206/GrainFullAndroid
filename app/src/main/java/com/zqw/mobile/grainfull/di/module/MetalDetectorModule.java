package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.RequestMapper;
import com.zqw.mobile.grainfull.mvp.contract.MetalDetectorContract;
import com.zqw.mobile.grainfull.mvp.model.MetalDetectorModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/10 16:38
 * ================================================
 */
@Module
//构建MetalDetectorModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class MetalDetectorModule {

    @Binds
    abstract MetalDetectorContract.Model bindMetalDetectorModel(MetalDetectorModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(MetalDetectorContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(MetalDetectorContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }
}