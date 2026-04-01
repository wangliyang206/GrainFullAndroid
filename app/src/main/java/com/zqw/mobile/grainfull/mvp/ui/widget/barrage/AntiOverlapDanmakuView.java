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
        float lastEndX; // 记录当前轨道最后一条弹幕的尾部X
    }

    private final Paint mTextPaint;
    private final Paint mBgPaint;
    private final List<DanmakuItem> mDanmakuList = new ArrayList<>();
    private final Random mRandom = new Random();

    private boolean isRunning = true;
    private final int mTrackCount = 6;        // 6条轨道
    private final Track[] mTracks;
    private final float mTextSize = 36;
    private final float mPadding = 16;
    private final float mCornerRadius = 20;
    private final float mSPEED = 3.2f;       // 统一速度（绝不超车）
    private final float mMIN_GAP = 150;      // 强制安全间距（绝对不重叠核心）

    private final List<String> mExternalQueue = new ArrayList<>();
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
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(mTextSize);

        // 半透明背景
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(Color.argb(160, 0, 0, 0));

        startAutoSend();
    }

    // ====================== 外部接口 ======================
    public void addDanmaku(String text) {
        synchronized (mExternalQueue) {
            mExternalQueue.add(text);
        }
    }

    public void addDanmakuList(List<String> textList) {
        if (textList == null || textList.isEmpty()) return;
        synchronized (mExternalQueue) {
            mExternalQueue.addAll(textList);
        }
    }

    // ====================== 1秒自动发1条 ======================
    private void startAutoSend() {
        mTimer = Executors.newSingleThreadScheduledExecutor();
        mTimer.scheduleAtFixedRate(() -> {
            String text;
            synchronized (mExternalQueue) {
                if (mExternalQueue.isEmpty()) return;
                text = mExternalQueue.remove(0);
            }
            post(() -> dispatchDanmaku(text));
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    // ====================== 核心：绝对不重叠算法 ======================
    private void dispatchDanmaku(String text) {
        if (!isRunning || getWidth() == 0) return;

        float textWidth = mTextPaint.measureText(text);
        int trackIndex = mRandom.nextInt(mTrackCount); // 随机轨道（可重复）
        Track track = mTracks[trackIndex];

        // 【关键】新弹幕必须在上一条尾部 + 安全间距之后才出现
        float startX = Math.max(getWidth(), track.lastEndX + mMIN_GAP);

        // 垂直居中
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float lineH = getHeight() / (float) mTrackCount;
        float y = lineH * (trackIndex + 0.5f) - (fm.ascent + fm.descent) / 2;

        DanmakuItem item = new DanmakuItem();
        item.text = text;
        item.textWidth = textWidth;
        item.track = trackIndex;
        item.x = startX;
        item.y = y;

        mDanmakuList.add(item);
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

            // 背景
            float left = item.x - mPadding;
            float top = item.y + fm.top - mPadding;
            float right = item.x + item.textWidth + mPadding;
            float bottom = item.y + fm.bottom + mPadding;
            canvas.drawRoundRect(new RectF(left, top, right, bottom), mCornerRadius, mCornerRadius, mBgPaint);

            // 文字
            canvas.drawText(item.text, item.x, item.y, mTextPaint);

            // 匀速移动
            item.x -= mSPEED;

            // 实时更新轨道尾部位置（保证后续弹幕正确计算间距）
            mTracks[item.track].lastEndX = item.x + item.textWidth;

            // 完全滑出再回收
            if (item.x + item.textWidth + mPadding * 4 < 0) {
                mDanmakuList.remove(i);
            }
        }
        invalidate();
    }

    // ====================== 控制 ======================
    public void start() { isRunning = true; }
    public void pause() { isRunning = false; }

    public void clear() {
        mDanmakuList.clear();
        synchronized (mExternalQueue) { mExternalQueue.clear(); }
        for (Track t : mTracks) t.lastEndX = -mMIN_GAP;
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTimer != null) mTimer.shutdown();
    }
}