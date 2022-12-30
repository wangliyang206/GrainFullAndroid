package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.BulletChatContract;
import com.zqw.mobile.grainfull.mvp.model.BulletChatModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/12/29 09:59
 * ================================================
 */
@Module
//构建BulletChatModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class BulletChatModule {

    @Binds
    abstract BulletChatContract.Model bindBulletChatModel(BulletChatModel model);
}