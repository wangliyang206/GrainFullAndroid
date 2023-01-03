package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.ColorPickerModule;
import com.zqw.mobile.grainfull.mvp.contract.ColorPickerContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.ColorPickerActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/03 16:06
 * ================================================
 */
@ActivityScope
@Component(modules = ColorPickerModule.class, dependencies = AppComponent.class)
public interface ColorPickerComponent {

    void inject(ColorPickerActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ColorPickerComponent.Builder view(ColorPickerContract.View view);

        ColorPickerComponent.Builder appComponent(AppComponent appComponent);

        ColorPickerComponent build();
    }
}