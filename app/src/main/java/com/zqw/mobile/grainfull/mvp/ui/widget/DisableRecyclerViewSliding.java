package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;

import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * 禁止RecyclerView上下滚动/滑动
 */
public class DisableRecyclerViewSliding extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public DisableRecyclerViewSliding(Context context) {
        super(context);
    }

    public DisableRecyclerViewSliding(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public DisableRecyclerViewSliding(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}
