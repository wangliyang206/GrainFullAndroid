package com.zqw.mobile.grainfull.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.zqw.mobile.grainfull.mvp.model.entity.BannerBean;
import com.zqw.mobile.grainfull.mvp.model.entity.DetailInfo;
import com.zqw.mobile.grainfull.mvp.model.entity.GoodsBean;
import com.zqw.mobile.grainfull.mvp.model.entity.GoodsInfo;

import java.util.List;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2025/02/18 11:35
 * ================================================
 */
public interface GoodsDetailContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void loadData(List<BannerBean> bannerList, GoodsInfo goodsInfo, DetailInfo detailInfo, List<GoodsBean> goodsList);
//        void switchColorThumb(List<BannerBean> bannerList);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

    }
}