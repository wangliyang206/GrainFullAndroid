package com.zqw.mobile.grainfull.mvp.ui.widget.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.Dimension;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;

import com.zqw.mobile.grainfull.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

/**
 * Creator: Flood
 * CreateDate: 2019-06-13
 * Update: 2020-09-06
 * Desc:
 * 1、继承{@link AppCompatTextView}
 * 2、可用于单独修改Drawable的宽度和高度
 * 3、文字在中心显示
 * 4、设置圆角（需要SDK_INT > 21）
 */
@SuppressWarnings({"UnusedReturnValue", "unused", "SameParameterValue", "RedundantSuppression"})
public class DrawableTextView extends AppCompatTextView {

    @IntDef({POSITION.START, POSITION.TOP, POSITION.END, POSITION.BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface POSITION {
        int START = 0;
        int TOP = 1;
        int END = 2;
        int BOTTOM = 3;
    }

    private Drawable[] mDrawables = new Drawable[]{null, null, null, null};
    private Rect[] mDrawablesBounds = new Rect[4];
    private float mTextWidth;
    private float mTextHeight;
    private boolean firstLayout;
    private int canvasTransX = 0, canvasTransY = 0;

    private boolean isCenterHorizontal;         //Gravity是否水平居中
    private boolean isCenterVertical;           //Gravity是否垂直居中
    private boolean enableCenterDrawables;      //drawable跟随文本居中
    private boolean enableTextInCenter;         //默认情况下文字与图片共同居中，开启后文字在最中间，图片紧挨
    private int radius;                         //四边圆角


    public DrawableTextView(Context context) {
        super(context);
        init(context, null);
    }

    public DrawableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Drawable[] drawables = getCompoundDrawablesRelative();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView);
        enableCenterDrawables = array.getBoolean(R.styleable.DrawableTextView_enableCenterDrawables, false);
        enableTextInCenter = array.getBoolean(R.styleable.DrawableTextView_enableTextInCenter, false);

        radius = array.getDimensionPixelSize(R.styleable.DrawableTextView_radius, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (radius > 0) {
                setClipToOutline(true);
                setOutlineProvider(new RadiusViewOutlineProvider());
            }
        }

        if (drawables[POSITION.START] != null) {
            Rect startBounds = drawables[POSITION.START].getBounds();
            startBounds.right = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableStartWidth, drawables[POSITION.START].getIntrinsicWidth());
            startBounds.bottom = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableStartHeight, drawables[POSITION.START].getIntrinsicHeight());
        }

        if (drawables[POSITION.TOP] != null) {
            Rect topBounds = drawables[POSITION.TOP].getBounds();
            topBounds.right = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableTopWidth, drawables[POSITION.TOP].getIntrinsicWidth());
            topBounds.bottom = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableTopHeight, drawables[POSITION.TOP].getIntrinsicHeight());
        }

        if (drawables[POSITION.END] != null) {
            Rect endBounds = drawables[POSITION.END].getBounds();
            endBounds.right = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableEndWidth, drawables[POSITION.END].getIntrinsicWidth());
            endBounds.bottom = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableEndHeight, drawables[POSITION.END].getIntrinsicHeight());
        }

        if (drawables[POSITION.BOTTOM] != null) {
            Rect bottomBounds = drawables[POSITION.BOTTOM].getBounds();
            bottomBounds.right = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableBottomWidth, drawables[POSITION.BOTTOM].getIntrinsicWidth());
            bottomBounds.bottom = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableBottomHeight, drawables[POSITION.BOTTOM].getIntrinsicHeight());
        }
        array.recycle();
        setCompoundDrawables(drawables[POSITION.START], drawables[POSITION.TOP], drawables[POSITION.END], drawables[POSITION.BOTTOM]);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (enableCenterDrawables) {
            final int absoluteGravity = Gravity.getAbsoluteGravity(getGravity(), getLayoutDirection());
            isCenterHorizontal = (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.CENTER_HORIZONTAL;
            isCenterVertical = (absoluteGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.CENTER_VERTICAL;
        }

        if (!firstLayout) {
            onFirstLayout(left, top, right, bottom);
            firstLayout = true;
        }
    }


    protected void onFirstLayout(int left, int top, int right, int bottom) {
        measureTextWidth();
        measureTextHeight();
    }

    /**
     * 在绘制前获取图片的Bounds，改变绘制的位置
     */
    @Override
    protected void onDraw(Canvas canvas) {

        if (enableCenterDrawables && (isCenterHorizontal | isCenterVertical)) {

            //有文字就才位移画布
            boolean textNoEmpty = !TextUtils.isEmpty(getText());
            //画布的偏移量
            int transX = 0, transY = 0;


            if (mDrawables[POSITION.START] != null && isCenterHorizontal) {
                Rect bounds = mDrawablesBounds[POSITION.START];
                int offset = (int) calcOffset(POSITION.START);
                mDrawables[POSITION.START].setBounds(bounds.left + offset, bounds.top,
                        bounds.right + offset, bounds.bottom);

                if (enableTextInCenter && textNoEmpty)
                    transX -= (mDrawablesBounds[POSITION.START].width() + getCompoundDrawablePadding()) >> 1;


            }

            if (mDrawables[POSITION.TOP] != null && isCenterVertical) {
                Rect bounds = mDrawablesBounds[POSITION.TOP];
                int offset = (int) calcOffset(POSITION.TOP);
                mDrawables[POSITION.TOP].setBounds(bounds.left, bounds.top + offset,
                        bounds.right, bounds.bottom + offset);

                if (enableTextInCenter && textNoEmpty)
                    transY -= (mDrawablesBounds[POSITION.TOP].height() + getCompoundDrawablePadding()) >> 1;


            }

            if (mDrawables[POSITION.END] != null && isCenterHorizontal) {
                Rect bounds = mDrawablesBounds[POSITION.END];
                int offset = -(int) calcOffset(POSITION.END);
                mDrawables[POSITION.END].setBounds(bounds.left + offset, bounds.top,
                        bounds.right + offset, bounds.bottom);

                if (enableTextInCenter && textNoEmpty)
                    transX += (mDrawablesBounds[POSITION.END].width() + getCompoundDrawablePadding()) >> 1;
            }

            if (mDrawables[POSITION.BOTTOM] != null && isCenterVertical) {
                Rect bounds = mDrawablesBounds[POSITION.BOTTOM];
                int offset = -(int) calcOffset(POSITION.BOTTOM);
                mDrawables[POSITION.BOTTOM].setBounds(bounds.left, bounds.top + offset,
                        bounds.right, bounds.bottom + offset);

                if (enableTextInCenter && textNoEmpty)
                    transY += (mDrawablesBounds[POSITION.BOTTOM].height() + getCompoundDrawablePadding()) >> 1;
            }


            if (enableTextInCenter && textNoEmpty) {
                canvas.translate(transX, transY);
                this.canvasTransX = transX;
                this.canvasTransY = transY;
            }
        }
        super.onDraw(canvas);
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        //再次平移回去
        canvas.translate(-canvasTransX, -canvasTransY);
        super.onDrawForeground(canvas);
    }

    /**
     * 计算drawable居中还需距离
     * 如果左右两边都有图片，左图片居中则需要加上右侧图片占用的空间{@link #getCompoundPaddingEnd()},其他同理
     *
     * @return 偏移量
     */
    private float calcOffset(@POSITION int position) {

        switch (position) {
            case POSITION.START:
            case POSITION.END:
                return (getWidth() - (getCompoundPaddingStart() + getCompoundPaddingEnd() + getTextWidth())) / 2;

            case POSITION.TOP:
            case POSITION.BOTTOM:
                return (getHeight() - (getCompoundPaddingTop() + getCompoundPaddingBottom() + getTextHeight())) / 2;

            default:
                return 0;

        }
    }

    protected int getCanvasTransX() {
        return canvasTransX;
    }

    protected int getCanvasTransY() {
        return canvasTransY;
    }


    /**
     * 测量文字的宽度，通过Paint测量所有文字的长度，
     * 但是这个数据不一定准确，文本还有可能换行，还需要通过{@link #getLineBounds}来获取文本的最大宽度
     */
    protected void measureTextWidth() {
        final Rect textBounds = new Rect();
        getLineBounds(0, textBounds);
        String text = "";
        if (getText() != null && getText().length() > 0) {
            text = getText().toString();
        } else if (getHint() != null && getHint().length() > 0) {
            text = getHint().toString();
        }
        final float width = getPaint().measureText(text);
        final float maxWidth = textBounds.width();
        mTextWidth = width <= maxWidth || maxWidth == 0 ? width : maxWidth;
    }

    /**
     * 获取文本的高度，通过{@link #getLineHeight}乘文本的行数
     */
    protected void measureTextHeight() {
        if ((getText() != null && getText().length() > 0)
                || (getHint() != null && getHint().length() > 0))
            mTextHeight = getLineHeight() * getLineCount();
        else
            mTextHeight = 0;
    }

    protected float getTextWidth() {
        return mTextWidth;
    }

    public float getTextHeight() {
        return mTextHeight;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        measureTextWidth();
        measureTextHeight();
    }

    /**
     * 设置Drawable，并设置宽高
     * 默认大小为Drawable的{@link Drawable#getBounds()} ,
     * 如果Bounds宽高为0则，取Drawable的内部固定尺寸{@link Drawable#getIntrinsicHeight()}
     *
     * @param position {@link POSITION}
     * @param drawable Drawable
     * @param width    Px
     * @param height   Px
     */
    public void setDrawable(@POSITION int position, @Nullable Drawable drawable, @Px int width, @Px int height) {
        mDrawables[position] = drawable;
        if (drawable != null) {
            Rect bounds = new Rect();
            if (width == -1 && height == -1) {
                if (drawable.getBounds().width() > 0 && drawable.getBounds().height() > 0) {
                    //如果Bounds宽高大于0,则保持默认
                    final Rect origin = drawable.getBounds();
                    bounds.set(origin.left, origin.top, origin.right, origin.bottom);
                } else {
                    //否则取Drawable的内部大小
                    bounds.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                }
            } else {
                bounds.right = width;
                bounds.bottom = height;
            }
            mDrawables[position].setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom);
            mDrawablesBounds[position] = bounds;
        }
        super.setCompoundDrawables(mDrawables[POSITION.START], mDrawables[POSITION.TOP], mDrawables[POSITION.END], mDrawables[POSITION.BOTTOM]);
    }


    @Override
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        storeDrawables(left, top, right, bottom);
    }

    @Override
    public void setCompoundDrawablesRelative(@Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        super.setCompoundDrawablesRelative(start, top, end, bottom);
        storeDrawables(start, top, end, bottom);
    }


    private void storeDrawables(@Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        if (mDrawables != null) {
            if (start != null && start != mDrawables[POSITION.START]) {
                mDrawablesBounds[POSITION.START] = start.copyBounds();
            }
            mDrawables[POSITION.START] = start;


            if (top != null && top != mDrawables[POSITION.TOP]) {
                mDrawablesBounds[POSITION.TOP] = top.copyBounds();
            }
            mDrawables[POSITION.TOP] = top;
            if (end != null && end != mDrawables[POSITION.END]) {
                mDrawablesBounds[POSITION.END] = end.copyBounds();
            }
            mDrawables[POSITION.END] = end;

            if (bottom != null && bottom != mDrawables[POSITION.BOTTOM]) {
                mDrawablesBounds[POSITION.BOTTOM] = bottom.copyBounds();
            }
            mDrawables[POSITION.BOTTOM] = bottom;
        }

    }


    protected Drawable[] copyDrawables(boolean clearOffset) {
        Drawable[] drawables = Arrays.copyOf(getDrawables(), 4);
        //clear offset
        if (clearOffset)
            clearOffset(drawables);

        return drawables;
    }

    private void clearOffset(Drawable... drawables) {
        for (Drawable drawable : drawables) {
            if (drawable != null) {
                Rect bounds = drawable.getBounds();
                bounds.offset(-bounds.left, -bounds.top);
            }
        }
    }

    protected int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public DrawableTextView setDrawableStart(Drawable drawableStart,
                                             @Dimension(unit = Dimension.DP) int width,
                                             @Dimension(unit = Dimension.DP) int height) {
        setDrawable(POSITION.START, drawableStart, dp2px(width), dp2px(height));
        return this;
    }

    public DrawableTextView setDrawableStart(Drawable drawableStart) {
        setDrawableStart(drawableStart, -1, -1);
        return this;
    }

    public DrawableTextView setDrawableTop(Drawable drawableTop,
                                           @Dimension(unit = Dimension.DP) int width,
                                           @Dimension(unit = Dimension.DP) int height) {
        setDrawable(POSITION.TOP, drawableTop, dp2px(width), dp2px(height));
        return this;
    }

    public DrawableTextView setDrawableTop(Drawable drawableTop) {
        setDrawableTop(drawableTop, -1, -1);
        return this;
    }

    public DrawableTextView setDrawableEnd(Drawable drawableEnd,
                                           @Dimension(unit = Dimension.DP) int width,
                                           @Dimension(unit = Dimension.DP) int height) {
        setDrawable(POSITION.END, drawableEnd, dp2px(width), dp2px(height));
        return this;
    }

    public DrawableTextView setDrawableEnd(Drawable drawableEnd) {
        setDrawableEnd(drawableEnd, -1, -1);
        return this;
    }


    public DrawableTextView setDrawableBottom(Drawable drawableBottom,
                                              @Dimension(unit = Dimension.DP) int width,
                                              @Dimension(unit = Dimension.DP) int height) {
        setDrawable(POSITION.BOTTOM, drawableBottom, dp2px(width), dp2px(height));
        return this;
    }

    public DrawableTextView setDrawableBottom(Drawable drawableBottom) {
        setDrawableBottom(drawableBottom, -1, -1);
        return this;
    }

    public DrawableTextView setEnableCenterDrawables(boolean enable) {
        if (enableCenterDrawables) {
            //清除之前的位移
            clearOffset(mDrawables);
        }
        this.enableCenterDrawables = enable;
        return this;
    }

    public DrawableTextView setEnableTextInCenter(boolean enable) {
        this.enableTextInCenter = enable;
        return this;
    }

    public boolean isEnableTextInCenter() {
        return enableTextInCenter;
    }

    public boolean isEnableCenterDrawables() {
        return enableCenterDrawables;
    }

    public Drawable[] getDrawables() {
        return mDrawables;
    }


    /**
     * 设置圆角，单位是PX
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DrawableTextView setRadius(@Px int px) {
        this.radius = px;
        if (!(getOutlineProvider() instanceof RadiusViewOutlineProvider)) {
            setOutlineProvider(new RadiusViewOutlineProvider());
            setClipToOutline(true);
        } else
            invalidateOutline();
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DrawableTextView setRadiusDP(@Dimension(unit = Dimension.DP) int dp) {
        return setRadius(dp2px(dp));
    }

    /**
     * 获取圆角大小，单位是PX
     */
    public int getRadius() {
        return radius;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public class RadiusViewOutlineProvider extends ViewOutlineProvider {
        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, getWidth(), getHeight(), radius);
        }
    }


}
