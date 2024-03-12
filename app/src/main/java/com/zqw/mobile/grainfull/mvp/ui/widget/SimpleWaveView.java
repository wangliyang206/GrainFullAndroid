package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Michael by Administrator
 * @date 2022/9/16 16:56
 * @Description 一个简单的水波纹扩散
 */
public class SimpleWaveView extends View {
    public boolean isClear;
    /**
     * 动画时间
     */
    private int animationTime = 5000;
    /**
     * 初始百分比半径
     */
    private float initRadiusPercent = 0.0f;
    /**
     * 中心点
     */
    private Point center = new Point();
    /**
     * 队列
     */
    private List<SimpleWaveViewBean> list = new ArrayList<>();
    /**
     * 最佳半径
     */
    private int radius;
    /**
     * 画笔
     */
    private Paint paint = new Paint();
    {
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#e9302d"));
    }

    public SimpleWaveView(Context context) {
        this(context, null);
    }

    public SimpleWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        list.add(new SimpleWaveViewBean(initRadiusPercent));
        list.add(new SimpleWaveViewBean(initRadiusPercent));
        list.add(new SimpleWaveViewBean(initRadiusPercent));
        list.add(new SimpleWaveViewBean(initRadiusPercent));
        for (int i = 0; i < list.size(); i++) {
            final int finalI = i;
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (finalI < list.size()) {
                        list.get(finalI).start();
                    }
                }
            }, i * (animationTime / 4));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        center.set(MeasureSpec.getSize(widthMeasureSpec) / 2, MeasureSpec.getSize(heightMeasureSpec) / 2);
        radius = Math.min(MeasureSpec.getSize(widthMeasureSpec) / 2, MeasureSpec.getSize(heightMeasureSpec) / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isClear) {
            paint.setColor(Color.parseColor("#e9302d"));
            canvas.drawCircle(center.x, center.y, initRadiusPercent * radius, paint);
            for (int i = 0; i < list.size(); i++) {
                int alpha = (int) ((int) (255 * (1f - list.get(i).percent)) * 0.4);
                paint.setColor(Color.argb(alpha, 233, 50, 50));
                canvas.drawCircle(center.x, center.y, (list.get(i).percent) * radius, paint);
            }
            invalidate();
        }
    }

    public void clear() {

        isClear = true;
        for (SimpleWaveViewBean bean : list) {
            bean.clear();
        }
        list.clear();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clear();
    }

    public class SimpleWaveViewBean {
        public float initRadiusPercent, percent;

        private ValueAnimator valueAnimator;


        public SimpleWaveViewBean(float percent) {
            this.initRadiusPercent = percent;
            this.percent = percent;
        }

        public void start() {
            valueAnimator = ValueAnimator.ofFloat(initRadiusPercent, 1f);
            valueAnimator.setDuration(animationTime);
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                percent = value;
            });
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.start();
        }

        public void clear() {
            if (valueAnimator != null) {
                valueAnimator.cancel();
                valueAnimator.removeAllListeners();
                valueAnimator.removeAllUpdateListeners();
            }
        }
    }
}

