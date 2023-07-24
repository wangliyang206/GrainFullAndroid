package com.zqw.mobile.grainfull.mvp.ui.holder;

import android.graphics.Rect;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.idl.face.platform.utils.DensityUtils;
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
import timber.log.Timber;

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

    private LinearLayoutManager mLayoutManager;
    private HomeBannerAdapter mAdapter;
    private List<HomeContentInfo> mList;

    public HomeTopHolder(View itemView) {
        super(itemView);

        // 初始化
        mList = new ArrayList<>();
        mAdapter = new HomeBannerAdapter(mList);
        mLayoutManager = new LinearLayoutManager(itemView.getContext(), RecyclerView.HORIZONTAL, false);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = DensityUtils.dip2px(itemView.getContext(), 10);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemCount = mLayoutManager.findFirstVisibleItemPosition();
                //一个itme的宽度
                double w = (ArmsUtils.getScreenWidth(itemView.getContext()) - DensityUtils.dip2px(itemView.getContext(), 65)) / 4;
                //第一个view
                View viewItem = mLayoutManager.findViewByPosition(firstVisibleItemCount);
                if (viewItem != null) {
                    double left = (double) (viewItem.getLeft() * 1.0F) / w;
                    setScroll(firstVisibleItemCount, left);
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 静止的时候 牵引效果
                if (newState == 0) {
                    double w = (ArmsUtils.getScreenWidth(itemView.getContext()) - DensityUtils.dip2px(itemView.getContext(), 16)) / 4;
                    int firstVisibleItemCount = mLayoutManager.findFirstVisibleItemPosition();
                    View findViewByPosition = mLayoutManager.findViewByPosition(firstVisibleItemCount);
                    if (findViewByPosition != null) {
                        int left = findViewByPosition.getLeft();
                        if (left == 0) {
                            return;
                        }
                        if ((float) left / w > -0.5f) {
                            recyclerView.smoothScrollBy(left, 0);
                        } else {
                            recyclerView.smoothScrollBy((int) (w + left), 0);
                        }
                    }
                }
            }
        });
    }

    /**
     * 设置滚动
     */
    private void setScroll(int firstVisibleItemCount, double left) {
        // 动态改变View的Margin
        setMargin(firstVisibleItemCount, left);
        setMargin1(firstVisibleItemCount + 1);
        setMargin1(firstVisibleItemCount + 2);
        setMargin1(firstVisibleItemCount + 3);
        setMargin2(firstVisibleItemCount + 4);
        setMargin2(firstVisibleItemCount + 5);
    }

    /**
     * Item 1 设置 Margin
     */
    private void setMargin(int lastVisibleItemCount, double left) {
        View viewItem = mLayoutManager.findViewByPosition(lastVisibleItemCount);
        if (viewItem != null) {
            Timber.i("###### item 1######Start");
            LinearLayout layoutBottom = viewItem.findViewById(R.id.lila_homebanneritemlayout_bottom);
            if (layoutBottom != null) {
                layoutBottom.setVisibility(View.GONE);
            }
            LinearLayout layoutTop = viewItem.findViewById(R.id.lila_homebanneritemlayout_top);
            if (layoutTop != null) {
                layoutTop.setVisibility(View.VISIBLE);
            }
            RecyclerView.LayoutParams layoupar = (RecyclerView.LayoutParams) viewItem.getLayoutParams();
            double w = (ArmsUtils.getScreenWidth(itemView.getContext()) - DensityUtils.dip2px(itemView.getContext(), 65)) / 4;

            layoupar.width = (int) (w - DensityUtils.dip2px(itemView.getContext(), 10));
            int w1 = layoupar.width;
            int w2 = (int) (w + DensityUtils.dip2px(itemView.getContext(), 30));
            layoupar.width = (int) (w2 - (w2 - w1) * Math.abs(left));
            layoupar.height = DensityUtils.dip2px(itemView.getContext(), 90);
            layoupar.topMargin = 50;
            viewItem.setLayoutParams(layoupar);
            Timber.i("###### item 1######End");
        }
    }

    /**
     * Item 2 ~ 4 设置 Margin
     */
    private void setMargin1(int lastVisibleItemCount) {
        View viewItem = mLayoutManager.findViewByPosition(lastVisibleItemCount);
        if (viewItem != null) {
            Timber.i("###### item 2######Start");
            LinearLayout layoutBottom = viewItem.findViewById(R.id.lila_homebanneritemlayout_bottom);
            if (layoutBottom != null) {
                layoutBottom.setVisibility(View.VISIBLE);
            }
            LinearLayout layoutTop = viewItem.findViewById(R.id.lila_homebanneritemlayout_top);
            if (layoutTop != null) {
                layoutTop.setVisibility(View.GONE);
            }
            RecyclerView.LayoutParams layoupar = (RecyclerView.LayoutParams) viewItem.getLayoutParams();
            double w = (ArmsUtils.getScreenWidth(itemView.getContext()) - DensityUtils.dip2px(itemView.getContext(), 65)) / 4;
            layoupar.width = (int) (w - DensityUtils.dip2px(itemView.getContext(), 10));
            layoupar.height = DensityUtils.dip2px(itemView.getContext(), 90);
            layoupar.topMargin = 50;
            viewItem.setLayoutParams(layoupar);
            Timber.i("###### item 2######End");
        }
    }

    /**
     * Item 5+ 设置 Margin
     */
    private void setMargin2(int lastVisibleItemCount) {
        View viewItem = mLayoutManager.findViewByPosition(lastVisibleItemCount);
        if (viewItem != null) {
            Timber.i("###### item 3######Start");
            LinearLayout layoutBottom = viewItem.findViewById(R.id.lila_homebanneritemlayout_bottom);
            if (layoutBottom != null) {
                layoutBottom.setVisibility(View.VISIBLE);
            }
            LinearLayout layoutTop = viewItem.findViewById(R.id.lila_homebanneritemlayout_top);
            if (layoutTop != null) {
                layoutTop.setVisibility(View.GONE);
            }
            RecyclerView.LayoutParams layoupar = (RecyclerView.LayoutParams) viewItem.getLayoutParams();
            double w = (ArmsUtils.getScreenWidth(itemView.getContext()) - DensityUtils.dip2px(itemView.getContext(), 65)) / 4;
            layoupar.width = (int) (w - DensityUtils.dip2px(itemView.getContext(), 10));
            layoupar.height = DensityUtils.dip2px(itemView.getContext(), 90);
            layoupar.topMargin = 50;
            viewItem.setLayoutParams(layoupar);
            Timber.i("###### item 3######End");
        }
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