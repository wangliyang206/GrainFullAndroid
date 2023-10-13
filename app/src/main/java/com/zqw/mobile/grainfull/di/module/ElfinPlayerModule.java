package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.ElfinPlayerContract;
import com.zqw.mobile.grainfull.mvp.model.ElfinPlayerModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/10/13 15:54
 * ================================================
 */
@Module
//构建ElfinPlayerModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class ElfinPlayerModule {

    @Binds
    abstract ElfinPlayerContract.Model bindElfinPlayerModel(ElfinPlayerModel model);
}