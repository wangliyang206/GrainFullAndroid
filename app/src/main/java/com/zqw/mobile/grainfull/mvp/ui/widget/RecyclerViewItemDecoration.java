package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by Jackor on 2019/3/29.
 * Email: jackor.liao@foxmail.com
 * Description: 设置RecyclerView item边距
 * // 设置item间隔
 * recyclerView.addItemDecoration(new RecyclerViewItemDecoration(ArmsUtils.pix2dip(this, mItemGapWidth)));
 * recyclerView.setLayoutManager(layoutManager);
 */
public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public RecyclerViewItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.right = space;
    }

}
