package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.AudioWaveformContract;
import com.zqw.mobile.grainfull.mvp.model.AudioWaveformModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/08/03 14:42
 * ================================================
 */
@Module
//构建AudioWaveformModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class AudioWaveformModule {

    @Binds
    abstract AudioWaveformContract.Model bindAudioWaveformModel(AudioWaveformModel model);
}