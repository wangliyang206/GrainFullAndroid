package com.zqw.mobile.grainfull.app.global;

import com.blankj.utilcode.util.SDCardUtils;

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
     * 图片路径
     */
    String IMAGE_PATH = SDCardUtils.getSDCardPathByEnvironment() + "/ZQW/grainFull/ImageInfo/";

    /**
     * 业务缓存目录
     */
    String CACHE_PATH = SDCardUtils.getSDCardPathByEnvironment() + "/ZQW/grainFull/Cache/";

    /**
     * APP升级路径
     */
    String APP_UPDATE_PATH = SDCardUtils.getSDCardPathByEnvironment() + "/ZQW/grainFull/AppUpdate/";

    /**
     * log路径
     */
    String LOG_PATH = SDCardUtils.getSDCardPathByEnvironment() + "/ZQW/grainFull/Log/";

    /**
     * 视频缓存路径
     */
    String VIDEO_PATH = SDCardUtils.getSDCardPathByEnvironment() + "/ZQW/grainFull/Video/";

    /**
     * 音频缓存路径
     */
    String AUDIO_PATH = SDCardUtils.getSDCardPathByEnvironment() + "/Download/grainFull/";

    /*----------------------------------------------------业务变量-------------------------------------------------------*/
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
