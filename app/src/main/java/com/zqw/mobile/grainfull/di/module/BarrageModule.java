package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.BarrageContract;
import com.zqw.mobile.grainfull.mvp.model.BarrageModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2026/04/01 17:22
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
//构建BarrageModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class BarrageModule {

    @Binds
    abstract BarrageContract.Model bindBarrageModel(BarrageModel model);
}