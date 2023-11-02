package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.ARFaceChangingModule;
import com.zqw.mobile.grainfull.mvp.contract.ARFaceChangingContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.ARFaceChangingActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/11/02 17:40
 * ================================================
 */
@ActivityScope
@Component(modules = ARFaceChangingModule.class, dependencies = AppComponent.class)
public interface ARFaceChangingComponent {

    void inject(ARFaceChangingActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ARFaceChangingComponent.Builder view(ARFaceChangingContract.View view);

        ARFaceChangingComponent.Builder appComponent(AppComponent appComponent);

        ARFaceChangingComponent build();
    }
}