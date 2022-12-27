package com.zqw.mobile.grainfull.mvp.ui.activity;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.ui.model.Const;
import com.baidu.idl.face.platform.ui.utils.NumberUtils;
import com.baidu.idl.face.platform.ui.widget.AmountView;
import com.baidu.idl.face.platform.ui.widget.SelectDialog;
import com.baidu.idl.face.platform.utils.FileUtils;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.utils.ArmsUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import com.zqw.mobile.grainfull.di.component.DaggerBaiduQualityParamsComponent;
import com.zqw.mobile.grainfull.mvp.contract.BaiduQualityParamsContract;
import com.zqw.mobile.grainfull.mvp.presenter.BaiduQualityParamsPresenter;
import com.zqw.mobile.grainfull.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:质量参数配置页面
 * <p>
 * Created on 2022/07/13 19:30
 *
 * @author 赤槿
 * module name is BaiduQualityParamsActivity
 */
public class BaiduQualityParamsActivity extends BaseActivity<BaiduQualityParamsPresenter> implements BaiduQualityParamsContract.View, SelectDialog.OnSelectDialogClickListener {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.scrollView)
    ScrollView mScrollView;
    @BindView(R.id.relative_default)
    RelativeLayout mRelativeDefault;
    @BindView(R.id.text_default)
    TextView mTextDefault;
    @BindView(R.id.view_bg)
    View mViewBg;

    @BindView(R.id.text_params_title)
    TextView mTextTitle;
    @BindView(R.id.text_save)
    TextView mTextSave;

    @BindView(R.id.amount_min_illum)
    AmountView mAmountMinIllum;   // 最小光照值
    @BindView(R.id.amount_max_illum)
    AmountView mAmountMaxIllum;   // 最大光照值
    @BindView(R.id.amount_blur)
    AmountView mAmountBlur;       // 模糊度

    @BindView(R.id.amount_left_eye)
    AmountView mAmountLeftEye;    // 左眼
    @BindView(R.id.amount_right_eye)
    AmountView mAmountRightEye;   // 右眼
    @BindView(R.id.amount_nose)
    AmountView mAmountNose;       // 鼻子
    @BindView(R.id.amount_mouth)
    AmountView mAmountMouth;      // 嘴巴
    @BindView(R.id.amount_left_cheek)
    AmountView mAmountLeftCheek;  // 左脸颊
    @BindView(R.id.amount_right_cheek)
    AmountView mAmountRightCheek; // 右脸颊
    @BindView(R.id.amount_chin)
    AmountView mAmountChin;       // 下巴

    @BindView(R.id.amount_pitch)
    AmountView mAmountPitch;      // 俯仰角
    @BindView(R.id.amount_yaw)
    AmountView mAmountYaw;        // 左右角
    @BindView(R.id.amount_roll)
    AmountView mAmountRoll;       // 旋转角

    /*------------------------------------------------业务区域------------------------------------------------*/
    SelectDialog mSelectDialog;
    private FaceConfig mConfig;

    private float mMinIllum;
    private float mMaxIllum;
    private float mBlur;

    private float mLeftEye;
    private float mRightEye;
    private float mNose;
    private float mMouth;
    private float mLeftCheek;
    private float mRightCheek;
    private float mChin;

    private int mPitch;
    private int mYaw;
    private int mRoll;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBaiduQualityParamsComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_baidu_quality_params;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra(Const.INTENT_QUALITY_TITLE);
            mTextTitle.setText(title);
            if (getResources().getString(R.string.setting_quality_custom_params_txt).equals(title)) {
                mRelativeDefault.setVisibility(View.GONE);
            } else {
                mRelativeDefault.setVisibility(View.VISIBLE);
            }
        }

        mSelectDialog = new SelectDialog(getApplicationContext());
        mSelectDialog.setDialogListener(this);
        mSelectDialog.setCanceledOnTouchOutside(false);
        mSelectDialog.setCancelable(false);
    }

    private void initData() {
        mConfig = FaceSDKManager.getInstance().getFaceConfig();

        // minIllum
        mAmountMinIllum.setAmount(mConfig.getBrightnessValue());
        mAmountMinIllum.setMinNum(20);
        mAmountMinIllum.setMaxNum(60);
        mAmountMinIllum.setInterval(1);
        mAmountMinIllum.setQuality(AmountView.QUALITY_ILLUM);
        mAmountMinIllum.setOnAmountChangeListener((view, amount) -> {
            mMinIllum = Float.valueOf(amount);
            modifyViewColor();
        });

        // maxIllum
        mAmountMaxIllum.setAmount(mConfig.getBrightnessMaxValue());
        mAmountMaxIllum.setMinNum(200);
        mAmountMaxIllum.setMaxNum(240);
        mAmountMaxIllum.setInterval(1);
        mAmountMaxIllum.setQuality(AmountView.QUALITY_ILLUM);
        mAmountMaxIllum.setOnAmountChangeListener((view, amount) -> {
            mMaxIllum = Float.valueOf(amount);
            modifyViewColor();
        });

        // blur
        mAmountBlur.setAmount(mConfig.getBlurnessValue());
        mAmountBlur.setMinNum(0.1f);
        mAmountBlur.setMaxNum(0.9f);
        mAmountBlur.setInterval(0.05f);
        mAmountBlur.setQuality(AmountView.QUALITY_BLUR);
        mAmountBlur.setOnAmountChangeListener((view, amount) -> {
            mBlur = Float.valueOf(amount);
            modifyViewColor();
        });

        // left_eye
        mAmountLeftEye.setAmount(mConfig.getOcclusionLeftEyeValue());
        mAmountLeftEye.setMinNum(0.3f);
        mAmountLeftEye.setMaxNum(1.0f);
        mAmountLeftEye.setInterval(0.05f);
        mAmountLeftEye.setQuality(AmountView.QUALITY_OCCLU);
        mAmountLeftEye.setOnAmountChangeListener((view, amount) -> {
            mLeftEye = Float.valueOf(amount);
            modifyViewColor();
        });

        // right_eye
        mAmountRightEye.setAmount(mConfig.getOcclusionRightEyeValue());
        mAmountRightEye.setMinNum(0.3f);
        mAmountRightEye.setMaxNum(1.0f);
        mAmountRightEye.setInterval(0.05f);
        mAmountRightEye.setQuality(AmountView.QUALITY_OCCLU);
        mAmountRightEye.setOnAmountChangeListener((view, amount) -> {
            mRightEye = Float.valueOf(amount);
            modifyViewColor();
        });

        // nose
        mAmountNose.setAmount(mConfig.getOcclusionNoseValue());
        mAmountNose.setMinNum(0.3f);
        mAmountNose.setMaxNum(1.0f);
        mAmountNose.setInterval(0.05f);
        mAmountNose.setQuality(AmountView.QUALITY_OCCLU);
        mAmountNose.setOnAmountChangeListener((view, amount) -> {
            mNose = Float.valueOf(amount);
            modifyViewColor();
        });

        // mouth
        mAmountMouth.setAmount(mConfig.getOcclusionMouthValue());
        mAmountMouth.setMinNum(0.3f);
        mAmountMouth.setMaxNum(1.0f);
        mAmountMouth.setInterval(0.05f);
        mAmountMouth.setQuality(AmountView.QUALITY_OCCLU);
        mAmountMouth.setOnAmountChangeListener((view, amount) -> {
            mMouth = Float.valueOf(amount);
            modifyViewColor();
        });

        // left_cheek
        mAmountLeftCheek.setAmount(mConfig.getOcclusionLeftContourValue());
        mAmountLeftCheek.setMinNum(0.3f);
        mAmountLeftCheek.setMaxNum(1.0f);
        mAmountLeftCheek.setInterval(0.05f);
        mAmountLeftCheek.setQuality(AmountView.QUALITY_OCCLU);
        mAmountLeftCheek.setOnAmountChangeListener((view, amount) -> {
            mLeftCheek = Float.valueOf(amount);
            modifyViewColor();
        });

        // right_cheek
        mAmountRightCheek.setAmount(mConfig.getOcclusionRightContourValue());
        mAmountRightCheek.setMinNum(0.3f);
        mAmountRightCheek.setMaxNum(1.0f);
        mAmountRightCheek.setInterval(0.05f);
        mAmountRightCheek.setQuality(AmountView.QUALITY_OCCLU);
        mAmountRightCheek.setOnAmountChangeListener((view, amount) -> {
            mRightCheek = Float.valueOf(amount);
            modifyViewColor();
        });

        // chin
        mAmountChin.setAmount(mConfig.getOcclusionChinValue());
        mAmountChin.setMinNum(0.3f);
        mAmountChin.setMaxNum(1.0f);
        mAmountChin.setInterval(0.05f);
        mAmountChin.setQuality(AmountView.QUALITY_OCCLU);
        mAmountChin.setOnAmountChangeListener((view, amount) -> {
            mChin = Float.valueOf(amount);
            modifyViewColor();
        });

        // pitch
        mAmountPitch.setAmount(mConfig.getHeadPitchValue());
        mAmountPitch.setMinNum(10);
        mAmountPitch.setMaxNum(50);
        mAmountPitch.setInterval(1);
        mAmountPitch.setQuality(AmountView.QUALITY_HEADPOSE);
        mAmountPitch.setOnAmountChangeListener((view, amount) -> {
            mPitch = Integer.valueOf(amount);
            modifyViewColor();
        });

        // yaw
        mAmountYaw.setAmount(mConfig.getHeadYawValue());
        mAmountYaw.setMinNum(10);
        mAmountYaw.setMaxNum(50);
        mAmountYaw.setInterval(1);
        mAmountYaw.setQuality(AmountView.QUALITY_HEADPOSE);
        mAmountYaw.setOnAmountChangeListener((view, amount) -> {
            mYaw = Integer.valueOf(amount);
            modifyViewColor();
        });

        // roll
        mAmountRoll.setAmount(mConfig.getHeadRollValue());
        mAmountRoll.setMinNum(10);
        mAmountRoll.setMaxNum(50);
        mAmountRoll.setInterval(1);
        mAmountRoll.setQuality(AmountView.QUALITY_HEADPOSE);
        mAmountRoll.setOnAmountChangeListener((view, amount) -> {
            mRoll = Integer.valueOf(amount);
            modifyViewColor();
        });
    }

    @OnClick({
            R.id.text_default,
            R.id.btn_quality_param_return,
            R.id.text_save,
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_default:
                // 如果参数未改动
                if (!judgeIsModified()) {
                    return;
                } else {
                    // 恢复默认配置
                    String title = mTextTitle.getText().toString();
                    int id = 0;
                    if (title.contains("宽松")) {
                        id = R.string.dialog_is_low_default;
                    } else if (title.contains("正常")) {
                        id = R.string.dialog_is_normal_default;
                    } else if (title.contains("严格")) {
                        id = R.string.dialog_is_high_default;
                    }
                    showMessageDialog(id, R.string.dialog_tips2, R.string.dialog_button_cancle,
                            R.string.dialog_button_default, Const.DIALOG_RESET_DEFAULT);
                }
                break;
            case R.id.btn_quality_param_return:
                // 如果参数未改动
                if (!judgeIsModified()) {
                    finish();
                    return;
                }
                if ("自定义".equals(mTextTitle.getText().toString())) {
                    showMessageDialog(R.string.dialog_is_save_modify,
                            R.string.dialog_tips3, R.string.dialog_button_exit,
                            R.string.dialog_button_save, Const.DIALOG_SAVE_CUSTOM_MODIFY);
                } else {
                    showMessageDialog(R.string.dialog_is_save_custom,
                            R.string.dialog_tips1, R.string.dialog_button_exit,
                            R.string.dialog_button_save_custom, Const.DIALOG_SAVE_RETURN_BUTTON);
                }
                break;
            case R.id.text_save:
                // 如果参数未改动
                if (!judgeIsModified()) {
                    return;
                } else {
                    showMessageDialog(R.string.dialog_is_save_custom,
                            R.string.dialog_tips1, R.string.dialog_button_cancle,
                            R.string.dialog_button_save_custom, Const.DIALOG_SAVE_SAVE_BUTTON);
                }
                break;
        }
    }

    /**
     * 判断是否修改参数值
     * @return
     */
    private boolean judgeIsModified() {
        if (mMinIllum == mConfig.getBrightnessValue() && mMaxIllum == mConfig.getBrightnessMaxValue()
                && mBlur == mConfig.getBlurnessValue() && mLeftEye == mConfig.getOcclusionLeftEyeValue()
                && mRightEye == mConfig.getOcclusionRightEyeValue() && mNose == mConfig.getOcclusionNoseValue()
                && mMouth == mConfig.getOcclusionMouthValue() && mLeftCheek == mConfig.getOcclusionLeftContourValue()
                && mRightCheek == mConfig.getOcclusionRightContourValue() && mChin == mConfig.getOcclusionChinValue()
                && mPitch == mConfig.getHeadPitchValue() && mYaw == mConfig.getHeadYawValue()
                && mRoll == mConfig.getHeadRollValue()) {
            return false;
        } else {
            return true;
        }
    }

    private void showMessageDialog(int titleId, int tipsId, int cancelId, int confirmId, int dialogType) {
        if (mViewBg != null) {
            mViewBg.setVisibility(View.VISIBLE);
        }
        mSelectDialog.show();
        mSelectDialog.setDialogTitle(titleId);
        mSelectDialog.setDialogTips(tipsId);
        mSelectDialog.setDialogBtnCancel(cancelId);
        mSelectDialog.setDialogBtnConfirm(confirmId);
        mSelectDialog.setDialogType(dialogType);
    }

    public void dismissDialog() {
        if (mSelectDialog != null) {
            mSelectDialog.dismiss();
        }
        if (mViewBg != null) {
            mViewBg.setVisibility(View.GONE);
        }
    }

    /**
     * 修改控件颜色
     */
    private void modifyViewColor() {
        if (mTextSave == null || mTextDefault == null) {
            return;
        }

        if (!judgeIsModified()) {
            mTextSave.setTextColor(getResources().getColor(R.color.setting_quality_grey));
            mTextDefault.setTextColor(getResources().getColor(R.color.setting_quality_grey));
        } else {
            mTextSave.setTextColor(getResources().getColor(R.color.setting_quality_blue));
            mTextDefault.setTextColor(getResources().getColor(R.color.setting_quality_blue));
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
    public void onConfirm(int dialogType) {
        switch (dialogType) {
            case Const.DIALOG_SAVE_SAVE_BUTTON:
            case Const.DIALOG_SAVE_RETURN_BUTTON:
            case Const.DIALOG_SAVE_CUSTOM_MODIFY:
                createJson();
                Intent intent = getIntent();
                intent.putExtra(Const.INTENT_QUALITY_LEVEL_PARAMS, Const.QUALITY_CUSTOM);
                setResult(Const.RESULT_QUALITY_PARAMS, intent);
                finish();
                break;

            case Const.DIALOG_RESET_DEFAULT:
                dismissDialog();
                resetDefaultParams();
                mScrollView.scrollTo(0, 0);
                showToast();
                break;
        }
    }

    /**
     * 创建自定义配置的json数据
     */
    private void createJson() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("minIllum", mMinIllum);
            jsonObject.put("maxIllum", mMaxIllum);
            jsonObject.put("leftEyeOcclusion", NumberUtils.floatToDouble(mLeftEye));
            jsonObject.put("rightEyeOcclusion", NumberUtils.floatToDouble(mRightEye));
            jsonObject.put("noseOcclusion", NumberUtils.floatToDouble(mNose));
            jsonObject.put("mouseOcclusion", NumberUtils.floatToDouble(mMouth));
            jsonObject.put("leftContourOcclusion", NumberUtils.floatToDouble(mLeftCheek));
            jsonObject.put("rightContourOcclusion", NumberUtils.floatToDouble(mRightCheek));
            jsonObject.put("chinOcclusion", NumberUtils.floatToDouble(mChin));
            jsonObject.put("pitch", mPitch);
            jsonObject.put("yaw", mYaw);
            jsonObject.put("roll", mRoll);
            jsonObject.put("blur", NumberUtils.floatToDouble(mBlur));
            // 保存文件 /data/data/packageName/
            FileUtils.writeToFile(new File(getFilesDir() + "/" + "custom_quality.txt"),
                    jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 恢复默认配置
     */
    private void resetDefaultParams() {
        // minIllum
        mAmountMinIllum.setAmount(mConfig.getBrightnessValue());
        // maxIllum
        mAmountMaxIllum.setAmount(mConfig.getBrightnessMaxValue());
        // blur
        mAmountBlur.setAmount(mConfig.getBlurnessValue());
        // left_eye
        mAmountLeftEye.setAmount(mConfig.getOcclusionLeftEyeValue());
        // right_eye
        mAmountRightEye.setAmount(mConfig.getOcclusionRightEyeValue());
        // nose
        mAmountNose.setAmount(mConfig.getOcclusionNoseValue());
        // mouth
        mAmountMouth.setAmount(mConfig.getOcclusionMouthValue());
        // left_cheek
        mAmountLeftCheek.setAmount(mConfig.getOcclusionLeftContourValue());
        // right_cheek
        mAmountRightCheek.setAmount(mConfig.getOcclusionRightContourValue());
        // chin
        mAmountChin.setAmount(mConfig.getOcclusionChinValue());
        // pitch
        mAmountPitch.setAmount(mConfig.getHeadPitchValue());
        // yaw
        mAmountYaw.setAmount(mConfig.getHeadYawValue());
        // roll
        mAmountRoll.setAmount(mConfig.getHeadRollValue());
    }

    public void showToast() {
        String title = mTextTitle.getText().toString();
        if (title.contains("宽松")) {
            showMessage("已恢复为宽松默认参数");
        } else if (title.contains("正常")) {
            showMessage("已恢复为正常默认参数");
        } else if (title.contains("严格")) {
            showMessage("已恢复为严格默认参数");
        }
    }

    @Override
    public void onReturn(int dialogType) {
        switch (dialogType) {
            case Const.DIALOG_SAVE_SAVE_BUTTON:
            case Const.DIALOG_RESET_DEFAULT:
                dismissDialog();
                break;

            case Const.DIALOG_SAVE_RETURN_BUTTON:
            case Const.DIALOG_SAVE_CUSTOM_MODIFY:
                finish();
                break;
        }
    }
}