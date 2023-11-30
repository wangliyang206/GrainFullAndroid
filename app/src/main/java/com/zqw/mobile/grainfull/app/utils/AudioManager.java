package com.zqw.mobile.grainfull.app.utils;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * DialogManager为何不写成单例
 * dialog 依赖于context（activity所在的）
 * 单例需要application级别，DialogManager使用的单例话
 * 造成内存泄漏，造成错误
 */
public class AudioManager {

    private MediaRecorder mMediaRecorder;
    private String mDir;
    private String mCurrentFilePath;

    private static AudioManager mInstance;

    //录音是否准备好
    private boolean bPrepared;


    /**
     * 回调准备完毕
     */
    public interface AudioStateListener{
        void wellPrepared();
    }

    public AudioStateListener mAduioStateListener;

    public void setOnAduioStateListener(AudioStateListener mAduioStateListener) {
        this.mAduioStateListener = mAduioStateListener;
    }


    public AudioManager(String dir){
        mDir = dir;
    }

    public static AudioManager getInstance(String dir){
        if(mInstance == null){
            synchronized (AudioManager.class){
                if(mInstance == null){
                    mInstance = new AudioManager(dir);
                }
            }
        }

        return mInstance;
    }

    public void prepareAudio(){

        try {
            //开始是为false
            bPrepared = false;

            File dir = new File(mDir);
            if(!dir.exists()){
                dir.mkdirs();
            }

            String fileName = generateFileName();
            File file = new File(dir, fileName);

            //当前文件的路径
            mCurrentFilePath = file.getAbsolutePath();
            mMediaRecorder = new MediaRecorder();
            //设置输出文件
            mMediaRecorder.setOutputFile(file.getAbsolutePath());
            //设置MediaRecorder的音频源为麦克风
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置音频格式
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            //设置音频的编码为amr
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            //准备结束
            bPrepared = true;
            if(mAduioStateListener != null){
                mAduioStateListener.wellPrepared();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 随机生成的文件名称
     * @return
     */
    private String generateFileName() {
        return UUID.randomUUID().toString() + ".amr";
    }

    public int getVoiceLevel(int maxLevel){
        if(bPrepared){
            try{
                //mMediaRecorder.getMaxAmplitude() 1-32767,得到0-1之间的值
                //最后取整是1 - 7
                return maxLevel * mMediaRecorder.getMaxAmplitude() / 32768 + 1;
            }catch (Exception e){

            }

        }
        return 1;
    }

    public void release(){
        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;
    }

    public void cancel(){
        release();
        if(mCurrentFilePath != null){
            File file = new File(mCurrentFilePath);
            file.delete();
            mCurrentFilePath = null;
        }
    }


    public String getCurrentFilePath() {
        return mCurrentFilePath;
    }

}
