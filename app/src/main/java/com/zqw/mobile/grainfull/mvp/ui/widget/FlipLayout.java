package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.zqw.mobile.grainfull.R;

import java.util.Calendar;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.widget
 * @ClassName: FlipLayout
 * @Description: 翻转布局(时钟效果)
 * @Author: WLY
 * @CreateDate: 2023/2/21 11:06
 */
public class FlipLayout extends FrameLayout {
    // 可见的
    private TextView mVisibleTextView;
    // 不可见
    private TextView mInvisibleTextView;


    private int layoutWidth;
    private int layoutHeight;
    private Scroller mScroller;
    private final String TAG = "FlipLayout";
    // 根据时间标记获取时间
    private String timeTag;
    private Camera mCamera = new Camera();
    private Matrix mMatrix = new Matrix();
    private Rect mTopRect = new Rect();
    private Rect mBottomRect = new Rect();
    private boolean isUp = true;
    private Paint mMinutenePaint = new Paint();
    private Paint mShadePaint = new Paint();
    private boolean isFlipping = false;
    // 设置显示的最大值
    private int maxNumber;
    private int flipTimes = 0;
    private int timesCount = 0;

    // 小时
    public static String hour = "HOUR";
    // 分钟
    public static String min = "MINUTE";
    // 秒
    public static String sec = "SECOND";

    private FlipOverListener mFlipOverListener;

    public FlipLayout(Context context) {
        super(context, null);
    }

    public FlipLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlipLayout);

        int resId = array.getResourceId(R.styleable.FlipLayout_flipTextBackground, -1);
        int color = Color.WHITE;
        if (-1 == resId) {
            color = array.getColor(R.styleable.FlipLayout_flipTextBackground, Color.WHITE);
        }
        float size = array.getDimension(R.styleable.FlipLayout_flipTextSize, 36);
        size = px2dip(context, size);
        int textColor = array.getColor(R.styleable.FlipLayout_flipTextColor, Color.BLACK);

        array.recycle();
        init(context, resId, color, size, textColor);
    }

    private void init(Context context, int resId, int color, float size, int textColor) {
        mScroller = new Scroller(context, new DecelerateInterpolator());//减速 动画插入器
//        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Aura.otf");
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/AtAura.otf");
        mInvisibleTextView = new TextView(context);
        mInvisibleTextView.setTextSize(size);
        mInvisibleTextView.setText("00");
        mInvisibleTextView.setGravity(Gravity.CENTER);
        mInvisibleTextView.setIncludeFontPadding(false);
        mInvisibleTextView.setTextColor(textColor);
        mInvisibleTextView.setTypeface(tf);
        if (resId == -1) {
            mInvisibleTextView.setBackgroundColor(color);
        } else {
            mInvisibleTextView.setBackgroundResource(resId);
        }
        addView(mInvisibleTextView);

        mVisibleTextView = new TextView(context);
        mVisibleTextView.setTextSize(size);
        mVisibleTextView.setText("00");
        mVisibleTextView.setGravity(Gravity.CENTER);
        mVisibleTextView.setIncludeFontPadding(false);
        mVisibleTextView.setTextColor(textColor);
        mVisibleTextView.setTypeface(tf);
        if (resId == -1) {
            mVisibleTextView.setBackgroundColor(color);
        } else {
            mVisibleTextView.setBackgroundResource(resId);
        }

        addView(mVisibleTextView);

        mShadePaint.setColor(Color.BLACK);
        mShadePaint.setStyle(Paint.Style.FILL);
        mMinutenePaint.setColor(Color.WHITE);
        mMinutenePaint.setStyle(Paint.Style.FILL);
    }

    public FlipLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public static float px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return pxValue / scale + 0.5f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        layoutWidth = MeasureSpec.getSize(widthMeasureSpec);
        layoutHeight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(layoutWidth, layoutHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            child.layout(0, 0, layoutWidth, layoutHeight);
        }

        mTopRect.top = 0;
        mTopRect.left = 0;
        mTopRect.right = getWidth();
        mTopRect.bottom = getHeight() / 2;

        mBottomRect.top = getHeight() / 2;
        mBottomRect.left = 0;
        mBottomRect.right = getWidth();
        mBottomRect.bottom = getHeight();
    }

    @Override
    public void computeScroll() {
//        Log.d(TAG,"computeScroll");
//        if(!mScroller.isFinished() && mScroller.computeScrollOffset()){
//            lastX = mScroller.getCurrX();
//            lastY = mScroller.getCurrY();
//            scrollTo(lastX,lastY);
//            postInvalidate();
//        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!mScroller.isFinished() && mScroller.computeScrollOffset()) {
            drawTopHalf(canvas);
            drawBottomHalf(canvas);
            drawFlipHalf(canvas);
            postInvalidate();
        } else {
            if (isFlipping) {
                showViews(canvas);
            }

            if (mScroller.isFinished() && !mScroller.computeScrollOffset()) {
                isFlipping = false;
            }

            if (timesCount < flipTimes) {
                timesCount += 1;

                initTextView();
                isFlipping = true;
                mScroller.startScroll(0, 0, 0, layoutHeight, getAnimDuration(flipTimes - timesCount));
                postInvalidate();
            } else {
                timesCount = 0;
                flipTimes = 0;
                if (null != mFlipOverListener && !isFlipping()) {
                    mFlipOverListener.onFLipOver(FlipLayout.this);
                }
            }
        }

    }

    /**
     * 显示需要显示的数字
     *
     * @param canvas
     */
    private void showViews(Canvas canvas) {
        String current = mVisibleTextView.getText().toString();
        if (mVisibleTextView.getText().toString().length() < 2) {
            current = "0" + mVisibleTextView.getText().toString();
        }
        String past = mInvisibleTextView.getText().toString();
        if (mInvisibleTextView.getText().toString().length() < 2) {
            past = "0" + mInvisibleTextView.getText().toString();
        }

        mVisibleTextView.setText(past);
        mInvisibleTextView.setText(current);
        //防止切换抖动
        drawChild(canvas, mVisibleTextView, 0);
    }

    /**
     * 画下半部分
     */
    private void drawBottomHalf(Canvas canvas) {
        canvas.save();

        canvas.clipRect(mBottomRect);
        View drawView = isUp ? mInvisibleTextView : mVisibleTextView;
        drawChild(canvas, drawView, 0);

        canvas.restore();
    }

    /**
     * 画上半部分
     */
    private void drawTopHalf(Canvas canvas) {
        canvas.save();

        canvas.clipRect(mTopRect);
        View drawView = isUp ? mVisibleTextView : mInvisibleTextView;
        drawChild(canvas, drawView, 0);

        canvas.restore();

    }

    /**
     * 画翻页部分
     */
    private void drawFlipHalf(Canvas canvas) {
        canvas.save();
        mCamera.save();

        View view = null;
        float deg = getDeg();
        if (deg > 90) {
            canvas.clipRect(isUp ? mTopRect : mBottomRect);
            mCamera.rotateX(isUp ? deg - 180 : -(deg - 180));
            view = mInvisibleTextView;
        } else {
            canvas.clipRect(isUp ? mBottomRect : mTopRect);
            mCamera.rotateX(isUp ? deg : -deg);
            view = mVisibleTextView;
        }

        mCamera.getMatrix(mMatrix);
        positionMatrix();
        canvas.concat(mMatrix);

        if (view != null) {
            drawChild(canvas, view, 0);
        }

        drawFlippingShademinutene(canvas);

        mCamera.restore();
        canvas.restore();
    }

    private float getDeg() {
        return mScroller.getCurrY() * 1.0f / layoutHeight * 180;
    }

    /**
     * 绘制翻页时的阳面和阴面
     */
    private void drawFlippingShademinutene(Canvas canvas) {
        final float degreesFlipped = getDeg();
        Log.d(TAG, "deg: " + degreesFlipped);
        if (degreesFlipped < 90) {
            final int alpha = getAlpha(degreesFlipped);
            Log.d(TAG, "小于90度时的透明度-------------------> " + alpha);
            mMinutenePaint.setAlpha(alpha);
            mShadePaint.setAlpha(alpha);
            canvas.drawRect(isUp ? mBottomRect : mTopRect, isUp ? mMinutenePaint : mShadePaint);
        } else {
            final int alpha = getAlpha(Math.abs(degreesFlipped - 180));
            Log.d(TAG, "大于90度时的透明度-------------> " + alpha);
            mShadePaint.setAlpha(alpha);
            mMinutenePaint.setAlpha(alpha);
            canvas.drawRect(isUp ? mTopRect : mBottomRect, isUp ? mShadePaint : mMinutenePaint);
        }
    }

    private int getAlpha(float degreesFlipped) {
        return (int) ((degreesFlipped / 90f) * 100);
    }

    private void positionMatrix() {
        mMatrix.preScale(0.25f, 0.25f);
        mMatrix.postScale(4.0f, 4.0f);
        mMatrix.preTranslate(-getWidth() / 2, -getHeight() / 2);
        mMatrix.postTranslate(getWidth() / 2, getHeight() / 2);
    }

    /**
     * 初始化隐藏textView显示的值
     */
    private void initCountTextView(int count) {

        int visibleValue = count;
        int invisibleValue = isUp ? visibleValue - 1 : visibleValue;

        if (invisibleValue < 0) {
            invisibleValue += maxNumber;
        }

        if (invisibleValue >= maxNumber) {
            invisibleValue -= maxNumber;
        }
        String value = String.valueOf(invisibleValue);
        if (value.length() < 2) {
            value = "0" + value;
        }
        mInvisibleTextView.setText(value);
    }

    /**
     * 初始化隐藏textView显示的值
     */
    private void initTextView() {

        int visibleValue = getTime();
        int invisibleValue = isUp ? visibleValue - 1 : visibleValue;

        if (invisibleValue < 0) {
            invisibleValue += maxNumber;
        }

        if (invisibleValue >= maxNumber) {
            invisibleValue -= maxNumber;
        }
        String value = String.valueOf(invisibleValue);
        if (value.length() < 2) {
            value = "0" + value;
        }
        mInvisibleTextView.setText(value);
    }

    /**
     * 初始化隐藏textView显示的值
     */
    private void initCoDownTextView(int count) {

        int visibleValue = count;
        String value = String.valueOf(visibleValue);
        if (value.length() < 2) {
            value = "0" + value;
        }
        mInvisibleTextView.setText(value);
    }

    /**
     * 根据传入的次数计算动画的时间
     * 控制翻页速度
     */
    private int getAnimDuration(int times) {
//        Log.e(TAG,"计算用的次数： " + times);
        if (times <= 0) {
            times = 1;
        }
        int animDuration = 500 - (500 - 100) / 9 * times;
//        Log.e(TAG, "animDuration: " + animDuration);
        return animDuration;
    }

    public static interface FlipOverListener {
        /**
         * 翻页完成回调
         *
         * @param flipLayout 当前翻页的控件
         */
        void onFLipOver(FlipLayout flipLayout);
    }

    //----------API-------------

    /**
     * 带动画翻动计时器
     * 需要翻动几次
     *
     * @param value   需要翻动的次数
     * @param isMinus 方向标识 true: 往上翻 - , false: 往下翻 +
     */
    public void smoothCountFlip(int value, int maxnumber, String timeTAG, boolean isMinus, int count) {
//        Log.e(TAG,"翻动的次数: " + value);
        this.timeTag = timeTAG;
        maxNumber = maxnumber;
        if (value <= 0) {
            //回调接口
            if (null != mFlipOverListener) {
                mFlipOverListener.onFLipOver(FlipLayout.this);
            }
            return;
        }
        flipTimes = value;
        this.isUp = isMinus;

        initCountTextView(count);
        isFlipping = true;
        mScroller.startScroll(0, 0, 0, layoutHeight, getAnimDuration(flipTimes - timesCount));
        timesCount = 1;
        postInvalidate();
    }

    /**
     * 带动画翻动倒计时
     * 需要翻动几次
     *
     * @param value   需要翻动的次数
     * @param isMinus 方向标识 true: 往上翻 - , false: 往下翻 +
     */
    public void smoothDownFlip(int value, int maxnumber, String timeTAG, boolean isMinus, int count) {
//        Log.e(TAG,"翻动的次数: " + value);
        this.timeTag = timeTAG;
        maxNumber = maxnumber;
        if (value <= 0) {
            //回调接口
            if (null != mFlipOverListener) {
                mFlipOverListener.onFLipOver(FlipLayout.this);
            }
            return;
        }
        flipTimes = value;
        this.isUp = isMinus;

        initCoDownTextView(count);
        isFlipping = true;
        mScroller.startScroll(0, 0, 0, layoutHeight, getAnimDuration(flipTimes - timesCount));
        timesCount = 1;
        postInvalidate();
    }


    /**
     * 带动画翻动
     * 需要翻动几次
     *
     * @param value   需要翻动的次数
     * @param isMinus 方向标识 true: 往上翻 - , false: 往下翻 +
     */
    public void smoothFlip(int value, int maxnumber, String timeTAG, boolean isMinus) {
//        Log.e(TAG,"翻动的次数: " + value);
        this.timeTag = timeTAG;
        maxNumber = maxnumber;
        if (value <= 0) {
            //回调接口
            if (null != mFlipOverListener) {
                mFlipOverListener.onFLipOver(FlipLayout.this);
            }
            return;
        }
        flipTimes = value;
        this.isUp = isMinus;

        initTextView();
        isFlipping = true;
        mScroller.startScroll(0, 0, 0, layoutHeight, getAnimDuration(flipTimes - timesCount));
        timesCount = 1;
        postInvalidate();
    }

    /**
     * 不带动画翻动
     *
     * @param value
     */
    public void flip(int value, int maxnumber, String timeTAG) {
        this.timeTag = timeTAG;
        maxNumber = maxnumber;
        String text = String.valueOf(value);
        if (text.length() < 2) {
            text = "0" + text;
        }
        mVisibleTextView.setText(text);
//        if(null != mFlipOverListener){
//            mFlipOverListener.onFLipOver(FlipLayout.this,timesCount);
//        }
    }

    public void addFlipOverListener(FlipOverListener flipOverListener) {
        this.mFlipOverListener = flipOverListener;
    }

    public TextView getmVisibleTextView() {
        return mVisibleTextView;
    }

    public TextView getmInvisibleTextView() {
        return mInvisibleTextView;
    }

    public boolean isUp() {
        return isUp;
    }

    public int getTimesCount() {
        return timesCount;
    }

    /**
     * @param resId 图片资源id
     */
    public void setFlipTextBackground(int resId) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (null != child) {
                child.setBackgroundResource(resId);
            }
        }
    }

    public void setFLipTextSize(float size) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            TextView child = (TextView) getChildAt(i);
            if (null != child) {
                child.setTextSize(size);
            }
        }
    }

    public void setFLipTextColor(int color) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            TextView child = (TextView) getChildAt(i);
            if (null != child) {
                child.setTextColor(color);
            }
        }
    }


    public boolean isFlipping() {
        return isFlipping && !mScroller.isFinished() && mScroller.computeScrollOffset();
    }

    public int getCurrentValue() {
        return Integer.parseInt(mVisibleTextView.getText().toString());
    }

    /**
     * 获取时间
     */
    private int getTime() {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int min = now.get(Calendar.MINUTE);
        int sec = now.get(Calendar.SECOND);
        switch (timeTag) {
            case "SECOND":
                return sec;
            case "MINUTE":
                return min;
            case "HOUR":
                return hour;

        }
        return 0;
    }

    private int getCouDownTime(int hour, int min, int sec) {
        switch (timeTag) {
            case "SECOND":
                return sec;
            case "MINUTE":
                return min;
            case "HOUR":
                return hour;

        }
        return 0;
    }
}
