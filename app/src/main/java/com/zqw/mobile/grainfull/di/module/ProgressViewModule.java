package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.ProgressViewContract;
import com.zqw.mobile.grainfull.mvp.model.ProgressViewModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/10 16:38
 * ================================================
 */
@Module
//构建ProgressViewModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class ProgressViewModule {

    @Binds
    abstract ProgressViewContract.Model bindProgressViewModel(ProgressViewModel model);
}