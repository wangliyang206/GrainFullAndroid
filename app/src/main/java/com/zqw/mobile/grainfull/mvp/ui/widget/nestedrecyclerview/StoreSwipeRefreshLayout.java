package com.zqw.mobile.grainfull.mvp.ui.widget.nestedrecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * 嵌套ParentRecyclerView后，下拉刷新控件
 */
public class StoreSwipeRefreshLayout extends SwipeRefreshLayout {
    private ParentRecyclerView mParentRecyclerView = null;

    public StoreSwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public StoreSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mParentRecyclerView == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child instanceof ParentRecyclerView) {
                    mParentRecyclerView = (ParentRecyclerView) child;
                    break;
                }
            }
        }
    }

    @Override
    public boolean canChildScrollUp() {
        return super.canChildScrollUp() || (mParentRecyclerView != null && mParentRecyclerView.isChildRecyclerViewCanScrollUp());
    }
}
