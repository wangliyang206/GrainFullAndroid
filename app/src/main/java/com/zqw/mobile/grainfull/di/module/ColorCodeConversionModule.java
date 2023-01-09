package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.ColorCodeConversionContract;
import com.zqw.mobile.grainfull.mvp.model.ColorCodeConversionModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/09 16:19
 * ================================================
 */
@Module
//构建ColorCodeConversionModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class ColorCodeConversionModule {

    @Binds
    abstract ColorCodeConversionContract.Model bindColorCodeConversionModel(ColorCodeConversionModel model);
}