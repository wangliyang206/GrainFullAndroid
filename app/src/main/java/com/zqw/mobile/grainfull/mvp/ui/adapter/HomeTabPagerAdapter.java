package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.zqw.mobile.grainfull.mvp.ui.fragment.HomeListContentView;

import java.util.ArrayList;

/**
 * 首页Tab适配器
 */
public class HomeTabPagerAdapter extends PagerAdapter {

    private ArrayList<HomeListContentView> mViewList;
    private ArrayList<String> mTabList;

    private HomeListContentView mCurrentPrimaryItem = null;

    public HomeTabPagerAdapter(ArrayList<HomeListContentView> viewList, ArrayList<String> tabList) {
        mViewList = viewList;
        mTabList = tabList;
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        HomeListContentView categoryView = mViewList.get(position);
        if (container == categoryView.getParent()) {
            container.removeView(categoryView);
        }
        container.addView(categoryView);
        return categoryView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        HomeListContentView categoryView = (HomeListContentView) object;
        if (categoryView != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.onUserVisibleChange(false);
            }
        }
        categoryView.onUserVisibleChange(true);
        mCurrentPrimaryItem = categoryView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTabList.get(position);
    }
}
