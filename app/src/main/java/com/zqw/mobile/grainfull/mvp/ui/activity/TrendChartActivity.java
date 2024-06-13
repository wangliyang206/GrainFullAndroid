package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerTrendChartComponent;
import com.zqw.mobile.grainfull.mvp.contract.TrendChartContract;
import com.zqw.mobile.grainfull.mvp.model.entity.SimpleCandleViewTxtBean;
import com.zqw.mobile.grainfull.mvp.model.entity.SimpleLinearChart2TxtBean;
import com.zqw.mobile.grainfull.mvp.model.entity.SimpleMultipleColumnTxtBean;
import com.zqw.mobile.grainfull.mvp.presenter.TrendChartPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.trendchart.SimpleCandleView;
import com.zqw.mobile.grainfull.mvp.ui.widget.trendchart.SimpleLinearChart;
import com.zqw.mobile.grainfull.mvp.ui.widget.trendchart.SimpleLinearChart2;
import com.zqw.mobile.grainfull.mvp.ui.widget.trendchart.SimpleMultipleColumnView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Description: 趋势图
 * <p>
 * Created on 2024/03/11 15:16
 *
 * @author 赤槿
 * module name is TrendChartActivity
 */
public class TrendChartActivity extends BaseActivity<TrendChartPresenter> implements TrendChartContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.view_trendchart_linear)
    SimpleLinearChart2 viewLinearOne;                                                               // 效果一

    @BindView(R.id.view_trendchart_linears)
    SimpleLinearChart viewLinearTwo;                                                                // 效果二

    @BindView(R.id.view_trendchart_multiple)
    SimpleMultipleColumnView viewLinearThree;                                                       // 效果三

    @BindView(R.id.view_trendchart_candle)
    SimpleCandleView viewCandle;                                                                    // 效果四
    /*------------------------------------------------业务区域------------------------------------------------*/


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTrendChartComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_trend_chart;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("趋势图");


        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "trend_chart_open");
        loadDataOne();
        loadDataTwo();
        loadDataThree();
        loadDataFour();
    }

    /**
     * 加载数据 - 第一个趋势图
     */
    private void loadDataOne() {
        //Y轴
        List<SimpleLinearChart2.OnSimpleLinearChart2XyAxisTextRealization<SimpleLinearChart2TxtBean>> simpleLinearChart2YAxisData = new ArrayList<>();
        simpleLinearChart2YAxisData.add(new SimpleLinearChart2TxtBean("5w", 50000));
        simpleLinearChart2YAxisData.add(new SimpleLinearChart2TxtBean("4w", 40000));
        simpleLinearChart2YAxisData.add(new SimpleLinearChart2TxtBean("3w", 30000));
        simpleLinearChart2YAxisData.add(new SimpleLinearChart2TxtBean("2w", 20000));
        simpleLinearChart2YAxisData.add(new SimpleLinearChart2TxtBean("1w", 10000));
        simpleLinearChart2YAxisData.add(new SimpleLinearChart2TxtBean("0", 0));
        SimpleLinearChart2.SimpleLinearChart2CoordinateAxisBean simpleLinearChart2YAxisBean = new SimpleLinearChart2.SimpleLinearChart2CoordinateAxisBean();
        simpleLinearChart2YAxisBean.setCoordinateAxisTextData(simpleLinearChart2YAxisData, true);
        //X轴
        List<SimpleLinearChart2.OnSimpleLinearChart2XyAxisTextRealization<SimpleLinearChart2TxtBean>> simpleLinearChart2XAxisData = new ArrayList<>();
        simpleLinearChart2XAxisData.add(new SimpleLinearChart2TxtBean("4月", 0));
        simpleLinearChart2XAxisData.add(new SimpleLinearChart2TxtBean("5月", 0));
        simpleLinearChart2XAxisData.add(new SimpleLinearChart2TxtBean("6月", 0));
        simpleLinearChart2XAxisData.add(new SimpleLinearChart2TxtBean("7月", 0));
        simpleLinearChart2XAxisData.add(new SimpleLinearChart2TxtBean("8月", 0));
        simpleLinearChart2XAxisData.add(new SimpleLinearChart2TxtBean("9月", 0));
        simpleLinearChart2XAxisData.add(new SimpleLinearChart2TxtBean("10月", 0));
        simpleLinearChart2XAxisData.add(new SimpleLinearChart2TxtBean("11月", 0));
        simpleLinearChart2XAxisData.add(new SimpleLinearChart2TxtBean("12月", 0));
        simpleLinearChart2XAxisData.add(new SimpleLinearChart2TxtBean("1月", 0));
        simpleLinearChart2XAxisData.add(new SimpleLinearChart2TxtBean("2月", 0));
        simpleLinearChart2XAxisData.add(new SimpleLinearChart2TxtBean("3月", 0));
        SimpleLinearChart2.SimpleLinearChart2CoordinateAxisBean simpleLinearChart2XAxisBean = new SimpleLinearChart2.SimpleLinearChart2CoordinateAxisBean();
        simpleLinearChart2XAxisBean.setCoordinateAxisTextData(simpleLinearChart2XAxisData, false);
        //内容轴
        List<SimpleLinearChart2.SimpleLinearChart2CoordinateAxisBean> contentAxisDataList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<SimpleLinearChart2.OnSimpleLinearChart2XyAxisTextRealization<SimpleLinearChart2TxtBean>> simpleLinearChart2ContentAxisData = new ArrayList<>();
            simpleLinearChart2ContentAxisData.add(new SimpleLinearChart2TxtBean("4月", (i + 1) * 5000));
            simpleLinearChart2ContentAxisData.add(new SimpleLinearChart2TxtBean("5月", (i + 1) * 10000));
            simpleLinearChart2ContentAxisData.add(new SimpleLinearChart2TxtBean("6月", (i + 1) * 9000));
            simpleLinearChart2ContentAxisData.add(new SimpleLinearChart2TxtBean("7月", (i + 1) * 12000));
            simpleLinearChart2ContentAxisData.add(new SimpleLinearChart2TxtBean("8月", (i + 1) * 9000));
            simpleLinearChart2ContentAxisData.add(new SimpleLinearChart2TxtBean("9月", (i + 1) * 8000));
            simpleLinearChart2ContentAxisData.add(new SimpleLinearChart2TxtBean("10月", (i + 1) * 7000));
            simpleLinearChart2ContentAxisData.add(new SimpleLinearChart2TxtBean("11月", (i + 1) * 8000));
            simpleLinearChart2ContentAxisData.add(new SimpleLinearChart2TxtBean("12月", (i + 1) * 11000));
            simpleLinearChart2ContentAxisData.add(new SimpleLinearChart2TxtBean("1月", (i + 1) * 10000));
            simpleLinearChart2ContentAxisData.add(new SimpleLinearChart2TxtBean("2月", (i + 1) * 9000));
            simpleLinearChart2ContentAxisData.add(new SimpleLinearChart2TxtBean("3月", (i + 1) * 10000));
            SimpleLinearChart2.SimpleLinearChart2CoordinateAxisBean simpleLinearChart2ContentAxisBean = new SimpleLinearChart2.SimpleLinearChart2CoordinateAxisBean();
            simpleLinearChart2ContentAxisBean.setRemark("202" + i + "年");
            simpleLinearChart2ContentAxisBean.setChecked(i == 1);
            simpleLinearChart2ContentAxisBean.setCoordinateAxisTextData(simpleLinearChart2ContentAxisData, false);
            contentAxisDataList.add(simpleLinearChart2ContentAxisBean);
        }

        viewLinearOne.setData(simpleLinearChart2YAxisBean, simpleLinearChart2XAxisBean, contentAxisDataList);
    }

    /**
     * 第二效果
     */
    private void loadDataTwo() {
//        viewLinearTwo.setOnClickListener(v -> {
//            List<SimpleLinearChart.SimpleLinearChartBean> list = new ArrayList<>();
//            list.add(new SimpleLinearChart.SimpleLinearChartBean(524, "1/1", "524"));
//            list.add(new SimpleLinearChart.SimpleLinearChartBean(532, "1/2", "532"));
//            list.add(new SimpleLinearChart.SimpleLinearChartBean(504, "1/3", "504"));
//            list.add(new SimpleLinearChart.SimpleLinearChartBean(517, "1/4", "517"));
//            list.add(new SimpleLinearChart.SimpleLinearChartBean(535, "1/5", "535"));
//            list.add(new SimpleLinearChart.SimpleLinearChartBean(549, "1/6", "549"));
//            viewLinearTwo.setData(list, 504, 549, true);
//        });

        viewLinearTwo.post(() -> {
            List<SimpleLinearChart.SimpleLinearChartBean> list = new ArrayList<>();
            list.add(new SimpleLinearChart.SimpleLinearChartBean(524, "1/1", "524"));
            list.add(new SimpleLinearChart.SimpleLinearChartBean(532, "1/2", "532"));
            list.add(new SimpleLinearChart.SimpleLinearChartBean(504, "1/3", "504"));
            list.add(new SimpleLinearChart.SimpleLinearChartBean(517, "1/4", "517"));
            list.add(new SimpleLinearChart.SimpleLinearChartBean(535, "1/5", "535"));
            list.add(new SimpleLinearChart.SimpleLinearChartBean(549, "1/6", "549"));
            viewLinearTwo.setData(list, 504, 549, true);
        });
    }

    /**
     * 第三效果
     */
    private void loadDataThree() {
        //Y轴
        List<SimpleMultipleColumnView.OnSimpleMultipleColumnViewXyAxisTextRealization<SimpleMultipleColumnTxtBean>> simpleMultipleColumnViewYAxisData = new ArrayList<>();
        simpleMultipleColumnViewYAxisData.add(new SimpleMultipleColumnTxtBean("25k", 25000));
        simpleMultipleColumnViewYAxisData.add(new SimpleMultipleColumnTxtBean("20k", 20000));
        simpleMultipleColumnViewYAxisData.add(new SimpleMultipleColumnTxtBean("15k", 15000));
        simpleMultipleColumnViewYAxisData.add(new SimpleMultipleColumnTxtBean("10k", 10000));
        simpleMultipleColumnViewYAxisData.add(new SimpleMultipleColumnTxtBean("5k", 5000));
        simpleMultipleColumnViewYAxisData.add(new SimpleMultipleColumnTxtBean("1k", 1000));
        SimpleMultipleColumnView.SimpleMultipleColumnViewCoordinateAxisBean simpleMultipleColumnViewYAxisBean = new SimpleMultipleColumnView.SimpleMultipleColumnViewCoordinateAxisBean();
        simpleMultipleColumnViewYAxisBean.setCoordinateAxisTextData(simpleMultipleColumnViewYAxisData, true);
        //X轴
        List<SimpleMultipleColumnView.OnSimpleMultipleColumnViewXyAxisTextRealization<SimpleMultipleColumnTxtBean>> simpleMultipleColumnViewXAxisData = new ArrayList<>();
        simpleMultipleColumnViewXAxisData.add(new SimpleMultipleColumnTxtBean("1月", 25000));
        simpleMultipleColumnViewXAxisData.add(new SimpleMultipleColumnTxtBean("2月", 20000));
        simpleMultipleColumnViewXAxisData.add(new SimpleMultipleColumnTxtBean("3月", 15000));
        simpleMultipleColumnViewXAxisData.add(new SimpleMultipleColumnTxtBean("4月", 10000));
        simpleMultipleColumnViewXAxisData.add(new SimpleMultipleColumnTxtBean("5月", 5000));
        simpleMultipleColumnViewXAxisData.add(new SimpleMultipleColumnTxtBean("6月", 1000));
        simpleMultipleColumnViewXAxisData.add(new SimpleMultipleColumnTxtBean("7月", 1000));
        simpleMultipleColumnViewXAxisData.add(new SimpleMultipleColumnTxtBean("8月", 1000));
        simpleMultipleColumnViewXAxisData.add(new SimpleMultipleColumnTxtBean("9月", 1000));
        simpleMultipleColumnViewXAxisData.add(new SimpleMultipleColumnTxtBean("10月", 1000));
        simpleMultipleColumnViewXAxisData.add(new SimpleMultipleColumnTxtBean("11月", 1000));
        simpleMultipleColumnViewXAxisData.add(new SimpleMultipleColumnTxtBean("12月", 1000));
        SimpleMultipleColumnView.SimpleMultipleColumnViewCoordinateAxisBean simpleMultipleColumnViewXAxisBean = new SimpleMultipleColumnView.SimpleMultipleColumnViewCoordinateAxisBean();
        simpleMultipleColumnViewXAxisBean.setCoordinateAxisTextData(simpleMultipleColumnViewXAxisData, false);
        //内容轴
        List<SimpleMultipleColumnView.OnSimpleMultipleColumnViewXyAxisTextRealization<SimpleMultipleColumnTxtBean>> simpleMultipleColumnViewContentAxisData = new ArrayList<>();
        simpleMultipleColumnViewContentAxisData.add(new SimpleMultipleColumnTxtBean("1月", 0f, 0f, 0f, 0f, 0f));
        simpleMultipleColumnViewContentAxisData.add(new SimpleMultipleColumnTxtBean("2月", 0.1f, 0.2f, 0.3f, 0.3f, 0.1f));
        simpleMultipleColumnViewContentAxisData.add(new SimpleMultipleColumnTxtBean("3月", 0.3f, 0.2f, 0.1f, 0.1f, 0.3f));
        simpleMultipleColumnViewContentAxisData.add(new SimpleMultipleColumnTxtBean("4月", 0.1f, 0.2f, 0.3f, 0.3f, 0.1f));
        simpleMultipleColumnViewContentAxisData.add(new SimpleMultipleColumnTxtBean("5月", 0.3f, 0.2f, 0.1f, 0.1f, 0.3f));
        simpleMultipleColumnViewContentAxisData.add(new SimpleMultipleColumnTxtBean("6月", 0.1f, 0.2f, 0.3f, 0.3f, 0.1f));
        simpleMultipleColumnViewContentAxisData.add(new SimpleMultipleColumnTxtBean("7月", 0.3f, 0.2f, 0.1f, 0.1f, 0.3f));
        simpleMultipleColumnViewContentAxisData.add(new SimpleMultipleColumnTxtBean("8月", 0.1f, 0.2f, 0.3f, 0.3f, 0.1f));
        simpleMultipleColumnViewContentAxisData.add(new SimpleMultipleColumnTxtBean("9月", 0.3f, 0.2f, 0.1f, 0.1f, 0.3f));
        simpleMultipleColumnViewContentAxisData.add(new SimpleMultipleColumnTxtBean("10月", 0.1f, 0.2f, 0.3f, 0.3f, 0.1f));
        simpleMultipleColumnViewContentAxisData.add(new SimpleMultipleColumnTxtBean("11月", 0.3f, 0.2f, 0.1f, 0.1f, 0.3f));
        simpleMultipleColumnViewContentAxisData.add(new SimpleMultipleColumnTxtBean("12月", 0.1f, 0.2f, 0.3f, 0.3f, 0.1f));
        SimpleMultipleColumnView.SimpleMultipleColumnViewCoordinateAxisBean simpleMultipleColumnViewContentAxisBean = new SimpleMultipleColumnView.SimpleMultipleColumnViewCoordinateAxisBean();
        simpleMultipleColumnViewContentAxisBean.setCoordinateAxisTextData(simpleMultipleColumnViewContentAxisData, false);


        viewLinearThree.setData(simpleMultipleColumnViewYAxisBean, simpleMultipleColumnViewXAxisBean, simpleMultipleColumnViewContentAxisBean);
    }

    private void loadDataFour(){
        //Y轴
        List<SimpleCandleView.OnSimpleCandleViewXyAxisTextRealization<SimpleCandleViewTxtBean>> yAxisData = new ArrayList<>();
        yAxisData.add(new SimpleCandleViewTxtBean("25k", 25000));
        yAxisData.add(new SimpleCandleViewTxtBean("20k", 20000));
        yAxisData.add(new SimpleCandleViewTxtBean("15k", 15000));
        yAxisData.add(new SimpleCandleViewTxtBean("10k", 10000));
        yAxisData.add(new SimpleCandleViewTxtBean("5k", 5000));
        yAxisData.add(new SimpleCandleViewTxtBean("1k", 1000));
        SimpleCandleView.SimpleCandleViewCoordinateAxisBean yAxisBean = new SimpleCandleView.SimpleCandleViewCoordinateAxisBean();
        yAxisBean.setCoordinateAxisTextData(yAxisData, true);
        //X轴
        List<SimpleCandleView.OnSimpleCandleViewXyAxisTextRealization<SimpleCandleViewTxtBean>> xAxisData = new ArrayList<>();
        xAxisData.add(new SimpleCandleViewTxtBean("4月", 25000));
        xAxisData.add(new SimpleCandleViewTxtBean("5月", 20000));
        xAxisData.add(new SimpleCandleViewTxtBean("6月", 15000));
        xAxisData.add(new SimpleCandleViewTxtBean("7月", 10000));
        xAxisData.add(new SimpleCandleViewTxtBean("8月", 5000));
        xAxisData.add(new SimpleCandleViewTxtBean("9月", 1000));
        xAxisData.add(new SimpleCandleViewTxtBean("10月", 1000));
        xAxisData.add(new SimpleCandleViewTxtBean("11月", 1000));
        xAxisData.add(new SimpleCandleViewTxtBean("12月", 1000));
        xAxisData.add(new SimpleCandleViewTxtBean("1月", 1000));
        xAxisData.add(new SimpleCandleViewTxtBean("2月", 1000));
        xAxisData.add(new SimpleCandleViewTxtBean("3月", 1000));
        SimpleCandleView.SimpleCandleViewCoordinateAxisBean xAxisBean = new SimpleCandleView.SimpleCandleViewCoordinateAxisBean();
        xAxisBean.setCoordinateAxisTextData(xAxisData, false);
        //内容轴
        List<SimpleCandleView.OnSimpleCandleViewXyAxisTextRealization<SimpleCandleViewTxtBean>> contentAxisData = new ArrayList<>();
        contentAxisData.add(new SimpleCandleViewTxtBean("4月", 20000, 15000, 4000, 2000));
        contentAxisData.add(new SimpleCandleViewTxtBean("5月", 19000, 14000, 6000, 4500));
        contentAxisData.add(new SimpleCandleViewTxtBean("6月", 22500, 18000, 6000, 4000));
        contentAxisData.add(new SimpleCandleViewTxtBean("7月", 20500, 16000, 10000, 5000));
        contentAxisData.add(new SimpleCandleViewTxtBean("8月", 22000, 18000, 12000, 10500));
        contentAxisData.add(new SimpleCandleViewTxtBean("9月", 22000, 18500, 7000, 6000));
        contentAxisData.add(new SimpleCandleViewTxtBean("10月", 19000, 16000, 13000, 11000));
        contentAxisData.add(new SimpleCandleViewTxtBean("11月", 18500, 15500, 7000, 6000));
        contentAxisData.add(new SimpleCandleViewTxtBean("12月", 20500, 18000, 8100, 7800));
        contentAxisData.add(new SimpleCandleViewTxtBean("1月", 21000, 16500, 4000, 4000));
        contentAxisData.add(new SimpleCandleViewTxtBean("2月", 23000, 18000, 13500, 13500));
        contentAxisData.add(new SimpleCandleViewTxtBean("3月", 17000, 13000, 4900, 1200));
        SimpleCandleView.SimpleCandleViewCoordinateAxisBean contentAxisBean = new SimpleCandleView.SimpleCandleViewCoordinateAxisBean();
        contentAxisBean.setCoordinateAxisTextData(contentAxisData, false);


        viewCandle.setData(yAxisBean, xAxisBean, contentAxisBean);
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