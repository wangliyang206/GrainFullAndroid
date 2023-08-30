package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.widget
 * @ClassName: SpacesItemDecoration
 * @Description: RecyclerView分割线
 * @Author: WLY
 * @CreateDate: 2023/8/30 14:35
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space = 0;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = this.space;
        outRect.right = this.space;
        outRect.bottom = this.space;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = this.space;
        }
    }
}
