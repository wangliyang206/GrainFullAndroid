package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.GoodsDetailModule;
import com.zqw.mobile.grainfull.mvp.contract.GoodsDetailContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.GoodsDetailActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2025/02/18 11:35
 * ================================================
 */
@ActivityScope
@Component(modules = GoodsDetailModule.class, dependencies = AppComponent.class)
public interface GoodsDetailComponent {

    void inject(GoodsDetailActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        GoodsDetailComponent.Builder view(GoodsDetailContract.View view);

        GoodsDetailComponent.Builder appComponent(AppComponent appComponent);

        GoodsDetailComponent build();
    }
}