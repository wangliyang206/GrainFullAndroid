package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.TemperatureContract;
import com.zqw.mobile.grainfull.mvp.model.TemperatureModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/28 14:10
 * ================================================
 */
@Module
//构建TemperatureModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class TemperatureModule {

    @Binds
    abstract TemperatureContract.Model bindTemperatureModel(TemperatureModel model);
}