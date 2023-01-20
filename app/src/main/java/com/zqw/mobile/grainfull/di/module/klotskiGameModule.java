package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.klotskiGameContract;
import com.zqw.mobile.grainfull.mvp.model.klotskiGameModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/20 18:00
 * ================================================
 */
@Module
//构建klotskiGameModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class klotskiGameModule {

    @Binds
    abstract klotskiGameContract.Model bindklotskiGameModel(klotskiGameModel model);
}