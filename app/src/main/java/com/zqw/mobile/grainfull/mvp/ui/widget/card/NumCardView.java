package com.zqw.mobile.grainfull.mvp.ui.widget.card;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.IntRange;

import com.blankj.utilcode.util.ImageUtils;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.widget.card
 * @ClassName: NumCardView
 * @Description: 3D 卡片(Card)
 * @Author: WLY
 * @CreateDate: 2023/2/24 11:40
 */
public class NumCardView extends View {
    @NotNull
    private Camera mCamera;
    @NotNull
    private Paint mPaint;
    @NotNull
    private Matrix mMatrix;
    private float depthZ;
    private float rotateX;
    private float rotateY;
    private ArrayList<Bitmap> numBms;
    private Integer[] numBmIds;
    @IntRange(from = 0, to = 9)
    private int curShowNum;
    private float paddingSize;
    private float cardWidth;
    private float cardHeight;
    private float cardShadowSize;
    private float cardShadowDistance;
    private boolean isNeedDrawUpCard;
    private boolean isNeedDrawMidCard;
    private boolean isNeedDrawDownCard;
    @Nullable
    private IFunc cardRotateFunc;
    @Nullable
    private IFunc cardShadowSizeFunc;
    @Nullable
    private IFunc cardShadowDistanceFunc;
    private final int STATE_UP_ING;
    private final int STATE_DOWN_ING;
    private final int STATE_NORMAL;
    private int curState;
    private ValueAnimator cardRotateAnim;
    private float downX;
    private float downY;
    private float offsetY;

    public NumCardView(@Nullable Context context) {
        this(context, null);
    }

    public NumCardView(@Nullable Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mCamera = new Camera();
        this.mPaint = new Paint();
        this.mMatrix = new Matrix();
        this.numBms = new ArrayList<>();
        this.numBmIds = new Integer[]{R.drawable.num0, R.drawable.num1, R.drawable.num2,
                R.drawable.num3, R.drawable.num4, R.drawable.num5,
                R.drawable.num6, R.drawable.num7, R.drawable.num8,
                R.drawable.num9};
        this.paddingSize = ArmsUtils.dip2px(getContext(), 20);
        this.cardShadowSize = 10.0F;
        this.cardShadowDistance = 10.0F;
        this.isNeedDrawUpCard = true;
        this.isNeedDrawMidCard = true;
        this.isNeedDrawDownCard = true;
        this.STATE_UP_ING = 2;
        this.STATE_DOWN_ING = 3;
        this.STATE_NORMAL = 4;
        this.curState = this.STATE_NORMAL;
        this.initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setColor(Color.RED);
        this.initNumBms();
    }

    private void initNumBms() {
        for (int i = 0; i <= 9; i++) {
            this.numBms.add(ImageUtils.drawable2Bitmap(getResources().getDrawable(numBmIds[i])));
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.cardWidth = (float) this.getWidth() - this.paddingSize * (float) 2;
        this.cardHeight = (float) (this.getHeight() / 2) - this.paddingSize;

        this.configFunc();
    }

    private void configFunc() {
        this.cardRotateFunc = new CardRotateFunc();
        cardRotateFunc.setInParamMin(0.0F);
        cardRotateFunc.setInParamMax(this.cardHeight * (float) 2);
        cardRotateFunc.setOutParamMin(0.0F);
        cardRotateFunc.setOutParamMax(180.0F);
        cardRotateFunc.setInitValue(45.0F);

        this.cardShadowSizeFunc = new CardShadowSizeFunc();
        cardShadowSizeFunc.setInParamMin(0.0F);
        cardShadowSizeFunc.setInParamMax(180.0F);
        cardShadowSizeFunc.setOutParamMax(50.0F);
        cardShadowSizeFunc.setOutParamMin(0.0F);
        cardShadowSizeFunc.setInitValue(10.0F);

        this.cardShadowDistanceFunc = new CardShadowDistanceFunc();
        cardShadowDistanceFunc.setInParamMin(0.0F);
        cardShadowDistanceFunc.setInParamMax(180.0F);
        cardShadowDistanceFunc.setOutParamMax(50.0F);
        cardShadowDistanceFunc.setOutParamMin(0.0F);
        cardShadowDistanceFunc.setInitValue(10.0F);
    }

    private void executeFunc(float offset) {
        if (cardRotateFunc != null) {
            float rate = (cardRotateFunc.getOutParamMin() - cardRotateFunc.getOutParamMax()) / (cardRotateFunc.getInParamMax() - cardRotateFunc.getInParamMin());
            float initH = (cardRotateFunc.getOutParamMin() - cardRotateFunc.getOutParamMax() + cardRotateFunc.getInitValue()) / rate;
            this.rotateX = cardRotateFunc.execute(initH + offset);
        }

        this.executeShadowFunc(this.rotateX);
    }

    private void executeShadowFunc(float rotate) {
        if (cardShadowSizeFunc != null) {
            this.cardShadowSize = cardShadowSizeFunc.execute(rotate);
        }

        if (cardShadowDistanceFunc != null) {
            this.cardShadowDistance = cardShadowDistanceFunc.execute(rotate);
        }

    }

    private void resetInitValue() {
        if (cardRotateFunc != null) {
            cardRotateFunc.setInitValue(this.rotateX);
        }

        if (cardShadowSizeFunc != null) {
            cardShadowSizeFunc.setInitValue(this.cardShadowSize);
        }

    }

    protected void onDraw(@Nullable Canvas canvas) {
        super.onDraw(canvas);
        this.setLayerType(LAYER_TYPE_SOFTWARE, null);
        this.judgeState(this.curState);
        if (canvas != null) {
            this.drawUpCard(canvas);
            this.drawDownCard(canvas);
            this.drawMidCard(canvas);
        }

    }

    private void judgeState(int state) {
        if (state == this.STATE_NORMAL) {
            this.isNeedDrawMidCard = false;
            this.isNeedDrawUpCard = true;
            this.isNeedDrawDownCard = true;
        } else if (state == this.STATE_UP_ING) {
            this.isNeedDrawMidCard = true;
            if (this.curShowNum + 1 > 9) {
                this.isNeedDrawDownCard = false;
            }
        } else if (state == this.STATE_DOWN_ING) {
            this.isNeedDrawMidCard = true;
            if (this.curShowNum - 1 < 0) {
                this.isNeedDrawUpCard = false;
            }
        }

    }

    private void drawUpCard(Canvas canvas) {
        if (this.isNeedDrawUpCard) {
            this.mPaint.setShadowLayer(10.0F, 0.0F, 10.0F, Color.GRAY);
            this.mPaint.setColor(Color.WHITE);
            RectF rectF = new RectF(this.paddingSize, this.paddingSize, this.paddingSize + this.cardWidth, this.paddingSize + this.cardHeight);
            canvas.drawRoundRect(rectF, 20.0F, 20.0F, this.mPaint);
            this.mPaint.clearShadowLayer();

            Bitmap curNumBm = numBms.get(curShowNum);
            if (this.curState == this.STATE_DOWN_ING) {
                Bitmap tempBm;
                if (this.curShowNum - 1 >= 0) {
                    tempBm = this.numBms.get(this.curShowNum - 1);
                    canvas.drawBitmap(tempBm, new Rect(0, 0, tempBm.getWidth(), tempBm.getHeight() / 2), rectF, this.mPaint);
                }
            } else {
                canvas.drawBitmap(curNumBm, new Rect(0, 0, curNumBm.getWidth(), curNumBm.getHeight() / 2), rectF, this.mPaint);
            }

        }
    }

    private void drawMidCard(Canvas canvas) {
        if (this.isNeedDrawMidCard) {
            canvas.save();
            this.mMatrix.reset();
            this.mCamera.save();
            this.mCamera.translate(0.0F, 0.0F, this.depthZ);
            this.mCamera.rotateX(this.rotateX);
            this.mCamera.rotateY(this.rotateY);
            this.mCamera.getMatrix(this.mMatrix);
            this.mCamera.restore();
            float scale = getResources().getDisplayMetrics().density;
            float[] mValues = new float[9];
            this.mMatrix.getValues(mValues);
            mValues[6] /= scale;
            mValues[7] /= scale;
            this.mMatrix.setValues(mValues);
            this.mMatrix.preTranslate((float) (-canvas.getWidth()) / 2.0F, (float) (-canvas.getHeight()) / 2.0F);
            this.mMatrix.postTranslate((float) canvas.getWidth() / 2.0F, (float) canvas.getHeight() / 2.0F);
            canvas.concat(this.mMatrix);
            this.mPaint.setColor(Color.WHITE);
            this.mPaint.setShadowLayer(this.cardShadowSize, 0.0F, this.cardShadowDistance, Color.GRAY);
            RectF rectF = new RectF(this.paddingSize, this.paddingSize + this.cardHeight, this.paddingSize + this.cardWidth, this.paddingSize + this.cardHeight * (float) 2);
            canvas.drawRoundRect(rectF, 20.0F, 20.0F, this.mPaint);
            this.mPaint.clearShadowLayer();
            Bitmap curNumBm = this.numBms.get(this.curShowNum);

            if (this.rotateX >= 90.0F) {
                Matrix matrix = new Matrix();
                matrix.postRotate(180.0F);
                matrix.postScale(-1.0F, 1.0F);
                Bitmap tempBm = null;
                if (this.curState == this.STATE_UP_ING) {
                    if (this.curShowNum + 1 <= 9) {
                        tempBm = Bitmap.createBitmap(this.numBms.get(this.curShowNum + 1), 0, 0, curNumBm.getWidth(), curNumBm.getHeight(), matrix, false);
                    }
                } else if (this.curState == this.STATE_DOWN_ING) {
                    float var10 = cardRotateFunc.getInitValue() - this.rotateX;
                    if (Math.abs(var10) >= 90.0F) {
                        if (this.curShowNum - 1 >= 0) {
                            tempBm = Bitmap.createBitmap(this.numBms.get(this.curShowNum - 1), 0, 0, curNumBm.getWidth(), curNumBm.getHeight(), matrix, false);
                        } else {
                            tempBm = Bitmap.createBitmap(this.numBms.get(0), 0, 0, curNumBm.getWidth(), curNumBm.getHeight(), matrix, false);
                        }
                    } else {
                        tempBm = Bitmap.createBitmap(this.numBms.get(this.curShowNum), 0, 0, curNumBm.getWidth(), curNumBm.getHeight(), matrix, false);
                    }
                }

                if (tempBm != null) {
                    canvas.drawBitmap(tempBm, new Rect(0, tempBm.getHeight() / 2, tempBm.getWidth(), tempBm.getHeight()), rectF, this.mPaint);
                }
            } else {
                float var13 = cardRotateFunc.getInitValue() - this.rotateX;
                if (Math.abs(var13) >= 90.0F) {
                    Bitmap tempBm = null;
                    if (this.curShowNum - 1 >= 0) {
                        tempBm = this.numBms.get(this.curShowNum - 1);
                    }

                    if (tempBm != null) {
                        canvas.drawBitmap(tempBm, new Rect(0, tempBm.getHeight() / 2, tempBm.getWidth(), tempBm.getHeight()), rectF, this.mPaint);
                    }
                } else {
                    canvas.drawBitmap(curNumBm, new Rect(0, curNumBm.getHeight() / 2, curNumBm.getWidth(), curNumBm.getHeight()), rectF, this.mPaint);
                }
            }

            canvas.restore();
        }
    }

    private void drawDownCard(Canvas canvas) {
        if (this.isNeedDrawDownCard) {
            this.mPaint.setShadowLayer(10.0F, 0.0F, 10.0F, Color.GRAY);
            this.mPaint.setColor(Color.WHITE);
            RectF rectF = new RectF(this.paddingSize, this.paddingSize + this.cardHeight, this.paddingSize + this.cardWidth, this.paddingSize + this.cardHeight * (float) 2);
            canvas.drawRoundRect(rectF, 20.0F, 20.0F, this.mPaint);
            this.mPaint.clearShadowLayer();
            Bitmap curNumBm = this.numBms.get(this.curShowNum);
            if (this.curState == this.STATE_UP_ING) {
                Bitmap tempBm = this.numBms.get(this.curShowNum + 1);
                if (this.curShowNum + 1 <= 9) {
                    canvas.drawBitmap(tempBm, new Rect(0, tempBm.getHeight() / 2, tempBm.getWidth(), tempBm.getHeight()), rectF, this.mPaint);
                }
            } else {
                canvas.drawBitmap(curNumBm, new Rect(0, curNumBm.getHeight() / 2, curNumBm.getWidth(), curNumBm.getHeight()), rectF, this.mPaint);
            }

        }
    }

    private void startCardUpAnim(int curNum) {
        if (cardRotateAnim != null) {
            cardRotateAnim.cancel();
        }

        this.cardRotateAnim = ValueAnimator.ofFloat(new float[]{this.rotateX, 180.0F});
        cardRotateAnim.setDuration(400L);

        cardRotateAnim.addUpdateListener(animation -> {
            rotateX = (float) animation.getAnimatedValue();
            executeShadowFunc(rotateX);
            postInvalidate();
        });

        cardRotateAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                resetInitValue();
                curState = STATE_NORMAL;
                curShowNum = curNum;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        cardRotateAnim.start();
    }

    private void startCardDownAnim(int curNum) {
        if (cardRotateAnim != null) {
            cardRotateAnim.cancel();
        }

        this.cardRotateAnim = ValueAnimator.ofFloat(new float[]{this.rotateX, 0.0F});
        cardRotateAnim.setDuration(400L);
        cardRotateAnim.addUpdateListener(animation -> {
            rotateX = (float) animation.getAnimatedValue();
            executeShadowFunc(rotateX);
            postInvalidate();
        });

        cardRotateAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                resetInitValue();
                curState = STATE_NORMAL;
                curShowNum = curNum;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        cardRotateAnim.start();
    }

    public boolean onTouchEvent(@Nullable MotionEvent event) {
        if (event != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    this.downX = event.getX();
                    this.downY = event.getY();
                    if (this.downY >= (float) (this.getHeight() / 2)) {
                        this.rotateX = 0.0F;
                        this.curState = this.STATE_UP_ING;
                    } else {
                        this.rotateX = 180.0F;
                        this.curState = this.STATE_DOWN_ING;
                    }

                    this.resetInitValue();
                    this.postInvalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    this.offsetY = event.getY() - this.downY;
                    this.executeFunc(this.offsetY);
                    this.postInvalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    // 判断是上翻还是下翻
                    if (this.rotateX >= 90.0F) {
                        float var5 = cardRotateFunc.getInitValue() - this.rotateX;
                        if (Math.abs(var5) >= 90.0F) {
                            if (this.curShowNum + 1 <= 9) {
                                this.startCardUpAnim(this.curShowNum + 1);
                            } else {
                                this.curShowNum = 9;
                                this.startCardDownAnim(9);
                            }
                        } else {
                            this.startCardUpAnim(this.curShowNum);
                        }
                    } else {
                        float var5 = cardRotateFunc.getInitValue() - this.rotateX;
                        if (Math.abs(var5) >= 90.0F) {
                            if (this.curShowNum - 1 >= 0) {
                                this.startCardDownAnim(this.curShowNum - 1);
                            } else {
                                this.curShowNum = 0;
                                this.startCardUpAnim(0);
                            }
                        } else {
                            this.startCardDownAnim(this.curShowNum);
                        }
                    }

                    this.downX = 0.0F;
                    this.downY = 0.0F;
                    break;
            }
        }

        return true;
    }

}