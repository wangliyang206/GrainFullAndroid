package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.TrendChartContract;
import com.zqw.mobile.grainfull.mvp.model.TrendChartModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/03/11 15:16
 * ================================================
 */
@Module
//构建TrendChartModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class TrendChartModule {

    @Binds
    abstract TrendChartContract.Model bindTrendChartModel(TrendChartModel model);
}