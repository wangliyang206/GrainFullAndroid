package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.GoodsEntity;
import com.zqw.mobile.grainfull.mvp.ui.holder.ProductDisplayItemHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 包名： com.zqw.mobile.aptitude.mvp.ui.adapter
 * 对象名： ProductDisplayAdapter
 * 描述：商品横向展示适配器
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2021/1/20 16:53
 */

public class ProductDisplayAdapter extends DefaultAdapter<GoodsEntity> {

    public ProductDisplayAdapter(List<GoodsEntity> infos) {
        super(infos);
    }

    @NotNull
    @Override
    public BaseHolder<GoodsEntity> getHolder(@NotNull View v, int viewType) {
        return new ProductDisplayItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.productdisplay_item_layout;
    }
}
