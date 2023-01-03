package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import timber.log.Timber;

/**
 * 自定义TextView跑马灯效果可控制启动/停止/速度/焦点
 */
public class MarqueeText extends androidx.appcompat.widget.AppCompatTextView implements Runnable {

    // 当前滚动的位置
    private int currentScrollX;
    // 是否停止
    private boolean isStop = false;
    // 文本宽度
    private int textWidth;
    // 是否测量
    private boolean isMeasure = false;
    // 滚动速度
    private int rollingSpeed = 2;
    // 滚动方向，0代表从左向右，1代表从右向左
    private int mRollingDirection = 0;
    // 是否第一次进来，作用：从右向左滑动时用到了，控制滑动起点
    private boolean isComeIn = true;

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

        if (mRollingDirection == 0) {
            // 从左向右
            currentScrollX -= rollingSpeed;// 滚动速度
            scrollTo(currentScrollX, 0);
            if (isStop) {
                return;
            }

            if (getScrollX() <= -(this.getWidth())) {
                scrollTo(textWidth, 0);
                currentScrollX = textWidth;
            }
        } else {
            // 从右向左

            // 这句判断不能去，否则刚进来时会从最左滚动没后，才会从最右侧出现。
            if (isComeIn && getScrollX() == 0) {
                isComeIn = false;
                currentScrollX = -getWidth();
            }
            Timber.i("####currentScrollX=" + currentScrollX + "    rollingSpeed=" + rollingSpeed + "    getScrollX=" + getScrollX() + "    getWidth=" + getWidth());
            currentScrollX += rollingSpeed;// 滚动速度
            scrollTo(currentScrollX, 0);
            if (isStop) {
                return;
            }

            // 滚动到最左后，重新回到最右侧
            if (getScrollX() >= textWidth) {
                Timber.i("####getWidth=" + getWidth());
                scrollTo(-getWidth(), 0);
                currentScrollX = -getWidth();
                Timber.i("####currentScrollX=" + currentScrollX + "    getWidth=" + -getWidth());
            }
        }


        postDelayed(this, 5);
    }

    /**
     * 开始滚动
     */
    public void startScroll() {
        isStop = false;
        this.removeCallbacks(this);
        post(this);

    }

    /**
     * 停止滚动
     */
    public void stopScroll() {
        isStop = true;
    }

    /**
     * 开始滚动
     *
     * @param index 0代表从头开始滚动。
     */
    public void startScroll(int index) {
        currentScrollX = index;
        startScroll();
    }

    /**
     * 设置滚动速，值越低滚动速度越快。
     */
    public void setRollingSpeed(int speed) {
        this.rollingSpeed = speed;
    }

    /**
     * 设置滑动方向：0代表从左向右(默认)，1代表从右向左
     */
    public void setRollingDirection(int mDirection) {
        this.mRollingDirection = mDirection;
    }
}