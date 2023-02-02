package com.zqw.mobile.grainfull.app.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Description : 打地鼠 - 背景音乐
 */
public class BackgroundMusic {

    private static final String TAG = "Bg_Music";
    private float mLeftVolume;
    private float mRightVolume;
    private Context mContext;
    private MediaPlayer mBackgroundMediaPlayer;
    private boolean mIsPaused;
    private String mCurrentPath;

    public BackgroundMusic(Context context) {
        this.mContext = context;
        initData();
    }

    /**
     * 初始化一些数据
     */
    private void initData() {
        mLeftVolume = 0.6f;
        mRightVolume = 0.6f;
        mBackgroundMediaPlayer = null;
        mIsPaused = false;
        mCurrentPath = null;
    }

    /**
     * 根据path路径播放背景音乐
     *
     * @param path   :assets中的音频路径
     * @param isLoop :是否循环播放
     */
    public void playBackgroundMusic(String path, boolean isLoop) {
        if (mCurrentPath == null) {
            //这是第一次播放背景音乐--- it is the first time to play background music
            //或者是执行end()方法后，重新被叫---or end() was called
            mBackgroundMediaPlayer = createMediaPlayerFromAssets(path);
            mCurrentPath = path;
        } else {
            if (!mCurrentPath.equals(path)) {
                //播放一个新的背景音乐--- play new background music
                //释放旧的资源并生成一个新的----release old resource and create a new one
                if (mBackgroundMediaPlayer != null) {
                    mBackgroundMediaPlayer.release();
                }
                mBackgroundMediaPlayer = createMediaPlayerFromAssets(path);
                //记录这个路径---record the path
                mCurrentPath = path;
            }
        }

        if (mBackgroundMediaPlayer == null) {
            Log.e(TAG, "playBackgroundMusic: background media player is null");
        } else {
            // 若果音乐正在播放或已近中断，停止它---if the music is playing or paused, stop it
            mBackgroundMediaPlayer.stop();
            mBackgroundMediaPlayer.setLooping(isLoop);
            try {
                mBackgroundMediaPlayer.prepare();
                mBackgroundMediaPlayer.seekTo(0);
                mBackgroundMediaPlayer.start();
                this.mIsPaused = false;
            } catch (Exception e) {
                Log.e(TAG, "playBackgroundMusic: error state");
            }
        }
    }

    /**
     * 从 assets 中创建音乐播放器
     */
    private MediaPlayer createMediaPlayerFromAssets(String path) {
        MediaPlayer mediaPlayer;
        try {
            AssetFileDescriptor assetFileDescriptor = mContext.getAssets().openFd(path);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            mediaPlayer.prepare();
            mediaPlayer.setVolume(mLeftVolume, mRightVolume);
        } catch (Exception e) {
            mediaPlayer = null;
            Log.e(TAG, "create mediaPlayer error: " + e.getMessage());
        }
        return mediaPlayer;
    }

    /**
     * 停止播放背景音乐
     */
    public void stopBackgroundMusic() {
        if (mBackgroundMediaPlayer != null) {
            mBackgroundMediaPlayer.stop();
            // should set the state, if not , the following sequence will be error
            // play -> pause -> stop -> resume
            this.mIsPaused = false;
        }
    }

    /**
     * 暂停播放背景音乐
     */
    public void pauseBackgroundMusic() {
        if (mBackgroundMediaPlayer != null && mBackgroundMediaPlayer.isPlaying()) {
            mBackgroundMediaPlayer.pause();
            this.mIsPaused = true;
        }
    }

    /**
     * 继续播放背景音乐
     */
    public void resumeBackgroundMusic() {
        if (mBackgroundMediaPlayer != null && this.mIsPaused) {
            mBackgroundMediaPlayer.start();
            this.mIsPaused = false;
        }
    }

    /**
     * 重新播放背景音乐
     */
    public void rewindBackgroundMusic() {
        if (mBackgroundMediaPlayer != null) {
            mBackgroundMediaPlayer.stop();
            try {
                mBackgroundMediaPlayer.prepare();
                mBackgroundMediaPlayer.seekTo(0);
                mBackgroundMediaPlayer.start();
                this.mIsPaused = false;
            } catch (Exception e) {
                Log.e(TAG, "rewindBackgroundMusic: error state");
            }
        }
    }

    /**
     * 判断背景音乐是否正在播放
     */
    public boolean isPlaying() {
        boolean ret;
        if (mBackgroundMediaPlayer == null) {
            ret = false;
        } else {
            ret = mBackgroundMediaPlayer.isPlaying();
        }
        return ret;
    }

    /**
     * 结束背景音乐，并释放资源
     */
    public void end() {
        if (mBackgroundMediaPlayer != null) {
            mBackgroundMediaPlayer.release();
        }
        //重新“初始化数据”
        initData();
    }

    /**
     * 得到背景音乐的“音量”
     */
    public float getBackgroundVolume() {
        if (this.mBackgroundMediaPlayer != null) {
            return (this.mLeftVolume + this.mRightVolume) / 2;
        } else {
            return 0.0f;
        }
    }

    /**
     * 设置背景音乐的音量
     */
    public void setBackgroundVolume(float volume) {
        this.mLeftVolume = this.mRightVolume = volume;
        if (this.mBackgroundMediaPlayer != null) {
            this.mBackgroundMediaPlayer.setVolume(this.mLeftVolume, this.mRightVolume);
        }
    }
}
