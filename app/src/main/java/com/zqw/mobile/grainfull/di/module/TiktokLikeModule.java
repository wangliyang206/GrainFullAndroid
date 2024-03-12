package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.TiktokLikeContract;
import com.zqw.mobile.grainfull.mvp.model.TiktokLikeModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/03/12 13:46
 * ================================================
 */
@Module
//构建TiktokLikeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class TiktokLikeModule {

    @Binds
    abstract TiktokLikeContract.Model bindTiktokLikeModel(TiktokLikeModel model);
}