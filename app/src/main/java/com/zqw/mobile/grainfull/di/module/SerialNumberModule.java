package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.SerialNumberContract;
import com.zqw.mobile.grainfull.mvp.model.SerialNumberModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/14 17:11
 * ================================================
 */
@Module
//构建SerialNumberModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class SerialNumberModule {

    @Binds
    abstract SerialNumberContract.Model bindSerialNumberModel(SerialNumberModel model);
}