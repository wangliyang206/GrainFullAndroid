package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.ui.holder.PictureMosaicItemHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 包名： com.zqw.mobile.aptitude.mvp.ui.adapter
 * 对象名： BankCardAdapter
 * 描述：图片拼接适配器
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2021/1/20 16:53
 */

public class PictureMosaicAdapter extends DefaultAdapter<String> {
    // 展示类型：1代表纵图；2代表横图；3代表2列纵图；4代表3列纵图；
    private int type = 1;

    public PictureMosaicAdapter(List<String> infos) {
        super(infos);
    }

    @NotNull
    @Override
    public BaseHolder<String> getHolder(@NotNull View v, int viewType) {
        return new PictureMosaicItemHolder(v);
    }

    @Override
    public void onBindViewHolder(BaseHolder<String> holder, int position) {
        ((PictureMosaicItemHolder) holder).initView(type);
        holder.setData(mInfos.get(position), position);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.picturemosaic_item_layout;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
