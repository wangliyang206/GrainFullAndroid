package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.zqw.mobile.grainfull.di.module.IdentifyIdCardModule;
import com.zqw.mobile.grainfull.mvp.contract.IdentifyIdCardContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.IdentifyIdCardActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/24/2022 11:00
 * ================================================
 */
@ActivityScope
@Component(modules = IdentifyIdCardModule.class, dependencies = AppComponent.class)
public interface IdentifyIdCardComponent {
    void inject(IdentifyIdCardActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        IdentifyIdCardComponent.Builder view(IdentifyIdCardContract.View view);
        IdentifyIdCardComponent.Builder appComponent(AppComponent appComponent);
        IdentifyIdCardComponent build();
    }
}