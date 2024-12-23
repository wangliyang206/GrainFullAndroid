package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.VoiceAnimationModule;
import com.zqw.mobile.grainfull.mvp.contract.VoiceAnimationContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.VoiceAnimationActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/12/23 12:38
 * ================================================
 */
@ActivityScope
@Component(modules = VoiceAnimationModule.class, dependencies = AppComponent.class)
public interface VoiceAnimationComponent {

    void inject(VoiceAnimationActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        VoiceAnimationComponent.Builder view(VoiceAnimationContract.View view);

        VoiceAnimationComponent.Builder appComponent(AppComponent appComponent);

        VoiceAnimationComponent build();
    }
}