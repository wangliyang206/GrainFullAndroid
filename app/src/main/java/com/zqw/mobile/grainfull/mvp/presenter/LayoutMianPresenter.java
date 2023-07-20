package com.zqw.mobile.grainfull.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.contract.LayoutMianContract;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeActionBarInfo;
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
        List<HomeContentInfo> list = new ArrayList<>();
        list.add(new HomeContentInfo("https://lmg.jj20.com/up/allimg/tp09/2106110T4305357-0-lp.jpg", "新科技", "IQOO Pad新品上市"));
        list.add(new HomeContentInfo("https://img0.baidu.com/it/u=2947025036,2907775544&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=889", "新上架", "禹田作物起源"));
        list.add(new HomeContentInfo("https://img2.baidu.com/it/u=1106624645,1258886294&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=889", "新包装", "小天材Z9航天款首发"));
        list.add(new HomeContentInfo("https://img0.baidu.com/it/u=3638921546,2426816085&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=888", "新款式", "石头 A10 上新"));
        list.add(new HomeContentInfo("https://img2.baidu.com/it/u=1734909540,1726839284&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=889", "新配置", "编程总动员"));
        list.add(new HomeContentInfo("https://img2.baidu.com/it/u=474077785,528780582&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=889", "新配方", "猫粮新品上市"));
        mDataList.add(new NewHomeInfo(list));
    }

    /**
     * 获取中间模块数据
     */
    private void getActionBarData() {
        NewHomeInfo info = new NewHomeInfo();
        List<HomeActionBarInfo> list = new ArrayList<>();
        list.add(new HomeActionBarInfo(R.mipmap.icon_home_99, "9块9包邮"));
        list.add(new HomeActionBarInfo(R.mipmap.icon_home_gaoyong, "高佣排名"));
        list.add(new HomeActionBarInfo(R.mipmap.icon_home_big_quan, "大额优惠券"));
        list.add(new HomeActionBarInfo(R.mipmap.icon_home_today_bao, "今日爆款"));
        list.add(new HomeActionBarInfo(R.mipmap.icon_home_low_price, "超低价"));
        list.add(new HomeActionBarInfo(R.mipmap.icon_home_today_jingdong, "京东"));
        list.add(new HomeActionBarInfo(R.mipmap.icon_home_pdd, "拼多多"));
        list.add(new HomeActionBarInfo(R.mipmap.icon_home_elm, "饿了么"));
        list.add(new HomeActionBarInfo(R.mipmap.icon_home_hfcz, "话费充值"));
        list.add(new HomeActionBarInfo(R.mipmap.icon_home_tmall_chaoshi, "天猫超市"));
        list.add(new HomeActionBarInfo(R.mipmap.icon_home_sams_club, "山姆会员店"));
        list.add(new HomeActionBarInfo(R.mipmap.icon_home_feizhu, "飞猪"));
        list.add(new HomeActionBarInfo(R.mipmap.icon_home_oil, "优惠加油"));
        list.add(new HomeActionBarInfo(R.mipmap.icon_home_kfc, "肯德基"));
        list.add(new HomeActionBarInfo(R.mipmap.icon_home_meituan, "美团"));
        info.setActionBarList(list);
        mDataList.add(info);
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
            if (tab == 0) {
                // 新发现
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw1%2F2bf87843-3aa2-4d40-ad09-6ce18e8eb41c%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692254573&t=0c84282280e6065799903f0b152b5672", "D001动漫"));
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw1%2F0914cf40-a7a9-41ec-8b09-8a7cc1fd87b9%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692254587&t=3bf3934cbbcfc8b8024e299e8b6cc67b", "D002动漫"));
                list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1109%2F351c4b71j00r2asr5002nc000hs0114c.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "D003动漫"));
                list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1109%2Fda793cecj00r2asr5003tc000hs0116c.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "D004动漫"));
                list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1109%2F3571be09j00r2asr5001ic000hs00tzc.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "D005动漫"));
                list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2022%2F0127%2F81845b82j00r6c5gh001wc000hs00vmc.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "D006动漫"));
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw1%2F62ae58c0-bbac-4741-8903-415954334c8e%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692254658&t=3ebacde7ec7c6e8d0a8787352b195cac", "D007动漫"));
                list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1109%2Fdaa61eddj00r2asr5002xc000hs010nc.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "D008动漫"));
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fci.xiaohongshu.com%2Fa4dd321e-30b9-c0c9-bc20-14d206c934c6%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fci.xiaohongshu.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692254680&t=6da8da567645370cf50dc3ace76369c6", "D009动漫"));
                list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1214%2F4cee9f56j00r43gy20019c000hs00vmc.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "D0010动漫"));
            } else if (tab == 1) {
                // 手机
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw1%2F192c0353-211a-4641-883a-34c33063b15c%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692418090&t=d767d12a4548dbebfe5043523689c100", "S001手机"));
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw1%2F4a243443-e826-4d67-aa6b-e25427e394fa%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692418150&t=81da8b95753366a5a5e67047877c3548", "S002手机"));
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F201312%2F01%2F20131201223120_S5KZW.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692419236&t=34641a42a5de95073f2c865891dce88f", "S003手机"));
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw1%2Fd47192d8-bc54-4123-ad5c-78c768194392%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692418162&t=07e300ea2d0b210d651f638e720e7b7a", "S004手机"));
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw%2F70e500dd-ca8f-4099-95db-8eed02d7984b%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692418161&t=dd1da68d87fa335c27031151af38cc91", "S005手机"));
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw%2F11a136b4-9dbe-4d27-ae67-2fd67102d5e7%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692418161&t=945edec33a7a99b2e1675f73b6f86a33", "S006手机"));
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw%2Fa3e4944c-bdc5-4bd7-ab63-758297c5c3f2%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692418161&t=26f6194154f7d4a8e8ad2da322d988c7", "S007手机"));
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw%2F76aaaf32-df8b-4dc6-99ef-75a87f69fc69%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692418161&t=bb33fe61d7cb091eb88e89e8e048c809", "S008手机"));
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw%2F9c2a2866-87c5-4655-8a5d-ff5e92c194b9%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692418161&t=3d1b0237f68aef886776bb0e7f65ccb8", "S009手机"));
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw%2Fb1d2156f-e648-416a-a030-f580e2e48ddb%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692418161&t=2fabcd818aa454518bb72a9fa7c0939a", "S0010手机"));

            } else if (tab == 2) {
                // 电脑办公
                list.add(new HomeContentInfo("https://lmg.jj20.com/up/allimg/4k/s/01/210924111H56014-0-lp.jpg", "W001电脑"));
                list.add(new HomeContentInfo("https://img2.baidu.com/it/u=689493287,2729984371&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500", "W002电脑"));
                list.add(new HomeContentInfo("https://lmg.jj20.com/up/allimg/tp01/1Z9101IH26200-0-lp.jpg", "W003电脑"));
                list.add(new HomeContentInfo("https://pic.616pic.com/photoone/00/07/41/61976373742a65579.jpg", "W004电脑"));
                list.add(new HomeContentInfo("https://file03.16sucai.com/2017/1100/16sucai_p566c008_052.JPG", "W005电脑"));
                list.add(new HomeContentInfo("https://img95.699pic.com/photo/30011/8774.jpg_wh300.jpg%21/fh/300/quality/90", "W006电脑"));
                list.add(new HomeContentInfo("https://img2.baidu.com/it/u=3915321900,11203426&fm=253&fmt=auto&app=138&f=JPEG?w=638&h=395", "W007电脑"));
                list.add(new HomeContentInfo("https://img0.baidu.com/it/u=1527663157,1089966240&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800", "W008电脑"));
                list.add(new HomeContentInfo("https://lmg.jj20.com/up/allimg/4k/s/01/210924113Z1I26-0-lp.jpg", "W009电脑"));
                list.add(new HomeContentInfo("https://pic.52112.com/2019/05/22/JPG-190522_644/jbPiy44IVo_small.jpg", "W0010电脑"));
            } else if (tab == 3) {
                // 电子配件
                list.add(new HomeContentInfo("https://img2.baidu.com/it/u=1312977597,3902060716&fm=253&fmt=auto&app=138&f=JPEG?w=966&h=500", "P001配件"));
                list.add(new HomeContentInfo("https://pic.quanjing.com/fd/ll/QJ6770215108.jpg@%21350h", "P002配件"));
                list.add(new HomeContentInfo("https://img1.baidu.com/it/u=978912865,1226982215&fm=253&fmt=auto&app=138&f=JPEG?w=543&h=500", "P003配件"));
                list.add(new HomeContentInfo("https://file03.16sucai.com/2017/1100/16sucai_P59202G175.JPG", "P004配件"));
                list.add(new HomeContentInfo("https://img0.baidu.com/it/u=924622588,1503872559&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500", "P005配件"));
                list.add(new HomeContentInfo("https://lmg.jj20.com/up/allimg/tp07/250417214333672-lp.jpg", "P006配件"));
                list.add(new HomeContentInfo("https://file03.16sucai.com/2017/1100/16sucai_P59202G170.JPG", "P007配件"));
                list.add(new HomeContentInfo("https://img0.baidu.com/it/u=1866135918,2985709255&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500", "P008配件"));
                list.add(new HomeContentInfo("https://img2.baidu.com/it/u=4136829161,3443612472&fm=253&fmt=auto&app=138&f=JPEG?w=754&h=500", "P009配件"));
                list.add(new HomeContentInfo("https://img1.baidu.com/it/u=2244671331,3315857175&fm=253&fmt=auto&app=138&f=JPEG?w=662&h=500", "P0010配件"));
            }

        } else {
            if (tab == 0) {
                // 新发现
                list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1115%2F40b58e25j00r2lr47001wc000hs00vlc.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "D0011动漫"));
                list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1109%2Fdd0d98caj00r2asr5002nc000hs00r5c.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "D0012动漫"));
                list.add(new HomeContentInfo("https://img.qzonei.com/data/attachment/portal/202206/30/102432q0lnhvlxqez7nb7e.jpg", "D0013动漫"));
                list.add(new HomeContentInfo("https://pics2.baidu.com/feed/0b7b02087bf40ad173890dc7bbdb39d9a8eccea7.jpeg?token=a8e0ebe159ca580d31e202d5c627a1d0&s=2575168FC002E4FF5E95E6AB0300E017", "D0014动漫"));
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F202002%2F16%2F20200216165019_etsba.thumb.1000_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692254761&t=0d2cf957c4d71db98791a11a84c30059", "D0015动漫"));
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201806%2F16%2F20180616112359_ketow.jpg&refer=http%3A%2F%2Fb-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692254770&t=e96c6215aa071b4aa32fd6ae01cfe2c8", "D0016动漫"));
                list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1030%2F97f21769j00r1s70y001pc000hs00vnc.jpg&thumbnail=650x2147483647&quality=80&type=jpg", "D0017动漫"));
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw%2F63525cbc-130f-4f52-9ad1-bc87693a1900%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692254799&t=18b702ec5d4b78b02410f9c89b4b1df3", "D0018动漫"));
                list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1030%2F8ead0baaj00r1s70y0029c000hs00vmc.jpg&thumbnail=650x2147483647&quality=80&type=jpg", "D0019动漫"));
                list.add(new HomeContentInfo("https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1205%2F1b0fce8cj00r3m5lx0025c000hs00vjc.jpg&thumbnail=660x2147483647&quality=80&type=jpg", "D0020动漫"));
            } else if (tab == 1) {
                // 手机
                list.add(new HomeContentInfo("https://img2.baidu.com/it/u=980606248,276058411&fm=253&fmt=auto&app=138&f=JPEG?w=333&h=499", "S0011手机"));
                list.add(new HomeContentInfo("https://img0.baidu.com/it/u=3500927138,3715877627&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=751", "S0012手机"));
                list.add(new HomeContentInfo("https://2d.zol-img.com.cn/product/152_1200x900/397/cecEhCtGzSjPQ.jpg", "S0013手机"));
                list.add(new HomeContentInfo("https://img0.baidu.com/it/u=1226771838,3845268511&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500", "S0014手机"));
                list.add(new HomeContentInfo("https://img2.baidu.com/it/u=323450749,3168380217&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500", "S0015手机"));
                list.add(new HomeContentInfo("https://img2.baidu.com/it/u=2492856519,2838102694&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=726", "S0016手机"));
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fcbu01.alicdn.com%2Fimg%2Fibank%2FO1CN01IiqpeR2EomwWBTKoQ_%21%212568558792-0-cib.jpg&refer=http%3A%2F%2Fcbu01.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692418422&t=4c7dfaa93cf4275cecbcb55aecd38866", "S0017手机"));
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fcbu01.alicdn.com%2Fimg%2Fibank%2F2017%2F921%2F154%2F5027451129_303013434.310x310.jpg&refer=http%3A%2F%2Fcbu01.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692418440&t=70c96b119868eda0808203fc68d8ffbd", "S0018手机"));
                list.add(new HomeContentInfo("https://img0.baidu.com/it/u=1360720020,3486529774&fm=253&fmt=auto&app=138&f=JPEG?w=460&h=460", "S0019手机"));
                list.add(new HomeContentInfo("https://img0.baidu.com/it/u=3258645793,2587405711&fm=253&fmt=auto&app=138&f=JPEG?w=310&h=310", "S0020手机"));
            } else if (tab == 2) {
                // 电脑办公
                list.add(new HomeContentInfo("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201509%2F14%2F20150914224828_WszXK.jpeg&refer=http%3A%2F%2Fb-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1692418761&t=ae781f1f79d3905aa9e1c30f6f5e8a6c", "W0011电脑"));
                list.add(new HomeContentInfo("https://file.xiazaii.com/file/img/202001121412/1_200113215241_3.jpg", "W0012电脑"));
                list.add(new HomeContentInfo("https://desk-fd.zol-img.com.cn/t_s960x600c5/g2/M00/05/00/ChMlWV0_rzqIfDxbACIYZPbIacMAAMNigFIhk4AIhh8497.jpg", "W0013电脑"));
                list.add(new HomeContentInfo("https://img-qn.51miz.com/2017/06/28/01/2017062801521438_P1227794_80e19641O.jpg", "W0014电脑"));
                list.add(new HomeContentInfo("https://img2.baidu.com/it/u=2084488935,3977133337&fm=253&fmt=auto&app=138&f=JPEG?w=750&h=500", "W0015电脑"));
                list.add(new HomeContentInfo("https://seopic.699pic.com/photo/50062/5876.jpg_wh1200.jpg", "W0016电脑"));
                list.add(new HomeContentInfo("https://img1.baidu.com/it/u=1482478410,3665694973&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=313", "W0017电脑"));
                list.add(new HomeContentInfo("https://img-qn.51miz.com/preview/photo/00/01/49/73/2392a599_P1497360_e84a890dO.jpg", "W0018电脑"));
                list.add(new HomeContentInfo("https://img95.699pic.com/photo/50180/8334.jpg_wh860.jpg", "W0019电脑"));
                list.add(new HomeContentInfo("https://file.moyublog.com/d/file/2020-12-09/b164205ef76a71fa2bd935af3c7bcca4.jpg", "W0020电脑"));
            } else if (tab == 3) {
                // 电子配件
                list.add(new HomeContentInfo("https://img1.baidu.com/it/u=295179711,439455154&fm=253&fmt=auto&app=138&f=JPEG?w=750&h=500", "P0011配件"));
                list.add(new HomeContentInfo("https://img2.baidu.com/it/u=4041880736,2135828973&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500", "P0012配件"));
                list.add(new HomeContentInfo("https://img0.baidu.com/it/u=3183700906,4067009152&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=345", "P0013配件"));
                list.add(new HomeContentInfo("https://img2.baidu.com/it/u=1173004868,2826384292&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=383", "P0014配件"));
                list.add(new HomeContentInfo("https://img1.baidu.com/it/u=3297464596,2791358192&fm=253&fmt=auto&app=138&f=JPEG?w=775&h=500", "P0015配件"));
                list.add(new HomeContentInfo("https://img1.baidu.com/it/u=240551229,4254651822&fm=253&fmt=auto&app=138&f=JPEG?w=964&h=500", "P0016配件"));
                list.add(new HomeContentInfo("https://img2.baidu.com/it/u=2356737520,832309119&fm=253&fmt=auto&app=138&f=JPEG?w=787&h=500", "P0017配件"));
                list.add(new HomeContentInfo("https://img0.baidu.com/it/u=1578946035,776201033&fm=253&fmt=auto&app=138&f=JPEG?w=680&h=453", "P0018配件"));
                list.add(new HomeContentInfo("https://img2.baidu.com/it/u=122494561,2157887037&fm=253&fmt=auto&app=138&f=JPEG?w=814&h=500", "P0019配件"));
                list.add(new HomeContentInfo("https://img0.baidu.com/it/u=508432878,593172356&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=373", "P0020配件"));
            }

        }

        info.setList(list);
        info.setPages(2);
        mAdapter.onRefreshChildData(tab, info);
    }
}