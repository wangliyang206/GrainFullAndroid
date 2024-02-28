package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.RadarEffectModule;
import com.zqw.mobile.grainfull.mvp.contract.RadarEffectContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.RadarEffectActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/02/28 14:33
 * ================================================
 */
@ActivityScope
@Component(modules = RadarEffectModule.class, dependencies = AppComponent.class)
public interface RadarEffectComponent {

    void inject(RadarEffectActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        RadarEffectComponent.Builder view(RadarEffectContract.View view);

        RadarEffectComponent.Builder appComponent(AppComponent appComponent);

        RadarEffectComponent build();
    }
}