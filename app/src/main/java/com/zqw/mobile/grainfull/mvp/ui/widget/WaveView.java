package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.zqw.mobile.grainfull.R;

import java.text.DecimalFormat;

/**
 * ======================================================================
 * <p/>
 * 作者：Renj
 * <p/>
 * 创建时间：2016-12-22    15:32
 * <p/>
 * 描述：水波纹效果控件
 * <p/>
 * ======================================================================
 */
public class WaveView extends View {
    /**
     * 默认波浪1长度
     */
    private final int WAVE_LENGTH1 = 600;
    /**
     * 默认波浪2长度
     */
    private final int WAVE_LENGTH2 = 550;
    /**
     * 默认波浪1高度
     */
    private final int WAVE_HEIGHT1 = 45;
    /**
     * 默认波浪2高度
     */
    private final int WAVE_HEIGHT2 = 35;
    /**
     * 默认每一次重绘时波峰1的移动的距离，实现移动效果
     */
    private final int WAVE_OFFSET1 = 12;
    /**
     * 默认每一次重绘时波峰2的移动的距离，实现移动效果
     */
    private final int WAVE_OFFSET2 = 4;
    /**
     * 默认波浪1颜色
     */
    private final int WAVE_COLOR1 = Color.parseColor("#0000ff");
    /**
     * 默认波浪2颜色
     */
    private final int WAVE_COLOR2 = Color.parseColor("#800000ff");
    /**
     * 默认边框颜色
     */
    private final int BORDER_COLOR = Color.parseColor("#800000ff");
    /**
     * 默认文字颜色
     */
    private final int DEFAULT_TEXT_COLOR = Color.parseColor("#ff0000");
    /**
     * 默认文字大小
     */
    private final int DEFAULT_TEXT_SIZE = 30;
    /**
     * 默认边框宽度
     */
    private final int BORDER_WIDTH = 2;
    /**
     * 默认两次重绘之间间隔的时间，5毫秒
     */
    private final int DEFAULT_TIME = 5;

    /**
     * 控件的宽度
     */
    private int mWidth;
    /**
     * 控件的高度
     */
    private int mHeight;
    /**
     * 水位上升时不断变化的 y 坐标
     */
    private float mChangeY;
    /**
     * 水位最终的高度，通过控件的高度和百分比计算出来
     */
    private float mFinalY;
    /**
     * 波浪1画笔
     */
    private Paint mWavePaint1;
    /**
     * 波浪2画笔
     */
    private Paint mWavePaint2;
    /**
     * 边框画笔
     */
    private Paint mBorderPaint;
    /**
     * 文字画笔
     */
    private Paint mTextPaint;
    /**
     * 波浪1路径
     */
    private Path mWavePath1;
    /**
     * 波浪2路径
     */
    private Path mWavePath2;
    /**
     * 当形状为圆形时，用于剪切画布的路径
     */
    private Path mCirclePath;
    /**
     * 波浪1颜色
     */
    private int mWaveColor1 = WAVE_COLOR1;
    /**
     * 波浪2颜色
     */
    private int mWaveColor2 = WAVE_COLOR2;
    /**
     * 边框颜色
     */
    private int mBorderColor = BORDER_COLOR;
    /**
     * 文字颜色
     */
    private int mTextColor = DEFAULT_TEXT_COLOR;
    /**
     * 文字大小
     */
    private int mTextSize = DEFAULT_TEXT_SIZE;
    /**
     * 波浪1高度
     */
    private int mWaveHeight1 = WAVE_HEIGHT1;
    /**
     * 波浪2高度
     */
    private int mWaveHeight2 = WAVE_HEIGHT2;
    /**
     * 波浪1长度
     */
    private int mWaveLenght1 = WAVE_LENGTH1;
    /**
     * 波浪2长度
     */
    private int mWaveLenght2 = WAVE_LENGTH2;
    /**
     * 边框宽度
     */
    private int mBorderWidth = BORDER_WIDTH;
    /**
     * 波峰的个数
     */
    private int mWaveCount;
    /**
     * 形状默认矩形
     */
    private ShowShape mShape = ShowShape.RECT;
    /**
     * 每一次重绘时波峰1的移动的距离，实现移动效果
     */
    private int mOffset1 = WAVE_OFFSET1;
    /**
     * 每一次重绘时波峰2的移动的距离，实现移动效果
     */
    private int mOffset2 = WAVE_OFFSET2;
    /**
     * 两次重绘之间间隔的时间，毫秒。
     */
    private int mTime = DEFAULT_TIME;
    /**
     * 绘制的高度，百分比。0表示内有高度，1表示全部高度
     */
    private float mPrecent = 0.5f;
    /**
     * 如果是圆形，定义圆形的 x 坐标，y 坐标和半径
     */
    private int mCenterX, mCenterY, mRadius;
    /**
     * 重绘值波峰移动距离的和
     */
    private int mMoveSum1, mMoveSum2;
    /**
     * 当前百分比
     */
    private float mCurrentPrecent = 0.0f;
    /**
     * 定义数字格式转行类
     */
    private DecimalFormat mFormat;
    /**
     * 能够绘制标记位，开始不能绘制
     */
    private boolean invalidateFlag = false;
    /**
     * 重置标记，开始为重置状态
     */
    private boolean isReset = true;
    /**
     * 百分比改变监听
     */
    private PrecentChangeListener mPrecentChangeListener;

    /**
     * 形状枚举，暂时只支持矩形和圆形，可扩展
     */
    public enum ShowShape {
        RECT, CIRCLE
    }

    /**
     * 百分比改变监听接口
     */
    public interface PrecentChangeListener {
        /**
         * 百分比发生改变时调用的方法
         *
         * @param precent 当前的百分比，格式 0.00 范围 [0.00 , 1.00]
         */
        void precentChange(double precent);
    }

    /**
     * 获取波浪1颜色
     *
     * @return
     */
    public int getWaveColor1() {
        return mWaveColor1;
    }

    /**
     * 设置波浪1颜色
     *
     * @param waveColor1
     * @return
     */
    public WaveView setWaveColor1(int waveColor1) {
        this.mWaveColor1 = waveColor1;
        return this;
    }

    /**
     * 获取波浪2颜色
     *
     * @return
     */
    public int getWaveColor2() {
        return mWaveColor2;
    }

    /**
     * 设置波浪2颜色
     *
     * @param waveColor2
     * @return
     */
    public WaveView setWaveColor2(int waveColor2) {
        this.mWaveColor2 = waveColor2;
        return this;
    }

    /**
     * 获取边框颜色
     *
     * @return
     */
    public int getBorderColor() {
        return mBorderColor;
    }

    /**
     * 设置边框颜色
     *
     * @param borderColor
     * @return
     */
    public WaveView setBorderColor(int borderColor) {
        this.mBorderColor = borderColor;
        return this;
    }

    /**
     * 获取文字颜色
     *
     * @return
     */
    public int getTextColor() {
        return mTextColor;
    }

    /**
     * 设置文字颜色
     *
     * @param textColor
     * @return
     */
    public WaveView setTextColor(int textColor) {
        this.mTextColor = textColor;
        return this;
    }

    /**
     * 获取文字大小
     *
     * @return
     */
    public int getTextSize() {
        return mTextSize;
    }

    /**
     * 设置文字大小
     *
     * @param textSize
     * @return
     */
    public WaveView setTextSize(int textSize) {
        this.mTextSize = textSize;
        return this;
    }

    /**
     * 获取波浪1的波峰高度
     *
     * @return
     */
    public int getWaveHeight1() {
        return mWaveHeight1;
    }

    /**
     * 设置波浪1的波峰高度
     *
     * @param waveHeight1
     * @return
     */
    public WaveView setWaveHeight1(int waveHeight1) {
        this.mWaveHeight1 = waveHeight1;
        return this;
    }

    /**
     * 获取波浪2的波峰高度
     *
     * @return
     */
    public int getWaveHeight2() {
        return mWaveHeight2;
    }

    /**
     * 设置波浪1的波峰高度
     *
     * @param waveHeight2
     * @return
     */
    public WaveView setWaveHeight2(int waveHeight2) {
        this.mWaveHeight2 = waveHeight2;
        return this;
    }

    /**
     * 获取波浪1的波长
     *
     * @return
     */
    public int getWaveLenght1() {
        return mWaveLenght1;
    }

    /**
     * 设置波浪1的波长
     *
     * @param waveLenght1
     * @return
     */
    public WaveView setWaveLenght1(int waveLenght1) {
        this.mWaveLenght1 = waveLenght1;
        return this;
    }

    /**
     * 获取波浪2的波长
     *
     * @return
     */
    public int getWaveLenght2() {
        return mWaveLenght2;
    }

    /**
     * 设置波浪2的波长
     *
     * @param waveLenght2
     * @return
     */
    public WaveView setWaveLenght2(int waveLenght2) {
        this.mWaveLenght2 = waveLenght2;
        return this;
    }

    /**
     * 获取边框宽度
     *
     * @return
     */
    public int getBorderWidth() {
        return mBorderWidth;
    }

    /**
     * 设置边框宽度
     *
     * @param borderWidth
     * @return
     */
    public WaveView setBorderWidth(int borderWidth) {
        this.mBorderWidth = borderWidth;
        return this;
    }

    /**
     * 获取当前显示形状
     *
     * @return
     */
    public ShowShape getShape() {
        return mShape;
    }

    /**
     * 设置当前显示形状
     *
     * @param shape
     * @return
     */
    public WaveView setShape(ShowShape shape) {
        this.mShape = shape;
        return this;
    }

    /**
     * 获取重绘时波浪1的偏移量
     *
     * @return
     */
    public int getOffset1() {
        return mOffset1;
    }

    /**
     * 设置重绘时波浪1的偏移量
     *
     * @param offset1
     * @return
     */
    public WaveView setOffset1(int offset1) {
        this.mOffset1 = offset1;
        return this;
    }

    /**
     * 获取重绘时波浪2的偏移量
     *
     * @return
     */
    public int getOffset2() {
        return mOffset2;
    }

    /**
     * 设置重绘时波浪2的偏移量
     *
     * @param offset2
     * @return
     */
    public WaveView setOffset2(int offset2) {
        this.mOffset2 = offset2;
        return this;
    }

    /**
     * 获取两次重绘之间的间隔时间
     *
     * @return
     */
    public int getTime() {
        return mTime;
    }

    /**
     * 设置两次重绘之间的间隔时间，毫秒
     *
     * @param time
     * @return
     */
    public WaveView setTime(int time) {
        this.mTime = time;
        return this;
    }

    /**
     * 获取的百分比
     *
     * @return
     */
    public float getPrecent() {
        return mPrecent;
    }

    /**
     * 设置百分比
     *
     * @param precent
     * @return
     */
    public WaveView setPrecent(float precent) {
        this.mPrecent = precent;
        return this;
    }

    /**
     * 获取百分比监听器对象
     *
     * @return
     */
    public PrecentChangeListener getPrecentChangeListener() {
        return mPrecentChangeListener;
    }

    /**
     * 设置百分比改变监听器
     *
     * @param precentChangeListener
     * @return
     */
    public WaveView setPrecentChangeListener(PrecentChangeListener precentChangeListener) {
        this.mPrecentChangeListener = precentChangeListener;
        return this;
    }

    /**
     * 获取当前的百分比
     *
     * @return
     */
    public float getCurrentPrecent() {
        return mCurrentPrecent;
    }

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs); // 获取布局文件中dingy9i的属性
        init();
    }

    /**
     * 获取布局文件中的初始化属性
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        mWaveLenght1 = typedArray.getInteger(R.styleable.WaveView_wave1Length, WAVE_LENGTH1);
        mWaveHeight1 = typedArray.getInteger(R.styleable.WaveView_wave1Height, WAVE_HEIGHT1);
        mWaveColor1 = typedArray.getColor(R.styleable.WaveView_wave1Color, WAVE_COLOR1);
        mOffset1 = typedArray.getInteger(R.styleable.WaveView_wave1Offset, WAVE_OFFSET1);

        mWaveLenght2 = typedArray.getInteger(R.styleable.WaveView_wave2Length, WAVE_LENGTH2);
        mWaveHeight2 = typedArray.getInteger(R.styleable.WaveView_wave2Height, WAVE_HEIGHT2);
        mWaveColor2 = typedArray.getColor(R.styleable.WaveView_wave2Color, WAVE_COLOR2);
        mOffset2 = typedArray.getInteger(R.styleable.WaveView_wave2Offset, WAVE_OFFSET2);

        mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.WaveView_borderWidth, BORDER_WIDTH);
        mBorderColor = typedArray.getColor(R.styleable.WaveView_borderColor, BORDER_COLOR);

        mTextSize = typedArray.getDimensionPixelSize(R.styleable.WaveView_textSize, DEFAULT_TEXT_SIZE);
        mTextColor = typedArray.getColor(R.styleable.WaveView_textColor, DEFAULT_TEXT_COLOR);

        mTime = typedArray.getInteger(R.styleable.WaveView_intervalTime, DEFAULT_TIME);
        mPrecent = typedArray.getFloat(R.styleable.WaveView_precent, 0.5f);
        int shapeValue = typedArray.getInteger(R.styleable.WaveView_showShape, 0);
        if (shapeValue == 0) mShape = ShowShape.RECT;
        else mShape = ShowShape.CIRCLE;
        typedArray.recycle();
    }

    /**
     * 初始化方法
     */
    private void init() {
        mWavePaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWavePaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mWavePath1 = new Path();
        mWavePath2 = new Path();
        mCirclePath = new Path();

        mWavePaint1.setColor(mWaveColor1);
        mWavePaint1.setStyle(Paint.Style.FILL);

        mWavePaint2.setColor(mWaveColor2);
        mWavePaint2.setStyle(Paint.Style.FILL);

        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setStyle(Paint.Style.STROKE);

        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

        // 定义数字显示个格式
        mFormat = new DecimalFormat("###,###,##0.00");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        mChangeY = mHeight;
        // 计算波峰个数，为了实现移动效果，保证至少绘制两个波峰
        mWaveCount = (int) Math.round(mWidth / Math.max(mWaveLenght1, mWaveLenght2) + 1.5);
        mFinalY = (1 - mPrecent) * mHeight; // 计算水位最终高度

        // 计算圆心和半径
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
        mRadius = Math.min(mWidth, mHeight) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mWavePath1.reset();
        mWavePath2.reset();
        mCirclePath.reset();

        // 如果想要显示更多的形状，在这里剪切不同形状即可
        if (mShape == ShowShape.CIRCLE) {
            // 关闭硬件加速，否则canvas的剪切无法生效
            if(canvas.isHardwareAccelerated() || this.isHardwareAccelerated() && invalidateFlag){
                this.setLayerType(LAYER_TYPE_SOFTWARE,null);
            }
            // 先判断是否为圆形，如果是，直接使用canvas的方法剪切一个圆形显示，其余地方不显示
            mCirclePath.addCircle(mCenterX, mCenterY, mRadius, Path.Direction.CW);
            canvas.clipPath(mCirclePath);
        }

        if (mBorderWidth > 0) {
            // 边框大于0,表示需要绘制边框
            if (mShape == ShowShape.CIRCLE) {
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mBorderPaint);
            } else if (mShape == ShowShape.RECT) {
                canvas.drawRect(0, 0, mWidth, mHeight, mBorderPaint);
            }
        }

        mWavePath1.moveTo(-mWaveLenght1, mChangeY);
        mWavePath2.moveTo(-mWaveLenght2, mChangeY);
        if (!isReset) { // 判断重置标记
            // 利用贝塞尔曲线绘制波浪
            for (int i = 0; i < mWaveCount; i++) {
                // 绘制波浪1的曲线
                mWavePath1.quadTo((-mWaveLenght1 * 3 / 4) + (i * mWaveLenght1) + mMoveSum1, mChangeY + mWaveHeight1, (-mWaveLenght1 / 2) + (i * mWaveLenght1) + mMoveSum1, mChangeY);
                mWavePath1.quadTo((-mWaveLenght1 * 1 / 4) + (i * mWaveLenght1) + mMoveSum1, mChangeY - mWaveHeight1, (i * mWaveLenght1) + mMoveSum1, mChangeY);

                // 绘制波浪2的曲线
                mWavePath2.quadTo((-mWaveLenght2 * 3 / 4) + (i * mWaveLenght2) + mMoveSum2, mChangeY - mWaveHeight2, (-mWaveLenght2 / 2) + (i * mWaveLenght2) + mMoveSum2, mChangeY);
                mWavePath2.quadTo((-mWaveLenght2 * 1 / 4) + (i * mWaveLenght2) + mMoveSum2, mChangeY + mWaveHeight2, (i * mWaveLenght2) + mMoveSum2, mChangeY);
            }

            // 不断改变高度，实现逐渐水位逐渐上涨效果
            mChangeY -= 1;
            if (mChangeY < mFinalY) mChangeY = mFinalY;

            // 波峰1往右移动，波峰2往左移动
            mMoveSum1 += mOffset1;
            mMoveSum2 -= mOffset2;
            if (mMoveSum1 >= mWaveLenght1) mMoveSum1 = 0;
            if (mMoveSum2 <= 0) mMoveSum2 = mWaveLenght2;

            // 填充矩形，让上涨之后的水位下面填充颜色
            mWavePath1.lineTo(mWidth, mHeight);
            mWavePath1.lineTo(0, mHeight);
            mWavePath1.close();
            mWavePath2.lineTo(mWidth, mHeight);
            mWavePath2.lineTo(0, mHeight);
            mWavePath2.close();

            canvas.drawPath(mWavePath1, mWavePaint1);
            canvas.drawPath(mWavePath2, mWavePaint2);
        } else {
            // 是重置
            canvas.drawColor(Color.TRANSPARENT);
        }

        // 计算当前的百分比
        mCurrentPrecent = 1 - mChangeY / mHeight;
        // 格式化数字格式
        String format1 = mFormat.format(mCurrentPrecent);
        // 绘制文字，将百分比绘制到界面。此处用的是 "50%" 的形式，可以根据需求改变格式
        double parseDouble = Double.parseDouble(format1);
        canvas.drawText((int) (parseDouble * 100) + " %", (mWidth - mTextPaint.measureText(format1)) / 2, mHeight / 5, mTextPaint);
        // 监听对象不为null并且没有达到设置高度时，调用监听方法
        if (mPrecentChangeListener != null && mChangeY != mFinalY) {
            mPrecentChangeListener.precentChange(parseDouble);
        }

        // 判断绘制标记
        if (invalidateFlag) postInvalidateDelayed(mTime);
    }

    /**
     * 开始
     */
    public void start() {
        invalidateFlag = true;
        isReset = false;
        postInvalidateDelayed(mTime);
    }

    /**
     * 暂停
     */
    public void stop() {
        invalidateFlag = false;
        isReset = false;
    }

    /**
     * 重置
     */
    public void reset() {
        invalidateFlag = false;
        isReset = true;
        mChangeY = mHeight;
        postInvalidate();
    }
}