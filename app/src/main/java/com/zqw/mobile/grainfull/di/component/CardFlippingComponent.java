package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.CardFlippingModule;
import com.zqw.mobile.grainfull.mvp.contract.CardFlippingContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.CardFlippingActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/07/10 17:00
 * ================================================
 */
@ActivityScope
@Component(modules = CardFlippingModule.class, dependencies = AppComponent.class)
public interface CardFlippingComponent {

    void inject(CardFlippingActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        CardFlippingComponent.Builder view(CardFlippingContract.View view);

        CardFlippingComponent.Builder appComponent(AppComponent appComponent);

        CardFlippingComponent build();
    }
}