package com.zqw.mobile.grainfull.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.di.module.LayoutCategoryModule;
import com.zqw.mobile.grainfull.mvp.contract.LayoutCategoryContract;
import com.zqw.mobile.grainfull.mvp.ui.fragment.LayoutCategoryFragment;

import dagger.BindsInstance;
import dagger.Component;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/09/04 12:01
 * ================================================
 */
@ActivityScope
@Component(modules = LayoutCategoryModule.class, dependencies = AppComponent.class)
public interface LayoutCategoryComponent {

    void inject(LayoutCategoryFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LayoutCategoryComponent.Builder view(LayoutCategoryContract.View view);

        LayoutCategoryComponent.Builder appComponent(AppComponent appComponent);

        LayoutCategoryComponent build();
    }
}