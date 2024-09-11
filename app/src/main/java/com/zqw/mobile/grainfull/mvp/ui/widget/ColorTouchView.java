package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;

import com.jess.arms.utils.ArmsUtils;

import java.util.Random;

import timber.log.Timber;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.widget
 * @ClassName: MultiTouchView
 * @Description:
 * @Author: WLY
 * @CreateDate: 2024/9/10 17:06
 */
public class ColorTouchView extends View {
    private static final int MAX_TOUCH_POINTS = 10;
    private PointF[] touchPoints = new PointF[MAX_TOUCH_POINTS];
    private Paint[] paints = new Paint[MAX_TOUCH_POINTS];
    private Runnable selectRandomTouchPointRunnable;
    // 被选中的触摸点索引
    private int selectedIndex = -1;
    // 是否抽签完毕(用于控制最后效果)
    private boolean isSucc;
    // 最终赢家的颜色
    private @ColorInt
    int color;
    // 赢家显示的文字
    private Paint mTextPaint;

    public ColorTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
        selectRandomTouchPointRunnable = this::selectRandomTouchPoint;
    }

    private void initPaints() {
        for (int i = 0; i < MAX_TOUCH_POINTS; i++) {
            paints[i] = new Paint();
            paints[i].setColor(getRandomColor());
            paints[i].setAntiAlias(true);
            paints[i].setStyle(Paint.Style.FILL);
        }

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.parseColor("#ffffff"));
        //居中绘制文字
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(ArmsUtils.sp2px(getContext(), 18));
    }

    /**
     * 获取随机颜色
     */
    private int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (selectedIndex > -1) {
            // 已抽签
            if (touchPoints[selectedIndex] != null) {
                if (isSucc) {
                    // 赢家专属满屏
                    canvas.drawColor(color);
                    canvas.drawText("恭喜您！", getWidth() / 2, getHeight() / 2, mTextPaint);
                } else {
                    canvas.drawCircle(touchPoints[selectedIndex].x, touchPoints[selectedIndex].y, 150, paints[selectedIndex]);
                    // 获取颜色值
                    color = paints[selectedIndex].getColor();
                    postDelayed(() -> {
                        isSucc = true;
                        invalidate();
                    }, 1000);
                }
            }
        } else {
            // 未抽签
            for (int i = 0; i < MAX_TOUCH_POINTS; i++) {
                if (touchPoints[i] != null) {
                    canvas.drawCircle(touchPoints[i].x, touchPoints[i].y, 150, paints[i]);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int pointerIndex = event.getActionIndex();
        int id = event.getPointerId(pointerIndex);

        switch (action) {
            case MotionEvent.ACTION_DOWN:                                                           // 按下
            case MotionEvent.ACTION_POINTER_DOWN:
                touchPoints[id] = new PointF(event.getX(pointerIndex), event.getY(pointerIndex));
                if (selectedIndex != -1) {
                    selectedIndex = -1;
                    isSucc = false;
                }
                invalidate();
                // 清除之前的定时器
                removeCallbacks(selectRandomTouchPointRunnable);
                // 重新设置定时器
                postDelayed(selectRandomTouchPointRunnable, 5000);
                break;
            case MotionEvent.ACTION_MOVE:                                                           // 移动
                touchPoints[id].x = event.getX(pointerIndex);
                touchPoints[id].y = event.getY(pointerIndex);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:                                                             // 抬起
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                touchPoints[id] = null;
                invalidate();
                break;
        }

        return true;
    }

    /**
     * 选择随机触摸点
     */
    private void selectRandomTouchPoint() {
        int winnerIndex = -1;
        for (int i = 0; i < MAX_TOUCH_POINTS; i++) {
            if (touchPoints[i] != null) {
                if (winnerIndex == -1 || Math.random() < 0.5) {
                    winnerIndex = i;
                }
            }
        }

        if (winnerIndex != -1) {
            selectedIndex = winnerIndex;
            Timber.i("### 随机抽中号码：%s", selectedIndex);
            invalidate();
        }
    }

}