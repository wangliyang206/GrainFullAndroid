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
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.mvp.ui.widget.VisualizeView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import jaygoo.library.converter.Mp3Converter;
import timber.log.Timber;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.app.dialog
 * @ClassName: AudioDialog
 * @Description: 音频播放+pcm转mp3+文件保存
 * @Author: WLY
 * @CreateDate: 2022/7/29 17:36
 */
public class AudioDialog extends PopupWindow implements View.OnClickListener {
    // PCM文件路径
    private String mAudioPath;
    String mSavePath;
    // 播放按钮
    private final Button mPlay;
    // 保存按钮
    private final Button mSave;
    // 显示音频音波的控件
    private final VisualizeView visualizerView;
    // 显示已保存的文件路径
    private final TextView txviPath;

    // 用于播放pcm格式的音频
    private AudioTrack audioTrack;
    private FileInputStream fis = null;
    private Visualizer visualizer;
    // 声道个数
    private int AC = 1;
    // 采样率
    private int AR = 8000;

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
        txviPath = view.findViewById(R.id.txvi_popaudiolayout_path);
        visualizerView = view.findViewById(R.id.vivi_popaudiolayout_graphics);
        // 播放
        mPlay = view.findViewById(R.id.btn_popaudiolayout_play);
        mPlay.setOnClickListener(this);
        // 保存
        mSave = view.findViewById(R.id.btn_popaudiolayout_save);
        mSave.setOnClickListener(this);
    }

    /**
     * 设置播放路径，默认使用单声道，采样率为16000Hz
     *
     * @param path 播放文件的路径，格式为pcm
     */
    public void setPlayPath(String path) {
        setPlayPath(path, AC, AR);
    }

    /**
     * 设置播放路径
     *
     * @param path 播放文件的路径，格式为pcm
     * @param ac   声道个数(1代表单声道，2代表立体声道)
     * @param ar   采样率(16000Hz、48000Hz)
     */
    public void setPlayPath(String path, int ac, int ar) {
        this.mAudioPath = path;
        this.AC = ac;
        this.AR = ar;

        txviPath.setText("");
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
//                ArmsUtils.makeText(getContentView().getContext(), "由于兼容问题，暂不开放！");
                onSave();
                break;
        }
    }

    /**
     * 保存
     */
    private void onSave() {
        mSavePath = Constant.AUDIO_PATH + "audio_" + TimeUtils.getNowString(new SimpleDateFormat("yyyyMMdd_HHmmss")) + ".mp3";

        // 1、inSampleRate 要转换的音频文件采样率，采样率每秒从连续信号中提取并组成离散信号的采样个数，单位Hz。数值越高，音质越好，常见的如8000Hz、11025Hz、22050Hz、32000Hz、44100Hz等。
        // 2、channel 声道个数
        // 3、mode 音频编码模式，包括VBR、ABR、CBR
        // 4、outSampleRate 转换后音频文件采样率
        // 5、outBitRate 输出的码率，码率又称比特率是指每秒传送的比特(bit)数，单位kbps，越高音质越好（相同编码格式下）。
        // 6、quality 压缩质量（具体数值含义上面注释已经写的很清楚了）

        // CBR常数比特率编码，码率固定，速度较快，但压缩的文件相比其他模式较大，音质也不会有很大提高，适用于流式播放方案，Lame默认的方案是这种。
        // VBR动态比特率编码，码率不固定。适用于下载后在本地播放或者在读取速度有限的设备播放，体积和为CBR的一半左右，但是输出码率不可控。
        // ABR平均比特率编码，是Lame针对CBR不佳的文件体积比和VBR生成文件大小不定的特点独创的编码模式。是一种折中方案，码率基本可控，但是好像用的不多。
        Mp3Converter.init(AR, AC, 0, AR, 96, 7);
        // 原始代码
//        Mp3Converter.init(44100, 1, 0, 44100, 96, 7);
        fileSize = new File(mAudioPath).length();
        new Thread(() -> Mp3Converter.convertMp3(mAudioPath, mSavePath)).start();

        handler.postDelayed(runnable, 500);
    }

    long fileSize;
    long bytes = 0;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            bytes = Mp3Converter.getConvertBytes();
            float progress = (100f * bytes / fileSize);
            if (bytes == -1) {
                progress = 100;
            }
            txviPath.setText("转换进度: " + progress);
            if (handler != null && progress < 100) {
                handler.postDelayed(this, 1000);
            } else {
                txviPath.setText("文件保存路径："+mSavePath);
            }
        }
    };

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

            if (msg.what == 3) {
                // 错误了
                ArmsUtils.makeText(getContentView().getContext(), "播放报错了！");
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
            mDispatcher.sendEmptyMessage(3);
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
            visualizer.release();
        }

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
