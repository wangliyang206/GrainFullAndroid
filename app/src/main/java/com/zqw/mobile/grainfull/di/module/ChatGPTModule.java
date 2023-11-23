package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.ChatGPTContract;
import com.zqw.mobile.grainfull.mvp.model.ChatGPTModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/11/23 15:49
 * ================================================
 */
@Module
//构建ChatGPTModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class ChatGPTModule {

    @Binds
    abstract ChatGPTContract.Model bindChatGPTModel(ChatGPTModel model);
}