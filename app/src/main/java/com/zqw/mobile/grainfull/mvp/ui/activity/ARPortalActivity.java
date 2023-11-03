package com.zqw.mobile.grainfull.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.utils.ArmsUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.unity3d.player.UnityPlayerActivity;
import com.zqw.mobile.grainfull.di.component.DaggerARPortalComponent;
import com.zqw.mobile.grainfull.mvp.contract.ARPortalContract;
import com.zqw.mobile.grainfull.mvp.presenter.ARPortalPresenter;
import com.zqw.mobile.grainfull.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:AR传送门
 * <p>
 * Created on 2023/11/03 11:48
 *
 * @author 赤槿
 * module name is ARPortalActivity
 */
public class ARPortalActivity extends BaseActivity<ARPortalPresenter> implements ARPortalContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_arportal)
    LinearLayout contentLayout;                                                                     // 总布局

    /*------------------------------------------------业务区域------------------------------------------------*/

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerARPortalComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_arportal;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("AR传送门");

    }

    @OnClick({
            R.id.btn_arportal_model,                                                                // 查看机型
            R.id.btn_arportal_startgame,                                                            // 开始游戏
    })
    @Override
    public void onClick(View v) {
        Bundle mBundle = new Bundle();
        switch (v.getId()) {
            case R.id.btn_arportal_model:                                                           // 查看机型
                mBundle.putString("TITLE", "支持的设备");
                mBundle.putString("URL", "https://developers.google.cn/ar/devices?hl=fi");
                mBundle.putBoolean("isShowTop", true);

                ActivityUtils.startActivity(mBundle, NewWindowX5Activity.class);
                break;
            case R.id.btn_arportal_startgame:                                                       // 开始游戏
                mBundle.putInt("layout", 6);
                ActivityUtils.startActivity(mBundle, UnityPlayerActivity.class);
                break;
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