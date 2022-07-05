package com.jess.arms.cj.colorful.setter;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 赤槿 on 2017/9/12.
 */

public class RecyclerViewSetter extends ViewGroupSetter
{

    public RecyclerViewSetter(ViewGroup targetView, int resId)
    {
        super(targetView, resId);
    }

    public RecyclerViewSetter(ViewGroup targetView)
    {
        super(targetView);
    }

    protected void clearRecyclerViewRecyclerBin(View rootView)
    {
        super.clearRecyclerViewRecyclerBin(rootView);
        ((RecyclerView) rootView).getRecycledViewPool().clear();
    }

}
