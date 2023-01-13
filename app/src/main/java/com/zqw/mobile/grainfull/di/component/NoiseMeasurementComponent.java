package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.NoiseMeasurementModule;
import com.zqw.mobile.grainfull.mvp.contract.NoiseMeasurementContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.NoiseMeasurementActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/13 10:24
 * ================================================
 */
@ActivityScope
@Component(modules = NoiseMeasurementModule.class, dependencies = AppComponent.class)
public interface NoiseMeasurementComponent {

    void inject(NoiseMeasurementActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        NoiseMeasurementComponent.Builder view(NoiseMeasurementContract.View view);

        NoiseMeasurementComponent.Builder appComponent(AppComponent appComponent);

        NoiseMeasurementComponent build();
    }
}