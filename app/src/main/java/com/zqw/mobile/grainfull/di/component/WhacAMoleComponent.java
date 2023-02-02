package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.WhacAMoleModule;
import com.zqw.mobile.grainfull.mvp.contract.WhacAMoleContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.WhacAMoleActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/02 09:20
 * ================================================
 */
@ActivityScope
@Component(modules = WhacAMoleModule.class, dependencies = AppComponent.class)
public interface WhacAMoleComponent {

    void inject(WhacAMoleActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        WhacAMoleComponent.Builder view(WhacAMoleContract.View view);

        WhacAMoleComponent.Builder appComponent(AppComponent appComponent);

        WhacAMoleComponent build();
    }
}