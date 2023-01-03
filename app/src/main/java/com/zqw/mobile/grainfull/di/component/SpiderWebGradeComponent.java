package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.SpiderWebGradeModule;
import com.zqw.mobile.grainfull.mvp.contract.SpiderWebGradeContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.SpiderWebGradeActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/03 16:06
 * ================================================
 */
@ActivityScope
@Component(modules = SpiderWebGradeModule.class, dependencies = AppComponent.class)
public interface SpiderWebGradeComponent {

    void inject(SpiderWebGradeActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SpiderWebGradeComponent.Builder view(SpiderWebGradeContract.View view);

        SpiderWebGradeComponent.Builder appComponent(AppComponent appComponent);

        SpiderWebGradeComponent build();
    }
}