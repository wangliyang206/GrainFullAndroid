package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerElectronicClockComponent;
import com.zqw.mobile.grainfull.mvp.contract.ElectronicClockContract;
import com.zqw.mobile.grainfull.mvp.presenter.ElectronicClockPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.electimeview.ElecTimeView;

import butterknife.BindView;

/**
 * Description:电子时钟
 * <p>
 * Created on 2023/02/23 14:30
 *
 * @author 赤槿
 * module name is ElectronicClockActivity
 */
public class ElectronicClockActivity extends BaseActivity<ElectronicClockPresenter> implements ElectronicClockContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_electronic_clock)
    LinearLayout contentLayout;                                                                     // 总布局

    @BindView(R.id.view_electronicclock_clock)
    ElecTimeView viewClock;

    /*------------------------------------------------业务区域------------------------------------------------*/

    @Override
    protected void onDestroy() {
        viewClock.onDestroy();
        super.onDestroy();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerElectronicClockComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_electronic_clock;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("电子时钟");

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