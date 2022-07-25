package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.BaiduVoiceRecogContract;
import com.zqw.mobile.grainfull.mvp.model.BaiduVoiceRecogModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/25 17:21
 * ================================================
 */
@Module
//构建BaiduVoiceRecogModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class BaiduVoiceRecogModule {

    @Binds
    abstract BaiduVoiceRecogContract.Model bindBaiduVoiceRecogModel(BaiduVoiceRecogModel model);
}