package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.CompassClockContract;
import com.zqw.mobile.grainfull.mvp.model.CompassClockModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/20 17:44
 * ================================================
 */
@Module
//构建CompassClockModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class CompassClockModule {

    @Binds
    abstract CompassClockContract.Model bindCompassClockModel(CompassClockModel model);
}