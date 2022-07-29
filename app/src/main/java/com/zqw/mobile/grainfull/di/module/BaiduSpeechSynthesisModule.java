package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.BaiduSpeechSynthesisContract;
import com.zqw.mobile.grainfull.mvp.model.BaiduSpeechSynthesisModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/07/26 18:34
 * ================================================
 */
@Module
//构建BaiduSpeechSynthesisModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class BaiduSpeechSynthesisModule {

    @Binds
    abstract BaiduSpeechSynthesisContract.Model bindBaiduSpeechSynthesisModel(BaiduSpeechSynthesisModel model);
}