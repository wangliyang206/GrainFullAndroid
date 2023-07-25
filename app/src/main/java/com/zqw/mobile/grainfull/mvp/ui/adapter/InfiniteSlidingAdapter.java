package com.zqw.mobile.grainfull.mvp.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.adapter
 * @ClassName: InfiniteSlidingAdapter
 * @Description: 自动循环和左右无限滑动Adapter
 * @Author: WLY
 * @CreateDate: 2023/7/25 10:47
 */
public class InfiniteSlidingAdapter extends PagerAdapter {

    private List<ImageView> mList = new ArrayList<>();

    public InfiniteSlidingAdapter() {

    }

    public void setData(List<ImageView> mList) {
        this.mList = mList;
    }

    @Override
    public int getCount() {
        // 将page总数返回整型最大，使之ViewPager可以无限向右滑动
        return mList.size() != 0 ? Integer.MAX_VALUE : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //将ViewPager的index转化为实际中Page的index
        if (mList.size() > 0) {
            int actualPos = position % mList.size();
            if (mList.get(actualPos).getParent() != null) {
                ((ViewPager) mList.get(actualPos).getParent()).removeView(mList.get(actualPos));
            }
            container.addView(mList.get(actualPos));
            return mList.get(actualPos);
        } else {
            return null;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        ImageView imvi = (ImageView) object;
        container.removeView(imvi);
        Timber.i("###### destroyItem ######position=" + position);
    }
}
