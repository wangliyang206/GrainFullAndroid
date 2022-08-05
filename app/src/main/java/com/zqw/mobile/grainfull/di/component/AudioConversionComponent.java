package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.AudioConversionModule;
import com.zqw.mobile.grainfull.mvp.contract.AudioConversionContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.AudioConversionActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/08/05 14:46
 * ================================================
 */
@ActivityScope
@Component(modules = AudioConversionModule.class, dependencies = AppComponent.class)
public interface AudioConversionComponent {

    void inject(AudioConversionActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        AudioConversionComponent.Builder view(AudioConversionContract.View view);

        AudioConversionComponent.Builder appComponent(AppComponent appComponent);

        AudioConversionComponent build();
    }
}