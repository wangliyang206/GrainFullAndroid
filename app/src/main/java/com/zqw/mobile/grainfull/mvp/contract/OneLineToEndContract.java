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
        // 显示关卡
        void showLevel(String tips);

        // 加载游戏
        void loadGame(RoadOnePen road);

        // 显示收集信息
        void showCollection(int rows, int columns, int difficulties, int passed);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        /**
         * 获取数据库中第一条未通关的关卡
         */
        RoadOnePen getSavedYibi(int rows, int columns, int difficulties);

        /**
         * 记录错误图，减少下次寻路时间
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
         * 生成关卡，并保存到数据库中
         */
        void insertSavedYibi(RoadOnePen road);

        /**
         * 检查关卡是否保存重复
         */
        boolean checkSavedYibi(RoadOnePen road);

        /**
         * 清理已生成的关卡数据
         */
        void clearPassedYibi();

        /**
         * 获取过关总数
         */
        int getPassedCount(int rows, int columns, int difficulties);
    }
}