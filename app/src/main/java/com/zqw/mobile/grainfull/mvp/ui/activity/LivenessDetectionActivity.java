package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.huawei.hms.mlsdk.livenessdetection.MLLivenessDetectView.DETECT_MASK;
import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huawei.hms.mlsdk.livenessdetection.MLLivenessCapture;
import com.huawei.hms.mlsdk.livenessdetection.MLLivenessCaptureConfig;
import com.huawei.hms.mlsdk.livenessdetection.MLLivenessCaptureResult;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.EventBusTags;
import com.zqw.mobile.grainfull.di.component.DaggerLivenessDetectionComponent;
import com.zqw.mobile.grainfull.mvp.contract.LivenessDetectionContract;
import com.zqw.mobile.grainfull.mvp.model.entity.MainEvent;
import com.zqw.mobile.grainfull.mvp.presenter.LivenessDetectionPresenter;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:活体检测
 * <p>
 * Created on 2022/07/13 15:29
 *
 * @author 赤槿
 * module name is LivenessDetectionActivity
 */
public class LivenessDetectionActivity extends BaseActivity<LivenessDetectionPresenter> implements LivenessDetectionContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.image_livenessdetection_result)
    ImageView mImageResult;                                                                         // 图片结果

    @BindView(R.id.txvi_livenessdetection_live)
    TextView mTextLive;                                                                             // 是否活体

    @BindView(R.id.txvi_livenessdetection_score)
    TextView mTextScore;                                                                            // 活体的量化分数

    @BindView(R.id.txvi_livenessdetection_yaw)
    TextView mTextYaw;                                                                              // 人脸左右旋转角度

    @BindView(R.id.txvi_livenessdetection_pitch)
    TextView mTextPitch;                                                                            // 人脸俯仰角度

    @BindView(R.id.txvi_livenessdetection_result)
    TextView mTextResult;                                                                           // 文字结果

    /*------------------------------------------------业务区域------------------------------------------------*/

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLivenessDetectionComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_liveness_detection;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("活体检测");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "liveness_detection_open");
    }

    /**
     * 自定义活体检测返回事件    回调
     */
    @Subscriber(tag = EventBusTags.HOME_TAG, mode = ThreadMode.POST)
    private void eventBusEvent(MainEvent mainEvent) {
        // 切换区域
        if (mainEvent.getCode() == EventBusTags.RETURN_LIVENESS_DETECTION) {
            if (mainEvent.getType() == 1) {
                customCallback.onSuccess(mainEvent.getResult());
            } else {
                customCallback.onFailure(mainEvent.getPosition());
            }

        }
    }

    @OnClick({
            R.id.btn_livenessdetection_default,                                                     // 默认视图
            R.id.btn_livenessdetection_customize,                                                   // 自定义视图
    })
    @Override
    public void onClick(View v) {
        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "liveness_detection");

        switch (v.getId()) {
            case R.id.btn_livenessdetection_default:                                                // 默认视图
                startCaptureActivity();
                break;
            case R.id.btn_livenessdetection_customize:                                              // 自定义视图
                startCustomActivity();
                break;
        }
    }

    /**
     * 默认视图
     */
    private void startCaptureActivity() {
        //Obtain liveness detection config and set detect mask and sunglasses
        MLLivenessCaptureConfig captureConfig = new MLLivenessCaptureConfig.Builder().setOptions(DETECT_MASK).build();
        // Obtains the liveness detection plug-in instance.
        MLLivenessCapture capture = MLLivenessCapture.getInstance();
        //set liveness detection config
        capture.setConfig(captureConfig);

        // Enable liveness detection.
        capture.startDetect(this, callback);
    }


    /**
     * 自定义视图
     */
    private void startCustomActivity() {
        Intent intent = new Intent(this, LivenessCustomDetectionActivity.class);
        this.startActivity(intent);
    }

    //Callback for receiving the liveness detection result.
    private final MLLivenessCapture.Callback callback = new MLLivenessCapture.Callback() {
        /**
         * Liveness detection success callback.
         * @param result result
         */
        @Override
        public void onSuccess(MLLivenessCaptureResult result) {

            // true：活体。
            // false：非活体。
            mTextLive.setText("是否活体：" + result.isLive());

            // 活体的置信度，量化分数为0.0、10.0、50.0、90.0四个分级。
            mTextScore.setText("活体的量化分数：" + result.getScore());

            // 正值表示人脸转向图像的右侧。
            // 负值表示人脸转向图像的左侧。
            mTextYaw.setText("人脸左右旋转角度：" + result.getYaw());

            // 正值表示人脸低头角度。
            // 负值表示人脸仰头角度。
            mTextPitch.setText("人脸俯仰角度：" + result.getPitch());

            // 正值表示人脸在图像竖直平面顺时针旋转。
            // 负值表示人脸在图像竖直平面逆时针旋转。
            mTextResult.setText("人脸在竖直平面的旋转角度：" + result.getRoll());

            mImageResult.setImageBitmap(result.getBitmap());
        }

        @Override
        public void onFailure(int errorCode) {
            mTextResult.setText("errorCode:" + errorCode);
        }
    };

    private final MLLivenessCapture.Callback customCallback = new MLLivenessCapture.Callback() {
        /**
         * Liveness detection success callback.
         * @param result result
         */
        @Override
        public void onSuccess(MLLivenessCaptureResult result) {

            // true：活体。
            // false：非活体。
            mTextLive.setText("是否活体：" + result.isLive());

            // 活体的置信度，量化分数为0.0、10.0、50.0、90.0四个分级。
            mTextScore.setText("活体的量化分数：" + result.getScore());

            // 正值表示人脸转向图像的右侧。
            // 负值表示人脸转向图像的左侧。
            mTextYaw.setText("人脸左右旋转角度：" + result.getYaw());

            // 正值表示人脸低头角度。
            // 负值表示人脸仰头角度。
            mTextPitch.setText("人脸俯仰角度：" + result.getPitch());

            // 正值表示人脸在图像竖直平面顺时针旋转。
            // 负值表示人脸在图像竖直平面逆时针旋转。
            mTextResult.setText("人脸在竖直平面的旋转角度：" + result.getRoll());

            mImageResult.setImageBitmap(result.getBitmap());
        }

        @Override
        public void onFailure(int errorCode) {
            mTextResult.setText("errorCode:" + errorCode);
        }
    };

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