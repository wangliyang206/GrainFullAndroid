package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.HmsCropImageModule;
import com.zqw.mobile.grainfull.mvp.contract.HmsCropImageContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.HmsCropImageActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/12/09 10:18
 * ================================================
 */
@ActivityScope
@Component(modules = HmsCropImageModule.class, dependencies = AppComponent.class)
public interface HmsCropImageComponent {

    void inject(HmsCropImageActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        HmsCropImageComponent.Builder view(HmsCropImageContract.View view);

        HmsCropImageComponent.Builder appComponent(AppComponent appComponent);

        HmsCropImageComponent build();
    }
}