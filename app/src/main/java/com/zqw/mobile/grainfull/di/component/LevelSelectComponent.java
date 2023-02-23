package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.LevelSelectModule;
import com.zqw.mobile.grainfull.mvp.contract.LevelSelectContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.LevelSelectActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/20 17:44
 * ================================================
 */
@ActivityScope
@Component(modules = LevelSelectModule.class, dependencies = AppComponent.class)
public interface LevelSelectComponent {

    void inject(LevelSelectActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LevelSelectComponent.Builder view(LevelSelectContract.View view);

        LevelSelectComponent.Builder appComponent(AppComponent appComponent);

        LevelSelectComponent build();
    }
}