package com.zqw.mobile.grainfull.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.FragmentScope;
import com.zqw.mobile.grainfull.di.module.SpecialEffectsModule;
import com.zqw.mobile.grainfull.mvp.contract.SpecialEffectsContract;
import com.zqw.mobile.grainfull.mvp.ui.fragment.SpecialEffectsFragment;

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
@Component(modules = SpecialEffectsModule.class, dependencies = AppComponent.class)
public interface SpecialEffectsComponent {
    void inject(SpecialEffectsFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SpecialEffectsComponent.Builder view(SpecialEffectsContract.View view);

        SpecialEffectsComponent.Builder appComponent(AppComponent appComponent);

        SpecialEffectsComponent build();
    }
}