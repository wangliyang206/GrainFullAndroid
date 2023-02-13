package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.zqw.mobile.grainfull.di.module.AboutModule;
import com.zqw.mobile.grainfull.mvp.contract.AboutContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.AboutActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/28/2020 11:19
 * ================================================
 */
@ActivityScope
@Component(modules = AboutModule.class, dependencies = AppComponent.class)
public interface AboutComponent {
    void inject(AboutActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        AboutComponent.Builder view(AboutContract.View view);

        AboutComponent.Builder appComponent(AppComponent appComponent);

        AboutComponent build();
    }
}