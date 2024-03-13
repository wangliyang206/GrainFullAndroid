package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.WaveEffectsContract;
import com.zqw.mobile.grainfull.mvp.model.WaveEffectsModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/03/13 13:51
 * ================================================
 */
@Module
//构建WaveEffectsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class WaveEffectsModule {

    @Binds
    abstract WaveEffectsContract.Model bindWaveEffectsModel(WaveEffectsModel model);
}