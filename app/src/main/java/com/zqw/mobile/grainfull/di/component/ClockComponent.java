package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.ClockModule;
import com.zqw.mobile.grainfull.mvp.contract.ClockContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.ClockActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/20 17:44
 * ================================================
 */
@ActivityScope
@Component(modules = ClockModule.class, dependencies = AppComponent.class)
public interface ClockComponent {

    void inject(ClockActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ClockComponent.Builder view(ClockContract.View view);

        ClockComponent.Builder appComponent(AppComponent appComponent);

        ClockComponent build();
    }
}