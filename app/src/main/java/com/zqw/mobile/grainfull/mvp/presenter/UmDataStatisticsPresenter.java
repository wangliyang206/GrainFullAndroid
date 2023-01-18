package com.zqw.mobile.grainfull.mvp.presenter;

import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.ocean.rawsdk.client.exception.OceanException;
import com.blankj.utilcode.util.TimeUtils;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.umeng.uapp.param.UmengUappEventListParam;
import com.umeng.uapp.param.UmengUappEventListResult;
import com.umeng.uapp.param.UmengUappGetAllAppDataParam;
import com.umeng.uapp.param.UmengUappGetAllAppDataResult;
import com.umeng.uapp.param.UmengUappGetDailyDataParam;
import com.umeng.uapp.param.UmengUappGetDailyDataResult;
import com.umeng.uapp.param.UmengUappGetDurationsParam;
import com.umeng.uapp.param.UmengUappGetDurationsResult;
import com.umeng.uapp.param.UmengUappGetNewUsersParam;
import com.umeng.uapp.param.UmengUappGetNewUsersResult;
import com.umeng.uapp.param.UmengUappGetTodayDataParam;
import com.umeng.uapp.param.UmengUappGetTodayDataResult;
import com.zqw.mobile.grainfull.BuildConfig;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.contract.UmDataStatisticsContract;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/16 10:42
 * ================================================
 */
@ActivityScope
public class UmDataStatisticsPresenter extends BasePresenter<UmDataStatisticsContract.Model, UmDataStatisticsContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    // 操作对象
    private ApiExecutor apiExecutor;

    @Inject
    public UmDataStatisticsPresenter(UmDataStatisticsContract.Model model, UmDataStatisticsContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 初始化
     */
    public void initDate() {
        // 请替换apiKey和apiSecurity
        apiExecutor = new ApiExecutor("3981280", "PW0nOdKBCsM");
        apiExecutor.setServerHost("gateway.open.umeng.com");


        new Thread() {
            @Override
            public void run() {
                getAllAppData();
                getNewUsers();
                getDurations(true);
                getDurations(false);
            }
        }.start();

    }

    /**
     * 获取所有App统计数据
     * 请求：无
     * 响应：
     * 今日活跃用户、今日新增用户、今日启动次数
     * 昨日活跃用户、昨日新增用户、昨日启动次数、昨日独立新增用户数、昨日独立活跃用户数
     * 总用户数
     */
    public void getAllAppData() {
        UmengUappGetAllAppDataParam param = new UmengUappGetAllAppDataParam();
        // 测试环境只支持http
        // param.getOceanRequestPolicy().setUseHttps(false);

        try {
            UmengUappGetAllAppDataResult result = apiExecutor.execute(param);
            mRootView.loadAllAppData(result.getAllAppData()[0]);
        } catch (OceanException e) {
            System.out.println("errorCode=" + e.getErrorCode() + ", errorMessage=" + e.getErrorMessage());
        }
    }

    /**
     * 获取App新增用户数
     * 请求：应用ID、查询起始日期、查询截止日期、查询类型（按日daily,按周weekly,按月monthly 查询）
     */
    public void getNewUsers() {
        UmengUappGetNewUsersParam param = new UmengUappGetNewUsersParam();
        // 测试环境只支持http
        // param.getOceanRequestPolicy().setUseHttps(false);
        param.setAppkey(BuildConfig.DEBUG ? mRootView.getActivity().getString(R.string.um_app_key_debug) : mRootView.getActivity().getString(R.string.um_app_key));

        // 获取当天日期
        Date mSameDay = new Date();
        // 计算出7天前的日期
        Date mNewData = new Date(mSameDay.getTime() - 604800000L);
        param.setStartDate(TimeUtils.date2String(mNewData, new SimpleDateFormat("yyyy-MM-dd")));
        param.setEndDate(TimeUtils.date2String(mSameDay, new SimpleDateFormat("yyyy-MM-dd")));
        // 按天查询
        param.setPeriodType("daily");

        try {
            UmengUappGetNewUsersResult result = apiExecutor.execute(param);
            System.out.println();
        } catch (OceanException e) {
            System.out.println("errorCode=" + e.getErrorCode() + ", errorMessage=" + e.getErrorMessage());
        }
    }

    /**
     * 获取App统计数据
     * 请求：应用ID、查询日期
     * 响应：统计日期、活跃用户数、总用户数、启动数、新增用户数、游戏付费用户数（仅游戏sdk）
     */
    public void getDailyData() {
        UmengUappGetDailyDataParam param = new UmengUappGetDailyDataParam();
        // 测试环境只支持http
        // param.getOceanRequestPolicy().setUseHttps(false);
        param.setAppkey(BuildConfig.DEBUG ? mRootView.getActivity().getString(R.string.um_app_key_debug) : mRootView.getActivity().getString(R.string.um_app_key));
        param.setDate("2023-01-16");
        // 渠道名称（仅限一个App%20Store）
//        param.setChannel(Constant.UM_CHANNEL);
        // 版本名称（仅限一个1.0.0）
//        param.setVersion(String.valueOf(BuildConfig.VERSION_CODE));

        try {
            UmengUappGetDailyDataResult result = apiExecutor.execute(param);
            System.out.println();
        } catch (OceanException e) {
            System.out.println("errorCode=" + e.getErrorCode() + ", errorMessage=" + e.getErrorMessage());
        }
    }


    /**
     * 获取App今天统计数据
     */
    public void getTodayDataParam() {
        // 把网络访问的代码放在这里
        UmengUappGetTodayDataParam param = new UmengUappGetTodayDataParam();
        param.setAppkey(BuildConfig.DEBUG ? mRootView.getActivity().getString(R.string.um_app_key_debug) : mRootView.getActivity().getString(R.string.um_app_key));

        try {
            UmengUappGetTodayDataResult result = apiExecutor.execute(param);
            System.out.println();
        } catch (OceanException e) {
            System.out.println("errorCode=" + e.getErrorCode() + ", errorMessage=" + e.getErrorMessage());
        }
    }

    /**
     * 获取App使用时长
     * 请求：应用ID、查询日期(单日)、查询时长统计类型（按天daily，按次daily_per_launch）
     * 响应：
     * 每次启动的平均使用时长
     * 时间区间单位秒、启动次数/用户数、此区间的时长占
     *
     * @param isDaily 是否按天
     */
    public void getDurations(boolean isDaily) {
        UmengUappGetDurationsParam param = new UmengUappGetDurationsParam();
        // 应用ID
        param.setAppkey(BuildConfig.DEBUG ? mRootView.getActivity().getString(R.string.um_app_key_debug) : mRootView.getActivity().getString(R.string.um_app_key));
        // 查询日期
        param.setDate("2023-01-16");
//        param.setDate(TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd")));
        // 查询时长统计类型（按天daily，按次daily_per_launch）
        if (isDaily) {
            param.setStatType("daily");
        } else {
            param.setStatType("daily_per_launch");
        }

        // 渠道名称（仅限一个App%20Store）
//        param.setChannel(Constant.UM_CHANNEL);
        // 版本名称（仅限一个1.0.0）
//        param.setVersion(BuildConfig.VERSION_NAME);

        try {
            UmengUappGetDurationsResult result = apiExecutor.execute(param);
            System.out.println();
        } catch (OceanException e) {
            System.out.println("errorCode=" + e.getErrorCode() + ", errorMessage=" + e.getErrorMessage());
        }
    }


    /**
     * 获取事件列表
     */
    private void getEventList() {
        UmengUappEventListParam param = new UmengUappEventListParam();
        // 测试环境只支持http
        // param.getOceanRequestPolicy().setUseHttps(false);
        param.setAppkey(BuildConfig.DEBUG ? mRootView.getActivity().getString(R.string.um_app_key_debug) : mRootView.getActivity().getString(R.string.um_app_key));
        param.setStartDate("2023-01-16");
        param.setEndDate("2023-01-17");
        param.setPerPage(50);
        param.setPage(1);
//        param.setVersion("");

        try {
            UmengUappEventListResult result = apiExecutor.execute(param);
            System.out.println();
        } catch (OceanException e) {
            System.out.println("errorCode=" + e.getErrorCode() + ", errorMessage=" + e.getErrorMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}