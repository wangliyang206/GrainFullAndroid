package com.zqw.mobile.grainfull.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.zqw.mobile.grainfull.mvp.model.entity.TabBean;

import java.util.List;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/09/05 17:34
 * ================================================
 */
public interface MineContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void showTabLayout(List<TabBean> list);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

    }
}