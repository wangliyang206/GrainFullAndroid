package com.zqw.mobile.grainfull.di.module;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.RequestMapper;
import com.zqw.mobile.grainfull.mvp.contract.UmDataStatisticsContract;
import com.zqw.mobile.grainfull.mvp.model.UmDataStatisticsModel;
import com.zqw.mobile.grainfull.mvp.model.entity.SevenStatistics;
import com.zqw.mobile.grainfull.mvp.ui.adapter.SevenStatisticsAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/16 10:42
 * ================================================
 */
@Module
//构建UmDataStatisticsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class UmDataStatisticsModule {

    @Binds
    abstract UmDataStatisticsContract.Model bindUmDataStatisticsModel(UmDataStatisticsModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(UmDataStatisticsContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(UmDataStatisticsContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(UmDataStatisticsContract.View view) {
        return new LinearLayoutManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static List<SevenStatistics> provideSevenStatistics() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static SevenStatisticsAdapter provideSevenStatisticsAdapter(List<SevenStatistics> list) {
        return new SevenStatisticsAdapter(list);
    }
}