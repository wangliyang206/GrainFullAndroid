package com.zqw.mobile.grainfull.mvp.presenter;

import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.ocean.rawsdk.client.exception.OceanException;
import com.blankj.utilcode.util.TimeUtils;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.umeng.uapp.param.UmengUappCountData;
import com.umeng.uapp.param.UmengUappDurationInfo;
import com.umeng.uapp.param.UmengUappEventInfo;
import com.umeng.uapp.param.UmengUappEventListParam;
import com.umeng.uapp.param.UmengUappEventListResult;
import com.umeng.uapp.param.UmengUappGetActiveUsersParam;
import com.umeng.uapp.param.UmengUappGetActiveUsersResult;
import com.umeng.uapp.param.UmengUappGetAllAppDataParam;
import com.umeng.uapp.param.UmengUappGetAllAppDataResult;
import com.umeng.uapp.param.UmengUappGetDailyDataParam;
import com.umeng.uapp.param.UmengUappGetDailyDataResult;
import com.umeng.uapp.param.UmengUappGetDurationsParam;
import com.umeng.uapp.param.UmengUappGetDurationsResult;
import com.umeng.uapp.param.UmengUappGetLaunchesParam;
import com.umeng.uapp.param.UmengUappGetLaunchesResult;
import com.umeng.uapp.param.UmengUappGetNewUsersParam;
import com.umeng.uapp.param.UmengUappGetNewUsersResult;
import com.umeng.uapp.param.UmengUappGetTodayDataParam;
import com.umeng.uapp.param.UmengUappGetTodayDataResult;
import com.zqw.mobile.grainfull.BuildConfig;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.mvp.contract.UmDataStatisticsContract;
import com.zqw.mobile.grainfull.mvp.model.entity.SevenStatistics;
import com.zqw.mobile.grainfull.mvp.model.entity.SingleDuration;
import com.zqw.mobile.grainfull.mvp.model.entity.UmEvent;
import com.zqw.mobile.grainfull.mvp.ui.adapter.SevenStatisticsAdapter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.SingleDurationAdapter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.UmEventAdapter;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

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

    @Named("mSevenAdapter")
    @Inject
    SevenStatisticsAdapter mSevenAdapter;                                                           // 七日详情适配器
    @Named("mSevenStatistics")
    @Inject
    List<SevenStatistics> mSevenStatistics;                                                         // 七日详情数据集

    @Named("mSingleAdapter")
    @Inject
    SingleDurationAdapter mSingleAdapter;                                                           // 单次使用时长分布 适配器
    @Named("mSingle")
    @Inject
    List<SingleDuration> mSingle;                                                                   // 单次使用时长分布 数据集

    @Named("mEventAdapter")
    @Inject
    UmEventAdapter mEventAdapter;                                                                   // 事件 适配器
    @Named("mEvent")
    @Inject
    List<UmEvent> mEvent;                                                                           // 事件 数据集

    @Inject
    public UmDataStatisticsPresenter(UmDataStatisticsContract.Model model, UmDataStatisticsContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 初始化
     */
    public void initDate(String mYesterday) {
        // 请替换apiKey和apiSecurity
        apiExecutor = new ApiExecutor("3981280", "PW0nOdKBCsM");
        apiExecutor.setServerHost("gateway.open.umeng.com");

        getAllAppData();
        getNewUsers();
        getDurations(mYesterday);
        getEventList(mYesterday);
    }

    /**
     * 获取所有App统计数据
     * 请求：无
     * 响应：
     * 今日活跃用户、今日新增用户、今日启动次数
     * 昨日活跃用户、昨日新增用户、昨日启动次数、昨日独立新增用户数、昨日独立活跃用户数
     * 总用户数
     */
    private void getAllAppData() {
        new Thread() {
            @Override
            public void run() {
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
        }.start();
    }

    /**
     * 获取App新增用户数
     * 请求：应用ID、查询起始日期、查询截止日期、查询类型（按日daily,按周weekly,按月monthly 查询）
     * 响应：统计日期、按版本或渠道的统计信息、按小时查询返回数组、其它情况返回整型，按天无版本无渠道，按周，按月查询。
     */
    public void getNewUsers() {
        new Thread() {
            @Override
            public void run() {
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
                    // 清理数据
                    mSevenStatistics.clear();
                    UmengUappGetNewUsersResult result = apiExecutor.execute(param);
                    // 组织数据
                    for (UmengUappCountData info : result.getNewUserInfo()) {
                        mSevenStatistics.add(new SevenStatistics(info.getDate(), String.valueOf(info.getValue())));
                    }
                    // 倒序
                    Collections.reverse(mSevenStatistics);
                } catch (OceanException e) {
                    System.out.println("errorCode=" + e.getErrorCode() + ", errorMessage=" + e.getErrorMessage());
                }

                mRootView.getActivity().runOnUiThread(() -> {
                    // 刷新数据
                    mSevenAdapter.notifyDataSetChanged();
                });
            }
        }.start();
    }

    /**
     * 获取App活跃用户数
     * 请求：应用ID、查询起始日期、查询截止日期、查询类型（按日daily,按周weekly,按月monthly,近7日7day,近30日30day 查询，接口限制：periodType=daily/7day/30day时，返回结果数量限制为60条；periodType=weekly时，返回结果数量限制为8条；periodType=monthly时，返回结果数量限制为3条。实际返回结果数量以接口为准。）
     * 响应：统计日期、按版本或渠道的统计信息、按小时查询返回数组、其它情况返回整型，按天无版本无渠道，按周，按月查询。
     */
    public void getActiveUsers() {
        new Thread() {
            @Override
            public void run() {
                UmengUappGetActiveUsersParam param = new UmengUappGetActiveUsersParam();
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
                    // 清理数据
                    mSevenStatistics.clear();
                    UmengUappGetActiveUsersResult result = apiExecutor.execute(param);
                    // 组织数据
                    for (UmengUappCountData info : result.getActiveUserInfo()) {
                        mSevenStatistics.add(new SevenStatistics(info.getDate(), String.valueOf(info.getValue())));
                    }
                    // 倒序
                    Collections.reverse(mSevenStatistics);
                } catch (OceanException e) {
                    System.out.println("errorCode=" + e.getErrorCode() + ", errorMessage=" + e.getErrorMessage());
                }
                mRootView.getActivity().runOnUiThread(() -> {
                    // 刷新数据
                    mSevenAdapter.notifyDataSetChanged();
                });
            }
        }.start();
    }

    /**
     * 获取App启动次数
     * 请求：应用ID、查询起始日期、查询截止日期、查询类型（按日daily,按周weekly,按月monthly,近7日7day,近30日30day 查询，接口限制：periodType=daily/7day/30day时，返回结果数量限制为60条；periodType=weekly时，返回结果数量限制为8条；periodType=monthly时，返回结果数量限制为3条。实际返回结果数量以接口为准。）
     * 响应：统计日期、按版本或渠道的统计信息、按小时查询返回数组、其它情况返回整型，按天无版本无渠道，按周，按月查询。
     */
    public void getLaunches() {
        new Thread() {
            @Override
            public void run() {
                UmengUappGetLaunchesParam param = new UmengUappGetLaunchesParam();
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
                    // 清理数据
                    mSevenStatistics.clear();
                    UmengUappGetLaunchesResult result = apiExecutor.execute(param);
                    // 组织数据
                    for (UmengUappCountData info : result.getLaunchInfo()) {
                        mSevenStatistics.add(new SevenStatistics(info.getDate(), String.valueOf(info.getValue())));
                    }
                    // 倒序
                    Collections.reverse(mSevenStatistics);
                } catch (OceanException e) {
                    System.out.println("errorCode=" + e.getErrorCode() + ", errorMessage=" + e.getErrorMessage());
                }
                mRootView.getActivity().runOnUiThread(() -> {
                    // 刷新数据
                    mSevenAdapter.notifyDataSetChanged();
                });
            }
        }.start();
    }

    /**
     * 获取APP使用时长
     */
    public void getDurations(String mDate) {
        mRootView.loadDurationDate(mDate);

        getDurations(true, mDate);
        getDurations(false, mDate);
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
    public void getDurations(boolean isDaily, String mDate) {
        new Thread() {
            @Override
            public void run() {
                UmengUappGetDurationsParam param = new UmengUappGetDurationsParam();
                // 应用ID
                param.setAppkey(BuildConfig.DEBUG ? mRootView.getActivity().getString(R.string.um_app_key_debug) : mRootView.getActivity().getString(R.string.um_app_key));
                // 查询日期
//                param.setDate("2023-01-18");
                param.setDate(mDate);
                // 查询时长统计类型（按天daily，按次daily_per_launch）
                if (isDaily) {
                    param.setStatType("daily");
                } else {
                    param.setStatType("daily_per_launch");
                }

                // 渠道名称（仅限一个App%20Store）
//                param.setChannel(Constant.UM_CHANNEL);
                // 版本名称（仅限一个1.0.0）
//                param.setVersion(BuildConfig.VERSION_NAME);

                try {
                    // 清理缓存
                    mSingle.clear();
                    UmengUappGetDurationsResult result = apiExecutor.execute(param);

                    if (result.getDurationInfos().length > 0) {
                        // 有数据
                        mRootView.viewDurations(false);
                        // 加载平均时长
                        mRootView.loadDurations(isDaily, CommonUtils.timeConversion(true, result.getAverage().intValue()));
                        if (!isDaily) {
                            // 加载单次详情
                            for (UmengUappDurationInfo info : result.getDurationInfos()) {
                                mSingle.add(new SingleDuration(info.getName(), info.getValue(), info.getPercent()));
                            }
                        }
                    } else {
                        // 无数据
                        mRootView.loadDurations(isDaily, "0");
                        mRootView.viewDurations(true);
                    }
                } catch (OceanException e) {
                    System.out.println("errorCode=" + e.getErrorCode() + ", errorMessage=" + e.getErrorMessage());
                    mRootView.loadDurations(isDaily, "0");
                    mRootView.viewDurations(true);
                }
                mRootView.getActivity().runOnUiThread(() -> {
                    // 刷新数据
                    mSingleAdapter.notifyDataSetChanged();
                });
            }
        }.start();
    }

    /**
     * 获取事件列表
     */
    public void getEventList(String mYesterday) {
        mRootView.loadEventDate(mYesterday);
        new Thread() {
            @Override
            public void run() {
                UmengUappEventListParam param = new UmengUappEventListParam();
                // 测试环境只支持http
                // param.getOceanRequestPolicy().setUseHttps(false);
                param.setAppkey(BuildConfig.DEBUG ? mRootView.getActivity().getString(R.string.um_app_key_debug) : mRootView.getActivity().getString(R.string.um_app_key));
//                param.setStartDate("2023-01-16");
                param.setStartDate(mYesterday);
                param.setEndDate(mYesterday);
                // 当前界面未做分页，如果想要全部显示，此值必须大于友盟统计后台中事件总数。
                param.setPerPage(50);
                param.setPage(1);
//                param.setVersion("");

                try {
                    // 清理历史数据
                    mEvent.clear();
                    UmengUappEventListResult result = apiExecutor.execute(param);
                    for (UmengUappEventInfo info : result.getEventInfo()) {
                        mEvent.add(new UmEvent(info.getId(), info.getName(), info.getCount(), info.getDisplayName()));
                    }
                } catch (OceanException e) {
                    System.out.println("errorCode=" + e.getErrorCode() + ", errorMessage=" + e.getErrorMessage());
                }
                mRootView.getActivity().runOnUiThread(() -> {
                    // 刷新数据
                    mEventAdapter.notifyDataSetChanged();
                    mRootView.loadEventCount(mEvent.size());
                });
            }
        }.start();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.apiExecutor = null;

        this.mSevenAdapter = null;
        this.mSevenStatistics = null;

        this.mSingleAdapter = null;
        this.mSingle = null;

        this.mEventAdapter = null;
        this.mEvent = null;
    }
}