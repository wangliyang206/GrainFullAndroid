package com.zqw.mobile.grainfull.mvp.ui.holder;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
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
import java.util.List;

import butterknife.BindView;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.holder
 * @ClassName: HomeNavigationHolder
 * @Description: 导航
 * @Author: WLY
 * @CreateDate: 2023/7/14 15:52
 */
public class HomeNavigationHolder extends BaseHolder<NewHomeInfo> implements View.OnClickListener, ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {
    @BindView(R.id.home_tab_item_layout)
    LinearLayout mLayout;

    @BindView(R.id.view_hometabitemlayout_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.view_hometabitemlayout_viewpager)
    ViewPager mViewPager;

    private ChildRecyclerView mCurrentRecyclerView;
    private final HashMap<String, HomeListContentView> cacheVies = new HashMap<>();
    private final ArrayList<HomeListContentView> viewList = new ArrayList<>();
    /**
     * 保存Title
     */
    private final List<String> itemTitle = new ArrayList<>();

    public HomeNavigationHolder(View itemView) {
        super(itemView);

//        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
//        mTabLayout.setTabTextColors(ContextCompat.getColor(itemView.getContext(), R.color.common_text_hint_color), ContextCompat.getColor(itemView.getContext(), android.R.color.black));
//        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(itemView.getContext(), R.color.progress_outsize_color4));
//        ViewCompat.setElevation(mTabLayout, 10);

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
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
        mTabLayout.removeOnTabSelectedListener(this);
        if (obj instanceof HomeTab) {
            HomeTab categoryBean = (HomeTab) obj;
            viewList.clear();
            itemTitle.clear();
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

                // Tab
                itemTitle.add(str);
                mTabLayout.addTab(mTabLayout.newTab().setText(itemTitle.get(i)));
            }
            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                if (tab != null) {
                    tab.setCustomView(getTabView(i));
                }
            }
            // Tab控制字体大小
            updateTabTextView(mTabLayout.getTabAt(mTabLayout.getSelectedTabPosition()), true);
            // init ViewPager
            mCurrentRecyclerView = viewList.get(mViewPager.getCurrentItem());
            int lastItem = mViewPager.getCurrentItem();
            mViewPager.setAdapter(new HomeTabPagerAdapter(viewList, categoryBean.getTabTitleList()));
            mViewPager.setCurrentItem(lastItem);
            // 加载数据
            viewList.get(lastItem).onRefreshNavi();
        }
        // 绑定事件
        mViewPager.addOnPageChangeListener(this);
        mTabLayout.addOnTabSelectedListener(this);
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
     * 自定义TabLayout
     */
    private View getTabView(int currentPosition) {
        View view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.tab_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tab_item_textview);
        textView.setText(itemTitle.get(currentPosition));
        return view;
    }

    /**
     * 控制TabLayout中文字大小、粗细
     */
    private void updateTabTextView(TabLayout.Tab tab, boolean isSelect) {
        if (isSelect) {
            //选中加粗
            TextView tabSelect = (TextView) tab.getCustomView().findViewById(R.id.tab_item_textview);
            tabSelect.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tabSelect.setText(tab.getText());
            tabSelect.setTextColor(ActivityCompat.getColor(itemView.getContext(), android.R.color.black));
            tabSelect.setTextSize(14);
        } else {
            TextView tabUnSelect = (TextView) tab.getCustomView().findViewById(R.id.tab_item_textview);
            tabUnSelect.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tabUnSelect.setText(tab.getText());
            tabUnSelect.setTextColor(ActivityCompat.getColor(itemView.getContext(), R.color.tab_text_unselected));
            tabUnSelect.setTextSize(13);
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
            mTabLayout.getTabAt(position).select();
            mCurrentRecyclerView = viewList.get(position);
            // 加载数据
            viewList.get(position).onRefreshNavi();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        updateTabTextView(tab, true);
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        updateTabTextView(tab, false);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
