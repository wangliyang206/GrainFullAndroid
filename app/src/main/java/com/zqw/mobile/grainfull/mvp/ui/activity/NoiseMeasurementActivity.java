package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.white.progressview.CircleProgressView;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.dialog.NoiseLevelDialog;
import com.zqw.mobile.grainfull.app.utils.RxUtils;
import com.zqw.mobile.grainfull.di.component.DaggerNoiseMeasurementComponent;
import com.zqw.mobile.grainfull.mvp.contract.NoiseMeasurementContract;
import com.zqw.mobile.grainfull.mvp.presenter.NoiseMeasurementPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Description:噪音测量
 * <p>
 * Created on 2023/01/13 10:24
 *
 * @author 赤槿
 * module name is NoiseMeasurementActivity
 */
public class NoiseMeasurementActivity extends BaseActivity<NoiseMeasurementPresenter> implements NoiseMeasurementContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_noise_measurement)
    ConstraintLayout contentLayout;                                                                 // 主布局

    @BindView(R.id.txvi_noisemeasurement_max)
    TextView txviMax;
    @BindView(R.id.txvi_noisemeasurement_min)
    TextView txviMin;

    @BindView(R.id.cpvi_noisemeasurement_curr)
    CircleProgressView mCircleProgressView;
    @BindView(R.id.txvi_noisemeasurement_tips)
    TextView txviTips;
    @BindView(R.id.txvi_noisemeasurement_intr)
    TextView txviIntr;

    /*------------------------------------------------业务区域------------------------------------------------*/
    // 噪音等级弹框
    private NoiseLevelDialog mDialog;
    // 获取语音运行是否运行着？
    private boolean isGetVoiceRun;
    // 录音操作对象
    private AudioRecord mAudioRecord;
    // 采样率（Hz）
    private static final int SAMPLE_RATE_IN_HZ = 8000;
    // 缓冲区大小
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
    // 当前的噪音值
    private int curDb = 0;
    // 噪音 最小分贝 和 最大分贝
    private int minDb = 40, maxDb = 30;
    // 修正偏移量
    public int mDbOffset = 0;
    // 锁
    private final Object mLock = new Object();
    // 设置采样速度
    public int mSampleTime = 5;

    @Override
    protected void onDestroy() {
        this.isGetVoiceRun = false;
        if (mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
        super.onDestroy();
        this.mDialog = null;
    }

    /**
     * 将状态栏改为浅色、深色模式(状态栏 icon 和字体，false = 浅色，true = 深色)
     */
    public boolean useLightStatusBar() {
        return false;
    }

    /**
     * 不支持滑动返回
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    /**
     * 根据主题使用不同的颜色。
     * 如果想要纯透明，则需要重写此方法，返回值为 -1 即可。
     */
    public int useStatusBarColor() {
        return getResources().getColor(R.color.noise_bg_color);
    }

    @Override
    protected void setForm() {
        // 设置屏幕常量
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerNoiseMeasurementComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_noise_measurement;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 增加下划线
        txviIntr.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        // 初始化弹框
        mDialog = new NoiseLevelDialog(this);

        // 延迟一秒后执行
        RxUtils.startDelayed(1, this, this::getNoiseLevel);

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "noise_measurement");
    }

    @OnClick({
            R.id.txvi_noisemeasurement_intr,                                                        // 查看噪音等级
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txvi_noisemeasurement_intr:                                                   // 查看噪音等级
                if (mDialog != null)
                    mDialog.show();
                break;
        }
    }

    /**
     * 获取噪声级别
     */
    public void getNoiseLevel() {
        isGetVoiceRun = true;
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
        // 更新记录
        refreshT.start();
        new Thread(() -> {
            mAudioRecord.startRecording();
            short[] buffer = new short[BUFFER_SIZE];
            while (isGetVoiceRun) {
                //r是实际读取的数据长度，一般而言r会小于buffersize
                int r = mAudioRecord.read(buffer, 0, BUFFER_SIZE);
                long v = 0;
                // 将 buffer 内容取出，进行平方和运算
                for (int i = 0; i < buffer.length; i++) {
                    v += buffer[i] * buffer[i];
                }
                // 平方和除以数据总长度，得到音量大小。
                double mean = v / (double) r;
                double volume = 10 * Math.log10(mean);
                Timber.d(TAG + "分贝值:" + volume);
                if (volume > 10)
                    // 转换为float防止输出过多
                    curDb = (int) volume + mDbOffset;
                if (curDb > maxDb) maxDb = curDb;
                else if (curDb < minDb) minDb = curDb;
                // 大概一秒十次
                synchronized (mLock) {
                    try {
                        // 等待mSampleTime毫秒
                        mLock.wait(1000 / mSampleTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            mAudioRecord.stop();
            mAudioRecord.release();
            mAudioRecord = null;
        }).start();
    }

    //------------------------多线程部分，负责控制控件显示噪音值-----------------//
    Handler mHandler = new Handler();
    final Runnable runnable = () -> {
        if (!isDestroyed()) {
            mCircleProgressView.setProgress(curDb);
            txviMin.setText(String.valueOf(minDb));
            txviMax.setText(String.valueOf(maxDb));
            String tips = goBewrite(curDb);
            if (!tips.isEmpty()) {
                txviTips.setText(tips);
            }

        }
    };
    final Thread refreshT = new Thread() {
        // public boolean isrun = true;
        @Override
        public void run() {
            while (true) {
                if (!isGetVoiceRun) {
                    // 结束，跳出
                    return;
                }
                // 加入到消息队列
                if (mHandler != null){
                    mHandler.post(runnable);
                }
                try {
                    // 更新时间
                    sleep(100);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    };

    /**
     * 根据噪音大小获取噪音所处的环境
     * <p>
     * 0 - 19       极静，几乎感觉不到
     * 20 - 39      安静，犹如轻声语
     * 40 - 59      正常，一般普通室内谈话
     * 60 - 69      吵闹，有损神经
     * 70 - 89      很吵，神经细胞受到破坏
     * 90 - 109     吵闹加剧，听力受损
     * 110 - 129    震耳欲聋
     * 130 - 179    无法忍受
     * 180及上       完全丧失听力
     *
     * @param degree 音量度
     */
    public String goBewrite(int degree) {
        if (degree < 10) {
            return "安静的环境";
        }
        if (degree > 10 && degree < 20) {
            return "钟表的滴答环境";

        }
        if (degree > 20 && degree < 30) {
            return "在图书馆环境";
        }
        if (degree > 30 && degree < 40) {
            return "公园舒适环境";
        }
        if (degree > 40 && degree < 50) {
            return "静谧的小道环境";
        }
        if (degree > 50 && degree < 60) {
            return "正常的交流讨论环境";
        }
        if (degree > 60 && degree < 70) {
            return "繁忙的街道里的环境";
        }
        if (degree > 70 && degree < 80) {
            return "手机铃声响起的环境";
        }
        if (degree > 80 && degree < 90) {
            return "摇滚音乐的聚会厅环境";
        }
        if (degree > 90 && degree < 100) {
            return "刺耳的雷声环境";
        }
        if (degree > 100) {
            return "人无法忍受的环境";
        }
//        return "正在获取环境...";
        return "";
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