package com.zqw.mobile.grainfull.app.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.audiofx.Visualizer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.mvp.ui.widget.VisualizeView;

import java.io.FileInputStream;
import java.io.IOException;

import timber.log.Timber;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.app.dialog
 * @ClassName: AudioDialog
 * @Description: 音频Dialog
 * @Author: WLY
 * @CreateDate: 2022/7/29 17:36
 */
public class AudioDialog extends PopupWindow implements View.OnClickListener {
    // PCM文件路径
    final String mAudioPath = Constant.AUDIO_PATH + "output-0.pcm";
    // 播放按钮
    private Button mPlay;
    // 保存按钮
    private Button mSave;
    // 显示音频音波的控件
    private VisualizeView visualizerView;
    private AudioTrack audioTrack;
    private FileInputStream fis = null;
    private Visualizer visualizer;

    public AudioDialog(Context context) {
        super(context);

        //初始化下拉列表
        View view = LayoutInflater.from(context).inflate(R.layout.pop_audio_layout, null);
        setContentView(view);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);

        // 设置背景色
        setBackgroundDrawable(new ColorDrawable());

        // 让popwin获取焦点
        setFocusable(true);
        // 点击弹出窗口区域之外的任意区域，则该窗口关闭
        setOutsideTouchable(true);

        // 关闭按钮
        view.findViewById(R.id.imvi_popaudiolayout_close).setOnClickListener(this);
        view.findViewById(R.id.lila_popaudiolayout_close).setOnClickListener(this);
        visualizerView = view.findViewById(R.id.vivi_popaudiolayout_graphics);
        // 播放
        mPlay = view.findViewById(R.id.btn_popaudiolayout_play);
        mPlay.setOnClickListener(this);
        // 保存
        mSave = view.findViewById(R.id.btn_popaudiolayout_save);
        mSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lila_popaudiolayout_close:
            case R.id.imvi_popaudiolayout_close:
                dismiss();
                break;
            case R.id.btn_popaudiolayout_play:
                onPlayPCM();
                break;
            case R.id.btn_popaudiolayout_save:

                break;
        }
    }

    /**
     * 播放PCM文件
     */
    public void onPlayPCM() {
        new Thread(playPCMRecord).start();
    }

    private final Handler mDispatcher = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                // 控制Btn不可以点击
                mPlay.setEnabled(false);
                mSave.setEnabled(false);
            }

            if (msg.what == 2) {
                // 控制Btn可以点击了
                mPlay.setEnabled(true);
                mSave.setEnabled(true);
            }

            if (msg.what == 4) {
                // 途中关闭，停止音频
                onClose();
            }
        }
    };

    private final Runnable playPCMRecord = () -> {
        mDispatcher.sendEmptyMessage(1);
        int bufferSize = AudioTrack.getMinBufferSize(16000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        Timber.i("PlayRecord2: %s", bufferSize);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 16000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
        try {
            visualizer = new Visualizer(audioTrack.getAudioSessionId());
            //生成Visualizer实例之后，为其设置可视化数据的大小，其范围是Visualizer.getCaptureSizeRange()[0] ~ Visualizer.getCaptureSizeRange()[1]，此处设置为最大值：
            visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
//            listener：回调对象
//            rate：采样的频率，其范围是0~Visualizer.getMaxCaptureRate()，此处设置为最大值一半。
//            waveform：是否获取波形信息
//            fft：是否获取快速傅里叶变换后的数据
            visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
                /** 波形数据回调 */
                @Override
                public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
                }

                /** 傅里叶数据回调，即频率数据回调 */
                @Override
                public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                    float[] model = new float[fft.length / 2 + 1];
                    model[0] = (byte) Math.abs(fft[1]);
                    int j = 1;

                    for (int i = 2; i < fft.length / 2; ) {
                        model[j] = (float) Math.hypot(fft[i], fft[i + 1]);
                        i += 2;
                        j++;
                        model[j] = (float) Math.abs(fft[j]);
                    }
                    // model即为最终用于绘制的数据
                    visualizerView.setColor(android.R.color.black);
                    visualizerView.setMode(VisualizeView.SINGLE);
                    visualizerView.setData(model);
                    Timber.i("###=%s", model.toString());
                }
            }, Visualizer.getMaxCaptureRate() / 2, false, true);
            // 设置Visualizer启动
            visualizer.setEnabled(true);

            audioTrack.play();
            fis = new FileInputStream(mAudioPath);
            byte[] buffer = new byte[bufferSize];
            int len = 0;
            while ((len = fis.read(buffer)) != -1) {
//                    Log.d(TAG, "playPCMRecord: len " + len);
                audioTrack.write(buffer, 0, len);
            }
        } catch (Exception e) {
            ArmsUtils.makeText(getContentView().getContext(), "播放报错了！");
        } finally {
            onClose();
            if (mDispatcher != null)
                mDispatcher.sendEmptyMessage(2);
        }
    };

    /**
     * 关闭语音播报
     */
    private void onClose() {
        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack = null;
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();

        // 删除 PCM 文件。
        FileUtils.delete(mAudioPath);
        if (mDispatcher != null) {
            mDispatcher.sendEmptyMessage(4);
        }

        if (visualizer != null) {
            visualizer.setEnabled(false);
            visualizer.release();
        }
    }
}
