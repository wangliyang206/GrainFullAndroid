package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.SevenStatistics;
import com.zqw.mobile.grainfull.mvp.ui.holder.SevenStatisticsItemHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 包名： com.zqw.mobile.aptitude.mvp.ui.adapter
 * 对象名： BankCardAdapter
 * 描述：七日统计适配器
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2021/1/20 16:53
 */

public class SevenStatisticsAdapter extends DefaultAdapter<SevenStatistics> {

    public SevenStatisticsAdapter(List<SevenStatistics> infos) {
        super(infos);
    }

    @NotNull
    @Override
    public BaseHolder<SevenStatistics> getHolder(@NotNull View v, int viewType) {
        return new SevenStatisticsItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.sevenstatistics_item_layout;
    }
}
