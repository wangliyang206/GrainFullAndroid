package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceActivationContract;
import com.zqw.mobile.grainfull.mvp.model.BaiduFaceActivationModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/22 18:29
 * ================================================
 */
@Module
//构建BaiduFaceActivationModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class BaiduFaceActivationModule {

    @Binds
    abstract BaiduFaceActivationContract.Model bindBaiduFaceActivationModel(BaiduFaceActivationModel model);
}