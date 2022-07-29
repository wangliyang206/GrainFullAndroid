package com.zqw.mobile.grainfull.app.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.blankj.utilcode.util.FileUtils;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;

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
        // 播放
        mPlay = view.findViewById(R.id.btn_popaudiolayout_play);
        mPlay.setOnClickListener(this);
        // 保存
        Button mSave = view.findViewById(R.id.btn_popaudiolayout_save);
        mSave.setOnClickListener(this);

        // 初始化声音
//        mAudioTrack = new AudioTrack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lila_popaudiolayout_close:
            case R.id.imvi_popaudiolayout_close:
                dismiss();
                break;
            case R.id.btn_popaudiolayout_play:
                onPlayPCM(mAudioPath);
                break;
            case R.id.btn_popaudiolayout_save:

                break;
        }
    }

    /**
     * 播放PCM文件
     */
    public void onPlayPCM(String path) {
        mPlay.setEnabled(false);
        int bufferSize = AudioTrack.getMinBufferSize(16000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        Timber.i("PlayRecord2: %s", bufferSize);
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 16000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
        FileInputStream fis = null;
        try {
            audioTrack.play();
            fis = new FileInputStream(path);
            byte[] buffer = new byte[bufferSize];
            int len = 0;
            while ((len = fis.read(buffer)) != -1) {
//                    Log.d(TAG, "playPCMRecord: len " + len);
                audioTrack.write(buffer, 0, len);
            }
        } catch (Exception e) {
            ArmsUtils.makeText(getContentView().getContext(), "播放报错了！");
        } finally {
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
        mPlay.setEnabled(true);
    }

    @Override
    public void dismiss() {
        super.dismiss();

        // 删除 PCM 文件。
        FileUtils.delete(mAudioPath);
    }
}
