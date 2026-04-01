package com.zqw.mobile.grainfull.mvp.ui.widget.barrage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 原生 Java 实现 多行弹幕
 * 功能：多行随机滚动、自动回收、开始/暂停/清空、白色文字 + 半透明背景
 * 支持：轨道复用、弹幕不重叠、完整展示
 */

public class AntiOverlapDanmakuView extends View {

    private static class DanmakuItem {
        String text;
        float x;
        float y;
        float textWidth;
        int track;
    }

    private static class Track {
        float lastEndX; // 轨道最后一条弹幕的尾部X坐标
    }

    private final Paint mTextPaint;
    private final Paint mBgPaint;
    private final List<DanmakuItem> mDanmakuList = new ArrayList<>();
    private final Random mRandom = new Random();

    private boolean isRunning = true;
    private final int mTrackCount = 6; // 保持6行轨道
    private final Track[] mTracks;
    private final float mTextSize = 36;
    private final float mPadding = 16;
    private final float mCornerRadius = 20;
    private final float mSPEED = 3.2f; // 统一速度，不超车
    private final float mMIN_GAP = 120; // 同轨道弹幕强制间距（不重叠核心）

    private final List<String> mExternalDanmakuQueue = new ArrayList<>();
    private ScheduledExecutorService mTimer;

    public AntiOverlapDanmakuView(Context context) {
        this(context, null);
    }

    public AntiOverlapDanmakuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTracks = new Track[mTrackCount];
        for (int i = 0; i < mTrackCount; i++) {
            mTracks[i] = new Track();
            mTracks[i].lastEndX = -mMIN_GAP;
        }

        // 白色文字
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(mTextSize);

        // 半透明背景
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(Color.argb(160, 0, 0, 0));

        startAutoSend();
    }

    // ====================== 外部调用接口 ======================
    // 添加单条弹幕
    public void addDanmaku(String text) {
        synchronized (mExternalDanmakuQueue) {
            mExternalDanmakuQueue.add(text);
        }
    }

    // 添加弹幕集合
    public void addDanmakuList(List<String> textList) {
        if (textList == null || textList.isEmpty()) return;
        synchronized (mExternalDanmakuQueue) {
            mExternalDanmakuQueue.addAll(textList);
        }
    }

    // ====================== 自动发送（1秒1条） ======================
    private void startAutoSend() {
        mTimer = Executors.newSingleThreadScheduledExecutor();
        mTimer.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                String text = null;
                synchronized (mExternalDanmakuQueue) {
                    if (!mExternalDanmakuQueue.isEmpty()) {
                        text = mExternalDanmakuQueue.remove(0);
                    }
                }
                if (text != null) {
                    String finalText = text;
                    post(new Runnable() {
                        @Override
                        public void run() {
                            dispatchDanmaku(finalText);
                        }
                    });
                }
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    // ====================== 核心：弹幕分发（轨道可重复，绝不重叠） ======================
    private void dispatchDanmaku(String text) {
        if (!isRunning || getWidth() == 0) return;

        float textWidth = mTextPaint.measureText(text);
        // 随机选轨道 → 轨道可以重复！
        int trackIndex = mRandom.nextInt(mTrackCount);
        Track track = mTracks[trackIndex];

        // 计算起始X：自动跟上一条拉开间距，保证不重叠
        float startX = Math.max(getWidth(), track.lastEndX + mMIN_GAP);

        // 计算垂直居中Y
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float lineHeight = getHeight() / (float) mTrackCount;
        float y = lineHeight * (trackIndex + 0.5f) - (fm.ascent + fm.descent) / 2;

        // 创建弹幕
        DanmakuItem item = new DanmakuItem();
        item.text = text;
        item.textWidth = textWidth;
        item.track = trackIndex;
        item.x = startX;
        item.y = y;

        mDanmakuList.add(item);
        // 更新轨道最后位置
        track.lastEndX = startX + textWidth;
    }

    // ====================== 绘制 & 滚动 ======================
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isRunning) return;

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();

        for (int i = mDanmakuList.size() - 1; i >= 0; i--) {
            DanmakuItem item = mDanmakuList.get(i);

            // 绘制背景
            float left = item.x - mPadding;
            float top = item.y + fm.top - mPadding;
            float right = item.x + item.textWidth + mPadding;
            float bottom = item.y + fm.bottom + mPadding;
            RectF rect = new RectF(left, top, right, bottom);
            canvas.drawRoundRect(rect, mCornerRadius, mCornerRadius, mBgPaint);

            // 绘制文字
            canvas.drawText(item.text, item.x, item.y, mTextPaint);

            // 匀速移动
            item.x -= mSPEED;

            // 更新轨道尾部坐标（关键：实时计算距离）
            mTracks[item.track].lastEndX = item.x + item.textWidth;

            // 完全滑出屏幕再回收
            if (item.x + item.textWidth + mPadding * 4 < 0) {
                mDanmakuList.remove(i);
            }
        }
        invalidate();
    }

    // ====================== 控制方法 ======================
    public void start() {
        isRunning = true;
    }

    public void pause() {
        isRunning = false;
    }

    public void clear() {
        mDanmakuList.clear();
        synchronized (mExternalDanmakuQueue) {
            mExternalDanmakuQueue.clear();
        }
        for (Track t : mTracks) t.lastEndX = -mMIN_GAP;
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTimer != null) mTimer.shutdown();
    }
}