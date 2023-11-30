package com.zqw.mobile.grainfull.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zqw.mobile.grainfull.R;

/**
 * 仿微信语音
 * 配合“AudioRecorderButton”使用
 */
public class DialogManager {

    private Dialog mDialog;

    private ImageView mIcon;
    private ImageView mVoice;
    private TextView mLabel;

    private Context mContext;

    public DialogManager(Context context) {
        mContext = context;
    }

    public void showRecordingDialog() {
        mDialog = new Dialog(mContext, R.style.Theme_AudioDialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_audio_recorder, null);
        mDialog.setContentView(view);

        mIcon = mDialog.findViewById(R.id.id_recorder_dialog_icon);
        mVoice = mDialog.findViewById(R.id.id_recorder_dialog_voice);
        mLabel = mDialog.findViewById(R.id.id_recorder_dialog_label);

        mDialog.show();
    }

    public void recording() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mLabel.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.mipmap.recorder);
            mLabel.setText("手指上滑，取消发送");
        }
    }

    public void wantToCancel() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLabel.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.mipmap.cancel);
            mLabel.setText("松开手指，取消发送");
        }

    }

    public void toShort() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLabel.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.mipmap.voice_to_short);
            mLabel.setText("录音时间过短");
        }

    }

    public void dimissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }

    }

    /**
     * 通过level更新声音级别图片
     * @param level
     */
    public void updateVoiceLevel(int level) {
        int resId = mContext.getResources().getIdentifier("v" + level,"mipmap", mContext.getPackageName());
        mVoice.setImageResource(resId);
    }
}
