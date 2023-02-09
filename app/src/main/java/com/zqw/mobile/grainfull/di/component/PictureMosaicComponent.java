package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.PictureMosaicModule;
import com.zqw.mobile.grainfull.mvp.contract.PictureMosaicContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.PictureMosaicActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/09 15:00
 * ================================================
 */
@ActivityScope
@Component(modules = PictureMosaicModule.class, dependencies = AppComponent.class)
public interface PictureMosaicComponent {

    void inject(PictureMosaicActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PictureMosaicComponent.Builder view(PictureMosaicContract.View view);

        PictureMosaicComponent.Builder appComponent(AppComponent appComponent);

        PictureMosaicComponent build();
    }
}