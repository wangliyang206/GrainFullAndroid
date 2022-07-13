/**
 * Copyright (C) 2017 Baidu Inc. All rights reserved.
 */
package com.baidu.idl.face.platform.ui.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * VolumeUtils
 * 描述:系统音量监听
 */
public class VolumeUtils {

    public static final String TAG = VolumeUtils.class.getSimpleName();

    public interface VolumeCallback {
        void volumeChanged();
    }

    public static class VolumeReceiver extends BroadcastReceiver {

        private VolumeCallback callback;

        public VolumeReceiver(VolumeCallback cb) {
            callback = cb;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")
                    && callback != null) {
                Log.e(TAG, "android.media.VOLUME_CHANGED_ACTION");
                callback.volumeChanged();
            }
        }
    }

    public static BroadcastReceiver registerVolumeReceiver(Context context, VolumeCallback callback) {
        VolumeReceiver mVolumeReceiver = null;
        try {
            mVolumeReceiver = new VolumeReceiver(callback);
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.media.VOLUME_CHANGED_ACTION");
            context.registerReceiver(mVolumeReceiver, filter);
        } catch (IllegalArgumentException ex1) {
            ex1.printStackTrace();
        } catch (Exception ex2) {
            ex2.printStackTrace();
        }
        return mVolumeReceiver;
    }

    public static void unRegisterVolumeReceiver(Context context, BroadcastReceiver receiver) {
        try {
            if (context != null && receiver != null) {
                context.unregisterReceiver(receiver);
            }
        } catch (IllegalArgumentException ex1) {
            ex1.printStackTrace();
        } catch (Exception ex2) {
            ex2.printStackTrace();
        }
    }
}
