package com.zqw.mobile.grainfull.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.di.module.LayoutForumModule;
import com.zqw.mobile.grainfull.mvp.contract.LayoutForumContract;
import com.zqw.mobile.grainfull.mvp.ui.fragment.GridFragment;
import com.zqw.mobile.grainfull.mvp.ui.fragment.LayoutForumFragment;

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
@Component(modules = LayoutForumModule.class, dependencies = AppComponent.class)
public interface LayoutForumComponent {
    void inject(LayoutForumFragment fragment);
    void inject(GridFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LayoutForumComponent.Builder view(LayoutForumContract.View view);

        LayoutForumComponent.Builder appComponent(AppComponent appComponent);

        LayoutForumComponent build();
    }
}