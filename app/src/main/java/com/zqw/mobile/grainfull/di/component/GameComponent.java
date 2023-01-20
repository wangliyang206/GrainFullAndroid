package com.zqw.mobile.grainfull.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.FragmentScope;
import com.zqw.mobile.grainfull.di.module.GameModule;
import com.zqw.mobile.grainfull.mvp.contract.GameContract;
import com.zqw.mobile.grainfull.mvp.ui.fragment.GameFragment;

import dagger.BindsInstance;
import dagger.Component;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/31/2019 10:34
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = GameModule.class, dependencies = AppComponent.class)
public interface GameComponent {
    void inject(GameFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        GameComponent.Builder view(GameContract.View view);

        GameComponent.Builder appComponent(AppComponent appComponent);

        GameComponent build();
    }
}