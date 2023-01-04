package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerLevitationButtonComponent;
import com.zqw.mobile.grainfull.mvp.contract.LevitationButtonContract;
import com.zqw.mobile.grainfull.mvp.presenter.LevitationButtonPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.DragFloatActionButtonHomeMobile;
import com.zqw.mobile.grainfull.mvp.ui.widget.DragFloatButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:悬浮按钮
 * <p>
 * Created on 2023/01/04 11:44
 *
 * @author 赤槿
 * module name is LevitationButtonActivity
 */
public class LevitationButtonActivity extends BaseActivity<LevitationButtonPresenter> implements LevitationButtonContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.btn_levitationbutton_one)
    DragFloatButton dfbuOne;

    @BindView(R.id.dfab_levitationbutton_fb)
    DragFloatActionButtonHomeMobile viewFb;                                                         // 打电话

    /*------------------------------------------------业务区域------------------------------------------------*/

    /**
     * 禁用测滑返回
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLevitationButtonComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_levitation_button;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("悬浮按钮");

        // 设置一个空事件，为了防止拖动时出现无效
        viewFb.setOnClickListener(v -> {
        });

        viewFb.setContact("15032134297");
    }

    @OnClick({
            R.id.btn_levitationbutton_one,                                                          // 点击第一个悬浮按钮
    })
    @Override
    public void onClick(View v) {
        if (viewFb != null && viewFb.getShow()) {
            viewFb.showView(false);
        } else {
            switch (v.getId()) {
                case R.id.btn_levitationbutton_one:                                                 // 点击第一个悬浮按钮
                    showMessage("点击了 牛头！");
                    break;
            }
        }
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
        ArmsUtils.snackbarText(message);
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