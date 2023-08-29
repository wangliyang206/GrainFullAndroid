package com.zqw.mobile.grainfull.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.zqw.mobile.grainfull.mvp.model.entity.BannerBean;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeInfoResponse;
import com.zqw.mobile.grainfull.mvp.model.entity.MenuBean;
import com.zqw.mobile.grainfull.mvp.model.entity.TabBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/07/05 11:06
 * ================================================
 */
public interface LayoutForumContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        // 轮播图
        void loadBanner(List<BannerBean> list);

        // 广告栏
        void loadAdvertisingBar(String url);

        // 菜单
        void loadNineMenu(List<MenuBean> nineMenuList);

        // tab
        void showTabLayout(List<TabBean> list);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        // 获取首页数据
        Observable<HomeInfoResponse> queryHomePageInfo();
    }
}