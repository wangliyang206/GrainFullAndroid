package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.TiktokLikeModule;
import com.zqw.mobile.grainfull.mvp.contract.TiktokLikeContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.TiktokLikeActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/03/12 13:46
 * ================================================
 */
@ActivityScope
@Component(modules = TiktokLikeModule.class, dependencies = AppComponent.class)
public interface TiktokLikeComponent {

    void inject(TiktokLikeActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        TiktokLikeComponent.Builder view(TiktokLikeContract.View view);

        TiktokLikeComponent.Builder appComponent(AppComponent appComponent);

        TiktokLikeComponent build();
    }
}