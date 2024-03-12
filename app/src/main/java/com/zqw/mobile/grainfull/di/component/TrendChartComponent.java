package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.TrendChartModule;
import com.zqw.mobile.grainfull.mvp.contract.TrendChartContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.TrendChartActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/03/11 15:16
 * ================================================
 */
@ActivityScope
@Component(modules = TrendChartModule.class, dependencies = AppComponent.class)
public interface TrendChartComponent {

    void inject(TrendChartActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        TrendChartComponent.Builder view(TrendChartContract.View view);

        TrendChartComponent.Builder appComponent(AppComponent appComponent);

        TrendChartComponent build();
    }
}