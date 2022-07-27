package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.dialog.PopupSelectList;
import com.zqw.mobile.grainfull.di.component.DaggerBaiduVoiceOnlineSettingComponent;
import com.zqw.mobile.grainfull.mvp.contract.BaiduVoiceOnlineSettingContract;
import com.zqw.mobile.grainfull.mvp.presenter.BaiduVoiceOnlineSettingPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:百度AI - 短语音识别 - 设置
 * <p>
 * Created on 2022/07/26 18:34
 *
 * @author 赤槿
 * module name is BaiduVoiceOnlineSettingActivity
 */
public class BaiduVoiceOnlineSettingActivity extends BaseActivity<BaiduVoiceOnlineSettingPresenter> implements BaiduVoiceOnlineSettingContract.View {
    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.activity_baidu_voice_online_setting)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.txvi_voiceonlinesetting_language)
    TextView txviLanguage;

    @BindView(R.id.checkbox_voiceonlinesetting_cb)
    CheckBox cbLongSpeech;

    @BindView(R.id.txvi_voiceonlinesetting_vad)
    TextView txviVad;

    @BindView(R.id.txvi_voiceonlinesetting_vadisopen)
    TextView txviVadIsOpen;
    /*------------------------------------------业务信息------------------------------------------*/
    // 缓存操作对象
    private SharedPreferences sp;
    // 选择语种
    private PopupSelectList popSelectPid;
    // 选择VAD时长设置
    private PopupSelectList popSelectVad;
    // 选择VAD是否开启
    private PopupSelectList popSelectVadIsOpen;

    private final String PID_KEY = "pid";
    private final String LONG_SPEECH = "enable.long.speech";
    private final String VAD_KEY = "vad.endpoint-timeout";
    private final String VAD_IS_OPEN_KEY = "vad";

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBaiduVoiceOnlineSettingComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_baidu_voice_online_setting;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("语音识别设置");
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        initPid();
        initLongSpeech();
        initVad();
        initVadIsOpen();
    }

    /**
     * 初始化语种
     */
    private void initPid() {
        String[] mVal = getResources().getStringArray(R.array.pid);
        List<String> mList = new ArrayList<>(Arrays.asList(mVal));

        PopupSelectList.ItemClick itemClick = (position, info) -> {
            // 显示内容
            txviLanguage.setText(info);
            setSharedPreferencesByKey(PID_KEY, info);
        };
        popSelectPid = new PopupSelectList(this, "PID，语种", mList, itemClick);

        String val = getSharedStringByKey(PID_KEY);
        if (!TextUtils.isEmpty(val)) {
            txviLanguage.setText(mList.get(0));
        }
    }

    /**
     * 设置长语音
     */
    private void initLongSpeech() {
        boolean val = getSharedBooleanByKey(LONG_SPEECH);
        cbLongSpeech.setChecked(val);

        cbLongSpeech.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setSharedPreferencesByKey(LONG_SPEECH, isChecked);
        });
    }

    /**
     * 初始化VAD时长
     */
    private void initVad() {
        String[] mVal = getResources().getStringArray(R.array.vad_timeout_list);
        List<String> mList = new ArrayList<>(Arrays.asList(mVal));

        PopupSelectList.ItemClick itemClick = (position, info) -> {
            // 显示内容
            txviVad.setText(info);
            setSharedPreferencesByKey(VAD_KEY, info);
        };
        popSelectVad = new PopupSelectList(this, "VAD时长设置", mList, itemClick);

        String val = getSharedStringByKey(VAD_KEY);
        if (!TextUtils.isEmpty(val)) {
            txviVad.setText(mList.get(0));
        }
    }


    /**
     * 初始化VAD是否开启
     */
    private void initVadIsOpen() {
        String[] mVal = getResources().getStringArray(R.array.vad_list);
        List<String> mList = new ArrayList<>(Arrays.asList(mVal));

        PopupSelectList.ItemClick itemClick = (position, info) -> {
            // 显示内容
            txviVadIsOpen.setText(info);
            setSharedPreferencesByKey(VAD_IS_OPEN_KEY, info);
        };
        popSelectVadIsOpen = new PopupSelectList(this, "VAD是否开启", mList, itemClick);

        String val = getSharedStringByKey(VAD_IS_OPEN_KEY);
        if (!TextUtils.isEmpty(val)) {
            txviVadIsOpen.setText(mList.get(0));
        }
    }

    @OnClick({
            R.id.lila_voiceonlinesetting_language,                                                  // 设置语种
            R.id.lila_voiceonlinesetting_longspeech,                                                // 长语音
            R.id.lila_voiceonlinesetting_vad,                                                       // VAD时长设置
            R.id.lila_voiceonlinesetting_vadisopen,                                                 // VAD是否开启
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lila_voiceonlinesetting_language:                                             // 设置语种
                if (popSelectPid != null) {
                    popSelectPid.showAtLocation(contentLayout, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else {
                    showMessage("暂无语种，请联系管理员！");
                }
                break;
            case R.id.lila_voiceonlinesetting_longspeech:                                           // 长语音
                cbLongSpeech.setChecked(!cbLongSpeech.isChecked());
                break;
            case R.id.lila_voiceonlinesetting_vad:                                                  // VAD时长设置
                if (popSelectVad != null) {
                    popSelectVad.showAtLocation(contentLayout, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else {
                    showMessage("暂无VAD时长设置，请联系管理员！");
                }
                break;
            case R.id.lila_voiceonlinesetting_vadisopen:                                            // VAD是否开启
                if (popSelectVadIsOpen != null) {
                    popSelectVadIsOpen.showAtLocation(contentLayout, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else {
                    showMessage("暂无VAD是否开启，请联系管理员！");
                }
                break;
        }
    }

    /**
     * 获取缓存文件中的值
     */
    private String getSharedStringByKey(String key) {
        return sp.getString(key, "");
    }

    private boolean getSharedBooleanByKey(String key) {
        return sp.getBoolean(key, false);
    }

    /**
     * 保存缓存文件
     */
    private void setSharedPreferencesByKey(String key, String val) {
        if (TextUtils.isEmpty(val)) {
            sp.edit().remove(key).apply();
        } else {
            sp.edit().putString(key, val).apply();
        }
    }

    private void setSharedPreferencesByKey(String key, boolean val) {
        if (val) {
            sp.edit().putBoolean(key, val).apply();
        } else {
            sp.edit().remove(key).apply();
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