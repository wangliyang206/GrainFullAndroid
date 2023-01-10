package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.DigitalToChineseModule;
import com.zqw.mobile.grainfull.mvp.contract.DigitalToChineseContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.DigitalToChineseActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/10 16:38
 * ================================================
 */
@ActivityScope
@Component(modules = DigitalToChineseModule.class, dependencies = AppComponent.class)
public interface DigitalToChineseComponent {

    void inject(DigitalToChineseActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        DigitalToChineseComponent.Builder view(DigitalToChineseContract.View view);

        DigitalToChineseComponent.Builder appComponent(AppComponent appComponent);

        DigitalToChineseComponent build();
    }
}