package com.zqw.mobile.grainfull.di.module;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.RequestMapper;
import com.zqw.mobile.grainfull.mvp.contract.EditTurntableContract;
import com.zqw.mobile.grainfull.mvp.model.EditTurntableModel;
import com.zqw.mobile.grainfull.mvp.ui.adapter.EditTurntableAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2022/12/28 16:08
 * ================================================
 */
@Module
//构建EditTurntableModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class EditTurntableModule {

    @Binds
    abstract EditTurntableContract.Model bindEditTurntableModel(EditTurntableModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(EditTurntableContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(EditTurntableContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(EditTurntableContract.View view) {
        return new LinearLayoutManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static List<String> provideList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static EditTurntableAdapter provideEditTurntableAdapter(List<String> list) {
        return new EditTurntableAdapter(list);
    }
}