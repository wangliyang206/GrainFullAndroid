package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerStatisticalEffectsComponent;
import com.zqw.mobile.grainfull.mvp.contract.StatisticalEffectsContract;
import com.zqw.mobile.grainfull.mvp.presenter.StatisticalEffectsPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.SimpleSpiderView;
import com.zqw.mobile.grainfull.mvp.ui.widget.trendchart.SimpleHalfPieChart;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Description: 统计效果
 * <p>
 * Created on 2024/03/12 13:46
 *
 * @author 赤槿
 * module name is StatisticalEffectsActivity
 */
public class StatisticalEffectsActivity extends BaseActivity<StatisticalEffectsPresenter> implements StatisticalEffectsContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.view_statisticaleffects_halfPieChart)
    SimpleHalfPieChart simpleHalfPieChart;                                                          // 效果二

    @BindView(R.id.view_statisticaleffects_spider)
    SimpleSpiderView simpleSpiderView;                                                              // 效果三
    /*------------------------------------------------业务区域------------------------------------------------*/


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerStatisticalEffectsComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_statistical_effects;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("统计效果");

        loadDataOne();
        loadDataTwo();
    }

    private void loadDataOne() {
        simpleHalfPieChart.setOnClickListener(v -> {
            List<SimpleHalfPieChart.SimpleHalfPieChartBean> list = new ArrayList<>();
            list.add(new SimpleHalfPieChart.SimpleHalfPieChartBean("医药", "19%", 0.19f, Color.parseColor("#7A95FF")));
            list.add(new SimpleHalfPieChart.SimpleHalfPieChartBean("艺术", "11%", 0.11f, Color.parseColor("#3A95FF")));
            list.add(new SimpleHalfPieChart.SimpleHalfPieChartBean("语言", "22%", 0.22f, Color.parseColor("#0A65FF")));
            list.add(new SimpleHalfPieChart.SimpleHalfPieChartBean("财经", "5%", 0.05f, Color.parseColor("#ff95FF")));
            list.add(new SimpleHalfPieChart.SimpleHalfPieChartBean("财经2", "5%", 0.05f, Color.parseColor("#ff95FF")));
            list.add(new SimpleHalfPieChart.SimpleHalfPieChartBean("理工", "13%", 0.13f, Color.parseColor("#09ff99")));
            list.add(new SimpleHalfPieChart.SimpleHalfPieChartBean("农林", "15%", 0.15f, Color.parseColor("#76582F")));
            list.add(new SimpleHalfPieChart.SimpleHalfPieChartBean("军事", "5%", 0.05f, Color.parseColor("#8541FF")));
            list.add(new SimpleHalfPieChart.SimpleHalfPieChartBean("军事2", "5%", 0.05f, Color.parseColor("#8541FF")));
            simpleHalfPieChart.setData("倾向", list, true);
        });
    }

    private void loadDataTwo() {
        simpleSpiderView.setOnClickListener(v -> {
            List<SimpleSpiderView.SimpleSpiderViewBean> data = new ArrayList<>();
            data.add(new SimpleSpiderView.SimpleSpiderViewBean("B", "目标规划", 0.6f));
            data.add(new SimpleSpiderView.SimpleSpiderViewBean("A+", "自我认知", 0.9f));
            data.add(new SimpleSpiderView.SimpleSpiderViewBean("A", "学习状态", 0.67f));
            data.add(new SimpleSpiderView.SimpleSpiderViewBean("A+", "心理健康", 1.0f));
            data.add(new SimpleSpiderView.SimpleSpiderViewBean("A", "生涯学习", 0.8f));
            simpleSpiderView.setData(data, true);
        });
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