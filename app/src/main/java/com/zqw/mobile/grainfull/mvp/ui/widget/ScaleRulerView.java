package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.zqw.mobile.grainfull.app.utils.ColorsUtil;

/**
 * 刻度尺
 */
public class ScaleRulerView extends View {

    final static String TAG = ScaleRulerView.class.getSimpleName();

    /**
     * 渐变色起始色
     */
    @ColorInt
    int startColor = Color.parseColor("#ff3415b0");

    /**
     * 渐变色结束色
     */
    @ColorInt
    int endColor = Color.parseColor("#ffcd0074");

    /**
     * 指示器颜色
     */
    @ColorInt
    int indicatorColor = startColor;

    /**
     * 控件宽高
     */
    int width, height;

    /**
     * 控件绘制画笔
     */
    Paint paint;

    /**
     * 文字画笔
     */
    Paint textPaint;

    /**
     * 线条宽度，默认12px
     */
    int lineWidth = 24;

    /**
     * 长、中、短刻度的高度
     */
    int maxLineHeight, midLineHeight, minLineHeight;

    /**
     * 指示器半径
     */
    int indicatorRadius = lineWidth / 2;

    /**
     * 刻度尺的开始、结束数字
     */
    int startNum = 0, endNum = 40;

    /**
     * 每个刻度代表的数字单位
     */
    int unitNum = 1;

    /**
     * 刻度间隔
     */
    int lineSpacing = 3 * lineWidth;

    /**
     * 第一刻度位置距离当前位置的偏移量，一定小于0
     */
    float offsetStart = 0;

    /**
     * 辅助计算滑动，主要用于惯性计算
     */
    Scroller scroller;

    /**
     * 跟踪用户手指滑动速度
     */
    VelocityTracker velocityTracker;

    /**
     * 定义惯性作用的最小速度
     */
    float minVelocityX;

    /**
     * 刻度文字大小
     */
    int textSize = 96;

    /**
     * 文字高度
     */
    float textHeight;

    /**
     * 当前选中数字
     */
    int curSelectNum;

    OnNumSelectListener listener;

    public interface OnNumSelectListener {
        void onNumSelect(int selectedNum);
    }

    public void setOnNumSelectListener(OnNumSelectListener listener) {
        this.listener = listener;
    }


    public ScaleRulerView(Context context) {
        this(context, null);
    }

    public ScaleRulerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    void initView(Context context, AttributeSet attrs) {
        paint = new Paint();
        paint.setColor(startColor);
        paint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(startColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        textHeight = fontMetrics.descent - fontMetrics.ascent;


        scroller = new Scroller(context);

        velocityTracker = VelocityTracker.obtain();
        minVelocityX = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制刻度
        for (int i = 0; i < ((endNum - startNum) / unitNum) + 1; i++) {
            int lineHeight = minLineHeight;
            if (i % 10 == 0) {
                lineHeight = maxLineHeight;
            } else if (i % 5 == 0) {
                lineHeight = midLineHeight;
            }
            float lineLeft = offsetStart + movedX + (width / 2) - (lineWidth / 2) + (i * lineSpacing);
            float lineRight = lineLeft + lineWidth;
            RectF rectF = new RectF(lineLeft, 4 * indicatorRadius, lineRight, lineHeight);
            paint.setColor(ColorsUtil.getColor(startColor, endColor, (float) i / (float) ((endNum - startNum) / unitNum)));
            canvas.drawRoundRect(rectF, lineWidth / 2, lineWidth / 2, paint);

            //绘制刻度文字
            if (i % 10 == 0) {
                textPaint.setColor(ColorsUtil.getColor(startColor, endColor, (float) i / (float) ((endNum - startNum) / unitNum)));
                canvas.drawText(i + "", lineLeft + lineWidth / 2 - textPaint.measureText("" + i) / 2, lineHeight + 20 + textHeight, textPaint);
            }
        }

        //draw indicator
        int indicatorX = width / 2;
        int indicatorY = indicatorRadius;
        indicatorColor = ColorsUtil.getColor(startColor, endColor, Math.abs((float) (offsetStart + movedX) / (float) (lineSpacing * ((endNum - startNum) / unitNum))));
        paint.setColor(indicatorColor);
        canvas.drawCircle(indicatorX, indicatorY, indicatorRadius, paint);

//        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //layout之后获取控件实际宽高
        width = w;
        height = h;
        //最长刻度线的长度默认为控件总高度的2/3
        maxLineHeight = height * 2 / 3;
        midLineHeight = maxLineHeight * 4 / 5;
        minLineHeight = maxLineHeight * 3 / 5;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    int getSelectedNum() {
        return (int) (Math.abs((offsetStart + movedX)) / lineSpacing);
    }

    public int getIndicatorColor() {
        return indicatorColor;
    }

    /**
     * 用户手指按下控件滑动时的初始位置坐标
     */
    float downX;

    /**
     * 当前手指移动的距离
     */
    float movedX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        velocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scroller.forceFinished(true);
                downX = event.getX();
                movedX = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                movedX = event.getX() - downX;
                Log.i(TAG, "offsetStart==>" + offsetStart);
                Log.i(TAG, "movedX==>" + movedX);
                Log.i(TAG, "offsetStart + movedX==>" + (offsetStart + movedX));
                //边界控制
                if (offsetStart + movedX > 0) {
                    movedX = 0;
                    offsetStart = 0;
                } else if (offsetStart + movedX < -((endNum - startNum) / unitNum) * lineSpacing) {
                    offsetStart = -((endNum - startNum) / unitNum) * lineSpacing;
                    movedX = 0;
                }
                if (listener != null) {
                    Log.i(TAG, "getSelectedNum()==>" + getSelectedNum());
                    listener.onNumSelect(getSelectedNum());
                }
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (offsetStart + movedX <= 0 && offsetStart + movedX >= -((endNum - startNum) / unitNum) * lineSpacing) {
                    //手指松开时需要磁吸效果
                    offsetStart = offsetStart + movedX;
                    movedX = 0;
                    offsetStart = ((int) (offsetStart / lineSpacing)) * lineSpacing;
                } else if (offsetStart + movedX > 0) {
                    movedX = 0;
                    offsetStart = 0;
                } else {
                    offsetStart = -((endNum - startNum) / unitNum) * lineSpacing;
                    movedX = 0;
                }
                if (listener != null) {
                    listener.onNumSelect(getSelectedNum());
                }
                //计算当前手指放开时的滑动速率
                velocityTracker.computeCurrentVelocity(500);
                float velocityX = velocityTracker.getXVelocity();
                if (Math.abs(velocityX) > minVelocityX) {
                    scroller.fling(0, 0, (int) velocityX, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
                }
                postInvalidate();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            if (scroller.getCurrX() == scroller.getFinalX()) {
                if (offsetStart + movedX <= 0 && offsetStart + movedX >= -((endNum - startNum) / unitNum) * lineSpacing) {
                    //手指松开时需要磁吸效果
                    offsetStart = offsetStart + movedX;
                    movedX = 0;
                    offsetStart = ((int) (offsetStart / lineSpacing)) * lineSpacing;
                } else if (offsetStart + movedX > 0) {
                    movedX = 0;
                    offsetStart = 0;
                } else {
                    offsetStart = -((endNum - startNum) / unitNum) * lineSpacing;
                    movedX = 0;
                }
            } else {
                //继续惯性滑动
                movedX = scroller.getCurrX() - scroller.getStartX();
                //滑动结束:边界控制
                if (offsetStart + movedX > 0) {
                    movedX = 0;
                    offsetStart = 0;
                    scroller.forceFinished(true);
                } else if (offsetStart + movedX < -((endNum - startNum) / unitNum) * lineSpacing) {
                    offsetStart = -((endNum - startNum) / unitNum) * lineSpacing;
                    movedX = 0;
                    scroller.forceFinished(true);
                }
            }
        } else {
            if (offsetStart + movedX >= 0) {
                offsetStart = 0;
                movedX = 0;
            }
        }
        if (listener != null) {
            listener.onNumSelect(getSelectedNum());
        }
        postInvalidate();
    }
}
