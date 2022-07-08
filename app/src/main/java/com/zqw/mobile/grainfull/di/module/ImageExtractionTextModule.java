package com.zqw.mobile.grainfull.di.module;

import com.zqw.mobile.grainfull.mvp.contract.ImageExtractionTextContract;
import com.zqw.mobile.grainfull.mvp.model.ImageExtractionTextModel;

import dagger.Binds;
import dagger.Module;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/08 16:11
 * ================================================
 */
@Module
//构建ImageExtractionTextModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class ImageExtractionTextModule {

    @Binds
    abstract ImageExtractionTextContract.Model bindImageExtractionTextModel(ImageExtractionTextModel model);
}