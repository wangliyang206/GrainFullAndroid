package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.StatisticalEffectsModule;
import com.zqw.mobile.grainfull.mvp.contract.StatisticalEffectsContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.StatisticalEffectsActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/03/12 13:46
 * ================================================
 */
@ActivityScope
@Component(modules = StatisticalEffectsModule.class, dependencies = AppComponent.class)
public interface StatisticalEffectsComponent {

    void inject(StatisticalEffectsActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        StatisticalEffectsComponent.Builder view(StatisticalEffectsContract.View view);

        StatisticalEffectsComponent.Builder appComponent(AppComponent appComponent);

        StatisticalEffectsComponent build();
    }
}