package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.huawei.hms.mlsdk.livenessdetection.MLLivenessDetectView.DETECT_MASK;
import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huawei.hms.mlsdk.livenessdetection.MLLivenessCaptureResult;
import com.huawei.hms.mlsdk.livenessdetection.MLLivenessDetectView;
import com.huawei.hms.mlsdk.livenessdetection.OnMLLivenessDetectCallback;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.EventBusTags;
import com.zqw.mobile.grainfull.di.component.DaggerLivenessCustomDetectionComponent;
import com.zqw.mobile.grainfull.mvp.contract.LivenessCustomDetectionContract;
import com.zqw.mobile.grainfull.mvp.model.entity.MainEvent;
import com.zqw.mobile.grainfull.mvp.presenter.LivenessCustomDetectionPresenter;

import org.simple.eventbus.EventBus;

import butterknife.BindView;

/**
 * Description:活体自定义检测
 * <p>
 * Created on 2022/07/15 15:37
 *
 * @author 赤槿
 * module name is LivenessCustomDetectionActivity
 */
public class LivenessCustomDetectionActivity extends BaseActivity<LivenessCustomDetectionPresenter> implements LivenessCustomDetectionContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.surface_layout)
    FrameLayout mPreviewContainer;

    /*------------------------------------------------业务区域------------------------------------------------*/
    private MLLivenessDetectView mlLivenessDetectView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mlLivenessDetectView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mlLivenessDetectView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mlLivenessDetectView.onResume();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLivenessCustomDetectionComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_liveness_custom_detection;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("活体自定义检测");

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        // Obtain MLLivenessDetectView
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;
        mlLivenessDetectView = new MLLivenessDetectView.Builder()
                .setContext(this)
                .setOptions(DETECT_MASK)
                // set Rect of face frame relative to surface in layout
                .setFaceFrameRect(new Rect(0, 0, widthPixels, dip2px(this, 480)))
                .setDetectCallback(new OnMLLivenessDetectCallback() {
                    @Override
                    public void onCompleted(MLLivenessCaptureResult result) {
                        EventBus.getDefault().post(new MainEvent(EventBusTags.RETURN_LIVENESS_DETECTION, 1, result), EventBusTags.HOME_TAG);
                        killMyself();
                    }

                    @Override
                    public void onError(int error) {
                        EventBus.getDefault().post(new MainEvent(EventBusTags.RETURN_LIVENESS_DETECTION, 0, error), EventBusTags.HOME_TAG);
                        killMyself();
                    }

                    @Override
                    public void onInfo(int infoCode, Bundle bundle) {

                    }

                    @Override
                    public void onStateChange(int state, Bundle bundle) {

                    }
                }).build();

        mPreviewContainer.addView(mlLivenessDetectView);
        mlLivenessDetectView.onCreate(savedInstanceState);
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
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