package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.GoodsEntity;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeActionBarInfo;
import com.zqw.mobile.grainfull.mvp.ui.holder.CardItemHolder;
import com.zqw.mobile.grainfull.mvp.ui.holder.ProductDisplayItemHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 包名： com.zqw.mobile.aptitude.mvp.ui.adapter
 * 对象名： CardAdapter
 * 描述：卡片展示适配器
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2021/1/20 16:53
 */

public class CardAdapter extends DefaultAdapter<HomeActionBarInfo> {

    public CardAdapter(List<HomeActionBarInfo> infos) {
        super(infos);
    }

    @NotNull
    @Override
    public BaseHolder<HomeActionBarInfo> getHolder(@NotNull View v, int viewType) {
        return new CardItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.cardoverlap_item_card;
    }
}
