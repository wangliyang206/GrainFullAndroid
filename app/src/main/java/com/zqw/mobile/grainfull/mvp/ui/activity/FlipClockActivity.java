package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerFlipClockComponent;
import com.zqw.mobile.grainfull.mvp.contract.FlipClockContract;
import com.zqw.mobile.grainfull.mvp.presenter.FlipClockPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.FlipLayout;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Description:翻转时钟
 * <p>
 * Created on 2023/02/21 11:24
 *
 * @author 赤槿
 * module name is FlipClockActivity
 */
public class FlipClockActivity extends BaseActivity<FlipClockPresenter> implements FlipClockContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_flip_clock)
    LinearLayout contentLayout;                                                                     // 总布局

    @BindView(R.id.btn_flipclock_start)
    Button btnStart;

    @BindView(R.id.view_flipclock_hour)
    FlipLayout viewHour;
    @BindView(R.id.view_flipclock_minute)
    FlipLayout viewMinute;
    @BindView(R.id.view_flipclock_second)
    FlipLayout viewSecond;

    /*------------------------------------------------业务区域------------------------------------------------*/
    // 是否开始
    private boolean isFunction = false;
    // 旧 - 小时
    private int oldHour = 0;
    // 旧 - 分钟
    private int oldminute = 0;
    // 旧 - 秒
    private int oldsecond = 0;

    @Override
    protected void onDestroy() {
        isFunction = false;
        super.onDestroy();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFlipClockComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_flip_clock;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("翻转时钟");

        // 初始化
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int min = now.get(Calendar.MINUTE);
        int sec = now.get(Calendar.SECOND);

        viewHour.flip(hour, 24, FlipLayout.hour);
        viewMinute.flip(min, 60, FlipLayout.min);
        viewSecond.flip(sec, 60, FlipLayout.sec);

        // 开始计时
        contentLayout.post(() -> {
            isFunction = true;
            doCountDown();
        });
    }


    @OnClick({
            R.id.btn_flipclock_start,                                                               // (开始/暂停)按钮
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_flipclock_start:                                                          //  (开始/暂停)按钮
                if (isFunction) {
                    // 运行中
                    isFunction = false;
                    btnStart.setText("开始");
                } else {
                    // 未开始
                    isFunction = true;
                    btnStart.setText("暂停");
                    doCountDown();
                }
                break;
        }
    }

    /**
     * 计时
     */
    private void doCountDown() {
        new Thread(() -> {
            while (isFunction) {
                try {
                    Calendar now = Calendar.getInstance();
                    int hour = now.get(Calendar.HOUR_OF_DAY);
                    int min = now.get(Calendar.MINUTE);
                    int sec = now.get(Calendar.SECOND);

                    int mHour = oldHour - hour;
                    int mMinute = oldminute - min;
                    int mSecond = oldsecond - sec;

                    Timber.i("####sec=" + sec + "   oldsecond=" + oldsecond + "    mSecond=" + mSecond);

                    oldHour = hour;
                    oldminute = min;
                    oldsecond = sec;
                    if (mHour == -1 || mHour == 23) {
                        viewHour.smoothDownFlip(1, 24, FlipLayout.hour, false, hour);
                    }

                    if (mMinute == -1 || mMinute == 59) {
                        viewMinute.smoothDownFlip(1, 60, FlipLayout.min, false, min);
                    }

                    if (mSecond == -1 || mSecond == 59) {
                        viewSecond.smoothDownFlip(1, 60, FlipLayout.sec, false, sec);
                    }
//                    Timber.i("####mHour=" + mHour + "   mMinute=" + mMinute + "  mSecond=" + mSecond);

                    Thread.sleep(1000);
                } catch (Exception ignored) {
                }

            }
        }).start();

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