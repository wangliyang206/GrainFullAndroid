package com.jess.arms.utils;

import android.app.Activity;
import android.os.Build;

import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

/**
 * StatusBarCompat 升级版工具类(可修改状态栏顶部)
 */
public class StatusBarCompatUtils {

    /**
     * SDK >= 23, 将状态栏改为浅色模式(状态栏 icon 和字体会变成深色)
     */
    public static void changeToLightStatusBar(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (activity == null) {
            return;
        }
        Window window = activity.getWindow();
        if (window == null) {
            return;
        }
        View decorView = window.getDecorView();
        if (decorView == null) {
            return;
        }
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    /**
     * 将状态栏改为深色模式(状态栏 icon 和字体会变成浅色, 即默认模式)
     */
    public static void cancelLightStatusBar(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (activity == null) {
            return;
        }
        Window window = activity.getWindow();
        if (window == null) {
            return;
        }
        View decorView = window.getDecorView();
        if (decorView == null) {
            return;
        }
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & (~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
    }
}
