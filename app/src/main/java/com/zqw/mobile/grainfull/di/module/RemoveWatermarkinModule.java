package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.RemoveWatermarkinContract;
import com.zqw.mobile.grainfull.mvp.model.RemoveWatermarkinModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/07/05 11:06
 * ================================================
 */
@Module
//构建RemoveWatermarkinModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class RemoveWatermarkinModule {

    @Binds
    abstract RemoveWatermarkinContract.Model bindRemoveWatermarkinModel(RemoveWatermarkinModel model);
}