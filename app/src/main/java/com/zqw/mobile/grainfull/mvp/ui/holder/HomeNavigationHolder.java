package com.zqw.mobile.grainfull.mvp.ui.holder;

import android.view.View;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jess.arms.base.BaseHolder;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeContentResponse;
import com.zqw.mobile.grainfull.mvp.model.entity.HomeTab;
import com.zqw.mobile.grainfull.mvp.model.entity.NewHomeInfo;
import com.zqw.mobile.grainfull.mvp.ui.adapter.HomeTabPagerAdapter;
import com.zqw.mobile.grainfull.mvp.ui.fragment.HomeListContentView;
import com.zqw.mobile.grainfull.mvp.ui.widget.nestedrecyclerview.ChildRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.holder
 * @ClassName: HomeNavigationHolder
 * @Description: 导航
 * @Author: WLY
 * @CreateDate: 2023/7/14 15:52
 */
public class HomeNavigationHolder extends BaseHolder<NewHomeInfo> implements View.OnClickListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.home_tab_item_layout)
    LinearLayout mLayout;

    @BindView(R.id.view_hometabitemlayout_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.view_hometabitemlayout_viewpager)
    ViewPager mViewPager;

    private ChildRecyclerView mCurrentRecyclerView;
    private HashMap<String, HomeListContentView> cacheVies = new HashMap<>();
    private ArrayList<HomeListContentView> viewList = new ArrayList<>();

    public HomeNavigationHolder(View itemView) {
        super(itemView);

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabTextColors(ContextCompat.getColor(itemView.getContext(), R.color.common_text_hint_color), ContextCompat.getColor(itemView.getContext(), android.R.color.black));
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(itemView.getContext(), R.color.progress_outsize_color4));
        ViewCompat.setElevation(mTabLayout, 10);
    }

    @Override
    public void setData(@NotNull NewHomeInfo info, int position) {

        bindData(info.getTab());
    }

    /**
     * 绑定数据
     */
    public void bindData(Object obj) {
        // 解绑事件
        mViewPager.removeOnPageChangeListener(this);
        if (obj instanceof HomeTab) {
            HomeTab categoryBean = (HomeTab) obj;
            viewList.clear();
            if (cacheVies.size() > categoryBean.getTabTitleList().size()) {
                cacheVies.clear();
            }
            for (int i = 0; i < categoryBean.getTabTitleList().size(); i++) {
                String str = categoryBean.getTabTitleList().get(i);
                HomeListContentView categoryView = cacheVies.get(str);
                if (categoryView == null || categoryView.getParent() != mViewPager) {
                    categoryView = new HomeListContentView(mViewPager.getContext());
                    cacheVies.put(str, categoryView);
                }
                categoryView.setType(i);
                viewList.add(categoryView);
            }
            mCurrentRecyclerView = viewList.get(mViewPager.getCurrentItem());
            int lastItem = mViewPager.getCurrentItem();
            mViewPager.setAdapter(new HomeTabPagerAdapter(viewList, categoryBean.getTabTitleList()));
            mTabLayout.setupWithViewPager(mViewPager);
            mViewPager.setCurrentItem(lastItem);
            // 加载数据
            viewList.get(lastItem).onRefreshNavi();
        }
        // 绑定事件
        mViewPager.addOnPageChangeListener(this);
    }

    /**
     * 加载或刷新数据
     */
    public void loadData(int position, HomeContentResponse info) {
        if (CommonUtils.isNotEmpty(viewList)) {
            viewList.get(position).onSuccessHomeOrder(info);
        }
    }

    /**
     * 获取当前子布局
     */
    public ChildRecyclerView getCurrentChildRecyclerView() {
        return mCurrentRecyclerView;
    }

    @Override
    protected void onRelease() {
        this.mLayout = null;
        this.mTabLayout = null;
        this.mViewPager = null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (!viewList.isEmpty()) {
            mCurrentRecyclerView = viewList.get(position);
            // 加载数据
            viewList.get(position).onRefreshNavi();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
