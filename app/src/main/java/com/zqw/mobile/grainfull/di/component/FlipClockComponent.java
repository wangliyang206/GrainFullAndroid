package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.FlipClockModule;
import com.zqw.mobile.grainfull.mvp.contract.FlipClockContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.FlipClockActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/20 17:44
 * ================================================
 */
@ActivityScope
@Component(modules = FlipClockModule.class, dependencies = AppComponent.class)
public interface FlipClockComponent {

    void inject(FlipClockActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        FlipClockComponent.Builder view(FlipClockContract.View view);

        FlipClockComponent.Builder appComponent(AppComponent appComponent);

        FlipClockComponent build();
    }
}