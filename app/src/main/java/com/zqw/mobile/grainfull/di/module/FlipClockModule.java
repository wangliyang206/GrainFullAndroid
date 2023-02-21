package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.FlipClockContract;
import com.zqw.mobile.grainfull.mvp.model.FlipClockModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/20 17:44
 * ================================================
 */
@Module
//构建FlipClockModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class FlipClockModule {

    @Binds
    abstract FlipClockContract.Model bindFlipClockModel(FlipClockModel model);
}