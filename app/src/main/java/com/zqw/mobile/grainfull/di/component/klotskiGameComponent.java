package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.klotskiGameModule;
import com.zqw.mobile.grainfull.mvp.contract.klotskiGameContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.klotskiGameActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/20 18:00
 * ================================================
 */
@ActivityScope
@Component(modules = klotskiGameModule.class, dependencies = AppComponent.class)
public interface klotskiGameComponent {

    void inject(klotskiGameActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        klotskiGameComponent.Builder view(klotskiGameContract.View view);

        klotskiGameComponent.Builder appComponent(AppComponent appComponent);

        klotskiGameComponent build();
    }
}