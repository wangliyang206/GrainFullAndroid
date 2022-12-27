package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.zqw.mobile.grainfull.app.tts.SynthActivity.PITCH;
import static com.zqw.mobile.grainfull.app.tts.SynthActivity.SPEAKER;
import static com.zqw.mobile.grainfull.app.tts.SynthActivity.SPEED;
import static com.zqw.mobile.grainfull.app.tts.SynthActivity.VOLUME;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.dialog.AudioDialog;
import com.zqw.mobile.grainfull.app.dialog.PopupSelectList;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.tts.SynthActivity;
import com.zqw.mobile.grainfull.app.tts.listener.FileSaveListener;
import com.zqw.mobile.grainfull.di.component.DaggerBaiduSpeechSynthesisComponent;
import com.zqw.mobile.grainfull.mvp.contract.BaiduSpeechSynthesisContract;
import com.zqw.mobile.grainfull.mvp.presenter.BaiduSpeechSynthesisPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:百度语音合成
 * <p>
 * Created on 2022/07/28 14:44
 *
 * @author 赤槿
 * module name is BaiduSpeechSynthesisActivity
 */
public class BaiduSpeechSynthesisActivity extends BaseActivity<BaiduSpeechSynthesisPresenter> implements BaiduSpeechSynthesisContract.View {
    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.activity_baidu_speech_synthesis)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.edit_speechsynthesis_text)
    EditText editInput;

    @BindView(R.id.sebr_speechsynthesis_speed)
    SeekBar seekBarSpeed;
    @BindView(R.id.txvi_speechsynthesis_speed_num)
    TextView txviSpeed;

    @BindView(R.id.sebr_speechsynthesis_intonation)
    SeekBar seekBarIntonation;
    @BindView(R.id.txvi_speechsynthesis_intonation_num)
    TextView txviIntonation;

    @BindView(R.id.sebr_speechsynthesis_volume)
    SeekBar seekBarVolume;
    @BindView(R.id.txvi_speechsynthesis_volume_num)
    TextView txviVolume;

    @BindView(R.id.txvi_speechsynthesis_soundlibrary)
    TextView txviSoundLibrary;

    /*------------------------------------------业务信息------------------------------------------*/
    private SynthActivity synthActivity;
    // 选择发声人
    private PopupSelectList popSelectSpeaker;
    // 合成结果
    private AudioDialog mAudioDialog;

    @Override
    protected void onDestroy() {
        if (synthActivity != null) {
            synthActivity.onDestroy();
            synthActivity = null;
        }

        if (popSelectSpeaker != null) {
            popSelectSpeaker.dismiss();
            popSelectSpeaker = null;
        }

        super.onDestroy();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBaiduSpeechSynthesisComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_baidu_speech_synthesis;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("语音合成");

        synthActivity = new SynthActivity();
        synthActivity.initTTS(getApplicationContext(), true);

        mAudioDialog = new AudioDialog(this);

        initSeekBar();
        initSpeaker();
    }

    private void initSeekBar() {
        seekBarSpeed.setProgress(Integer.parseInt(SPEED));
        txviSpeed.setText(SPEED);
        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txviSpeed.setText(String.valueOf(progress));
                SPEED = String.valueOf(progress);
                synthActivity.setParams();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarIntonation.setProgress(Integer.parseInt(PITCH));
        txviIntonation.setText(PITCH);
        seekBarIntonation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txviIntonation.setText(String.valueOf(progress));
                PITCH = String.valueOf(progress);
                synthActivity.setParams();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarVolume.setProgress(Integer.parseInt(VOLUME));
        txviVolume.setText(VOLUME);
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txviVolume.setText(String.valueOf(progress));
                VOLUME = String.valueOf(progress);
                synthActivity.setParams();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * 初始化发声人
     */
    private void initSpeaker() {
        List<String> mList = new ArrayList<>();
        mList.add("小美（普通女声）");
        mList.add("小宇（成熟男声）");
        mList.add("逍遥（磁性男声）");
        mList.add("丫丫（可爱女童）");

        mList.add("博文（情感男声）");
        mList.add("小童（情感儿童声）");
        mList.add("小萌（情感女声）");
        mList.add("米朵（情感儿童声）");
        mList.add("小娇（情感女声）");
        mList.add("度逍遥（磁性男声）");
        mList.add("小鹿（甜美女声）");

        PopupSelectList.ItemClick itemClick = (position, info) -> {
            // 显示内容
            txviSoundLibrary.setText(info);

            switch (position) {
                case 0:                                                                             // 小美（普通女声）
                    SPEAKER = "0";
                    break;
                case 1:                                                                             // 小宇（成熟男声）
                    SPEAKER = "1";
                    break;
                case 2:                                                                             // 逍遥（磁性男声）
                    SPEAKER = "3";
                    break;
                case 3:                                                                             // 丫丫（可爱女童）
                    SPEAKER = "4";
                    break;
                case 4:                                                                             // 博文（情感男声）
                    SPEAKER = "106";
                    break;
                case 5:                                                                             // 小童（情感儿童声）
                    SPEAKER = "110";
                    break;
                case 6:                                                                             // 小萌（情感女声）
                    SPEAKER = "111";
                    break;
                case 7:                                                                             // 米朵（情感儿童声）
                    SPEAKER = "103";
                    break;
                case 8:                                                                             // 小娇（情感女声）
                    SPEAKER = "5";
                    break;
                case 9:                                                                             // 度逍遥（磁性男声）
                    SPEAKER = "5003";
                    break;
                case 10:                                                                            // 小鹿（甜美女声）
                    SPEAKER = "5118";
                    break;

            }
            synthActivity.setParams();

        };
        popSelectSpeaker = new PopupSelectList(this, "发声人", mList, itemClick);
    }

    @OnClick({
            R.id.lila_speechsynthesis_soundlibrary,                                                 // 选择声线
            R.id.btn_speechsynthesis_synthesis,                                                     // 合成
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lila_speechsynthesis_soundlibrary:                                            // 选择声线
                if (popSelectSpeaker != null) {
                    popSelectSpeaker.showAtLocation(contentLayout, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else {
                    showMessage("暂无发声人，请联系管理员！");
                }
                break;
            case R.id.btn_speechsynthesis_synthesis:                                                // 合成
                String val = editInput.getText().toString();

                if (TextUtils.isEmpty(val)) {
                    showMessage("请输入需要合成语音的文字！");
                    return;
                }

                // 开始合成(合成的文件格式为pcm，文件名称：output-0.pcm)
                synthActivity.synthesize(val);
                // 弹出Dialog，可以播放与保存。
                if (mAudioDialog != null) {
                    mAudioDialog.setPlayPath(Constant.AUDIO_PATH + FileSaveListener.fileName);
                    mAudioDialog.showAtLocation(contentLayout, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else {
                    showMessage("暂无合成结果，请联系管理员！");
                }
                break;
        }
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