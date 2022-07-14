package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.BaiduQualityParamsModule;
import com.zqw.mobile.grainfull.mvp.contract.BaiduQualityParamsContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduQualityParamsActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/12 12:30
 * ================================================
 */
@ActivityScope
@Component(modules = BaiduQualityParamsModule.class, dependencies = AppComponent.class)
public interface BaiduQualityParamsComponent {

    void inject(BaiduQualityParamsActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BaiduQualityParamsComponent.Builder view(BaiduQualityParamsContract.View view);

        BaiduQualityParamsComponent.Builder appComponent(AppComponent appComponent);

        BaiduQualityParamsComponent build();
    }
}