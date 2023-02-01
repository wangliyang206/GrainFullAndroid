package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.AircraftWarContract;
import com.zqw.mobile.grainfull.mvp.model.AircraftWarModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/01 14:15
 * ================================================
 */
@Module
//构建AircraftWarModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class AircraftWarModule {

    @Binds
    abstract AircraftWarContract.Model bindAircraftWarModel(AircraftWarModel model);
}