package com.zqw.mobile.grainfull.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.mvp.contract.GoodsDetailContract;
import com.zqw.mobile.grainfull.mvp.model.entity.AppraiseBean;
import com.zqw.mobile.grainfull.mvp.model.entity.BannerBean;
import com.zqw.mobile.grainfull.mvp.model.entity.DetailInfo;
import com.zqw.mobile.grainfull.mvp.model.entity.GoodsBean;
import com.zqw.mobile.grainfull.mvp.model.entity.GoodsInfo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2025/02/18 11:35
 * ================================================
 */
@ActivityScope
public class GoodsDetailPresenter extends BasePresenter<GoodsDetailContract.Model, GoodsDetailContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public GoodsDetailPresenter(GoodsDetailContract.Model model, GoodsDetailContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }

    /**
     * 查询商品详情
     */
    public void queryGoodsDetail() {
        // banner
        List<BannerBean> bannerList = new ArrayList<>();
        List<String> imgList1 = new ArrayList<>();
        imgList1.add("https://m.360buyimg.com/mobilecms/s1080x1080_jfs/t1/212471/24/17530/169099/625d9642Eadb21db3/f5c28554c39fee6d.jpg!q70.jpg.webp");
        imgList1.add("https://m.360buyimg.com/mobilecms/s1080x1080_jfs/t1/223092/16/13147/109176/623043b7Ec33b6057/8dff30acead53213.jpg!q70.jpg.webp");
        imgList1.add("https://m.360buyimg.com/mobilecms/s1080x1080_jfs/t1/217344/6/14891/89067/623043b7E49c2bf31/1c8f4ba0ccf38c9e.jpg!q70.jpg.webp");
        imgList1.add("https://m.360buyimg.com/mobilecms/s1080x1080_jfs/t1/145881/30/24164/150428/623043b7E8fb57fea/44bdb9081eaec2be.jpg!q70.jpg.webp");
        imgList1.add("https://m.360buyimg.com/mobilecms/s1080x1080_jfs/t1/85372/28/21455/69456/623043b7Ef403bd0a/8ca83cf7694ebd07.jpg!q70.jpg.webp");
        imgList1.add("https://m.360buyimg.com/mobilecms/s1080x1080_jfs/t1/87488/39/23761/210846/623043b8E15396993/e71af88949e071ed.jpg!q70.jpg.webp");
        imgList1.add("https://m.360buyimg.com/mobilecms/s1080x1080_jfs/t1/185539/31/21780/117681/6230217cE99cb7265/4de912b7b8ae3bfe.jpg!q70.jpg.webp");
        bannerList.add(new BannerBean("color1", "黑灰色/红色", "https://m.360buyimg.com/n2/jfs/t1/212471/24/17530/169099/625d9642Eadb21db3/f5c28554c39fee6d.jpg", imgList1, true));

        List<String> imgList2 = new ArrayList<>();
        imgList2.add("https://m.360buyimg.com/mobilecms/s1080x1080_jfs/t1/185796/23/23365/154983/625d9644E96e93807/0e30192ad676f8a8.jpg!q70.jpg.webp");
        imgList2.add("https://m.360buyimg.com/mobilecms/s1080x1080_jfs/t1/117422/24/20800/87701/62295f18Edbec5730/a1a9fae9bed57523.jpg!q70.jpg.webp");
        imgList2.add("https://m.360buyimg.com/mobilecms/s1080x1080_jfs/t1/122414/38/23345/79829/62295f18E274d9ffa/b9d2b41d33493999.jpg!q70.jpg.webp");
        imgList2.add("https://m.360buyimg.com/mobilecms/s1080x1080_jfs/t1/147359/11/22931/135736/62295f19Edd661297/4cac5b0ea0c5a0da.jpg!q70.jpg.webp");
        imgList2.add("https://m.360buyimg.com/mobilecms/s1080x1080_jfs/t1/89066/12/22981/53911/62295f19Eff96ea37/a93e6f167964bcfb.jpg!q70.jpg.webp");
        imgList2.add("https://m.360buyimg.com/mobilecms/s1080x1080_jfs/t1/214240/2/14478/182817/62295f19Eb7afb2f5/11e72ba025a0da3c.jpg!q70.jpg.webp");
        imgList2.add("https://m.360buyimg.com/mobilecms/s1080x1080_jfs/t1/97439/8/24039/117681/622842a3E760fbb99/e4ad2acca9283634.jpg!q70.jpg.webp");
        bannerList.add(new BannerBean("color2", "蓝色/黄色", "https://m.360buyimg.com/mobilecms/s1080x1080_jfs/t1/185796/23/23365/154983/625d9644E96e93807/0e30192ad676f8a8.jpg!q70.jpg.webp", imgList2));

        // 商品信息
        List<String> tagList = new ArrayList<>();
        tagList.add("买满300减40");
        tagList.add("满199减20");
        tagList.add("满49减1");

        List<AppraiseBean> mAppraiseList = new ArrayList<>();
        mAppraiseList.add(new AppraiseBean("https://ik.imagekit.io/guoguodad/mall/WechatIMG17.jpeg", "大**行", "外观颜值:外形很好看 别具一格再增加点颜色选择就更好了", 1, "蓝色/黄色", "44"));
        mAppraiseList.add(new AppraiseBean("https://ik.imagekit.io/guoguodad/mall/iShot2022-04-22_11.00.41.png", 2));
        mAppraiseList.add(new AppraiseBean("https://ik.imagekit.io/guoguodad/mall/header.jpg", "梦**仙", "第一次买亚瑟士的跑鞋，可以说很惊喜了，穿着很舒服，回弹也很到位，跑鞋非常专业！", 1, "黑灰色/红色", "42"));
        mAppraiseList.add(new AppraiseBean("https://ik.imagekit.io/guoguodad/mall/iShot2022-04-22_11.01.00.png", 2));
        mAppraiseList.add(new AppraiseBean("https://ik.imagekit.io/guoguodad/mall/iShot2022-04-22_11.01.00.png", 2));
        GoodsInfo goodsInfo = new GoodsInfo("890.00", "810", tagList, "ASICS亚瑟士 2022春夏男鞋稳定支持经典跑鞋舒适回弹运动鞋 GT-2000 10 蓝色/黄色", mAppraiseList);

        // 商品详情
        List<String> introductionList = new ArrayList<>();
        introductionList.add("https://img30.360buyimg.com/popWareDetail/jfs/t1/130765/11/26608/146901/624d5c6aEe590921f/b8bfe5686b57e194.jpg.dpg");
        introductionList.add("https://img30.360buyimg.com/popWareDetail/jfs/t1/129566/36/28066/71317/624d5c69E0b9ba09c/a5f6f70467c6b0a6.jpg.dpg");
        introductionList.add("https://img30.360buyimg.com/popWareDetail/jfs/t1/109170/17/27071/49050/624d5c69Ed53303f8/bec3690c3ea0ed3a.jpg.dpg");
        introductionList.add("https://img30.360buyimg.com/popWareDetail/jfs/t1/189785/35/22461/262616/624d5c69Ee2835623/e6377e50fa1e1b2f.jpg.dpg");
        introductionList.add("https://img30.360buyimg.com/popWareDetail/jfs/t1/121994/38/26242/215472/624d5c69E93b2b771/9eb4e5c1dced910f.jpg.dpg");
        introductionList.add("https://img30.360buyimg.com/popWareDetail/jfs/t1/200046/18/20726/293757/624d5c69Ea7628ea7/47fa0c49693dcc71.jpg.dpg");
        introductionList.add("https://img30.360buyimg.com/popWareDetail/jfs/t1/116482/24/23676/268365/624d5c69Eeb48ab6c/4e6006511b0cc5d6.jpg.dpg");
        introductionList.add("https://img30.360buyimg.com/popWareDetail/jfs/t1/99540/22/26782/8746/624d5c68E08308a06/404b26a0d0a18103.jpg.dpg");
        introductionList.add("https://img30.360buyimg.com/popWareDetail/jfs/t1/205726/37/20745/137848/624d5c68E0daebe01/afd9b75c4ff05764.jpg.dpg");
        introductionList.add("https://img30.360buyimg.com/popWareDetail/jfs/t1/205726/37/20745/137848/624d5c68E0daebe01/afd9b75c4ff05764.jpg.dpg");
        introductionList.add("https://img30.360buyimg.com/popWareDetail/jfs/t1/216582/30/16606/127427/624d5c68E619d2d6d/c8cf40cac78e0532.jpg.dpg");
        introductionList.add("https://img30.360buyimg.com/popWareDetail/jfs/t1/166575/9/22244/214537/624d5c69E28b09fb7/1e52c37e4d539d0b.jpg.dpg");
        introductionList.add("https://img30.360buyimg.com/popWareDetail/jfs/t1/87859/27/25402/137998/624d5c68E6d8dc293/e0dc610050c48363.jpg.dpg");
        introductionList.add("https://img30.360buyimg.com/popWareDetail/jfs/t1/213761/40/16381/110431/624d5c68Eb09a4fca/acc39177f635cb84.jpg.dpg");
        introductionList.add("https://img30.360buyimg.com/popWareDetail/jfs/t1/213761/40/16381/110431/624d5c68Eb09a4fca/acc39177f635cb84.jpg.dpg");

        List<String> serviceList = new ArrayList<>();
        serviceList.add("https://img30.360buyimg.com/sku/jfs/t1/118720/15/16298/415064/5f487872E0d237cd1/283084254c9e270a.jpg.dpg");

        DetailInfo detailInfo = new DetailInfo("https://img13.360buyimg.com/cms/jfs/t1/197792/33/22051/102624/625d0076Ef4252b55/fdf3b1d52374f37d.jpg.dpg", "https://img11.360buyimg.com/cms/jfs/t1/131411/22/20981/284862/6259240fE7980bcbe/b2b9db04abe679f9.jpg.dpg", introductionList, serviceList);

        List<GoodsBean> goodsList = new ArrayList<>();
        goodsList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/223778/18/6959/168828/622cc318E47c3b663/753cd44c08858430.jpg!q70.dpg.webp", "", "", "时尚运动", "活动惊爆价", "实惠好货等你抢", 2));
        goodsList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/208465/36/8682/93776/618a6339E208f6217/44fbee25f6b7d872.jpg", "帅电乔1男休闲运动篮球鞋倒钩TS 女鞋深棕鬼脸", "799.00", "", "", "", 1));
        goodsList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s1080x1080_jfs/t1/185796/23/23365/154983/625d9644E96e93807/0e30192ad676f8a8.jpg!q70.jpg.webp", "", "", "好物集市", "专业运动装备", "运动与你相伴", 2));
        goodsList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/106053/8/25901/375428/622cc43dE7be3f2c7/8de7c9f9f9691068.jpg", "ASICS 亚瑟士 男鞋运动鞋 GEL-VENTURE 7 MX 抓地缓冲越野跑鞋 黑色", "590.00", "", "", "", 1));
        goodsList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/174007/12/4609/59798/607947f3Ed185a0e8/339959c1bb02c110.jpg", "塞洛蒙（Salomon）男款 户外运动轻便舒适网布透气排水浅滩涉水溯溪鞋 AMPHIB BOLD", "798.00", "", "", "", 1));
        goodsList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/223761/1/3118/231155/61de4e31E67751289/7a863c20f2dfdb01.jpg", "蝴蝶乒乓球鞋 男比赛专业训练鞋防滑透气轻便耐磨鞋", "268.60", "", "", "", 1));
        goodsList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/85379/5/23716/185324/622b6ff4E9c5b48d9/48e7d4d725bb654c.jpg!q70.dpg.webp", "乔丹官方旗舰板鞋春新款网透气休闲运动鞋高帮轻便搭扣滑板鞋", "205.10", "", "", "", 1));
        goodsList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/89996/21/25563/131810/622ce8faE08399a9a/0554f25e84207de0.jpg!q70.dpg.webp", "yysports 阿迪达斯adidas男鞋女鞋 三叶草 OZWE", "809.00", "", "", "", 1));
        goodsList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/183854/38/19727/543220/611cd169E30d7d836/f043008c71aafcb3.jpg!q70.dpg.webp", "帅电乔1男休闲运动篮球鞋倒钩TS 女鞋深棕鬼脸", "799.00", "", "", "", 1));
        goodsList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/146075/32/25809/139065/622b435aE314833fc/b977c681b857c136.jpg!q70.dpg.webp", "ASICS 亚瑟士 男鞋运动鞋 GEL-VENTURE 7 MX 抓地缓冲越野跑鞋 黑色", "590.00", "", "", "", 1));
        goodsList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/142532/19/20779/102990/61976e06Ee88d1a31/c14a124344d5b88b.jpg!q70.dpg.webp", "帅电乔1男休闲运动篮球鞋倒钩TS 女鞋深棕鬼脸", "799.00", "", "", "", 1));
        goodsList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/137009/10/23801/161776/622cc71eE068ed3fe/f94465f4d2a1a911.jpg!q70.dpg.webp", "ASICS 亚瑟士 男鞋运动鞋 GEL-VENTURE 7 MX 抓地缓冲越野跑鞋 黑色", "590.00", "", "", "", 1));
        goodsList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/119098/1/23199/165424/622cc317E2d149455/25323fdd66edf577.jpg!q70.dpg.webp", "ASICS亚瑟士 女鞋运动鞋稳定旗舰跑鞋耐磨 GE", "1399.00", "", "", "", 1));
        goodsList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/207289/24/3349/344826/61592344Efc667435/2ef0dac91e137342.jpg!q70.dpg.webp", "ASICS 亚瑟士 男鞋运动鞋 GEL-VENTURE 7 MX 抓地缓冲越野跑鞋 黑色", "590.00", "", "", "", 1));
        goodsList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/212981/31/14635/156447/622b42a9Eb150cd2e/2f5292cbcfd99480.jpg!q70.dpg.webp", "帅电乔1男休闲运动篮球鞋倒钩TS 女鞋深棕鬼脸", "799.00", "", "", "", 1));
        goodsList.add(new GoodsBean("https://m.360buyimg.com/mobilecms/s714x714_jfs/t1/104661/29/25123/136724/622cc70eE81d81a49/dada0961ff208767.jpg", "ASICS 亚瑟士 休闲鞋低帮运动鞋舒适透气 COO", "390.00", "", "", "", 1));

        // 加载数据
        mRootView.loadData(bannerList, goodsInfo, detailInfo, goodsList);
    }
}