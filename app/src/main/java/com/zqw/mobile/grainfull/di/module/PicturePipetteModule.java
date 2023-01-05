package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.PicturePipetteContract;
import com.zqw.mobile.grainfull.mvp.model.PicturePipetteModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/03 16:06
 * ================================================
 */
@Module
//构建PicturePipetteModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class PicturePipetteModule {

    @Binds
    abstract PicturePipetteContract.Model bindPicturePipetteModel(PicturePipetteModel model);
}