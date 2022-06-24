package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.zqw.mobile.grainfull.mvp.contract.IdentifyBankCardsContract;
import com.zqw.mobile.grainfull.mvp.model.IdentifyBankCardsModel;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/23/2022 19:34
 * ================================================
 */
@Module
public abstract class IdentifyBankCardsModule {

    @Binds
    abstract IdentifyBankCardsContract.Model bindIdentifyBankCardsModel(IdentifyBankCardsModel model);
}