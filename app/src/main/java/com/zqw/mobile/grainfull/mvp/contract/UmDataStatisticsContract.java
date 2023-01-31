package com.zqw.mobile.grainfull.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.umeng.uapp.param.UmengUappAllAppData;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/16 10:42
 * ================================================
 */
public interface UmDataStatisticsContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        // 加载 所有App统计数据
        void loadAllAppData(UmengUappAllAppData info);

        // 加载时长日期
        void loadDurationDate(String mDate);
        // 加载时长
        void loadDurations(boolean isDaily, String duration);
        // 控制时长布局
        void viewDurations(boolean isShow);

        // 加载事件日期
        void loadEventDate(String mDate);
        // 显示事件条数
        void loadEventCount(int count);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

    }
}