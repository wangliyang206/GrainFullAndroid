package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.MagnifierModule;
import com.zqw.mobile.grainfull.mvp.contract.MagnifierContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.MagnifierActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/03 16:06
 * ================================================
 */
@ActivityScope
@Component(modules = MagnifierModule.class, dependencies = AppComponent.class)
public interface MagnifierComponent {

    void inject(MagnifierActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        MagnifierComponent.Builder view(MagnifierContract.View view);

        MagnifierComponent.Builder appComponent(AppComponent appComponent);

        MagnifierComponent build();
    }
}