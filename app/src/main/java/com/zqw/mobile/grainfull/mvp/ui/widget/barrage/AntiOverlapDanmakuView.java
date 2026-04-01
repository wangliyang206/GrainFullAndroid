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

/**
 * 原生Java实现 多行弹幕
 * 功能：多行随机滚动、自动回收、开始/暂停/清空、白色文字+半透明背景
 */
public class AntiOverlapDanmakuView extends View {

    // 弹幕实体
    private static class DanmakuItem {
        String text;
        float x, y;
        float speed;
        float textWidth;
        float textHeight;
    }

    private final Paint mTextPaint;
    private final Paint mBgPaint;
    private final List<DanmakuItem> mDanmakuList = new ArrayList<>();
    private final Random mRandom = new Random();

    private boolean isRunning = true;
    private int mLineCount = 6;
    private float mTextSize = 36;
    private final float mPadding = 16;
    private final float mCornerRadius = 20;

    public AntiOverlapDanmakuView(Context context) {
        this(context, null);
    }

    public AntiOverlapDanmakuView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 白色文字
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // 半透明背景
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(Color.argb(160, 0, 0, 0));
    }

    public void addDanmaku(String text) {
        if (getWidth() == 0 || getHeight() == 0) return;

        DanmakuItem item = new DanmakuItem();
        item.text = text;
        item.textWidth = mTextPaint.measureText(text);

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        item.x = getWidth();

        float lineHeight = getHeight() / (float) mLineCount;
        int line = mRandom.nextInt(mLineCount);
        item.y = lineHeight * (line + 0.5f) - (fm.ascent + fm.descent) / 2;

        item.speed = 2.5f + mRandom.nextFloat() * 2.5f;
        mDanmakuList.add(item);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isRunning) return;

        // 提前获取 FontMetrics
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();

        for (int i = mDanmakuList.size() - 1; i >= 0; i--) {
            DanmakuItem item = mDanmakuList.get(i);

            // 背景位置（修复后）
            float left = item.x - mPadding;
            float top = item.y + fm.top - mPadding;  // 这里已修复
            float right = item.x + item.textWidth + mPadding;
            float bottom = item.y + fm.bottom + mPadding;

            RectF bgRect = new RectF(left, top, right, bottom);
            canvas.drawRoundRect(bgRect, mCornerRadius, mCornerRadius, mBgPaint);

            // 绘制文字
            canvas.drawText(item.text, item.x, item.y, mTextPaint);

            // 滚动
            item.x -= item.speed;

            // 自动回收
            if (item.x + item.textWidth < 0) {
                mDanmakuList.remove(i);
            }
        }

        if (!mDanmakuList.isEmpty()) {
            invalidate();
        }
    }

    public void start() {
        isRunning = true;
        invalidate();
    }

    public void pause() {
        isRunning = false;
    }

    public void clear() {
        mDanmakuList.clear();
        invalidate();
    }

    public void setLineCount(int lineCount) {
        this.mLineCount = lineCount;
    }
}
