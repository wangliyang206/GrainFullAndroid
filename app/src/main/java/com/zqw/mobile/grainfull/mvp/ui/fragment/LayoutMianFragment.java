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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.baidu.idl.face.platform.utils.DensityUtils;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerLayoutHomeComponent;
import com.zqw.mobile.grainfull.mvp.contract.LayoutHomeContract;
import com.zqw.mobile.grainfull.mvp.presenter.LayoutHomePresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.fragment
 * @ClassName: LayoutMianFragment
 * @Description: 其它 - 首页 - 首页
 * @Author: WLY
 * @CreateDate: 2023/7/14 16:46
 */
public class LayoutMianFragment extends BaseFragment<LayoutHomePresenter> implements LayoutHomeContract.View, View.OnClickListener {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.fragment_layouthome_main)
    ConstraintLayout contentLayout;                                                                   // 主布局

    @BindView(R.id.rela_layouthome_search)
    RelativeLayout relaSearch;                                                                      // 搜索
    @BindView(R.id.imvi_layouthome_search)
    ImageView imviSearch;
    @BindView(R.id.edit_layouthome_search)
    EditText editSearch;
    @BindView(R.id.imvi_layouthome_close)
    ImageView imviClose;

    /*------------------------------------------业务信息------------------------------------------*/
    // 自动转换
    private AutoTransition autoTransition;

    public static LayoutMianFragment instantiate() {
        return new LayoutMianFragment();
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerLayoutHomeComponent
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
        /* 输入法键盘的搜索监听 */
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
            R.id.imvi_layouthome_search,                                                            // 搜索按钮
            R.id.imvi_layouthome_close,                                                             // 关闭
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvi_layouthome_search:                                                       // 搜索按钮
                initExpand();
                break;
            case R.id.imvi_layouthome_close:                                                        // 关闭
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
        LayoutParams.width = DensityUtils.dip2px(getContext(), DensityUtils.px2dip(getContext(), DensityUtils.getDisplayWidth(getContext())) - 40);
        LayoutParams.setMargins(0, DensityUtils.dip2px(getContext(), 30), DensityUtils.dip2px(getContext(), 20), 0);
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
        LayoutParams.width = DensityUtils.dip2px(getContext(), 48);
        LayoutParams.height = DensityUtils.dip2px(getContext(), 48);
        LayoutParams.setMargins(0, DensityUtils.dip2px(getContext(), 30), DensityUtils.dip2px(getContext(), 20), 0);
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
