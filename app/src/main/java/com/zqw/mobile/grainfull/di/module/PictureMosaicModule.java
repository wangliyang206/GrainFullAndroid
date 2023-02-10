package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.RequestMapper;
import com.zqw.mobile.grainfull.mvp.contract.PictureMosaicContract;
import com.zqw.mobile.grainfull.mvp.model.PictureMosaicModel;
import com.zqw.mobile.grainfull.mvp.ui.adapter.PictureMosaicAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/09 15:00
 * ================================================
 */
@Module
//构建PictureMosaicModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class PictureMosaicModule {

    @Binds
    abstract PictureMosaicContract.Model bindPictureMosaicModel(PictureMosaicModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(PictureMosaicContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(PictureMosaicContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }

    @ActivityScope
    @Provides
    static List<String> provideSevenStatistics() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static PictureMosaicAdapter providePictureMosaicAdapter(List<String> list) {
        return new PictureMosaicAdapter(list);
    }
}