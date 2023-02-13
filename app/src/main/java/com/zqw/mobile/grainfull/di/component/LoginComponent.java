package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.zqw.mobile.grainfull.di.module.LoginModule;
import com.zqw.mobile.grainfull.mvp.contract.LoginContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.LoginActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/16/2019 10:32
 * ================================================
 */
@ActivityScope
@Component(modules = LoginModule.class, dependencies = AppComponent.class)
public interface LoginComponent {
    void inject(LoginActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LoginComponent.Builder view(LoginContract.View view);

        LoginComponent.Builder appComponent(AppComponent appComponent);

        LoginComponent build();
    }
}