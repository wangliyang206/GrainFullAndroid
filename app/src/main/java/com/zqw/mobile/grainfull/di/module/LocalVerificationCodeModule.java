package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.LocalVerificationCodeContract;
import com.zqw.mobile.grainfull.mvp.model.LocalVerificationCodeModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/03/07 15:35
 * ================================================
 */
@Module
//构建LocalVerificationCodeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class LocalVerificationCodeModule {

    @Binds
    abstract LocalVerificationCodeContract.Model bindLocalVerificationCodeModel(LocalVerificationCodeModel model);
}