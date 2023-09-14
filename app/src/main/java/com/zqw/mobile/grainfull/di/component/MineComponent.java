package com.zqw.mobile.grainfull.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.di.module.MineModule;
import com.zqw.mobile.grainfull.mvp.contract.MineContract;
import com.zqw.mobile.grainfull.mvp.ui.fragment.MineFragment;

import dagger.BindsInstance;
import dagger.Component;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/09/05 17:34
 * ================================================
 */
@ActivityScope
@Component(modules = MineModule.class, dependencies = AppComponent.class)
public interface MineComponent {

    void inject(MineFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        MineComponent.Builder view(MineContract.View view);

        MineComponent.Builder appComponent(AppComponent appComponent);

        MineComponent build();
    }
}