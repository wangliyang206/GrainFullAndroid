package com.zqw.mobile.grainfull.mvp.ui.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeActionBarInfo;
import com.zqw.mobile.grainfull.mvp.model.entity.NewHomeInfo;
import com.zqw.mobile.grainfull.mvp.ui.adapter.HomeActionBarAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.holder
 * @ClassName: HomeActionBarHolder
 * @Description: 操作栏
 * @Author: WLY
 * @CreateDate: 2023/7/14 15:49
 */
public class HomeActionBarHolder extends BaseHolder<NewHomeInfo> implements View.OnClickListener {
    @BindView(R.id.home_actionbar_item_layout)
    LinearLayout mLayout;

    @BindView(R.id.view_homeactionbaritemlayout_content)
    RecyclerView mRecyclerView;
    @BindView(R.id.rela_homeactionbaritemlayout_indicator)
    RelativeLayout relaIndicator;
    @BindView(R.id.view_homeactionbaritemlayout_line)
    View viewLine;

    private HomeActionBarAdapter mAdapter;
    private List<HomeActionBarInfo> mList;

    public HomeActionBarHolder(View itemView) {
        super(itemView);

        // 初始化
        mList = new ArrayList<>();
        mAdapter = new HomeActionBarAdapter(mList);
        ArmsUtils.configRecyclerView(mRecyclerView, new GridLayoutManager(itemView.getContext(), 2, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //当前RcyclerView显示区域的高度。水平列表屏幕从左侧到右侧显示范围
                int extent = recyclerView.computeHorizontalScrollExtent();

                //整体的高度，注意是整体，包括在显示区域之外的。
                int range = recyclerView.computeHorizontalScrollRange();

                //已经滚动的距离，为0时表示已处于顶部。
                int offset = recyclerView.computeHorizontalScrollOffset();

                //计算出溢出部分的宽度，即屏幕外剩下的宽度
                float maxEndX = range - extent;

                //计算比例
                float proportion = offset / maxEndX;

                int layoutWidth = relaIndicator.getWidth();
                int indicatorViewWidth = viewLine.getWidth();

                //可滑动的距离
                int scrollableDistance = layoutWidth - indicatorViewWidth;

                //设置滚动条移动
                viewLine.setTranslationX(scrollableDistance * proportion);
            }
        });
        mAdapter.setOnItemClickListener((view, viewType, data, position) -> {
            HomeActionBarInfo info = (HomeActionBarInfo) data;
            ArmsUtils.makeText(mLayout.getContext(), info.getName());
        });
    }

    @Override
    public void setData(@NotNull NewHomeInfo info, int position) {
        mList.clear();
        mList.addAll(info.getActionBarList());
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 动态设置Margin
     */
    private void setLayoutMargin(boolean isSet) {
        ConstraintLayout.LayoutParams layoutParam = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        if (isSet)
            layoutParam.setMargins(0, ConvertUtils.dp2px(10), 0, 0);
        else
            layoutParam.setMargins(0, 0, 0, 0);

        mLayout.setLayoutParams(layoutParam);
    }

    @Override
    protected void onRelease() {
        // super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(mRecyclerView);
        this.mLayout = null;
        this.relaIndicator = null;
        this.viewLine = null;

        this.mAdapter = null;
        this.mList = null;
    }
}
