package com.baidu.idl.face.platform.ui.utils;

public class IntentUtils {
    private static IntentUtils instance = null;

    private String mBitmap;

    /**
     * 单例模式
     * @return FaceSDKManager实体
     */
    public static IntentUtils getInstance() {
        if (instance == null) {
            synchronized (IntentUtils.class) {
                if (instance == null) {
                    instance = new IntentUtils();
                }
            }
        }
        return instance;
    }

    public void setBitmap(String bitmap) {
        mBitmap = bitmap;
    }

    public String getBitmap() {
        return mBitmap;
    }

    public void release() {
        if (instance != null) {
            instance = null;
        }
    }
}
