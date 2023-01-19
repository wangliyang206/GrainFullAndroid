package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.TimeUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.uapp.param.UmengUappAllAppData;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.di.component.DaggerUmDataStatisticsComponent;
import com.zqw.mobile.grainfull.mvp.contract.UmDataStatisticsContract;
import com.zqw.mobile.grainfull.mvp.presenter.UmDataStatisticsPresenter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.SevenStatisticsAdapter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.SingleDurationAdapter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.UmEventAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:友盟 - 数据统计
 * <p>
 * Created on 2023/01/16 15:22
 *
 * @author 赤槿
 * module name is UmDataStatisticsActivity
 */
public class UmDataStatisticsActivity extends BaseActivity<UmDataStatisticsPresenter> implements UmDataStatisticsContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_um_data_statistics)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.txvi_umdatastatistics_cumulativeusers)
    TextView txviCumulativeUsers;                                                                   // 累计用户

    @BindView(R.id.lila_umdatastatistics_newuser_layout)
    LinearLayout lilaNewUser;                                                                       // 新增用户
    @BindView(R.id.txvi_umdatastatistics_newuser_today)
    TextView txviNewUserToday;                                                                      // 新增用户 - 今日
    @BindView(R.id.txvi_umdatastatistics_newuser_yesterday)
    TextView txviNewUserYesterday;                                                                  // 新增用户 - 昨日
    @BindView(R.id.imvi_umdatastatistics_left)
    ImageView imviLeft;                                                                             // 左侧间隔

    @BindView(R.id.lila_umdatastatistics_activeuser_layout)
    LinearLayout lilaActiveUser;                                                                    // 活跃用户
    @BindView(R.id.txvi_umdatastatistics_activeuser_today)
    TextView txviActiveUserToday;                                                                   // 活跃用户 - 今日
    @BindView(R.id.txvi_umdatastatistics_activeuser_yesterday)
    TextView txviActiveUserYesterday;                                                               // 活跃用户 - 昨日
    @BindView(R.id.imvi_umdatastatistics_right)
    ImageView imviRight;                                                                            // 右侧间隔

    @BindView(R.id.lila_umdatastatistics_startsnum_layout)
    LinearLayout lilaStartsNum;                                                                     // 启动次数
    @BindView(R.id.txvi_umdatastatistics_startsnum_today)
    TextView txviStartsNumToday;                                                                    // 启动次数 - 今日
    @BindView(R.id.txvi_umdatastatistics_startsnum_yesterday)
    TextView txviStartsNumYesterday;                                                                // 启动次数 - 昨日

    @BindView(R.id.revi_umdatastatistics_sevendays)
    RecyclerView mSevenList;                                                                        // 七日详情


    @BindView(R.id.txvi_umdatastatistics_durationdate)
    TextView txviDurationDate;                                                                      // 日期
    @BindView(R.id.txvi_umdatastatistics_dayduration)
    TextView txviDayDuration;                                                                       // 平均日使用时长
    @BindView(R.id.txvi_umdatastatistics_singleduration)
    TextView txviSingleDuration;                                                                    // 平均单次使用时长
    @BindView(R.id.revi_umdatastatistics_single)
    RecyclerView mSingleList;                                                                       // 单次使用时长分布
    @BindView(R.id.revi_umdatastatistics_single_not)
    TextView txviDurationNot;                                                                       // 无数据

    @BindView(R.id.revi_umdatastatistics_event)
    RecyclerView mEventList;                                                                        // 事件
    /*------------------------------------------------业务区域------------------------------------------------*/
    // 选项：1代表 新增用户；2代表活跃用户；3代表启动次数；
    private int mTab = 1;

    @Named("mSevenLayoutManager")
    @Inject
    RecyclerView.LayoutManager mSevenLayoutManager;
    @Named("mSevenAdapter")
    @Inject
    SevenStatisticsAdapter mSevenAdapter;                                                           // 七日统计适配器


    @Named("mSingleLayoutManager")
    @Inject
    RecyclerView.LayoutManager mSingleLayoutManager;
    @Named("mSingleAdapter")
    @Inject
    SingleDurationAdapter mSingleAdapter;                                                           // 单次使用时长分布 适配器


    @Named("mEventLayoutManager")
    @Inject
    RecyclerView.LayoutManager mEventLayoutManager;
    @Named("mEventAdapter")
    @Inject
    UmEventAdapter mEventAdapter;                                                                   // 事件 适配器

    // 日期时间
    private Calendar calendar = Calendar.getInstance(Locale.CHINA);

    @Override
    protected void onDestroy() {
        // super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(mSevenList);
        DefaultAdapter.releaseAllHolder(mSingleList);
        DefaultAdapter.releaseAllHolder(mEventList);
        super.onDestroy();

        this.mSevenLayoutManager = null;
        this.mSevenAdapter = null;

        this.mSingleLayoutManager = null;
        this.mSingleAdapter = null;

        this.mEventLayoutManager = null;
        this.mEventAdapter = null;

        this.calendar = null;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerUmDataStatisticsComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_um_data_statistics;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("数据统计");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "um_data_statistics");

        // 初始控件
        ArmsUtils.configRecyclerView(mSevenList, mSevenLayoutManager);
        mSevenList.setAdapter(mSevenAdapter);

        ArmsUtils.configRecyclerView(mSingleList, mSingleLayoutManager);
        mSingleList.setAdapter(mSingleAdapter);

        ArmsUtils.configRecyclerView(mEventList, mEventLayoutManager);
        mEventList.setAdapter(mEventAdapter);

        // 前一天
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        if (mPresenter != null) {
            mPresenter.initDate(TimeUtils.date2String(calendar.getTime(), new SimpleDateFormat("yyyy-MM-dd")));
        }
    }

    /**
     * 所有App统计数据
     */
    @Override
    public void loadAllAppData(UmengUappAllAppData info) {
        runOnUiThread(() -> {
            // 累计用户
            txviCumulativeUsers.setText(String.valueOf(info.getTotalUsers()));

            // 新增用户
            txviNewUserToday.setText("今日：" + info.getTodayNewUsers());
            txviNewUserYesterday.setText("昨日：" + info.getYesterdayNewUsers());

            // 活跃用户
            txviActiveUserToday.setText("今日：" + info.getTodayActivityUsers());
            txviActiveUserYesterday.setText("昨日：" + info.getYesterdayActivityUsers());

            // 启动次数
            txviStartsNumToday.setText("今日：" + info.getTodayLaunches());
            txviStartsNumYesterday.setText("昨日：" + info.getYesterdayLaunches());
        });
    }

    /**
     * 加载时长日期
     */
    @Override
    public void loadDate(String mDate) {
        txviDurationDate.setText(mDate);
    }

    /**
     * 加载平均时长
     *
     * @param isDaily  是否是按天
     * @param duration 时长
     */
    @Override
    public void loadDurations(boolean isDaily, String duration) {
        runOnUiThread(() -> {
            if (isDaily) {
                txviDayDuration.setText(duration);
            } else {
                txviSingleDuration.setText(duration);
            }
        });
    }

    /**
     * 控制时长列表是否显示无数据
     */
    @Override
    public void viewDurations(boolean isShow) {
        runOnUiThread(() -> {
            txviDurationNot.setVisibility(isShow ? View.VISIBLE : View.GONE);
        });
    }

    @OnClick({
            R.id.lila_umdatastatistics_newuser_layout,                                              // 新增用户
            R.id.lila_umdatastatistics_activeuser_layout,                                           // 活跃用户
            R.id.lila_umdatastatistics_startsnum_layout,                                            // 启动次数

            R.id.txvi_umdatastatistics_durationdate,                                                // 使用时长 - 筛选日期
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lila_umdatastatistics_newuser_layout:                                         // 新增用户
                if (mTab != 1) {
                    mTab = 1;
                    lilaNewUser.setBackgroundResource(R.drawable.view_bg_left_selector);
                    lilaActiveUser.setBackground(null);
                    imviLeft.setVisibility(View.VISIBLE);
                    imviLeft.setImageResource(R.mipmap.b62);
                    imviRight.setVisibility(View.INVISIBLE);
                    lilaStartsNum.setBackground(null);

                    if (mPresenter != null) {
                        mPresenter.getNewUsers();
                    }
                }

                break;
            case R.id.lila_umdatastatistics_activeuser_layout:                                      // 活跃用户
                if (mTab != 2) {
                    mTab = 2;
                    lilaNewUser.setBackground(null);
                    lilaActiveUser.setBackgroundResource(R.drawable.view_bg_centre_selector);
                    imviLeft.setVisibility(View.VISIBLE);
                    imviLeft.setImageResource(R.mipmap.b61);
                    imviRight.setVisibility(View.VISIBLE);
                    imviRight.setImageResource(R.mipmap.b62);
                    lilaStartsNum.setBackground(null);

                    if (mPresenter != null) {
                        mPresenter.getActiveUsers();
                    }
                }

                break;
            case R.id.lila_umdatastatistics_startsnum_layout:                                       // 启动次数
                if (mTab != 3) {
                    mTab = 3;
                    lilaNewUser.setBackground(null);
                    lilaActiveUser.setBackground(null);
                    imviLeft.setVisibility(View.INVISIBLE);
                    imviRight.setVisibility(View.VISIBLE);
                    imviRight.setImageResource(R.mipmap.b61);
                    lilaStartsNum.setBackgroundResource(R.drawable.view_bg_right_selector);

                    if (mPresenter != null) {
                        mPresenter.getLaunches();
                    }
                }

                break;
            case R.id.txvi_umdatastatistics_durationdate:                                           // 使用时长 - 筛选日期
                showDatePickerDialog();
                break;
        }
    }

    /**
     * 日期选择
     */
    public void showDatePickerDialog() {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        // 绑定监听器(How the parent is notified that the date is set.)
        new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> {
            // 此处得到选择的时间，可以进行你想要的操作
            String mVal = year + "-" + CommonUtils.format0Right(String.valueOf(monthOfYear + 1)) + "-" + dayOfMonth;

            // 保留选中的时间
            calendar.set(year, monthOfYear, dayOfMonth);

            txviDurationDate.setText(mVal);
            if (mPresenter != null) {
                mPresenter.getDurations(mVal);
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
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