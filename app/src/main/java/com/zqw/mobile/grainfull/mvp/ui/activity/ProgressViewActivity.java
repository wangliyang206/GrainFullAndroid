package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.white.progressview.CircleProgressView;
import com.white.progressview.HorizontalProgressView;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerProgressViewComponent;
import com.zqw.mobile.grainfull.mvp.contract.ProgressViewContract;
import com.zqw.mobile.grainfull.mvp.presenter.ProgressViewPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:进度条
 * <p>
 * Created on 2023/01/11 18:39
 *
 * @author 赤槿
 * module name is ProgressViewActivity
 */
public class ProgressViewActivity extends BaseActivity<ProgressViewPresenter> implements ProgressViewContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_progress_view)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.cpvi_progressview_normal)
    CircleProgressView mCircleProgressViewNormal;
    @BindView(R.id.cpvi_progressview_fill_in)
    CircleProgressView mCircleProgressViewFillIn;
    @BindView(R.id.cpvi_progressview_fill_in_arc)
    CircleProgressView mCircleProgressViewFillInArc;

    @BindView(R.id.cpvi_progressview_20)
    HorizontalProgressView mProgressView20;
    @BindView(R.id.cpvi_progressview_40)
    HorizontalProgressView mProgressView40;
    @BindView(R.id.cpvi_progressview_60)
    HorizontalProgressView mProgressView60;
    @BindView(R.id.cpvi_progressview_80)
    HorizontalProgressView mProgressView80;
    @BindView(R.id.cpvi_progressview_100)
    HorizontalProgressView mProgressView100;

    /*------------------------------------------------业务区域------------------------------------------------*/

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerProgressViewComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_progress_view;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("进度条");

        mProgressView20.setProgressPosition(HorizontalProgressView.BOTTOM);
        mProgressView80.setProgressPosition(HorizontalProgressView.TOP);
        mProgressView100.setProgressPosition(HorizontalProgressView.CENTRE);
    }

    @OnClick({
            R.id.btn_progressview_start
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_progressview_start:
                mProgressView20.runProgressAnim(1000);
                mProgressView40.runProgressAnim(2000);
                mProgressView60.runProgressAnim(3000);
                mProgressView80.runProgressAnim(4000);
                mProgressView100.runProgressAnim(5000);

                mCircleProgressViewNormal.runProgressAnim(1000);
                mCircleProgressViewFillIn.runProgressAnim(2000);
                mCircleProgressViewFillInArc.runProgressAnim(3000);
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