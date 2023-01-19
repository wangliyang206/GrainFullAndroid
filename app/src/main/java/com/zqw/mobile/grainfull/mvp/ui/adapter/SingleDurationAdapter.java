package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.SingleDuration;
import com.zqw.mobile.grainfull.mvp.ui.holder.SingleDurationItemHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 包名： com.zqw.mobile.aptitude.mvp.ui.adapter
 * 对象名： SingleDurationAdapter
 * 描述：单次使用时长分布 适配器
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2021/1/20 16:53
 */

public class SingleDurationAdapter extends DefaultAdapter<SingleDuration> {

    public SingleDurationAdapter(List<SingleDuration> infos) {
        super(infos);
    }

    @NotNull
    @Override
    public BaseHolder<SingleDuration> getHolder(@NotNull View v, int viewType) {
        return new SingleDurationItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.singleduration_item_layout;
    }
}
