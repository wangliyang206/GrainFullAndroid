package com.zqw.mobile.grainfull.di.module;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.RequestMapper;
import com.zqw.mobile.grainfull.mvp.contract.ProductDisplayContract;
import com.zqw.mobile.grainfull.mvp.model.ProductDisplayModel;
import com.zqw.mobile.grainfull.mvp.model.entity.GoodsEntity;
import com.zqw.mobile.grainfull.mvp.ui.adapter.ProductDisplayAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/08/14 17:03
 * ================================================
 */
@Module
//构建ProductDisplayModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class ProductDisplayModule {

    @Binds
    abstract ProductDisplayContract.Model bindProductDisplayModel(ProductDisplayModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(ProductDisplayContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(ProductDisplayContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }

    @ActivityScope
    @Provides
    static LinearLayoutManager provideLayoutManager(ProductDisplayContract.View view) {
        return new LinearLayoutManager(view.getActivity(), RecyclerView.HORIZONTAL, false);
    }

    @ActivityScope
    @Provides
    static List<GoodsEntity> provideGoodsEntity() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static ProductDisplayAdapter provideProductDisplayAdapter(List<GoodsEntity> list) {
        return new ProductDisplayAdapter(list);
    }
}