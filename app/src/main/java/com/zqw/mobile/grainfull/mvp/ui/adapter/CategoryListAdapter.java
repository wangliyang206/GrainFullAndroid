package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.CategoryBean;
import com.zqw.mobile.grainfull.mvp.ui.holder.CategoryLeftItemHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.adapter
 * @ClassName: CategoryListAdapter
 * @Description: 分类左适配器
 * @Author: WLY
 * @CreateDate: 2023/9/5 9:16
 */
public class CategoryListAdapter extends DefaultAdapter<CategoryBean> {

    public CategoryListAdapter(List<CategoryBean> infos) {
        super(infos);
    }

    @NotNull
    @Override
    public BaseHolder<CategoryBean> getHolder(@NotNull View v, int viewType) {
        return new CategoryLeftItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.main_left_item;
    }
}
