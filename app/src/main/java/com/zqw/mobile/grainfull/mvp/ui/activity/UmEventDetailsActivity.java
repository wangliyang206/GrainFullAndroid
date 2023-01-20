package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ClipboardUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.di.component.DaggerUmEventDetailsComponent;
import com.zqw.mobile.grainfull.mvp.contract.UmEventDetailsContract;
import com.zqw.mobile.grainfull.mvp.presenter.UmEventDetailsPresenter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.SevenStatisticsAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:友盟事件详情
 * <p>
 * Created on 2023/01/20 14:09
 *
 * @author 赤槿
 * module name is UmEventDetailsActivity
 */
public class UmEventDetailsActivity extends BaseActivity<UmEventDetailsPresenter> implements UmEventDetailsContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_um_event_details)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.txvi_umeventdetails_id)
    TextView txviId;                                                                                // ID
    @BindView(R.id.txvi_umeventdetails_name)
    TextView txviName;                                                                              // 事件ID
    @BindView(R.id.txvi_umeventdetails_displayname)
    TextView txviDisplayName;                                                                       // 事件名称

    @BindView(R.id.revi_umeventdetails_event)
    RecyclerView mRecyclerView;                                                                     // 事件详情
    /*------------------------------------------------业务区域------------------------------------------------*/

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    SevenStatisticsAdapter mAdapter;                                                                // 适配器

    @Override
    protected void onDestroy() {
        // super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(mRecyclerView);
        super.onDestroy();

        this.mLayoutManager = null;
        this.mAdapter = null;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerUmEventDetailsComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_um_event_details;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("事件详情");

        // 初始控件
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        if (mPresenter != null) {
            mPresenter.initData(getIntent().getExtras());
        }
    }

    /**
     * 加载数据
     */
    @Override
    public void loadData(String id, String name, String displayName) {
        txviId.append(CommonUtils.isEmptyReturnStr(id));
        txviName.append(CommonUtils.isEmptyReturnStr(name));
        txviDisplayName.append(CommonUtils.isEmptyReturnStr(displayName));
    }

    @OnClick({
            R.id.txvi_umeventdetails_id,                                                            // 复制 id
            R.id.txvi_umeventdetails_name,                                                          // 复制 事件ID
            R.id.txvi_umeventdetails_displayname,                                                   // 复制 事件名称
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txvi_umeventdetails_id:                                                       // 复制 id
                String mId = txviId.getText().toString();
                if (!TextUtils.isEmpty(mId)) {
                    ClipboardUtils.copyText(mId);
                    showMessage("复制成功！");
                }
                break;
            case R.id.txvi_umeventdetails_name:                                                     // 复制 事件ID
                String mName = txviName.getText().toString();
                if (!TextUtils.isEmpty(mName)) {
                    ClipboardUtils.copyText(mName);
                    showMessage("复制成功！");
                }
                break;
            case R.id.txvi_umeventdetails_displayname:                                              // 复制 事件名称
                String mDisplayName = txviDisplayName.getText().toString();
                if (!TextUtils.isEmpty(mDisplayName)) {
                    ClipboardUtils.copyText(mDisplayName);
                    showMessage("复制成功！");
                }
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