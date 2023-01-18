package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.uapp.param.UmengUappAllAppData;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerUmDataStatisticsComponent;
import com.zqw.mobile.grainfull.mvp.contract.UmDataStatisticsContract;
import com.zqw.mobile.grainfull.mvp.presenter.UmDataStatisticsPresenter;

import javax.inject.Inject;

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
    RecyclerView mRecyclerView;                                                                     // 七日统计
    /*------------------------------------------------业务区域------------------------------------------------*/

    @Inject
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onDestroy() {
        // super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(mRecyclerView);
        super.onDestroy();

        this.mLayoutManager = null;
//        this.mAdapter = null;
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

        if (mPresenter != null) {
            mPresenter.initDate();
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

    @OnClick({
            R.id.lila_umdatastatistics_newuser_layout,                                              // 新增用户
            R.id.lila_umdatastatistics_activeuser_layout,                                           // 活跃用户
            R.id.lila_umdatastatistics_startsnum_layout,                                            // 启动次数
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lila_umdatastatistics_newuser_layout:                                         // 新增用户
                lilaNewUser.setBackgroundResource(R.drawable.view_bg_left_selector);
                lilaActiveUser.setBackground(null);
                imviLeft.setVisibility(View.VISIBLE);
                imviLeft.setImageResource(R.mipmap.b62);
                imviRight.setVisibility(View.INVISIBLE);
                lilaStartsNum.setBackground(null);
                break;
            case R.id.lila_umdatastatistics_activeuser_layout:                                      // 活跃用户
                lilaNewUser.setBackground(null);
                lilaActiveUser.setBackgroundResource(R.drawable.view_bg_centre_selector);
                imviLeft.setVisibility(View.VISIBLE);
                imviLeft.setImageResource(R.mipmap.b61);
                imviRight.setVisibility(View.VISIBLE);
                imviRight.setImageResource(R.mipmap.b62);
                lilaStartsNum.setBackground(null);
                break;
            case R.id.lila_umdatastatistics_startsnum_layout:                                       // 启动次数
                lilaNewUser.setBackground(null);
                lilaActiveUser.setBackground(null);
                imviLeft.setVisibility(View.INVISIBLE);
                imviRight.setVisibility(View.VISIBLE);
                imviRight.setImageResource(R.mipmap.b61);
                lilaStartsNum.setBackgroundResource(R.drawable.view_bg_right_selector);
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