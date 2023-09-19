package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.CardOverlapModule;
import com.zqw.mobile.grainfull.mvp.contract.CardOverlapContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.CardOverlapActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/09/19 11:46
 * ================================================
 */
@ActivityScope
@Component(modules = CardOverlapModule.class, dependencies = AppComponent.class)
public interface CardOverlapComponent {

    void inject(CardOverlapActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        CardOverlapComponent.Builder view(CardOverlapContract.View view);

        CardOverlapComponent.Builder appComponent(AppComponent appComponent);

        CardOverlapComponent build();
    }
}