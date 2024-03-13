package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.WaveEffectsModule;
import com.zqw.mobile.grainfull.mvp.contract.WaveEffectsContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.WaveEffectsActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/03/13 13:51
 * ================================================
 */
@ActivityScope
@Component(modules = WaveEffectsModule.class, dependencies = AppComponent.class)
public interface WaveEffectsComponent {

    void inject(WaveEffectsActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        WaveEffectsComponent.Builder view(WaveEffectsContract.View view);

        WaveEffectsComponent.Builder appComponent(AppComponent appComponent);

        WaveEffectsComponent build();
    }
}