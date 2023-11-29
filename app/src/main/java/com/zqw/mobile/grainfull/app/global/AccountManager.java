package com.zqw.mobile.grainfull.app.global;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.bugly.crashreport.CrashReport;
import com.zqw.mobile.grainfull.BuildConfig;
import com.zqw.mobile.grainfull.app.utils.AppPreferencesHelper;
import com.zqw.mobile.grainfull.mvp.model.entity.LoginResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * @ Title: AccountManager
 * @ Package com.zqw.mobile.smelter.api
 * @ Description: 用户信息存取类：从SharedPreferences中读取用户登录信息
 * @ author: wly
 * @ date: 2020/09/15 14:24
 */
@Singleton
public final class AccountManager {

    /*----------------------------------------------业务常量-------------------------------------------------*/
    // Token
    private final String TOKEN = "Token";
    // 用户id
    private final String USERID = "Userid";
    // 头像
    private final String PHOTOURL = "PhotoUrl";
    // 账号
    private final String ACCOUNT = "Account";
    // 密码
    private final String PASSWORD = "Password";
    // 用户姓名
    private final String USER_NAME = "UserName";
    // 电话
    private final String RECYCLE_PHONE = "Mobile";

    // APP启动次数(达到一定次数后清理日志)
    private final String START_TIME = "StartTime";

    // 定位 - 经度
    private static final String LONGITUDE = "Longitude";
    // 定位 - 纬度
    private static final String LATITUDE = "Latitude";
    // 定位 - 地址
    private static final String ADDRESS = "Address";
    // 定位 - 半径
    private static final String RADIUS = "Radius";
    // 定位 - 省
    private static final String PROVINCE = "Province";
    // 定位 - 市
    private static final String CITY = "City";
    // 定位 - 区
    private static final String DISTRICT = "District";

    // 选择城市
    private static final String SELECT_CITY = "SelectCity";

    // 是否提醒过 APP升级
    private final String UPGRADE = "Upgrade";

    // 是否保持清醒
    private final String KEEP_WAKE = "keepWake";
    // 金属探测仪的报警值
    private final String ALARM_LIMIT = "alarmLimit";

    /**
     * 是否同意隐私政策
     */
    private static final String PRIVACY_POLICY = "privacyPolicy";

    private static final String CHATGPT_VERSION = "ChatGPTVersion";
    /*----------------------------------------------操作对象-------------------------------------------------*/

    private AppPreferencesHelper spHelper;

    @Inject
    public AccountManager(Context context) {
        this.spHelper = new AppPreferencesHelper(context.getApplicationContext(), BuildConfig.SHARED_NAME_INVEST, 1);

        updateBugly();
    }

    /**
     * 更新Bugly状态
     */
    private void updateBugly() {
        try {
            String userId = getUserid();
            if (!isLogin())
                userId = "Not Login";
            CrashReport.setUserId(userId);// 记录当前是谁上报的
        } catch (Exception ignored) {
        }
    }

    /**
     * 保存登录信息(登录成功后调用此方法)
     *
     * @param account       账号
     * @param password      密码
     * @param loginResponse 用户信息
     */
    public void saveAccountInfo(String account, String password, LoginResponse loginResponse) {
        spHelper.put(ACCOUNT, account);
        spHelper.put(PASSWORD, password);
        spHelper.put(TOKEN, loginResponse.getToken());

        updateAccountInfo(loginResponse);
    }

    /**
     * 更新登录信息(登录成功后调用此方法)
     *
     * @param loginResponse 用户信息
     */
    public void updateAccountInfo(LoginResponse loginResponse) {
        spHelper.put(USERID, loginResponse.getUserId());
        spHelper.put(USER_NAME, loginResponse.getLoginName());
        spHelper.put(PHOTOURL, "");
        spHelper.put(RECYCLE_PHONE, loginResponse.getUserPhone());

        updateBugly();
    }

    /**
     * 清除账号信息(手动点击退出登录后调用此方法)
     */
    public void clearAccountInfo() {
        spHelper.put(ACCOUNT, "");
        spHelper.put(PASSWORD, "");
        spHelper.put(TOKEN, "");
        spHelper.put(USER_NAME, "");
        spHelper.put(USERID, "");
        spHelper.put(PHOTOURL, "");
        spHelper.put(RECYCLE_PHONE, "");

        updateBugly();
    }

    /**
     * 设置Token
     *
     * @param token token
     */
    public void setToken(String token) {
        spHelper.put(TOKEN, token);
    }

    /**
     * 获取用户名称(昵称)
     *
     * @return 如果为空则返回账号
     */
    public String getUserName() {
        String username = spHelper.getPref(USER_NAME, "");
        if (TextUtils.isEmpty(username)) {
            username = spHelper.getPref(ACCOUNT, "");
        }
        return username;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        spHelper.put(USER_NAME, name);
    }

    /**
     * 获取账号
     *
     * @return 回调
     */
    public String getAccount() {
        return spHelper.getPref(ACCOUNT, "");
    }

    /**
     * 获取电话
     */
    public String getCurrPhone() {
        return spHelper.getPref(RECYCLE_PHONE, "");
    }

    /**
     * 获取密码
     *
     * @return 回调
     */
    public String getPassword() {
        return spHelper.getPref(PASSWORD, "");
    }

    /**
     * 获取Token
     *
     * @return 返回数据
     */
    public String getToken() {
        String str = spHelper.getPref(TOKEN, "");
        Timber.i("RetrofitFactoty：Token=%s", str);
        return str;
    }

    /**
     * 获取头像URL
     *
     * @return 返回数据
     */
    public String getPhotoUrl() {
        return spHelper.getPref(PHOTOURL, "");
    }

    /**
     * 更新头像URL
     */
    public void setPhotoUrl(String url) {
        spHelper.put(PHOTOURL, url);
    }


    /**
     * 更新定位信息(经纬度)
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @param address   详细地址
     */
    public void updateLocation(double latitude, double longitude, float radius, String province, String city, String district, String address) {
        spHelper.put(LATITUDE, String.valueOf(latitude));
        spHelper.put(LONGITUDE, String.valueOf(longitude));
        spHelper.put(RADIUS, radius);

        if (!TextUtils.isEmpty(province))
            spHelper.put(PROVINCE, province);
        if (!TextUtils.isEmpty(city))
            spHelper.put(CITY, city);
        if (!TextUtils.isEmpty(district))
            spHelper.put(DISTRICT, district);

        if (!TextUtils.isEmpty(address))
            spHelper.put(ADDRESS, address);
    }

    /**
     * 获取用户id
     *
     * @return 返回数据
     */
    public String getUserid() {
        return spHelper.getPref(USERID, "");
    }


    /**
     * 当前是否登录
     *
     * @return token存在则表示已登录(返回true)否则未登录(返回false)
     */
    public boolean isLogin() {
        String token = spHelper.getPref(TOKEN, "");
        return !TextUtils.isEmpty(token);
    }

    /**
     * 保存APP启动次数
     */
    public void setStartTime(int num) {
        spHelper.put(START_TIME, num);
    }


    /**
     * 获取APP启动次数，如果达到一定次数开始清理日志
     */
    public int getStartTime() {
        return spHelper.getPref(START_TIME, 0);
    }


    /**
     * 获取经度
     *
     * @return 回调
     */
    public String getLongitude() {
        return spHelper.getPref(LONGITUDE, "");
    }

    /**
     * 获取纬度
     *
     * @return 回调
     */
    public String getLatitude() {
        return spHelper.getPref(LATITUDE, "");
    }

    /**
     * 获取半径
     *
     * @return 回调
     */
    public float getRadius() {
        return spHelper.getPref(RADIUS, 0);
    }

    /**
     * 获得省份
     */
    public String getProvince() {
        return spHelper.getPref(PROVINCE, "");
    }

    /**
     * 获得城市
     */
    public String getCity() {
        return spHelper.getPref(CITY, "");
    }

    /**
     * 获得区域
     */
    public String getDistrict() {
        return spHelper.getPref(DISTRICT, "");
    }

    /**
     * 获取定位地址
     *
     * @return 回调
     */
    public String getAddress() {
        return spHelper.getPref(ADDRESS, "");
    }

    /**
     * APP升级记录(APP启动时初始设置，提醒过一次后，修改设置)：true 表示 需要提醒设置，false 表示 已经提醒过了
     */
    public void setUpgrade(boolean isVal) {
        spHelper.put(UPGRADE, isVal);
    }

    /**
     * 获取APP升级记录
     *
     * @return 回调
     */
    public boolean getUpgrade() {
        return spHelper.getPref(UPGRADE, false);
    }

    /**
     * 选择城市
     */
    public String getSelectCity() {
        String val = spHelper.getPref(SELECT_CITY, "");

        // 如果等于空，则显示定位城市
        if (TextUtils.isEmpty(val)) {
            val = getCity();
        }
        return val;
    }

    /**
     * 设置选择城市
     */
    public void setSelectCity(String val) {
        spHelper.put(SELECT_CITY, val);
    }

    /**
     * 获取是否保持清醒
     *
     * @return 回调
     */
    public boolean getKeepWake() {
        return spHelper.getPref(KEEP_WAKE, true);
    }

    /**
     * 设置是否保持清醒
     */
    public void setKeepWake(boolean val) {
        spHelper.put(KEEP_WAKE, val);
    }

    /**
     * 金属探测仪的报警值
     *
     * @return 回调
     */
    public String getAlarmLimit() {
        return spHelper.getPref(ALARM_LIMIT, "");
    }

    /**
     * 金属探测仪的报警值
     *
     * @return 回调
     */
    public void setAlarmLimit(String val) {
        spHelper.put(ALARM_LIMIT, val);
    }


    /**
     * 获取当前是否同意隐私政策
     */
    public boolean getPrivacyPolicy() {
        return spHelper.getPref(PRIVACY_POLICY, false);
    }

    /**
     * 设置隐私政策
     */
    public void setPrivacyPolicy(boolean isValue) {
        spHelper.put(PRIVACY_POLICY, isValue);
    }

    /**
     * 获取ChatGPT版本
     */
    public String getChatGptVersion() {
        return spHelper.getPref(CHATGPT_VERSION, "gpt-3.5-turbo");
    }

    /**
     * 设置ChatGPT版本
     */
    public void setChatGptVersion(boolean isLargeVersion) {
        if (isLargeVersion) {
            spHelper.put(CHATGPT_VERSION, "gpt-4");
        } else {
            spHelper.put(CHATGPT_VERSION, "gpt-3.5-turbo");
        }
    }

}
