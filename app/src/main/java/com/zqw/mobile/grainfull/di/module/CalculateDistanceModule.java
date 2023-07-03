package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.CalculateDistanceContract;
import com.zqw.mobile.grainfull.mvp.model.CalculateDistanceModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/06/30 12:14
 * ================================================
 */
@Module
//构建CalculateDistanceModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class CalculateDistanceModule {

    @Binds
    abstract CalculateDistanceContract.Model bindCalculateDistanceModel(CalculateDistanceModel model);
}