package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.VisualizerHelper;
import com.zqw.mobile.grainfull.di.component.DaggerAudioWaveformComponent;
import com.zqw.mobile.grainfull.mvp.contract.AudioWaveformContract;
import com.zqw.mobile.grainfull.mvp.presenter.AudioWaveformPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.AttachmentRingView;
import com.zqw.mobile.grainfull.mvp.ui.widget.ColumnarView;
import com.zqw.mobile.grainfull.mvp.ui.widget.DiffusionRingView;
import com.zqw.mobile.grainfull.mvp.ui.widget.HorizontalEnergyView;
import com.zqw.mobile.grainfull.mvp.ui.widget.SiriView;
import com.zqw.mobile.grainfull.mvp.ui.widget.SoundRotatingCircleView;
import com.zqw.mobile.grainfull.mvp.ui.widget.WaveRingView;

import butterknife.BindView;

/**
 * Description:音频波形 效果
 * <p>
 * Created on 2022/08/03 14:42
 *
 * @author 赤槿
 * module name is AudioWaveformActivity
 */
public class AudioWaveformActivity extends BaseActivity<AudioWaveformPresenter> implements AudioWaveformContract.View {
    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.view_audiowaveform_wave)
    WaveRingView viewWave;
    @BindView(R.id.view_audiowaveform_ring)
    AttachmentRingView viewRing;
    @BindView(R.id.view_audiowaveform_siri)
    SiriView viewSiri;
    @BindView(R.id.view_audiowaveform_rotating)
    SoundRotatingCircleView viewRotating;

    @BindView(R.id.view_audiowaveform_diffusion)
    DiffusionRingView viewDiffusion;
    @BindView(R.id.view_audiowaveform_horizontalEnergyView)
    HorizontalEnergyView horizontalEnergyView;
    @BindView(R.id.view_audiowaveform_columnar)
    ColumnarView viewColumnarView;

    /*------------------------------------------业务信息------------------------------------------*/
    private Visualizer visualizer;
    private MediaPlayer player;

    @Override
    protected void onDestroy() {
        if (player != null) {
            player.stop();
            player.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player.isPlaying()) {
            player.pause();
        }

        VisualizerHelper.getInstance().removeCallBack(viewWave);
        VisualizerHelper.getInstance().removeCallBack(viewRing);
        VisualizerHelper.getInstance().removeCallBack(viewSiri);
        VisualizerHelper.getInstance().removeCallBack(viewRotating);
        VisualizerHelper.getInstance().removeCallBack(viewDiffusion);
        VisualizerHelper.getInstance().removeCallBack(horizontalEnergyView);
        VisualizerHelper.getInstance().removeCallBack(viewColumnarView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!player.isPlaying()) {
            player.start();
        }

        VisualizerHelper.getInstance().addCallBack(viewWave);
        VisualizerHelper.getInstance().addCallBack(viewRing);
        VisualizerHelper.getInstance().addCallBack(viewSiri);
        VisualizerHelper.getInstance().addCallBack(viewRotating);
        VisualizerHelper.getInstance().addCallBack(viewDiffusion);
        VisualizerHelper.getInstance().addCallBack(horizontalEnergyView);
        VisualizerHelper.getInstance().addCallBack(viewColumnarView);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAudioWaveformComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_audio_waveform;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("音频波形");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "audio_waveform");

        init();
        initPlayer();
    }

    private void init() {
        // 基线
        viewWave.setBase(true);
        // 波纹
        viewWave.setWave(true);
        // 能量点
        viewWave.setPoint(true);
        // 显示文字
        viewWave.setDrawText(true);
        // 均衡能量强度
        viewWave.setPowerOffset(true);
        // 能量点扩散
        viewWave.setSpread(true);
        // 旋转
        viewWave.setRotate(true);
        // 文字位移
        viewWave.setMove(true);
        // 能量阈值设置
        viewWave.setScope(10);
        // 能量取值设置
        viewWave.setValue(150);
        // 能量点扩散速度设置
        viewWave.setSpeed(3);
        // 文字位移阈值设置
        viewWave.setMoveValue(1000);


        // 音柱
        viewRing.setColumnar(true);
        // 爆炸粒子
//        viewRing.setBomb(true);
        // 声波
//        viewRing.setWave(true);
        // 旋转
        viewRing.setRotate(true);
        // 随机开始角度
        viewRing.setRandom(true);
        // 范围设置
        viewRing.setScope(50);
        // 起点幅度设置
        viewRing.setStart(10);


        // 阈值设置
        viewDiffusion.setThreshold(300);
        // 起点半径设置
        viewDiffusion.setStartRadius(10);

        // 设置形状，true圆形，false方形
        horizontalEnergyView.setCircle(true);

        // 能量块下将速度设置
        viewColumnarView.setBlockSpeed(3);
    }

    /**
     * 初始化播放器
     */
    private void initPlayer() {
        player = MediaPlayer.create(this, R.raw.demo_1);
        player.setOnPreparedListener(mp -> mp.start());
        player.setLooping(true);
        player.start();

        int mediaPlayerId = player.getAudioSessionId();
        if (visualizer == null) {
            visualizer = new Visualizer(mediaPlayerId);
        } else {
            visualizer.release();
        }
        //可视化数据的大小： getCaptureSizeRange()[0]为最小值，getCaptureSizeRange()[1]为最大值
        int captureSize = Visualizer.getCaptureSizeRange()[1];
        int captureRate = Visualizer.getMaxCaptureRate() * 3 / 4;

        visualizer.setCaptureSize(captureSize);
        visualizer.setDataCaptureListener(VisualizerHelper.getInstance().getDataCaptureListener(), captureRate, true, true);
        visualizer.setScalingMode(Visualizer.SCALING_MODE_NORMALIZED);
        visualizer.setEnabled(true);
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