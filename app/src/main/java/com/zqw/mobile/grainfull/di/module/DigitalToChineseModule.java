package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.DigitalToChineseContract;
import com.zqw.mobile.grainfull.mvp.model.DigitalToChineseModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/10 16:38
 * ================================================
 */
@Module
//构建DigitalToChineseModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class DigitalToChineseModule {

    @Binds
    abstract DigitalToChineseContract.Model bindDigitalToChineseModel(DigitalToChineseModel model);
}