package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.TranslateContract;
import com.zqw.mobile.grainfull.mvp.model.TranslateModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/06/29 09:30
 * ================================================
 */
@Module
//构建TranslateModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class TranslateModule {

    @Binds
    abstract TranslateContract.Model bindTranslateModel(TranslateModel model);
}