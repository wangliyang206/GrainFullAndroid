package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.HmsSceneAnimationEffectModule;
import com.zqw.mobile.grainfull.mvp.contract.HmsSceneAnimationEffectContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.HmsSceneAnimationEffectActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/12/10 09:16
 * ================================================
 */
@ActivityScope
@Component(modules = HmsSceneAnimationEffectModule.class, dependencies = AppComponent.class)
public interface HmsSceneAnimationEffectComponent {

    void inject(HmsSceneAnimationEffectActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        HmsSceneAnimationEffectComponent.Builder view(HmsSceneAnimationEffectContract.View view);

        HmsSceneAnimationEffectComponent.Builder appComponent(AppComponent appComponent);

        HmsSceneAnimationEffectComponent build();
    }
}