package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.BaiduFaceSettingModule;
import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceSettingContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduFaceSettingActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/12 12:30
 * ================================================
 */
@ActivityScope
@Component(modules = BaiduFaceSettingModule.class, dependencies = AppComponent.class)
public interface BaiduFaceSettingComponent {

    void inject(BaiduFaceSettingActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BaiduFaceSettingComponent.Builder view(BaiduFaceSettingContract.View view);

        BaiduFaceSettingComponent.Builder appComponent(AppComponent appComponent);

        BaiduFaceSettingComponent build();
    }
}