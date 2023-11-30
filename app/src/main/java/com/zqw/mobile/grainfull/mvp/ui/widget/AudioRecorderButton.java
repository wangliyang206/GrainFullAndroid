package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.dialog.DialogManager;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.AudioManager;

/**
 * 按住说话
 */
public class AudioRecorderButton extends Button implements AudioManager.AudioStateListener {

    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_TO_CANCEL = 3;

    private int mCurSate = STATE_NORMAL;
    //已经开始录音
    private boolean isRecording = false;
    private static final int DISANCE_Y_CANCEL = 50;

    private DialogManager mDialogManager;

    private AudioManager mAudioManager;

    //记时
    private float mTime;
    //是否处罚onlongclick
    private boolean mReady;

    public AudioRecorderButton(Context context) {
        this(context, null);
    }

    public AudioRecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDialogManager = new DialogManager(getContext());

        mAudioManager = AudioManager.getInstance(Constant.AUDIO_PATH);
        mAudioManager.setOnAduioStateListener(this);
    }

    public void onStart() {
        //触发点击事件
        mReady = true;
        mAudioManager.prepareAudio();
    }

    /**
     * 录音完成的回调
     */
    public interface AudioFinishRecorderListener {
        void onFinish(float seconds, String filePath);
    }

    private AudioFinishRecorderListener onAudioFinishRecorderListener;


    public void setOnAudioFinishRecorderListener(AudioFinishRecorderListener onAudioFinishRecorderListener) {
        this.onAudioFinishRecorderListener = onAudioFinishRecorderListener;
    }

    /**
     * 音量大小Runnable
     */
    private Runnable mGetVoiceLevelRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRecording) {
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private static final int MSG_AUDIO_PREPARED = 0X110;
    private static final int MSG_VOICE_CHANGED = 0X111;
    private static final int MSG_DIALOG_DIMISS = 0X112;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_AUDIO_PREPARED:
                    //显示在aduio end process后
                    mDialogManager.showRecordingDialog();
                    isRecording = true;
                    new Thread(mGetVoiceLevelRunnable).start();
                    break;
                case MSG_VOICE_CHANGED:
                    mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));
                    break;
                case MSG_DIALOG_DIMISS:
                    mDialogManager.dimissDialog();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void wellPrepared() {
        mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                changeState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_UP:
                //未触发onlongclick,此时默认为不处理
                if (!mReady) {
                    reset();
                    return super.onTouchEvent(event);
                }

                if (!isRecording || mTime < 0.6f) {
                    mDialogManager.toShort();
                    mAudioManager.cancel();
                    //延时消失
                    mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1300);
                } else if (mCurSate == STATE_RECORDING) {//正常录制结束
                    mDialogManager.dimissDialog();
                    mAudioManager.release();

                    //监听录音完成接口
                    if (onAudioFinishRecorderListener != null) {
                        onAudioFinishRecorderListener.onFinish(mTime, mAudioManager.getCurrentFilePath());
                    }

                    //release
                    //callbackToAct
                } else if (mCurSate == STATE_WANT_TO_CANCEL) {
                    mDialogManager.dimissDialog();
                    mAudioManager.cancel();
                    //cancel
                }

                reset();
                break;
            case MotionEvent.ACTION_MOVE:
                //已经开始录音
                if (isRecording) {
                    //根据x,y坐标，判断是否想要取消
                    if (wantToCanel(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL);
                    } else {
                        changeState(STATE_RECORDING);
                    }
                }
                break;
            default:


        }
        return super.onTouchEvent(event);
    }

    /**
     * 恢复状态及标志位
     */
    private void reset() {
        isRecording = false;
        mTime = 0;
        mReady = false;
        changeState(STATE_NORMAL);
    }

    private boolean wantToCanel(int x, int y) {
        //横坐标在按钮外，到达按钮右侧
        if (x < 0 || x > getWidth()) {
            return true;
        }

        //手指横坐标在内部，y轴超过指定范围
        if (y < -DISANCE_Y_CANCEL || y > getWidth() + DISANCE_Y_CANCEL) {
            return true;
        }
        return false;
    }

    private void changeState(int stateRecording) {
        if (mCurSate != stateRecording) {
            mCurSate = stateRecording;
            switch (stateRecording) {
                case STATE_NORMAL:
                    setText(R.string.str_recorder_noraml);
                    break;
                case STATE_RECORDING:
                    setText(R.string.str_recorder_recording);
                    if (isRecording) {
                        mDialogManager.recording();
                    }
                    break;
                case STATE_WANT_TO_CANCEL:
                    setText(R.string.str_recorder_want_cancel);
                    mDialogManager.wantToCancel();
                    break;
            }
        }
    }

}
