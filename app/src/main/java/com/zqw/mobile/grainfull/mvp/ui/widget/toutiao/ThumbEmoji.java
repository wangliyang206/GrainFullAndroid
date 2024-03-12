package com.zqw.mobile.grainfull.mvp.ui.widget.toutiao;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.zqw.mobile.grainfull.R;

/**
 * 表情符号
 */
public class ThumbEmoji extends View {
    private boolean isRelease = true;
    private int height = ConvertUtils.dp2px(25);
    private int width = ConvertUtils.dp2px(25);
    public static final int DURATION = 500;
    /**
     * 所有随机表情
     */
    public static final int[] emojiArray = {R.drawable.emoji_1, R.drawable.emoji_2, R.drawable.emoji_3, R.drawable.emoji_4, R.drawable.emoji_5, R.drawable.emoji_6,
            R.drawable.emoji_7, R.drawable.emoji_8, R.drawable.emoji_9, R.drawable.emoji_10, R.drawable.emoji_11, R.drawable.emoji_12, R.drawable.emoji_13,
            R.drawable.emoji_14, R.drawable.emoji_15, R.drawable.emoji_16, R.drawable.emoji_17, R.drawable.emoji_18, R.drawable.emoji_19, R.drawable.emoji_20};
    private Bitmap bitmap;
    private Paint paint = new Paint();
    private AnimatorListener animatorListener;


    public ThumbEmoji(Context context) {
        this(context, null);
    }

    public ThumbEmoji(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThumbEmoji(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        metrics = context.getResources().getDisplayMetrics();
        paint.setAntiAlias(true);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inScaled = true;
        opt.inSampleSize = 2;
        opt.outWidth = width;
        opt.outHeight = height / 2;
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeResource(getResources(), emojiArray[(int) (Math.random() * emojiArray.length)], opt).copy(Bitmap.Config.ARGB_8888, true);
        bitmap.setDensity(getResources().getDisplayMetrics().densityDpi);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    Rect dst = new Rect();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            dst.left = 0;
            dst.top = 0;
            dst.right = width;
            dst.bottom = height;
            canvas.drawBitmap(bitmap, null, dst, paint);
        }
    }


    float topX;
    float topY;
    DisplayMetrics metrics;

    private void createPositionForXy() {
        topX = -(metrics.widthPixels - 200) + (float) ((metrics.heightPixels - 400) * Math.random());
        topY = -300 + (float) (-700 * Math.random());
    }


    AnimatorSet upAnimatorSet;
    //上升动画
    ObjectAnimator translateAnimationXUp, translateAnimationYUp;
    //表情缩放动画
    ObjectAnimator translateAnimationScaleXUp, translateAnimationScaleYUp;
    Animator.AnimatorListener upAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            createDownAnimationSet();
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

    };


    private void createAndStartUpAnimationSet() {
        if (upAnimatorListener != null && upAnimatorSet != null) {
            upAnimatorSet.removeAllListeners();
            upAnimatorSet.removeListener(upAnimatorListener);
        }
        /************************************************************************/
        //上升动画
        //抛物线动画 x方向
        translateAnimationXUp = ObjectAnimator.ofFloat(this, "translationX", 0, topX);
        translateAnimationXUp.setDuration(DURATION);
        translateAnimationXUp.setInterpolator(new LinearInterpolator());
        //y方向
        translateAnimationYUp = ObjectAnimator.ofFloat(this, "translationY", 0, topY);
        translateAnimationYUp.setDuration(DURATION);
        translateAnimationYUp.setInterpolator(new DecelerateInterpolator());
        /************************************************************************/
        //表情缩放动画
        translateAnimationScaleXUp = ObjectAnimator.ofFloat(this, "scaleX", 0.3f, 1f);
        translateAnimationScaleXUp.setDuration(DURATION);
        translateAnimationScaleYUp = ObjectAnimator.ofFloat(this, "scaleY", 0.3f, 1f);
        translateAnimationScaleYUp.setDuration(DURATION);
        /************************************************************************/
        //透明度
        setAlpha(1f);
        /************************************************************************/
        //动画集合
        upAnimatorSet = new AnimatorSet();
        upAnimatorSet.addListener(upAnimatorListener);
        upAnimatorSet
                .play(translateAnimationXUp)
                .with(translateAnimationYUp)
                .with(translateAnimationScaleXUp)
                .with(translateAnimationScaleYUp);
        upAnimatorSet.start();
    }

    AnimatorSet downAnimatorSet;
    //下降动画
    ObjectAnimator translateAnimationXDown, translateAnimationYDown;
    ObjectAnimator alphaAnimation;
    Animator.AnimatorListener downAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isRelease = true;
            animatorListener.onAnimationEmojiEnd();
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

    };

    private void createDownAnimationSet() {
        if (downAnimatorListener != null && downAnimatorSet != null) {
            downAnimatorSet.removeAllListeners();
            downAnimatorSet.removeListener(downAnimatorListener);
        }

        //下降动画
        //抛物线动画，原理：两个位移动画，一个横向匀速移动，一个纵向变速移动，两个动画同时执行，就有了抛物线的效果。
        //x方向
        translateAnimationXDown = ObjectAnimator.ofFloat(this, "translationX", topX, topX * 1.2f);
        translateAnimationXDown.setDuration(DURATION / 2);
        translateAnimationXDown.setInterpolator(new LinearInterpolator());
        //y方向
        translateAnimationYDown = ObjectAnimator.ofFloat(this, "translationY", topY, topY * 0.8f);
        translateAnimationYDown.setDuration(DURATION / 2);
        translateAnimationYDown.setInterpolator(new AccelerateInterpolator());
        /************************************************************************/
        //透明度
        alphaAnimation = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
        alphaAnimation.setDuration(DURATION / 2);
        /************************************************************************/
        //动画集合
        downAnimatorSet = new AnimatorSet();
        downAnimatorSet.addListener(downAnimatorListener);
        downAnimatorSet
                .play(translateAnimationXDown)
                .with(translateAnimationYDown)
                .with(alphaAnimation);
        downAnimatorSet.start();
    }

    public boolean isRelease() {
        return isRelease;
    }

    public void start() {
        if (isRelease()) {
            setTranslationX(0f);
            setTranslationY(0f);
            isRelease = false;
            createPositionForXy();
            createAndStartUpAnimationSet();
        }
    }

    public void setAnimatorListener(AnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
    }

    public interface AnimatorListener {
        void onAnimationEmojiEnd();
    }

}