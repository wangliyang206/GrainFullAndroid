package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerSpiderWebGradeComponent;
import com.zqw.mobile.grainfull.mvp.contract.SpiderWebGradeContract;
import com.zqw.mobile.grainfull.mvp.presenter.SpiderWebGradePresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.spider.ModelSpider;
import com.zqw.mobile.grainfull.mvp.ui.widget.spider.RxCobwebView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

/**
 * Description:蜘蛛网等级
 * <p>
 * Created on 2023/01/03 16:06
 *
 * @author 赤槿
 * module name is SpiderWebGradeActivity
 */
public class SpiderWebGradeActivity extends BaseActivity<SpiderWebGradePresenter> implements SpiderWebGradeContract.View, SeekBar.OnSeekBarChangeListener {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_spider_web_grade)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.view_spiderwebgrade_cobweb)
    RxCobwebView mRxCobwebView;

    @BindView(R.id.seekbar_level)
    SeekBar seekbarLevel;

    @BindView(R.id.seekbar_spider_number)
    SeekBar seekbarSpiderNumber;
    /*------------------------------------------------业务区域------------------------------------------------*/
    /**
     * 数据源
     */
    private final String[] nameStrs = {
            "金钱", "能力", "美貌", "智慧", "交际",
            "口才", "力量", "智力", "体力", "体质",
            "敏捷", "精神", "耐力", "精通", "急速",
            "暴击", "回避", "命中", "跳跃", "反应",
            "幸运", "魅力", "感知", "活力", "意志"
    };

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSpiderWebGradeComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_spider_web_grade;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("蜘蛛网等级");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "spider_web_grade");
        seekbarLevel.setOnSeekBarChangeListener(this);
        seekbarSpiderNumber.setOnSeekBarChangeListener(this);
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekbar_level:
                mRxCobwebView.setSpiderMaxLevel(progress + 1);
                break;
            case R.id.seekbar_spider_number:
                int number = progress + 1;
                List<ModelSpider> modelSpiders = new ArrayList<>();
                for (int i = 0; i < number; i++) {
                    modelSpiders.add(new ModelSpider(nameStrs[i], 1 + new Random().nextInt(mRxCobwebView.getSpiderMaxLevel())));
                }
                mRxCobwebView.setSpiderList(modelSpiders);
                break;
            default:
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}