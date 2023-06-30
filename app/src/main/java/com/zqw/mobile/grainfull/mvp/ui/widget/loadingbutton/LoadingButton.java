package com.zqw.mobile.grainfull.mvp.ui.widget.loadingbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.ui.widget.textview.DrawableTextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * SimpleDes:
 * Creator: Flod
 * Date: 2019-06-13
 * UseDes:
 * <p>
 * 1、改变loading的大小 √
 * 2、收缩动画后不居中 √
 * 3、收缩后的大小随loading的大小决定 √
 * 4、设置loading 可以设置为上下左右 √
 * 5、重复start的动画处理 √
 * 6、恢复动画还没结束时，点击收缩会变成恢复的状态 √
 * 7、正在显示EndDrawable时，再次点击start会变成恢复状态的加载
 * 先执行 beginChangeSize(true) 后执行 beginChangeSize(false); √
 * <p>
 * 8、多次start和end 会出错 √
 * 9、设置完Drawable大小后，start后再次设置rootView大小失控,是因为原来是wrap_content √
 * 10、start和compete同时按Loading没有关 √
 * 11、偶尔由onTouchEvent导致出现selector 状态异常，√
 * 12、收缩状态下，先点击Fail 再点击cancel，会再跑一遍收缩恢复 √
 * 13、收缩后定义形状 √
 * 14、设置按钮圆角 √
 */

@SuppressWarnings({"unused", "UnusedReturnValue", "RedundantSuppression"})
public class LoadingButton extends DrawableTextView {

    private int curStatus = STATUS.IDE;      //当前的状态

    interface STATUS {
        int IDE = 0;
        int SHRINKING = 1;
        int LOADING = 2;
        int END_DRAWABLE_SHOWING = 3;
        int RESTORING = 4;
    }


    //Arr
    private boolean enableShrink;            //是否开启收缩动画，                   默认开启
    private boolean enableRestore;           //完成时是否恢复按钮                   默认关闭
    private boolean disableClickOnLoading;   //Loading中禁用点击，                 默认开启

    private Drawable[] mDrawablesSaved;
    private int mDrawablePaddingSaved;
    private CharSequence mTextSaved;
    private boolean mEnableTextInCenterSaved;
    private final int[] mRootViewSizeSaved = new int[]{0, 0};

    private ValueAnimator mShrinkAnimator;
    private int mShrinkDuration;             //收缩和恢复的时间                     默认450ms
    private int mShrinkShape;                //收缩后的形状

    @IntDef({ShrinkShape.DEFAULT, ShrinkShape.OVAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ShrinkShape {
        int DEFAULT = 0;  //默认形状
        int OVAL = 1;     //圆形
    }


    private CircularProgressDrawable mLoadingDrawable;
    private OnStatusChangedListener mListener;
    private EndDrawable mEndDrawable;
    private int mLoadingSize;
    private int mLoadingPosition;

    private boolean isSizeChanging;          //当前布局尺寸正发生改变
    private boolean nextReverse;             //下一步是否是恢复动画
    private boolean isFail;                  //是否失败


    public LoadingButton(Context context) {
        super(context);
        init(context, null);
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        //getConfig
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton);

        disableClickOnLoading = array.getBoolean(R.styleable.LoadingButton_disableClickOnLoading, true);

        enableShrink = array.getBoolean(R.styleable.LoadingButton_enableShrink, true);
        enableRestore = array.getBoolean(R.styleable.LoadingButton_enableRestore, false);
        mShrinkDuration = array.getInt(R.styleable.LoadingButton_shrinkDuration, 450);
        mShrinkShape = array.getInt(R.styleable.LoadingButton_shrinkShape, ShrinkShape.DEFAULT);

        int loadingDrawableSize = array.getDimensionPixelSize(R.styleable.LoadingButton_loadingEndDrawableSize, (int) (enableShrink ? getTextSize() * 2 : getTextSize()));
        int loadingDrawableColor = array.getColor(R.styleable.LoadingButton_loadingDrawableColor, getTextColors().getDefaultColor());
        int loadingDrawablePosition = array.getInt(R.styleable.LoadingButton_loadingDrawablePosition, POSITION.START);
        int endSuccessDrawable = array.getResourceId(R.styleable.LoadingButton_endSuccessDrawable, -1);
        int endFailDrawableResId = array.getResourceId(R.styleable.LoadingButton_endFailDrawable, -1);
        int endDrawableAppearTime = array.getInt(R.styleable.LoadingButton_endDrawableAppearTime, EndDrawable.DEFAULT_APPEAR_DURATION);
        int endDrawableDuration = array.getInt(R.styleable.LoadingButton_endDrawableDuration, 900);

        array.recycle();

        //initLoadingDrawable
        mLoadingDrawable = new CircularProgressDrawable(context);
        mLoadingDrawable.setColorSchemeColors(loadingDrawableColor);
        mLoadingDrawable.setStrokeWidth(loadingDrawableSize * 0.14f);

        mLoadingSize = loadingDrawableSize;
        mLoadingPosition = loadingDrawablePosition;
        setDrawable(mLoadingPosition, mLoadingDrawable, loadingDrawableSize, loadingDrawableSize);
        setEnableCenterDrawables(true);

        //initLoadingDrawable
        if (endSuccessDrawable != -1 || endFailDrawableResId != -1) {
            mEndDrawable = new EndDrawable(endSuccessDrawable, endFailDrawableResId);
            mEndDrawable.mAppearAnimator.setDuration(endDrawableAppearTime);
            mEndDrawable.setKeepDuration(endDrawableDuration);
        }

        //initShrinkAnimator
        setUpShrinkAnimator();

        //initShrinkShape
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mShrinkShape > 0) {
                setClipToOutline(true);
                setOutlineProvider(new ShrinkShapeOutlineProvider());
            }

        }

        //Start|End -> true  Top|Bottom ->false
        setEnableTextInCenter(mLoadingPosition % 2 == 0);


        if (isInEditMode()) {
            mLoadingDrawable.setStartEndTrim(0, 0.8f);
        }

    }


    /**
     * 设置收缩动画，主要用来收缩和恢复布局的宽度，动画开始前会保存一些收缩前的参数（文字，其他Drawable等）
     */
    private void setUpShrinkAnimator() {
        mShrinkAnimator = ValueAnimator.ofFloat(0, 1f);
        mShrinkAnimator.setDuration(mShrinkDuration);
        mShrinkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // y = kx + b
                // b = getRootViewSize()
                // k = getRootViewSize() - getLoadingSize
                getLayoutParams().width = (int) ((getShrinkSize() - mRootViewSizeSaved[0]) * (float) animation.getAnimatedValue() + mRootViewSizeSaved[0]);
                getLayoutParams().height = (int) ((getShrinkSize() - mRootViewSizeSaved[1]) * (float) animation.getAnimatedValue() + mRootViewSizeSaved[1]);
                requestLayout();
            }
        });


        mShrinkAnimator.addListener(new AnimatorListenerAdapter() {

            //onAnimationStart(Animator animation, boolean isReverse) 在7.0测试没有调用fuck
            @Override
            public void onAnimationStart(Animator animation) {
                if (!nextReverse) {
                    //begin shrink
                    curStatus = STATUS.SHRINKING;
                    isSizeChanging = true;
                    if (mListener != null) {
                        mListener.onShrinking();
                    }

                    LoadingButton.super.setText("", TextView.BufferType.NORMAL);
                    setCompoundDrawablePadding(0);
                    setCompoundDrawablesRelative(mLoadingDrawable, null, null, null);
                    setEnableTextInCenter(false);

                } else {
                    //begin restore
                    stopLoading();
                    curStatus = STATUS.RESTORING;
                    if (mListener != null) {
                        mListener.onRestoring();
                    }

                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!nextReverse) {
                    //shrink over
                    curStatus = STATUS.LOADING;
                    startLoading();
                    nextReverse = true;

                } else {
                    //restore over
                    isSizeChanging = false;
                    nextReverse = false;
                    toIde();
                    if (mListener != null) {
                        mListener.onRestored();
                    }
                }
            }

        });

    }


    /**
     * 开始收缩或恢复
     *
     * @param isReverse true：恢复，且开始时停止Loading false：收缩，且结束时开始Loading
     * @param lastFrame 是否只显示最后一帧
     */
    private void beginShrink(boolean isReverse, boolean lastFrame) {
        if (mShrinkAnimator.isRunning()) {
            //如果上一个动画还在执行，就结束到最后一帧
            mShrinkAnimator.end();
        }
        this.nextReverse = isReverse;
        if (!isReverse) {
            mShrinkAnimator.start();

        } else {
            mShrinkAnimator.reverse();

        }
        if (lastFrame) {
            mShrinkAnimator.end();
        }

    }



    /**
     * 保存一些即将被改变的数据或状态
     */
    private void saveStatus() {
        mTextSaved = getText();
        mDrawablesSaved = copyDrawables(true);
        mDrawablePaddingSaved = getCompoundDrawablePadding();
        mEnableTextInCenterSaved = isEnableTextInCenter();
    }

    /**
     * 恢复保存的状态
     */
    private void restoreStatus() {
        setText(mTextSaved);
        setCompoundDrawablePadding(mDrawablePaddingSaved);
        setCompoundDrawablesRelative(mDrawablesSaved[POSITION.START], mDrawablesSaved[POSITION.TOP], mDrawablesSaved[POSITION.END], mDrawablesSaved[POSITION.BOTTOM]);
        setEnableTextInCenter(mEnableTextInCenterSaved);
        getLayoutParams().width = mRootViewSizeSaved[0];
        getLayoutParams().height = mRootViewSizeSaved[1];
        requestLayout();

        addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                measureTextHeight();
                measureTextWidth();
                removeOnLayoutChangeListener(this);
            }
        });

    }

    private void toIde() {
        curStatus = STATUS.IDE;
        restoreStatus();
        isFail = false;

    }

    /**
     * 如果disableClickOnLoading==true，且不是闲置状态，点击会无效
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //disable click
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && disableClickOnLoading && curStatus != STATUS.IDE)
            return true;
        return super.onTouchEvent(event);
    }


    /**
     * 开始加载
     */
    private void startLoading() {
        curStatus = STATUS.LOADING;

        if (!mLoadingDrawable.isRunning()) {
            mLoadingDrawable.start();
        }

        if (mListener != null) {
            mListener.onLoadingStart();
        }
    }

    /**
     * 停止加载
     */
    private void stopLoading() {
        if (mLoadingDrawable.isRunning()) {
            mLoadingDrawable.stop();
            if (mListener != null) {
                mListener.onLoadingStop();
            }
        }

    }

    /**
     * 取消当前所有的动画进程
     *
     * @param withRestoreAnim 是否显示恢复动画
     */
    private void cancelAllRunning(boolean withRestoreAnim) {

        switch (curStatus) {
            case STATUS.SHRINKING:
                beginShrink(true, !withRestoreAnim);
                break;
            case STATUS.LOADING: {
                stopLoading();
                if (enableShrink) {
                    beginShrink(true, !withRestoreAnim);
                } else {
                    toIde();
                }
                break;
            }
            case STATUS.END_DRAWABLE_SHOWING:
                if (mEndDrawable != null) {
                    mEndDrawable.cancel(withRestoreAnim);
                } else {
                    beginShrink(true, !withRestoreAnim);
                }
                break;
            case STATUS.RESTORING:
                if (!withRestoreAnim)
                    mShrinkAnimator.end();
                else {
                    nextReverse = true;
                    mShrinkAnimator.reverse();
                }
                break;
        }

    }


    /**
     * 开始加载，默认禁用加载时的点击
     */
    public void start() {
        //cancel last loading
        cancelAllRunning(false);

        saveStatus();
        if (enableShrink) {
            beginShrink(false, false);
        } else {
            if (TextUtils.isEmpty(getText())) {
                setCompoundDrawablePadding(0);
            }
            startLoading();
        }
    }

    /**
     * 完成加载,显示对应的EndDrawable
     * <p>
     *
     * @param isSuccess 是否加载成功，将参数传递给回调{@link OnStatusChangedListener#onCompleted(boolean)} ()},
     */
    public void complete(boolean isSuccess) {
        stopLoading();
        if (mEndDrawable != null) {
            if (mShrinkAnimator.isRunning())
                mShrinkAnimator.end();

            mEndDrawable.show(isSuccess);
        } else {
            //No EndDrawable,enableShrink
            this.isFail = !isSuccess;
            if (enableShrink) {
                if (enableRestore)
                    beginShrink(true, curStatus != STATUS.LOADING);

                else {
                    if (mListener != null) {
                        mListener.onCompleted(isSuccess);
                    }
                }
            } else {
                //No EndDrawable,disableShrink
                if (mListener != null) {
                    mListener.onCompleted(isSuccess);
                }

                if (enableRestore)
                    toIde();
            }


        }
    }


    /**
     * 取消加载，默认显示恢复动画
     */
    public void cancel() {
        cancel(true);
    }

    /**
     * 取消加载
     *
     * @param withRestoreAnim 是否显示收缩动画
     */
    public void cancel(boolean withRestoreAnim) {
        if (curStatus != STATUS.IDE) {
            cancelAllRunning(withRestoreAnim);

            if (mListener != null)
                mListener.onCanceled();
        }
    }

    /**
     * 设置加载中不可点击
     */
    public LoadingButton setDisableClickOnLoading(boolean disable) {
        this.disableClickOnLoading = disable;
        return this;
    }

    /**
     * 设置是否显示收缩动画
     */
    public LoadingButton setEnableShrink(boolean enable) {
        this.enableShrink = enable;
        return this;
    }

    /**
     * 完成后是否恢复
     */
    public LoadingButton setEnableRestore(boolean enable) {
        this.enableRestore = enable;
        return this;
    }


    /**
     * 设置收缩后的形状
     *
     * @see ShrinkShape
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadingButton setShrinkShape(@ShrinkShape int shrinkShape) {
        this.mShrinkShape = shrinkShape;
        if (!(getOutlineProvider() instanceof ShrinkShapeOutlineProvider)) {
            setOutlineProvider(new ShrinkShapeOutlineProvider());
            setClipToOutline(true);
        } else
            invalidateOutline();

        return this;
    }

    /**
     * 获取收缩后的形状
     *
     * @see ShrinkShape
     */
    public int getShrinkShape() {
        return mShrinkShape;
    }

    /**
     * 收缩后的尺寸
     */
    public int getShrinkSize() {
        return Math.max(Math.min(mRootViewSizeSaved[0], mRootViewSizeSaved[1]), getLoadingEndDrawableSize());
    }

    /**
     * 收缩的Animator，可自行设置参数
     */
    public ValueAnimator getShrinkAnimator() {
        return mShrinkAnimator;
    }

    /**
     * 设置收缩的总时间
     */
    public LoadingButton setShrinkDuration(long milliseconds) {
        mShrinkAnimator.setDuration(milliseconds);
        return this;
    }

    /**
     * 收缩的总时间
     */
    public int getShrinkDuration() {
        return mShrinkDuration;
    }

    /**
     * 拿到CircularProgressDrawable 可自行设置想要的参数
     *
     * @return CircularProgressDrawable
     */
    public CircularProgressDrawable getLoadingDrawable() {
        return mLoadingDrawable;
    }


    /**
     * 设置LoadingDrawable的位置，如果开启收缩动画，则建议放Start或End
     *
     * @param position {@link DrawableTextView.POSITION}
     */
    public LoadingButton setLoadingPosition(@POSITION int position) {
        boolean enableTextInCenter = position % 2 == 0;
        setEnableTextInCenter(enableTextInCenter);
        mEnableTextInCenterSaved = enableTextInCenter;
        setDrawable(mLoadingPosition, null, 0, 0);
        mLoadingPosition = position;
        setDrawable(position, getLoadingDrawable(), getLoadingEndDrawableSize(), getLoadingEndDrawableSize());
        return this;
    }


    /**
     * 设置LoadingDrawable和EnaDrawable的尺寸
     */
    public LoadingButton setLoadingEndDrawableSize(@Px int px) {
        mLoadingSize = px;
        setDrawable(mLoadingPosition, mLoadingDrawable, px, px);
        return this;
    }


    public int getLoadingEndDrawableSize() {
        return mLoadingSize;
    }


    public LoadingButton setSuccessDrawable(@DrawableRes int drawableRes) {
        if (mEndDrawable == null)
            mEndDrawable = new EndDrawable(drawableRes, -1);
        else {
            mEndDrawable.setSuccessDrawable(drawableRes);
        }
        return this;
    }

    public LoadingButton setSuccessDrawable(Drawable drawable) {
        if (mEndDrawable == null)
            mEndDrawable = new EndDrawable(drawable, null);
        else {
            mEndDrawable.setSuccessDrawable(drawable);
        }
        return this;
    }

    public LoadingButton setFailDrawable(@DrawableRes int drawableRes) {
        if (mEndDrawable == null)
            mEndDrawable = new EndDrawable(-1, drawableRes);
        else {
            mEndDrawable.setFailDrawable(drawableRes);
        }
        return this;
    }

    public LoadingButton setFailDrawable(Drawable drawable) {
        if (mEndDrawable == null)
            mEndDrawable = new EndDrawable(null, drawable);
        else {
            mEndDrawable.setFailDrawable(drawable);
        }
        return this;
    }

    /**
     * EndDrawable 停留显示的时间
     */
    public LoadingButton setEndDrawableKeepDuration(long milliseconds) {
        if (mEndDrawable != null)
            mEndDrawable.setKeepDuration(milliseconds);
        return this;
    }

    public long getEndDrawableDuration() {
        if (mEndDrawable != null)
            return mEndDrawable.mKeepDuration;
        return EndDrawable.DEFAULT_APPEAR_DURATION;
    }


    /**
     * CompleteDrawable或FailDrawable 变大出现的时间
     */
    public LoadingButton setEndDrawableAppearDuration(long milliseconds) {
        if (mEndDrawable != null)
            mEndDrawable.getAppearAnimator().setDuration(milliseconds);
        return this;
    }

    @Nullable
    public ObjectAnimator getEndDrawableAnimator() {
        if (mEndDrawable != null) {
            return mEndDrawable.getAppearAnimator();
        }
        return null;
    }


    @Override
    public void setCompoundDrawablePadding(int pad) {
        super.setCompoundDrawablePadding(pad);
        if (curStatus == STATUS.IDE)
            mDrawablePaddingSaved = pad;
    }


    @Override
    public void setText(CharSequence text, TextView.BufferType type) {
        if (TextUtils.isEmpty(text) && curStatus != STATUS.IDE) {
            setCompoundDrawablePadding(0);
        }

        if (enableShrink && isSizeChanging) {
            return;
        }
        super.setText(text, type);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (curStatus == STATUS.IDE) {
            mRootViewSizeSaved[0] = getWidth();
            mRootViewSizeSaved[1] = getHeight();
        }
    }

    /**
     * 第一次Layout
     */
    @Override
    protected void onFirstLayout(int left, int top, int right, int bottom) {
        super.onFirstLayout(left, top, right, bottom);
        saveStatus();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mEndDrawable != null)
            mEndDrawable.draw(canvas);
        super.onDraw(canvas);
    }


    @SuppressWarnings("SameParameterValue")
    public class EndDrawable {
        private static final int DEFAULT_APPEAR_DURATION = 300;
        private Bitmap mSuccessBitmap;
        private Bitmap mFailBitmap;
        private Paint mPaint;
        private final Rect mBounds = new Rect();
        private Path mCirclePath;   //圆形裁剪路径
        private ObjectAnimator mAppearAnimator;
        private long mKeepDuration;
        private float animValue;
        int[] offsetTemp = new int[]{0, 0};
        private boolean isShowing;
        private Runnable mRunnable;

        private EndDrawable(@Nullable Drawable successDrawable, @Nullable Drawable failDrawable) {
            setSuccessDrawable(successDrawable);
            setFailDrawable(failDrawable);
            init();
        }

        private EndDrawable(@DrawableRes int successResId, @DrawableRes int failResId) {
            setSuccessDrawable(successResId);
            setFailDrawable(failResId);
            init();
        }

        private void init() {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
            mCirclePath = new Path();
            mAppearAnimator = ObjectAnimator.ofFloat(this, "animValue", 1.0f);
            mRunnable = new Runnable() {
                @Override
                public void run() {

                    if (enableShrink) {
                        if (enableRestore) {
                            setAnimValue(0);
                            beginShrink(true, !nextReverse);
                        }
                    } else {
                        if (enableRestore) {
                            setAnimValue(0);
                            toIde();
                        }
                    }

                    isShowing = false;
                }
            };
            mAppearAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    curStatus = STATUS.END_DRAWABLE_SHOWING;
                    if (mListener != null) {
                        mListener.onEndDrawableAppear(!isFail, mEndDrawable);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (isShowing) {
                        postDelayed(mRunnable, mKeepDuration);
                    }
                    if (mListener != null) {
                        mListener.onCompleted(!isFail);
                    }
                }
            });
        }


        /**
         * 显示EndDrawable
         */
        private void show(boolean isSuccess) {

            //end showing endDrawable
            if (isShowing) {
                cancel(false);
            }

            LoadingButton.this.isFail = !isSuccess;
            mAppearAnimator.start();
            isShowing = true;

        }

        /**
         * 取消出现动画
         *
         * @param withAnim 是否显示恢复动画
         */
        private void cancel(boolean withAnim) {
            isShowing = false;

            getHandler().removeCallbacks(mRunnable);
            if (mAppearAnimator.isRunning()) {
                mAppearAnimator.end();
            }

            if (enableShrink)
                beginShrink(true, !(withAnim && nextReverse));
            else {
                toIde();
            }
            setAnimValue(0);
        }

        /**
         * 消失动画，暂不使用
         */
        private void hide() {
            if (isShowing) {
                cancel(false);
            }
            mAppearAnimator.reverse();
            isShowing = true;
        }

        /**
         * 测量EndDrawable需要位移的offsetX和offsetY,(因为EnaDrawable一开始是在左上角开始显示的)
         *
         * @param canvas 当前画布
         * @param bounds LoadingDrawable的显示范围
         * @param pos    EndDrawable的显示位置
         * @return int[0] = offsetX，int[1] = offsetY
         */
        private int[] calcOffset(Canvas canvas, Rect bounds, @POSITION int pos) {
            final int[] offset = offsetTemp;
            offset[0] = canvas.getWidth() / 2;
            offset[1] = canvas.getHeight() / 2;

            switch (pos) {
                case POSITION.START: {
                    offset[0] -= (int) getTextWidth() / 2 + bounds.width() + getCompoundDrawablePadding();
                    if (enableShrink && nextReverse) {
                        offset[0] += bounds.width() / 2;
                    } else if (!isEnableTextInCenter()) {
                        offset[0] += (bounds.width() + getCompoundDrawablePadding()) / 2;
                    }

                    offset[1] -= bounds.height() / 2;
                    break;
                }
                case POSITION.TOP: {
                    offset[0] -= bounds.width() / 2;
                    offset[1] -= (int) getTextHeight() / 2 + bounds.height() + getCompoundDrawablePadding();
                    if (enableShrink && nextReverse) {
                        offset[1] += bounds.height() / 2;
                    } else if (!isEnableTextInCenter()) {
                        offset[1] += (bounds.height() + getCompoundDrawablePadding()) / 2;
                    }
                    break;
                }
                case POSITION.END: {
                    offset[0] += (int) getTextWidth() / 2 + getCompoundDrawablePadding();
                    if (enableShrink && nextReverse) {
                        offset[0] -= bounds.width() / 2;
                    } else if (!isEnableTextInCenter()) {
                        offset[0] -= (bounds.width() + getCompoundDrawablePadding()) / 2;
                    }
                    offset[1] -= bounds.height() / 2;
                    break;
                }
                case POSITION.BOTTOM: {
                    offset[0] -= bounds.width() / 2;
                    offset[1] += (int) getTextHeight() / 2 + getCompoundDrawablePadding();
                    if (enableShrink && nextReverse) {
                        offset[1] -= bounds.height() / 2;
                    } else if (!isEnableTextInCenter()) {
                        offset[1] -= (bounds.height() + getCompoundDrawablePadding()) / 2;
                    }
                    break;
                }
            }
            return offset;
        }

        /**
         * 绘制
         * <p>
         * 步骤:
         * 将画布平移到LoadingDrawable的位置 -> 裁剪出一个圆形画布（由小到大）-> 在裁剪后的绘制图形
         * ->随animValue值画布逐渐变大，实现出现的效果
         */
        private void draw(Canvas canvas) {
            if (getAnimValue() > 0 && mLoadingDrawable != null) {
                final Bitmap targetBitMap = isFail ? mFailBitmap : mSuccessBitmap;
                if (targetBitMap != null) {
                    final Rect bounds = mLoadingDrawable.getBounds();
                    mBounds.right = bounds.width();
                    mBounds.bottom = bounds.height();

                    final int[] offsets = calcOffset(canvas, mBounds, mLoadingPosition);
                    canvas.save();
                    canvas.translate(offsets[0], offsets[1]);
                    mCirclePath.reset();
                    mCirclePath.addCircle(mBounds.centerX(), mBounds.centerY(),
                            ((getLoadingEndDrawableSize() >> 1) * 1.5f) * animValue, Path.Direction.CW);
                    canvas.clipPath(mCirclePath);
                    canvas.drawBitmap(targetBitMap, null, mBounds, mPaint);
                    canvas.restore();
                }
            }
        }

        private void setAnimValue(float animValue) {
            this.animValue = animValue;
            invalidate();
        }

        public float getAnimValue() {
            return animValue;
        }

        /**
         * EndDrawable的Animator
         *
         * @return ObjectAnimator
         */
        public ObjectAnimator getAppearAnimator() {
            return mAppearAnimator;
        }

        /**
         * EndDrawable的停留时间
         *
         * @param keepDuration millionMs
         */
        public void setKeepDuration(long keepDuration) {
            this.mKeepDuration = keepDuration;
        }


        public void setSuccessDrawable(Drawable drawable) {
            mSuccessBitmap = getBitmap(drawable);
        }

        public void setSuccessDrawable(@DrawableRes int id) {
            if (id != -1) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), id);
                mSuccessBitmap = getBitmap(drawable);
            }
        }

        public void setFailDrawable(@DrawableRes int id) {
            if (id != -1) {
                Drawable failDrawable = ContextCompat.getDrawable(getContext(), id);
                mFailBitmap = getBitmap(failDrawable);
            }
        }

        public void setFailDrawable(Drawable drawable) {
            mSuccessBitmap = getBitmap(drawable);
        }

    }

    @Nullable
    private Bitmap getBitmap(Drawable drawable) {
        if (drawable != null) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
        return null;
    }


    @Override
    protected void onDetachedFromWindow() {
        //release
        mShrinkAnimator.cancel();
        mLoadingDrawable.stop();
        if (mEndDrawable != null)
            mEndDrawable.mAppearAnimator.cancel();

        super.onDetachedFromWindow();

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public class ShrinkShapeOutlineProvider extends RadiusViewOutlineProvider {
        @Override
        public void getOutline(View view, Outline outline) {
            if (enableShrink && mShrinkShape == ShrinkShape.OVAL
                    && (curStatus == STATUS.LOADING || curStatus == STATUS.END_DRAWABLE_SHOWING)) {
                outline.setOval(0, 0, getShrinkSize(), getShrinkSize());
            } else {
                super.getOutline(view, outline);
            }

        }
    }


    /**
     * 状态回调
     * start -> onShrinking ->  onLoadingStart -> complete -> onCompleted
     * -> onLoadingStop -> onEndDrawableAppear -> onRestoring -> onRestored
     */
    public static class OnStatusChangedListener {

        public void onShrinking() {

        }

        public void onLoadingStart() {

        }

        public void onLoadingStop() {

        }

        public void onEndDrawableAppear(boolean isSuccess, EndDrawable endDrawable) {

        }

        public void onRestoring() {

        }

        public void onRestored() {

        }
        public void onCompleted(boolean isSuccess) {

        }

        public void onCanceled() {

        }
    }

    public LoadingButton setOnStatusChangedListener(OnStatusChangedListener listener) {
        mListener = listener;
        return this;
    }


}
