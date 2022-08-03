package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.AudioWaveformModule;
import com.zqw.mobile.grainfull.mvp.contract.AudioWaveformContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.AudioWaveformActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/08/03 14:42
 * ================================================
 */
@ActivityScope
@Component(modules = AudioWaveformModule.class, dependencies = AppComponent.class)
public interface AudioWaveformComponent {

    void inject(AudioWaveformActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        AudioWaveformComponent.Builder view(AudioWaveformContract.View view);

        AudioWaveformComponent.Builder appComponent(AppComponent appComponent);

        AudioWaveformComponent build();
    }
}