package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.ChineseToPinyinModule;
import com.zqw.mobile.grainfull.mvp.contract.ChineseToPinyinContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.ChineseToPinyinActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/10 16:38
 * ================================================
 */
@ActivityScope
@Component(modules = ChineseToPinyinModule.class, dependencies = AppComponent.class)
public interface ChineseToPinyinComponent {

    void inject(ChineseToPinyinActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ChineseToPinyinComponent.Builder view(ChineseToPinyinContract.View view);

        ChineseToPinyinComponent.Builder appComponent(AppComponent appComponent);

        ChineseToPinyinComponent build();
    }
}