package com.zqw.mobile.grainfull.mvp.model.entity;

import java.util.List;

/**
 * 包名： PACKAGE_NAME
 * 对象名： LoginResponse
 * 描述：登录    响应结构
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/3/24 11:28
 */

public class LoginResponse {
    /* Token */
    private String token = "";

    /* 用户id */
    private String userId = "";

    /* 用户名 */
    private String loginName = "";

    /* 手机号 */
    private String userPhone = "";

    public LoginResponse() {
    }

    public LoginResponse(String token, String userId, String loginName, String userPhone) {
        this.token = token;
        this.userId = userId;
        this.loginName = loginName;
        this.userPhone = userPhone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
