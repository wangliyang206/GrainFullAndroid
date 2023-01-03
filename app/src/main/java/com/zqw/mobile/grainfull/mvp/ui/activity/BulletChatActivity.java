package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerBulletChatComponent;
import com.zqw.mobile.grainfull.mvp.contract.BulletChatContract;
import com.zqw.mobile.grainfull.mvp.presenter.BulletChatPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.MarqueeText;

import butterknife.BindView;

/**
 * Description:展示弹幕
 * <p>
 * Created on 2022/12/30 11:18
 *
 * @author 赤槿
 * module name is BulletChatActivity
 */
public class BulletChatActivity extends BaseActivity<BulletChatPresenter> implements BulletChatContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.txvi_bulletchat_content)
    MarqueeText txviContent;                                                                        // 跑马灯

    /*------------------------------------------------业务区域------------------------------------------------*/
    // 当前展示的文字
    private String text;
    // 字体大小(字号)
    private int txtSize;
    // 滚动速度：1 - 30
    private int speed;

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
    protected void onDestroy() {
        // 停止滚动
        txviContent.stopScroll();
        super.onDestroy();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBulletChatComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_bullet_chat;
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
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            text = mBundle.getString("text");
            txtSize = mBundle.getInt("txtSize");
            speed = mBundle.getInt("speed");
        }

        // 设置显示内容
        txviContent.setText(text);
        // 设置字号
        txviContent.setTextSize(txtSize);
        // 设置滚动速度
        txviContent.setRollingSpeed(speed / 2);
        // 设置滚动方向
        txviContent.setRollingDirection(1);
        // 开始滚动
        txviContent.startScroll();
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