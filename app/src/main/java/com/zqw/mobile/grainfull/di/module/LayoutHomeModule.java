package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.LayoutHomeContract;
import com.zqw.mobile.grainfull.mvp.model.LayoutHomeModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/07/05 11:06
 * ================================================
 */
@Module
//构建LayoutHomeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class LayoutHomeModule {

    @Binds
    abstract LayoutHomeContract.Model bindLayoutHomeModel(LayoutHomeModel model);
}