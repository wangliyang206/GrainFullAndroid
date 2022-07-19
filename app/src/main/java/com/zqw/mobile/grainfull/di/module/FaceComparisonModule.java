package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.FaceComparisonContract;
import com.zqw.mobile.grainfull.mvp.model.FaceComparisonModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/19 14:20
 * ================================================
 */
@Module
//构建FaceComparisonModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class FaceComparisonModule {

    @Binds
    abstract FaceComparisonContract.Model bindFaceComparisonModel(FaceComparisonModel model);
}