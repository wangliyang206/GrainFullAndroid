package com.zqw.mobile.grainfull.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.di.module.LayoutMianModule;
import com.zqw.mobile.grainfull.mvp.contract.LayoutMianContract;
import com.zqw.mobile.grainfull.mvp.ui.fragment.LayoutMianFragment;

import dagger.BindsInstance;
import dagger.Component;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/07/05 11:06
 * ================================================
 */
@ActivityScope
@Component(modules = LayoutMianModule.class, dependencies = AppComponent.class)
public interface LayoutMianComponent {
    void inject(LayoutMianFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LayoutMianComponent.Builder view(LayoutMianContract.View view);

        LayoutMianComponent.Builder appComponent(AppComponent appComponent);

        LayoutMianComponent build();
    }
}