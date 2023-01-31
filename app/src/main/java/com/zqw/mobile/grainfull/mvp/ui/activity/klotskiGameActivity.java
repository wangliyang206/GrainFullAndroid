package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.di.component.DaggerklotskiGameComponent;
import com.zqw.mobile.grainfull.mvp.contract.klotskiGameContract;
import com.zqw.mobile.grainfull.mvp.presenter.klotskiGamePresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.KlotskiView;

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

    @BindView(R.id.txvi_klotskigame_level)
    TextView txviLevel;
    @BindView(R.id.txvi_klotskigame_steps)
    TextView txviSteps;
    @BindView(R.id.klvi_klotskigame_view)
    KlotskiView klviView;

    @BindView(R.id.btn_klotskigame_start)
    Button btnStart;
    /*------------------------------------------------业务区域------------------------------------------------*/
    // 当前关卡(共三关)
    private int mLevel = 1;

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

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "klotski_game_open");

        // 注册游戏监听
        klviView.setOnClickChangeListener((isSuccess, steps) -> {
            // 显示步数
            txviSteps.setText(String.valueOf(steps));
            // 判断是否成功
            if (isSuccess) {
                showSucc(steps);
            }
        });
    }

    @OnClick({
            R.id.btn_klotskigame_start,                                                             // 开始游戏
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_klotskigame_start:                                                        // 开始游戏
                if (!CommonUtils.isDoubleClick()) {
                    // 友盟统计 - 自定义事件
                    MobclickAgent.onEvent(getApplicationContext(), "klotski_game");
                    // 隐藏按钮
                    v.setVisibility(View.GONE);
                    // 开始游戏
                    startGame();
                }
                break;
        }
    }

    /**
     * 开始游戏
     */
    private void startGame() {
        txviSteps.setText("0");
        txviLevel.setText(String.valueOf(mLevel));
        if (mLevel == 2) {
            klviView.setImageResource(R.mipmap.klotski_level_2);
        } else if (mLevel == 3) {
            klviView.setImageResource(R.mipmap.klotski_level_3);
        }
        klviView.startGame(mLevel);
    }

    /**
     * 显示成功
     */
    private void showSucc(int steps) {
        if (mLevel == 3) {
            new AlertDialog.Builder(this)
                    .setTitle("拼图成功")
                    .setCancelable(false)
                    .setMessage("恭喜您，已完成所有通关！本次移动了" + steps + "次。")
                    .setPositiveButton("重新玩", (dialog, which) -> {
                        mLevel = 1;
                        startGame();
                    })
                    .setNegativeButton("不玩了", (dialog, which) -> {
                        btnStart.setVisibility(View.VISIBLE);
                    })
                    .create()
                    .show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("拼图成功")
                    .setCancelable(false)
                    .setMessage("恭喜您，第" + mLevel + "关拼图成功！移动了" + steps + "次，\n是否继续下一关？")
                    .setPositiveButton("下一关", (dialog, which) -> {
                        mLevel++;
                        startGame();
                    })
                    .setNegativeButton("不玩了", (dialog, which) -> {
                        btnStart.setVisibility(View.VISIBLE);
                    })
                    .create()
                    .show();
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