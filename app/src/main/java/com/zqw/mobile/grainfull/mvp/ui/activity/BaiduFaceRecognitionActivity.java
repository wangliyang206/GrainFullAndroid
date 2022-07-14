package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceEnvironment;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.baidu.idl.face.platform.listener.IInitCallback;
import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.QualityConfigManager;
import com.zqw.mobile.grainfull.di.component.DaggerBaiduFaceRecognitionComponent;
import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceRecognitionContract;
import com.zqw.mobile.grainfull.mvp.model.entity.QualityConfig;
import com.zqw.mobile.grainfull.mvp.presenter.BaiduFaceRecognitionPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Description:百度AI - 人脸采集
 * <p>
 * Created on 2022/07/12 17:35
 *
 * @author 赤槿
 * module name is BaiduFaceRecognitionActivity
 */
public class BaiduFaceRecognitionActivity extends BaseActivity<BaiduFaceRecognitionPresenter> implements BaiduFaceRecognitionContract.View {
    /*------------------------------------------------------------------控件区域------------------------------------------------------------------*/
    @BindView(R.id.is_check_box)
    CheckBox isCheckBox;
    @BindView(R.id.face_agreement)
    TextView faceAgreement;
    @BindView(R.id.but_start_gather)
    Button butStartGather;
    /*------------------------------------------------------------------业务区域------------------------------------------------------------------*/
    // 动作活体条目集合
    public static List<LivenessTypeEnum> livenessList = new ArrayList<>();
    // 活体随机开关
    public static boolean isLivenessRandom = true;
    // 语音播报开关
    public static boolean isOpenSound = true;
    // 活体检测开关
    public static boolean isActionLive = true;
    // 质量等级（0：正常、1：宽松、2：严格、3：自定义）
    public static int qualityLevel = 0;
    // 保存UI对象，用于后面做释放
    private static Map<String, Activity> destroyMap = new HashMap<>();


    /**
     * 炫彩活体颜色条目集合
     */
    public static List<String> livenessColorList = new ArrayList<>();

    /**
     * 统计视频录制中出现不兼容情况的手机
     */
    public static List<String> phoneList = new ArrayList<>();
    // 是否初始化成功
    private boolean mIsInitSuccess;

    /**
     * 根据主题使用不同的颜色。
     * 如果想要纯透明，则需要重写此方法，返回值为 -1 即可。
     */
    @Override
    public int useStatusBarColor() {
        return -1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放
        FaceSDKManager.getInstance().release();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBaiduFaceRecognitionComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_baidu_face_recognition;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        addActionLive();
        addLiveColor();
        addVideoPhone();
        initLicense();
    }

    private void addActionLive() {
        // 根据需求添加活体动作
        livenessList.clear();
        livenessList.add(LivenessTypeEnum.Eye);
        livenessList.add(LivenessTypeEnum.Mouth);
        livenessList.add(LivenessTypeEnum.HeadRight);
    }

    private void addLiveColor() {
        // 根据需求添加颜色排序
        livenessColorList.clear();
        livenessColorList.add("BGR");
        livenessColorList.add("BRG");
        livenessColorList.add("GBR");
        livenessColorList.add("GRB");
        livenessColorList.add("RBG");
        livenessColorList.add("RGB");
    }

    private void addVideoPhone() {
        // 统计视频录制中出现不兼容情况的手机
        phoneList.clear();
        // OPPO R17
        phoneList.add("OPPO_PBET00");
        // 荣耀30 PRO
        phoneList.add("HONOR_EBG-AN00");
        // 联想K5 PRO
        phoneList.add("Lenovo_Lenovo L38041");
        // vivo Z6
        phoneList.add("vivo_V1963A");
        // 华为 nova5
        phoneList.add("HUAWEI_SEA-AL00");
        // OPPO K1
        phoneList.add("OPPO_PBCM30");
        // OPPO R11s
        phoneList.add("OPPO_OPPO R11s");
    }

    private void initLicense() {
        boolean success = setFaceConfig();
        if (!success) {
            showMessage("初始化失败 = json配置文件解析出错");
            return;
        }
        // 为了android和ios 区分授权，appId=appname_face_android ,其中appname为申请sdk时的应用名
        // 应用上下文
        // 申请License取得的APPID
        // assets目录下License文件名
        FaceSDKManager.getInstance().initialize(getApplicationContext(), "GrainFull-face-android",
                "idl-license.face-android", new IInitCallback() {
                    @Override
                    public void initSuccess() {
                        runOnUiThread(() -> {
                            Timber.e("初始化成功");
                            showMessage("初始化成功");
                            mIsInitSuccess = true;
                        });
                    }

                    @Override
                    public void initFailure(final int errCode, final String errMsg) {
                        runOnUiThread(() -> {
                            Timber.e("初始化失败 = " + errCode + " " + errMsg);
                            showMessage("初始化失败 = " + errCode + ", " + errMsg);
                            mIsInitSuccess = false;
                        });
                    }
                });
    }

    @OnClick({
            R.id.but_setting,                                                                       // 设置
            R.id.is_tongyi,                                                                         // 同意协议
            R.id.face_agreement,                                                                    // 查看协议
            R.id.but_start_gather,                                                                  // 开始采集
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_setting:                                                                  // 设置
                ActivityUtils.startActivity(BaiduFaceSettingActivity.class);
                break;
            case R.id.is_tongyi:                                                                    // 同意协议
                isCheckBox.setChecked(!isCheckBox.isChecked());
                break;
            case R.id.face_agreement:                                                               // 查看协议
                ActivityUtils.startActivity(BaiduFaceAgreementActivity.class);
                break;
            case R.id.but_start_gather:                                                             // 开始采集
                boolean checked = isCheckBox.isChecked();
                if (!checked) {
                    showMessage("请先同意《用户协议及详情》");
                    return;
                }
                if (!mIsInitSuccess) {
                    showMessage("初始化中，请稍候...");
                    return;
                }
                startCollect();
                break;
        }
    }

    /**
     * 开始收集
     */
    public void startCollect() {
        if (mPresenter != null) {
            mPresenter.onJump(isActionLive);
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


    /**
     * 参数配置方法
     */
    private boolean setFaceConfig() {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），也可以根据实际需求进行数值调整

        // 根据质量等级获取相应的质量值（注：第二个参数要与质量等级的set方法参数一致）
        QualityConfigManager manager = QualityConfigManager.getInstance();
        manager.readQualityFile(getApplicationContext(), qualityLevel);
        QualityConfig qualityConfig = manager.getConfig();
        if (qualityConfig == null) {
            return false;
        }
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
        // 设置可检测的最小人脸阈值
        config.setMinFaceSize(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        // 设置可检测到人脸的阈值
        config.setNotFaceValue(FaceEnvironment.VALUE_NOT_FACE_THRESHOLD);
        // 设置闭眼阈值
        config.setEyeClosedValue(FaceEnvironment.VALUE_CLOSE_EYES);
        // 设置图片缓存数量
        config.setCacheImageNum(FaceEnvironment.VALUE_CACHE_IMAGE_NUM);
        // 设置活体动作，通过设置list，LivenessTypeEunm.Eye, LivenessTypeEunm.Mouth,
        // LivenessTypeEunm.HeadUp, LivenessTypeEunm.HeadDown, LivenessTypeEunm.HeadLeft,
        // LivenessTypeEunm.HeadRight
        config.setLivenessTypeList(livenessList);
        // 设置动作活体是否随机
        config.setLivenessRandom(isLivenessRandom);
        // 设置开启提示音
        config.setSound(isOpenSound);
        // 原图缩放系数
        config.setScale(FaceEnvironment.VALUE_SCALE);
        // 抠图宽高的设定，为了保证好的抠图效果，建议高宽比是4：3
        config.setCropHeight(FaceEnvironment.VALUE_CROP_HEIGHT);
        config.setCropWidth(FaceEnvironment.VALUE_CROP_WIDTH);
        // 抠图人脸框与背景比例
        config.setEnlargeRatio(FaceEnvironment.VALUE_CROP_ENLARGERATIO);
        // 检测超时设置
        config.setTimeDetectModule(FaceEnvironment.TIME_DETECT_MODULE);
        // 检测框远近比率
        config.setFaceFarRatio(FaceEnvironment.VALUE_FAR_RATIO);
        config.setFaceClosedRatio(FaceEnvironment.VALUE_CLOSED_RATIO);
//        // 是否开启动作活体
//        config.setOpenActionLive(isActionLive);
//        // 设置动作活体颜色类型列表
//        config.setLivenessColorTypeList(livenessColorList);
//        // 设置活体阈值
//        config.setLivenessValue(FaceEnvironment.VALUE_LIVENESS_SCORE);
//        // 设置炫彩活体颜色分数阈值
//        config.setLivenessColorValue(FaceEnvironment.VALUE_LIVENESS_COLOR_SCORE);
//        // 设置视频录制时间
//        config.setRecordVideoTime(FaceEnvironment.TIME_RECORD_VIDEO);
//        // 视频录制中出现分辨率改变的手机或其它不兼容的手机统计
//        config.setPhoneList(phoneList);
        FaceSDKManager.getInstance().setFaceConfig(config);
        return true;
    }

    /**
     * 添加到销毁队列
     *
     * @param activity 要销毁的activity
     */
    public static void addDestroyActivity(Activity activity, String activityName) {
        destroyMap.put(activityName, activity);
    }

    /**
     * 销毁指定Activity
     */
    public static void destroyActivity(String activityName) {
        Set<String> keySet = destroyMap.keySet();
        for (String key : keySet) {
            destroyMap.get(key).finish();
        }
    }
}