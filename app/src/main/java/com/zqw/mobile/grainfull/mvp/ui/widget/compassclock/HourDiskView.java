package com.zqw.mobile.grainfull.mvp.ui.widget.compassclock;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import com.jess.arms.utils.ArmsUtils;

import timber.log.Timber;

/**
 * 罗盘时钟 - 小时
 */
public class HourDiskView extends DiskView {
    private static final String TAG = "HourDiskView";
    Paint mPaint;
    // 当前指示的小时数
    int hour = 1;

    // 圆盘颜色
    int diskColor = 0xff262d6a;

    // 数字颜色
    int numColor = 0xffffffff;

    // 数字选中颜色
//    int selectNumColor = 0xfff43e3e;
    int selectNumColor = 0xfff0ff00;

    public HourDiskView(Context context) {
        this(context, null);
    }

    public HourDiskView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public HourDiskView(Context context, int radius) {
        super(context);
        mRadius = radius;
        initView(context);
    }

    void initView(Context context) {
        if (mRadius == 0) {
            mRadius = 450;
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(diskColor);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(ArmsUtils.sp2px(mContext, 20));//默认20sp
    }

    private void initView(Context context, AttributeSet attrs) {
        if (mRadius == 0) {
            mRadius = 450;
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(diskColor);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(ArmsUtils.sp2px(mContext, 20));//默认20sp
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画时钟的圆盘
        mPaint.setColor(diskColor);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        //画数字
        mPaint.setColor(numColor);
        Rect bounds = new Rect();
        for (int i = 1; i <= 12; i++) {
            mPaint.getTextBounds(i + "", 0, (i + "").length(), bounds);
            if (i == hour) {
                mPaint.setColor(selectNumColor);
            } else {
                mPaint.setColor(numColor);
            }
            canvas.drawText(i + "", mRadius - bounds.width() / 2, mRadius * 2 - bounds.height(), mPaint);
            canvas.rotate(-30, mRadius, mRadius);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                if (isNeedReturn == false) {
                    Timber.i(TAG + " degree + curDegree - startDegree==>" + (degree + curDegree - startDegree));
                    int tmpDegree = degree + curDegree - startDegree;
                    int tmpD = tmpDegree % 30;
                    int detaD = tmpDegree - tmpD;
                    if (detaD < 0) {
                        detaD = 360 + detaD;
                    }
                    if (tmpD == 0) {
                        hour = detaD / 30 + 1;
                        if (hour > 12) {
                            hour -= 12;
                        }
                    } else if (tmpD < 15) {
                        hour = detaD / 30 + 1;
                        if (hour > 12) {
                            hour -= 12;
                        }
                    } else {
                        hour = detaD / 30 + 2;
                        if (hour > 12) {
                            hour -= 12;
                        }
                    }
                    if (onHourChangedListener != null) {
                        onHourChangedListener.onHourChanged(hour);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedReturn == false) {
                    //做定位矫正,定位到最近的数字
                    int tmp = degree % 30;
                    Timber.i(TAG + "degree==>" + degree);
                    Timber.i(TAG + "tmp==>" + tmp);
                    int detaDD = degree - tmp;
                    if (detaDD < 0) {
                        detaDD = 360 + detaDD;
                    }
                    if (tmp == 0) {
                        hour = detaDD / 30 + 1;
                        if (hour > 12) {
                            hour -= 12;
                        }
                        Timber.i(TAG + "hour==>" + hour);
                    } else if (tmp < 15) {
                        hour = detaDD / 30 + 1;
                        if (hour > 12) {
                            hour -= 12;
                        }
                        Timber.i(TAG + "hour==>" + hour);
                        animator = ValueAnimator.ofInt(degree, degree - tmp);
                        animator.setDuration(100);
                        animator.addUpdateListener(animation -> {
                            degree = (int) animation.getAnimatedValue();
                            postInvalidate();
                        });
                        animator.start();
                    } else {
                        hour = detaDD / 30 + 2;
                        if (hour > 12) {
                            hour -= 12;
                        }
                        Timber.i(TAG + "hour==>" + hour);
                        animator = ValueAnimator.ofInt(degree, degree - tmp + 30);
                        animator.setDuration(100);
                        animator.addUpdateListener(animation -> {
                            degree = (int) animation.getAnimatedValue();
                            postInvalidate();
                        });
                        animator.start();
                    }
                    if (onHourChangedListener != null) {
                        onHourChangedListener.onHourChanged(hour);
                    }
                }

                break;
        }
        if (Math.sqrt(
                (startX - mRadius) * (startX - mRadius) + (startY - mRadius) * (startY - mRadius)
        ) > mRadius) {
            return false;
        }
        return true;
    }

    /**
     * 显示当前的小时，12小时制
     */
    public void setCurTime(int hour) {
        //换算成对应的角度
        this.hour = hour;
        int tmpDegree = 30 * (hour - 1);
        animator = ValueAnimator.ofInt(degree, tmpDegree);
        if (Math.abs(tmpDegree - degree) / 6 <= 5) {
            animator.setDuration(80 * Math.abs(tmpDegree - degree) / 6);
        } else {
            animator.setDuration(500);
        }
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            degree = (int) animation.getAnimatedValue();
            postInvalidate();
        });
        animator.start();
    }

    OnHourChangedListener onHourChangedListener;

    public void setOnHourChangedListener(OnHourChangedListener listener) {
        this.onHourChangedListener = listener;
    }

    public interface OnHourChangedListener {
        void onHourChanged(int hour);
    }
}
