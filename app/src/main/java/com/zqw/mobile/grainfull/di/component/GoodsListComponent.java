package com.zqw.mobile.grainfull.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.di.module.GoodsListModule;
import com.zqw.mobile.grainfull.mvp.contract.GoodsListContract;
import com.zqw.mobile.grainfull.mvp.ui.fragment.GoodsListFragment;

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
@Component(modules = GoodsListModule.class, dependencies = AppComponent.class)
public interface GoodsListComponent {
    void inject(GoodsListFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        GoodsListComponent.Builder view(GoodsListContract.View view);

        GoodsListComponent.Builder appComponent(AppComponent appComponent);

        GoodsListComponent build();
    }
}