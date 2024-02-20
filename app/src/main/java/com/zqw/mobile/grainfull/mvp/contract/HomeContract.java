package com.zqw.mobile.grainfull.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.zqw.mobile.grainfull.mvp.model.entity.LoginFastGptResponse;

import java.util.List;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/31/2019 10:34
 * ================================================
 */
public interface HomeContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        // 首页定位失败
        void homemPositioningFailure();

        // 设置头像
        void homeSetAvatar(String url);

        // 设置城市
        void homeSetLocateAdd(String city);

        // 首页热门关键字
        void homeSearchHot(List<String> list);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        // 登录FastGPT
        Observable<LoginFastGptResponse> logiFastGpt(String username, String password);
    }
}
