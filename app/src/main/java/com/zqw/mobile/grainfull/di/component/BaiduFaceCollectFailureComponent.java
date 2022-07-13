package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.BaiduFaceCollectFailureModule;
import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceCollectFailureContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduFaceCollectFailureActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/12 12:30
 * ================================================
 */
@ActivityScope
@Component(modules = BaiduFaceCollectFailureModule.class, dependencies = AppComponent.class)
public interface BaiduFaceCollectFailureComponent {

    void inject(BaiduFaceCollectFailureActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BaiduFaceCollectFailureComponent.Builder view(BaiduFaceCollectFailureContract.View view);

        BaiduFaceCollectFailureComponent.Builder appComponent(AppComponent appComponent);

        BaiduFaceCollectFailureComponent build();
    }
}