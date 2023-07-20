package com.zqw.mobile.grainfull.mvp.ui.holder;

import android.view.View;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeContentInfo;
import com.zqw.mobile.grainfull.mvp.model.entity.NewHomeInfo;
import com.zqw.mobile.grainfull.mvp.ui.adapter.HomeBannerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.holder
 * @ClassName: HomeTopHolder
 * @Description: 首页2.0顶部模块
 * @Author: WLY
 * @CreateDate: 2023/7/14 15:42
 */
public class HomeTopHolder extends BaseHolder<NewHomeInfo> implements View.OnClickListener {
    @BindView(R.id.home_top_item_layout)
    RelativeLayout mLayout;

    @BindView(R.id.revi_hometopitemlayout_bg)
    RecyclerView mRecyclerViewBg;
    @BindView(R.id.revi_hometopitemlayout_content)
    RecyclerView mRecyclerView;

    private HomeBannerAdapter mAdapter;
    private List<HomeContentInfo> mList;

    public HomeTopHolder(View itemView) {
        super(itemView);

        // 初始化
        mList = new ArrayList<>();
        mAdapter = new HomeBannerAdapter(mList);
        ArmsUtils.configRecyclerView(mRecyclerView, new GridLayoutManager(itemView.getContext(), 1, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setData(@NotNull NewHomeInfo info, int position) {
        mList.clear();
        mList.addAll(info.getTopList());
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 动态设置Margin
     */
    private void setLayoutMargin(boolean isSet) {
        ConstraintLayout.LayoutParams layoutParam = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        if (isSet)
            layoutParam.setMargins(ConvertUtils.dp2px(10), 0, 0, 0);
        else
            layoutParam.setMargins(0, 0, 0, 0);

        mLayout.setLayoutParams(layoutParam);
    }

    @Override
    protected void onRelease() {
        // super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(mRecyclerView);
        this.mLayout = null;

        this.mAdapter = null;
        this.mList = null;
    }
}