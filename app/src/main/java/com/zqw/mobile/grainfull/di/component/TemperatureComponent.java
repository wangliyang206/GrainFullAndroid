package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.TemperatureModule;
import com.zqw.mobile.grainfull.mvp.contract.TemperatureContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.TemperatureActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/28 14:10
 * ================================================
 */
@ActivityScope
@Component(modules = TemperatureModule.class, dependencies = AppComponent.class)
public interface TemperatureComponent {

    void inject(TemperatureActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        TemperatureComponent.Builder view(TemperatureContract.View view);

        TemperatureComponent.Builder appComponent(AppComponent appComponent);

        TemperatureComponent build();
    }
}