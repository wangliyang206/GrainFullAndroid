package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.PicturePipetteModule;
import com.zqw.mobile.grainfull.mvp.contract.PicturePipetteContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.PicturePipetteActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/03 16:06
 * ================================================
 */
@ActivityScope
@Component(modules = PicturePipetteModule.class, dependencies = AppComponent.class)
public interface PicturePipetteComponent {

    void inject(PicturePipetteActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PicturePipetteComponent.Builder view(PicturePipetteContract.View view);

        PicturePipetteComponent.Builder appComponent(AppComponent appComponent);

        PicturePipetteComponent build();
    }
}