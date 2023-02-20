package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.DashboardContract;
import com.zqw.mobile.grainfull.mvp.model.DashboardModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/20 17:44
 * ================================================
 */
@Module
//构建DashboardModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class DashboardModule {

    @Binds
    abstract DashboardContract.Model bindDashboardModel(DashboardModel model);
}