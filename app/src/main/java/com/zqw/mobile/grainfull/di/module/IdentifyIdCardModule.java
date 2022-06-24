package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.zqw.mobile.grainfull.mvp.contract.IdentifyIdCardContract;
import com.zqw.mobile.grainfull.mvp.model.IdentifyIdCardModel;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/24/2022 11:00
 * ================================================
 */
@Module
public abstract class IdentifyIdCardModule {

    @Binds
    abstract IdentifyIdCardContract.Model bindIdentifyIdCardModel(IdentifyIdCardModel model);
}