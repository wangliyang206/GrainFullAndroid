package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.DecisionModule;
import com.zqw.mobile.grainfull.mvp.contract.DecisionContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.DecisionActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/12/27 16:16
 * ================================================
 */
@ActivityScope
@Component(modules = DecisionModule.class, dependencies = AppComponent.class)
public interface DecisionComponent {

    void inject(DecisionActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        DecisionComponent.Builder view(DecisionContract.View view);

        DecisionComponent.Builder appComponent(AppComponent appComponent);

        DecisionComponent build();
    }
}