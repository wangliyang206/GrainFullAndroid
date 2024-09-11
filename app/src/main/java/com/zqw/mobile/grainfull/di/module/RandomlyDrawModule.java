package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.RandomlyDrawContract;
import com.zqw.mobile.grainfull.mvp.model.RandomlyDrawModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/09/10 17:04
 * ================================================
 */
@Module
//构建RandomlyDrawModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class RandomlyDrawModule {

    @Binds
    abstract RandomlyDrawContract.Model bindRandomlyDrawModel(RandomlyDrawModel model);
}