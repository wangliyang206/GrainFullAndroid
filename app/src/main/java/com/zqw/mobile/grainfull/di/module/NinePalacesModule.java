package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.NinePalacesContract;
import com.zqw.mobile.grainfull.mvp.model.NinePalacesModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/13 16:37
 * ================================================
 */
@Module
//构建NinePalacesModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class NinePalacesModule {

    @Binds
    abstract NinePalacesContract.Model bindNinePalacesModel(NinePalacesModel model);
}