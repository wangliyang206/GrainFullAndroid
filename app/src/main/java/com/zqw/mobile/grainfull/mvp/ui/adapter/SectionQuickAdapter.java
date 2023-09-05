package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.CategoryModal;
import com.zqw.mobile.grainfull.mvp.ui.holder.CategoryRightHeadItemHolder;
import com.zqw.mobile.grainfull.mvp.ui.holder.CategoryRightItemHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.adapter
 * @ClassName: SectionQuickAdapter
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/9/4 15:07
 */
public class SectionQuickAdapter extends DefaultAdapter<CategoryModal> {
    // 标题
    private final int VIEW_TITLE = 0;
    // 内容
    private final int VIEW_CONTENT = 1;

    public SectionQuickAdapter(List<CategoryModal> infos) {
        super(infos);
    }

    public void setData(List<CategoryModal> infos) {
        this.mInfos = infos;
    }

    public List<CategoryModal> getData() {
        return mInfos;
    }

    public CategoryModal getData(int position) {
        return mInfos.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        CategoryModal modal = getData(position);
        if (modal.isTitle()) {
            return VIEW_TITLE;
        } else {
            return VIEW_CONTENT;
        }
    }

    @NotNull
    @Override
    public BaseHolder<CategoryModal> getHolder(@NotNull View v, int viewType) {
        if (viewType == VIEW_TITLE) {
            return new CategoryRightHeadItemHolder(v);
        } else {
            return new CategoryRightItemHolder(v);
        }
    }

    @Override
    public int getLayoutId(int viewType) {
        if (viewType == VIEW_TITLE) {
            return R.layout.main_right_grid_header;
        } else {
            return R.layout.main_right_grid;
        }

    }
}