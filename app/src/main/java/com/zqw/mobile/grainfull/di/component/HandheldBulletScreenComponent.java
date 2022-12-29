package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.HandheldBulletScreenModule;
import com.zqw.mobile.grainfull.mvp.contract.HandheldBulletScreenContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.HandheldBulletScreenActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/12/29 09:59
 * ================================================
 */
@ActivityScope
@Component(modules = HandheldBulletScreenModule.class, dependencies = AppComponent.class)
public interface HandheldBulletScreenComponent {

    void inject(HandheldBulletScreenActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        HandheldBulletScreenComponent.Builder view(HandheldBulletScreenContract.View view);

        HandheldBulletScreenComponent.Builder appComponent(AppComponent appComponent);

        HandheldBulletScreenComponent build();
    }
}