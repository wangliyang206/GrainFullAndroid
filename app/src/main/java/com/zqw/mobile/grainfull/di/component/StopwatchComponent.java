package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.StopwatchModule;
import com.zqw.mobile.grainfull.mvp.contract.StopwatchContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.StopwatchActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/27 16:34
 * ================================================
 */
@ActivityScope
@Component(modules = StopwatchModule.class, dependencies = AppComponent.class)
public interface StopwatchComponent {

    void inject(StopwatchActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        StopwatchComponent.Builder view(StopwatchContract.View view);

        StopwatchComponent.Builder appComponent(AppComponent appComponent);

        StopwatchComponent build();
    }
}