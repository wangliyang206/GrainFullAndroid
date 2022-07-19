package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.FaceComparisonModule;
import com.zqw.mobile.grainfull.mvp.contract.FaceComparisonContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.FaceComparisonActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/19 14:20
 * ================================================
 */
@ActivityScope
@Component(modules = FaceComparisonModule.class, dependencies = AppComponent.class)
public interface FaceComparisonComponent {

    void inject(FaceComparisonActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        FaceComparisonComponent.Builder view(FaceComparisonContract.View view);

        FaceComparisonComponent.Builder appComponent(AppComponent appComponent);

        FaceComparisonComponent build();
    }
}