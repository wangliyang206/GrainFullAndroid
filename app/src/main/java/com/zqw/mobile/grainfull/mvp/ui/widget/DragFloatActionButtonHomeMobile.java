package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.arms.utils.DeviceUtils;
import com.zqw.mobile.grainfull.R;

/**
 * 可移动悬浮按钮-首页电话
 */
public class DragFloatActionButtonHomeMobile extends RelativeLayout implements View.OnClickListener {

    private int parentHeight;
    private int parentWidth;

    // 未呼叫界面
    private TextView txviTips;
    private TextView txviPhone;
    // 呼叫界面
    private LinearLayout lilaCall;

    // 是否显示呼叫界面
    private boolean isShow;
    // 电话
    private String mContact;

    public DragFloatActionButtonHomeMobile(Context context) {
        this(context, null);
    }

    public DragFloatActionButtonHomeMobile(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragFloatActionButtonHomeMobile(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.view_homemobile_layout, this);

        findViewById(R.id.btn_viewhomemobilelayout_call).setOnClickListener(this);
        findViewById(R.id.btn_viewhomemobilelayout_cancel).setOnClickListener(this);
        txviTips = findViewById(R.id.txvi_viewhomemobilelayout_tips);
        txviPhone = findViewById(R.id.txvi_viewhomemobilelayout_phone);
        lilaCall = findViewById(R.id.lila_viewhomemobilelayout_call);

        txviTips.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txvi_viewhomemobilelayout_tips:                                               // 专属联系人
                // 点击后需要展开呼叫布局
                showView(true);
                break;
            case R.id.btn_viewhomemobilelayout_cancel:                                              // 取消呼叫
                showView(false);
                break;
            case R.id.btn_viewhomemobilelayout_call:                                                // 呼叫
                if (!TextUtils.isEmpty(mContact)) {
                    DeviceUtils.openDial(getContext(), mContact.trim());
                }
                break;
        }
    }

    public String getContact() {
        return mContact;
    }

    /**
     * 对外提供设置电话
     */
    public void setContact(String contact) {
        this.mContact = contact;
        if (TextUtils.isEmpty(mContact)) {
            this.txviPhone.setText("暂无联系电话");
        } else {
            this.txviPhone.setText("是否呼叫：" + mContact + "？");
        }

    }

    /**
     * 对外提供呼叫布局是否显示
     */
    public boolean getShow() {
        return isShow;
    }

    /**
     * 控制布局是否显示呼叫布局
     */
    public void showView(boolean isVal) {
        this.isShow = isVal;
        if (isVal) {
            txviTips.setVisibility(GONE);
            lilaCall.setVisibility(VISIBLE);
            // 向左边移入
            lilaCall.setAnimation(AnimationUtils.makeInAnimation(getContext(), false));
        } else {
            txviTips.setVisibility(VISIBLE);
            lilaCall.setVisibility(GONE);
        }
    }

    private int lastX;
    private int lastY;
    private boolean isDrag;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
                isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                ViewGroup parent;
                if (getParent() != null) {
                    parent = (ViewGroup) getParent();
                    parentHeight = parent.getHeight();
                    parentWidth = parent.getWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (parentHeight <= 0 || parentWidth == 0) {
                    isDrag = false;
                    break;
                } else {
                    isDrag = true;
                }
                //计算手指移动了多少
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                //这里修复一些华为手机无法触发点击事件
                int distance = (int) Math.sqrt(dx * dx + dy * dy);
                if (distance == 0) {
                    isDrag = false;
                    break;
                }
                float x = getX() + dx;
                float y = getY() + dy;
                //检测是否到达边缘 左上右下
                x = x < 0 ? 0 : x > parentWidth - getWidth() ? parentWidth - getWidth() : x;
                y = getY() < 0 ? 0 : getY() + getHeight() > parentHeight ? parentHeight - getHeight() : y;
                setX(x);
                setY(y);
                lastX = rawX;
                lastY = rawY;
//                Log.i("aa", "isDrag=" + isDrag + "getX=" + getX() + ";getY=" + getY() + ";parentWidth=" + parentWidth);
                break;
            case MotionEvent.ACTION_UP:
                if (!isNotDrag()) {
                    //恢复按压效果
                    setPressed(false);

                    //只允许靠右吸附
                    animate().setInterpolator(new DecelerateInterpolator())
                            .setDuration(500)
                            .xBy(parentWidth - getWidth() - getX())
                            .start();

//                    if (rawX >= parentWidth / 2) {
//                        // 翻转
//                        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "rotationY", 180f, 0f);
//                        objectAnimator.setDuration(500);
//                        objectAnimator.start();
//
//                        //靠右吸附
//                        animate().setInterpolator(new DecelerateInterpolator())
//                                .setDuration(500)
//                                .xBy(parentWidth - getWidth() - getX())
//                                .start();
//                    } else {
//                        // 翻转
//                        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "rotationY", 0f, 180f);
//                        objectAnimator.setDuration(500);
//                        objectAnimator.start();
//
//                        //靠左吸附
//                        ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
//                        oa.setInterpolator(new DecelerateInterpolator());
//                        oa.setDuration(500);
//                        oa.start();
//                    }
                }
                break;
        }
        //如果是拖拽则消s耗事件，否则正常传递即可。
        return !isNotDrag() || super.onTouchEvent(event);
//        return event.getAction() != MotionEvent.ACTION_UP && (isDrag || super.onTouchEvent(event));
    }

    private boolean isNotDrag() {
        return !isDrag && (getX() == 0
                || (getX() == parentWidth - getWidth()));
    }

}