package com.zqw.mobile.grainfull.app.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.ui.activity.OneLineToEndActivity;

/**
 * 一笔画完 - 播放音乐服务
 */
public class OneLineToEndMusicService extends Service {

    private MediaPlayer mediaPlayer;
    private boolean isStop = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final int op;
        if (intent == null) op = 2;
        else {
            op = intent.getIntExtra(OneLineToEndActivity.key_toPlayMusic, 2);
        }
        switch (op) {
            case 0:
                if (mediaPlayer == null || isStop) {
                    if (mediaPlayer != null) mediaPlayer.release();
                    mediaPlayer = MediaPlayer.create(this, R.raw.one_line_to_end);
                    mediaPlayer.setLooping(true);
                }
                mediaPlayer.start();
                isStop = false;
                break;
            case 1:
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    isStop = false;
                }
                break;
            default:
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    isStop = true;
                }
                break;
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }
}
