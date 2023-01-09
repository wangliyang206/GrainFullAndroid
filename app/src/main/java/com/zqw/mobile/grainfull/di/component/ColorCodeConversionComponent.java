package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.ColorCodeConversionModule;
import com.zqw.mobile.grainfull.mvp.contract.ColorCodeConversionContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.ColorCodeConversionActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/09 16:19
 * ================================================
 */
@ActivityScope
@Component(modules = ColorCodeConversionModule.class, dependencies = AppComponent.class)
public interface ColorCodeConversionComponent {

    void inject(ColorCodeConversionActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ColorCodeConversionComponent.Builder view(ColorCodeConversionContract.View view);

        ColorCodeConversionComponent.Builder appComponent(AppComponent appComponent);

        ColorCodeConversionComponent build();
    }
}