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

import com.donkingliang.consecutivescroller.ConsecutiveScrollerLayout;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerGoodsListComponent;
import com.zqw.mobile.grainfull.mvp.contract.GoodsListContract;
import com.zqw.mobile.grainfull.mvp.presenter.GoodsListPresenter;

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
