package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.CalculateDistanceModule;
import com.zqw.mobile.grainfull.mvp.contract.CalculateDistanceContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.CalculateDistanceActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/06/30 12:14
 * ================================================
 */
@ActivityScope
@Component(modules = CalculateDistanceModule.class, dependencies = AppComponent.class)
public interface CalculateDistanceComponent {

    void inject(CalculateDistanceActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        CalculateDistanceComponent.Builder view(CalculateDistanceContract.View view);

        CalculateDistanceComponent.Builder appComponent(AppComponent appComponent);

        CalculateDistanceComponent build();
    }
}