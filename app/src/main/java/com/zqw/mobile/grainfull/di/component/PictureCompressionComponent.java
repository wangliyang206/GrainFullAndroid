package com.zqw.mobile.grainfull.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.di.module.PictureCompressionModule;
import com.zqw.mobile.grainfull.mvp.contract.PictureCompressionContract;
import com.zqw.mobile.grainfull.mvp.ui.activity.PictureCompressionActivity;
import com.zqw.mobile.grainfull.mvp.ui.fragment.CompressPicScaleFragment;
import com.zqw.mobile.grainfull.mvp.ui.fragment.CompressPicSizeFragment;

import dagger.BindsInstance;
import dagger.Component;

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
    void inject(CompressPicSizeFragment fragment);
    void inject(CompressPicScaleFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PictureCompressionComponent.Builder view(PictureCompressionContract.View view);

        PictureCompressionComponent.Builder appComponent(AppComponent appComponent);

        PictureCompressionComponent build();
    }
}