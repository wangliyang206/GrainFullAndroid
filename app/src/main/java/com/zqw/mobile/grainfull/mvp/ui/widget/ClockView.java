package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;

import java.util.Calendar;

/**
 * 时钟视图
 */
public class ClockView extends View {

    /* 全局画布 */
    private Canvas mCanvas;
    /* 12、3、6、9小时文本画笔 */
    private Paint mTextPaint;
    /* 测量小时文本宽高的矩形 */
    private Rect mTextRect = new Rect();
    /* 小时圆圈画笔 */
    private Paint mCirclePaint;
    /* 小时圆圈线条宽度 */
    private float mCircleStrokeWidth = 4;
    /* 小时圆圈的外接矩形 */
    private RectF mCircleRectF = new RectF();

    /* 亮色，用于分针、秒针、渐变终止色 */
    private int mLightColor;
    /* 暗色，圆弧、刻度线、时针、渐变起始色 */
    private int mDarkColor;
    /* 背景色 */
    private int mBackgroundColor;
    /* 小时文本字体大小 */
    private float mTextSize;

    /* 时钟半径，不包括padding值 */
    private float mRadius;
    /* 刻度线长度 */
    private float mScaleLength;

    /* 时针角度 */
    private float mHourDegree;
    /* 分针角度 */
    private float mMinuteDegree;
    /* 秒针角度 */
    private float mSecondDegree;
    /* 时针画笔 */
    private Paint mHourHandPaint;
    /* 分针画笔 */
    private Paint mMinuteHandPaint;
    /* 秒针画笔 */
    private Paint mSecondHandPaint;
    /* 时针路径 */
    private Path mHourHandPath = new Path();
    /* 分针路径 */
    private Path mMinuteHandPath = new Path();
    /* 秒针路径 */
    private Path mSecondHandPath = new Path();

    /* 加一个默认的padding值，为了防止用camera旋转时钟时造成四周超出view大小 */
    private float mDefaultPadding;
    private float mPaddingLeft;
    private float mPaddingTop;
    private float mPaddingRight;
    private float mPaddingBottom;

    /* 梯度扫描渐变 */
    private SweepGradient mSweepGradient;
    /* 渐变矩阵，作用在SweepGradient */
    private Matrix mGradientMatrix;

    /* 指针的在x轴的位移 */
    private float mCanvasTranslateX;
    /* 指针的在y轴的位移 */
    private float mCanvasTranslateY;
    /* 指针的最大位移 */
    private float mMaxCanvasTranslate;

    /* 刻度圆弧画笔 */
    private Paint mScaleArcPaint;
    /* 刻度圆弧的外接矩形 */
    private RectF mScaleArcRectF = new RectF();
    /* 刻度线画笔 */
    private Paint mScaleLinePaint;

    public ClockView(Context context) {
        super(context);
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClockView, 0, 0);
        mBackgroundColor = ta.getColor(R.styleable.ClockView_clock_backgroundColor, Color.parseColor("#237EAD"));
        mLightColor = ta.getColor(R.styleable.ClockView_clock_lightColor, Color.parseColor("#ffffff"));
        mDarkColor = ta.getColor(R.styleable.ClockView_clock_darkColor, Color.parseColor("#80ffffff"));
        mTextSize = ta.getDimension(R.styleable.ClockView_clock_textSize, ArmsUtils.sp2px(context, 14));
        ta.recycle();

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mDarkColor);
        //居中绘制文字
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mCircleStrokeWidth);
        mCirclePaint.setColor(mDarkColor);

        mScaleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScaleLinePaint.setStyle(Paint.Style.STROKE);
        mScaleLinePaint.setColor(mBackgroundColor);

        mScaleArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScaleArcPaint.setStyle(Paint.Style.STROKE);

        mGradientMatrix = new Matrix();

        mSecondHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondHandPaint.setStyle(Paint.Style.FILL);
        mSecondHandPaint.setColor(mLightColor);

        mHourHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHourHandPaint.setStyle(Paint.Style.FILL);
        mHourHandPaint.setColor(mDarkColor);

        mMinuteHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMinuteHandPaint.setStyle(Paint.Style.FILL);
        mMinuteHandPaint.setColor(mLightColor);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureDimension(widthMeasureSpec), measureDimension(heightMeasureSpec));
    }

    private int measureDimension(int measureSpec) {
        int defaultSize = 800;
        int model = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (model) {
            case MeasureSpec.EXACTLY:
                return size;
            case MeasureSpec.AT_MOST:
                return Math.min(size, defaultSize);
            case MeasureSpec.UNSPECIFIED:
                return defaultSize;
            default:
                return defaultSize;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = Math.min(w - getPaddingLeft() - getPaddingRight(),
                h - getPaddingTop() - getPaddingBottom()) / 2;// 各个指针长度
        mDefaultPadding = 0.12f * mRadius;
        mPaddingLeft = mDefaultPadding + w / 2 - mRadius + getPaddingLeft();// 钟离左边界距离
        mPaddingRight = mDefaultPadding + w / 2 - mRadius + getPaddingRight();// 钟离右边界距离
        mPaddingTop = mDefaultPadding + h / 2 - mRadius + getPaddingTop();// 钟离上边界距离
        mPaddingBottom = mDefaultPadding + h / 2 - mRadius + getPaddingBottom();// 钟离下边界距离

        mScaleLength = 0.12f * mRadius;// 根据比例确定刻度线长度
        mScaleLinePaint.setStrokeWidth(0.012f * mRadius);// 刻度圈的宽度

        mScaleArcPaint.setStrokeWidth(mScaleLength);

        //梯度扫描渐变，以(w/2,h/2)为中心点，两种起止颜色梯度渐变
        //float数组表示，[0,0.75)为起始颜色所占比例，[0.75,1}为起止颜色渐变所占比例
        mSweepGradient = new SweepGradient(w / 2, h / 2,
                new int[]{mDarkColor, mLightColor}, new float[]{0.75f, 1});
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("owen", "clock onDraw start");
        mCanvas = canvas;
        drawOutSideArc();// 外面的圈圈
        getCurrentTime();
        drawScaleLine();// 刻度 加 渐变色
        drawSecondNeedle();// 三个针
        drawMinuteNeedle();
        drawHourHand();
        invalidate();
        Log.i("owen", "clock onDraw end");
    }

    /**
     * 获取当前时间
     */
    private void getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        float milliSecond = calendar.get(Calendar.MILLISECOND);
        float second = calendar.get(Calendar.SECOND) + milliSecond / 1000;// 精确到小数点后 保证圆滑
        float minute = calendar.get(Calendar.MINUTE) + second / 60;
        float hour = calendar.get(Calendar.HOUR) + minute / 60;
        mSecondDegree = second / 60 * 360;
        mMinuteDegree = minute / 60 * 360;
        mHourDegree = hour / 12 * 360;

        if (mListener != null) {
            mListener.onClockMonitor(calendar.get(Calendar.AM_PM) == Calendar.PM, Math.round(hour), Math.round(minute), Math.round(second));
        }
    }

    /**
     * 画最外圈的时间 12、3、6、9 文本和4段弧线
     */
    private void drawOutSideArc() {
        String[] timeList = new String[]{"12", "3", "6", "9"};
        //计算数字的高度
        mTextPaint.getTextBounds(timeList[0], 0, timeList[1].length(), mTextRect);
        mCircleRectF.set(mPaddingLeft + mTextRect.width() / 2 + mCircleStrokeWidth / 2,// 画一个外界小矩形，在矩形里画圆
                mPaddingTop + mTextRect.height() / 2 + mCircleStrokeWidth / 2,
                getWidth() - mPaddingRight - mTextRect.width() / 2 - mCircleStrokeWidth / 2,
                getHeight() - mPaddingBottom - mTextRect.height() / 2 - mCircleStrokeWidth / 2);
        mCanvas.drawText(timeList[0], getWidth() / 2, mCircleRectF.top + mTextRect.height() / 2, mTextPaint);// 定点写字
        mCanvas.drawText(timeList[1], mCircleRectF.right, getHeight() / 2 + mTextRect.height() / 2, mTextPaint);
        mCanvas.drawText(timeList[2], getWidth() / 2, mCircleRectF.bottom + mTextRect.height() / 2, mTextPaint);
        mCanvas.drawText(timeList[3], mCircleRectF.left, getHeight() / 2 + mTextRect.height() / 2, mTextPaint);
        //画连接数字的4段弧线
        for (int i = 0; i < 4; i++) {
            // 画四个弧线 sweepAngle 弧线角度（扇形角度）
            mCanvas.drawArc(mCircleRectF, 5 + 90 * i, 80, false, mCirclePaint);
        }
    }

    /**
     * 画一圈梯度渲染的亮暗色渐变圆弧，重绘时不断旋转，上面盖一圈背景色的刻度线
     */
    private void drawScaleLine() {
        mCanvas.save();
        mCanvas.translate(mCanvasTranslateX, mCanvasTranslateY);
        mScaleArcRectF.set(mPaddingLeft + 1.5f * mScaleLength + mTextRect.height() / 2,
                mPaddingTop + 1.5f * mScaleLength + mTextRect.height() / 2,
                getWidth() - mPaddingRight - mTextRect.height() / 2 - 1.5f * mScaleLength,
                getHeight() - mPaddingBottom - mTextRect.height() / 2 - 1.5f * mScaleLength);

        // matrix默认会在三点钟方向开始颜色的渐变，为了吻合钟表十二点钟顺时针旋转的方向，把秒针旋转的角度减去90度
        mGradientMatrix.setRotate(mSecondDegree - 90, getWidth() / 2, getHeight() / 2);
        mSweepGradient.setLocalMatrix(mGradientMatrix);
        mScaleArcPaint.setShader(mSweepGradient);
        mCanvas.drawArc(mScaleArcRectF, 0, 360, false, mScaleArcPaint);

        // 画背景色刻度线
        for (int i = 0; i < 200; i++) {
            mCanvas.drawLine(getWidth() / 2, mPaddingTop + mScaleLength + mTextRect.height() / 2,
                    getWidth() / 2, mPaddingTop + 2 * mScaleLength + mTextRect.height() / 2, mScaleLinePaint);
            mCanvas.rotate(1.8f, getWidth() / 2, getHeight() / 2);
        }
        mCanvas.restore();
    }

    /**
     * 秒针
     */
    private void drawSecondNeedle() {
        mCanvas.save();// ❑ save：用来保存Canvas的状态。save之后，可以调用Canvas的平移、放缩、旋转、错切、裁剪等操作。
        mCanvas.rotate(mSecondDegree, getWidth() / 2, getHeight() / 2);// 设置指针位置
        mSecondHandPath.reset();
        float offset = mPaddingTop + mTextRect.height() / 2;

        mSecondHandPath.moveTo(getWidth() / 2, offset + 0.26f * mRadius);// 这三行绘制三角尖
        mSecondHandPath.lineTo(getWidth() / 2 - 0.05f * mRadius, offset + 0.34f * mRadius);
        mSecondHandPath.lineTo(getWidth() / 2 + 0.05f * mRadius, offset + 0.34f * mRadius);
        mSecondHandPath.close();
        mSecondHandPaint.setColor(mLightColor);
        mCanvas.drawPath(mSecondHandPath, mSecondHandPaint);
        mCanvas.restore();// ❑ restore：用来恢复Canvas之前保存的状态。防止save后对Canvas执行的操作对后续的绘制有影响。
    }

    /**
     * 绘制分针
     */
    private void drawMinuteNeedle() {
        mCanvas.save();
        mCanvas.translate(mCanvasTranslateX * 2f, mCanvasTranslateY * 2f);
        mCanvas.rotate(mMinuteDegree, getWidth() / 2, getHeight() / 2);
        mMinuteHandPath.reset();

        float offset = mPaddingTop + mTextRect.height() / 2;
        mMinuteHandPath.moveTo(getWidth() / 2 - 0.01f * mRadius, getHeight() / 2 - 0.03f * mRadius);
        mMinuteHandPath.lineTo(getWidth() / 2 - 0.008f * mRadius, offset + 0.365f * mRadius);
        mMinuteHandPath.quadTo(getWidth() / 2, offset + 0.345f * mRadius,
                getWidth() / 2 + 0.008f * mRadius, offset + 0.365f * mRadius);
        mMinuteHandPath.lineTo(getWidth() / 2 + 0.01f * mRadius, getHeight() / 2 - 0.03f * mRadius);
        mMinuteHandPath.close();
        mMinuteHandPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawPath(mMinuteHandPath, mMinuteHandPaint);

        mCircleRectF.set(getWidth() / 2 - 0.03f * mRadius, getHeight() / 2 - 0.03f * mRadius,//绘制指针轴的小圆圈
                getWidth() / 2 + 0.03f * mRadius, getHeight() / 2 + 0.03f * mRadius);
        mMinuteHandPaint.setStyle(Paint.Style.STROKE);
        mMinuteHandPaint.setStrokeWidth(0.02f * mRadius);
        mCanvas.drawArc(mCircleRectF, 0, 360, false, mMinuteHandPaint);
        mCanvas.restore();
    }

    /**
     * 绘制时针
     */
    private void drawHourHand() {
        mCanvas.save();
        mCanvas.translate(mCanvasTranslateX * 1.2f, mCanvasTranslateY * 1.2f);
        mCanvas.rotate(mHourDegree, getWidth() / 2, getHeight() / 2);
        mHourHandPath.reset();
        float offset = mPaddingTop + mTextRect.height() / 2;
        mHourHandPath.moveTo(getWidth() / 2 - 0.018f * mRadius, getHeight() / 2 - 0.03f * mRadius);
        mHourHandPath.lineTo(getWidth() / 2 - 0.009f * mRadius, offset + 0.48f * mRadius);
        mHourHandPath.quadTo(getWidth() / 2, offset + 0.46f * mRadius,
                getWidth() / 2 + 0.009f * mRadius, offset + 0.48f * mRadius);
        mHourHandPath.lineTo(getWidth() / 2 + 0.018f * mRadius, getHeight() / 2 - 0.03f * mRadius);
        mHourHandPath.close();
        mHourHandPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawPath(mHourHandPath, mHourHandPaint);

        mCircleRectF.set(getWidth() / 2 - 0.03f * mRadius, getHeight() / 2 - 0.03f * mRadius,
                getWidth() / 2 + 0.03f * mRadius, getHeight() / 2 + 0.03f * mRadius);
        mHourHandPaint.setStyle(Paint.Style.STROKE);
        mHourHandPaint.setStrokeWidth(0.01f * mRadius);
        mCanvas.drawArc(mCircleRectF, 0, 360, false, mHourHandPaint);
        mCanvas.restore();
    }

    private onClockMonitorListener mListener;

    public interface onClockMonitorListener {
        void onClockMonitor(boolean isNoon, int hour, int minute, int second);
    }

    public void setOnClockMonitorListener(onClockMonitorListener listener) {
        this.mListener = listener;
    }
}
