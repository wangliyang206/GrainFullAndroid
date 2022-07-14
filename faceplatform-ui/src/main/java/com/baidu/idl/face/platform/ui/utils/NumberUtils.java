package com.baidu.idl.face.platform.ui.utils;

/**
 * 数字工具类
 */
public class NumberUtils {

    /**
     * float转double
     * @param num float数据
     * @return    double数据
     */
    public static double floatToDouble(float num) {
        return Double.valueOf(String.valueOf(num));
    }
}
