package com.zqw.mobile.grainfull.mvp.model;

import android.app.Application;

import com.blankj.utilcode.util.ImageUtils;
import com.google.gson.Gson;
import com.huawei.hms.common.util.Base64Utils;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.mvp.contract.RemoveWatermarkinContract;
import com.zqw.mobile.grainfull.mvp.model.api.AccountService;
import com.zqw.mobile.grainfull.mvp.model.entity.BaiduAiResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/07/05 11:06
 * ================================================
 */
@ActivityScope
public class RemoveWatermarkinModel extends BaseModel implements RemoveWatermarkinContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public RemoveWatermarkinModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaiduAiResponse> getBaiduToken(String APIKey, String SecretKey) {
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "client_credentials");
        params.put("client_id", APIKey);
        params.put("client_secret", SecretKey);

        return mRepositoryManager.obtainRetrofitService(AccountService.class).getBaiduToken(Constant.BAIDU_AI_URL + "oauth/2.0/token", params);
    }

    @Override
    public Observable<BaiduAiResponse> removeWatermarkin(String accessToken, String filePath) {
        byte[] imgData = ImageUtils.bitmap2Bytes(ImageUtils.getBitmap(filePath));
        String imgStr = Base64Utils.encode(imgData);
        String imgParam = null;
        try {
            imgParam = URLEncoder.encode(imgStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuilder param = new StringBuilder();
        param.append("image=" + imgParam);

        RequestBody mVal = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), param.toString());
        return mRepositoryManager.obtainRetrofitService(AccountService.class).removeWatermarkin(Constant.BAIDU_AI_URL + "rest/2.0/image-process/v1/doc_repair?access_token=" + accessToken, mVal);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
}