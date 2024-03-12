package com.zqw.mobile.grainfull.mvp.ui.widget;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zqw.mobile.grainfull.R;

import java.util.Random;


/**
 * ================================================
 * Description:
 * <p> 点击屏幕 点赞动效 - 抖音
 * ================================================
 */
public class DYLikeLayout extends FrameLayout {

    private Drawable icon;
    private Context context;
    private static int timeout = 300;//双击间四百毫秒延时
    private long currentTime = 0;//当前点击次数
    private LikeClickCallBack likeClickCallBack;


    public interface LikeClickCallBack {
        void onLikeListener();//连续点击两次的回调

        void onSingleListener();
    }

    public void setLikeClickCallBack(LikeClickCallBack likeClickCallBack) {
        this.likeClickCallBack = likeClickCallBack;
    }

    public DYLikeLayout(@NonNull Context context) {
        super(context);
        init(context);

    }

    public DYLikeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DYLikeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        icon = context.getResources().getDrawable(R.mipmap.ic_heart);
        //避免旋转时红心被遮挡
        setClipChildren(false);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            long newClickTime = System.currentTimeMillis();
            if (newClickTime - currentTime < timeout) {
                // 按下时在Layout中生成红心
                float x = event.getX();
                float y = event.getY();
                addHeartView(x, y);
                if (likeClickCallBack != null) {
                    likeClickCallBack.onLikeListener();
                }
            } else {
                if (likeClickCallBack != null) {
                    likeClickCallBack.onSingleListener();
                }
            }
            currentTime = newClickTime;
        }
        return super.onTouchEvent(event);
    }


    private void addHeartView(float x, float y) {
        LayoutParams lp = new LayoutParams(icon.getIntrinsicWidth(), icon.getIntrinsicHeight());   //计算点击的点位红心的下部中间
        lp.leftMargin = (int) (x - icon.getIntrinsicWidth() / 2);
        lp.topMargin = (int) (y - icon.getIntrinsicHeight());

        ImageView img = new ImageView(context);
        img.setScaleType(ImageView.ScaleType.MATRIX);
        Matrix matrix = new Matrix();
        matrix.postRotate(getRandomRotate());    //设置红心的微量偏移
        img.setImageMatrix(matrix);
        img.setImageDrawable(icon);
        img.setLayoutParams(lp);
        addView(img);
        AnimatorSet animSet = getShowAnimSet(img);
        AnimatorSet hideSet = getHideAnimSet(img);
        animSet.start();
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                hideSet.start();
            }
        });

        hideSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //动画结束移除红心
                removeView(img);
            }
        });

    }

    private AnimatorSet getHideAnimSet(ImageView view) {
        // 1.alpha动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.1f);
        // 2.缩放动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 2f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 2f);
        // 3.translation动画
        ObjectAnimator translation = ObjectAnimator.ofFloat(view, "translationY", 0f, -150f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(alpha, scaleX, scaleY, translation);
        animSet.setDuration(500);
        return animSet;

    }

    private AnimatorSet getShowAnimSet(ImageView view) {
        // 缩放动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.2f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(scaleX, scaleY);
        animSet.setDuration(100);
        return animSet;

    }

    private float getRandomRotate() {
        return new Random().nextInt(20) - 10;
    }
}
