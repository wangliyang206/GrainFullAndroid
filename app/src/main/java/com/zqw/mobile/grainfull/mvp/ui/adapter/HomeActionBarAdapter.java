package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeActionBarInfo;
import com.zqw.mobile.grainfull.mvp.ui.holder.HomeActionBarItemHolder;

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
public class HomeActionBarAdapter extends DefaultAdapter<HomeActionBarInfo> {

    public HomeActionBarAdapter(List<HomeActionBarInfo> infos) {
        super(infos);
    }

    @NotNull
    @Override
    public BaseHolder<HomeActionBarInfo> getHolder(@NotNull View v, int viewType) {
        return new HomeActionBarItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.homeactionbar_item_layout;
    }
}
