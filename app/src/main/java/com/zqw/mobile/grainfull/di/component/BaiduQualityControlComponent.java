package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.BaiduQualityControlModule;
import com.zqw.mobile.grainfull.mvp.contract.BaiduQualityControlContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduQualityControlActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/12 12:30
 * ================================================
 */
@ActivityScope
@Component(modules = BaiduQualityControlModule.class, dependencies = AppComponent.class)
public interface BaiduQualityControlComponent {

    void inject(BaiduQualityControlActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BaiduQualityControlComponent.Builder view(BaiduQualityControlContract.View view);

        BaiduQualityControlComponent.Builder appComponent(AppComponent appComponent);

        BaiduQualityControlComponent build();
    }
}