package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.NewHomeInfo;
import com.zqw.mobile.grainfull.mvp.ui.holder.HomeActionBarHolder;
import com.zqw.mobile.grainfull.mvp.ui.holder.HomeNavigationHolder;
import com.zqw.mobile.grainfull.mvp.ui.holder.HomeTopHolder;
import com.zqw.mobile.grainfull.mvp.ui.widget.nestedrecyclerview.ChildRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.adapter
 * @ClassName: NewHomeAdapter
 * @Description: 首页2.0适配器
 * @Author: WLY
 * @CreateDate: 2023/7/14 15:18
 */
public class NewHomeAdapter extends DefaultAdapter<NewHomeInfo> {
    // 顶部模块
    private final int VIEW_TOP = 0;
    // 中间模块
    private final int VIEW_ACTIONBAR = 1;
    // 导航 + 订单
    private final int VIEW_NAVIGATION = 2;

    HomeNavigationHolder mHomeNavigationHolder;

    public NewHomeAdapter(List<NewHomeInfo> infos) {
        super(infos);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {                                                                        // 第0个位置是顶部模块
            return VIEW_TOP;
        } else if (position == 1) {                                                                 // 第1个位置是中间模块
            return VIEW_ACTIONBAR;
        } else {                                                                                    // 第2个位置是导航 + 订单
            return VIEW_NAVIGATION;
        }
    }

    @NotNull
    @Override
    public BaseHolder<NewHomeInfo> getHolder(@NotNull View v, int viewType) {
        if (viewType == VIEW_TOP) {                                                                 // 顶部模块
            return new HomeTopHolder(v);
        } else if (viewType == VIEW_ACTIONBAR) {                                                    // 中间模块
            return new HomeActionBarHolder(v);
        } else {                                                                                    // 导航 + 订单
            mHomeNavigationHolder = new HomeNavigationHolder(v);
            return mHomeNavigationHolder;
        }
    }

    @Override
    public void onBindViewHolder(BaseHolder<NewHomeInfo> holder, int position) {
//        ((PictureMosaicItemHolder) holder).initView(type);
//        holder.setData(mInfos.get(position), position);

        // 判断不同的ViewHolder做不同的处理
        if (holder instanceof HomeTopHolder) {
            holder.setData(mInfos.get(position), position);
        } else if (holder instanceof HomeActionBarHolder) {
            holder.setData(mInfos.get(position), position);
        } else if (holder instanceof HomeNavigationHolder) {
            holder.setData(mInfos.get(position), position);
        }
    }

    @Override
    public int getLayoutId(int viewType) {
        if (viewType == VIEW_TOP) {
            return R.layout.home_top_item_layout;
        } else if (viewType == VIEW_ACTIONBAR) {
            return R.layout.home_actionbar_item_layout;
        } else {
            return R.layout.home_tab_item_layout;
        }
    }

    public ChildRecyclerView getCurrentChildRecyclerView() {
        if (mHomeNavigationHolder != null) {
            return mHomeNavigationHolder.getCurrentChildRecyclerView();
        }
        return null;
    }
}
