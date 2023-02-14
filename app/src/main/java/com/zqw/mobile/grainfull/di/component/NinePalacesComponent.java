package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.NinePalacesModule;
import com.zqw.mobile.grainfull.mvp.contract.NinePalacesContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.NinePalacesActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/13 16:37
 * ================================================
 */
@ActivityScope
@Component(modules = NinePalacesModule.class, dependencies = AppComponent.class)
public interface NinePalacesComponent {

    void inject(NinePalacesActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        NinePalacesComponent.Builder view(NinePalacesContract.View view);

        NinePalacesComponent.Builder appComponent(AppComponent appComponent);

        NinePalacesComponent build();
    }
}