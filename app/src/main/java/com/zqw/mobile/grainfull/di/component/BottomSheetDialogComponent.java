package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.BottomSheetDialogModule;
import com.zqw.mobile.grainfull.mvp.contract.BottomSheetDialogContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.BottomSheetDialogActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/03/29 10:16
 * ================================================
 */
@ActivityScope
@Component(modules = BottomSheetDialogModule.class, dependencies = AppComponent.class)
public interface BottomSheetDialogComponent {

    void inject(BottomSheetDialogActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BottomSheetDialogComponent.Builder view(BottomSheetDialogContract.View view);

        BottomSheetDialogComponent.Builder appComponent(AppComponent appComponent);

        BottomSheetDialogComponent build();
    }
}