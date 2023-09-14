package com.zqw.mobile.grainfull.mvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.baidu.idl.face.platform.utils.DensityUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.donkingliang.consecutivescroller.ConsecutiveScrollerLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.di.component.DaggerMineComponent;
import com.zqw.mobile.grainfull.mvp.contract.MineContract;
import com.zqw.mobile.grainfull.mvp.model.entity.TabBean;
import com.zqw.mobile.grainfull.mvp.presenter.MinePresenter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.GoodsListAdapter;
import com.zqw.mobile.grainfull.mvp.ui.widget.SpacesItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.fragment
 * @ClassName: LayoutCategoryFragment
 * @Description: MineFragment
 * @Author: WLY
 * @CreateDate: 2023/9/4 12:02
 */
public class MineFragment extends BaseFragment<MinePresenter> implements MineContract.View, View.OnClickListener {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.fragment_layouthome_mine)
    SmartRefreshLayout contentLayout;                                                               // 主布局

    @BindView(R.id.lila_layouthomemine_banner)
    LinearLayout lilaBanner;
    @BindView(R.id.view_layouthomemine_consecutive)
    ConsecutiveScrollerLayout consecutiveLayout;
    @BindView(R.id.view_layouthomemine_header)
    LinearLayoutCompat lilaHeaderLayout;
    @BindView(R.id.lila_layouthomemine_mine_userInfoLayout)
    LinearLayout userInfoLinearLayout;
    @BindView(R.id.txvi_layouthomemine_mine_title)
    TextView txviTitle;
    @BindView(R.id.lila_layouthomemine_mine_userInfo)
    LinearLayout lilaUserInfoLayout;
    @BindView(R.id.imvi_layouthomemine_mine_image)
    CircleImageView profileImage;
    @BindView(R.id.view_layouthomemine_tab)
    TabLayout tabLayout;
    @BindView(R.id.view_layouthomemine_backTop)
    FloatingActionButton backTop;
    @BindView(R.id.revi_layouthomemine_recommend)
    RecyclerView mRecyclerView;
    /*------------------------------------------------业务区域------------------------------------------------*/
    @Inject
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Inject
    GoodsListAdapter mAdapter;

    private float userInfoLayoutMaxMarginTop;
    private float userInfoLayoutMinMarginTop;
    private int maxHeight;
    private int minHeight;

    public static MineFragment instantiate() {
        return new MineFragment();
    }

    @Override
    public void onDestroy() {
        // super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(mRecyclerView);
        super.onDestroy();
        this.mRecyclerView = null;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerMineComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layouthome_mine, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        userInfoLayoutMaxMarginTop = DensityUtils.dip2px(getContext(), 40);
        userInfoLayoutMinMarginTop = DensityUtils.dip2px(getContext(), 10);
        maxHeight = DensityUtils.dip2px(getContext(), 58);
        minHeight = DensityUtils.dip2px(getContext(), 25);

        lilaHeaderLayout.setPadding(0, CommonUtils.getHeight(getContext()), 0, 0);
        consecutiveLayout.setOnVerticalScrollChangeListener((v, scrollY, oldScrollY, scrollState) -> handleScroll((int) (scrollY * 0.65)));

        // 初始化
//        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(10));
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
//        goodsListAdapter.setOnItemClickListener(this);

        // 禁止下拉刷新和上拉加载更多
        contentLayout.setEnableRefresh(false);
        contentLayout.setEnableLoadMore(false);

        initData();
    }

    /**
     * 请求数据
     */
    private void initData() {
        if (mPresenter != null) {
            mPresenter.initGoodsList();
        }
    }

    /**
     * 滑动
     */
    private void handleScroll(int scrollY) {
        // 处理背景色、用户信息显示与隐藏
        if (scrollY >= CommonUtils.getHeight(getContext())) {
            lilaUserInfoLayout.setVisibility(View.GONE);
            lilaHeaderLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
            txviTitle.setTextColor(getResources().getColor(android.R.color.black));
            lilaBanner.setBackgroundResource(R.drawable.banner_bg);
        } else {
            lilaUserInfoLayout.setVisibility(View.VISIBLE);
            lilaHeaderLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            txviTitle.setTextColor(getResources().getColor(android.R.color.transparent));
            lilaBanner.setBackgroundResource(R.drawable.mine_top_bg);
        }
        // 用户信息Layout距离顶部距离
        float userInfoLayoutNewMarginTop = userInfoLayoutMaxMarginTop - scrollY;
        RelativeLayout.LayoutParams userInfoLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (userInfoLayoutNewMarginTop <= userInfoLayoutMinMarginTop) {
            userInfoLp.topMargin = (int) userInfoLayoutMinMarginTop;
        } else {
            userInfoLp.topMargin = (int) userInfoLayoutNewMarginTop;
        }
        userInfoLinearLayout.setLayoutParams(userInfoLp);

        // 处理头像大小
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int height = (maxHeight - scrollY < minHeight) ? minHeight : maxHeight - scrollY;

        lp.height = height;
        lp.width = height;
        lp.leftMargin = DensityUtils.dip2px(getContext(), 20f);

        profileImage.setLayoutParams(lp);

        // 改变tabLayout背景色
        if (getCurrentStickyViews() == -1) {
            tabLayout.setBackgroundResource(R.color.frame_bg_color);
        } else {
            tabLayout.setBackgroundResource(android.R.color.white);
        }
        if (scrollY == 0) {
            tabLayout.setBackgroundResource(R.color.frame_bg_color);
        }

        // 显示backToTop
        if (scrollY >= ScreenUtils.getScreenHeight()) {
            backTop.setVisibility(View.VISIBLE);
        } else {
            backTop.setVisibility(View.GONE);
        }
    }

    private int getCurrentStickyViews() {
        for (int i = 0; i < consecutiveLayout.getCurrentStickyViews().size(); i++) {
            View view = consecutiveLayout.getCurrentStickyViews().get(i);
            if (view.getId() == R.id.view_layouthomemine_tab) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 加载Tab
     */
    @Override
    public void showTabLayout(List<TabBean> list) {
        tabLayout.removeAllTabs();
        for (TabBean itemTab : list) {
            tabLayout.addTab(tabLayout.newTab().setText(itemTab.getName()));
        }

    }

    @OnClick({
            R.id.view_layouthomemine_backTop
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_layouthomemine_backTop:
                consecutiveLayout.scrollToChild(consecutiveLayout.getChildAt(0));
                break;
        }
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showMessage(@NonNull String message) {

    }

}
