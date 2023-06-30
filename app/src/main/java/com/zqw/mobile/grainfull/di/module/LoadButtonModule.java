package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.LoadButtonContract;
import com.zqw.mobile.grainfull.mvp.model.LoadButtonModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/06/30 12:14
 * ================================================
 */
@Module
//构建LoadButtonModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class LoadButtonModule {

    @Binds
    abstract LoadButtonContract.Model bindLoadButtonModel(LoadButtonModel model);
}