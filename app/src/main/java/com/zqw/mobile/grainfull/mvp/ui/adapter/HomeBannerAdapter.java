package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeContentInfo;
import com.zqw.mobile.grainfull.mvp.ui.holder.HomeBannerItemHolder;

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
public class HomeBannerAdapter extends DefaultAdapter<HomeContentInfo> {

    public HomeBannerAdapter(List<HomeContentInfo> infos) {
        super(infos);
    }

    @NotNull
    @Override
    public BaseHolder<HomeContentInfo> getHolder(@NotNull View v, int viewType) {
        return new HomeBannerItemHolder(v);
    }

    @Override
    public void onBindViewHolder(BaseHolder<HomeContentInfo> holder, int position) {
        holder.setData(mInfos.get(position % mInfos.size()), position);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.homebanner_item_layout;
    }

    @Override
    public int getItemCount() {
        return mInfos.size() != 0 ? Integer.MAX_VALUE : 0;
    }
}
