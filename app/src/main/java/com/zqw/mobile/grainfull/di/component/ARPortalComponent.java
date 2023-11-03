package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.ARPortalModule;
import com.zqw.mobile.grainfull.mvp.contract.ARPortalContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.ARPortalActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/11/02 17:40
 * ================================================
 */
@ActivityScope
@Component(modules = ARPortalModule.class, dependencies = AppComponent.class)
public interface ARPortalComponent {

    void inject(ARPortalActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ARPortalComponent.Builder view(ARPortalContract.View view);

        ARPortalComponent.Builder appComponent(AppComponent appComponent);

        ARPortalComponent build();
    }
}