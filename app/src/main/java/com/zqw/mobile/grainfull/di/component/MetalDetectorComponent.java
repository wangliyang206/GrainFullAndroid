package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.MetalDetectorModule;
import com.zqw.mobile.grainfull.mvp.contract.MetalDetectorContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.MetalDetectorActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/10 16:38
 * ================================================
 */
@ActivityScope
@Component(modules = MetalDetectorModule.class, dependencies = AppComponent.class)
public interface MetalDetectorComponent {

    void inject(MetalDetectorActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        MetalDetectorComponent.Builder view(MetalDetectorContract.View view);

        MetalDetectorComponent.Builder appComponent(AppComponent appComponent);

        MetalDetectorComponent build();
    }
}