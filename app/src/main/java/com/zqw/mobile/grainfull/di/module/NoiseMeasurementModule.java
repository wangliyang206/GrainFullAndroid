package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.NoiseMeasurementContract;
import com.zqw.mobile.grainfull.mvp.model.NoiseMeasurementModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/13 10:24
 * ================================================
 */
@Module
//构建NoiseMeasurementModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class NoiseMeasurementModule {

    @Binds
    abstract NoiseMeasurementContract.Model bindNoiseMeasurementModel(NoiseMeasurementModel model);
}