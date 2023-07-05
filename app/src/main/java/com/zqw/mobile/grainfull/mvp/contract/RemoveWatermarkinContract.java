package com.zqw.mobile.grainfull.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.zqw.mobile.grainfull.mvp.model.entity.BaiduAiResponse;

import io.reactivex.Observable;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/07/05 11:06
 * ================================================
 */
public interface RemoveWatermarkinContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        // 显示提交Loding
        void showLoadingSubmit();

        // 隐藏提交Loding
        void hideLoadingSubmit();

        // 加载图片
        void loadImage(String image);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        // 获取百度Token
        Observable<BaiduAiResponse> getBaiduToken(String APIKey, String SecretKey);

        // 去掉水印
        Observable<BaiduAiResponse> removeWatermarkin(String accessToken,String path);
    }
}