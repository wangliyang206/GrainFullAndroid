package com.zqw.mobile.grainfull.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.di.module.LayoutHomeModule;
import com.zqw.mobile.grainfull.mvp.contract.LayoutHomeContract;
import com.zqw.mobile.grainfull.mvp.ui.activity.LayoutHomeActivity;
import com.zqw.mobile.grainfull.mvp.ui.fragment.LayoutMianFragment;
import com.zqw.mobile.grainfull.mvp.ui.fragment.LayoutOtherFragment;

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
@Component(modules = LayoutHomeModule.class, dependencies = AppComponent.class)
public interface LayoutHomeComponent {

    void inject(LayoutHomeActivity activity);

    void inject(LayoutMianFragment fragment);

    void inject(LayoutOtherFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LayoutHomeComponent.Builder view(LayoutHomeContract.View view);

        LayoutHomeComponent.Builder appComponent(AppComponent appComponent);

        LayoutHomeComponent build();
    }
}