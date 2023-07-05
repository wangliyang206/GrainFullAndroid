package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: BaiduAiResponse
 * @Description: 百度AI响应
 * @Author: WLY
 * @CreateDate: 2023/7/4 11:20
 */
public class BaiduAiResponse {
    /*-------------------------------------------鉴权认证机制-------------------------------------------*/
    private String refresh_token;
    // Access Token的有效期(秒为单位，有效期30天)
    private String expires_in;
    private String session_key;
    // 要获取的Access Token
    private String access_token;
    private String scope;
    private String session_secret;

    // 错误结构
    private String error;
    private String error_description;
    /*-------------------------------------------图像特效-------------------------------------------*/
    // 唯一的log id，用于问题定位(64位)
    private long log_id;
    // 处理后图片的Base64编码
    private String image;

    // 错误结构
    private int error_code;
    private String error_msg;

    /*-------------------------------------------图像增强-------------------------------------------*/
    // 处理后图片的Base64编码
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError() {
        return error;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSession_secret() {
        return session_secret;
    }

    public void setSession_secret(String session_secret) {
        this.session_secret = session_secret;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
