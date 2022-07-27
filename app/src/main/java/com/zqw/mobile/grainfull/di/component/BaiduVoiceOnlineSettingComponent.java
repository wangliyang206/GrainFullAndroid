package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.BaiduVoiceOnlineSettingModule;
import com.zqw.mobile.grainfull.mvp.contract.BaiduVoiceOnlineSettingContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduVoiceOnlineSettingActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/26 18:34
 * ================================================
 */
@ActivityScope
@Component(modules = BaiduVoiceOnlineSettingModule.class, dependencies = AppComponent.class)
public interface BaiduVoiceOnlineSettingComponent {

    void inject(BaiduVoiceOnlineSettingActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BaiduVoiceOnlineSettingComponent.Builder view(BaiduVoiceOnlineSettingContract.View view);

        BaiduVoiceOnlineSettingComponent.Builder appComponent(AppComponent appComponent);

        BaiduVoiceOnlineSettingComponent build();
    }
}