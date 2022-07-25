package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.BaiduVoiceRecogModule;
import com.zqw.mobile.grainfull.mvp.contract.BaiduVoiceRecogContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduVoiceRecogActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/25 17:21
 * ================================================
 */
@ActivityScope
@Component(modules = BaiduVoiceRecogModule.class, dependencies = AppComponent.class)
public interface BaiduVoiceRecogComponent {

    void inject(BaiduVoiceRecogActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BaiduVoiceRecogComponent.Builder view(BaiduVoiceRecogContract.View view);

        BaiduVoiceRecogComponent.Builder appComponent(AppComponent appComponent);

        BaiduVoiceRecogComponent build();
    }
}