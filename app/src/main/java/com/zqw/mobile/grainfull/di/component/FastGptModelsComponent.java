package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.FastGptModelsModule;
import com.zqw.mobile.grainfull.mvp.contract.FastGptModelsContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.FastGptModelsActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/12/26 09:59
 * ================================================
 */
@ActivityScope
@Component(modules = FastGptModelsModule.class, dependencies = AppComponent.class)
public interface FastGptModelsComponent {

    void inject(FastGptModelsActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        FastGptModelsComponent.Builder view(FastGptModelsContract.View view);

        FastGptModelsComponent.Builder appComponent(AppComponent appComponent);

        FastGptModelsComponent build();
    }
}