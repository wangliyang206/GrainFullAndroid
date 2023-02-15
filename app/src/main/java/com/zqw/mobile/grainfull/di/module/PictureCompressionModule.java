package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.PictureCompressionContract;
import com.zqw.mobile.grainfull.mvp.model.PictureCompressionModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/15 13:51
 * ================================================
 */
@Module
//构建PictureCompressionModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class PictureCompressionModule {

    @Binds
    abstract PictureCompressionContract.Model bindPictureCompressionModel(PictureCompressionModel model);
}