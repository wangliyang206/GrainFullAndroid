package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.BaiduFaceCollectionSuccessModule;
import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceCollectionSuccessContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduFaceCollectionSuccessActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/12 12:30
 * ================================================
 */
@ActivityScope
@Component(modules = BaiduFaceCollectionSuccessModule.class, dependencies = AppComponent.class)
public interface BaiduFaceCollectionSuccessComponent {

    void inject(BaiduFaceCollectionSuccessActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BaiduFaceCollectionSuccessComponent.Builder view(BaiduFaceCollectionSuccessContract.View view);

        BaiduFaceCollectionSuccessComponent.Builder appComponent(AppComponent appComponent);

        BaiduFaceCollectionSuccessComponent build();
    }
}