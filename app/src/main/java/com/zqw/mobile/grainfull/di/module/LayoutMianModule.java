package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.RequestMapper;
import com.zqw.mobile.grainfull.mvp.contract.LayoutMianContract;
import com.zqw.mobile.grainfull.mvp.model.LayoutMianModel;
import com.zqw.mobile.grainfull.mvp.model.entity.NewHomeInfo;
import com.zqw.mobile.grainfull.mvp.ui.adapter.NewHomeAdapter;

import java.util.ArrayList;
import java.util.List;

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
public abstract class LayoutMianModule {

    @Binds
    abstract LayoutMianContract.Model bindLayoutMianModel(LayoutMianModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(LayoutMianContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(LayoutMianContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }

    @ActivityScope
    @Provides
    static List<NewHomeInfo> provideNewHomeInfo() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static NewHomeAdapter provideNewHomeAdapter(List<NewHomeInfo> list) {
        return new NewHomeAdapter(list);
    }
}