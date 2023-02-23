package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.ElectronicClockModule;
import com.zqw.mobile.grainfull.mvp.contract.ElectronicClockContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.ElectronicClockActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/20 17:44
 * ================================================
 */
@ActivityScope
@Component(modules = ElectronicClockModule.class, dependencies = AppComponent.class)
public interface ElectronicClockComponent {

    void inject(ElectronicClockActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ElectronicClockComponent.Builder view(ElectronicClockContract.View view);

        ElectronicClockComponent.Builder appComponent(AppComponent appComponent);

        ElectronicClockComponent build();
    }
}