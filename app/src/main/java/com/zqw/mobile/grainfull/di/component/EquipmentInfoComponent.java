package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.EquipmentInfoModule;
import com.zqw.mobile.grainfull.mvp.contract.EquipmentInfoContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.EquipmentInfoActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/12 09:54
 * ================================================
 */
@ActivityScope
@Component(modules = EquipmentInfoModule.class, dependencies = AppComponent.class)
public interface EquipmentInfoComponent {

    void inject(EquipmentInfoActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        EquipmentInfoComponent.Builder view(EquipmentInfoContract.View view);

        EquipmentInfoComponent.Builder appComponent(AppComponent appComponent);

        EquipmentInfoComponent build();
    }
}