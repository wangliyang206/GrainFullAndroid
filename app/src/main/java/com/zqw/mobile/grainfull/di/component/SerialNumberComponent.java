package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.SerialNumberModule;
import com.zqw.mobile.grainfull.mvp.contract.SerialNumberContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.SerialNumberActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/14 17:11
 * ================================================
 */
@ActivityScope
@Component(modules = SerialNumberModule.class, dependencies = AppComponent.class)
public interface SerialNumberComponent {

    void inject(SerialNumberActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SerialNumberComponent.Builder view(SerialNumberContract.View view);

        SerialNumberComponent.Builder appComponent(AppComponent appComponent);

        SerialNumberComponent build();
    }
}