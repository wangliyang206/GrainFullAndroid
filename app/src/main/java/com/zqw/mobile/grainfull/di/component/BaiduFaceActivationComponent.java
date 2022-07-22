package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.BaiduFaceActivationModule;
import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceActivationContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduFaceActivationActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/22 18:29
 * ================================================
 */
@ActivityScope
@Component(modules = BaiduFaceActivationModule.class, dependencies = AppComponent.class)
public interface BaiduFaceActivationComponent {

    void inject(BaiduFaceActivationActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BaiduFaceActivationComponent.Builder view(BaiduFaceActivationContract.View view);

        BaiduFaceActivationComponent.Builder appComponent(AppComponent appComponent);

        BaiduFaceActivationComponent build();
    }
}