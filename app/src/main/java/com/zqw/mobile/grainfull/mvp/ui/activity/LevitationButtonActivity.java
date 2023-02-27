package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.service.FloatingWindowService;
import com.zqw.mobile.grainfull.di.component.DaggerLevitationButtonComponent;
import com.zqw.mobile.grainfull.mvp.contract.LevitationButtonContract;
import com.zqw.mobile.grainfull.mvp.presenter.LevitationButtonPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.DragFloatActionButtonHomeMobile;
import com.zqw.mobile.grainfull.mvp.ui.widget.DragFloatButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:悬浮窗口/按钮
 * <p>
 * Created on 2023/01/04 11:44
 *
 * @author 赤槿
 * module name is LevitationButtonActivity
 */
public class LevitationButtonActivity extends BaseActivity<LevitationButtonPresenter> implements LevitationButtonContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.btn_levitationbutton_one)
    DragFloatButton dfbuOne;                                                                        // 悬浮按钮 - 牛头
    @BindView(R.id.dfab_levitationbutton_fb)
    DragFloatActionButtonHomeMobile viewFb;                                                         // 悬浮按钮 - 打电话


    @BindView(R.id.btn_levitationbutton_start)
    Button btnStart;                                                                                // 启动服务按钮
    @BindView(R.id.btn_levitationbutton_stop)
    Button btnStop;                                                                                 // 停止服务按钮

    /*------------------------------------------------业务区域------------------------------------------------*/
    private Intent intent;

    /**
     * 禁用测滑返回
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    protected void onDestroy() {
        if (FloatingWindowService.isStarted){
            stopService(intent);
        }
        super.onDestroy();

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
        setTitle("悬浮窗口");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "levitation_button");
        // 设置一个空事件，为了防止拖动时出现无效
        viewFb.setOnClickListener(v -> {
        });

        viewFb.setContact("15032134297");

        // 悬浮窗口
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            btnStart.setVisibility(View.VISIBLE);
            btnStop.setVisibility(View.VISIBLE);
            initFloatingWindow();
        } else {
            btnStart.setVisibility(View.GONE);
            btnStop.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化悬浮窗口
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initFloatingWindow() {
        intent = new Intent(this, FloatingWindowService.class);
        if (!Settings.canDrawOverlays(this)) {
            showMessage("当前无权限，请授权");
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
        }
    }

    @OnClick({
            R.id.btn_levitationbutton_one,                                                          // 点击第一个悬浮按钮
            R.id.btn_levitationbutton_start,                                                        // 启动服务按钮
            R.id.btn_levitationbutton_stop,                                                         // 停止服务按钮
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
                case R.id.btn_levitationbutton_start:                                               // 启动服务按钮
                    if (!FloatingWindowService.isStarted){
                        startService(intent);
                    }
                    break;
                case R.id.btn_levitationbutton_stop:                                                // 停止服务按钮
                    stopService(intent);
                    break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (!Settings.canDrawOverlays(this)) {
                showMessage("授权失败");
            } else {
                showMessage("授权成功");
                startService(intent);
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