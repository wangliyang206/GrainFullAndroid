package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.CompassClockModule;
import com.zqw.mobile.grainfull.mvp.contract.CompassClockContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.CompassClockActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/20 17:44
 * ================================================
 */
@ActivityScope
@Component(modules = CompassClockModule.class, dependencies = AppComponent.class)
public interface CompassClockComponent {

    void inject(CompassClockActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        CompassClockComponent.Builder view(CompassClockContract.View view);

        CompassClockComponent.Builder appComponent(AppComponent appComponent);

        CompassClockComponent build();
    }
}