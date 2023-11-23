package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.grainfull.mvp.contract.ChatGPTWebContract;
import com.zqw.mobile.grainfull.mvp.model.ChatGPTWebModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/11/23 14:00
 * ================================================
 */
@Module
//构建ChatGPTWebModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class ChatGPTWebModule {

    @Binds
    abstract ChatGPTWebContract.Model bindChatGPTWebModel(ChatGPTWebModel model);
}