package com.zqw.mobile.grainfull.mvp.ui.fragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.donkingliang.consecutivescroller.ConsecutiveScrollerLayout;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerGoodsListComponent;
import com.zqw.mobile.grainfull.mvp.contract.GoodsListContract;
import com.zqw.mobile.grainfull.mvp.presenter.GoodsListPresenter;
import com.zqw.mobile.grainfull.mvp.ui.activity.GoodsDetailActivity;
import com.zqw.mobile.grainfull.mvp.ui.adapter.GoodsListAdapter;
import com.zqw.mobile.grainfull.mvp.ui.widget.SpacesItemDecoration;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.fragment
 * @ClassName: GoodsListFragment
 * @Description: 模仿 - 首页2.0 - 商城 - 商品
 * @Author: WLY
 * @CreateDate: 2023/7/14 16:46
 */
public class GoodsListFragment extends BaseFragment<GoodsListPresenter> implements GoodsListContract.View, View.OnClickListener {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.goodsLayout)
    ConsecutiveScrollerLayout goodsLayout;                                                          // 主布局
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;                                                                     // 内容
    /*------------------------------------------------业务区域------------------------------------------------*/
    // 类型：1精选，2新品，3直播，4实惠，5进口
    private String type;

    @Inject
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    @Inject
    GoodsListAdapter mAdapter;

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.staggeredGridLayoutManager = null;
        this.mAdapter = null;
    }

    public static GoodsListFragment instantiate(String type) {
        GoodsListFragment fragment = new GoodsListFragment();
        fragment.type = type;
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerGoodsListComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_goods, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initRecyclerView();

        if (mPresenter != null) {
            mPresenter.initGoodsList(type);
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        // 解决加载下一页后重新排列的问题
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        ArmsUtils.configRecyclerView(mRecyclerView, staggeredGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((view, viewType, data, position) -> {
            // 商品详情
            ArmsUtils.startActivity(GoodsDetailActivity.class);
        });
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(10));
    }

    @Override
    public void onClick(View v) {

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
