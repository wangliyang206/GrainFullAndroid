package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.PictureMosaicContract;
import com.zqw.mobile.grainfull.mvp.model.PictureMosaicModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/09 15:00
 * ================================================
 */
@Module
//构建PictureMosaicModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class PictureMosaicModule {

    @Binds
    abstract PictureMosaicContract.Model bindPictureMosaicModel(PictureMosaicModel model);
}