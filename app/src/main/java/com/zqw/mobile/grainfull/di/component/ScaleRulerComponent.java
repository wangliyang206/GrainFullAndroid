package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.ScaleRulerModule;
import com.zqw.mobile.grainfull.mvp.contract.ScaleRulerContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.ScaleRulerActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/20 17:44
 * ================================================
 */
@ActivityScope
@Component(modules = ScaleRulerModule.class, dependencies = AppComponent.class)
public interface ScaleRulerComponent {

    void inject(ScaleRulerActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ScaleRulerComponent.Builder view(ScaleRulerContract.View view);

        ScaleRulerComponent.Builder appComponent(AppComponent appComponent);

        ScaleRulerComponent build();
    }
}