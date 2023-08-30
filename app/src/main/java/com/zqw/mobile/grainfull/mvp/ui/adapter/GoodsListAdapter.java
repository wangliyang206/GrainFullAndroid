package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.GoodsBean;
import com.zqw.mobile.grainfull.mvp.ui.holder.GoodsOneItemHolder;
import com.zqw.mobile.grainfull.mvp.ui.holder.GoodsTwoItemHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.adapter
 * @ClassName: GoodsListAdapter
 * @Description: 商品适配器
 * @Author: WLY
 * @CreateDate: 2023/8/30 12:04
 */
public class GoodsListAdapter extends DefaultAdapter<GoodsBean> {
    // 布局一
    private final int VIEW_ONE = 0;
    // 布局二
    private final int VIEW_TWO = 1;

    public GoodsListAdapter(List<GoodsBean> infos) {
        super(infos);
    }

    @NotNull
    @Override
    public BaseHolder<GoodsBean> getHolder(@NotNull View v, int viewType) {
        if (viewType == VIEW_ONE) {
            return new GoodsOneItemHolder(v);
        } else {
            return new GoodsTwoItemHolder(v);
        }
    }

    @Override
    public int getLayoutId(int viewType) {
        if (viewType == VIEW_ONE) {
            return R.layout.fragment_homegoods_item;
        } else {
            return R.layout.fragment_homegoodssecond_item;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mInfos.get(position).getType() == 1) {
            return VIEW_ONE;
        } else {
            return VIEW_TWO;
        }
    }
}
