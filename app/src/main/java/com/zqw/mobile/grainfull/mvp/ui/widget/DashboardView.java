package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.widget
 * @ClassName: DashboardView
 * @Description: 仪表盘
 * 注意:因为是在固定开发板用,没怎么考虑适配情况.
 * 1.表盘大小是根据控件布局文件的width决定
 * 2.控件宽度太小可能刻度文字显示不全,可以看情况调整下文字大小.
 * @Author: WLY
 * @CreateDate: 2023/2/20 17:40
 */
public class DashboardView extends View {
    private static final String TAG = "DashboardView";
    private Paint arcPaint;
    //圆环的角度
    private int SWEEPANGLE = 280;
    //刻度画笔
    private Paint pointerPaint;

    //圆环半径
    private int mRadius;
    //圆环的宽度
    int arcW = 10;
    //发光的宽度
    int gleamyArcW = arcW * 3;
    //刻度宽度
    int minScalew = 5;
    int maxScalew = 5;
    //刻度的长度
    int maxScaleLength = 50;
    int minScaleLength = 30;
    private Paint gleamyArcPaint;
    //发光圆环的半径
    private int mRadiusG;
    //阴影宽度
    private int shade_w = 40;

    private Path pointerPath;

    private float currentDegree = 0;
    //指针当前的角度.
    private int startAngele = 90 + (360 - SWEEPANGLE) / 2;
    //仪表盘显示的数字
    private String speed = "0";
    private Paint mTextPaint;
    private Paint mPaint;
    private ValueAnimator mAnim;

    public DashboardView(Context context) {
        super(context);
        init(context);
    }


    public DashboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        // 关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        //外层动态圆环画笔
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);//画线模式
        mPaint.setAntiAlias(true);

        //圆环画笔
        arcPaint = new Paint();
        arcPaint.setStyle(Paint.Style.STROKE);//画线模式
        arcPaint.setStrokeWidth(arcW);//线宽度
        arcPaint.setColor(Color.parseColor("#07A6EC"));
        arcPaint.setAntiAlias(true);
        //刻度
        pointerPaint = new Paint();
        pointerPaint.setAntiAlias(true);
        pointerPaint.setColor(Color.parseColor("#26396F"));
        pointerPaint.setTextSize(40);
        pointerPaint.setTextAlign(Paint.Align.RIGHT);

        pointerPath = new Path();

        //发光圆环
        gleamyArcPaint = new Paint();
        gleamyArcPaint.setAntiAlias(true);
        gleamyArcPaint.setStyle(Paint.Style.STROKE);
        gleamyArcPaint.setStrokeWidth(gleamyArcW);
        //文字
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(60);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mRadius = (int) (getMeasuredWidth() / 2 * 0.8);
        mRadiusG = mRadius - gleamyArcW / 2;
        shade_w = (int) (mRadius * 0.4);
        //Log.i(TAG, "onDraw: mRadius"+mRadius+"mRadiusG:"+mRadiusG+"shade_w:"+shade_w);
        //表盘背景颜色
        canvas.drawColor(Color.parseColor("#040613"));
        //canvas.drawColor(Color.WHITE);
        //Log.i(TAG, "onDraw1: w:"+getMeasuredWidth()+"h:"+getMeasuredHeight());
        //Log.i(TAG, "onDraw2: w:"+getWidth()+"h:"+getHeight());
        //最外层白色动态圆环
        drawDynamicArcs(canvas);
        //圆环
        drawArcs(canvas);
        //发光圆环
        drawGleamyArc(canvas);
        //刻度
        drawDegree(canvas);
        //指针阴影
        drawShade(canvas);
        //黑色圆形背景
        drawCircleBlack(canvas);
        //指针
        drawPointer(canvas);
        //中心圆环
        drawCenterArcs(canvas);
        //中心显示文字
        drawCenterText(canvas);
    }

    private void drawDynamicArcs(Canvas canvas) {
        //半径
        int dyRaduis = (int) (getMeasuredWidth() / 2 * 0.88);
        int w = 10;
        int[] colorSweep = new int[]{Color.parseColor("#00FFFFFF"), Color.parseColor("#FFFFFFFF")};
        float[] position = new float[]{0f, 0.5f};
        SweepGradient mShader = new SweepGradient(getMeasuredWidth() / 2, getMeasuredHeight() / 2, colorSweep, position);

        //旋转渐变
        Matrix matrix = new Matrix();
        matrix.setRotate(startAngele, canvas.getWidth() / 2, canvas.getHeight() / 2);
        mShader.setLocalMatrix(matrix);
        mPaint.setShader(mShader);
        //arcPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(w);
        RectF rectF = new RectF();
        rectF.left = (float) (getMeasuredWidth() / 2 - (dyRaduis - w / 2));
        rectF.top = (float) (getMeasuredHeight() / 2 - (dyRaduis - w / 2));
        rectF.right = (float) (getMeasuredWidth() / 2 + (dyRaduis - w / 2));
        rectF.bottom = (float) (getMeasuredHeight() / 2 + (dyRaduis - w / 2));
        canvas.drawArc(rectF, 90 + (360 - SWEEPANGLE) / 2, currentDegree, false, mPaint);
    }

    private void drawCenterText(Canvas canvas) {
        mTextPaint.reset();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(60);
        mTextPaint.setAntiAlias(true);
        double mm = mRadius * 0.4;
        RectF rect = new RectF();
        rect.left = (float) (getMeasuredWidth() / 2 - mm);
        rect.top = (float) (getMeasuredHeight() / 2 - mm);
        rect.right = (float) (getMeasuredWidth() / 2 + mm);
        rect.bottom = (float) (getMeasuredHeight() / 2 + mm);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        float baseline = rect.centerY() + distance;
        //速度
        canvas.drawText(speed, rect.centerX(), baseline, mTextPaint);


        //canvas.drawText(speed,getMeasuredWidth()/2,getMeasuredHeight()/2,pointerPaint);
        mTextPaint.setTextSize(40);

        //绘制底部文字(向下20px)
        float text_h = Math.abs(fontMetrics.top - fontMetrics.bottom) - 20;
        //Log.i(TAG, "drawCenterText: "+fontMetrics.bottom+"=="+fontMetrics.top+"=="+text_w);
        String info = speed + "Wh/km";
        canvas.drawText(info, getMeasuredWidth() / 2, (float) (getMeasuredHeight() / 2) + (mRadius - text_h), mTextPaint);
        //速度文字下划线
        float text_w = mTextPaint.measureText(info);
        // Log.i(TAG, "drawCenterText:aaaa"+text_w);
        //pointerPaint.setShadowLayer(10, 0, 0, Color.parseColor("#f8b62e"));
        int[] color = {Color.parseColor("#80041B25"), Color.parseColor("#0496C6"), Color.parseColor("#80041B25")};
        float[] position = {0f, 0.5f, 1f};
        mTextPaint.setStrokeWidth(3);
        mTextPaint.setShader(new LinearGradient((float) (getMeasuredWidth() / 2) - text_w / 2, (float) (getMeasuredHeight() / 2) + (mRadius - text_h + 10),
                (float) (getMeasuredWidth() / 2) + text_w / 2, (float) (getMeasuredHeight() / 2) + (mRadius - text_h + 10), color, position
                , Shader.TileMode.MIRROR));
        canvas.drawLine((float) (getMeasuredWidth() / 2) - text_w / 2, (float) (getMeasuredHeight() / 2) + (mRadius - text_h + 10)
                , (float) (getMeasuredWidth() / 2) + text_w / 2, (float) (getMeasuredHeight() / 2) + (mRadius - text_h + 10), mTextPaint);
    }

    private void drawCenterArcs(Canvas canvas) {
        //中心发光圆环
        pointerPaint.setColor(Color.parseColor("#050D3D"));
        pointerPaint.setStyle(Paint.Style.FILL);
        pointerPaint.setShadowLayer(15, 0, 0, Color.parseColor("#006EC6"));
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, (float) (mRadius * 0.4), pointerPaint);

        //内部深色实心圆
        pointerPaint.setColor(Color.parseColor("#040613"));
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2,
                (float) (mRadius * 0.4) - pointerPaint.getStrokeWidth(), pointerPaint);

    }

    private void drawPointer(Canvas canvas) {
        canvas.save();
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        canvas.rotate(startAngele + currentDegree);
        pointerPaint.setColor(Color.WHITE);
        pointerPath.moveTo(mRadius, 0);
        pointerPath.lineTo(0, 0 - 5);
        pointerPath.lineTo(0, 0 + 5);
        pointerPath.close();
        canvas.drawPath(pointerPath, pointerPaint);
        canvas.restore();
    }

    private void drawCircleBlack(Canvas canvas) {
        //Paint pointerPaint = new Paint();
        //pointerPaint.setAntiAlias(true);
        pointerPaint.setStyle(Paint.Style.FILL);
        pointerPaint.setColor(Color.parseColor("#040613"));
        //pointerPaint.setColor(Color.parseColor("#f8b62e"));
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, (float) (mRadius * 0.6), pointerPaint);
    }

    /**
     * 指针阴影
     */
    private void drawShade(Canvas canvas) {
        int[] colorSweep = new int[]{0x66FFE9EC, 0x0328E9EC, 0x1a28E9EC, 0x66FFE9EC};
        float[] position = new float[]{0f, 0.36f, 0.5f, 0.7f};
        SweepGradient mShader = new SweepGradient(getMeasuredWidth() / 2, getMeasuredHeight() / 2, colorSweep, position);
        gleamyArcPaint.setShader(mShader);
        //arcPaint.setStyle(Paint.Style.STROKE);
        gleamyArcPaint.setStrokeWidth(shade_w);
        //arcPaint.clearShadowLayer();
        RectF rectF = new RectF();
        rectF.left = (float) (getMeasuredWidth() / 2 - (mRadiusG - shade_w / 2));
        rectF.top = (float) (getMeasuredHeight() / 2 - (mRadiusG - shade_w / 2));
        rectF.right = (float) (getMeasuredWidth() / 2 + (mRadiusG - shade_w / 2));
        rectF.bottom = (float) (getMeasuredHeight() / 2 + (mRadiusG - shade_w / 2));
        canvas.drawArc(rectF, 90 + (360 - SWEEPANGLE) / 2, currentDegree, false, gleamyArcPaint);
    }

    /**
     * 画发光圆
     */
    private void drawGleamyArc(Canvas canvas) {

        gleamyArcPaint.setStrokeWidth(gleamyArcW);
        int[] a = {Color.parseColor("#000947C3"), Color.parseColor("#ff0947C3")};
        float[] b = {0.9f, 1f};
        RadialGradient radialGradient = new RadialGradient(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mRadius - arcW, a, b, Shader.TileMode.CLAMP);
        //gleamyArcPaint.clearShadowLayer();
        gleamyArcPaint.setShader(radialGradient);

        RectF rectF = new RectF(getMeasuredWidth() / 2 - mRadiusG, getMeasuredHeight() / 2 - mRadiusG,
                getMeasuredWidth() / 2 + mRadiusG, getMeasuredHeight() / 2 + mRadiusG);
        canvas.drawArc(rectF, 90 + (360 - SWEEPANGLE) / 2, SWEEPANGLE, false, gleamyArcPaint);

        //canvas.drawArc();
    }

    // 整数刻度间隔为4；
    // 36个刻度，180数值；
    // 40个刻度，200数值；
    // 44个刻度，220数值；
    int clockPointNum = 40;

    /**
     * 获取仪表盘中最大数值
     * 刻度 * 间隔 = 总数
     */
    public int getDashboardMax() {
        // 间隔 = 每4格间隔一次 + 1(最后一格)；
        // 最大数值 = 刻度 * 间隔；
        return clockPointNum * 5;
    }

    private void drawDegree(Canvas canvas) {
        pointerPaint.setColor(Color.parseColor("#26396F"));
        pointerPaint.setTextSize(40);
        pointerPaint.clearShadowLayer();
        pointerPaint.setTextAlign(Paint.Align.RIGHT);

        canvas.save();
        //原点移到空间中心点
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        //canvas.rotate(30);

        canvas.rotate((360 - SWEEPANGLE) / 2 + 90);//(360-240)/2+90;
        //设置刻度文字颜色大小.
        mTextPaint.reset();
        mTextPaint.setColor(Color.parseColor("#26396F"));
        mTextPaint.setTextSize(40);
        mTextPaint.clearShadowLayer();
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        mTextPaint.setAntiAlias(true);
        for (int i = 0; i < clockPointNum; i++) {
            if (i % 4 == 0) {     //长表针
                pointerPaint.setStrokeWidth(maxScalew);
                canvas.drawLine(mRadiusG - arcW, maxScalew / 2, mRadiusG - maxScaleLength, maxScalew / 2, pointerPaint);
                //Log.i(TAG, "drawDegree: 长刻度I"+i);
                drawPointerText(canvas, mRadiusG - arcW, maxScalew / 2, i);

            } else {    //短表针
                pointerPaint.setStrokeWidth(minScalew);
                canvas.drawLine(mRadiusG - arcW, maxScalew / 2, mRadiusG - minScaleLength, maxScalew / 2, pointerPaint);
            }

            canvas.rotate((float) SWEEPANGLE / (float) clockPointNum);
            // Log.i(TAG, "onDraw: "+i+"---"+(float) SWEEPANGLE/ (float) clockPointNum);
        }
        //最后一根
        canvas.drawLine(mRadiusG - arcW, -maxScalew / 2, mRadiusG - maxScaleLength, -maxScalew / 2, pointerPaint);

        drawPointerText(canvas, mRadiusG - arcW, maxScalew / 2, 36);
        canvas.restore();
        count = 0;
    }

    int count = 0;

    private void drawPointerText(Canvas canvas, int x, int y, int i) {
        //动态设置刻度文字颜色。
        float a = (float) SWEEPANGLE / (float) clockPointNum;
        if (currentDegree > a * i)
            mTextPaint.setColor(Color.WHITE);
        else
            mTextPaint.setColor(Color.parseColor("#26396F"));

        if (i != 0)
            count += 20;
        canvas.drawText(String.valueOf(count), x - maxScaleLength, y, mTextPaint);
    }

    private void drawArcs(Canvas canvas) {
        RectF rectF = new RectF(getMeasuredWidth() / 2 - mRadius, getMeasuredHeight() / 2 - mRadius,
                getMeasuredWidth() / 2 + mRadius, getMeasuredHeight() / 2 + mRadius);
        canvas.drawArc(rectF, 90 + (360 - SWEEPANGLE) / 2, SWEEPANGLE, false, arcPaint);
    }


    /**
     * 外部更新刻度.
     *
     * @param speedssss 速度
     */
    public void udDataSpeed(int speedssss) {
        float a = SWEEPANGLE / 180f;
        if (speedssss < 0) throw new IllegalArgumentException("----speed不能小于0----");
        speed = String.valueOf(speedssss);
        //Log.i(TAG, "udDataSpeed: \ncurrentDegree:"+currentDegree+"\na:"+a+"\nspeedssss:"+speedssss);
        startAnimation(currentDegree, (float) speedssss * a);
    }

    /**
     * 指针+阴影偏移动画
     */
    private void startAnimation(float start, float end) {
        if (mAnim != null) {
            if (mAnim.isRunning() || mAnim.isStarted()) {
                mAnim.cancel();
                mAnim.removeAllUpdateListeners();
            }
            boolean running = mAnim.isRunning();
            boolean started = mAnim.isStarted();
            Log.i(TAG, "startAnimation: running:" + running + "--started" + started);
        }
        mAnim = ValueAnimator.ofFloat(start, end);
        //anim.setRepeatCount(ValueAnimator.INFINITE);//设置无限重复
        //anim.setRepeatMode(ValueAnimator.REVERSE);//设置重复模式
        mAnim.setDuration(500);
        mAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) mAnim.getAnimatedValue();
                //Log.i(TAG, "onAnimationUpdate: " + value);
                currentDegree = value;
                invalidate();
            }
        });
        mAnim.start();
    }

    /**
     * 退出动画.
     */
    public void closeAnimation() {
        if (mAnim != null) {
            mAnim.cancel();
            mAnim.removeAllUpdateListeners();
        }
    }
}
