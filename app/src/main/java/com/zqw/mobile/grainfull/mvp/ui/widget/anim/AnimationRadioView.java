package com.zqw.mobile.grainfull.mvp.ui.widget.anim;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.airbnb.lottie.LottieAnimationView;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.widget.anim
 * @ClassName: AnimationRadioView
 * @Description: 选中自动播放动画(Tab动态图标 ， 结合Lottie动画一起使用)
 * @Author: WLY
 * @CreateDate: 2023/7/13 15:06
 */
public class AnimationRadioView extends LottieAnimationView implements Checkable {
    private boolean checked;

    public AnimationRadioView(Context context) {
        super(context);
    }

    public AnimationRadioView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimationRadioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setChecked(boolean checked) {
        try {
            if (this.checked != checked) {
                this.checked = checked;
                if (this.isAnimating()) {
                    this.cancelAnimation();
                }

                if (checked) {
                    if (this.getSpeed() < 0.0f) {
                        this.reverseAnimationSpeed();
                    }

                    this.playAnimation();
                } else {
                    if (this.getSpeed() > 0.0f) {
                        this.reverseAnimationSpeed();
                    }

                    this.playAnimation();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        this.setChecked(!this.checked);
    }
}
