package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.StatisticalEffectsContract;
import com.zqw.mobile.grainfull.mvp.model.StatisticalEffectsModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/03/12 13:46
 * ================================================
 */
@Module
//构建StatisticalEffectsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class StatisticalEffectsModule {

    @Binds
    abstract StatisticalEffectsContract.Model bindStatisticalEffectsModel(StatisticalEffectsModel model);
}