package com.zqw.mobile.grainfull.mvp.ui.widget.anim;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import timber.log.Timber;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.widget.anim
 * @ClassName: FlipCardAnimation
 * @Description: 翻转卡片动画
 * @Author: WLY
 * @CreateDate: 2023/7/11 11:48
 *
 * 使用方法：
 * int width = view.getWidth() / 2;
 *         int height = view.getHeight() / 2;
 *         FlipCardAnimation animationToFront = new FlipCardAnimation(0, 180, width, height);
 *         animationToFront.setInterpolator(new AnticipateOvershootInterpolator());
 *         animationToFront.setDuration(1500);
 *         animationToFront.setFillAfter(false);
 *         animationToFront.setRepeatCount(0);
 *         animationToFront.FlipDirection(false);
 *         animationToFront.setAnimationListener(new Animation.AnimationListener() {
 *             @Override
 *             public void onAnimationStart(Animation animation) {
 *             }
 *
 *             @Override
 *             public void onAnimationEnd(Animation animation) {
 *             }
 *
 *             @Override
 *             public void onAnimationRepeat(Animation animation) {
 *                 ((FlipCardAnimation) animation).setCanContentChange();
 *             }
 *         });
 *         animationToFront.setOnContentChangeListener(() -> {
 *             ImageView imviBg = view.findViewById(R.id.imvi_cardflippingitem_bg);
 *             ImageView imviContent = view.findViewById(R.id.imvi_cardflippingitem_content);
 *             imviBg.setImageResource(info.getImageBg());
 *             imviContent.setVisibility(View.VISIBLE);
 *             imviContent.setImageResource(info.getImageContent());
 *         });
 *         view.startAnimation(animationToFront);
 */
public class FlipCardAnimation extends Animation {
    private final float mFromDegrees;
    private final float mToDegrees;
    private final float mCenterX;
    private final float mCenterY;

    private Camera mCamera;
    //用于确定内容是否开始变化
    private boolean isContentChange = false;
    private OnContentChangeListener listener;
    // 翻转方向，默认上下翻转
    private boolean mDirection = true;

    public FlipCardAnimation(float fromDegrees, float toDegrees, float centerX, float centerY) {
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        mCenterX = centerX;
        mCenterY = centerY;
    }

    /**
     * 翻转方向，默认上下翻转
     */
    public void FlipDirection(boolean isUpsideDown) {
        mDirection = isUpsideDown;
    }

    //用于确定内容是否开始变化  在动画开始之前调用
    public void setCanContentChange() {
        this.isContentChange = false;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final float fromDegrees = mFromDegrees;
        float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);
        final float centerX = mCenterX;
        final float centerY = mCenterY;
        final Camera camera = mCamera;
        final Matrix matrix = t.getMatrix();
        camera.save();

        if (degrees > 90 || degrees < -90) {
            if (!isContentChange) {
                if (listener != null) {
                    listener.contentChange();
                }
                isContentChange = true;
            }

            if (degrees > 0) {
                degrees = 270 + degrees - 90;
            } else if (degrees < 0) {
                degrees = -270 + (degrees + 90);
            }
        }

        if (mDirection) {
            camera.rotateX(degrees);
        } else {
            camera.rotateY(degrees);
        }

        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }

    public void setOnContentChangeListener(OnContentChangeListener listener) {
        this.listener = listener;
    }

    public interface OnContentChangeListener {
        void contentChange();
    }
}
