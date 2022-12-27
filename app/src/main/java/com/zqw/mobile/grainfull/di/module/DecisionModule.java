package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.DecisionContract;
import com.zqw.mobile.grainfull.mvp.model.DecisionModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/12/27 16:16
 * ================================================
 */
@Module
//构建DecisionModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class DecisionModule {

    @Binds
    abstract DecisionContract.Model bindDecisionModel(DecisionModel model);
}