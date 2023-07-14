package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeContentInfo;
import com.zqw.mobile.grainfull.mvp.ui.holder.HomeContentItemHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.adapter
 * @ClassName: HomeContentAdapter
 * @Description: 首页 - 集合 -内容适配器
 * @Author: WLY
 * @CreateDate: 2023/7/14 17:25
 */
public class HomeContentAdapter extends DefaultAdapter<HomeContentInfo> {
    // 0 = 已派单；1 = 已收货；2 = 已入库；
    private int mType = 0;
    // 是否到底了
    private boolean showBottom;

    public HomeContentAdapter(List<HomeContentInfo> infos) {
        super(infos);
    }

    @NotNull
    @Override
    public BaseHolder<HomeContentInfo> getHolder(@NotNull View v, int viewType) {
        return new HomeContentItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.homecontent_item_layout;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public void setShowBottom(boolean showBottom) {
        this.showBottom = showBottom;
    }
}
