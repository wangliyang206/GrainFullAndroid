package com.zqw.mobile.grainfull.mvp.presenter;

import android.os.Bundle;

import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.ocean.rawsdk.client.exception.OceanException;
import com.blankj.utilcode.util.TimeUtils;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.umeng.uapp.param.UmengUappDateCountInfo;
import com.umeng.uapp.param.UmengUappEventGetDataParam;
import com.umeng.uapp.param.UmengUappEventGetDataResult;
import com.zqw.mobile.grainfull.BuildConfig;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.contract.UmEventDetailsContract;
import com.zqw.mobile.grainfull.mvp.model.entity.SevenStatistics;
import com.zqw.mobile.grainfull.mvp.ui.adapter.SevenStatisticsAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/01/20 14:09
 * ================================================
 */
@ActivityScope
public class UmEventDetailsPresenter extends BasePresenter<UmEventDetailsContract.Model, UmEventDetailsContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    // 操作对象
    private ApiExecutor apiExecutor;

    @Inject
    SevenStatisticsAdapter mAdapter;                                                                // 适配器
    @Inject
    List<SevenStatistics> mEventList;

    // id
    private String id;
    // 事件ID
    private String name;
    // 事件中文
    private String displayName;

    // 昨天日期时间
    private Calendar mYesterDay = Calendar.getInstance(Locale.CHINA);
    // 半个月前的日期日间
    private Calendar mHalfMonthAgo = Calendar.getInstance(Locale.CHINA);

    @Inject
    public UmEventDetailsPresenter(UmEventDetailsContract.Model model, UmEventDetailsContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 外面提供的参数
     */
    private void getBundleValues(Bundle bundle) {
        if (bundle != null) {
            id = bundle.getString("id");
            name = bundle.getString("name");
            displayName = bundle.getString("displayName");
        }
    }

    /**
     * 初始
     */
    public void initData(Bundle bundle) {
        // 拿到外面提供的数据
        getBundleValues(bundle);
        // 请替换apiKey和apiSecurity
        apiExecutor = new ApiExecutor("3981280", "PW0nOdKBCsM");
        apiExecutor.setServerHost("gateway.open.umeng.com");

        // 今天的前一天
        mYesterDay.add(Calendar.DAY_OF_MONTH, -1);
        // 半月前
        mHalfMonthAgo.add(Calendar.DAY_OF_MONTH, -15);

        // 加载数据
        mRootView.loadData(id, name,displayName);
        // 获取事件详情
        getEventList();
    }

    /**
     * 获取事件详情
     * 逻辑：查询半月前至前一天的数据。
     */
    private void getEventList() {
        new Thread() {
            @Override
            public void run() {
                UmengUappEventGetDataParam param = new UmengUappEventGetDataParam();
                // 测试环境只支持http
                // param.getOceanRequestPolicy().setUseHttps(false);
                param.setAppkey(BuildConfig.DEBUG ? mRootView.getActivity().getString(R.string.um_app_key_debug) : mRootView.getActivity().getString(R.string.um_app_key));
                // 2023-01-16号开通友盟数据统计
                param.setStartDate(TimeUtils.date2String(mHalfMonthAgo.getTime(), new SimpleDateFormat("yyyy-MM-dd")));
                param.setEndDate(TimeUtils.date2String(mYesterDay.getTime(), new SimpleDateFormat("yyyy-MM-dd")));
                // 63c622254d182e302fa21b39
                // picture_pipette

                // 63c621fe0a26f850ba2a1a2f
                // color_picker
                param.setEventName(name);

                try {
                    // 清理历史数据
                    mEventList.clear();
                    UmengUappEventGetDataResult result = apiExecutor.execute(param);
                    UmengUappDateCountInfo info = result.getEventData()[0];
                    for (int i = 0; i < info.getDates().length; i++) {
                        mEventList.add(new SevenStatistics(info.getDates()[i], String.valueOf(info.getData()[i])));
                    }
                    // 倒序
                    Collections.reverse(mEventList);
                } catch (OceanException e) {
                    System.out.println("errorCode=" + e.getErrorCode() + ", errorMessage=" + e.getErrorMessage());
                }
                mRootView.getActivity().runOnUiThread(() -> {
                    // 刷新数据
                    mAdapter.notifyDataSetChanged();
                });
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.apiExecutor = null;
        this.mAdapter = null;
        this.mEventList = null;
        this.mYesterDay = null;
        this.mHalfMonthAgo = null;
    }
}