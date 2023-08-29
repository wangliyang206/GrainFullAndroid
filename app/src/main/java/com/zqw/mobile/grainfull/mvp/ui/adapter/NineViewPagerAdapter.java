package com.zqw.mobile.grainfull.mvp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.mvp.model.entity.MenuBean;
import com.zqw.mobile.grainfull.mvp.ui.fragment.GridFragment;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.adapter
 * @ClassName: NineViewPagerAdapter
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/8/29 17:30
 */
public class NineViewPagerAdapter extends FragmentStateAdapter {
    private List<MenuBean> list;
    private int pageSize;

    public NineViewPagerAdapter(@NonNull Fragment fragment, List<MenuBean> list, int pageSize) {
        super(fragment);
        this.list = list;
        this.pageSize = pageSize;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new GridFragment(list, position, pageSize);
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil(CommonUtils.div(list.size() , pageSize));
    }
}
