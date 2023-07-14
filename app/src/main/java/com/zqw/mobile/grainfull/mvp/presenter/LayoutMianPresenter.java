package com.zqw.mobile.grainfull.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.app.utils.EventBusTags;
import com.zqw.mobile.grainfull.mvp.contract.LayoutMianContract;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeContentInfo;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeContentResponse;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeTab;
import com.zqw.mobile.grainfull.mvp.model.entity.MainEvent;
import com.zqw.mobile.grainfull.mvp.model.entity.NewHomeInfo;
import com.zqw.mobile.grainfull.mvp.ui.adapter.NewHomeAdapter;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/07/05 11:06
 * ================================================
 */
@ActivityScope
public class LayoutMianPresenter extends BasePresenter<LayoutMianContract.Model, LayoutMianContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    List<NewHomeInfo> mDataList;
    @Inject
    NewHomeAdapter mAdapter;                                                                        // 适配器

    // Tab项
    private String[] strArray = new String[]{"新发现", "手机", "电脑办公", "电子配件"};

    @Inject
    public LayoutMianPresenter(LayoutMianContract.Model model, LayoutMianContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        if (mDataList != null) {
            mDataList.clear();
            mDataList = null;
        }
        this.mAdapter = null;
    }

    /**
     * 获取首页
     */
    public void getHomeList() {
        mDataList.clear();
        getTopModuleData();
        getActionBarData();
        getListData();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 获取顶部模块数据
     */
    private void getTopModuleData() {
        mDataList.add(new NewHomeInfo());
    }

    /**
     * 获取中间模块数据
     */
    private void getActionBarData() {
        mDataList.add(new NewHomeInfo());
    }

    /**
     * 获取列表数据
     */
    private void getListData() {
        HomeTab categoryBean = new HomeTab();
        categoryBean.getTabTitleList().clear();
        categoryBean.getTabTitleList().addAll(Arrays.asList(strArray));
        mDataList.add(new NewHomeInfo(categoryBean));
    }

    /**
     * 获取首页数据
     */
    public void getHomeContentData(final boolean pullToRefresh) {
        HomeContentResponse info = new HomeContentResponse();
        List<HomeContentInfo> list = new ArrayList<>();
        if (pullToRefresh) {
            list.add(new HomeContentInfo("", "A001店铺"));
            list.add(new HomeContentInfo("", "A002店铺"));
            list.add(new HomeContentInfo("", "A003店铺"));
            list.add(new HomeContentInfo("", "A004店铺"));
            list.add(new HomeContentInfo("", "A005店铺"));
            list.add(new HomeContentInfo("", "A006店铺"));
            list.add(new HomeContentInfo("", "A007店铺"));
            list.add(new HomeContentInfo("", "A008店铺"));
            list.add(new HomeContentInfo("", "A009店铺"));
            list.add(new HomeContentInfo("", "A0010店铺"));
        } else {
            list.add(new HomeContentInfo("", "A0011店铺"));
            list.add(new HomeContentInfo("", "A0012店铺"));
            list.add(new HomeContentInfo("", "A0013店铺"));
            list.add(new HomeContentInfo("", "A0014店铺"));
            list.add(new HomeContentInfo("", "A0015店铺"));
            list.add(new HomeContentInfo("", "A0016店铺"));
            list.add(new HomeContentInfo("", "A0017店铺"));
            list.add(new HomeContentInfo("", "A0018店铺"));
            list.add(new HomeContentInfo("", "A0019店铺"));
            list.add(new HomeContentInfo("", "A0020店铺"));
        }

        info.setList(list);
        info.setPages(2);
        EventBus.getDefault().post(new MainEvent(EventBusTags.NEW_HOME_SUCC_TAG, info), EventBusTags.HOME_TAG);
    }
}