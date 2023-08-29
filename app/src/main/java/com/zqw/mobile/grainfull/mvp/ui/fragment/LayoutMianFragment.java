package com.zqw.mobile.grainfull.mvp.ui.fragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.baidu.idl.face.platform.utils.DensityUtils;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.EventBusTags;
import com.zqw.mobile.grainfull.di.component.DaggerLayoutMianComponent;
import com.zqw.mobile.grainfull.mvp.contract.LayoutMianContract;
import com.zqw.mobile.grainfull.mvp.model.entity.MainEvent;
import com.zqw.mobile.grainfull.mvp.presenter.LayoutMianPresenter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.NewHomeAdapter;
import com.zqw.mobile.grainfull.mvp.ui.widget.nestedrecyclerview.ParentRecyclerView;
import com.zqw.mobile.grainfull.mvp.ui.widget.nestedrecyclerview.StoreSwipeRefreshLayout;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.fragment
 * @ClassName: LayoutMianFragment
 * @Description: 模仿 - 首页2.0 - 首页
 * @Author: WLY
 * @CreateDate: 2023/7/14 16:46
 */
public class LayoutMianFragment extends BaseFragment<LayoutMianPresenter> implements LayoutMianContract.View, View.OnClickListener {
    /*-------------------------------------------控件信息-------------------------------------------*/
    @BindView(R.id.fragment_layouthome_main)
    ConstraintLayout contentLayout;                                                                 // 主布局

    @BindView(R.id.txvi_layouthomemain_title)
    TextView txviTitle;

    @BindView(R.id.rela_layouthomemain_search)
    RelativeLayout relaSearch;                                                                      // 搜索
    @BindView(R.id.imvi_layouthomemain_search)
    ImageView imviSearch;
    @BindView(R.id.edit_layouthome_search)
    EditText editSearch;
    @BindView(R.id.imvi_layouthomemain_close)
    ImageView imviClose;

    @BindView(R.id.srla_layouthomemain_refresh)
    StoreSwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.revi_layouthomemain_content)
    ParentRecyclerView mRecyclerView;

    /*-------------------------------------------业务信息-------------------------------------------*/
    @Inject
    NewHomeAdapter mAdapter;                                                                        // 适配器

    // 自动转换
    private AutoTransition autoTransition;
    // 列表下拉刷新事件
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener;

    /**
     * 实例化 - 入口
     */
    public static LayoutMianFragment instantiate() {
        return new LayoutMianFragment();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.autoTransition = null;
        this.mAdapter = null;
        this.mRefreshListener = null;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerLayoutMianComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layouthome_main, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        // 初始化搜索
        initSearch();
        // 初始化下拉刷新
        initSwipeRefresh();
        // 第一次拿数据
        onRefresh();
    }

    /**
     * 下拉刷新
     */
    private void onRefresh() {
        if (mPresenter != null) {
            mPresenter.getHomeList();
        }
    }

    @Subscriber(tag = EventBusTags.HOME_TAG, mode = ThreadMode.POST)
    private void eventBusEvent(MainEvent mainEvent) {
        if (mainEvent.getCode() == EventBusTags.NEW_HOME_REFRESH_TAG) {
            if (mPresenter != null) {
                mPresenter.getHomeContentData(mainEvent.getType(), true);
            }
        }
        if (mainEvent.getCode() == EventBusTags.NEW_HOME_MORE_TAG) {
            if (mPresenter != null) {
                mPresenter.getHomeContentData(mainEvent.getType(), false);
            }
        }
    }

    /**
     * 初始化下拉刷新
     */
    private void initSwipeRefresh() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.initLayoutManager(getContext());
        // 绑定刷新事件
        mRefreshListener = () -> {
            // 刷新数据
            onRefresh();
        };
        mSwipeRefreshLayout.setOnRefreshListener(mRefreshListener);
    }

    /**
     * 数据加载完成
     */
    @Override
    public void loadSucc() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 初始化搜索
     */
    private void initSearch() {
        // 输入法键盘的搜索监听
        editSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String str = editSearch.getText().toString();
                if (!TextUtils.isEmpty(str)) {
                    showMessage(str);
                } else {
                    showMessage("请输入内容");
                }
            }
            return false;
        });
    }

    @OnClick({
            R.id.imvi_layouthomemain_search,                                                        // 搜索按钮
            R.id.imvi_layouthomemain_close,                                                         // 关闭
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvi_layouthomemain_search:                                                   // 搜索按钮
                initExpand();
                break;
            case R.id.imvi_layouthomemain_close:                                                    // 关闭
                initClose();
                break;
        }
    }

    /**
     * 展开
     */
    public void initExpand() {
        editSearch.setVisibility(View.VISIBLE);
        imviClose.setVisibility(View.VISIBLE);
        ConstraintLayout.LayoutParams LayoutParams = (ConstraintLayout.LayoutParams) relaSearch.getLayoutParams();
        // 屏幕宽度 - logo(包含左右间隔) - 标题 = 剩余宽度
        int mScreenWidth = DensityUtils.px2dip(getContext(), DensityUtils.getDisplayWidth(getContext()));
        LayoutParams.width = DensityUtils.dip2px(getContext(), mScreenWidth - 100 - DensityUtils.px2dip(getContext(), txviTitle.getWidth()));
        relaSearch.setPadding(14, 0, 14, 0);
        relaSearch.setLayoutParams(LayoutParams);
        editSearch.setOnTouchListener((v, event) -> {
            editSearch.setFocusable(true);
            editSearch.setFocusableInTouchMode(true);
            return false;
        });
        //开始动画
        beginDelayedTransition(relaSearch);
    }

    /**
     * 设置收缩状态时的布局
     */
    private void initClose() {
        editSearch.setVisibility(View.GONE);
        editSearch.setText("");
        imviClose.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams LayoutParams = (ConstraintLayout.LayoutParams) relaSearch.getLayoutParams();
        LayoutParams.width = DensityUtils.dip2px(getContext(), 35);
        LayoutParams.height = DensityUtils.dip2px(getContext(), 35);
        relaSearch.setLayoutParams(LayoutParams);
        //隐藏键盘
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        editSearch.setOnClickListener(v -> editSearch.setCursorVisible(true));
        //开始动画
        beginDelayedTransition(relaSearch);
    }

    /**
     * 开始延迟转换
     */
    private void beginDelayedTransition(ViewGroup view) {
        if (autoTransition == null) {
            autoTransition = new AutoTransition();
            autoTransition.setDuration(500);
        }
        TransitionManager.beginDelayedTransition(view, autoTransition);
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
