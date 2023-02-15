package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.PictureCompressionModule;
import com.zqw.mobile.grainfull.mvp.contract.PictureCompressionContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.PictureCompressionActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/15 13:51
 * ================================================
 */
@ActivityScope
@Component(modules = PictureCompressionModule.class, dependencies = AppComponent.class)
public interface PictureCompressionComponent {

    void inject(PictureCompressionActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PictureCompressionComponent.Builder view(PictureCompressionContract.View view);

        PictureCompressionComponent.Builder appComponent(AppComponent appComponent);

        PictureCompressionComponent build();
    }
}