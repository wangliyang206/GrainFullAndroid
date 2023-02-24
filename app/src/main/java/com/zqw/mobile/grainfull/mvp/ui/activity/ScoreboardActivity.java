package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerScoreboardComponent;
import com.zqw.mobile.grainfull.mvp.contract.ScoreboardContract;
import com.zqw.mobile.grainfull.mvp.presenter.ScoreboardPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.card.NumCardView;

import butterknife.BindView;

/**
 * Description:记分牌
 * <p>
 * Created on 2023/02/24 09:57
 *
 * @author 赤槿
 * module name is ScoreboardActivity
 */
public class ScoreboardActivity extends BaseActivity<ScoreboardPresenter> implements ScoreboardContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_scoreboard)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.view_scoreboard_left1)
    NumCardView viewLeft1;
    @BindView(R.id.view_scoreboard_left2)
    NumCardView viewLeft2;
    @BindView(R.id.view_scoreboard_right1)
    NumCardView viewRight1;
    @BindView(R.id.view_scoreboard_right2)
    NumCardView viewRight2;

    /*------------------------------------------------业务区域------------------------------------------------*/

    /**
     * 不使用状态栏为透明功能
     */
    public boolean useStatusBar() {
        return false;
    }

    /**
     * 不使用侧滑返回
     */
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerScoreboardComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_scoreboard;
    }

    @Override
    protected void setForm() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        } else {
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        getWindow().setAttributes(lp);
        View decorView = getWindow().getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility();
        // 隐藏导航栏 | 隐藏状态栏
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        systemUiVisibility |= flags;
        getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);

        // 设置屏幕常量
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("记分牌");

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