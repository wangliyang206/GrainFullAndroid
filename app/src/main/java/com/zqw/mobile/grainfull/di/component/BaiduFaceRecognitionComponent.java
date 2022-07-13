package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.BaiduFaceRecognitionModule;
import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceRecognitionContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduFaceRecognitionActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/12 12:30
 * ================================================
 */
@ActivityScope
@Component(modules = BaiduFaceRecognitionModule.class, dependencies = AppComponent.class)
public interface BaiduFaceRecognitionComponent {

    void inject(BaiduFaceRecognitionActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BaiduFaceRecognitionComponent.Builder view(BaiduFaceRecognitionContract.View view);

        BaiduFaceRecognitionComponent.Builder appComponent(AppComponent appComponent);

        BaiduFaceRecognitionComponent build();
    }
}