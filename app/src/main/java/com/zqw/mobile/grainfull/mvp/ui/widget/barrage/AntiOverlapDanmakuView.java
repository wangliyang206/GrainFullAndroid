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
 * 修正版：绝对不重叠弹幕 View
 * 解决了同一轨道多弹幕重叠问题
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
        float lastEndX; // 记录该轨道最后一条弹幕的尾部X坐标
    }

    private final Paint mTextPaint;
    private final Paint mBgPaint;
    private final List<DanmakuItem> mDanmakuList = new ArrayList<>();
    private final Random mRandom = new Random();
    private boolean isRunning = true;
    private final int mTrackCount = 6;
    private final Track[] mTracks;
    private final float mTextSize = 36;
    private final float mPadding = 16;
    private final float mCornerRadius = 20;
    private final float mSPEED = 3.2f;
    // 这里的 MIN_GAP 只是逻辑上的最小间距，实际间距由轨道状态决定
    private final float mMIN_GAP = 150;
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
            // 初始化为负值，确保第一条弹幕可以从屏幕右侧进入
            mTracks[i].lastEndX = -mMIN_GAP;
        }

        // 文字画笔 (白色)
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(mTextSize);

        // 背景画笔 (半透明黑)
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

    // ====================== 自动发送逻辑 ======================
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

    // ====================== 核心：绝对不重叠算法 (关键修正) ======================
    private void dispatchDanmaku(String text) {
        if (!isRunning || getWidth() <= 0) return;

        float textWidth = mTextPaint.measureText(text);
        // 1. 寻找合适的轨道 (这里采用寻找最空闲轨道的策略，避免随机导致的拥堵)
        int trackIndex = findBestTrack();
        Track track = mTracks[trackIndex];

        // 2. 计算起始位置
        // 核心逻辑：新弹幕的起始X = Max(屏幕宽度, 轨道最后尾部 + 间距)
        // 这保证了无论上一条弹幕移动到哪里，新弹幕都在它后面
        float startX = Math.max(getWidth(), track.lastEndX + mMIN_GAP);

        // 3. 计算垂直位置 (居中)
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float lineH = getHeight() / (float) mTrackCount;
        float y = lineH * (trackIndex + 0.5f) - (fm.ascent + fm.descent) / 2;

        // 4. 创建弹幕对象
        DanmakuItem item = new DanmakuItem();
        item.text = text;
        item.textWidth = textWidth;
        item.track = trackIndex;
        item.x = startX;
        item.y = y;

        mDanmakuList.add(item);

        // 5. 【关键点】立即更新轨道状态
        // 这里记录的是这条新弹幕的尾部位置
        // 这样下次如果再选中这个轨道，新新弹幕就会排在这条的后面
        track.lastEndX = startX + textWidth;
    }

    // 寻找最佳轨道：寻找 lastEndX 最小的轨道（即最空闲的轨道）
    // 这样可以最大化利用屏幕空间，避免有的轨道挤满，有的轨道空着
    private int findBestTrack() {
        int bestIndex = 0;
        float minLastEndX = mTracks[0].lastEndX;

        for (int i = 1; i < mTrackCount; i++) {
            if (mTracks[i].lastEndX < minLastEndX) {
                minLastEndX = mTracks[i].lastEndX;
                bestIndex = i;
            }
        }
        return bestIndex;
    }

    // ====================== 绘制 & 滚动 ======================
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isRunning) return;

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();

        // 倒序遍历，防止删除时索引错乱
        for (int i = mDanmakuList.size() - 1; i >= 0; i--) {
            DanmakuItem item = mDanmakuList.get(i);

            // 1. 绘制背景圆角矩形
            float left = item.x - mPadding;
            float top = item.y + fm.top - mPadding;
            float right = item.x + item.textWidth + mPadding;
            float bottom = item.y + fm.bottom + mPadding;
            canvas.drawRoundRect(new RectF(left, top, right, bottom), mCornerRadius, mCornerRadius, mBgPaint);

            // 2. 绘制文字
            canvas.drawText(item.text, item.x, item.y, mTextPaint);

            // 3. 移动弹幕
            // 注意：这里只移动 item.x，绝对不更新 Track.lastEndX
            item.x -= mSPEED;

            // 4. 回收机制：当弹幕完全滑出屏幕左侧
            // 这里不需要重置 Track.lastEndX，因为 Track.lastEndX 记录的是逻辑位置
            // 即使弹幕滑出了，Track.lastEndX 依然保留着它消失前的尾部位置
            // 这样可以防止新弹幕从屏幕右侧“瞬移”过来填补空缺（产生闪烁或逻辑错误）
            // 只有当新弹幕被创建时，才会覆盖这个 lastEndX 值
            if (item.x + item.textWidth < -mPadding * 4) {
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
        synchronized (mExternalQueue) {
            mExternalQueue.clear();
        }
        // 重置所有轨道状态
        for (Track t : mTracks) {
            t.lastEndX = -mMIN_GAP;
        }
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTimer != null && !mTimer.isShutdown()) {
            mTimer.shutdown();
        }
    }
}