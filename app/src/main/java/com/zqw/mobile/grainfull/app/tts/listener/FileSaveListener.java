package com.zqw.mobile.grainfull.app.tts.listener;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.baidu.tts.client.SpeechError;
import com.zqw.mobile.grainfull.app.utils.MediaStoreUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import timber.log.Timber;

/**
 * 保存回调音频流到文件。您也可以直接处理音频流
 * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
 * Created by fujiayi on 2017/9/15.
 */

public class FileSaveListener extends UiMessageListener {

    private Context mContext;
    /**
     * 保存的文件名 baseName + utteranceId， 通常是 output-0.pcm
     */
    public static final String fileName = "audio.pcm";

    /**
     * 保存文件的目录
     */
    private String destDir;

    /**
     * ttsFile 文件流
     */
    private OutputStream mOutputStream;

    /**
     * ttsFile 文件buffer流
     */
    private BufferedOutputStream ttsFileBufferedOutputStream;

    private static final String TAG = "FileSaveListener";


    public FileSaveListener(Context mContext, Handler mainHandler, String destDir) {
        super(mainHandler);
        this.mContext = mContext;
        this.destDir = destDir;
    }

    @Override
    public void onSynthesizeStart(String utteranceId) {
//        String filename = baseName + utteranceId + ".pcm";
        // 保存的语音文件是 16K采样率 16bits编码 单声道 pcm文件。
        Timber.i("##### destDir =%s", destDir);
        Timber.i("##### fileName =%s", fileName);
        try {
            // 创建FileOutputStream对象
            mOutputStream = MediaStoreUtils.onCreateFileByDownload(mContext, destDir, fileName, "audio/adpcm");
            if (mOutputStream != null) {
                // 创建BufferedOutputStream对象
                ttsFileBufferedOutputStream = new BufferedOutputStream(mOutputStream);
            }
        } catch (IOException e) {
            // 请自行做错误处理
            e.printStackTrace();
            sendMessage("创建文件失败:" + destDir + fileName);
            throw new RuntimeException(e);
        }
        sendMessage("创建文件成功:" + destDir + fileName);

    }

    /**
     * 语音流 16K采样率 16bits编码 单声道 。
     *
     * @param utteranceId
     * @param data        二进制语音 ，注意可能有空data的情况，可以忽略
     * @param progress    如合成“百度语音问题”这6个字， progress肯定是从0开始，到6结束。 但progress无法保证和合成到第几个字对应。
     * @param engineType  1:音频数据由离线引擎合成； 0：音频数据由在线引擎（百度服务器）合成。
     */
    @Override
    public void onSynthesizeDataArrived(String utteranceId, byte[] data, int progress, int engineType) {
        super.onSynthesizeDataArrived(utteranceId, data, progress, engineType);
        Log.i(TAG, "合成进度回调, progress：" + progress + ";序列号:" + utteranceId);
        try {
            if (ttsFileBufferedOutputStream != null)
                ttsFileBufferedOutputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSynthesizeFinish(String utteranceId) {
        super.onSynthesizeFinish(utteranceId);
        close();
    }

    /**
     * 当合成或者播放过程中出错时回调此接口
     *
     * @param utteranceId
     * @param speechError 包含错误码和错误信息
     */
    @Override
    public void onError(String utteranceId, SpeechError speechError) {
        close();
        super.onError(utteranceId, speechError);
    }

    /**
     * 关闭流，注意可能stop导致该方法没有被调用
     */
    private void close() {
        if (ttsFileBufferedOutputStream != null) {
            try {
                ttsFileBufferedOutputStream.flush();
                ttsFileBufferedOutputStream.close();
                ttsFileBufferedOutputStream = null;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (mOutputStream != null) {
            try {
                mOutputStream.close();
                mOutputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sendMessage("关闭文件成功");
    }
}
