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
import com.zqw.mobile.grainfull.mvp.ui.widget.RotateView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:交给苍天
 * <p>
 * Created on 2022/12/27 16:16
 *
 * @author 赤槿
 * module name is DecisionActivity
 */
public class DecisionActivity extends BaseActivity<DecisionPresenter> implements DecisionContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.rovi_decisionactivity_turntable)
    RotateView mRotateView;

    /*------------------------------------------------业务区域------------------------------------------------*/
    // 转盘中的图片
    private List<Integer> images = new ArrayList<>();
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
        setTitle("交给苍天");

        names = Arrays.asList(getResources().getStringArray(R.array.LotteryTurntable));

        // 初始化-转盘
        images.add(R.mipmap.role);
        images.add(R.mipmap.sports);
        images.add(R.mipmap.words);
        images.add(R.mipmap.action);
        images.add(R.mipmap.combat);
        images.add(R.mipmap.moba);
        mRotateView.setSectorColor(Color.rgb(60, 149, 143),Color.rgb(200, 255, 251));
        mRotateView.setImageIcon(images);
        mRotateView.setStrName(names);
        // 获取到位置
        mRotateView.setOnCallBackPosition(pos -> showMessage("位置：" + names.get(pos)));
    }

    @OnClick({
            R.id.imvi_decisionactivity_one_start,                                                   // 转盘 - 开始按钮
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvi_decisionactivity_one_start:                                              // 转盘 - 开始按钮
                // -1为随机数或者指定位置，但必须小于总个数
                mRotateView.startAnimation(-1);
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