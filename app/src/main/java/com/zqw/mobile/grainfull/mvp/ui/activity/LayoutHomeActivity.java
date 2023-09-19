package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerLayoutHomeComponent;
import com.zqw.mobile.grainfull.mvp.contract.LayoutHomeContract;
import com.zqw.mobile.grainfull.mvp.presenter.LayoutHomePresenter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.FragmentPagerAdapter;
import com.zqw.mobile.grainfull.mvp.ui.fragment.LayoutCategoryFragment;
import com.zqw.mobile.grainfull.mvp.ui.fragment.LayoutForumFragment;
import com.zqw.mobile.grainfull.mvp.ui.fragment.LayoutMianFragment;
import com.zqw.mobile.grainfull.mvp.ui.fragment.LayoutOtherFragment;
import com.zqw.mobile.grainfull.mvp.ui.fragment.MineFragment;
import com.zqw.mobile.grainfull.mvp.ui.widget.anim.AnimationRadioView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Description: 模仿 - 首页2.0
 * <p>
 * Created on 2023/07/05 14:16
 *
 * @author 赤槿
 * module name is LayoutHomeActivity
 */
public class LayoutHomeActivity extends BaseActivity<LayoutHomePresenter> implements LayoutHomeContract.View {
    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.view_layouthome_pager)
    ViewPager mViewPager;

    @BindView(R.id.magic_layouthome_indicator)
    MagicIndicator mMagicIndicator;

    /*------------------------------------------业务信息------------------------------------------*/
    private List<String> iconList = new ArrayList<>();

    /**
     * 管理器
     */
    private FragmentPagerAdapter mAdapter;
    /**
     * 当前Fragment
     */
    private Fragment mCurFragment;

    /**
     * 禁止侧滑关闭
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    public boolean isStatusBarFragment() {
        return true;
    }

    @Override
    protected void onDestroy() {
        if (iconList != null) {
            iconList.clear();
            iconList = null;
        }

        super.onDestroy();

        this.mAdapter = null;
        this.mCurFragment = null;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLayoutHomeComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_layout_home;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("首页");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "layout_home_open");

        initData();
        initViewPager();
        initMagicIndicator();
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
        mViewPager.setOffscreenPageLimit(0);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        iconList.add("tab_article.json");
        iconList.add("tab_club.json");
        iconList.add("tab_car.json");
        iconList.add("tab_used_car.json");
        iconList.add("tab_me.json");
    }

    /**
     * 初始化分页
     */
    private void initViewPager() {
        mViewPager.setAdapter(mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    // 首页
                    return LayoutMianFragment.instantiate();
                } else if (position == 1) {
                    // 商城
                    return LayoutForumFragment.instantiate();
                } else if (position == 2) {
                    // 分类
                    return LayoutCategoryFragment.instantiate();
                } else if (position == 4) {
                    // 我的
                    return MineFragment.instantiate();
                } else {
                    // 其它
                    return LayoutOtherFragment.instantiate(position);
                }

            }

            @Override
            public int getCount() {
                return iconList.size();
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                super.setPrimaryItem(container, position, object);
                if (mCurFragment == null) {
                    commitUpdate();
                }
                mCurFragment = (Fragment) object;
            }

            //this is called when notifyDataSetChanged() is called
            @Override
            public int getItemPosition(@NotNull Object object) {
                return PagerAdapter.POSITION_NONE;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mAdapter.commitUpdate();
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

            }
        });
    }

    /**
     * 初始化指示器
     */
    private void initMagicIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return iconList == null ? 0 : iconList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
                View customLayout = LayoutInflater.from(context).inflate(R.layout.item_tab_layouthome, null, false);
                AnimationRadioView tabImage = customLayout.findViewById(R.id.item_tab_image);
                tabImage.setAnimation(iconList.get(index));
                commonPagerTitleView.setContentView(customLayout);
                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
                    @Override
                    public void onSelected(int index, int totalCount) {
                        tabImage.setChecked(true);
                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        tabImage.setChecked(false);
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

                    }
                });
                commonPagerTitleView.setOnClickListener(view -> mViewPager.setCurrentItem(index));
                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                // 没有指示器，因为title的指示作用已经很明显了
                return null;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
    }

    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.makeText(this, message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}