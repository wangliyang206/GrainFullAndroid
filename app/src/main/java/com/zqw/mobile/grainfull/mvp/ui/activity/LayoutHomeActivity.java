package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baidu.idl.face.platform.utils.DensityUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerLayoutHomeComponent;
import com.zqw.mobile.grainfull.mvp.contract.LayoutHomeContract;
import com.zqw.mobile.grainfull.mvp.presenter.LayoutHomePresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description: 界面 - 首页
 * <p>
 * Created on 2023/07/05 14:16
 *
 * @author 赤槿
 * module name is LayoutHomeActivity
 */
public class LayoutHomeActivity extends BaseActivity<LayoutHomePresenter> implements LayoutHomeContract.View {
    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.rela_layouthome_search)
    RelativeLayout relaSearch;

    @BindView(R.id.imvi_layouthome_search)
    ImageView imviSearch;

    @BindView(R.id.edit_layouthome_search)
    EditText editSearch;

    @BindView(R.id.imvi_layouthome_close)
    ImageView imviClose;

    /*------------------------------------------业务信息------------------------------------------*/
    // 自动转换
    private AutoTransition autoTransition;

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
        LinearLayout.LayoutParams LayoutParams = (LinearLayout.LayoutParams) relaSearch.getLayoutParams();
        LayoutParams.width = DensityUtils.dip2px(this, DensityUtils.px2dip(this, DensityUtils.getDisplayWidth(this)) - 40);
        LayoutParams.setMargins(0, 0, 0, 0);
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
        LinearLayout.LayoutParams LayoutParams = (LinearLayout.LayoutParams) relaSearch.getLayoutParams();
        LayoutParams.width = DensityUtils.dip2px(this, 48);
        LayoutParams.height = DensityUtils.dip2px(this, 48);
        LayoutParams.setMargins(0, 0, 0, 0);
        relaSearch.setLayoutParams(LayoutParams);
        //隐藏键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getWindowToken(), 0);
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