package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.ScoreboardModule;
import com.zqw.mobile.grainfull.mvp.contract.ScoreboardContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.ScoreboardActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/20 17:44
 * ================================================
 */
@ActivityScope
@Component(modules = ScoreboardModule.class, dependencies = AppComponent.class)
public interface ScoreboardComponent {

    void inject(ScoreboardActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ScoreboardComponent.Builder view(ScoreboardContract.View view);

        ScoreboardComponent.Builder appComponent(AppComponent appComponent);

        ScoreboardComponent build();
    }
}