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
import androidx.core.widget.NestedScrollView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerStopwatchComponent;
import com.zqw.mobile.grainfull.mvp.contract.StopwatchContract;
import com.zqw.mobile.grainfull.mvp.presenter.StopwatchPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.particle.AnimNumberView;
import com.zqw.mobile.grainfull.mvp.ui.widget.particle.ProgressLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description: 计时器
 * <p>
 * Created on 2023/02/27 16:34
 *
 * @author 赤槿
 * module name is StopwatchActivity
 */
public class StopwatchActivity extends BaseActivity<StopwatchPresenter> implements StopwatchContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_stopwatch)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.view_stopwatchactivity_progress)
    ProgressLayout viewProgress;
    @BindView(R.id.view_stopwatchactivity_scroll)
    NestedScrollView viewScroll;
    @BindView(R.id.txvi_stopwatchactivity_sign)
    TextView txviSign;
    @BindView(R.id.imvi_stopwatchactivity_start)
    ImageView btnStart;
    @BindView(R.id.imvi_stopwatchactivity_clear)
    ImageView btnClear;

    /*------------------------------------------------业务区域------------------------------------------------*/
    // 类型：0代表 未开始；1代表已开始；2代表已暂停；
    private int mType = 0;
    // 累加数
    private int mIndex = 0;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerStopwatchComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_stopwatch;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("计时器");

        // 加载默认效果
        viewProgress.initView();
    }

    @OnClick({
            R.id.imvi_stopwatchactivity_start,                                                      // 开始 or 暂停
            R.id.imvi_stopwatchactivity_clear,                                                      // 清零
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvi_stopwatchactivity_start:                                                  // 开始 or 暂停
                if (mType == 0) {
                    // 未开始，可执行开始
                    mType = 1;
                    btnStart.setImageResource(R.mipmap.icon_pause);
                    btnClear.setVisibility(View.VISIBLE);
                    btnClear.setImageResource(R.mipmap.icon_sign);
                    viewProgress.setTimer(0, AnimNumberView.UP_TIMER);
//                    viewProgress.setOnCompleteListener(() -> showMessage("正计时结束"));
                } else if (mType == 1) {
                    // 已开始，可执行暂停
                    mType = 2;
                    btnStart.setImageResource(R.mipmap.icon_start);
                    btnClear.setImageResource(R.mipmap.icon_stop);
                    viewProgress.onPause();
                } else if (mType == 2) {
                    // 已暂停，可执行继续
                    mType = 1;
                    btnStart.setImageResource(R.mipmap.icon_pause);
                    btnClear.setImageResource(R.mipmap.icon_sign);
                    viewProgress.onContinue();
                }
                break;
            case R.id.imvi_stopwatchactivity_clear:                                                 // 清零
                // 暂停状态才允许清零
                if (mType == 2) {
                    mType = 0;
                    mIndex = 0;
                    viewProgress.onClear();
                    btnClear.setVisibility(View.GONE);
                } else {
                    if (mIndex == 0) {
                        txviSign.setText("");
                    }
                    if (mIndex > 0) {
                        txviSign.append("\n\n");
                    }
                    mIndex++;
                    // 追加文字
                    txviSign.append("#" + mIndex + "     " + viewProgress.getCurrTime());
                    // 滑动到最底部\=
                    viewScroll.fullScroll(View.FOCUS_DOWN);
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