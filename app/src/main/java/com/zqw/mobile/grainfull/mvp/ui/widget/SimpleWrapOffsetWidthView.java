package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.ConvertUtils;

/**
 * 简单环绕偏移宽度视图
 */
public class SimpleWrapOffsetWidthView extends FrameLayout {
    private int width = 0;
    /**
     * 溃缩
     */
    public static final int STATE_COLLAPSED = 1;
    /**
     * 扩张
     */
    public static final int STATE_EXPANDED = 0;
    /**
     * 当前状态
     */
    private int currentState = STATE_EXPANDED;
    /**
     * 动画
     */
    private ValueAnimator valueAnimator;
    /**
     * 阈值
     */
    private int threshold = ConvertUtils.dp2px(200);

    private OnSimpleWrapOffsetWidthViewCallBack onSimpleWrapOffsetWidthViewCallBack;

    public void setOnSimpleWrapOffsetWidthViewCallBack(OnSimpleWrapOffsetWidthViewCallBack onSimpleWrapOffsetWidthViewCallBack) {
        this.onSimpleWrapOffsetWidthViewCallBack = onSimpleWrapOffsetWidthViewCallBack;
    }

    public SimpleWrapOffsetWidthView(Context context) {
        this(context, null);
    }

    public SimpleWrapOffsetWidthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleWrapOffsetWidthView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SimpleWrapOffsetWidthView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() != 1) {
            throw new RuntimeException("you must add one child view!");
        }
        if (width == 0) {
            width = getChildAt(0).getMeasuredWidth();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clear();
    }

    /**
     * 开始动画
     *
     * @param state 状态 {@link #STATE_COLLAPSED} 或 {@link #STATE_EXPANDED}
     */
    public void start(int state) {
        start(state, threshold);
    }

    public void start(final int state, int threshold) {
        if (currentState == state) {
            return;
        }
        if (valueAnimator != null && valueAnimator.isRunning()) {
            return;
        }
        this.threshold = threshold;
        clear();
        if (state == STATE_COLLAPSED) {
            valueAnimator = ValueAnimator.ofInt(width, threshold);
        } else {
            valueAnimator = ValueAnimator.ofInt(threshold, width);
        }

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (onSimpleWrapOffsetWidthViewCallBack != null) {
                    onSimpleWrapOffsetWidthViewCallBack.onStatusChangeStart(currentState, getChildAt(0).getLeft(), getChildAt(0).getTop(), getChildAt(0).getRight(), getChildAt(0).getBottom());
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                currentState = state;
                if (onSimpleWrapOffsetWidthViewCallBack != null) {
                    onSimpleWrapOffsetWidthViewCallBack.onStatusChangedComplete(currentState, getChildAt(0).getLeft(), getChildAt(0).getTop(), getChildAt(0).getRight(), getChildAt(0).getBottom());
                }
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                LayoutParams layoutParams = (LayoutParams) getChildAt(0).getLayoutParams();
                layoutParams.width = value;
                getChildAt(0).setLayoutParams(layoutParams);
//                getChildAt(0).layout(value, getChildAt(0).getTop(), getChildAt(0).getRight(), getChildAt(0).getBottom());
                if (onSimpleWrapOffsetWidthViewCallBack != null) {
                    onSimpleWrapOffsetWidthViewCallBack.onStatusChanging(currentState, getChildAt(0).getLeft(), getChildAt(0).getTop(), getChildAt(0).getRight(), getChildAt(0).getBottom());
                }

            }
        });
        valueAnimator.setDuration(150);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.start();
    }

    private void clear() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.removeAllListeners();
        }
    }

    public interface OnSimpleWrapOffsetWidthViewCallBack {
        /**
         * 状态更改准备开始更改回调
         *
         * @param state       状态 {@link #STATE_COLLAPSED} 或 {@link #STATE_EXPANDED}
         * @param childLeft   子布局左边
         * @param childTop    子布局顶边
         * @param childRight  子布局右边
         * @param childBottom 子布局底边
         */
        void onStatusChangeStart(int state, int childLeft, int childTop, int childRight, int childBottom);

        /**
         * 状态更改准备开始更改回调
         *
         * @param state       状态 {@link #STATE_COLLAPSED} 或 {@link #STATE_EXPANDED}
         * @param childLeft   子布局左边
         * @param childTop    子布局顶边
         * @param childRight  子布局右边
         * @param childBottom 子布局底边
         */
        void onStatusChanging(int state, int childLeft, int childTop, int childRight, int childBottom);

        /**
         * 状态更改完成后回调
         *
         * @param state       状态 {@link #STATE_COLLAPSED} 或 {@link #STATE_EXPANDED}
         * @param childLeft   子布局左边
         * @param childTop    子布局顶边
         * @param childRight  子布局右边
         * @param childBottom 子布局底边
         */
        void onStatusChangedComplete(int state, int childLeft, int childTop, int childRight, int childBottom);
    }
}