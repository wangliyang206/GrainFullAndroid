package com.jess.arms.cj;

/**
 * @Title: GsonRequest
 * @Package com.cj.mobile.common.model
 * @Description: 网络请求数据格式的基类
 * @author: 王力杨
 * @date: 16/5/24 上午9:23
 */
public class GsonRequest<T> {
    /**登陆用户id登陆时颁发 调用需要登陆权限的接口都带上 服务端判断sessId所记录的用户是否与此一致*/
    private String userId;

    /**网络请求的版本*/
    private int version;

    /**客户端信息*/
    private Object client;

    /** token */
    private String token;

    /**语言(APP 使用语言，用于服务端返回不同语言信息判断；ZH——中文（简体）； EN——英语；AR——阿拉伯语)*/
    private String language;

    private T data;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Object getClient() {
        return client;
    }

    public void setClient(Object client) {
        this.client = client;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
