package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.ui.holder.SelectFinancialItemHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 包名： com.zqw.mobile.operation.mvp.ui.adapter
 * 对象名： SelectListAdapter
 * 描述：请选择
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2018/6/25 17:40
 */

public class SelectListAdapter extends DefaultAdapter<String> {
    public SelectListAdapter(List<String> infos) {
        super(infos);
    }

    public void setData(List<String> infos) {
        this.mInfos = infos;
    }

    @NotNull
    @Override
    public BaseHolder<String> getHolder(@NotNull View v, int viewType) {
        return new SelectFinancialItemHolder(v, getItemCount());
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.select_financial_item_layout;
    }
}
