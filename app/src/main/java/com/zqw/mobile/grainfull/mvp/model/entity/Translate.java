package com.zqw.mobile.grainfull.mvp.model.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: Translate
 * @Description: 翻译数据源
 * @Author: WLY
 * @CreateDate: 2023/6/29 12:17
 */
public class Translate {

    private final static Map<String, String> config = new LinkedHashMap<>();
    private final static List<String> mKeyList = new ArrayList<>();
    private final static List<String> mValueList = new ArrayList<>();

    /**
     * 初始化数据
     */
    public static void onInit() {
        // 语言简写,名称
        if (config.size() == 0) {
            config.put("auto", "自动检测");
            config.put("zh", "中文");
            config.put("en", "英语");
            config.put("yue", "粤语");
            config.put("wyw", "文言文");
            config.put("jp", "日语");
            config.put("kor", "韩语");
            config.put("fra", "法语");
            config.put("spa", "西班牙语");
            config.put("th", "泰语");
            config.put("ara", "阿拉伯语");
            config.put("ru", "俄语");
            config.put("pt", "葡萄牙语");
            config.put("de", "德语");
            config.put("it", "意大利语");
            config.put("el", "希腊语");
            config.put("nl", "荷兰语");
            config.put("pl", "波兰语");
            config.put("bul", "保加利亚语");
            config.put("est", "爱沙尼亚语");
            config.put("dan", "丹麦语");
            config.put("fin", "芬兰语");
            config.put("cs", "捷克语");
            config.put("rom", "罗马尼亚语");
            config.put("slo", "斯洛文尼亚语");
            config.put("swe", "瑞典语");
            config.put("hu", "匈牙利语");
            config.put("cht", "繁体中文");
            config.put("vie", "越南语");
        }
    }

    /**
     * 释放内存
     */
    public static void onDestroy() {
        config.clear();
        mKeyList.clear();
        mValueList.clear();
    }

    /**
     * 翻译 - 语言列表 - 键
     */
    public static List<String> getLanguageKey(boolean isAfter) {
        mKeyList.clear();
        for (String key : config.keySet()) {
            if (isAfter && key.equalsIgnoreCase("auto")) {
                continue;
            }
            mKeyList.add(key);
        }
        return mKeyList;
    }

    /**
     * 翻译 - 语言列表 - 值
     */
    public static List<String> getLanguageValue(boolean isAfter) {
        mValueList.clear();
        for (String value : config.values()) {
            if (isAfter && value.equalsIgnoreCase("自动检测")) {
                continue;
            }
            mValueList.add(value);
        }
        return mValueList;
    }
}
