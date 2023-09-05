package com.zqw.mobile.grainfull.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.mvp.contract.LayoutCategoryContract;
import com.zqw.mobile.grainfull.mvp.model.entity.CateBean;
import com.zqw.mobile.grainfull.mvp.model.entity.CategoryBean;
import com.zqw.mobile.grainfull.mvp.model.entity.ContentCateResponse;
import com.zqw.mobile.grainfull.mvp.ui.adapter.CategoryListAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/09/04 12:01
 * ================================================
 */
@ActivityScope
public class LayoutCategoryPresenter extends BasePresenter<LayoutCategoryContract.Model, LayoutCategoryContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    List<CategoryBean> mList;

    @Inject
    CategoryListAdapter mAdapter;

    @Inject
    public LayoutCategoryPresenter(LayoutCategoryContract.Model model, LayoutCategoryContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获取左侧数据
     */
    public void queryCategoryList() {
        mList.clear();
        mList.add(new CategoryBean("推荐分类", "000", true, ""));
        mList.add(new CategoryBean("运动户外", "001", false, "up"));
        mList.add(new CategoryBean("男装", "002", false, ""));
        mList.add(new CategoryBean("手机", "003", false, ""));
        mList.add(new CategoryBean("家电", "004", false, ""));
        mList.add(new CategoryBean("电脑办公", "005", false, ""));
        mList.add(new CategoryBean("母婴童装", "006", false, ""));
        mList.add(new CategoryBean("食品酒饮", "007", false, ""));
        mList.add(new CategoryBean("鞋靴", "008", false, ""));
        mList.add(new CategoryBean("家具家装", "009", false, ""));
        mList.add(new CategoryBean("个护清洁", "010", false, ""));
        mList.add(new CategoryBean("家居厨具", "011", false, ""));
        mList.add(new CategoryBean("内衣配饰", "012", false, ""));
        mList.add(new CategoryBean("医疗保健", "013", false, ""));
        mList.add(new CategoryBean("数码", "014", false, ""));
        mList.add(new CategoryBean("女装", "015", false, ""));
        mList.add(new CategoryBean("生鲜", "016", false, ""));
        mList.add(new CategoryBean("美妆护肤", "017", false, ""));
        mList.add(new CategoryBean("宠物鲜花", "018", false, ""));
        mList.add(new CategoryBean("钟表珠宝", "019", false, ""));
        mList.add(new CategoryBean("玩具乐器", "020", false, ""));
        mList.add(new CategoryBean("箱包", "021", false, ""));
        mList.add(new CategoryBean("奢侈品", "022", false, ""));
        mList.add(new CategoryBean("医药", "023", false, ""));
        mList.add(new CategoryBean("图书文娱", "024", false, ""));
        mList.add(new CategoryBean("拍卖", "025", false, ""));

        mAdapter.notifyDataSetChanged();

        queryContentByCate("000");
    }

    /**
     * 获取右侧数据
     */
    public void queryContentByCate(String code) {
        List<CateBean> cateList = new ArrayList<>();

        List<CateBean> item01 = new ArrayList<>();
        item01.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/141473/11/25570/14501/61cd6301E3c727cb2/da05614ff4867715.jpg.webp", "男装馆", "0101", new ArrayList<>()));
        item01.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/207827/27/14233/18109/61cc0854E07a96cac/851f91561d5f3747.jpg.webp", "玩潮立志", "0102", new ArrayList<>()));
        item01.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/212383/40/9399/14223/61cc0878E049e6595/ae2170778a34733b.jpg.webp", "国际大牌", "0103", new ArrayList<>()));
        item01.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/173215/29/25248/17460/61cc088cEb0e0cd42/7658a73fa7dd528b.jpg.webp", "羽绒馆", "0104", new ArrayList<>()));
        item01.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/164378/26/23724/18872/61cc08acEddaebac2/0eaac2ee6db5d7f0.jpg.webp", "咱爸衣橱", "0105", new ArrayList<>()));
        item01.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/148278/39/20944/14535/61cc08beEa06b4c23/518ee222bf5ad8e6.jpg.webp", "私人定制", "0106", new ArrayList<>()));
        cateList.add(new CateBean("", "专场推荐", "01", item01));

        List<CateBean> item02 = new ArrayList<>();
        item02.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/214801/32/6183/9012/61a617c7Eb5acc17f/5983ebf99a4ddb91.jpg.webp", "轻薄羽绒服", "0201", new ArrayList<>()));
        item02.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/168308/39/21416/8512/61a617ddE2f59c938/27fdd62e87bf8ad1.jpg.webp", "极寒羽绒服", "0202", new ArrayList<>()));
        item02.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/197753/23/18889/7379/61a61802Eb280d514/820871b0b5a0d43a.jpg.webp", "工装羽绒服", "0203", new ArrayList<>()));
        item02.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/209922/39/10991/6685/61a61821Ead099563/b948af080e3dfd0b.jpg.webp", "亮面羽绒服", "0204", new ArrayList<>()));
        item02.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/171295/13/25243/10361/61a61862E15e06620/c628f88fb273da20.jpg.webp", "潮流棉服", "0205", new ArrayList<>()));
        item02.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/156393/14/26082/8196/61a6189fE1249c583/9093e87ccee89125.jpg.webp", "双面昵大衣", "0206", new ArrayList<>()));
        cateList.add(new CateBean("", "御寒外套", "02", item02));

        List<CateBean> item03 = new ArrayList<>();
        item03.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/139878/15/21771/5741/61a62990Edd6a8d3d/381ad5dbeb9e8c62.jpg.webp", "休闲卫裤", "0301", new ArrayList<>()));
        item03.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/218385/11/6182/4977/61a629c9Eac7cd906/9b1d27df28b6ce51.jpg.webp", "束脚裤", "0302", new ArrayList<>()));
        item03.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/204825/38/17128/4541/61a62a64Ead5461cc/ed8e6ffe3fad505c.jpg.webp", "西裤", "0303", new ArrayList<>()));
        item03.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/173782/26/22142/5819/61a62aa8E8ccb7b21/02fe7a8c85eebc24.jpg.webp", "加绒裤", "0304", new ArrayList<>()));
        item03.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/211632/8/11088/5086/61a62ae8Ee5f5140d/7100b6676025123e.jpg.webp", "直筒裤", "0305", new ArrayList<>()));
        item03.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/163789/18/21667/5210/61a62b2fEe8f84c19/6b54343b3d308cef.jpg.webp", "锥形裤", "0306", new ArrayList<>()));
        item03.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/215476/37/6242/6340/61a62b68E82c52867/4305c580dc5a7543.jpg.webp", "复古牛仔裤", "0307", new ArrayList<>()));
        item03.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/161527/26/26072/7280/61a62b98Ee413b080/343a1c9e8528a269.jpg.webp", "休闲牛仔裤", "0308", new ArrayList<>()));
        item03.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/200103/32/18671/4494/61a62bc1Eb8fbf1fe/94fd6be154138abf.jpg.webp", "直筒牛仔裤", "0309", new ArrayList<>()));
        cateList.add(new CateBean("", "裤装", "03", item03));

        List<CateBean> item04 = new ArrayList<>();
        item04.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/122035/39/12144/5877/5f58adf7Edb2efbfe/7e9c1fa095398659.jpg.webp", "童装馆", "0401", new ArrayList<>()));
        item04.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/123033/22/12331/4913/5f58ae4bE787905e3/7f2a85cc77aa93f8.jpg.webp", "自选精选", "0402", new ArrayList<>()));
        item04.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/156629/24/11357/6669/6038c486Eb622ac3e/17177cb667e4f101.jpg.webp", "萌宝婴童", "0403", new ArrayList<>()));
        item04.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/162018/2/22515/4263/6180e9fbE3ea6a3b1/b10388dd51c1a154.jpg.webp", "羽绒服", "0404", new ArrayList<>()));
        item04.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/206598/35/7670/7405/6180ead5E8043e13c/85d4a643ec0b88c4.jpg.webp", "外套大衣", "0405", new ArrayList<>()));
        item04.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/218953/1/2890/4636/6180eb28E9e0dc3d2/dae0eab607e3f98b.jpg.webp", "棉服", "0406", new ArrayList<>()));
        item04.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/113309/37/20366/4800/6180e914Edefd4520/69fd06c35194d162.jpg.webp", "套装", "0407", new ArrayList<>()));
        item04.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/206884/29/7806/3844/6180e93dE74f05a2e/e998ec69230310a5.jpg.webp", "裤子", "0408", new ArrayList<>()));
        item04.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/204108/14/13613/6258/6180eb6eE841039f3/435cb5288436a38c.jpg.webp", "卫衣", "0409", new ArrayList<>()));
        cateList.add(new CateBean("", "童装", "04", item04));

        List<CateBean> item05 = new ArrayList<>();
        item05.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/143380/3/20784/10134/61a61cb9E6488d1ec/f287293cfc1a5c7c.jpg.webp", "LOGO卫衣", "0501", new ArrayList<>()));
        item05.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/219378/35/6264/8953/61a61f91E2ad6dca6/b901fab826bff5c5.jpg.webp", "男女同款卫衣", "0502", new ArrayList<>()));
        item05.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/168209/21/21692/9803/61a61fb3E99acede8/599fc5283d59147f.jpg.webp", "宽松卫衣", "0503", new ArrayList<>()));
        item05.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/205911/14/17147/11994/61a62004E1c1301e5/485e699b501065e7.jpg.webp", "格纹衬衫", "0504", new ArrayList<>()));
        item05.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/170340/22/25716/8385/61a62058E47243470/c5108c7f987abd82.jpg.webp", "纯色衬衫", "0505", new ArrayList<>()));
        item05.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/142204/23/24852/9773/61a620caE107f0241/826f72cfb1d6beaf.jpg.webp", "法兰绒衬衫", "0506", new ArrayList<>()));
        item05.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/214649/20/6156/8260/61a62112Eda1dcfe2/90e30c862b869018.jpg.webp", "套头针织衫", "0507", new ArrayList<>()));
        item05.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/215960/3/6147/8224/61a62183E1a115d97/c0adeb677f412a29.jpg.webp", "针织开衫", "0508", new ArrayList<>()));
        item05.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/209141/40/11177/12881/61a621e9E27ff5d17/cbd1409fc31a3b84.jpg.webp", "羊毛衫", "0509", new ArrayList<>()));
        cateList.add(new CateBean("", "上衣", "05", item05));

        List<CateBean> item06 = new ArrayList<>();
        item06.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/216347/29/6212/9380/61a62d5aE5afe1454/f851319c31d25287.jpg.webp", "棒球服", "0601", new ArrayList<>()));
        item06.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/211306/19/11023/9678/61a62d8fEd1e7248d/f193ac5518a9637e.jpg.webp", "羊羔绒", "0602", new ArrayList<>()));
        item06.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/211394/26/6767/13233/6177b765Eb705e73d/cc3162cbe1a4d724.jpg.webp", "牛仔夹克", "0603", new ArrayList<>()));
        item06.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/160514/29/26663/8494/61a62d00E2fb6379c/2d6decf40528a366.jpg.webp", "工装夹克", "0604", new ArrayList<>()));
        item06.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/118547/25/24745/7776/61a62e07E371ca1ef/011a1477a782d88c.jpg.webp", "飞行夹克", "0605", new ArrayList<>()));
        item06.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/218613/31/6222/12676/61a62e3eE961c559a/fb84b6900d402807.jpg.webp", "刺绣夹克", "0606", new ArrayList<>()));
        cateList.add(new CateBean("", "夹克", "06", item06));

        List<CateBean> item07 = new ArrayList<>();
        item07.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/171267/7/22112/11465/617c0be7Ef6d12abe/06c61017fa105c31.jpg.webp", "军事风夹克", "0701", new ArrayList<>()));
        item07.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/197026/17/14630/10767/6177b98dEcfd4a414/ab97f70ffc07cb18.jpg.webp", "鬼脸卫衣", "0702", new ArrayList<>()));
        item07.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/173502/16/27648/6079/6177b91bEda550c83/c1dbc65d16a57ee9.jpg.webp", "vibe风长裤", "0703", new ArrayList<>()));
        item07.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/209160/18/6765/11124/6177b858E6a80cbde/bb4dc1bde38d981b.jpg.webp", "涂鸦卫衣", "0704", new ArrayList<>()));
        item07.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/222200/3/101/8707/6177b816Ed70fe33e/d2f95e25689bfbf6.jpg.webp", "大码卫衣", "0705", new ArrayList<>()));
        item07.add(new CateBean("https://m.360buyimg.com/mobile/s130x130_jfs/t1/128585/40/25231/9999/61a62efdEbeb270c7/5252b9aa6899a0d0.jpg.webp", "pu皮羽绒服", "0706", new ArrayList<>()));
        cateList.add(new CateBean("", "潮流单品", "07", item07));

        mRootView.loadCategoryRightData(new ContentCateResponse("https://ik.imagekit.io/guoguodad/mall/20220228111221231.jpg?updatedAt=1688348186490", cateList));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        if (CommonUtils.isNotEmpty(mList)) {
            this.mList.clear();
            this.mList = null;
        }
        this.mAdapter = null;
    }
}