package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.EditTurntableModule;
import com.zqw.mobile.grainfull.mvp.contract.EditTurntableContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.EditTurntableActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/12/28 16:08
 * ================================================
 */
@ActivityScope
@Component(modules = EditTurntableModule.class, dependencies = AppComponent.class)
public interface EditTurntableComponent {

    void inject(EditTurntableActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        EditTurntableComponent.Builder view(EditTurntableContract.View view);

        EditTurntableComponent.Builder appComponent(AppComponent appComponent);

        EditTurntableComponent build();
    }
}