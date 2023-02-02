package com.zqw.mobile.grainfull.app.utils;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.zqw.mobile.grainfull.app.global.GameMsg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * Description : 随机线程（打地鼠游戏用到）
 */
public class RandomThread extends Thread {

    private static final String TAG = RandomThread.class.getSimpleName();
    private static final long GAME_TIME = 18 * 1000;

    private static final int MAX_MUSHROOM = 12;

    private boolean threadControl;
    private boolean startFlag;

    private Handler mainHandler;
    private Random gameRandom;
    private HashSet<Integer> mKeepSet;
    private ArrayList<AnimationDrawable> mRatAnimList;
    private ArrayList<AnimationDrawable> mMushRoomAnimList;

    private CountDownTimer mCountDownTimer;
    private BackgroundMusic mBgMusicManager;
    private boolean isNearEnd;  //判断接近尾声 就不在出精灵了
    private int mCurMushroomCount;

    private int speedControl;
    private int bgIndex;

    public RandomThread(Context context, Handler handler, HashSet<Integer> container) {
        this.mainHandler = handler;
        this.threadControl = true;
        this.startFlag = false;
        this.gameRandom = new Random();
        this.mKeepSet = container;

        mBgMusicManager = new BackgroundMusic(context);

        mCountDownTimer = new CountDownTimer(GAME_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                if (millisUntilFinished < 1300) {
                    isNearEnd = true;
                }

                Message.obtain(mainHandler, GameMsg.MSG_WHAT_GAP,
                        (int) (millisUntilFinished / 1000), 0).sendToTarget();
            }

            @Override
            public void onFinish() {
                mainHandler.sendEmptyMessage(GameMsg.MSG_WHAT_END);
            }
        };
    }

    public void setAnimList(ArrayList<AnimationDrawable> ratList,
                            ArrayList<AnimationDrawable> mushList) {
        this.mRatAnimList = ratList;
        this.mMushRoomAnimList = mushList;
    }

    @Override
    public void run() {
        super.run();

        while (threadControl) {

            if (startFlag && !isNearEnd) {

                handleRandom();
                try {

                    ++speedControl;
                    if (speedControl > 20) {
                        Thread.sleep(300);
                    } else if (speedControl > 15) {
                        Thread.sleep(460);
                    } else if (speedControl > 10) {
                        Thread.sleep(550);
                    } else if (speedControl > 5) {
                        Thread.sleep(750);
                    } else {
                        Thread.sleep(850);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    private void handleRandom() {

        //Log.d(TAG, "handleRandom -- 新的循环 找洞start -- ");

        //随机出来 接下来要出现 精灵的洞
        int hole = randomHole();
        //Log.d(TAG, "handleRandom -- 检测到可以出现精灵的洞 random hole : " + hole);

        Message obtain = Message.obtain(mainHandler);
        obtain.what = GameMsg.MSG_WHAT_REFRESH;
        obtain.arg1 = hole;

        //随机精灵模型
        int model = GameMsg.MODEL_RAT;
        if (mCurMushroomCount < MAX_MUSHROOM) {

            //蘑菇出现数量少于最大限制，就进行随机筛选 概率还是老鼠大
            int randomModel = Math.random() > 0.7 ? GameMsg.MODEL_MUSHROOM : GameMsg.MODEL_RAT;
            if (randomModel == GameMsg.MODEL_MUSHROOM) {
                mCurMushroomCount++;
            }
            model = randomModel;
        }
        obtain.arg2 = model;

        obtain.sendToTarget();
    }

    /**
     * 找到一个未播放的洞
     * 检测不符合要求就递归
     */
    private int randomHole() {

        //Log.d(TAG, "randomHole -- 执行随机找洞");

        int turnUp = gameRandom.nextInt(9);
        //Log.d(TAG, " randomHole -- 随机出来的数 : " + turnUp);

        boolean isPlaying = checkPlayingHole(turnUp);
        boolean isKeep = mKeepSet.contains(turnUp);

        if (isPlaying || isKeep) {
            return randomHole();
        } else {
            return turnUp;
        }
    }

    /**
     * 检查洞是否符合条件
     */
    private boolean checkPlayingHole(int turnUp) {

        AnimationDrawable animationDrawable1 = mRatAnimList.get(turnUp);
        AnimationDrawable animationDrawable2 = mMushRoomAnimList.get(turnUp);
        return animationDrawable1.isRunning() || animationDrawable2.isRunning();
    }

    public void startGame() {
        startFlag = true;
        speedControl = 0;
        mCurMushroomCount = 0;
        mCountDownTimer.start();
        mBgMusicManager.playBackgroundMusic(getMusic(), true);
    }

    @NonNull
    private String getMusic() {
        return bgIndex == 0 ? "game_bg.mp3" : "game_bg2.mp3";
    }

    public void changeBGMusic() {
        bgIndex = bgIndex == 0 ? 1 : 0;
    }

    public void stopGame() {

        mCountDownTimer.cancel();
        mBgMusicManager.stopBackgroundMusic();
        mBgMusicManager.end();
        threadControl = false;
    }

    public void release() {

        if (null != mBgMusicManager) {
            mBgMusicManager.stopBackgroundMusic();
            mBgMusicManager.end();
        }
    }
}
