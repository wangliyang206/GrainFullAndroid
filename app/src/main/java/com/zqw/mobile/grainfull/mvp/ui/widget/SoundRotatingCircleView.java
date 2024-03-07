package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.app.utils.VisualizerHelper;

/**
 * 音频可视化控件 - 动感の圆
 */
public class SoundRotatingCircleView extends View implements VisualizerHelper.OnVisualizerEnergyCallBack {
    //中心点
    private int centerX, centerY;
    //音频能量百分比
    private float energyPercent = 0;
    //旋转角度
    private float degress = 0f;
    //半径
    private int radius = ConvertUtils.dp2px(200);

    private final float maxDistance = ConvertUtils.dp2px(100);

    private Bitmap bitmap;
    private Matrix matrix = new Matrix();
    private Paint paint = new Paint();
    private RectF bitmapRect = new RectF();
    private ValueAnimator valueAnimator;

    public SoundRotatingCircleView(Context context) {
        super(context);
        init();
    }

    public SoundRotatingCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SoundRotatingCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setAntiAlias(true);
        // 先将图片缩放，然后将图片转成圆形
        bitmap = ImageUtils.toRound(CommonUtils.getScaleBitmap(radius, R.mipmap.icon_yunyun, getResources()));

        valueAnimator = ValueAnimator.ofFloat(0f, 359.9f);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setDuration(10000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                degress = (float) animation.getAnimatedValue();
            }
        });
        valueAnimator.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = ConvertUtils.dp2px(100);
        }
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = ConvertUtils.dp2px(100);
        }
        centerX = widthSize / 2;
        centerY = heightSize / 2;
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    public void setWaveData(byte[] data, float totalEnergy) {

        if (bitmap != null) {
            energyPercent = totalEnergy / (VisualizerHelper.isFFT ? 100f : 1000f);
            bitmapRect.left = centerX - 100;
            bitmapRect.top = centerY - 100;
            bitmapRect.right = centerX + 100;
            bitmapRect.bottom = centerY + 100;
        }
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.e("SSSSS", energyPercent + "");
        bitmapRect.left = bitmapRect.left - maxDistance;
        bitmapRect.top = bitmapRect.top - maxDistance;
        bitmapRect.right = bitmapRect.right + maxDistance;
        bitmapRect.bottom = bitmapRect.bottom + maxDistance;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            paint.setColor(Color.argb(VisualizerHelper.ALPHA * energyPercent, 100 + VisualizerHelper.RED * energyPercent, 120 + VisualizerHelper.GREEN * energyPercent, 150 + VisualizerHelper.BLUE * energyPercent));
        } else {
            paint.setColor(Color.argb((int) (VisualizerHelper.ALPHA * energyPercent), (int) (VisualizerHelper.ALPHA * energyPercent), (int) (100 + VisualizerHelper.RED * energyPercent), (int) (150 + VisualizerHelper.BLUE * energyPercent)));
        }
        canvas.drawArc(bitmapRect, 0, 360, false, paint);
        paint.setAlpha(VisualizerHelper.ALPHA);
        canvas.rotate(degress, centerX, centerY);
        canvas.translate(centerX - radius / 2, centerY - radius / 2);
        canvas.drawBitmap(bitmap, matrix, paint);
    }

}
