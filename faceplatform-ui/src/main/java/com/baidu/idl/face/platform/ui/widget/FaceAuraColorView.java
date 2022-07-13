/**
 * Copyright (C) 2017 Baidu Inc. All rights reserved.
 */
package com.baidu.idl.face.platform.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.baidu.idl.face.platform.model.FaceExtInfo;
import com.baidu.idl.face.platform.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 人脸炫彩View
 */
public class FaceAuraColorView extends View {

    private static final String TAG = FaceAuraColorView.class.getSimpleName();

    public static final float WIDTH_SPACE_RATIO = 0.28f;
    public static final float HEIGHT_RATIO = 0.1f;
    public static final float HEIGHT_EXT_RATIO = 0.2f;

    public static final int COLOR_ROUND = Color.parseColor("#FFA800");
    public int mColorBg = Color.parseColor("#F0F1F2");
    public static final int COLOR_ROUND_BORDER1 = Color.parseColor("#26171D24");
    public static final int COLOR_ROUND_BORDER2 = Color.parseColor("#0D171D24");

    // 关于炫彩活体相关
    // 扩散圆圈颜色
    // private int mDiffuseColor = Color.parseColor("#FF0000");
    // 扩散圆宽度
    // private int mDiffuseWidth = 10;
    private static final int DEFAULT_DIFFUSESPEED = 120;
    // 圆形扩散速度
    private int mDiffuseSpeed = DEFAULT_DIFFUSESPEED;
    // 背景透明度
    private int alphaBackground = 255;
    // 最大宽度
    private Integer mMaxWidth = 1920;
    // 是否正在扩散中
    private boolean mIsDiffuse;
    // 扩散圆半径集合
    private List<Integer> mWidths = new ArrayList<>();
    private Paint mDiffusePaint;

    private Paint mBGPaint;
    private Paint mFaceRoundPaint;

    private Rect mFaceRect;
    private Rect mFaceDetectRect;
    private Paint mTextSecondPaint;
    private Paint mTextTopPaint;
    private Paint mCircleBorderPaint1;
    private Paint mCircleBorderPaint2;

    private float mX;
    private float mY;
    private float mR;
    private String mTipSecondText;
    private String mTipTopText;

    public FaceAuraColorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mBGPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBGPaint.setColor(mColorBg);
        mBGPaint.setStyle(Paint.Style.FILL);
        mBGPaint.setAntiAlias(true);
        mBGPaint.setDither(true);

        mFaceRoundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFaceRoundPaint.setColor(COLOR_ROUND);
        mFaceRoundPaint.setStyle(Paint.Style.FILL);
        mFaceRoundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mFaceRoundPaint.setAntiAlias(true);
        mFaceRoundPaint.setDither(true);

        mTextSecondPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextSecondPaint.setColor(Color.parseColor("#666666"));
        mTextSecondPaint.setTextSize(DensityUtils.dip2px(getContext(), 16));
        mTextSecondPaint.setTextAlign(Paint.Align.CENTER);
        mTextSecondPaint.setAntiAlias(true);
        mTextSecondPaint.setDither(true);

        mTextTopPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextTopPaint.setColor(Color.parseColor("#000000"));
        mTextTopPaint.setTextSize(DensityUtils.dip2px(getContext(), 22));
        mTextTopPaint.setTextAlign(Paint.Align.CENTER);
        mTextTopPaint.setAntiAlias(true);
        mTextTopPaint.setFakeBoldText(true);
        mTextTopPaint.setDither(true);

        mDiffusePaint = new Paint();
        mDiffusePaint.setAntiAlias(true);
        mWidths.add(10);

        mCircleBorderPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleBorderPaint1.setColor(COLOR_ROUND_BORDER1);
        mCircleBorderPaint1.setStyle(Paint.Style.STROKE);
        mCircleBorderPaint1.setStrokeWidth(10);
        mCircleBorderPaint1.setAntiAlias(true);
        mCircleBorderPaint1.setDither(true);

        mCircleBorderPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleBorderPaint2.setColor(COLOR_ROUND_BORDER2);
        mCircleBorderPaint2.setStyle(Paint.Style.STROKE);
        mCircleBorderPaint2.setStrokeWidth(10);
        mCircleBorderPaint2.setAntiAlias(true);
        mCircleBorderPaint2.setDither(true);
    }

    public void setTextColor(int color) {
        mTextTopPaint.setColor(color);
        invalidate();
    }

    public void setTipTopText(String tipTopText) {
        mTipTopText = tipTopText;
        if (!TextUtils.isEmpty(tipTopText)) {
            invalidate();
        }
    }

    public void setTipSecondText(String tipSecondText) {
        mTipSecondText = tipSecondText;
        if (!TextUtils.isEmpty(tipSecondText)) {
            invalidate();
        }
    }

    /**
     * 设置背景色
     * @param colorBg 背景色
     */
    public void setColorBg(int colorBg) {
        if (colorBg == -1) {
            mColorBg = Color.parseColor("#F0F1F2");
            mBGPaint.setColor(mColorBg);
            invalidate();
            return;
        }
        mColorBg = colorBg;
        mBGPaint.setColor(mColorBg);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        float canvasWidth = right - left;
        float canvasHeight = bottom - top;
        // mMaxWidth = (int) canvasHeight;

        float x = canvasWidth / 2;
        float y = (canvasHeight / 2) - ((canvasHeight / 2) * HEIGHT_RATIO);
        float r = (canvasWidth / 2) - ((canvasWidth / 2) * WIDTH_SPACE_RATIO);

        if (mFaceRect == null) {
            mFaceRect = new Rect((int) (x - r),
                    (int) (y - r),
                    (int) (x + r),
                    (int) (y + r));
        }
        if (mFaceDetectRect == null) {
            float hr = r + (r * HEIGHT_EXT_RATIO);
            mFaceDetectRect = new Rect((int) (x - r),
                    (int) (y - hr),
                    (int) (x + r),
                    (int) (y + hr));
        }
        mX = x;
        mY = y;
        mR = r;
        mMaxWidth = (int) (canvasHeight - y);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // canvas.drawColor(Color.TRANSPARENT);
        // 设置背景透明度
        mBGPaint.setAlpha(alphaBackground);
        canvas.drawPaint(mBGPaint);
        alphaBackground -= 20;
        // 绘制扩散圆
        if (mIsDiffuse) {
            drawDiffuseCircle(canvas);
        }
        canvas.drawCircle(mX, mY, mR, mFaceRoundPaint);
        canvas.drawCircle(mX, mY, mR - 5, mCircleBorderPaint1);
        canvas.drawCircle(mX, mY, mR - 15, mCircleBorderPaint2);

        // 画文字
        if (!TextUtils.isEmpty(mTipSecondText)) {
            canvas.drawText(mTipSecondText, mX, mY - mR - 40 - 25 - 59, mTextSecondPaint);
        }
        if (!TextUtils.isEmpty(mTipTopText)) {
            canvas.drawText(mTipTopText, mX, mY - mR - 40 - 25 - 59 - 90, mTextTopPaint);
        }
    }

    // 绘制扩散圆
    private void drawDiffuseCircle(Canvas canvas) {
        // mDiffusePaint.setColor(mDiffuseColor);
        for (int i = 0; i < mWidths.size(); i++) {
            // 绘制扩散圆
            Integer width = mWidths.get(i);
            canvas.drawCircle(mX, mY, mR + width, mDiffusePaint);

            if (width < mMaxWidth){
                mWidths.set(i, width + mDiffuseSpeed);
                mDiffuseSpeed += 20;
            }

//            // 判断当扩散圆扩散到指定宽度时添加新扩散圆
//            if (mWidths.get(mWidths.size() - 1) >= mMaxWidth / mDiffuseWidth) {
//                mWidths.add(0);
//            }
//
//            // 超过10个扩散圆，删除最外层
//            if(mWidths.size() >= 10){
//                mWidths.remove(0);
//            }

            if (mIsDiffuse) {
                invalidate();
            }
        }
    }

    /**
     * 开始扩散
     */
    public void start(int color) {
        mWidths.clear();
        mWidths.add(0);
        alphaBackground = 255;
        mDiffuseSpeed = DEFAULT_DIFFUSESPEED;
        // mDiffuseColor = color;
        mDiffusePaint.setColor(color);
        mIsDiffuse = true;
        invalidate();
    }

    public void release() {
        mWidths.clear();
        mIsDiffuse = false;
    }
}