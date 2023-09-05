package com.zqw.mobile.grainfull.mvp.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerLayoutCategoryComponent;
import com.zqw.mobile.grainfull.mvp.contract.LayoutCategoryContract;
import com.zqw.mobile.grainfull.mvp.model.entity.CategoryBean;
import com.zqw.mobile.grainfull.mvp.model.entity.ContentCateResponse;
import com.zqw.mobile.grainfull.mvp.presenter.LayoutCategoryPresenter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.CategoryListAdapter;
import com.zqw.mobile.grainfull.mvp.ui.widget.CategoryRightView;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.fragment
 * @ClassName: LayoutCategoryFragment
 * @Description: 分类
 * @Author: WLY
 * @CreateDate: 2023/9/4 12:02
 */
public class LayoutCategoryFragment extends BaseFragment<LayoutCategoryPresenter> implements LayoutCategoryContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.fragment_layouthome_category)
    ConstraintLayout contentLayout;                                                                 // 主布局

    @BindView(R.id.categoryList)
    RecyclerView categoryList;                                                                      // 左边

    @BindView(R.id.rightContainer)
    SmartRefreshLayout rightContainer;
    @BindView(R.id.categoryRightView)
    CategoryRightView categoryRightView;                                                            // 右边

    /*------------------------------------------------业务区域------------------------------------------------*/
    @Inject
    LinearLayoutManager categoryLayoutManager;

    @Inject
    CategoryListAdapter categoryListAdapter;

    // recyclerView 可视区域高度 - 当前点击item的高度
    private int visualHeight = -1;

    public static LayoutCategoryFragment instantiate() {
        return new LayoutCategoryFragment();
    }

    @Override
    public void onDestroy() {
        // super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(categoryList);
        super.onDestroy();
        this.categoryList = null;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerLayoutCategoryComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layouthome_category, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        rightContainer.removeAllViews();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        // 初始化
        ArmsUtils.configRecyclerView(categoryList, categoryLayoutManager);
        categoryList.setAdapter(categoryListAdapter);
        categoryListAdapter.setOnItemClickListener(this);

        // 禁止下拉刷新和上拉加载更多
        rightContainer.setEnableRefresh(false);
        rightContainer.setEnableLoadMore(false);

        initData();
    }

    private void initData() {
        if (mPresenter != null) {
            mPresenter.queryCategoryList();
        }
    }

    /**
     * 左边点击事件
     */
    @Override
    public void onItemClick(@NonNull View view, int viewType, @NonNull Object data, int position) {
        // 拿到点击item数据
        CategoryBean info = (CategoryBean) data;

        Rect rect = new Rect();
        categoryList.getGlobalVisibleRect(rect);
        visualHeight = rect.bottom - rect.top - view.getHeight();

        setSelectCategory(position);
        scrollToMiddle(position);
        if (mPresenter != null) {
            mPresenter.queryContentByCate(info.getCode());
        }
    }

    /**
     * 设置选中分类
     */
    private void setSelectCategory(int selectIndex) {
        // 之前选中的索引
        int preSelectIndex = -1;
        for (int i = 0; i < categoryListAdapter.getInfos().size(); i++) {
            CategoryBean info = categoryListAdapter.getInfos().get(i);
            if (info.isSelect()) {
                preSelectIndex = i;
                break;
            }
        }

        int preStart = preSelectIndex;
        int preCount = 1;
        categoryListAdapter.getInfos().get(preSelectIndex).setSelect(false);
        if (preSelectIndex - 1 >= 0) {
            categoryListAdapter.getInfos().get(preSelectIndex - 1).setFillet("");
            preStart = preSelectIndex - 1;
            preCount += 1;
        }

        if (preSelectIndex + 1 < categoryListAdapter.getInfos().size()) {
            categoryListAdapter.getInfos().get(preSelectIndex + 1).setFillet("");
            preCount += 1;
        }
        categoryListAdapter.notifyItemRangeChanged(preStart, preCount);

        int start = selectIndex;
        int count = 1;
        categoryListAdapter.getInfos().get(selectIndex).setSelect(true);
        if (selectIndex - 1 >= 0) {
            categoryListAdapter.getInfos().get(selectIndex - 1).setFillet("down");
            start = selectIndex - 1;
            count += 1;
        }
        if (selectIndex + 1 < categoryListAdapter.getInfos().size()) {
            categoryListAdapter.getInfos().get(selectIndex + 1).setFillet("up");
            count += 1;
        }
        categoryListAdapter.notifyItemRangeChanged(start, count);
    }

    /**
     * 滚动到中间
     */
    private void scrollToMiddle(int position) {
        int firstPosition = categoryLayoutManager.findFirstVisibleItemPosition();
        //当前点击的item距离 recyclerview 顶部的距离
        int top = categoryList.getChildAt(position - firstPosition).getTop();
        //recyclerView可视区域高度-当前点击item的高度 的一半高度
        int half = visualHeight / 2;

        categoryList.smoothScrollBy(0, top - half);
    }

    /**
     * 加载右侧数据
     */
    @Override
    public void loadCategoryRightData(ContentCateResponse info) {
        categoryRightView.setData(info);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showMessage(@NonNull String message) {

    }

}
