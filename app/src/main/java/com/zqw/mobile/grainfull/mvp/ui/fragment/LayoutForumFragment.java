package com.zqw.mobile.grainfull.mvp.ui.fragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.baidu.idl.face.platform.utils.DensityUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.donkingliang.consecutivescroller.ConsecutiveScrollerLayout;
import com.donkingliang.consecutivescroller.ConsecutiveViewPager2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.mikepenz.materialize.util.UIUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.di.component.DaggerLayoutForumComponent;
import com.zqw.mobile.grainfull.mvp.contract.LayoutForumContract;
import com.zqw.mobile.grainfull.mvp.model.entity.BannerBean;
import com.zqw.mobile.grainfull.mvp.model.entity.MenuBean;
import com.zqw.mobile.grainfull.mvp.model.entity.TabBean;
import com.zqw.mobile.grainfull.mvp.presenter.LayoutForumPresenter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.BannerExampleAdapter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.NineViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.fragment
 * @ClassName: LayoutForumFragment
 * @Description: 模仿 - 首页2.0 - 商城
 * @Author: WLY
 * @CreateDate: 2023/7/14 16:46
 */
public class LayoutForumFragment extends BaseFragment<LayoutForumPresenter> implements LayoutForumContract.View, View.OnClickListener {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.fragment_layouthome_forum)
    RelativeLayout contentLayout;                                                                   // 主布局

    @BindView(R.id.srla_layouthomeforum_refreshView)
    SmartRefreshLayout refreshView;                                                                 // 智能下拉刷新布局
    @BindView(R.id.csla_layouthomeforum_consecutiveScrollerLayout)
    ConsecutiveScrollerLayout consecutiveScrollerLayout;                                            // 可持续滑动布局
    @BindView(R.id.lila_layouthomeforum_searchLinearLayout)
    LinearLayoutCompat searchLinearLayout;                                                          // 搜索外层布局
    @BindView(R.id.include_home_top_view)
    View searchHeader;                                                                              // 搜索布局
    @BindView(R.id.rela_layouthomeforumtopview_searchlayout)
    RelativeLayout search;                                                                          // 搜索布局
    @BindView(R.id.view_layouthomeforum_tabLayout)
    TabLayout tabLayout;                                                                            // 选项卡布局
    @BindView(R.id.view_layouthomeforum_viewPager)
    ConsecutiveViewPager2 viewPager;                                                                // viewPager
    @BindView(R.id.view_layouthomeforum_backTop)
    FloatingActionButton backTop;                                                                   // 回到顶部

    @BindView(R.id.view_layouthomeforum_banner)
    Banner mBanner;                                                                                 // 轮播图
    @BindView(R.id.imvi_layouthomeforum_bar)
    ImageView imviBar;                                                                              // 广告栏
    @BindView(R.id.view_layouthomeforum_nineViewPager)
    ViewPager2 nineViewPager;                                                                       // 操作栏
    @BindView(R.id.view_layouthomeforum_pointsLayout)
    LinearLayout pointsLayout;
    /*------------------------------------------------业务区域------------------------------------------------*/
    @Inject
    ImageLoader mImageLoader;

    private FragmentStateAdapter tabViewPagerAdapter;
    private final List<TabBean> tabList = new ArrayList<>();

    private float searchHeaderMaxHeight;
    private float searchHeaderMinHeight;
    private float searchMaxMarginTop;
    private float searchMinMarginTop;
    private float searchMaxMarginLeft;
    private float searchMaxMarginRight;
    private float searchMinMargin;
    private float searchHeight;

    // 操作栏适配器
    private NineViewPagerAdapter nineViewPagerAdapter;
    private FrameLayout.LayoutParams layoutParams;
    private List<ImageView> points;
    private List<MenuBean> menuList;
    private int pageSize;

    @Override
    public void onStart() {
        super.onStart();
        // 开始轮播
        mBanner.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        // 停止轮播
        mBanner.stop();
    }

    @Override
    public void onDestroy() {
        // 销毁
        if (mBanner != null)
            mBanner.destroy();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        refreshView.removeAllViews();
    }

    public static LayoutForumFragment instantiate() {
        return new LayoutForumFragment();
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerLayoutForumComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layouthome_forum, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        this.searchHeaderMaxHeight = DensityUtils.dip2px(getContext(), 80.0F);
        this.searchHeaderMinHeight = DensityUtils.dip2px(getContext(), 40.0F);
        this.searchMaxMarginTop = DensityUtils.dip2px(getContext(), 40.0F);
        this.searchMinMarginTop = DensityUtils.dip2px(getContext(), 5.0F);
        this.searchMaxMarginLeft = DensityUtils.dip2px(getContext(), 55.0F);
        this.searchMaxMarginRight = DensityUtils.dip2px(getContext(), 90.0F);
        this.searchMinMargin = DensityUtils.dip2px(getContext(), 15.0F);
        this.searchHeight = DensityUtils.dip2px(getContext(), 30.0F);

        initHeader();
        initRefreshView();
        initTabViewPagerAdapter();
        initNineViewPagerAdapter();
        onRefresh();
    }

    /**
     * 请求网络
     */
    private void onRefresh() {
        if (mPresenter != null) {
            mPresenter.initData();
        }
    }

    /**
     * 设置 搜索框 距离顶部距离
     */
    private void initHeader() {
        searchLinearLayout.setPadding(0, CommonUtils.getHeight(getContext()), 0, 0);
    }

    /**
     * 初始化 - 智能下拉刷新
     */
    private void initRefreshView() {
        // 不使用加载更多
        refreshView.setEnableLoadMore(false);
        // 设置下拉刷新样式
        refreshView.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshView.setRefreshFooter(new ClassicsFooter(getContext()));
        refreshView.setOnRefreshListener(refreshLayout -> {
            // 这里请求网络
            onRefresh();
        });

    }

    /**
     * 轮播图
     */
    @Override
    public void loadBanner(List<BannerBean> list) {
        // 添加生命周期观察者
        mBanner.addBannerLifecycleObserver(this)
                .setAdapter(new BannerExampleAdapter(getContext(), list))
                .setIndicator(new CircleIndicator(getContext()));
    }

    /**
     * 广告栏
     */
    @Override
    public void loadAdvertisingBar(String url) {
        mImageLoader.loadImage(mContext, ImageConfigImpl.builder().url(url)
                .imageView(imviBar).build());
    }

    /**
     * 菜单
     */
    @Override
    public void loadNineMenu(List<MenuBean> nineMenuList) {
        int totalPage = (int) Math.ceil(CommonUtils.div(nineMenuList.size(),pageSize));

        menuList.clear();
        menuList.addAll(nineMenuList);
        nineViewPagerAdapter.notifyDataSetChanged();

        layoutParams.leftMargin = 5;
        layoutParams.rightMargin = 5;

        if (points.size() != totalPage) {
            points.clear();

            for (int current = 0; current < totalPage; current++) {
                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(16, 16));
                if (current == 1) {
                    imageView.setBackgroundResource(R.drawable.selected_indicator);
                } else {
                    imageView.setBackgroundResource(R.drawable.normal_indicator);
                }
                points.add(imageView);
                pointsLayout.addView(imageView, layoutParams);
            }
        }
    }

    /**
     * 加载Tab数据
     */
    public void showTabLayout(List<TabBean> list) {
        // 加载完成
        refreshView.finishRefresh();

        this.tabList.clear();
        this.tabList.addAll(list);
        tabViewPagerAdapter.notifyDataSetChanged();

        viewPager.setOffscreenPageLimit(list.size());
        ViewPager2 var6 = viewPager.getViewPager2();
        if (var6 != null) {
            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, var6, (tab, position) -> {
                tab.setText(list.get(position).getName());
            });
            tabLayoutMediator.attach();
        }
    }

    /**
     * 初始化 ViewPager
     */
    private void initTabViewPagerAdapter() {
        tabViewPagerAdapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return GoodsListFragment.instantiate(tabList.get(position).getCode());
            }

            @Override
            public int getItemCount() {
                return tabList.size();
            }
        };

        viewPager.setAdapter(tabViewPagerAdapter);
        // 监听页面滚动
        consecutiveScrollerLayout.setOnVerticalScrollChangeListener((v, scrollY, oldScrollY, scrollState) -> searchHeaderOnScroll(scrollY));
    }

    private void initNineViewPagerAdapter() {
        layoutParams = new FrameLayout.LayoutParams(new ViewGroup.LayoutParams(16, 16));
        points = new ArrayList<>();
        menuList = new ArrayList<>();
        pageSize = 10;

        nineViewPagerAdapter = new NineViewPagerAdapter(this, menuList, pageSize);
        nineViewPager.setAdapter(nineViewPagerAdapter);
        nineViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                selectedPoints(position);
            }
        });

    }

    private void selectedPoints(int position) {
        for (int index = 0; index < points.size(); index++) {
            if (index == position) {
                points.get(index).setBackgroundResource(R.drawable.selected_indicator);
            } else {
                points.get(index).setBackgroundResource(R.drawable.normal_indicator);
            }
        }
    }

    /**
     * 滚动搜索标题
     */
    private void searchHeaderOnScroll(int scrollY) {
        double containerNewHeight = searchHeaderMaxHeight - scrollY * 0.5;
        double searchNewMarginTop = searchMaxMarginTop - scrollY * 0.5;
        double searchNewMarginLeft = searchMinMargin + scrollY * 1.3;
        double searchNewMarginRight = searchMinMargin + scrollY * 1.6;

        if (scrollY >= ScreenUtils.getScreenHeight()) {
            backTop.setVisibility(View.VISIBLE);
        } else {
            backTop.setVisibility(View.GONE);
        }

        LinearLayoutCompat.LayoutParams containerLp = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams searchLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        searchLp.height = (int) searchHeight;
        if (searchNewMarginTop <= searchMinMarginTop) {
            searchLp.topMargin = (int) searchMinMarginTop;
            containerLp.height = (int) searchHeaderMinHeight;
        } else {
            searchLp.topMargin = (int) searchNewMarginTop;
            containerLp.height = (int) containerNewHeight;
        }
        searchHeader.setLayoutParams(containerLp);
        if (searchNewMarginLeft >= searchMaxMarginLeft)
            searchLp.leftMargin = (int) searchMaxMarginLeft;
        else
            searchLp.leftMargin = (int) searchNewMarginLeft;

        if (searchNewMarginRight >= searchMaxMarginRight)
            searchLp.rightMargin = (int) searchMaxMarginRight;
        else
            searchLp.rightMargin = (int) searchNewMarginRight;

        search.setLayoutParams(searchLp);
    }

    @OnClick({
            R.id.imvi_layouthomeforumtopview_message,                                               // 消息
            R.id.imvi_layouthomeforumtopview_scan,                                                  // 扫码
            R.id.imvi_layouthomeforumtopview_camera,                                                // 搜索拍照
            R.id.view_layouthomeforum_backTop                                                                            // 回收顶部
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvi_layouthomeforumtopview_message:                                          // 消息
                showMessage("您点击了消息！");
                break;
            case R.id.imvi_layouthomeforumtopview_scan:                                             // 扫码
                showMessage("您点击了扫码！");
                break;
            case R.id.imvi_layouthomeforumtopview_camera:                                           // 搜索拍照
                showMessage("您点击了搜索中的拍照！");
                break;
            case R.id.view_layouthomeforum_backTop:                                                                      // 回收顶部
                consecutiveScrollerLayout.scrollToChild(consecutiveScrollerLayout.getChildAt(0));
                break;
        }
    }

    @Override
    public void setData(@Nullable Object data) {

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
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

}
