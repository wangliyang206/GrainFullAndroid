package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.RandomlyDrawModule;
import com.zqw.mobile.grainfull.mvp.contract.RandomlyDrawContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.RandomlyDrawActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/09/10 17:04
 * ================================================
 */
@ActivityScope
@Component(modules = RandomlyDrawModule.class, dependencies = AppComponent.class)
public interface RandomlyDrawComponent {

    void inject(RandomlyDrawActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        RandomlyDrawComponent.Builder view(RandomlyDrawContract.View view);

        RandomlyDrawComponent.Builder appComponent(AppComponent appComponent);

        RandomlyDrawComponent build();
    }
}