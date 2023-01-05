package com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.zqw.mobile.grainfull.R;

import timber.log.Timber;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker
 * @ClassName: ImageColorPickerView
 * @Description: 图片中提取颜色值
 * @Author: WLY
 * @CreateDate: 2023/1/5 11:28
 */
public class ImageColorPickerView extends AppCompatImageView {
    // 句柄
    private final Context mContext;
    // view高
    private int mHeight;
    // view宽
    private int mWidth;
    // 左侧宽度
    private int LEFT_WIDTH;
    // 暗色图标
    private Bitmap mLocationBitmap;
    // 亮色图标
    private Bitmap mLocationPressBitmap;
    // 画笔
    private Paint mBitmapPaint;
    // 选中的位置
    private PointF mSelectPoint;
    // 颜色变更监听事件
    private OnColorChangedListener mChangedListener;
    // 判断是否亮色
    private boolean isLightColor = false;
    // 视图半径
    private float mBitmapRadius;
    // 渐变视图
    private Bitmap mGradualChangeBitmap;
    // 临时视图
    private Bitmap bitmapTemp;
    // 选中的颜色
    private int mSelectColor;
    // 初始后自动取一次
    private boolean initAutoObtain = true;

    public ImageColorPickerView(Context context) {
        this(context, null);
    }


    public ImageColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public void setOnColorChangedListenner(OnColorChangedListener listener) {
        mChangedListener = listener;
    }

    /**
     * 初始化资源与画笔
     */
    private void init() {
        mBitmapPaint = new Paint();

        mLocationBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_precise_target);
        mLocationPressBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_precise_target_press);
        mBitmapRadius = mLocationBitmap.getWidth() / 2;

        BitmapDrawable drawable = (BitmapDrawable) getDrawable();
        if (drawable == null) {
            // 默认图
            bitmapTemp = BitmapFactory.decodeResource(getResources(), R.drawable.text_image);
        } else {
            bitmapTemp = drawable.getBitmap();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSelectPoint = new PointF(w / 2, h / 2);
    }

    // important patient please!!!
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(getGradual(), null, new Rect(0, 0, LEFT_WIDTH, mHeight), mBitmapPaint);

        // 判断是否亮色
        if (isLightColor) {
            // 亮色用暗图标
            canvas.drawBitmap(mLocationBitmap, mSelectPoint.x - mBitmapRadius, mSelectPoint.y - mBitmapRadius, mBitmapPaint);
        } else {
            // 暗色用亮图标
            try {
                canvas.drawBitmap(mLocationPressBitmap, mSelectPoint.x - mBitmapRadius, mSelectPoint.y - mBitmapRadius, mBitmapPaint);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        // 判断是否自动获取
        if (initAutoObtain) {
            initAutoObtain = false;
            if (mChangedListener != null)
                mChangedListener.onColorChanged(getLeftColor(mSelectPoint.x, mSelectPoint.y));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = width;
        } else {
            mWidth = bitmapTemp.getWidth();
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = height;
        } else {
            mHeight = bitmapTemp.getHeight();
        }
        LEFT_WIDTH = mWidth;
        setMeasuredDimension(mWidth, mHeight);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 按下
            case MotionEvent.ACTION_MOVE:
                // 移动
                // 获取当前选中的取色
                mSelectColor = getLeftColor(x, y);
                setControlIcon(mSelectColor);
                if (mChangedListener != null)
                    mChangedListener.onColorChanged(mSelectColor);
                proofLeft(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                // 抬起
                Timber.i("#### x=" + x + "  y=" + y);
                invalidate();
        }
        return true;
    }

    /**
     * 根据当前颜色来控制应该显示哪个图标
     */
    private void setControlIcon(int mColor) {
        isLightColor = ColorUtils.isLightColor(mColor);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mGradualChangeBitmap != null && !mGradualChangeBitmap.isRecycled()) {
            mGradualChangeBitmap.recycle();
        }
        if (mLocationBitmap != null && !mLocationBitmap.isRecycled()) {
            mLocationBitmap.recycle();
        }
        if (mLocationPressBitmap != null && !mLocationPressBitmap.isRecycled()) {
            mLocationPressBitmap.recycle();
        }
        super.onDetachedFromWindow();
    }

    private Bitmap getGradual() {
        if (mGradualChangeBitmap == null) {
            Paint leftPaint = new Paint();
            leftPaint.setStrokeWidth(1);
            mGradualChangeBitmap = Bitmap.createBitmap(LEFT_WIDTH, mHeight, Bitmap.Config.RGB_565);
            mGradualChangeBitmap.eraseColor(Color.WHITE);
            Canvas canvas = new Canvas(mGradualChangeBitmap);
            canvas.drawBitmap(bitmapTemp, null, new Rect(0, 0, LEFT_WIDTH, mHeight), mBitmapPaint);
        }
        return mGradualChangeBitmap;
    }

    // 校正xy
    private void proofLeft(float x, float y) {
        if (x < 0) {
            mSelectPoint.x = 0;
        } else if (x > (LEFT_WIDTH)) {
            mSelectPoint.x = LEFT_WIDTH;
        } else {
            mSelectPoint.x = x;
        }
        if (y < 0) {
            mSelectPoint.y = 0;
        } else if (y > (mHeight - 0)) {
            mSelectPoint.y = mHeight - 0;
        } else {
            mSelectPoint.y = y;
        }
    }

    /**
     * 获取选中位置中的颜色
     */
    private int getLeftColor(float x, float y) {
        Bitmap temp = getGradual();
        // 为了防止越界
        int intX = (int) x;
        int intY = (int) y;
        if (intX < 0) intX = 0;
        if (intY < 0) intY = 0;
        if (intX >= temp.getWidth()) {
            intX = temp.getWidth() - 1;
        }
        if (intY >= temp.getHeight()) {
            intY = temp.getHeight() - 1;
        }

        Timber.i("leftColor=%s", temp.getPixel(intX, intY));
        return temp.getPixel(intX, intY);
    }

    // ### 内部类 ###
    public interface OnColorChangedListener {
        void onColorChanged(int color);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        this.bitmapTemp = bm;
        mGradualChangeBitmap = null;
        initAutoObtain = true;
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        this.bitmapTemp = ConvertUtils.drawable2Bitmap(drawable);
        mGradualChangeBitmap = null;
        initAutoObtain = true;
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        this.bitmapTemp = BitmapFactory.decodeResource(getResources(), resId, options);
        mGradualChangeBitmap = null;
        initAutoObtain = true;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmapTemp = bitmap;
        mGradualChangeBitmap = null;
        initAutoObtain = true;
    }
}
