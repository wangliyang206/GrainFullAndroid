package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.HmsPhotoFilterModule;
import com.zqw.mobile.grainfull.mvp.contract.HmsPhotoFilterContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.HmsPhotoFilterActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/12/05 15:44
 * ================================================
 */
@ActivityScope
@Component(modules = HmsPhotoFilterModule.class, dependencies = AppComponent.class)
public interface HmsPhotoFilterComponent {

    void inject(HmsPhotoFilterActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        HmsPhotoFilterComponent.Builder view(HmsPhotoFilterContract.View view);

        HmsPhotoFilterComponent.Builder appComponent(AppComponent appComponent);

        HmsPhotoFilterComponent build();
    }
}