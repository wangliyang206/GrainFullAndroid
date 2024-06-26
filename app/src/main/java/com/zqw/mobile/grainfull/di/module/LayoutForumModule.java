package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.RequestMapper;
import com.zqw.mobile.grainfull.mvp.contract.LayoutForumContract;
import com.zqw.mobile.grainfull.mvp.model.LayoutForumModel;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/07/05 11:06
 * ================================================
 */
@Module
//构建LayoutHomeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class LayoutForumModule {

    @Binds
    abstract LayoutForumContract.Model bindLayoutForumModel(LayoutForumModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(LayoutForumContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(LayoutForumContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }
}