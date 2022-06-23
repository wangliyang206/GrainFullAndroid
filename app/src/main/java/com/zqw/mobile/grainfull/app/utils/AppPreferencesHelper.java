package com.zqw.mobile.grainfull.app.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import net.grandcentrix.tray.TrayPreferences;

/**
 * 包名： com.cj.mobile.common.util
 * 对象名： AppPreferencesHelper
 * 描述：轻量级缓存(TrayPreferences代替SharedPreferences，支持跨服务同步)
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/8/24 18:30
 */

public class AppPreferencesHelper extends TrayPreferences {

    public AppPreferencesHelper(@NonNull Context context, @NonNull String module, int version) {
        super(context, module, version);
    }

    /*----------------------------------------------读取信息-------------------------------------------------*/

    /**
     * 读取保存信息
     *
     * @param key          最初使用保存的键
     * @param defaultValue 默认值
     * @return 有结果则是结果，无结果则是默认值
     */
    public String getPref(String key, String defaultValue) {
        try {
            return this.getString(key, defaultValue);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * 读取保存信息
     *
     * @param key          最初使用保存的键
     * @param defaultValue 默认值
     * @return 有结果则是结果，无结果则是默认值
     */
    public int getPref(String key, int defaultValue) {
        try {
            return this.getInt(key, defaultValue);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * 读取保存信息
     *
     * @param key          最初使用保存的键
     * @param defaultValue 默认值
     * @return 有结果则是结果，无结果则是默认值
     */
    public boolean getPref(String key, boolean defaultValue) {
        try {
            return this.getBoolean(key, defaultValue);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * 读取保存信息
     *
     * @param key          最初使用保存的键
     * @param defaultValue 默认值
     * @return 有结果则是结果，无结果则是默认值
     */
    public long getPref(String key, long defaultValue) {
        try {
            return this.getLong(key, defaultValue);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * 读取保存信息
     *
     * @param key          最初使用保存的键
     * @param defaultValue 默认值
     * @return 有结果则是结果，无结果则是默认值
     */
    public float getPref(String key, float defaultValue) {
        try {
            return this.getFloat(key, defaultValue);
        } catch (Exception ex) {
            return defaultValue;
        }
    }
}

