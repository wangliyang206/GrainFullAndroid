package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.zqw.mobile.grainfull.di.module.IdentifyBankCardsModule;
import com.zqw.mobile.grainfull.mvp.contract.IdentifyBankCardsContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.IdentifyBankCardsActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/23/2022 19:34
 * ================================================
 */
@ActivityScope
@Component(modules = IdentifyBankCardsModule.class, dependencies = AppComponent.class)
public interface IdentifyBankCardsComponent {
    void inject(IdentifyBankCardsActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        IdentifyBankCardsComponent.Builder view(IdentifyBankCardsContract.View view);

        IdentifyBankCardsComponent.Builder appComponent(AppComponent appComponent);

        IdentifyBankCardsComponent build();
    }
}