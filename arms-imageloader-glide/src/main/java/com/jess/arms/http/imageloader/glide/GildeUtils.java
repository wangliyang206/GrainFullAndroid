package com.jess.arms.http.imageloader.glide;

import android.content.Context;

/** 图片工具 */
public class GildeUtils {
    //SD卡下图片
    public static String getSDSource(String fullPath){
        return "file://"+ fullPath;
    }

    //ASSETS下图片
    public static String getAssetsSource(String fileName){
        return "file:///android_asset/"+fileName;
    }

    //Raw下视频可以解析一张图片
    public static String getRawSource(Context context, int rawRid){
        return "android.resource://"+context.getPackageName()+"/raw/"+rawRid;
    }

    //Drawable图片
    public static String getDrawableSource(Context context,int drawRid){
        return "android.resource://"+context.getPackageName()+"/drawable/"+drawRid;
    }
}
