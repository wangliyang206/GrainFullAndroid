package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.UmEvent;
import com.zqw.mobile.grainfull.mvp.ui.holder.UmEventItemHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 包名： com.zqw.mobile.aptitude.mvp.ui.adapter
 * 对象名： UmEventAdapter
 * 描述：事件 适配器
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2021/1/20 16:53
 */

public class UmEventAdapter extends DefaultAdapter<UmEvent> {

    public UmEventAdapter(List<UmEvent> infos) {
        super(infos);
    }

    @NotNull
    @Override
    public BaseHolder<UmEvent> getHolder(@NotNull View v, int viewType) {
        return new UmEventItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.umevent_item_layout;
    }
}
