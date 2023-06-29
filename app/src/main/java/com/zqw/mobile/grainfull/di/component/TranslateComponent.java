package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.TranslateModule;
import com.zqw.mobile.grainfull.mvp.contract.TranslateContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.TranslateActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/06/29 09:30
 * ================================================
 */
@ActivityScope
@Component(modules = TranslateModule.class, dependencies = AppComponent.class)
public interface TranslateComponent {

    void inject(TranslateActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        TranslateComponent.Builder view(TranslateContract.View view);

        TranslateComponent.Builder appComponent(AppComponent appComponent);

        TranslateComponent build();
    }
}