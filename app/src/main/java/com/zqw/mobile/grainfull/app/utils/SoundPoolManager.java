package com.zqw.mobile.grainfull.app.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.util.SparseIntArray;

/**
 * Description : 打地鼠 - 声音池管理器
 */
public class SoundPoolManager implements SoundPool.OnLoadCompleteListener {

    private static final String TAG = SoundPoolManager.class.getSimpleName();
    private SoundPool.Builder spBuilder;
    private SoundPool soundPool;
    private SparseIntArray fmArray;

    public void init(Context context, int[] soundArray) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (null == spBuilder) {
                spBuilder = new SoundPool.Builder();
                AudioAttributes.Builder builder = new AudioAttributes.Builder();
                builder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
                spBuilder.setAudioAttributes(builder.build());
                spBuilder.setMaxStreams(10);
            }
            if (null == soundPool) {
                soundPool = spBuilder.build();
            }
        } else {
            if (null == soundPool) {
                soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,
                        10); //最多播放10个音效，格式为Steam_music，音质为10
            }
        }

        soundPool.setOnLoadCompleteListener(this);
        if (null != soundPool) {

            if (null == fmArray) {
                fmArray = new SparseIntArray();
            }
            for (int i = 0; i < soundArray.length; i++) {
                //将需要播放的资源添加到SoundPool中，并保存返回的StreamID，通过StreamID可以停止某个音效
                fmArray.put(i, soundPool.load(context, soundArray[i], 1));
            }
        }
    }

    public void play(int position) {

        if (null == soundPool || position < 0 || null == fmArray || fmArray.size() <= 0) {
            return;
        }

        if (position >= fmArray.size()) {
            return;
        }

        soundPool.play(fmArray.get(position), 1, 1, 0, 0, 1);
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        Log.d(TAG, " onLoadComplete");
    }
}
