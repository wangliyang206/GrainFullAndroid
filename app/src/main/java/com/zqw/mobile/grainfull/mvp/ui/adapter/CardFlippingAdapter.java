package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.CardFlipping;
import com.zqw.mobile.grainfull.mvp.ui.holder.CardFlippingItemHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import timber.log.Timber;

/**
 * 包名： com.zqw.mobile.grainfull.mvp.ui.adapter
 * 对象名： CardFlippingAdapter
 * 描述：卡牌消消乐 适配器
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2023/7/10 16:53
 */

public class CardFlippingAdapter extends DefaultAdapter<CardFlipping> {

    // 控件高度
    private int viewHeight;

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    public CardFlippingAdapter(List<CardFlipping> infos) {
        super(infos);
    }

    @NotNull
    @Override
    public BaseHolder<CardFlipping> getHolder(@NotNull View v, int viewType) {
        return new CardFlippingItemHolder(v);
    }

    @Override
    public void onBindViewHolder(BaseHolder<CardFlipping> holder, int position) {
        ((CardFlippingItemHolder) holder).initView(viewHeight);
        holder.setData(mInfos.get(position), position);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.cardflipping_item_layout;
    }

    /**
     * 是否完成
     */
    public boolean isSucc() {
        // 未完成的数量
        Timber.i("####开始打印完成项");
        int notNum = 0;
        for (CardFlipping info : mInfos) {
            if (info.isDisappear()) {
                Timber.i("####=" + info.getId() + "###=" + info.getSign());
            } else {
                notNum++;
            }
        }
        Timber.i("####打印结束");
        return notNum == 0;
    }
}
