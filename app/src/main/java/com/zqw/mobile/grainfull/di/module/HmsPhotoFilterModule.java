package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.HmsPhotoFilterContract;
import com.zqw.mobile.grainfull.mvp.model.HmsPhotoFilterModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/12/05 15:44
 * ================================================
 */
@Module
//构建HmsPhotoFilterModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class HmsPhotoFilterModule {

    @Binds
    abstract HmsPhotoFilterContract.Model bindHmsPhotoFilterModel(HmsPhotoFilterModel model);
}