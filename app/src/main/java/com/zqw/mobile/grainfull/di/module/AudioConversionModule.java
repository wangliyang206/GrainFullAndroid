package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.AudioConversionContract;
import com.zqw.mobile.grainfull.mvp.model.AudioConversionModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/08/05 14:46
 * ================================================
 */
@Module
//构建AudioConversionModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class AudioConversionModule {

    @Binds
    abstract AudioConversionContract.Model bindAudioConversionModel(AudioConversionModel model);
}