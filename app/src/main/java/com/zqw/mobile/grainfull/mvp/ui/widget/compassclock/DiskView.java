package com.zqw.mobile.grainfull.mvp.ui.widget.compassclock;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

/**
 * 罗盘视图
 */

public class DiskView extends View {
    private static final String TAG = "DiskView";
    Context mContext;
    /**
     * 圆盘半径
     */
    int mRadius = 0;
    /**
     * 手指第一次按下时的坐标
     */
    float startX, startY;
    /**
     * 当前手指按下点的坐标
     */
    float curX, curY;
    /**
     * 第一次手指按下的点与初始位置形成的夹角
     */
    int startDegree;
    /**
     * 手指按下的点与初始位置形成的夹角
     */
    int curDegree;
    /**
     * 圆盘当前位置相对初始位置的角度，初始位置角度为0度
     */
    int degree = 0;
    /**
     * 手指抬起后是否需要回归原来的状态
     */
    boolean isNeedReturn = true;

    ValueAnimator animator;

    public DiskView(Context context) {
        this(context, null);
    }

    public DiskView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.rotate(degree + curDegree - startDegree, mRadius, mRadius);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        curX = event.getX();
        curY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                startDegree = computeCurrentAngle(curX, curY);
                //起始落点不能超过圆盘界限
                if (Math.sqrt(
                        (startX - mRadius) * (startX - mRadius) + (startY - mRadius) * (startY - mRadius)
                ) > mRadius) {
                    startDegree = 0;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //起始落点不能超过圆盘界限
                if (Math.sqrt(
                        (startX - mRadius) * (startX - mRadius) + (startY - mRadius) * (startY - mRadius)
                ) > mRadius) {
                    return false;
                }
                curDegree = computeCurrentAngle(curX, curY);
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (Math.sqrt(
                        (startX - mRadius) * (startX - mRadius) + (startY - mRadius) * (startY - mRadius)
                ) > mRadius) {
                    return false;
                }
                int tmpDegree = degree;//手指按下前的圆盘角度
                degree = degree + curDegree - startDegree;
                if (Math.abs(degree) > 360) {
                    degree %= 360;
                }
                startDegree = 0;
                curDegree = 0;
                startX = 0;
                startY = 0;
                //是否需要回位
                if (isNeedReturn) {
                    animator = ValueAnimator.ofInt(degree, tmpDegree);
                    animator.addUpdateListener(animation -> {
                        degree = (int) animation.getAnimatedValue();
                        postInvalidate();
                    });
                    animator.setDuration(200);
                    animator.setInterpolator(new DecelerateInterpolator());
                    animator.start();
                }
                break;
        }
        return true;
    }

    /**
     * 计算某点的角度
     *
     * @param x
     * @param y
     * @return
     */
    private int computeCurrentAngle(float x, float y) {
        float distance = (float) Math
                .sqrt(((x - mRadius) * (x - mRadius) + (y - mRadius)
                        * (y - mRadius)));
        int degree = (int) (Math.acos((x - mRadius) / distance) * 180 / Math.PI);
        if (y < mRadius) {
            degree = -degree;
        }
        return degree;
    }

    public int getmRadius() {
        return mRadius;
    }

    public void setmRadius(int mRadius) {
        this.mRadius = mRadius;
    }
}
