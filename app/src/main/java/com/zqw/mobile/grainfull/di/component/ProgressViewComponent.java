package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.ProgressViewModule;
import com.zqw.mobile.grainfull.mvp.contract.ProgressViewContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.ProgressViewActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/10 16:38
 * ================================================
 */
@ActivityScope
@Component(modules = ProgressViewModule.class, dependencies = AppComponent.class)
public interface ProgressViewComponent {

    void inject(ProgressViewActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ProgressViewComponent.Builder view(ProgressViewContract.View view);

        ProgressViewComponent.Builder appComponent(AppComponent appComponent);

        ProgressViewComponent build();
    }
}