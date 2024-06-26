package com.zqw.mobile.grainfull.mvp.presenter;

import static com.zqw.mobile.grainfull.BuildConfig.IS_DEBUG_DATA;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.app.utils.RxUtils;
import com.zqw.mobile.grainfull.mvp.contract.LayoutForumContract;
import com.zqw.mobile.grainfull.mvp.model.entity.BannerBean;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeInfoResponse;
import com.zqw.mobile.grainfull.mvp.model.entity.MenuBean;
import com.zqw.mobile.grainfull.mvp.model.entity.TabBean;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/07/05 11:06
 * ================================================
 */
@ActivityScope
public class LayoutForumPresenter extends BasePresenter<LayoutForumContract.Model, LayoutForumContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public LayoutForumPresenter(LayoutForumContract.Model model, LayoutForumContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }

    /**
     * 获取数据
     */
    public void initData() {
        if (IS_DEBUG_DATA) {
            // 加载Banner数据
            List<BannerBean> mBannerList = new ArrayList<>();
            mBannerList.add(new BannerBean("https://m15.360buyimg.com/mobilecms/jfs/t1/218369/27/14203/132191/6226a702E5a0b9236/a11294e884bc7635.jpg!cr_1053x420_4_0!q70.jpg","1"));
            mBannerList.add(new BannerBean("https://m15.360buyimg.com/mobilecms/jfs/t1/158791/25/27003/106834/620c4bc2Efb15fc57/7c89841a597ce41b.jpg!cr_1053x420_4_0!q70.jpg","2"));
            mBannerList.add(new BannerBean("https://m15.360buyimg.com/mobilecms/jfs/t1/121592/2/24818/138081/622ccc8fEdf840f95/cd229433d699c70c.jpg!cr_1053x420_4_0!q70.jpg.dpg.webp","4"));
            mBannerList.add(new BannerBean("https://img01.yohoboys.com/contentimg/2017/09/21/18/01153d76e13f27594dcadd9be3eefb5c2c.jpg","5"));
            mRootView.loadBanner(mBannerList);

            // 加载广告数据
            mRootView.loadAdvertisingBar("https://m15.360buyimg.com/mobilecms/jfs/t1/105817/5/25878/84922/622f2e3eE548c75b1/a564811c5763d4e8.png!q70.jpg");

            // 加载菜单数据
            List<MenuBean> nineMenuList = new ArrayList<>();
            nineMenuList.add(new MenuBean("https://m15.360buyimg.com/mobilecms/jfs/t1/102632/18/20350/13255/61e131feE788947ef/c391b8590cdf427e.png!q70.jpg","超市","m01","https://prodev.m.jd.com/mall/active/3xhqjGH1wMz5FaMgrfYhR22sFvqz/index.html"));
            nineMenuList.add(new MenuBean("https://m15.360buyimg.com/mobilecms/jfs/t1/146216/7/22157/182757/622f37e8Ec2445bac/658b95154cb12771.gif","电器","m02","https://prodev.m.jd.com/mall/active/8tHNdJLcqwqhkLNA8hqwNRaNu5f/index.html"));
            nineMenuList.add(new MenuBean("https://m15.360buyimg.com/mobilecms/jfs/t1/104637/39/25735/190791/622f3682E168960d2/2dbfbaf4147134c1.gif","国际","m11","https://gmart.jd.com"));
            nineMenuList.add(new MenuBean("https://m15.360buyimg.com/mobilecms/jfs/t1/172178/32/9487/10851/609d18e0Ed273eec1/a1e099f1601c8cc2.png!q70.jpg","服饰","m03","https://h5.m.jd.com/pb/014076750/48qPXkwvatBBCAhdeTXG5WQam4yq/index.html"));
            nineMenuList.add(new MenuBean("https://m15.360buyimg.com/mobilecms/jfs/t1/193038/4/3881/13295/60a4b6a7E4124dc9e/fdd2934e97eab3ac.png!q70.jpg","免费水果","m04",""));
            nineMenuList.add(new MenuBean("https://m15.360buyimg.com/mobilecms/jfs/t1/107776/17/21311/9709/61e12b6eEa79cbefa/bd0bb902e126fafb.png!q70.jpg","会员","m10","https://shop.m.jd.com/shop/home?shopId=1000343263&utm_source=iosapp&utm_medium=appshare&utm_campaign=t_335139774&utm_term=Wxfriends&ad_od=share&utm_user=plusmember&gx=RnAomTM2bjTfycsT-YNzCTmOd9y9Fa0&gxd=RnAokmcIOjONmssR_YNyCIMKIb4Xa5DwoYs6Qbze_4h9aUHPDecui7HTjqAWskU"));
            nineMenuList.add(new MenuBean("https://m15.360buyimg.com/mobilecms/jfs/t1/189533/37/5319/12852/60b05d59Eb3b3fd29/4c478e3347507e51.png!q70.jpg","生鲜","m13","https://pro.m.jd.com/mall/active/4P9a2T9osR9JvtzHVaYTPvsecRtg/index.html"));
            nineMenuList.add(new MenuBean("https://m15.360buyimg.com/mobilecms/jfs/t1/188405/35/3707/11116/60a26055Ef91c306d/1ba7aa3b9328e35e.png!q70.jpg","拍拍二手","m17","https://prodev.m.jd.com/mall/active/LHGZv1DrGkva1jNpUkKFuNFN6oo/index.html"));
            nineMenuList.add(new MenuBean("https://m15.360buyimg.com/mobilecms/jfs/t1/181856/30/3968/10274/609e3d99E07c63af4/cb0d5b21c461b07f.png!q70.jpg","喜喜","m05",""));
            nineMenuList.add(new MenuBean("https://m15.360buyimg.com/mobilecms/jfs/t1/91538/20/21166/8105/61e128b9E06558f00/2c9273cdee9b2e3d.png!q70.jpg","生活缴费","m06",""));
            nineMenuList.add(new MenuBean("https://m15.360buyimg.com/mobilecms/jfs/t1/185536/13/17333/14522/6108a9f0E62572408/8222cc8a66134776.png!q70.jpg","领豆","m07",""));
            nineMenuList.add(new MenuBean("https://m15.360buyimg.com/mobilecms/jfs/t1/175369/35/26446/17302/61e12945E09ef2a2f/87b391aff2da560a.png!q70.jpg","领券","m08",""));
            nineMenuList.add(new MenuBean("https://m15.360buyimg.com/mobilecms/jfs/t1/127505/17/20504/10647/61e112b0E4f382c96/7a042862fc7c479e.png!q70.jpg","领金贴","m09",""));
            nineMenuList.add(new MenuBean("https://m15.360buyimg.com/mobilecms/jfs/t1/129902/19/16911/11005/60b05dd8Edad29a3f/ca6b3ea0f67e1826.png!q70.jpg","沃尔玛","m14",""));
            nineMenuList.add(new MenuBean("https://m15.360buyimg.com/mobilecms/jfs/t1/192361/9/5316/11815/60b05d25Eec7f6b5e/5555dcc59d99428d.png!q70.jpg","旅行","m15",""));
            nineMenuList.add(new MenuBean("https://m15.360buyimg.com/mobilecms/jfs/t1/109159/13/17721/9654/60b05d73Eefa154db/3a4a46ef2755c622.png!q70.jpg","看病购药","m16",""));
            nineMenuList.add(new MenuBean("https://m15.360buyimg.com/mobilecms/jfs/t1/177688/12/4847/7352/60a39a9bEe0a7e4f8/1a8efe458d1c3ee2.png!q70.jpg","种豆得豆","m18",""));
            nineMenuList.add(new MenuBean("https://m15.360buyimg.com/mobilecms/jfs/t1/184718/8/4051/11977/609e5222Ea2816259/d29fec6d4d8558f1.png!q70.jpg","萌宠","m19",""));
            nineMenuList.add(new MenuBean("https://m15.360buyimg.com/mobilecms/jfs/t1/126310/38/18675/7912/60b05e32E736cb5fe/2bb8508e955b88ee.png!q70.jpg","更多频道","m20",""));

            mRootView.loadNineMenu(nineMenuList);

            // 加载Tab数据
            List<TabBean> mList = new ArrayList<>();
            mList.add(new TabBean("精选", "1"));
            mList.add(new TabBean("新品", "2"));
            mList.add(new TabBean("直播", "3"));
            mList.add(new TabBean("实惠", "4"));
            mList.add(new TabBean("进口", "5"));
            mRootView.showTabLayout(mList);
        } else {
            mModel.queryHomePageInfo()
                    .compose(RxUtils.applySchedulers(mRootView))
                    .subscribe(new ErrorHandleSubscriber<HomeInfoResponse>(mErrorHandler) {

                        @Override
                        public void onNext(HomeInfoResponse infoResponse) {
                            if (infoResponse != null) {
                                // 加载Banner数据
                                mRootView.loadBanner(infoResponse.getBannerList());
                                // 加载广告数据
                                mRootView.loadAdvertisingBar(infoResponse.getAdUrl());
                                // 加载菜单数据
                                mRootView.loadNineMenu(infoResponse.getNineMenuList());
                                // 加载Tab数据
                                mRootView.showTabLayout(infoResponse.getTabList());
                            }
                        }
                    });
        }
    }

}