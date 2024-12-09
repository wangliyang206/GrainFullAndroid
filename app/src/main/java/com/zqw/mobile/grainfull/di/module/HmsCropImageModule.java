package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.HmsCropImageContract;
import com.zqw.mobile.grainfull.mvp.model.HmsCropImageModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/12/09 10:18
 * ================================================
 */
@Module
//构建HmsCropImageModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class HmsCropImageModule {

    @Binds
    abstract HmsCropImageContract.Model bindHmsCropImageModel(HmsCropImageModel model);
}