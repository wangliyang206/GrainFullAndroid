package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

import com.zqw.mobile.grainfull.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 转盘 - 只有文字
 */

public class TurntableView extends View {

    /**
     * 扇形内停止位置是否随机：默认false不随机(停在中间)，true为扇形内随机停
     */
    private boolean isSectorPositionRandom = false;
    /**
     * 转盘中的项数
     */
    private Integer mPanNum;
    /**
     * 转盘中的文字说明
     */
    private ArrayList<String> mNamesStrs = new ArrayList<>();
    /**
     * 控件宽
     */
    private int mWid;
    /**
     * 控件高
     */
    private int mHei;
    /**
     * 圆心x坐标
     */
    private int mCenterX;
    /**
     * 圆心y坐标
     */
    private int mCenterY;
    /**
     * 圆中每个item部分占有的角度
     */
    private float mOffsetAngle;
    /**
     * 圆半径
     */
    private int mRadius;
    /**
     * 画笔
     */
    private Paint mPaint = new Paint();
    /**
     * item颜色
     */
    private ArrayList<Integer> mColors = new ArrayList<>();
    /**
     * 屏幕高
     */
    private int mScreenHeight;
    /**
     * 屏幕宽
     */
    private int mScreenWidth;
    /**
     * 控件宽占屏幕宽的比例
     */
    private float mPercentage;
    /**
     * 手势探测器
     */
    private GestureDetectorCompat mDetector;
    /**
     * 当前初始角度
     */
    private float mCurrentAngle = 0;
    /**
     * 持续时间
     */
    private long mDuration = 2000;
    /**
     * 手势识别，手指开始x坐标
     */
    private float mStartX;
    /**
     * 手势识别，手指开始y坐标
     */
    private float mStartY;
    /**
     * onFling中的属性动画对象
     */
    private ValueAnimator mOnFlingAnimator;
    /**
     * 转盘转动方向，true顺时针，false逆时针
     */
    private boolean isClockwise = true;
    private String TAG = "TurntableView";
    /**
     * 转盘停止后停在某个item的某个比例的位置
     */
    private float mRandomPositionPro = (float) 0.2;
    /**
     * 是否正在抽奖
     */
    private boolean isDrawingLottery = false;
    /**
     * 控件监听
     */
    private ITurntableListener listener;

    public TurntableView(Context context) {
        this(context, null);
    }

    public TurntableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TurntableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    /**
     * 初始化view
     *
     * @param context
     * @param attrs
     */
    private void initView(Context context, AttributeSet attrs) {
        //手势识别对象
        mDetector = new GestureDetectorCompat(context, new TurntableGestureListener());
        //抗锯齿
        mPaint.setAntiAlias(true);

        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TurntableView);
        if (array != null) {
            mPanNum = array.getInteger(R.styleable.TurntableView_pannum, 8);
            int colorsId = array.getResourceId(R.styleable.TurntableView_colors, R.array.colors);
            int namesArray = array.getResourceId(R.styleable.TurntableView_names, R.array.dinnerFoodName);
            mPercentage = array.getFloat(R.styleable.TurntableView_percentage, (float) 0.75);
            int[] colors = context.getResources().getIntArray(colorsId);
            String[] namesStrs = context.getResources().getStringArray(namesArray);

            for (int i = 0; i < colors.length; i++) {
                mColors.add(colors[i]);
            }

            for (int i = 0; i < namesStrs.length; i++) {
                mNamesStrs.add(namesStrs[i]);
            }
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //控制view的宽为屏幕宽的mPercentage
        int viewWid = (int) ((float) mScreenWidth * mPercentage);
        setMeasuredDimension(viewWid, viewWid);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWid = w;
        mHei = h;

        //让转盘的宽为view的宽，半径为view的宽的一半
        mCenterX = mWid / 2;
        mCenterY = mCenterX;
        mRadius = mWid / 2;

        mOffsetAngle = (float) 360 / (float) mPanNum;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画圆的背景颜色
        drawBackground(canvas);

        //画文本
        drawText(canvas);
    }

    /**
     * 画圆的整体背景
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        RectF rectF = new RectF(0, 0, mWid, mHei);

        float angle = mCurrentAngle;
        for (int i = 0; i < mPanNum; i++) {
            int yushu = i % mColors.size();
            mPaint.setColor(mColors.get(yushu));
            canvas.drawArc(rectF, angle, mOffsetAngle, true, mPaint);
            angle = angle + mOffsetAngle;
        }
    }

    /**
     * 画文本
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(30);
        RectF rectF = new RectF(0, 0, mWid, mHei);

        //计算text文本的高度
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        float textHeight = fm.bottom - fm.top;

        float startAngle = mCurrentAngle;
        for (int i = 0; i < mNamesStrs.size(); i++) {
            //使文本根据，每个item的圆弧路径绘制
            Path path = new Path();
            path.addArc(rectF, startAngle, mOffsetAngle);
            canvas.drawTextOnPath(mNamesStrs.get(i), path, 0, textHeight + 10, mPaint);
            startAngle = startAngle + mOffsetAngle;
        }
    }

    /**
     * 让转盘根据rotation值重绘
     *
     * @param rotation
     */
    private void setRotate(float rotation) {
        //控制mCurrentAngle在0到360之间
        mCurrentAngle = (rotation % 360 + 360) % 360;
        invalidate();
    }

    /**
     * 开始转动到指定位置
     *
     * @param position
     */
    public void startRotate(int position, ITurntableListener listener) {
        this.listener = listener;
        if (isDrawingLottery) {
            return;
        }
        if (position >= 0 && position < mPanNum) {
            //指定位置
            setScrollToPosition(position);
        } else {
            //如果用户输入的数值，圆中没有对应位置的话，那么随机
            int random = getRandom(mPanNum);
            setScrollToPosition(random);
        }
    }

    /**
     * 开始随机转动到随机位置
     */
    public void startRotate(ITurntableListener listener) {
        this.listener = listener;
        if (isDrawingLottery) {
            return;
        }
        //随机
        int random = getRandom(mPanNum);
        setScrollToPosition(random);
    }

    /**
     * 返回0-num之间的数值
     *
     * @return
     */
    public int getRandom(int num) {
        Random random = new Random();
        return random.nextInt(num);
    }

    /**
     * 滚动到position位置
     *
     * @param position
     */
    private void setScrollToPosition(final int position) {
        if (isSectorPositionRandom) {
            // 扇形内随机停
            mRandomPositionPro = getRandomPositionPro();
        } else {
            // 停在扇形中间
            mRandomPositionPro = 0.5f;
        }

        //计算转动到position位置停止后的角度值
        float entAngle = 270 - mOffsetAngle * ((float) position + mRandomPositionPro);
        if (entAngle < mCurrentAngle) {
            entAngle = entAngle + 4 * 360;
        } else {
            entAngle = entAngle + 3 * 360;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(mCurrentAngle, entAngle);
        animator.setDuration(mDuration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                setRotate(animatedValue);
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isDrawingLottery = true;
                if (listener != null) {
                    listener.onStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isDrawingLottery = false;
                if (listener != null) {
                    listener.onEnd(position, mNamesStrs.get(position));
                }
            }
        });
        animator.start();
    }

    /**
     * 转盘滚动终点随机停止的位置
     *
     * @return
     */
    public float getRandomPositionPro() {
        float num = (float) Math.random();
        if (num > 0 && num < 1) {
            return num;
        } else {
            return (float) 0.5;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    private class TurntableGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            //如果正在抽奖，则不可以手势滑动
            if (isDrawingLottery) {
                return false;
            }
            if (mOnFlingAnimator != null) {
                mOnFlingAnimator.cancel();
            }
            mStartX = e.getX();
            mStartY = e.getY();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i(TAG, "onScroll");
            float e2X = e2.getX();
            float e2Y = e2.getY();

            float xStart = mStartX - mCenterX;
            float yStart = mCenterY - mStartY;
            double distancestart = Math.sqrt(xStart * xStart + yStart * yStart);
            //计算移动点到圆心的距离
            float xMove = e2X - mCenterX;
            float yMove = mCenterY - e2Y;
            double distanceMove = Math.sqrt(xMove * xMove + yMove * yMove);
            double distanceMoveDz = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

            double cosValue = (distancestart * distancestart + distanceMove * distanceMove - distanceMoveDz * distanceMoveDz) / (2 * distancestart * distanceMove);
            //多指触控的时候会造成cosValue 大于1，这里控制一下
            if (cosValue > 1) {
                Log.i(TAG, "大于1" + cosValue);
                cosValue = 1;
            } else if (cosValue < -1) {
                Log.i(TAG, "小于1" + cosValue);
                cosValue = -1;
            }
            double acos = Math.acos(cosValue);
            double changeAngleDz = Math.toDegrees(acos);

            //大于0 顺时针，小于0 逆时针
            float value = (mStartX - mCenterX) * (e2Y - mCenterY) - (mStartY - mCenterY) * (e2X - mCenterX);
            if (value >= 0) {
                mCurrentAngle = (float) (mCurrentAngle + changeAngleDz);
                isClockwise = true;
            } else {
                mCurrentAngle = (float) (mCurrentAngle - changeAngleDz);
                isClockwise = false;
            }
            setRotate(mCurrentAngle);

            mStartX = e2X;
            mStartY = e2Y;
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, final MotionEvent e2, final float velocityX, final float velocityY) {
            float e2X = e2.getX();
            float e2Y = e2.getY();

            float dx = velocityX * (float) 0.005;
            float dy = velocityY * (float) 0.005;
            float xStart = mStartX - mCenterX;
            float yStart = mCenterY - mStartY;
            double distancestart = Math.sqrt(xStart * xStart + yStart * yStart);
            //计算移动点到圆心的距离
            float xMove = e2X - mCenterX;
            float yMove = mCenterY - e2Y;
            double distanceMove = Math.sqrt(xMove * xMove + yMove * yMove);
            double distanceMoveDz = Math.sqrt(dx * dx + dy * dy);

            double cosValue = (distancestart * distancestart + distanceMove * distanceMove - distanceMoveDz * distanceMoveDz) / (2 * distancestart * distanceMove);
            //多指触控的时候会造成cosValue 大于1，这里控制一下
            if (cosValue > 1) {
                cosValue = 1;
            } else if (cosValue < -1) {
                cosValue = -1;
            }
            double acos = Math.acos(cosValue);
            double changeAngleDz = Math.toDegrees(acos);

            if (isClockwise) {

            } else {
                //逆时针
                changeAngleDz = changeAngleDz * -1;
            }

            mOnFlingAnimator = ValueAnimator.ofFloat((float) changeAngleDz, 0);
            mOnFlingAnimator.setDuration(1000);
            mOnFlingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedX = (float) animation.getAnimatedValue();
                    mCurrentAngle = mCurrentAngle + animatedX;
                    setRotate(mCurrentAngle);
                }
            });

            mOnFlingAnimator.start();
            return true;
        }
    }


    /**
     * 设置转盘背景item的颜色
     *
     * @param colors
     */
    public void setBackColor(ArrayList<Integer> colors) {
        //转盘转动时候不能修改值
        if (isDrawingLottery) {
            return;
        }
        mColors.clear();
        mColors.addAll(colors);
        invalidate();
    }

    /**
     * 修改转盘基本数据
     *
     * @param num
     * @param names
     */
    public void setDatas(int num, List<String> names) {
        //转盘转动时候不能修改值
        if (isDrawingLottery) {
            return;
        }
        if (names != null && num > 1 && names.size() == num) {
            mPanNum = num;
            mOffsetAngle = (float) 360 / (float) mPanNum;
            mNamesStrs.clear();
            mNamesStrs.addAll(names);
            invalidate();
        }
    }

    /**
     * 单个扇形内是否随机停
     */
    public void setPositionRandom(boolean isPositionRandom) {
        this.isSectorPositionRandom = isPositionRandom;
    }

    public interface ITurntableListener {
        void onStart();

        void onEnd(int position, String name);
    }
}
