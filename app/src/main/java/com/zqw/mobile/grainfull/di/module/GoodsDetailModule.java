package com.zqw.mobile.grainfull.di.module;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.app.global.RequestMapper;
import com.zqw.mobile.grainfull.mvp.contract.GoodsDetailContract;
import com.zqw.mobile.grainfull.mvp.model.GoodsDetailModel;
import com.zqw.mobile.grainfull.mvp.model.entity.AppraiseBean;
import com.zqw.mobile.grainfull.mvp.model.entity.BannerBean;
import com.zqw.mobile.grainfull.mvp.model.entity.GoodsBean;
import com.zqw.mobile.grainfull.mvp.ui.adapter.AppraiseListSectionAdapter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.ColorThumbListAdapter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.GoodsDesImgListAdapter;
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
 * Created by MVPArmsTemplate on 2025/02/18 11:35
 * ================================================
 */
@Module
//构建GoodsDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class GoodsDetailModule {

    @Binds
    abstract GoodsDetailContract.Model bindGoodsDetailModel(GoodsDetailModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(GoodsDetailContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(GoodsDetailContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }

    /*------------------------------------------选择物品颜色------------------------------------------*/
    @ActivityScope
    @Provides
    static List<BannerBean> provideBannerBean() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static ColorThumbListAdapter provideColorThumbListAdapter(List<BannerBean> list) {
        return new ColorThumbListAdapter(R.layout.color_thumb_item, list);
    }

    /*------------------------------------------评价列表------------------------------------------*/
    @ActivityScope
    @Provides
    static List<AppraiseBean> provideAppraiseBean() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static AppraiseListSectionAdapter provideAppraiseListSectionAdapter(List<AppraiseBean> list) {
        return new AppraiseListSectionAdapter(R.layout.activity_detail_appraise_header, R.layout.activity_detail_appraise_item, list);
    }

    /*------------------------------------------商品详情------------------------------------------*/
    @ActivityScope
    @Provides
    static List<String> provideString() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static GoodsDesImgListAdapter provideGoodsDesImgListAdapter(List<String> list) {
        return new GoodsDesImgListAdapter(R.layout.activity_detail_des_img, list);
    }

    /*------------------------------------------商品推荐------------------------------------------*/
    @ActivityScope
    @Provides
    static List<GoodsBean> provideGoodsBean() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static GoodsListAdapter provideGoodsListAdapter(List<GoodsBean> list) {
        return new GoodsListAdapter(list);
    }
}