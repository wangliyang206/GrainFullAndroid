package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.LotteryTurntableModule;
import com.zqw.mobile.grainfull.mvp.contract.LotteryTurntableContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.LotteryTurntableActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/12/27 17:08
 * ================================================
 */
@ActivityScope
@Component(modules = LotteryTurntableModule.class, dependencies = AppComponent.class)
public interface LotteryTurntableComponent {

    void inject(LotteryTurntableActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LotteryTurntableComponent.Builder view(LotteryTurntableContract.View view);

        LotteryTurntableComponent.Builder appComponent(AppComponent appComponent);

        LotteryTurntableComponent build();
    }
}