package com.zqw.mobile.grainfull.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.di.module.SettingModule;
import com.zqw.mobile.grainfull.mvp.contract.SettingContract;
import com.zqw.mobile.grainfull.mvp.ui.activity.SettingActivity;

import dagger.BindsInstance;
import dagger.Component;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/28/2020 13:17
 * ================================================
 */
@ActivityScope
@Component(modules = SettingModule.class, dependencies = AppComponent.class)
public interface SettingComponent {
    void inject(SettingActivity fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SettingComponent.Builder view(SettingContract.View view);

        SettingComponent.Builder appComponent(AppComponent appComponent);

        SettingComponent build();
    }
}