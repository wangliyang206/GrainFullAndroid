package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.LevitationButtonModule;
import com.zqw.mobile.grainfull.mvp.contract.LevitationButtonContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.LevitationButtonActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/03 16:06
 * ================================================
 */
@ActivityScope
@Component(modules = LevitationButtonModule.class, dependencies = AppComponent.class)
public interface LevitationButtonComponent {

    void inject(LevitationButtonActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LevitationButtonComponent.Builder view(LevitationButtonContract.View view);

        LevitationButtonComponent.Builder appComponent(AppComponent appComponent);

        LevitationButtonComponent build();
    }
}