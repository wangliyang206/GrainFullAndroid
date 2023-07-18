package com.zqw.mobile.grainfull.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.mvp.contract.LayoutMianContract;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeContentInfo;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeContentResponse;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeTab;
import com.zqw.mobile.grainfull.mvp.model.entity.NewHomeInfo;
import com.zqw.mobile.grainfull.mvp.ui.adapter.NewHomeAdapter;

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
    public void getHomeContentData(int tab, final boolean pullToRefresh) {
        HomeContentResponse info = new HomeContentResponse();
        List<HomeContentInfo> list = new ArrayList<>();
        if (pullToRefresh) {
            list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw1%2F2bf87843-3aa2-4d40-ad09-6ce18e8eb41c%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692254573&t=0c84282280e6065799903f0b152b5672", "A001店铺"));
            list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw1%2F0914cf40-a7a9-41ec-8b09-8a7cc1fd87b9%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692254587&t=3bf3934cbbcfc8b8024e299e8b6cc67b", "A002店铺"));
            list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1109%2F351c4b71j00r2asr5002nc000hs0114c.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "A003店铺"));
            list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1109%2Fda793cecj00r2asr5003tc000hs0116c.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "A004店铺"));
            list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1109%2F3571be09j00r2asr5001ic000hs00tzc.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "A005店铺"));
            list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2022%2F0127%2F81845b82j00r6c5gh001wc000hs00vmc.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "A006店铺"));
            list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw1%2F62ae58c0-bbac-4741-8903-415954334c8e%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692254658&t=3ebacde7ec7c6e8d0a8787352b195cac", "A007店铺"));
            list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1109%2Fdaa61eddj00r2asr5002xc000hs010nc.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "A008店铺"));
            list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fci.xiaohongshu.com%2Fa4dd321e-30b9-c0c9-bc20-14d206c934c6%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fci.xiaohongshu.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692254680&t=6da8da567645370cf50dc3ace76369c6", "A009店铺"));
            list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1214%2F4cee9f56j00r43gy20019c000hs00vmc.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "A0010店铺"));
        } else {
            list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1115%2F40b58e25j00r2lr47001wc000hs00vlc.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "A0011店铺"));
            list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1109%2Fdd0d98caj00r2asr5002nc000hs00r5c.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "A0012店铺"));
            list.add(new HomeContentInfo("https://img.qzonei.com/data/attachment/portal/202206/30/102432q0lnhvlxqez7nb7e.jpg", "A0013店铺"));
            list.add(new HomeContentInfo("https://pics2.baidu.com/feed/0b7b02087bf40ad173890dc7bbdb39d9a8eccea7.jpeg?token=a8e0ebe159ca580d31e202d5c627a1d0&s=2575168FC002E4FF5E95E6AB0300E017", "A0014店铺"));
            list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F202002%2F16%2F20200216165019_etsba.thumb.1000_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692254761&t=0d2cf957c4d71db98791a11a84c30059", "A0015店铺"));
            list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201806%2F16%2F20180616112359_ketow.jpg&refer=http%3A%2F%2Fb-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692254770&t=e96c6215aa071b4aa32fd6ae01cfe2c8", "A0016店铺"));
            list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1030%2F97f21769j00r1s70y001pc000hs00vnc.jpg&thumbnail=650x2147483647&quality=80&type=jpg", "A0017店铺"));
            list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw%2F63525cbc-130f-4f52-9ad1-bc87693a1900%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692254799&t=18b702ec5d4b78b02410f9c89b4b1df3", "A0018店铺"));
            list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1030%2F8ead0baaj00r1s70y0029c000hs00vmc.jpg&thumbnail=650x2147483647&quality=80&type=jpg", "A0019店铺"));
            list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1205%2F1b0fce8cj00r3m5lx0025c000hs00vjc.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "A0020店铺"));
        }

        info.setList(list);
        info.setPages(2);
        mAdapter.onRefreshChildData(tab, info);
    }
}