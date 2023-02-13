package com.zqw.mobile.grainfull.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.FragmentScope;
import com.zqw.mobile.grainfull.di.module.MoleHitModule;
import com.zqw.mobile.grainfull.mvp.contract.MoleHitContract;
import com.zqw.mobile.grainfull.mvp.ui.fragment.MoleHitResultFragment;
import com.zqw.mobile.grainfull.mvp.ui.fragment.MoleHitMainFragment;
import com.zqw.mobile.grainfull.mvp.ui.fragment.MoleHitStartFragment;

import dagger.BindsInstance;
import dagger.Component;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/31/2019 10:34
 * ================================================
 */
@FragmentScope
@Component(modules = MoleHitModule.class, dependencies = AppComponent.class)
public interface MoleHitComponent {
    void inject(MoleHitMainFragment fragment);
    void inject(MoleHitStartFragment fragment);
    void inject(MoleHitResultFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        MoleHitComponent.Builder view(MoleHitContract.View view);

        MoleHitComponent.Builder appComponent(AppComponent appComponent);

        MoleHitComponent build();
    }
}