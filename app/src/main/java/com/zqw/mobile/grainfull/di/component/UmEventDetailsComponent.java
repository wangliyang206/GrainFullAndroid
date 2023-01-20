package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.UmEventDetailsModule;
import com.zqw.mobile.grainfull.mvp.contract.UmEventDetailsContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.UmEventDetailsActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/20 14:09
 * ================================================
 */
@ActivityScope
@Component(modules = UmEventDetailsModule.class, dependencies = AppComponent.class)
public interface UmEventDetailsComponent {

    void inject(UmEventDetailsActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        UmEventDetailsComponent.Builder view(UmEventDetailsContract.View view);

        UmEventDetailsComponent.Builder appComponent(AppComponent appComponent);

        UmEventDetailsComponent build();
    }
}