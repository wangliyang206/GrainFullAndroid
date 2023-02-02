package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.OneLineToEndModule;
import com.zqw.mobile.grainfull.mvp.contract.OneLineToEndContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.OneLineToEndActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/01 14:15
 * ================================================
 */
@ActivityScope
@Component(modules = OneLineToEndModule.class, dependencies = AppComponent.class)
public interface OneLineToEndComponent {

    void inject(OneLineToEndActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        OneLineToEndComponent.Builder view(OneLineToEndContract.View view);

        OneLineToEndComponent.Builder appComponent(AppComponent appComponent);

        OneLineToEndComponent build();
    }
}