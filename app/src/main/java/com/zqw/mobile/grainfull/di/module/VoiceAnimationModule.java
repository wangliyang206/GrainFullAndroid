package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.VoiceAnimationContract;
import com.zqw.mobile.grainfull.mvp.model.VoiceAnimationModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/12/23 12:38
 * ================================================
 */
@Module
//构建VoiceAnimationModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class VoiceAnimationModule {

    @Binds
    abstract VoiceAnimationContract.Model bindVoiceAnimationModel(VoiceAnimationModel model);
}