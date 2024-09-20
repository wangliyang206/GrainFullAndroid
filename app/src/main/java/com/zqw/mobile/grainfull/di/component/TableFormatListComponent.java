package com.zqw.mobile.grainfull.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.grainfull.di.module.TableFormatListModule;
import com.zqw.mobile.grainfull.mvp.contract.TableFormatListContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.mvp.ui.activity.TableFormatListActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2024/09/20 11:28
 * ================================================
 */
@ActivityScope
@Component(modules = TableFormatListModule.class, dependencies = AppComponent.class)
public interface TableFormatListComponent {

    void inject(TableFormatListActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        TableFormatListComponent.Builder view(TableFormatListContract.View view);

        TableFormatListComponent.Builder appComponent(AppComponent appComponent);

        TableFormatListComponent build();
    }
}