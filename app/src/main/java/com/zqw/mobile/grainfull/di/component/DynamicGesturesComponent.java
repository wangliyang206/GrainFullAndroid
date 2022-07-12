package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.DynamicGesturesModule;
import com.zqw.mobile.grainfull.mvp.contract.DynamicGesturesContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.DynamicGesturesActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/12 12:30
 * ================================================
 */
@ActivityScope
@Component(modules = DynamicGesturesModule.class, dependencies = AppComponent.class)
public interface DynamicGesturesComponent {

    void inject(DynamicGesturesActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        DynamicGesturesComponent.Builder view(DynamicGesturesContract.View view);

        DynamicGesturesComponent.Builder appComponent(AppComponent appComponent);

        DynamicGesturesComponent build();
    }
}