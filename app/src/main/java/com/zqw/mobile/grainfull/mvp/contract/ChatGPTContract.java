package com.zqw.mobile.grainfull.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/11/23 15:49
 * ================================================
 */
public interface ChatGPTContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        // 加载消息
        void onLoadMessage(StringBuffer info);
        // 完成一次会话
        void onSucc();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        // 创建“聊天”会话
        Observable<ResponseBody> chatCreate(String message);
    }
}