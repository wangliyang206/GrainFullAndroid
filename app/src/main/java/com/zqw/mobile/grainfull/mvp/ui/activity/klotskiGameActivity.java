package com.zqw.mobile.grainfull.mvp.ui.activity;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.utils.ArmsUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.di.component.DaggerklotskiGameComponent;
import com.zqw.mobile.grainfull.mvp.contract.klotskiGameContract;
import com.zqw.mobile.grainfull.mvp.presenter.klotskiGamePresenter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.ui.widget.KlotskiView;
import com.zqw.mobile.grainfull.mvp.ui.widget.TurntableView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:华容道
 * <p>
 * Created on 2023/01/20 18:00
 *
 * @author 赤槿
 * module name is klotskiGameActivity
 */
public class klotskiGameActivity extends BaseActivity<klotskiGamePresenter> implements klotskiGameContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_klotski_game)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.klvi_klotskigame_view)
    KlotskiView klviView;
    /*------------------------------------------------业务区域------------------------------------------------*/

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerklotskiGameComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_klotski_game;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("华容道");

    }

    @OnClick({
            R.id.btn_klotskigame_start,                                                             // 开始游戏
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_klotskigame_start:                                                        // 开始游戏
                if (!CommonUtils.isDoubleClick()) {
                    v.setEnabled(false);
                    klviView.startGame();
                }
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