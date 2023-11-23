package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.ChatGPTWebModule;
import com.zqw.mobile.grainfull.mvp.contract.ChatGPTWebContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.ChatGPTWebActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/11/23 14:00
 * ================================================
 */
@ActivityScope
@Component(modules = ChatGPTWebModule.class, dependencies = AppComponent.class)
public interface ChatGPTWebComponent {

    void inject(ChatGPTWebActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ChatGPTWebComponent.Builder view(ChatGPTWebContract.View view);

        ChatGPTWebComponent.Builder appComponent(AppComponent appComponent);

        ChatGPTWebComponent build();
    }
}