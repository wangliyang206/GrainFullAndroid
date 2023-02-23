package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

/**
 * 层级选择控件
 */
public class LevelSelectView extends View {

    public static final String TAG = LevelSelectView.class.getSimpleName();

    /**
     * 控件宽高
     */
    int width, height;

    /**
     * 指示器半径
     */
    float indicatorRadius = 30;

    /**
     * 初始指示器半径
     */
    float indicatorInitRadius = 30;

    /**
     * 指示器圆心坐标
     */
    float indicatorX, indicatorY;

    /**
     * 指示器较上一位置的偏移距离
     */
    float indicatorOffset = 0;

    /**
     * 指示器颜色
     */
    @ColorInt
    int indicatorColor = Color.WHITE;

    /**
     * 线段颜色
     */
    @ColorInt
    int lineSegmentColor = Color.BLACK;

    /**
     * 指示器左边的线条渐变色的开始颜色
     */
    int leftLineStartColor = 0xff7FFFD4;

    /**
     * 指示器左边的线条渐变色的结束颜色
     */
    int leftLineEndColor = 0xff008080;

    /**
     * 阴影颜色
     */
    @ColorInt
    int shadowColor = 0xffe0e0e0;

    /**
     * 指示器中心颜色
     */
    @ColorInt
    int indicatorDotColor = 0xff008080;

    /**
     * 线段点颜色
     */
    @ColorInt
    int dotColor = Color.GRAY;

    /**
     * 线段点半径
     */
    float dotRadius = 10;

    /**
     * 指示器移动动画、缩放动画
     */
    ValueAnimator indicatorTransAnimator, indicatorScaleAnimator;

    /**
     * 分级段数：即线段个数
     */
    int levelCount = 3;

    /**
     * 线段线条粗细
     */
    int lineStrokeWidth = 4;

    /**
     * 线条背景高度
     */
    int lineBgHeight;

    /**
     * 每条线段的长度
     */
    float lineSegmentWidth;

    Paint indicatorPaint;

    Paint linePaint;

    Paint dotPaint;
    // 当前选中的item
    private int indexSegmentCount;

    public LevelSelectView(Context context) {
        this(context, null);
    }

    public LevelSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    void initView(Context context, AttributeSet attrs) {
        linePaint = new Paint();
        linePaint.setColor(lineSegmentColor);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(lineStrokeWidth);
        linePaint.setAntiAlias(true);

        indicatorPaint = new Paint();
        indicatorPaint.setStyle(Paint.Style.FILL);
        indicatorPaint.setColor(indicatorColor);
        indicatorPaint.setAntiAlias(true);

        dotPaint = new Paint();
        dotPaint.setStyle(Paint.Style.FILL);
        dotPaint.setColor(dotColor);
        dotPaint.setAntiAlias(true);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;

        indicatorInitRadius = height / 6;
        indicatorRadius = indicatorInitRadius;
        dotRadius = indicatorRadius / 6;

        indicatorX = 2 * indicatorInitRadius + indicatorOffset;
        indicatorY = height / 2;

        lineSegmentWidth = (width - 4 * indicatorInitRadius) / levelCount;

        lineBgHeight = (int) indicatorInitRadius;
    }

    /**
     * 画背景线条
     */
    void drawLineBg(Canvas canvas) {
        canvas.save();

        RectF rectF = new RectF(2 * indicatorInitRadius - lineBgHeight / 2, indicatorY - lineBgHeight / 2, width - 2 * indicatorInitRadius + lineBgHeight / 2, indicatorY + lineBgHeight / 2);
        //设置内阴影
        Path path = new Path();
        path.addRoundRect(rectF, lineBgHeight / 2, lineBgHeight / 2, Path.Direction.CW);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.clearShadowLayer();
        linePaint.setColor(0x88e0e0e0);
        canvas.drawPath(path, linePaint);

        //绘制指示器左边的线段背景
        drawIndicatorLeftLineBg(canvas);

        linePaint.setShadowLayer(3, 0, 2, shadowColor);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(0xffe0e0e0);
        canvas.clipPath(path);
        canvas.drawPath(path, linePaint);

        canvas.restore();
    }

    /**
     * 画指示器
     */
    void drawIndicator(Canvas canvas) {
        indicatorPaint.setColor(Color.WHITE);
        indicatorPaint.setStyle(Paint.Style.FILL);
        indicatorPaint.setShadowLayer(3, 0, 2, Color.GRAY);
        canvas.drawCircle(indicatorX + indicatorOffset, indicatorY, indicatorRadius, indicatorPaint);

        indicatorPaint.setColor(indicatorDotColor);
        indicatorPaint.setStyle(Paint.Style.FILL);
        indicatorPaint.clearShadowLayer();
        canvas.drawCircle(indicatorX + indicatorOffset, indicatorY, indicatorRadius / 2, indicatorPaint);

        //绘制内阴影
        canvas.save();

        Path path = new Path();
        path.addCircle(indicatorX + indicatorOffset, indicatorY, indicatorRadius / 2, Path.Direction.CW);
        indicatorPaint.setColor(Color.WHITE);
        indicatorPaint.setShadowLayer(2, 0, 2, Color.GRAY);
        indicatorPaint.setStyle(Paint.Style.STROKE);

        canvas.clipPath(path);
        canvas.drawPath(path, indicatorPaint);

        canvas.restore();
    }

    void drawIndicatorLeftLineBg(Canvas canvas) {
        LinearGradient linearGradient = new LinearGradient(0, 0, width, 0, leftLineStartColor, leftLineEndColor, Shader.TileMode.CLAMP);
        linePaint.setShader(linearGradient);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.clearShadowLayer();


        Path path = new Path();
        RectF rectF = new RectF(2 * indicatorInitRadius - lineBgHeight / 2, indicatorY - lineBgHeight / 2, indicatorX + indicatorOffset + lineBgHeight / 2, indicatorY + lineBgHeight / 2);
        path.addRoundRect(rectF, lineBgHeight / 2, lineBgHeight / 2, Path.Direction.CW);
        canvas.drawPath(path, linePaint);

        linePaint.clearShadowLayer();
        linePaint.setShader(null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        //画线段点、线段
        for (int i = 0; i < levelCount + 1; i++) {
            canvas.drawCircle(lineSegmentWidth * i + 2 * indicatorInitRadius, height / 2, dotRadius, dotPaint);
        }

        //画背景线条
        drawLineBg(canvas);


        //画指示器
        drawIndicator(canvas);

    }

    /**
     * 扩大指示器动画
     */
    void scaleOutIndicator() {
        if (indicatorScaleAnimator != null) {
            indicatorScaleAnimator.cancel();
        }
        indicatorScaleAnimator = ValueAnimator.ofFloat(indicatorInitRadius, indicatorInitRadius * 2);
        indicatorScaleAnimator.setDuration(150L);
        indicatorScaleAnimator.addUpdateListener(animation -> {
            indicatorRadius = (float) animation.getAnimatedValue();
            postInvalidate();
        });
        indicatorScaleAnimator.start();
    }

    /**
     * 缩小指示器动画
     */
    void scaleInIndicator() {
        if (indicatorScaleAnimator != null) {
            indicatorScaleAnimator.cancel();
        }
        indicatorScaleAnimator = ValueAnimator.ofFloat(indicatorRadius, indicatorInitRadius);
        indicatorScaleAnimator.setDuration(300L);
        indicatorScaleAnimator.addUpdateListener(animation -> {
            indicatorRadius = (float) animation.getAnimatedValue();
            postInvalidate();
        });
        indicatorScaleAnimator.start();
    }

    /**
     * 用户交互时手指按下的点的XY坐标
     */
    float downX, downY;

    /**
     * 是否拖拽
     */
    boolean isDrag = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                isDrag = false;
                //放手后的磁吸效果
                //移动到下一个最近节点
                if (indicatorTransAnimator != null) {
                    indicatorTransAnimator.cancel();
                }
                //移动的线段数
                int overLineSegmentCount = (int) Math.abs((indicatorX + indicatorOffset) / lineSegmentWidth);
                //要移动的目标线段所在的线段数，从0开始
                indexSegmentCount = overLineSegmentCount;
                if (Math.abs((indicatorX + indicatorOffset) % lineSegmentWidth) > lineSegmentWidth / 2) {
                    //移动到下一个临近节点
                    indexSegmentCount = overLineSegmentCount + 1;
                } else {
                    //回滚至上一个临近节点
                    indexSegmentCount = overLineSegmentCount;
                }
                indicatorTransAnimator = ValueAnimator.ofFloat(indicatorX + indicatorOffset, indexSegmentCount * lineSegmentWidth + 2 * indicatorInitRadius);
                indicatorTransAnimator.setDuration(150L);
                indicatorTransAnimator.addUpdateListener(animation -> {
                    indicatorX = (float) animation.getAnimatedValue();
                    postInvalidate();
                });
                indicatorTransAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //开始缩放动画
                        scaleInIndicator();
                    }
                });
                indicatorX = indicatorX + indicatorOffset;
                indicatorOffset = 0;
                downX = 0;
                downY = 0;
                indicatorTransAnimator.start();
                if(mListener != null){
                    mListener.onChange(indexSegmentCount);
                }
                Log.i(TAG, "indexSegmentCount==>" + indexSegmentCount);
                break;
            case MotionEvent.ACTION_DOWN:
                //如果在指示器点击范围内
                downX = event.getX();
                downY = event.getY();
                Log.i(TAG, "downX==>" + downX + ";downY==>" + downY);
                if (Math.abs(downX - indicatorX) < indicatorInitRadius) {
                    isDrag = true;
                    //开始缩放动画
                    scaleOutIndicator();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isDrag) {
                    break;
                }
                indicatorOffset = event.getX() - downX;
                if (indicatorX + indicatorOffset <= 2 * indicatorInitRadius) {
                    indicatorOffset = 2 * indicatorInitRadius - indicatorX;
                } else if (indicatorX + indicatorOffset >= width - 2 * indicatorInitRadius) {
                    indicatorOffset = width - 2 * indicatorInitRadius - indicatorX;
                }
                postInvalidate();
                break;
        }
        return true;
    }

    private onChangeListener mListener;

    public interface onChangeListener {
        void onChange(int index);
    }

    public void setOnChangeListener(onChangeListener listener) {
        this.mListener = listener;
    }
}
