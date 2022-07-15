package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.LivenessCustomDetectionModule;
import com.zqw.mobile.grainfull.mvp.contract.LivenessCustomDetectionContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.LivenessCustomDetectionActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/15 15:37
 * ================================================
 */
@ActivityScope
@Component(modules = LivenessCustomDetectionModule.class, dependencies = AppComponent.class)
public interface LivenessCustomDetectionComponent {

    void inject(LivenessCustomDetectionActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LivenessCustomDetectionComponent.Builder view(LivenessCustomDetectionContract.View view);

        LivenessCustomDetectionComponent.Builder appComponent(AppComponent appComponent);

        LivenessCustomDetectionComponent build();
    }
}