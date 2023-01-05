package com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zqw.mobile.grainfull.R;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker
 * @ClassName: ImageColorPickerView
 * @Description: 图片中提取颜色值
 * @Author: WLY
 * @CreateDate: 2023/1/5 11:28
 */
public class ImageColorPickerView extends View {
    // 句柄
    private Context mContext;
    // view高
    private int mHeight;
    // view宽
    private int mWidth;
    // 左侧宽度
    private int LEFT_WIDTH;
    // 移动时的图标
    private Bitmap mLocationBitmap;
    // 停止移动，选中后的图标
    private Bitmap mLocationPressBitmap;
    // 画笔
    private Paint mBitmapPaint;
    // 左侧选择点
    private PointF mLeftSelectPoint;
    // 颜色变更监听事件
    private OnColorChangedListener mChangedListener;
    // 是否移动中
    private boolean isMove = false;
    // 视图半径
    private float mBitmapRadius;
    // 渐变视图
    private Bitmap mGradualChangeBitmap;
    // 临时视图
    private Bitmap bitmapTemp;
    // 图片的宽度
    private int newWidgth;
    // 图片的高度
    private int newHeigh;

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
        bitmapTemp = BitmapFactory.decodeResource(getResources(), R.drawable.text_image);
        mBitmapPaint = new Paint();

        mLocationBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.front_back_switch);
        mLocationPressBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.front_back_switch);
        mBitmapRadius = mLocationBitmap.getWidth() / 2;
        mLeftSelectPoint = new PointF(0, 0);
        newWidgth = BitmapFactory.decodeResource(getResources(), R.drawable.text_image).getWidth();
        newHeigh = BitmapFactory.decodeResource(getResources(), R.drawable.text_image).getHeight();
    }

    // important patient please!!!
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(getGradual(), null, new Rect(0, 0, LEFT_WIDTH, mHeight), mBitmapPaint);

        // 是否移动
        if (isMove) {
            canvas.drawBitmap(mLocationBitmap, mLeftSelectPoint.x - mBitmapRadius, mLeftSelectPoint.y - mBitmapRadius, mBitmapPaint);
        } else {
            try {
                canvas.drawBitmap(mLocationPressBitmap, mLeftSelectPoint.x - mBitmapRadius, mLeftSelectPoint.y - mBitmapRadius, mBitmapPaint);
            } catch (Exception e) {
                // TODO: handle exception
            }
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
            mWidth = newHeigh;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = height;
        } else {
            mHeight = newHeigh;
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
                isMove = true;
                proofLeft(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                // 取色
                if (mChangedListener != null)
                    mChangedListener.onColorChanged(getLeftColor(x, y));
                isMove = false;
                invalidate();
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mGradualChangeBitmap != null && mGradualChangeBitmap.isRecycled() == false) {
            mGradualChangeBitmap.recycle();
        }
        if (mLocationBitmap != null && mLocationBitmap.isRecycled() == false) {
            mLocationBitmap.recycle();
        }
        if (mLocationPressBitmap != null && mLocationPressBitmap.isRecycled() == false) {
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
            mLeftSelectPoint.x = 0;
        } else if (x > (LEFT_WIDTH)) {
            mLeftSelectPoint.x = LEFT_WIDTH;
        } else {
            mLeftSelectPoint.x = x;
        }
        if (y < 0) {
            mLeftSelectPoint.y = 0;
        } else if (y > (mHeight - 0)) {
            mLeftSelectPoint.y = mHeight - 0;
        } else {
            mLeftSelectPoint.y = y;
        }
    }

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

        System.out.println("leftColor" + temp.getPixel(intX, intY));
        return temp.getPixel(intX, intY);
    }

    // ### 内部类 ###
    public interface OnColorChangedListener {
        void onColorChanged(int color);
    }
}
