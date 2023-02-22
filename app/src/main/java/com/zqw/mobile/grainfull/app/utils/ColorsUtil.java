package com.zqw.mobile.grainfull.app.utils;

import android.graphics.Color;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.app.utils
 * @ClassName: ColorsUtil
 * @Description: 颜色工具
 * @Author: WLY
 * @CreateDate: 2023/1/3 17:29
 */
public class ColorsUtil {
    private ColorsUtil() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * 计算渐变颜色中间色值
     *
     * @param startColor 起始颜色
     * @param endColor   结束颜色
     * @param radio      百分比，取值范围【0~1】
     * @return 颜色值
     */
    public static int getColor(int startColor, int endColor, float radio) {
        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);

        int red = (int) (redStart + ((redEnd - redStart) * radio + 0.5));
        int greed = (int) (greenStart + ((greenEnd - greenStart) * radio + 0.5));
        int blue = (int) (blueStart + ((blueEnd - blueStart) * radio + 0.5));
        return Color.argb(255, red, greed, blue);
    }

    /**
     * 获取十六进制字符串
     */
    public static String getHexString(int color, boolean showAlpha) {
        int base = showAlpha ? 0xFFFFFFFF : 0xFFFFFF;
        String format = showAlpha ? "#%08X" : "#%06X";
        return String.format(format, (base & color)).toUpperCase();
    }

    /**
     * 颜色亮度
     */
    public static int colorAtLightness(int color, float lightness) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = lightness;
        return Color.HSVToColor(hsv);
    }

    public static int adjustAlpha(float alpha, int color) {
        return alphaValueAsInt(alpha) << 24 | (0x00ffffff & color);
    }

    public static int alphaValueAsInt(float alpha) {
        return Math.round(alpha * 255);
    }

    /**
     * 获取字母百分比
     */
    public static float getAlphaPercent(int argb) {
        return Color.alpha(argb) / 255f;
    }

    /**
     * 颜色的亮度
     */
    public static float lightnessOfColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return hsv[2];
    }

    /**
     * Returns the cmyk according to the rgb parameters
     *
     * @param red
     * @param green
     * @param blue
     * @return float[]
     */

    public static float[] getCMYK(int red, int green, int blue) {
        float[] list = new float[4];
        float r;
        float g;
        float b;
        float highestValue;
        r = red / 255f;
        g = green / 255f;
        b = blue / 255f;
        highestValue = Math.max(r, g);
        highestValue = Math.max(highestValue, b);

        list[3] = (float) 1 - highestValue;

        list[0] = (1 - r - list[3]) / (1 - list[3]);
        list[1] = (1 - g - list[3]) / (1 - list[3]);
        list[2] = (1 - b - list[3]) / (1 - list[3]);
        return list;
    }

    /**
     * Returns the rgb value according to the cmyk parameters
     *
     * @param cyan
     * @param magenta
     * @param yellow
     * @param key
     * @return int[]
     */

    public static int[] getRGB(float cyan, float magenta, float yellow, float key) {
        int[] arrReturn = new int[3];
        arrReturn[0] = Math.round(255 * (1 - cyan) * (1 - key));
        arrReturn[1] = Math.round(255 * (1 - magenta) * (1 - key));
        arrReturn[2] = Math.round(255 * (1 - yellow) * (1 - key));
        return arrReturn;
    }
}
