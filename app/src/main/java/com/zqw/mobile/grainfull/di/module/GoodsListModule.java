package com.zqw.mobile.grainfull.di.module;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.RequestMapper;
import com.zqw.mobile.grainfull.mvp.contract.GoodsListContract;
import com.zqw.mobile.grainfull.mvp.model.GoodsListModel;
import com.zqw.mobile.grainfull.mvp.model.entity.GoodsBean;
import com.zqw.mobile.grainfull.mvp.ui.adapter.GoodsListAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/07/05 11:06
 * ================================================
 */
@Module
//构建LayoutHomeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class GoodsListModule {

    @Binds
    abstract GoodsListContract.Model bindLayoutForumModel(GoodsListModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(GoodsListContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(GoodsListContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }

    @ActivityScope
    @Provides
    static StaggeredGridLayoutManager provideLayoutManager(GoodsListContract.View view) {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @ActivityScope
    @Provides
    static List<GoodsBean> provideNewHomeInfo() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static GoodsListAdapter provideGoodsListAdapter(List<GoodsBean> list) {
        return new GoodsListAdapter(list);
    }

}