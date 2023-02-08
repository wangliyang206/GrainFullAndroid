package com.zqw.mobile.grainfull.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.zqw.mobile.grainfull.mvp.model.entity.RoadOnePen;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/02/01 14:15
 * ================================================
 */
public interface OneLineToEndContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void loadView(RoadOnePen road);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        /**
         * 获取数据库中第一条未通关的关卡
         */
        RoadOnePen getSavedYibi(int rows, int columns, int difficulties);

        /**
         * 保存错误路线
         */
        void insertErrorYibi(int rows, int columns, String difficultiesStr, int startPosition);

        /**
         * 检查是否是错误路线
         */
        boolean checkErrorYibi(int rows, int columns, String difficultiesStr, int startPosition);

        /**
         * 保存通关记录
         */
        void insertPassedYibi(RoadOnePen road);

        /**
         * 检查是否通关
         */
        boolean checkPassedYibi(RoadOnePen road);

        /**
         * 保存路线
         */
        void insertSavedYibi(RoadOnePen road);

        /**
         * 检查是否有新增
         */
        boolean checkSavedYibi(RoadOnePen road);

        /**
         * 清理关卡内容
         */
        void clearPassedYibi();
    }
}