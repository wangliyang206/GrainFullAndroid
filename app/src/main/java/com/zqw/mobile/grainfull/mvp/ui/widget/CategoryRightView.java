package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.model.entity.CateBean;
import com.zqw.mobile.grainfull.mvp.model.entity.CategoryModal;
import com.zqw.mobile.grainfull.mvp.model.entity.ContentCateResponse;
import com.zqw.mobile.grainfull.mvp.ui.adapter.SectionQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.widget
 * @ClassName: CategoryRightView
 * @Description: 右侧分类
 * @Author: WLY
 * @CreateDate: 2023/9/4 15:23
 */
public class CategoryRightView extends FrameLayout {
    // 头部图片
    private ImageView topImg;
    // Tab 导航
    private TabLayout tabLayout;
    // 列表
    private RecyclerView categoryRecyclerView;

    private SectionQuickAdapter sectionQuickAdapter;
    private GridLayoutManager gridLayoutManager;
    // 记录上一次位置，防止在同一内容块里滑动 重复定位到tabLayout
    private int lastPos;
    private boolean isRecyclerScroll;
    // 记录目标位置
    private int mToPosition;
    // 目标项是否在最后一个可见项之后
    private boolean mShouldScroll;

    // 数据源
    private List<CateBean> cateList;

    /**
     * 用于加载图片的管理类, 默认使用 Glide, 使用策略模式, 可替换框架
     */
    private ImageLoader mImageLoader;

    public CategoryRightView(@NonNull Context context) {
        super(context);

        initView(context);
    }

    public CategoryRightView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView(context);
    }

    public CategoryRightView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {

        //可以在任何可以拿到 Context 的地方, 拿到 AppComponent, 从而得到用 Dagger 管理的单例对象
        AppComponent mAppComponent = ArmsUtils.obtainAppComponentFromContext(context);
        mImageLoader = mAppComponent.imageLoader();

        View view = LayoutInflater.from(context).inflate(R.layout.category_right, null);
        topImg = view.findViewById(R.id.imvi_categoryright_top);
        tabLayout = view.findViewById(R.id.view_categoryright_tablayout);
        categoryRecyclerView = view.findViewById(R.id.revi_categoryright_list);

        this.gridLayoutManager = new GridLayoutManager(context, 3);
        this.sectionQuickAdapter = new SectionQuickAdapter(getContext(), R.layout.category_right_grid_header, R.layout.category_right_grid, new ArrayList());

        categoryRecyclerView.setLayoutManager(gridLayoutManager);
        categoryRecyclerView.setAdapter(sectionQuickAdapter);
        categoryRecyclerView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                isRecyclerScroll = true;
            }
            return false;
        });

        // 增加滑动监听
        categoryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isRecyclerScroll) {
                    int position = findHeaderPositionByTab(gridLayoutManager.findFirstVisibleItemPosition());
                    if (position != lastPos) {
                        tabLayout.setScrollPosition(position, 0F, true);
                    }
                    lastPos = position;
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mShouldScroll) {
                    mShouldScroll = false;
                    scrollTop(mToPosition);
                }
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position != -1) {
                    scrollRecycleView2Top(position);
                }
                isRecyclerScroll = false;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        addView(view);
    }

    /**
     * 设置数据
     */
    public void setData(ContentCateResponse data) {
        this.cateList = data.getCateList();
        // 显示顶部图片
        if (TextUtils.isEmpty(data.getBannerUrl())) {
            topImg.setVisibility(GONE);
        } else {
            topImg.setVisibility(VISIBLE);
            mImageLoader.loadImage(getContext(),
                    ImageConfigImpl.builder().url(data.getBannerUrl())
                            .imageView(topImg).build());
        }

        tabLayout.removeAllTabs();
        for (CateBean item : cateList) {
            tabLayout.addTab(tabLayout.newTab().setText(item.getCategoryName()));
        }

        // 组织数据，将二级过滤成一级
        List<CategoryModal> list = new ArrayList<>();
        for (CateBean group : cateList) {
            list.add(new CategoryModal(group.getIconUrl(), group.getCategoryName(), group.getCategoryCode(), true));
            for (CateBean item : group.getCateList()) {
                list.add(new CategoryModal(item.getIconUrl(), item.getCategoryName(), item.getCategoryCode(), false));
            }
        }

        sectionQuickAdapter.setList(list);
        sectionQuickAdapter.notifyDataSetChanged();
    }

    /**
     * 按选项卡查找页眉位置
     */
    private int findHeaderPositionByTab(int position) {
        CategoryModal currentItem = sectionQuickAdapter.getItem(position);

        // 标题中寻找索引
        int index = -1;
        for (int i = 0; i < cateList.size(); i++) {
            CateBean group = cateList.get(i);
            if (group.getCategoryCode().equalsIgnoreCase(currentItem.getCategoryCode())) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            return index;
        } else {
            // 详情中寻找索引
            int innerIndex = -1;
            for (int i = 0; i < cateList.size(); i++) {
                CateBean group = cateList.get(i);
                for (int j = 0; j < group.getCateList().size(); j++) {
                    CateBean interGroup = group.getCateList().get(j);
                    if (interGroup.getCategoryCode().equalsIgnoreCase(currentItem.getCategoryCode())) {
                        innerIndex = i;
                        break;
                    }
                }
            }
            return innerIndex;
        }
    }

    /**
     * 滚动循环视图2顶部
     */
    private void scrollRecycleView2Top(int position) {
        CateBean currentItem = cateList.get(position);
        int currentPosition = -1;

        for (int i = 0; i < sectionQuickAdapter.getData().size(); i++) {
            CategoryModal v = sectionQuickAdapter.getData().get(i);
            if (v.getCategoryCode().equalsIgnoreCase(currentItem.getCategoryCode())) {
                currentPosition = i;
                break;
            }
        }

        scrollTop(currentPosition);
    }

    /**
     * 回到顶部
     */
    private void scrollTop(int position) {
        int firstPosition = gridLayoutManager.findFirstVisibleItemPosition();
        int lastPosition = gridLayoutManager.findLastVisibleItemPosition();

        if (position < firstPosition) {
            // 如果跳转位置在第一个可见位置之前，就smoothScrollToPosition可以直接跳转
            categoryRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastPosition) {
            // 跳转位置在第一个可见项之后，最后一个可见项之前
            // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置
            int movePosition = position - firstPosition;
            if (movePosition >= 0 && movePosition < categoryRecyclerView.getChildCount()) {
                int scrollY = categoryRecyclerView.getChildAt(position - firstPosition).getTop();
                categoryRecyclerView.smoothScrollBy(0, scrollY);
            }
        } else {
            // 如果要跳转的位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            categoryRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }
}
