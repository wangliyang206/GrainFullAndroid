package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.ProductDisplayModule;
import com.zqw.mobile.grainfull.mvp.contract.ProductDisplayContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.ProductDisplayActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/08/14 17:03
 * ================================================
 */
@ActivityScope
@Component(modules = ProductDisplayModule.class, dependencies = AppComponent.class)
public interface ProductDisplayComponent {

    void inject(ProductDisplayActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ProductDisplayComponent.Builder view(ProductDisplayContract.View view);

        ProductDisplayComponent.Builder appComponent(AppComponent appComponent);

        ProductDisplayComponent build();
    }
}