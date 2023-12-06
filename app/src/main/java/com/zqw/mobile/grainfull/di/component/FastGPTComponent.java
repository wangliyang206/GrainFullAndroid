package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.FastGPTModule;
import com.zqw.mobile.grainfull.mvp.contract.FastGPTContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.FastGPTActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/12/06 16:12
 * ================================================
 */
@ActivityScope
@Component(modules = FastGPTModule.class, dependencies = AppComponent.class)
public interface FastGPTComponent {

    void inject(FastGPTActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        FastGPTComponent.Builder view(FastGPTContract.View view);

        FastGPTComponent.Builder appComponent(AppComponent appComponent);

        FastGPTComponent build();
    }
}