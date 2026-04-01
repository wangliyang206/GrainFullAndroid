package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.BarrageModule;
import com.zqw.mobile.grainfull.di.module.BarrageContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.BarrageActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2026/04/01 17:22
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = BarrageModule.class, dependencies = AppComponent.class)
public interface BarrageComponent {

    void inject(BarrageActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BarrageComponent.Builder view(BarrageContract.View view);

        BarrageComponent.Builder appComponent(AppComponent appComponent);

        BarrageComponent build();
    }

}