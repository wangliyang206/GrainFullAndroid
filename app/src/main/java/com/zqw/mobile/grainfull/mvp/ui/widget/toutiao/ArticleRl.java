package com.zqw.mobile.grainfull.mvp.ui.widget.toutiao;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 第Rl条(仿头条点赞)
 */
public class ArticleRl extends RelativeLayout implements ThumbEmoji.AnimatorListener {
    private long lastClickTime;
    private Context mContext;
    private int currentNumber;
    private ThumbNumber thumbNumber;
    private List<ThumbEmoji> emojiList = new ArrayList<>();


    public ArticleRl(Context context) {
        this(context, null);
    }

    public ArticleRl(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArticleRl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    private void addThumbImage(Context context, float x, float y, ThumbEmoji.AnimatorListener animatorListener) {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins((int) x, (int) y - 50, 0, 0);
        if (emojiList.size() < 10) {
            ThumbEmoji articleThumb = new ThumbEmoji(context);
            articleThumb.setAnimatorListener(animatorListener);
            emojiList.add(articleThumb);
            this.addView(articleThumb, layoutParams);
            articleThumb.start();
        } else {
            for (ThumbEmoji thumbEmoji : emojiList) {
                if (thumbEmoji.isRelease()) {
                    thumbEmoji.setLayoutParams(layoutParams);
                    thumbEmoji.start();
                    break;
                }
            }
        }

    }


    public void setThumb(float x, float y) {
        if (System.currentTimeMillis() - lastClickTime > 800) {//单次点击
            addThumbImage(mContext, x, y, this);
            lastClickTime = System.currentTimeMillis();

            currentNumber = 0;
            if (thumbNumber != null) {
                removeView(thumbNumber);
                thumbNumber = null;
            }
        } else {
            //连续点击
            lastClickTime = System.currentTimeMillis();
            addThumbImage(mContext, x, y, this);

            currentNumber++;
            //添加数字连击view
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int) x - 300, (int) (y) - 300, 0, 150);
            if (thumbNumber == null) {
                thumbNumber = new ThumbNumber(mContext);
                addView(thumbNumber, layoutParams);
            }
            thumbNumber.setNumber(currentNumber);
        }
    }

    @Override
    public void onAnimationEmojiEnd() {

        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (thumbNumber != null && System.currentTimeMillis() - lastClickTime > 800) {
                    removeView(thumbNumber);
                    thumbNumber = null;
                }
            }
        }, 1000);
    }
}