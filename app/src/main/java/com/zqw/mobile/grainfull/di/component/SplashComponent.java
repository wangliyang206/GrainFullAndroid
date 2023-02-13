package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.zqw.mobile.grainfull.di.module.SplashModule;
import com.zqw.mobile.grainfull.mvp.contract.SplashContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.SplashActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/05/2019 13:57
 * ================================================
 */
@ActivityScope
@Component(modules = SplashModule.class, dependencies = AppComponent.class)
public interface SplashComponent {
    void inject(SplashActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder view(SplashContract.View view);

        Builder appComponent(AppComponent appComponent);

        SplashComponent build();
    }
}