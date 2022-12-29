package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.HandheldBulletScreenContract;
import com.zqw.mobile.grainfull.mvp.model.HandheldBulletScreenModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/12/29 09:59
 * ================================================
 */
@Module
//构建HandheldBulletScreenModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class HandheldBulletScreenModule {

    @Binds
    abstract HandheldBulletScreenContract.Model bindHandheldBulletScreenModel(HandheldBulletScreenModel model);
}