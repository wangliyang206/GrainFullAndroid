package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerDecisionComponent;
import com.zqw.mobile.grainfull.mvp.contract.DecisionContract;
import com.zqw.mobile.grainfull.mvp.presenter.DecisionPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.TurntableView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:做个决定
 * <p>
 * Created on 2022/12/27 16:16
 *
 * @author 赤槿
 * module name is DecisionActivity
 */
public class DecisionActivity extends BaseActivity<DecisionPresenter> implements DecisionContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.rovi_decisionactivity_turntable)
    TurntableView mTurntable;

    /*------------------------------------------------业务区域------------------------------------------------*/
    // 转盘中的文字
    private List<String> names = new ArrayList<>();

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDecisionComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_decision;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("做个决定");

        // 加载默认数据
        names = Arrays.asList(getResources().getStringArray(R.array.foodName));

        changeColors();
        changeDatas();
    }

    /**
     * 变更扇形颜色
     */
    private void changeColors() {
        int num = names.size();
        ArrayList<Integer> colors = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            if (num % 2 == 0) {
                // 两个颜色
                if (i % 2 == 0) {
                    colors.add(Color.parseColor("#3c76cc"));
                } else {
                    colors.add(Color.parseColor("#97b9dc"));
                }
            } else {
                // 三个颜色
                if (i == num - 1) {
                    // 最后一个颜色
//                    colors.add(getResources().getColor(R.color.colorAccent));
                    colors.add(Color.parseColor("#eed1bd"));
                } else {
                    if (i % 2 == 0) {
                        colors.add(Color.parseColor("#3c76cc"));
                    } else {
                        colors.add(Color.parseColor("#97b9dc"));
                    }
                }

            }
        }

        mTurntable.setBackColor(colors);
    }

    /**
     * 变更数据
     */
    private void changeDatas() {
        int num = names.size();
        mTurntable.setDatas(num, names);
    }

    @OnClick({
            R.id.imvi_decisionactivity_one_start,                                                   // 转盘 - 开始按钮
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvi_decisionactivity_one_start:                                              // 转盘 - 开始按钮
                // 以下为随即抽奖
                mTurntable.startRotate(new TurntableView.ITurntableListener() {
                    @Override
                    public void onStart() {
                        showMessage("开始抽奖");
                    }

                    @Override
                    public void onEnd(int position, String name) {
                        showMessage("抽中抽奖:" + name);
                    }
                });

                // 以下为指定抽奖
//                mTurntable.startRotate(3,new TurntableView.ITurntableListener() {
//                    @Override
//                    public void onStart() {
//                        showMessage("开始抽奖");
//                    }
//
//                    @Override
//                    public void onEnd(int position, String name) {
//                        showMessage("抽中抽奖:" + name);
//                    }
//                });
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