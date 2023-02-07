package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.FragmentScope;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.RequestMapper;
import com.zqw.mobile.grainfull.app.utils.DaoManager;
import com.zqw.mobile.grainfull.mvp.contract.OneLineToEndContract;
import com.zqw.mobile.grainfull.mvp.model.OneLineToEndModel;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/01 14:15
 * ================================================
 */
@Module
//构建OneLineToEndModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class OneLineToEndModule {

    @Binds
    abstract OneLineToEndContract.Model bindOneLineToEndModel(OneLineToEndModel model);

    @FragmentScope
    @Provides
    static AccountManager provideAccountManager(OneLineToEndContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @FragmentScope
    @Provides
    static IRequestMapper providerRequestMapper(OneLineToEndContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @FragmentScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }

    @FragmentScope
    @Provides
    static DaoManager providerDaoManager(OneLineToEndContract.View view) {
        return new DaoManager(view.getActivity());
    }
}