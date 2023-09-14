package com.zqw.mobile.grainfull.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.mvp.contract.MineContract;
import com.zqw.mobile.grainfull.mvp.model.entity.GoodsBean;
import com.zqw.mobile.grainfull.mvp.model.entity.TabBean;
import com.zqw.mobile.grainfull.mvp.ui.adapter.GoodsListAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/09/05 17:34
 * ================================================
 */
@ActivityScope
public class MinePresenter extends BasePresenter<MineContract.Model, MineContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    List<GoodsBean> mList;
    @Inject
    GoodsListAdapter mAdapter;

    @Inject
    public MinePresenter(MineContract.Model model, MineContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 初始化数据
     */
    public void initGoodsList() {
        // 加载Tab数据
        List<TabBean> mTabList = new ArrayList<>();
        mTabList.add(new TabBean("精选推荐", "1"));
        mTabList.add(new TabBean("趋势精选", "2"));
        mTabList.add(new TabBean("骑行日记", "3"));
        mTabList.add(new TabBean("型酷搭配", "4"));
        mTabList.add(new TabBean("野餐出游", "5"));
        mTabList.add(new TabBean("温暖出行", "6"));
        mTabList.add(new TabBean("伴你旅行", "7"));
        mTabList.add(new TabBean("夏日防晒", "8"));
        mRootView.showTabLayout(mTabList);

        // 加载内容
        mList.clear();
        mList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/223778/18/6959/168828/622cc318E47c3b663/753cd44c08858430.jpg!q70.dpg.webp", "", "", "时尚运动", "活动惊爆价", "实惠好货等你抢", 2));
        mList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/208465/36/8682/93776/618a6339E208f6217/44fbee25f6b7d872.jpg", "帅电乔1男休闲运动篮球鞋倒钩TS 女鞋深棕鬼脸", "799.00", "", "", "", 1));
        mList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s1080x1080_jfs/t1/185796/23/23365/154983/625d9644E96e93807/0e30192ad676f8a8.jpg!q70.jpg.webp", "", "", "好物集市", "专业运动装备", "运动与你相伴", 2));
        mList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/106053/8/25901/375428/622cc43dE7be3f2c7/8de7c9f9f9691068.jpg", "ASICS 亚瑟士 男鞋运动鞋 GEL-VENTURE 7 MX 抓地缓冲越野跑鞋 黑色", "590.00", "", "", "", 1));
        mList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/174007/12/4609/59798/607947f3Ed185a0e8/339959c1bb02c110.jpg", "塞洛蒙（Salomon）男款 户外运动轻便舒适网布透气排水浅滩涉水溯溪鞋 AMPHIB BOLD", "798.00", "", "", "", 1));
        mList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/223761/1/3118/231155/61de4e31E67751289/7a863c20f2dfdb01.jpg", "蝴蝶乒乓球鞋 男比赛专业训练鞋防滑透气轻便耐磨鞋", "268.60", "", "", "", 1));
        mList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/85379/5/23716/185324/622b6ff4E9c5b48d9/48e7d4d725bb654c.jpg!q70.dpg.webp", "乔丹官方旗舰板鞋春新款网透气休闲运动鞋高帮轻便搭扣滑板鞋", "205.10", "", "", "", 1));
        mList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/89996/21/25563/131810/622ce8faE08399a9a/0554f25e84207de0.jpg!q70.dpg.webp", "yysports 阿迪达斯adidas男鞋女鞋 三叶草 OZWE", "809.00", "", "", "", 1));
        mList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/183854/38/19727/543220/611cd169E30d7d836/f043008c71aafcb3.jpg!q70.dpg.webp", "帅电乔1男休闲运动篮球鞋倒钩TS 女鞋深棕鬼脸", "799.00", "", "", "", 1));
        mList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/146075/32/25809/139065/622b435aE314833fc/b977c681b857c136.jpg!q70.dpg.webp", "ASICS 亚瑟士 男鞋运动鞋 GEL-VENTURE 7 MX 抓地缓冲越野跑鞋 黑色", "590.00", "", "", "", 1));
        mList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/142532/19/20779/102990/61976e06Ee88d1a31/c14a124344d5b88b.jpg!q70.dpg.webp", "帅电乔1男休闲运动篮球鞋倒钩TS 女鞋深棕鬼脸", "799.00", "", "", "", 1));
        mList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/137009/10/23801/161776/622cc71eE068ed3fe/f94465f4d2a1a911.jpg!q70.dpg.webp", "ASICS 亚瑟士 男鞋运动鞋 GEL-VENTURE 7 MX 抓地缓冲越野跑鞋 黑色", "590.00", "", "", "", 1));
        mList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/119098/1/23199/165424/622cc317E2d149455/25323fdd66edf577.jpg!q70.dpg.webp", "ASICS亚瑟士 女鞋运动鞋稳定旗舰跑鞋耐磨 GE", "1399.00", "", "", "", 1));
        mList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/207289/24/3349/344826/61592344Efc667435/2ef0dac91e137342.jpg!q70.dpg.webp", "ASICS 亚瑟士 男鞋运动鞋 GEL-VENTURE 7 MX 抓地缓冲越野跑鞋 黑色", "590.00", "", "", "", 1));
        mList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/212981/31/14635/156447/622b42a9Eb150cd2e/2f5292cbcfd99480.jpg!q70.dpg.webp", "帅电乔1男休闲运动篮球鞋倒钩TS 女鞋深棕鬼脸", "799.00", "", "", "", 1));
        mList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/104661/29/25123/136724/622cc70eE81d81a49/dada0961ff208767.jpg", "ASICS 亚瑟士 休闲鞋低帮运动鞋舒适透气 COO", "390.00", "", "", "", 1));

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        if (mList != null) {
            mList.clear();
            this.mList = null;
        }
        this.mAdapter = null;
    }
}