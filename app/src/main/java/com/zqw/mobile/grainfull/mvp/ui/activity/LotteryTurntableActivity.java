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
import com.zqw.mobile.grainfull.di.component.DaggerLotteryTurntableComponent;
import com.zqw.mobile.grainfull.mvp.contract.LotteryTurntableContract;
import com.zqw.mobile.grainfull.mvp.presenter.LotteryTurntablePresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.RotateLayoutView;
import com.zqw.mobile.grainfull.mvp.ui.widget.RotateView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:抽奖转盘
 * <p>
 * Created on 2022/12/27 17:08
 *
 * @author 赤槿
 * module name is LotteryTurntableActivity
 */
public class LotteryTurntableActivity extends BaseActivity<LotteryTurntablePresenter> implements LotteryTurntableContract.View {
    @BindView(R.id.rovi_lotteryturntable_one)
    RotateView roviOne;

    @BindView(R.id.rovi_lotteryturntable_two)
    RotateView roviTwo;

    @BindView(R.id.rlvi_lotteryturntable_three)
    RotateLayoutView rlviThree;

    /*------------------------------------------------业务区域------------------------------------------------*/
    // 转盘中的图片
    private List<Integer> images = new ArrayList<>();
    // 转盘中的文字
    private List<String> names = new ArrayList<>();

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLotteryTurntableComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_lottery_turntable;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("抽奖转盘");

        // 初始化数据
        images.add(R.mipmap.role);
        images.add(R.mipmap.sports);
        images.add(R.mipmap.words);
        images.add(R.mipmap.action);
        images.add(R.mipmap.combat);
        images.add(R.mipmap.moba);
        names = Arrays.asList(getResources().getStringArray(R.array.LotteryTurntable));

        // 初始化-第一个转盘
        roviOne.setImageIcon(images);
        roviOne.setStrName(names);
        // 获取到位置
        roviOne.setOnCallBackPosition(pos -> showMessage("位置：" + names.get(pos)));


        // 初始化-第二个转盘
        roviTwo.setImageIcon(images);
        roviTwo.setStrName(names);
        // 获取到位置
        roviTwo.setOnCallBackPosition(pos -> showMessage("位置：" + names.get(pos)));

        // 初始化-第三个转盘
        rlviThree.setImageIcon(images);
        rlviThree.setStrName(names);
        // 获取到位置
        rlviThree.setOnCallBackPosition(4, pos -> showMessage("位置：" + names.get(pos)));
    }

    @OnClick({
            R.id.imvi_lotteryturntable_one_start,                                                   // 第一个转盘 - 开始按钮
            R.id.imvi_lotteryturntable_two_start,                                                   // 第二个转盘 - 开始按钮
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvi_lotteryturntable_one_start:                                              // 第一个转盘 - 开始按钮
                // -1为随机数或者指定位置，但必须小于总个数
                roviOne.startAnimation(-1);
                break;
            case R.id.imvi_lotteryturntable_two_start:                                              // 第二个转盘 - 开始按钮
                // -1为随机数或者指定位置，但必须小于总个数
                roviTwo.startAnimation(-1);
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