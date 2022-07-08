package com.zqw.mobile.grainfull.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.di.module.ImageExtractionTextModule;
import com.zqw.mobile.grainfull.mvp.contract.ImageExtractionTextContract;
import com.zqw.mobile.grainfull.mvp.ui.activity.ImageExtractionTextActivity;

import dagger.BindsInstance;
import dagger.Component;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/08 16:11
 * ================================================
 */
@ActivityScope
@Component(modules = ImageExtractionTextModule.class, dependencies = AppComponent.class)
public interface ImageExtractionTextComponent {

    void inject(ImageExtractionTextActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ImageExtractionTextComponent.Builder view(ImageExtractionTextContract.View view);

        ImageExtractionTextComponent.Builder appComponent(AppComponent appComponent);

        ImageExtractionTextComponent build();
    }

}