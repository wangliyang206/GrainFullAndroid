package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.LivenessDetectionModule;
import com.zqw.mobile.grainfull.mvp.contract.LivenessDetectionContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.LivenessDetectionActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/12 12:30
 * ================================================
 */
@ActivityScope
@Component(modules = LivenessDetectionModule.class, dependencies = AppComponent.class)
public interface LivenessDetectionComponent {

    void inject(LivenessDetectionActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LivenessDetectionComponent.Builder view(LivenessDetectionContract.View view);

        LivenessDetectionComponent.Builder appComponent(AppComponent appComponent);

        LivenessDetectionComponent build();
    }
}