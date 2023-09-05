package com.zqw.mobile.grainfull.di.module;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.RequestMapper;
import com.zqw.mobile.grainfull.mvp.contract.LayoutCategoryContract;
import com.zqw.mobile.grainfull.mvp.model.LayoutCategoryModel;
import com.zqw.mobile.grainfull.mvp.model.entity.CategoryBean;
import com.zqw.mobile.grainfull.mvp.ui.adapter.CategoryListAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/09/04 12:01
 * ================================================
 */
@Module
//构建LayoutCategoryModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class LayoutCategoryModule {

    @Binds
    abstract LayoutCategoryContract.Model bindLayoutCategoryModel(LayoutCategoryModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(LayoutCategoryContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(LayoutCategoryContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }

    @ActivityScope
    @Provides
    static LinearLayoutManager provideLayoutManager(LayoutCategoryContract.View view) {
        return new LinearLayoutManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static List<CategoryBean> provideCategoryBean() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static CategoryListAdapter provideCategoryListAdapter(List<CategoryBean> list) {
        return new CategoryListAdapter(list);
    }
}