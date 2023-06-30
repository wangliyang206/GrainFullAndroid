package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.LoadButtonModule;
import com.zqw.mobile.grainfull.mvp.contract.LoadButtonContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.LoadButtonActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/06/30 12:14
 * ================================================
 */
@ActivityScope
@Component(modules = LoadButtonModule.class, dependencies = AppComponent.class)
public interface LoadButtonComponent {

    void inject(LoadButtonActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LoadButtonComponent.Builder view(LoadButtonContract.View view);

        LoadButtonComponent.Builder appComponent(AppComponent appComponent);

        LoadButtonComponent build();
    }
}