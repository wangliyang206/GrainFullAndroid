package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.zqw.mobile.grainfull.R;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.widget
 * @ClassName: RadarView
 * @Description: 雷达扫描控件(显示后自动旋转)
 * @Author: WLY
 * @CreateDate: 2024/2/28 12:01
 */
public class RadarView extends View {
    //雷达画笔
    private final Paint scanPaint = new Paint();
    //中心圆圈画笔
    private final Paint circlePaint = new Paint();

    //雷达扫描的半径
    private float scanCircleRadius = 0f;
    //中心圆圈半径
    private float circleRadius = 0f;
    //旋转角度
    private float mRoration = 0f;

    //阴影
    private Shader scanShader;
    //阴影矩阵
    private final Matrix scanMatrix = new Matrix();

    // 是否停止扫描
    private boolean stopScan = false;
    // 是否正向旋转
    private boolean isForwardRotation;

    public RadarView(Context context) {
        super(context);
        init();
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        // 雷达颜色
        scanPaint.setColor(getResources().getColor(R.color.colorPrimary, null));
        scanPaint.setAntiAlias(true);
        scanPaint.setStyle(Paint.Style.FILL);
        // 中心点的颜色
        circlePaint.setColor(Color.GREEN);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 开始扫描
     */
    public void startScan() {
        stopScan = false;
        invalidate();
    }

    /**
     * 停止扫描
     */
    public void stopScan() {
        stopScan = true;
    }

    public boolean isForwardRotation() {
        return isForwardRotation;
    }

    public void setForwardRotation(boolean forwardRotation) {
        isForwardRotation = forwardRotation;

        if (isForwardRotation) {
            // 正向
            mRoration = 0f;
        } else {
            // 反向
            mRoration = 360f;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        scanCircleRadius = w / 2f;
        //计算半径
        circleRadius = scanCircleRadius * 0.035f;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startScan();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopScan();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (isForwardRotation) {
            scanShader = new SweepGradient(getWidth() / 2f, getHeight() / 2f,
                    new int[]{Color.WHITE,getResources().getColor(R.color.colorPrimary)},new float[]{0f, 1f});
        }else {
            scanShader = new SweepGradient(getWidth() / 2f, getHeight() / 2f,
                    new int[]{getResources().getColor(R.color.colorPrimary), Color.WHITE},new float[]{0f, 1f});
        }

        scanPaint.setShader(scanShader);
        scanShader.setLocalMatrix(scanMatrix);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas != null) {
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, scanCircleRadius, scanPaint);
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, circleRadius, circlePaint);
            setRotation();
        }
    }

    /**
     * 改变旋转角度
     */
    public void setRotation() {
        if (isForwardRotation) {
            if (mRoration >= 360) {
                mRoration = 0f;
            }
            mRoration += 2;
        } else {
            if (mRoration <= 0) {
                mRoration = 360f;
            }
            mRoration -= 2;
        }
        scanMatrix.setRotate(mRoration, getWidth() / 2f, getHeight() / 2f);
        scanShader.setLocalMatrix(scanMatrix);
        if (!stopScan) {
            invalidate();
        }
    }
}
