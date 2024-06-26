package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.RequestMapper;
import com.zqw.mobile.grainfull.mvp.contract.FastGptModelsContract;
import com.zqw.mobile.grainfull.mvp.model.FastGptModelsModel;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/12/26 09:59
 * ================================================
 */
@Module
//构建FastGptModelsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class FastGptModelsModule {

    @Binds
    abstract FastGptModelsContract.Model bindFastGptModelsModel(FastGptModelsModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(FastGptModelsContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(FastGptModelsContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }
}