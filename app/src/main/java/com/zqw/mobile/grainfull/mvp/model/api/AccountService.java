package com.zqw.mobile.grainfull.mvp.model.api;

import com.jess.arms.cj.GsonRequest;
import com.jess.arms.cj.GsonResponse;
import com.zqw.mobile.grainfull.mvp.model.entity.AppUpdate;
import com.zqw.mobile.grainfull.mvp.model.entity.BaiduAiResponse;
import com.zqw.mobile.grainfull.mvp.model.entity.CommonResponse;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeInfoResponse;
import com.zqw.mobile.grainfull.mvp.model.entity.LoginResponse;
import com.zqw.mobile.grainfull.mvp.model.entity.TranslateResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 包名： PACKAGE_NAME
 * 对象名： AccountService
 * 描述：账户相关接口
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/3/24 10:03
 */

public interface AccountService {

    /*-----------------------------------------------------------------------用户基本-----------------------------------------------------------------------*/
    //登录
    @POST("member/login")
    Observable<GsonResponse<LoginResponse>> toLogin(@Body GsonRequest<Map<String, Object>> request);

    //验证Token有效性
    @POST("member/validToken")
    Observable<GsonResponse<LoginResponse>> validToken(@Body GsonRequest<Map<String, Object>> request);

    //发送短信验证码
    @POST("member/forgetPassword")
    Observable<GsonResponse<CommonResponse>> sendSms(@Body GsonRequest<Map<String, Object>> request);

    //重置密码
    @POST("member/resetPwd")
    Observable<GsonResponse<CommonResponse>> forgotPassword(@Body GsonRequest<Map<String, Object>> request);

    /*-----------------------------------------------------------------------通用-----------------------------------------------------------------------*/

    // 获取APP版本信息
    @POST("system/getVersion")
    Observable<GsonResponse<AppUpdate>> getVersion(@Body GsonRequest<Map<String, Object>> request);

    // 下载
    @Streaming
    @GET()
    Observable<ResponseBody> download(@Url String Url);

    // 翻译
    @GET()
    Observable<TranslateResponse> translate(@Url String Url);

    // 翻译，post的请求不成功，废弃
//    @POST()
//    Observable<TranslateResponse> translate(@Url String Url, @Body Map<String, Object> request);

    // 获取百度AI - Token
    @POST()
    Observable<BaiduAiResponse> getBaiduToken(@Url String Url, @QueryMap Map<String, Object> request);

    // 人像动漫化 - 收费接口
    @POST()
    Observable<BaiduAiResponse> selfieAnime(@Url String Url, @Body RequestBody params);

    // 文档图片去底纹 - 收费接口
    @POST()
    Observable<BaiduAiResponse> removeWatermarkin(@Url String Url, @Body RequestBody params);

    // 获取首页数据
    @POST()
    Observable<HomeInfoResponse> queryHomePageInfo(@Url String Url);
}
