package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.RemoveWatermarkinModule;
import com.zqw.mobile.grainfull.mvp.contract.RemoveWatermarkinContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.RemoveWatermarkinActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/07/05 11:06
 * ================================================
 */
@ActivityScope
@Component(modules = RemoveWatermarkinModule.class, dependencies = AppComponent.class)
public interface RemoveWatermarkinComponent {

    void inject(RemoveWatermarkinActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        RemoveWatermarkinComponent.Builder view(RemoveWatermarkinContract.View view);

        RemoveWatermarkinComponent.Builder appComponent(AppComponent appComponent);

        RemoveWatermarkinComponent build();
    }
}