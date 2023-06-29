package com.zqw.mobile.grainfull.mvp.model;

import android.app.Application;

import com.blankj.utilcode.util.EncryptUtils;
import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.mvp.contract.TranslateContract;
import com.zqw.mobile.grainfull.mvp.model.api.AccountService;
import com.zqw.mobile.grainfull.mvp.model.entity.TranslateResponse;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/06/29 09:30
 * ================================================
 */
@ActivityScope
public class TranslateModel extends BaseModel implements TranslateContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public TranslateModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<TranslateResponse> translate(String text, String from, String to) {
        Map<String, Object> params = new HashMap<>();
        //请求翻译query
        params.put("q", text);
        //翻译源语言
        params.put("from", from);
        // 译文语言
        params.put("to", to);
        // APP ID
        params.put("appid", Constant.BAIDU_TRANSLATE_APPID);

        // 生成一个随机数
        int num = CommonUtils.getRandomNum(100);
        // 随机数
        params.put("salt", String.valueOf(num));

        // 签名：appid+q+salt+密钥 的MD5值
        params.put("sign", EncryptUtils.encryptMD5ToString(Constant.BAIDU_TRANSLATE_APPID + text + num + Constant.BAIDU_TRANSLATE_SECRETKEY).toLowerCase(Locale.ROOT));

        // POST请求
//        return mRepositoryManager.obtainRetrofitService(AccountService.class).translate(Constant.BAIDU_TRANSLATE_URL, params);
        // Get请求
        return mRepositoryManager.obtainRetrofitService(AccountService.class).translate(Constant.BAIDU_TRANSLATE_URL + "?q=" + text + "&from=" + from + "&to=" + to + "&appid=" + Constant.BAIDU_TRANSLATE_APPID + "&salt=" + num + "&sign=" + params.get("sign"));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
}