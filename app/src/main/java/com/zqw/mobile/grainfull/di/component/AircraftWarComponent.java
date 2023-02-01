package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.AircraftWarModule;
import com.zqw.mobile.grainfull.mvp.contract.AircraftWarContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.AircraftWarActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/01 14:15
 * ================================================
 */
@ActivityScope
@Component(modules = AircraftWarModule.class, dependencies = AppComponent.class)
public interface AircraftWarComponent {

    void inject(AircraftWarActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        AircraftWarComponent.Builder view(AircraftWarContract.View view);

        AircraftWarComponent.Builder appComponent(AppComponent appComponent);

        AircraftWarComponent build();
    }
}