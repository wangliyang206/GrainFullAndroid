package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.BaiduFaceAgreementModule;
import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceAgreementContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduFaceAgreementActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/12 12:30
 * ================================================
 */
@ActivityScope
@Component(modules = BaiduFaceAgreementModule.class, dependencies = AppComponent.class)
public interface BaiduFaceAgreementComponent {

    void inject(BaiduFaceAgreementActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BaiduFaceAgreementComponent.Builder view(BaiduFaceAgreementContract.View view);

        BaiduFaceAgreementComponent.Builder appComponent(AppComponent appComponent);

        BaiduFaceAgreementComponent build();
    }
}