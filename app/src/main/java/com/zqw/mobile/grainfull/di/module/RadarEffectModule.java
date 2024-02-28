package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.RadarEffectContract;
import com.zqw.mobile.grainfull.mvp.model.RadarEffectModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/02/28 14:33
 * ================================================
 */
@Module
//构建RadarEffectModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class RadarEffectModule {

    @Binds
    abstract RadarEffectContract.Model bindRadarEffectModel(RadarEffectModel model);
}