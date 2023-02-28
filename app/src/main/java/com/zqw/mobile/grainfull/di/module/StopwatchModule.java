package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.StopwatchContract;
import com.zqw.mobile.grainfull.mvp.model.StopwatchModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/27 16:34
 * ================================================
 */
@Module
//构建StopwatchModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class StopwatchModule {

    @Binds
    abstract StopwatchContract.Model bindStopwatchModel(StopwatchModel model);
}