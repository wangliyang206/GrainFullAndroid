package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.UmDataStatisticsModule;
import com.zqw.mobile.grainfull.mvp.contract.UmDataStatisticsContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.UmDataStatisticsActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/16 10:42
 * ================================================
 */
@ActivityScope
@Component(modules = UmDataStatisticsModule.class, dependencies = AppComponent.class)
public interface UmDataStatisticsComponent {

    void inject(UmDataStatisticsActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        UmDataStatisticsComponent.Builder view(UmDataStatisticsContract.View view);

        UmDataStatisticsComponent.Builder appComponent(AppComponent appComponent);

        UmDataStatisticsComponent build();
    }
}