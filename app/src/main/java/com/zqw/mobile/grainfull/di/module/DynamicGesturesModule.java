package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.DynamicGesturesContract;
import com.zqw.mobile.grainfull.mvp.model.DynamicGesturesModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/12 12:30
 * ================================================
 */
@Module
//构建DynamicGesturesModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class DynamicGesturesModule {

    @Binds
    abstract DynamicGesturesContract.Model bindDynamicGesturesModel(DynamicGesturesModel model);
}