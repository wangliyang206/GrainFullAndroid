package com.baidu.idl.face.platform.ui.model;

public class Const {
    // 用于sharepreference的key
    public static final String KEY_QUALITY_LEVEL_SAVE = "quality_save";

    // 用于Intent传参
    public static final String INTENT_QUALITY_LEVEL = "intent_quality";
    public static final String INTENT_QUALITY_TITLE = "intent_quality_title";
    public static final String INTENT_QUALITY_LEVEL_PARAMS = "intent_quality_params";

    // quality类型：0：正常、1：宽松、2：严格、3：自定义
    public static final int QUALITY_NORMAL = 0;
    public static final int QUALITY_LOW = 1;
    public static final int QUALITY_HIGH = 2;
    public static final int QUALITY_CUSTOM = 3;

    // 页面跳转请求code和回复code
    public static final int REQUEST_QUALITY_CONTROL = 100;
    public static final int RESULT_QUALITY_CONTROL = 101;

    public static final int REQUEST_QUALITY_PARAMS = 102;
    public static final int RESULT_QUALITY_PARAMS = 103;

    // dialog类型
    public static final int DIALOG_SAVE_SAVE_BUTTON = 1;    // 非自定义页面，点击『保存』
    public static final int DIALOG_SAVE_RETURN_BUTTON = 2;  // 非自定义页面，点击『返回』
    public static final int DIALOG_RESET_DEFAULT = 3;       // 非自定义页面，点击『恢复默认』
    public static final int DIALOG_SAVE_CUSTOM_MODIFY = 4;  // 自定义页面，点击『返回』
}
