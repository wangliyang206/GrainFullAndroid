package com.zqw.mobile.grainfull.app.global;

import com.blankj.utilcode.util.PathUtils;

/**
 * 包名： com.zqw.mobile.operation.app.global
 * 对象名： Constant
 * 描述：公共设置
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/10/18 14:47
 */

public interface Constant {
    /*----------------------------------------------APP SdCard目录地址-------------------------------------------------*/

    /* 创建目录与文件需要做详细配置，10以下正常使用File，10+需要走分区存储 */
    /**
     * APP升级路径
     */
    String APP_UPDATE_PATH = PathUtils.getExternalDocumentsPath() + "/GrainFull/AppUpdate/";

    /**
     * log路径
     */
    String LOG_PATH = PathUtils.getExternalDocumentsPath() + "/GrainFull/Log/";

    /**
     * 音频缓存路径
     */
    String AUDIO_PATH = PathUtils.getExternalDownloadsPath() + "/GrainFull/";

    /**
     * 保存图片路径
     */
    String IMAGE_PATH = PathUtils.getExternalPicturesPath() + "/GrainFull/Image/";

    /**
     * 模板路径
     */
    String TEMPLATE_PATH = PathUtils.getExternalPicturesPath() + "/GrainFull/Template/";

    /*----------------------------------------------------业务变量-------------------------------------------------------*/
    /**
     * ChatGPT 服务地址
     */
//    String CHATGPT_URL = "https://api.openai.com/v1/chat/completions";
    String CHATGPT_URL = "https://openkey.cloud/v1/chat/completions";
    // /api/openai
    String CHATGPT_KEY = "sk-UpxszeOO0Bjag203991240BbCa6b48Dc89C90f1b451b9c39";
    /**
     * 百度翻译 - 服务地址
     */
    String BAIDU_TRANSLATE_URL = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    /**
     * 百度AI
     */
    String BAIDU_AI_URL = "https://aip.baidubce.com/";
    /**
     * 百度翻译 - APP ID
     */
    String BAIDU_TRANSLATE_APPID = "20221128001474660";

    /**
     * 百度翻译 - 密钥
     */
    String BAIDU_TRANSLATE_SECRETKEY = "MIeDZYeJsGadJRg6n8sE";

    /**
     * 金莱特-服务协议
     */
    String serviceAgreementUrl = "http://www.buypb.cn/useragreement/zqwservicegreement_jlt.html";

    /**
     * 金莱特-隐私政策
     */
    String privacyPolicyUrl = "http://www.buypb.cn/useragreement/ruserprivacy_jlt.html";

    /**
     * 友盟统计 - 渠道
     */
    String UM_CHANNEL = "test_channel";

    /**
     * API版本号
     */
    int version = 1;

    /**
     * 默认展示20条
     */
    int PAGESIZE = 20;

    /**
     * 统计默认展示50条
     */
    int STATISTICS_PAGESIZE = 50;

    /**
     * 正则：行开头、至少出现一次数字、(任意字符和至少出现一次数字)出现1次或0次、行结尾
     */
    String regular = "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";

    /*----------------------------------------------------跳转设定-------------------------------------------------------*/

    /**
     * 图片参数key
     */
    String IMAGE_URL = "IMAGE_URL";

    /**
     * 头像
     */
    int MAIN_AVATAR = 0;

    /**
     * 基本信息
     */
    int MAIN_BASICINFO = 1;

    /**
     * 夜间模式
     */
    int MAIN_NIGHTMODE = 8;

    /**
     * 关于
     */
    int MAIN_ABOUT = 9;

    /**
     * 设置
     */
    int MAIN_SETTING = 10;

    /**
     * 选择图片
     */
    int REQUEST_SELECT_IMAGES_CODE = 0x01;
}
