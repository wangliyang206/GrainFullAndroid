package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.DashboardModule;
import com.zqw.mobile.grainfull.mvp.contract.DashboardContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.DashboardActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/20 17:44
 * ================================================
 */
@ActivityScope
@Component(modules = DashboardModule.class, dependencies = AppComponent.class)
public interface DashboardComponent {

    void inject(DashboardActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        DashboardComponent.Builder view(DashboardContract.View view);

        DashboardComponent.Builder appComponent(AppComponent appComponent);

        DashboardComponent build();
    }
}