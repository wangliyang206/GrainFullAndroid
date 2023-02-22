package com.zqw.mobile.grainfull.mvp.ui.widget.compassclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.jess.arms.utils.ArmsUtils;

/**
 * 罗盘时钟 - 中心 - 上午/下午
 */

public class AmPmDiskView extends DiskView {
    Paint mPaint;
    /**
     * 显示在圆盘中心的文字内容
     */
    String str;

    public AmPmDiskView(Context context) {
        this(context, null);
    }

    public AmPmDiskView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AmPmDiskView(Context context, int radius) {
        super(context);
        mRadius = radius;
        initView(context);
    }

    private void initView(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xff1a1f4a);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(ArmsUtils.sp2px(mContext, 24));//默认36sp
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画圆盘
        mPaint.setColor(0xff1a1f4a);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);

        //画指示文字
        if (str != null && !str.equals("")) {
            mPaint.setColor(Color.WHITE);
            Rect bounds = new Rect();
            mPaint.getTextBounds(str, 0, str.length(), bounds);
            canvas.drawText(str, mRadius - bounds.width() / 2, mRadius + bounds.height() / 2, mPaint);
        } else {
            mPaint.setColor(Color.WHITE);
            Rect bounds = new Rect();
            mPaint.getTextBounds("AM", 0, "AM".length(), bounds);
            canvas.drawText("AM", mRadius - bounds.width() / 2, mRadius + bounds.height() / 2, mPaint);
        }
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                if (onAPMChangedListener != null) {
                    if (str != null && str.equals("AM")) {
                        str = "PM";
                    } else {
                        str = "AM";
                    }
                    onAPMChangedListener.onAPMChanged(str);
                }
                postInvalidate();
                break;
        }
        if (Math.sqrt(
                (startX - mRadius) * (startX - mRadius) + (startY - mRadius) * (startY - mRadius)
        ) > mRadius) {
            return false;
        }
        return true;
    }

    OnAPMChangedListener onAPMChangedListener;

    public void setOnAPMChangedListener(OnAPMChangedListener listener) {
        this.onAPMChangedListener = listener;
    }

    interface OnAPMChangedListener {
        void onAPMChanged(String str);
    }
}
