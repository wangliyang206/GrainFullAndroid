package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.HmsSceneAnimationEffectContract;
import com.zqw.mobile.grainfull.mvp.model.HmsSceneAnimationEffectModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/12/10 09:16
 * ================================================
 */
@Module
//构建HmsSceneAnimationEffectModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class HmsSceneAnimationEffectModule {

    @Binds
    abstract HmsSceneAnimationEffectContract.Model bindHmsSceneAnimationEffectModel(HmsSceneAnimationEffectModel model);
}