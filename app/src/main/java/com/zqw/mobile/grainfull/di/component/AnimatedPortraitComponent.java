package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.AnimatedPortraitModule;
import com.zqw.mobile.grainfull.mvp.contract.AnimatedPortraitContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.AnimatedPortraitActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/07/04 10:14
 * ================================================
 */
@ActivityScope
@Component(modules = AnimatedPortraitModule.class, dependencies = AppComponent.class)
public interface AnimatedPortraitComponent {

    void inject(AnimatedPortraitActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        AnimatedPortraitComponent.Builder view(AnimatedPortraitContract.View view);

        AnimatedPortraitComponent.Builder appComponent(AppComponent appComponent);

        AnimatedPortraitComponent build();
    }
}