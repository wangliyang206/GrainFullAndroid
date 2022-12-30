package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeText extends TextView implements Runnable {

    // 当前滚动的位置
    private int currentScrollX;
    // 是否停止
    private boolean isStop = false;
    // 文本宽度
    private int textWidth;
    // 是否测量
    private boolean isMeasure = false;

    public MarqueeText(Context context) {
        super(context);
        // todo auto-generated constructor stub

    }

    public MarqueeText(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public MarqueeText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // todo auto-generated method stub
        super.onDraw(canvas);

        // 文字宽度只需获取一次就可以了
        if (!isMeasure) {
            getTextWidth();
            isMeasure = true;
        }
    }

    /**
     * 获取文字宽度
     */
    private void getTextWidth() {
        Paint paint = this.getPaint();
        String str = this.getText().toString();
        textWidth = (int) paint.measureText(str);

    }

    @Override
    public void run() {
        currentScrollX -= 2;// 滚动速度
        scrollTo(currentScrollX, 0);
        if (isStop) {
            return;
        }

        if (getScrollX() <= -(this.getWidth())) {
            scrollTo(textWidth, 0);
            currentScrollX = textWidth;
            // return;
        }

        postDelayed(this, 5);
    }

    // 开始滚动
    public void startScroll() {
        isStop = false;
        this.removeCallbacks(this);
        post(this);

    }

    // 停止滚动
    public void stopScroll() {
        isStop = true;
    }

    // 从头开始滚动
    public void startFor0() {
        currentScrollX = 0;
        startScroll();
    }

}