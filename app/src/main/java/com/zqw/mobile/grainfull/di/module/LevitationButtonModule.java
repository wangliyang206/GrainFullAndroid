package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.LevitationButtonContract;
import com.zqw.mobile.grainfull.mvp.model.LevitationButtonModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/03 16:06
 * ================================================
 */
@Module
//构建LevitationButtonModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class LevitationButtonModule {

    @Binds
    abstract LevitationButtonContract.Model bindLevitationButtonModel(LevitationButtonModel model);
}