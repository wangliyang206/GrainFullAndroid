package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.LotteryTurntableContract;
import com.zqw.mobile.grainfull.mvp.model.LotteryTurntableModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/12/27 17:08
 * ================================================
 */
@Module
//构建LotteryTurntableModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class LotteryTurntableModule {

    @Binds
    abstract LotteryTurntableContract.Model bindLotteryTurntableModel(LotteryTurntableModel model);
}