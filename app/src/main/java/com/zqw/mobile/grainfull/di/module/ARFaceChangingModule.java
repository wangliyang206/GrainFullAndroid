package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.ARFaceChangingContract;
import com.zqw.mobile.grainfull.mvp.model.ARFaceChangingModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/11/02 17:40
 * ================================================
 */
@Module
//构建ARFaceChangingModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class ARFaceChangingModule {

    @Binds
    abstract ARFaceChangingContract.Model bindARFaceChangingModel(ARFaceChangingModel model);
}