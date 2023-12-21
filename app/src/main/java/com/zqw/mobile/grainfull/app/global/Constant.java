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
    /**
     * 项目目录
     */
    String APP_CATALOGUE = "GrainFull";
    /* 创建目录与文件需要做详细配置，10以下正常使用File，10+需要走分区存储 */
    /**
     * APP升级路径
     */
    String APP_UPDATE_PATH = PathUtils.getExternalDownloadsPath() + "/" + APP_CATALOGUE + "/AppUpdate/";

    /**
     * log路径
     */
    String LOG_PATH = PathUtils.getExternalDocumentsPath() + "/" + APP_CATALOGUE + "/Log/";

    /**
     * 音频缓存路径
     */
    String AUDIO_PATH = PathUtils.getExternalDownloadsPath() + "/" + APP_CATALOGUE + "/";

    /**
     * 保存图片路径
     */
    String IMAGE_PATH = PathUtils.getExternalPicturesPath() + "/" + APP_CATALOGUE + "/Image/";

    /**
     * 模板路径
     */
    String TEMPLATE_PATH = PathUtils.getExternalPicturesPath() + "/" + APP_CATALOGUE + "/Template/";

    /*----------------------------------------------------业务变量-------------------------------------------------------*/
    /**
     * ChatGPT “官方”服务地址
     */
//    String CHATGPT_URL = "https://api.openai.com/v1/chat/completions";
    /**
     * ChatGPT “OpenKEY”服务“闲聊”地址
     */
    String CHATGPT_CHAT_URL = "https://openkey.cloud/v1/chat/completions";
    /**
     * ChatGPT “OpenKEY”服务“图片”地址
     */
    String CHATGPT_IMAGE_URL = "https://openkey.cloud/v1/images/generations";
    /**
     * ChatGPT 查询令牌余额
     */
    String CHATGPT_TOKEN = "https://billing.openkey.cloud/api/token";
    /**
     * 语音转文字
     */
    String CHATGPT_TRANSCRIPTIONS_URL = "https://openkey.cloud/v1/audio/transcriptions";
    /**
     * 文字转语音
     */
    String CHATGPT_SPEECH_URL = "https://openkey.cloud/v1/audio/speech";
    /**
     * ChatGPT key，有额度(1元 500000 tokens)
     */
    String CHATGPT_KEY = "sk-GlgmPE0qiewPnNg6760703686fD4468683C655Ed1eA75e37";


    /**
     * FastGPT API 地址
     */
    String FASTGPT_CHAT_URL = "https://ai.fastgpt.in/api/v1/chat/completions";
    /**
     * 获取历史记录
     */
    String FASTGPT_HISTORY_URL = "https://ai.fastgpt.in/api/core/chat/init";
    /**
     * 语音转文字
     */
    String FASTGPT_TRANSCRIPTIONS_URL = "https://ai.fastgpt.in/api/v1/audio/transcriptions";
    /**
     * 文字转语音
     */
    String FASTGPT_SPEECH_URL = "https://ai.fastgpt.in/api/v1/audio/speech";
    // 企业智能客服：带有知识库，可以回答企业任何问题(appId=656fce2d993ca09b160e9ea7)
//    String FASTGPT_KEY = "fastgpt-jeq4kr7IUN9Qvvi1Bv4C7ddJIeW3GJtHE";
    // 豆芽AI助手(appId=6571425b3edacb78a123cf0c)
    String FASTGPT_KEY = "fastgpt-lEmLoX75QqwHeUmvwbVFkIXwJSsREJ";

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
     * 服务协议
     */
    String serviceAgreementUrl = "http://www.buypb.cn/useragreement/zqwservicegreement_jlt.html";

    /**
     * 隐私政策
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
