package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.BulletChatModule;
import com.zqw.mobile.grainfull.mvp.contract.BulletChatContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.BulletChatActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/12/29 09:59
 * ================================================
 */
@ActivityScope
@Component(modules = BulletChatModule.class, dependencies = AppComponent.class)
public interface BulletChatComponent {

    void inject(BulletChatActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BulletChatComponent.Builder view(BulletChatContract.View view);

        BulletChatComponent.Builder appComponent(AppComponent appComponent);

        BulletChatComponent build();
    }
}