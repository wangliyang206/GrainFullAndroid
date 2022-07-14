package com.zqw.mobile.grainfull.mvp.ui.activity;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.ui.model.Const;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.utils.ArmsUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import com.zqw.mobile.grainfull.app.utils.QualityConfigManager;
import com.zqw.mobile.grainfull.di.component.DaggerBaiduQualityControlComponent;
import com.zqw.mobile.grainfull.mvp.contract.BaiduQualityControlContract;
import com.zqw.mobile.grainfull.mvp.model.entity.QualityConfig;
import com.zqw.mobile.grainfull.mvp.presenter.BaiduQualityControlPresenter;
import com.zqw.mobile.grainfull.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:质量控制设置
 * <p>
 * Created on 2022/07/13 19:13
 *
 * @author 赤槿
 * module name is BaiduQualityControlActivity
 */
public class BaiduQualityControlActivity extends BaseActivity<BaiduQualityControlPresenter> implements BaiduQualityControlContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/

    @BindView(R.id.relative_low)
    RelativeLayout mRelativeLow;
    @BindView(R.id.relative_normal)
    RelativeLayout mRelativeNormal;
    @BindView(R.id.relative_high)
    RelativeLayout mRelativeHigh;
    @BindView(R.id.relative_custom)
    RelativeLayout mRelativeCustom;

    @BindView(R.id.radio_low)
    RadioButton mRadioLow;
    @BindView(R.id.radio_normal)
    RadioButton mRadioNormal;
    @BindView(R.id.radio_high)
    RadioButton mRadioHigh;
    @BindView(R.id.radio_custom)
    RadioButton mRadioCustom;

    @BindView(R.id.text_low_enter)
    TextView mTextLowEnter;
    @BindView(R.id.text_normal_enter)
    TextView mTextNormalEnter;
    @BindView(R.id.text_high_enter)
    TextView mTextHighEnter;
    @BindView(R.id.text_custom_enter)
    TextView mTextCustomEnter;

    /*------------------------------------------------业务区域------------------------------------------------*/
    String mSelectQuality;
    private int mIntentCount = 0;

    @Override
    protected void onStop() {
        super.onStop();
        mIntentCount = 0;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBaiduQualityControlComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_baidu_quality_control;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({
            R.id.but_quality_return,
            R.id.relative_low,
            R.id.relative_normal,
            R.id.relative_high,
            R.id.relative_custom,
            R.id.text_low_enter,
            R.id.text_normal_enter,
            R.id.text_high_enter,
            R.id.text_custom_enter,

    })
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.but_quality_return:
                Intent intent = getIntent();
                intent.putExtra(Const.INTENT_QUALITY_LEVEL, mSelectQuality);
                setResult(Const.RESULT_QUALITY_CONTROL, intent);
                finish();
                break;
            case R.id.relative_low:       // 点击宽松
                disableOthers(R.id.radio_low);
                handleSelectStatus(Const.QUALITY_LOW);
                break;

            case R.id.relative_normal:    // 点击正常
                disableOthers(R.id.radio_normal);
                handleSelectStatus(Const.QUALITY_NORMAL);
                break;

            case R.id.relative_high:      // 点击严格
                disableOthers(R.id.radio_high);
                handleSelectStatus(Const.QUALITY_HIGH);
                break;

            case R.id.relative_custom:    // 点击自定义
                disableOthers(R.id.radio_custom);
                handleSelectStatus(Const.QUALITY_CUSTOM);
                startIntent(R.string.setting_quality_custom_params_txt);
                break;

            case R.id.text_low_enter:       // 点击宽松跳转
                startIntent(R.string.setting_quality_low_params_txt);
                break;

            case R.id.text_normal_enter:    // 点击正常跳转
                startIntent(R.string.setting_quality_normal_params_txt);
                break;

            case R.id.text_high_enter:      // 点击严格跳转
                startIntent(R.string.setting_quality_high_params_txt);
                break;

            case R.id.text_custom_enter:    // 点击自定义跳转
                startIntent(R.string.setting_quality_custom_params_txt);
                break;
        }
    }

    /**
     * 单选时，其它状态置成false
     * @param viewId
     */
    private void disableOthers(int viewId) {
        if (R.id.radio_low != viewId && mRadioLow.isChecked()) {
            mRadioLow.setChecked(false);
            mTextLowEnter.setVisibility(View.GONE);
        }

        if (R.id.radio_normal != viewId && mRadioNormal.isChecked()) {
            mRadioNormal.setChecked(false);
            mTextNormalEnter.setVisibility(View.GONE);
        }

        if (R.id.radio_high != viewId && mRadioHigh.isChecked()) {
            mRadioHigh.setChecked(false);
            mTextHighEnter.setVisibility(View.GONE);
        }

        if (R.id.radio_custom != viewId && mRadioCustom.isChecked()) {
            mRadioCustom.setChecked(false);
            mTextCustomEnter.setVisibility(View.GONE);
        }
    }

    private void handleSelectStatus(int qualityLevel) {
        if (qualityLevel == Const.QUALITY_NORMAL) {
            mRadioNormal.setChecked(true);
            mSelectQuality = mRadioNormal.getText().toString();
            mTextNormalEnter.setVisibility(View.VISIBLE);
            setFaceConfig(Const.QUALITY_NORMAL);
//            mSharedPreferencesUtil.put(Const.KEY_QUALITY_LEVEL_SAVE, Const.QUALITY_NORMAL);
        } else if (qualityLevel == Const.QUALITY_LOW) {
            mRadioLow.setChecked(true);
            mSelectQuality = mRadioLow.getText().toString();
            mTextLowEnter.setVisibility(View.VISIBLE);
            setFaceConfig(Const.QUALITY_LOW);
//            mSharedPreferencesUtil.put(Const.KEY_QUALITY_LEVEL_SAVE, Const.QUALITY_LOW);
        } else if (qualityLevel == Const.QUALITY_HIGH) {
            mRadioHigh.setChecked(true);
            mSelectQuality = mRadioHigh.getText().toString();
            mTextHighEnter.setVisibility(View.VISIBLE);
            setFaceConfig(Const.QUALITY_HIGH);
//            mSharedPreferencesUtil.put(Const.KEY_QUALITY_LEVEL_SAVE, Const.QUALITY_HIGH);
        } else if (qualityLevel == Const.QUALITY_CUSTOM) {
            mRadioCustom.setChecked(true);
            mSelectQuality = mRadioCustom.getText().toString();
            mTextCustomEnter.setVisibility(View.VISIBLE);
            setFaceConfig(Const.QUALITY_CUSTOM);
//            mSharedPreferencesUtil.put(Const.KEY_QUALITY_LEVEL_SAVE, Const.QUALITY_CUSTOM);
        }
    }

    /**
     * 参数配置方法
     */
    private void setFaceConfig(int qualityLevel) {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        config.setQualityLevel(qualityLevel);
        // 根据质量等级获取相应的质量值（注：第二个参数要与质量等级的set方法参数一致）
        QualityConfigManager manager = QualityConfigManager.getInstance();
        manager.readQualityFile(getApplicationContext(), qualityLevel);
        QualityConfig qualityConfig = manager.getConfig();
        // 设置模糊度阈值
        config.setBlurnessValue(qualityConfig.getBlur());
        // 设置最小光照阈值（范围0-255）
        config.setBrightnessValue(qualityConfig.getMinIllum());
        // 设置最大光照阈值（范围0-255）
        config.setBrightnessMaxValue(qualityConfig.getMaxIllum());
        // 设置左眼遮挡阈值
        config.setOcclusionLeftEyeValue(qualityConfig.getLeftEyeOcclusion());
        // 设置右眼遮挡阈值
        config.setOcclusionRightEyeValue(qualityConfig.getRightEyeOcclusion());
        // 设置鼻子遮挡阈值
        config.setOcclusionNoseValue(qualityConfig.getNoseOcclusion());
        // 设置嘴巴遮挡阈值
        config.setOcclusionMouthValue(qualityConfig.getMouseOcclusion());
        // 设置左脸颊遮挡阈值
        config.setOcclusionLeftContourValue(qualityConfig.getLeftContourOcclusion());
        // 设置右脸颊遮挡阈值
        config.setOcclusionRightContourValue(qualityConfig.getRightContourOcclusion());
        // 设置下巴遮挡阈值
        config.setOcclusionChinValue(qualityConfig.getChinOcclusion());
        // 设置人脸姿态角阈值
        config.setHeadPitchValue(qualityConfig.getPitch());
        config.setHeadYawValue(qualityConfig.getYaw());
        config.setHeadRollValue(qualityConfig.getRoll());
        FaceSDKManager.getInstance().setFaceConfig(config);
    }

    /**
     * 跳转至参数配置页面
     */
    private void startIntent(int qualityId) {
        if (mIntentCount >= 1) {
            return;
        }
        mIntentCount++;
        Intent intent = new Intent(this, BaiduQualityParamsActivity.class);
        intent.putExtra(Const.INTENT_QUALITY_TITLE, getResources().getString(qualityId));
        startActivityForResult(intent, Const.REQUEST_QUALITY_PARAMS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REQUEST_QUALITY_PARAMS && resultCode == Const.RESULT_QUALITY_PARAMS) {
            if (data != null) {
                int qualityLevel = data.getIntExtra(Const.INTENT_QUALITY_LEVEL_PARAMS, Const.QUALITY_NORMAL);
                if (qualityLevel == Const.QUALITY_LOW) {
                    disableOthers(R.id.radio_low);
                } else if (qualityLevel == Const.QUALITY_NORMAL) {
                    disableOthers(R.id.radio_normal);
                } else if (qualityLevel == Const.QUALITY_HIGH) {
                    disableOthers(R.id.radio_high);
                } else if (qualityLevel == Const.QUALITY_CUSTOM) {
                    disableOthers(R.id.radio_custom);
                }
                handleSelectStatus(qualityLevel);
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
}