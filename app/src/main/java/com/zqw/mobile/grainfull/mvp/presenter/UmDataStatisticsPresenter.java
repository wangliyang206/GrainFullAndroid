package com.zqw.mobile.grainfull.mvp.presenter;

import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.ocean.rawsdk.client.exception.OceanException;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.umeng.uapp.param.UmengUappGetAllAppDataParam;
import com.umeng.uapp.param.UmengUappGetAllAppDataResult;
import com.umeng.uapp.param.UmengUappGetAppListParam;
import com.umeng.uapp.param.UmengUappGetAppListResult;
import com.umeng.uapp.param.UmengUappGetDailyDataParam;
import com.umeng.uapp.param.UmengUappGetDailyDataResult;
import com.umeng.uapp.param.UmengUappGetDurationsParam;
import com.umeng.uapp.param.UmengUappGetDurationsResult;
import com.umeng.uapp.param.UmengUappGetTodayDataParam;
import com.umeng.uapp.param.UmengUappGetTodayDataResult;
import com.zqw.mobile.grainfull.BuildConfig;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.mvp.contract.UmDataStatisticsContract;

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
                getAppList();
                getAllAppData();
                getDailyData();
                getTodayDataParam();
                getDurations();
            }
        }.start();

    }

    /** 获取App列表 */
    public void getAppList(){
        UmengUappGetAppListParam param = new UmengUappGetAppListParam();
        // 测试环境只支持http
        // param.getOceanRequestPolicy().setUseHttps(false);
        param.setPage(1);
        param.setPerPage(10);
//        param.setAccessToken("");

        try {
            UmengUappGetAppListResult result = apiExecutor.execute(param);
            System.out.println();
        } catch (OceanException e) {
            System.out.println("errorCode=" + e.getErrorCode() + ", errorMessage=" + e.getErrorMessage());
        }
    }

    /**
     * 获取所有App统计数据
     */
    public void getAllAppData() {
        UmengUappGetAllAppDataParam param = new UmengUappGetAllAppDataParam();
        // 测试环境只支持http
        // param.getOceanRequestPolicy().setUseHttps(false);

        try {
            UmengUappGetAllAppDataResult result = apiExecutor.execute(param);
            System.out.println();
        } catch (OceanException e) {
            System.out.println("errorCode=" + e.getErrorCode() + ", errorMessage=" + e.getErrorMessage());
        }
    }

    /**
     * 获取App统计数据
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
     */
    public void getDurations() {
        UmengUappGetDurationsParam param = new UmengUappGetDurationsParam();
        // 应用ID
        param.setAppkey(BuildConfig.DEBUG ? mRootView.getActivity().getString(R.string.um_app_key_debug) : mRootView.getActivity().getString(R.string.um_app_key));
        // 查询日期
        param.setDate("2023-01-16");
//        param.setDate(TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd")));
        // 查询时长统计类型（按天daily，按次daily_per_launch）
//        param.setStatType("daily");
        param.setStatType("daily_per_launch");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}