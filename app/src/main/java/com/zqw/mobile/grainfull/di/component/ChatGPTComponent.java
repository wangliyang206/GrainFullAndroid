package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.ChatGPTModule;
import com.zqw.mobile.grainfull.mvp.contract.ChatGPTContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.ChatGPTActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/11/23 15:49
 * ================================================
 */
@ActivityScope
@Component(modules = ChatGPTModule.class, dependencies = AppComponent.class)
public interface ChatGPTComponent {

    void inject(ChatGPTActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ChatGPTComponent.Builder view(ChatGPTContract.View view);

        ChatGPTComponent.Builder appComponent(AppComponent appComponent);

        ChatGPTComponent build();
    }
}