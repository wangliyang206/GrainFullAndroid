package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.ChineseToPinyinContract;
import com.zqw.mobile.grainfull.mvp.model.ChineseToPinyinModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/10 16:38
 * ================================================
 */
@Module
//构建ChineseToPinyinModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class ChineseToPinyinModule {

    @Binds
    abstract ChineseToPinyinContract.Model bindChineseToPinyinModel(ChineseToPinyinModel model);
}