package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.baidu.idl.face.platform.ui.model.Const;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerBaiduFaceSettingComponent;
import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceSettingContract;
import com.zqw.mobile.grainfull.mvp.presenter.BaiduFaceSettingPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:百度AI - 人脸识别 - 设置
 * <p>
 * Created on 2022/07/13 16:55
 *
 * @author 赤槿
 * module name is BaiduFaceSettingActivity
 */
public class BaiduFaceSettingActivity extends BaseActivity<BaiduFaceSettingPresenter> implements BaiduFaceSettingContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/

    @BindView(R.id.announcements_switch)
    Switch announcementsSwitch;                                                                     // 语音播报开关
    @BindView(R.id.live_detect_switch)
    Switch liveDetectSwitch;                                                                        // 活体检测开关
    @BindView(R.id.actionlive_switch)
    Switch actionliveSwitch;                                                                        // 动作活体随机开关
    @BindView(R.id.actionlive_blink_checkbox)
    CheckBox blinkCheckbox;                                                                         // 眨眨眼
    @BindView(R.id.actionlive_left_turn_checkbox)
    CheckBox leftTurnCheckbox;                                                                      // 向左摇头
    @BindView(R.id.actionlive_right_turn_checkbox)
    CheckBox rightTurnCheckbox;                                                                     // 向右摇头
    @BindView(R.id.actionlive_nod_checkbox)
    CheckBox nodCheckbox;                                                                           // 点点头
    @BindView(R.id.actionlive_look_up_checkbox)
    CheckBox lookUpCheckbox;                                                                        // 向上抬头
    @BindView(R.id.actionlive_open_mouth_checkbox)
    CheckBox openMouthCheckbox;                                                                     // 张嘴
    @BindView(R.id.layout_active_type)
    RelativeLayout relativeActionType;
    @BindView(R.id.actionlive_layout)
    RelativeLayout relativeActionRandom;


    @BindView(R.id.text_enter_quality)
    TextView mTextEnterQuality;
    @BindView(R.id.blink_layout)
    RelativeLayout mRelativeEye;
    @BindView(R.id.shake_head_layout)
    RelativeLayout mRelativeShake;
    @BindView(R.id.left_turn_layout)
    RelativeLayout mRelativeLeft;
    @BindView(R.id.right_turn_layout)
    RelativeLayout mRelativeRight;
    @BindView(R.id.nod_layout)
    RelativeLayout mRelativeNod;
    @BindView(R.id.look_up_layout)
    RelativeLayout mRelativeUp;
    @BindView(R.id.open_mouth_layout)
    RelativeLayout mRelativeMouth;
    @BindView(R.id.quality_layout)
    RelativeLayout mRelativeQuality;

    /*------------------------------------------------业务区域------------------------------------------------*/
    private static final int VALUE_MIN_ACTIVE_NUM = 1;  // 设置最少动作活体数量
    private List<LivenessTypeEnum> livenessList = new ArrayList<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaiduFaceRecognitionActivity.livenessList.clear();
        Collections.sort(this.livenessList, new ComparatorValues());
        BaiduFaceRecognitionActivity.livenessList = this.livenessList;
        BaiduFaceRecognitionActivity.isLivenessRandom = actionliveSwitch.isChecked();
        BaiduFaceRecognitionActivity.isOpenSound = announcementsSwitch.isChecked();
        BaiduFaceRecognitionActivity.isActionLive = liveDetectSwitch.isChecked();
        setFaceConfig();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBaiduFaceSettingComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_baidu_face_setting;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initView();
        settingChecked();
        initListener();
    }

    private void initView() {
        blinkCheckbox.setTag(LivenessTypeEnum.Eye);
        // shakeHeadCheckbox.setTag(LivenessTypeEnum.HeadLeftOrRight);
        leftTurnCheckbox.setTag(LivenessTypeEnum.HeadLeft);
        rightTurnCheckbox.setTag(LivenessTypeEnum.HeadRight);
        nodCheckbox.setTag(LivenessTypeEnum.HeadDown);
        lookUpCheckbox.setTag(LivenessTypeEnum.HeadUp);
        openMouthCheckbox.setTag(LivenessTypeEnum.Mouth);
    }

    private void settingChecked() {
        // 语音播报开关
        announcementsSwitch.setChecked(BaiduFaceRecognitionActivity.isOpenSound);
        // 活体检测开关
        liveDetectSwitch.setChecked(BaiduFaceRecognitionActivity.isActionLive);
        // 动作活体随机开关
        actionliveSwitch.setChecked(BaiduFaceRecognitionActivity.isLivenessRandom);
        if (!liveDetectSwitch.isChecked()) {
            relativeActionRandom.setVisibility(View.GONE);
            relativeActionType.setVisibility(View.GONE);
        } else {
            relativeActionRandom.setVisibility(View.VISIBLE);
            relativeActionType.setVisibility(View.VISIBLE);
        }

        List<LivenessTypeEnum> list = BaiduFaceRecognitionActivity.livenessList;
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == LivenessTypeEnum.Eye) {
                    blinkCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.Eye)) {
                        livenessList.add(LivenessTypeEnum.Eye);
                    }
                }
                if (list.get(i) == LivenessTypeEnum.Mouth) {
                    openMouthCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.Mouth)) {
                        livenessList.add(LivenessTypeEnum.Mouth);
                    }
                }
                if (list.get(i) == LivenessTypeEnum.HeadRight) {
                    rightTurnCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadRight)) {
                        livenessList.add(LivenessTypeEnum.HeadRight);
                    }
                }
                if (list.get(i) == LivenessTypeEnum.HeadLeft) {
                    leftTurnCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadLeft)) {
                        livenessList.add(LivenessTypeEnum.HeadLeft);
                    }
                }
                if (list.get(i) == LivenessTypeEnum.HeadUp) {
                    lookUpCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadUp)) {
                        livenessList.add(LivenessTypeEnum.HeadUp);
                    }
                }
                if (list.get(i) == LivenessTypeEnum.HeadDown) {
                    nodCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadDown)) {
                        livenessList.add(LivenessTypeEnum.HeadDown);
                    }
                }
//                if (list.get(i) == LivenessTypeEnum.HeadLeftOrRight) {
//                    shakeHeadCheckbox.setChecked(true);
//                    if (!livenessList.contains(LivenessTypeEnum.HeadLeftOrRight)) {
//                        livenessList.add(LivenessTypeEnum.HeadLeftOrRight);
//                    }
//                }
            }
        }
    }


    private void initListener() {
        liveDetectSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                relativeActionRandom.setVisibility(View.GONE);
                relativeActionType.setVisibility(View.GONE);
            } else {
                relativeActionRandom.setVisibility(View.VISIBLE);
                relativeActionType.setVisibility(View.VISIBLE);
            }
        });

//        shakeHeadCheckbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (shakeHeadCheckbox.isChecked()) {
//                    shakeHeadCheckbox.setChecked(true);
//                    if (!livenessList.contains(LivenessTypeEnum.HeadLeftOrRight)) {
//                        livenessList.add(LivenessTypeEnum.HeadLeftOrRight);
//                    }
//                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        shakeHeadCheckbox.setChecked(true);
//                        return;
//                    }
//                    shakeHeadCheckbox.setChecked(false);
//                    livenessList.remove(LivenessTypeEnum.HeadLeftOrRight);
//                }
//            }
//        });
    }

    @OnClick({
            R.id.but_setting_return,                                                                // 返回键
            R.id.blink_layout,
            R.id.actionlive_blink_checkbox,
            R.id.shake_head_layout,
            R.id.left_turn_layout,
            R.id.actionlive_left_turn_checkbox,
            R.id.right_turn_layout,
            R.id.actionlive_right_turn_checkbox,
            R.id.nod_layout,
            R.id.actionlive_nod_checkbox,
            R.id.look_up_layout,
            R.id.actionlive_look_up_checkbox,
            R.id.open_mouth_layout,
            R.id.actionlive_open_mouth_checkbox,
            R.id.quality_layout,
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_setting_return:                                                           // 返回键
                if (livenessList.size() < VALUE_MIN_ACTIVE_NUM && liveDetectSwitch.isChecked()) {
                    showMessage("至少需要选择一项活体动作");
                    return;
                }
                finish();
                break;
            case R.id.blink_layout:
                if (!blinkCheckbox.isChecked()) {
                    blinkCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.Eye)) {
                        livenessList.add(LivenessTypeEnum.Eye);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        return;
//                    }
                    blinkCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.Eye);
                }
                break;
            case R.id.actionlive_blink_checkbox:
                if (blinkCheckbox.isChecked()) {
                    blinkCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.Eye)) {
                        livenessList.add(LivenessTypeEnum.Eye);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        blinkCheckbox.setChecked(true);
//                        return;
//                    }
                    blinkCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.Eye);
                }
                break;
            case R.id.shake_head_layout:
//                if (!shakeHeadCheckbox.isChecked()) {
//                    shakeHeadCheckbox.setChecked(true);
//                    if (!livenessList.contains(LivenessTypeEnum.HeadLeftOrRight)) {
//                        livenessList.add(LivenessTypeEnum.HeadLeftOrRight);
//                    }
//                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        return;
//                    }
//                    shakeHeadCheckbox.setChecked(false);
//                    livenessList.remove(LivenessTypeEnum.HeadLeftOrRight);
//                }
                break;
            case R.id.left_turn_layout:
                if (!leftTurnCheckbox.isChecked()) {
                    leftTurnCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadLeft)) {
                        livenessList.add(LivenessTypeEnum.HeadLeft);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        return;
//                    }
                    leftTurnCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.HeadLeft);
                }
                break;
            case R.id.actionlive_left_turn_checkbox:
                if (leftTurnCheckbox.isChecked()) {
                    leftTurnCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadLeft)) {
                        livenessList.add(LivenessTypeEnum.HeadLeft);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        leftTurnCheckbox.setChecked(true);
//                        return;
//                    }
                    leftTurnCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.HeadLeft);
                }
                break;
            case R.id.right_turn_layout:
                if (!rightTurnCheckbox.isChecked()) {
                    rightTurnCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadRight)) {
                        livenessList.add(LivenessTypeEnum.HeadRight);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        return;
//                    }
                    rightTurnCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.HeadRight);
                }
                break;
            case R.id.actionlive_right_turn_checkbox:
                if (rightTurnCheckbox.isChecked()) {
                    rightTurnCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadRight)) {
                        livenessList.add(LivenessTypeEnum.HeadRight);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        rightTurnCheckbox.setChecked(true);
//                        return;
//                    }
                    rightTurnCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.HeadRight);
                }
                break;
            case R.id.nod_layout:
                if (!nodCheckbox.isChecked()) {
                    nodCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadDown)) {
                        livenessList.add(LivenessTypeEnum.HeadDown);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        return;
//                    }
                    nodCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.HeadDown);
                }
                break;
            case R.id.actionlive_nod_checkbox:
                if (nodCheckbox.isChecked()) {
                    nodCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadDown)) {
                        livenessList.add(LivenessTypeEnum.HeadDown);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        nodCheckbox.setChecked(true);
//                        return;
//                    }
                    nodCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.HeadDown);
                }
                break;
            case R.id.look_up_layout:
                if (!lookUpCheckbox.isChecked()) {
                    lookUpCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadUp)) {
                        livenessList.add(LivenessTypeEnum.HeadUp);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        return;
//                    }
                    lookUpCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.HeadUp);
                }
                break;
            case R.id.actionlive_look_up_checkbox:
                if (lookUpCheckbox.isChecked()) {
                    lookUpCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadUp)) {
                        livenessList.add(LivenessTypeEnum.HeadUp);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        lookUpCheckbox.setChecked(true);
//                        return;
//                    }
                    lookUpCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.HeadUp);
                }
                break;
            case R.id.open_mouth_layout:
                if (!openMouthCheckbox.isChecked()) {
                    openMouthCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.Mouth)) {
                        livenessList.add(LivenessTypeEnum.Mouth);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        return;
//                    }
                    openMouthCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.Mouth);
                }
                break;
            case R.id.actionlive_open_mouth_checkbox:
                if (openMouthCheckbox.isChecked()) {
                    openMouthCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.Mouth)) {
                        livenessList.add(LivenessTypeEnum.Mouth);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        openMouthCheckbox.setChecked(true);
//                        return;
//                    }
                    openMouthCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.Mouth);
                }
                break;
            case R.id.quality_layout:
                Intent intent = new Intent(this, BaiduQualityControlActivity.class);
                intent.putExtra(Const.INTENT_QUALITY_LEVEL, mTextEnterQuality.getText().toString().trim());
                startActivityForResult(intent, Const.REQUEST_QUALITY_CONTROL);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REQUEST_QUALITY_CONTROL
                && resultCode == Const.RESULT_QUALITY_CONTROL) {
            if (data != null) {
                mTextEnterQuality.setText(data.getStringExtra(Const.INTENT_QUALITY_LEVEL));
            }
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

    @Override
    public void onBackPressed() {
        if (livenessList.size() < VALUE_MIN_ACTIVE_NUM && liveDetectSwitch.isChecked()) {
            showMessage("至少需要选择一项活体动作");
            return;
        }
        super.onBackPressed();
    }

    public static class ComparatorValues implements Comparator<LivenessTypeEnum> {

        @Override
        public int compare(LivenessTypeEnum object1, LivenessTypeEnum object2) {
            int m1 = object1.ordinal();
            int m2 = object2.ordinal();
            int result = 0;
            if (m1 > m2) {
                result = 1;
            } else if (m1 < m2) {
                result = -1;
            } else {
                result = 0;
            }
            return result;
        }

    }

    /**
     * 参数配置方法
     */
    private void setFaceConfig() {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // 设置活体动作，通过设置list，LivenessTypeEunm.Eye, LivenessTypeEunm.Mouth,
        // LivenessTypeEunm.HeadUp, LivenessTypeEunm.HeadDown, LivenessTypeEunm.HeadLeft,
        // LivenessTypeEunm.HeadRight
        config.setLivenessTypeList(BaiduFaceRecognitionActivity.livenessList);
        // 设置动作活体是否随机
        config.setLivenessRandom(BaiduFaceRecognitionActivity.isLivenessRandom);
        // 设置开启提示音
        config.setSound(BaiduFaceRecognitionActivity.isOpenSound);
        FaceSDKManager.getInstance().setFaceConfig(config);
    }
}