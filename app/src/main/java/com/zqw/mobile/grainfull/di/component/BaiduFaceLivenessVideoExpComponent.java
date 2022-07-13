package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.BaiduFaceLivenessVideoExpModule;
import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceLivenessVideoExpContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduFaceLivenessVideoExpActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/12 12:30
 * ================================================
 */
@ActivityScope
@Component(modules = BaiduFaceLivenessVideoExpModule.class, dependencies = AppComponent.class)
public interface BaiduFaceLivenessVideoExpComponent {

    void inject(BaiduFaceLivenessVideoExpActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BaiduFaceLivenessVideoExpComponent.Builder view(BaiduFaceLivenessVideoExpContract.View view);

        BaiduFaceLivenessVideoExpComponent.Builder appComponent(AppComponent appComponent);

        BaiduFaceLivenessVideoExpComponent build();
    }
}