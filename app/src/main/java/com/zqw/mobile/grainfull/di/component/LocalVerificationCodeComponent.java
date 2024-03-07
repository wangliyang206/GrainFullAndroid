package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.LocalVerificationCodeModule;
import com.zqw.mobile.grainfull.mvp.contract.LocalVerificationCodeContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.LocalVerificationCodeActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/03/07 15:35
 * ================================================
 */
@ActivityScope
@Component(modules = LocalVerificationCodeModule.class, dependencies = AppComponent.class)
public interface LocalVerificationCodeComponent {

    void inject(LocalVerificationCodeActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LocalVerificationCodeComponent.Builder view(LocalVerificationCodeContract.View view);

        LocalVerificationCodeComponent.Builder appComponent(AppComponent appComponent);

        LocalVerificationCodeComponent build();
    }
}