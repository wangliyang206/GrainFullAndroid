package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 包名： com.zqw.mobile.recycling.ui
 * 对象名： ExpandableRecyclerView
 * 描述：解决ScrollView嵌套RecyclerView(横向)或ListView(横向)时，横向滑动不顺畅的问题。
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2018/5/15 17:52
 */

public class ExpandableRecyclerView extends RecyclerView {

    public ExpandableRecyclerView(Context context) {
        super(context);
    }

    public ExpandableRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private float lastX, lastY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        boolean intercept = super.onInterceptTouchEvent(e);

        switch (e.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastX = e.getX();
                lastY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 只要横向大于竖向，就拦截掉事件。
                // 部分机型点击事件（slopx==slopy==0），会触发MOVE事件。
                // 所以要加判断(slopX > 0 || sloy > 0)
                float slopX = Math.abs(e.getX() - lastX);
                float slopY = Math.abs(e.getY() - lastY);
                //  Log.log("slopX=" + slopX + ", slopY="  + slopY);
                if((slopX > 0 || slopY > 0) && slopX >= slopY){
                    requestDisallowInterceptTouchEvent(true);
                    intercept = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        // Log.log("intercept"+e.getAction()+"=" + intercept);
        return intercept;
    }

}