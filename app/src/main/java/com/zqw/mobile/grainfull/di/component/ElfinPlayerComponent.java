package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.ElfinPlayerModule;
import com.zqw.mobile.grainfull.mvp.contract.ElfinPlayerContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.ElfinPlayerActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/10/13 15:54
 * ================================================
 */
@ActivityScope
@Component(modules = ElfinPlayerModule.class, dependencies = AppComponent.class)
public interface ElfinPlayerComponent {

    void inject(ElfinPlayerActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ElfinPlayerComponent.Builder view(ElfinPlayerContract.View view);

        ElfinPlayerComponent.Builder appComponent(AppComponent appComponent);

        ElfinPlayerComponent build();
    }
}