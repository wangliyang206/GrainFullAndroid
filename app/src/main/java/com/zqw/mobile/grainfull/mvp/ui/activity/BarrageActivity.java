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
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerBarrageComponent;
import com.zqw.mobile.grainfull.mvp.contract.BarrageContract;
import com.zqw.mobile.grainfull.mvp.presenter.BarragePresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.barrage.AntiOverlapDanmakuView;
import com.zqw.mobile.grainfull.mvp.ui.widget.barrage.DanmakuView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created on 2026/04/01 17:22
 *
 * @author Love_xie
 * module name is BarrageActivity
 */
public class BarrageActivity extends BaseActivity<BarragePresenter> implements BarrageContract.View {
    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.view_barrage_one)
    DanmakuView viewBarrageOne;

    @BindView(R.id.view_barrage_two)
    AntiOverlapDanmakuView viewBarrageTwo;
    /*------------------------------------------业务信息------------------------------------------*/
    private final Random mRandom = new Random();

    @Override
    protected void onResume() {
        super.onResume();
        viewBarrageOne.start();
        viewBarrageTwo.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewBarrageOne.pause();
        viewBarrageTwo.pause();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBarrageComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_barrage;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("弹幕特效");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "barrage");
    }

    @OnClick({
            R.id.btn_barrage_start,
            R.id.btn_barrage_clear
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_barrage_start:
                onClickStart();
                break;
            case R.id.btn_barrage_clear:
                viewBarrageOne.clear();
                viewBarrageTwo.clear();
                break;
        }
    }

    /**
     * 点击开始弹幕
     */
    private void onClickStart() {
        List<String> list = new ArrayList<>();
        list.add("666666！");
        list.add("原生弹幕太丝滑了");
        list.add("Android Java 实现");
        list.add("无第三方依赖");
        list.add("太强了吧！");
        list.add("这效果可以直接上线");
        list.add("太强了！这弹幕完全不重叠");
        list.add("原生Java实现就是香");
        list.add("轨道算法太稳定了");
        list.add("666666");
        list.add("Android 开发不易");
        list.add("防重叠效果满分");
        list.add("这就是专业弹幕系统");

        // 方案一
        viewBarrageOne.addDanmakuList(list);

        // 方案二
        viewBarrageTwo.addDanmakuList(list);
    }

    @Override
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