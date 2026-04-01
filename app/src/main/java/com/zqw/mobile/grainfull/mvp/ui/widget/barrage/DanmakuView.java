package com.zqw.mobile.grainfull.mvp.ui.widget.barrage;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 原生Java实现弹幕View
 */
public class DanmakuView extends View {

    // 弹幕实体类
    public static class DanmakuItem {
        public String text;    // 弹幕文字
        public int color;     // 文字颜色
        public float size;    // 文字大小
        public float x;       // X坐标（滚动用）
        public float y;       // Y坐标
        public float speed;   // 滚动速度
    }

    private final Paint mPaint;
    private final List<DanmakuItem> mDanmakuList = new ArrayList<>();
    private final Random mRandom = new Random();
    private int mLineCount = 5; // 弹幕行数
    private boolean isRunning = true;

    public DanmakuView(Context context) {
        this(context, null);
    }

    public DanmakuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true); // 抗锯齿
    }

    /**
     * 添加一条弹幕
     */
    public void addDanmaku(String text, int textColor, float textSize) {
        DanmakuItem item = new DanmakuItem();
        item.text = text;
        item.color = textColor;
        item.size = textSize;

        // 初始X：屏幕右侧外
        item.x = getWidth();
        // 随机分配到某一行
        float lineHeight = getHeight() / (float) mLineCount;
        int line = mRandom.nextInt(mLineCount);
        item.y = lineHeight * (line + 0.5f); // 垂直居中

        // 随机速度
        item.speed = 3 + mRandom.nextFloat() * 4;

        mDanmakuList.add(item);
    }

    /**
     * 批量添加弹幕
     */
    public void addDanmakuList(List<String> texts) {
        for (String text : texts) {
            int color = Color.rgb(mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
            addDanmaku(text, color, 40);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isRunning) return;

        // 遍历绘制所有弹幕
        for (int i = mDanmakuList.size() - 1; i >= 0; i--) {
            DanmakuItem item = mDanmakuList.get(i);
            mPaint.setColor(item.color);
            mPaint.setTextSize(item.size);

            // 绘制文字
            canvas.drawText(item.text, item.x, item.y, mPaint);

            // 滚动
            item.x -= item.speed;

            // 移除滚出屏幕的弹幕
            if (item.x + mPaint.measureText(item.text) < 0) {
                mDanmakuList.remove(i);
            }
        }

        // 持续刷新
        invalidate();
    }

    /**
     * 开始弹幕
     */
    public void start() {
        isRunning = true;
        invalidate();
    }

    /**
     * 暂停弹幕
     */
    public void pause() {
        isRunning = false;
    }

    /**
     * 清空所有弹幕
     */
    public void clear() {
        mDanmakuList.clear();
        invalidate();
    }

    /**
     * 设置弹幕行数
     */
    public void setLineCount(int lineCount) {
        this.mLineCount = lineCount;
    }
}
