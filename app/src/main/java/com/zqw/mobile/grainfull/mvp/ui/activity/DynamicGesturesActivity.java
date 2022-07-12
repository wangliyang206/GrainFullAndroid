package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huawei.hms.mlsdk.common.LensEngine;
import com.huawei.hms.mlsdk.common.MLAnalyzer;
import com.huawei.hms.mlsdk.gesture.MLGesture;
import com.huawei.hms.mlsdk.gesture.MLGestureAnalyzer;
import com.huawei.hms.mlsdk.gesture.MLGestureAnalyzerFactory;
import com.huawei.hms.mlsdk.gesture.MLGestureAnalyzerSetting;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerDynamicGesturesComponent;
import com.zqw.mobile.grainfull.mvp.contract.DynamicGesturesContract;
import com.zqw.mobile.grainfull.mvp.presenter.DynamicGesturesPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.camera.GraphicOverlay;
import com.zqw.mobile.grainfull.mvp.ui.widget.camera.LensEnginePreview;
import com.zqw.mobile.grainfull.mvp.ui.widget.handgesture.HandGestureGraphic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Description:动态手势识别
 * <p>
 * Created on 2022/07/12 12:30
 *
 * @author 赤槿
 * module name is DynamicGesturesActivity
 */
public class DynamicGesturesActivity extends BaseActivity<DynamicGesturesPresenter> implements DynamicGesturesContract.View {
    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.lepr_dynamicgestures_preview)
    LensEnginePreview mPreview;

    @BindView(R.id.grov_dynamicgestures_overlay)
    GraphicOverlay mOverlay;

    /*------------------------------------------业务信息------------------------------------------*/
    // 镜头类型
    private int lensType = LensEngine.BACK_LENS;
    // 手势分析器
    private MLGestureAnalyzer mAnalyzer;
    // 镜头引擎
    private LensEngine mLensEngine;
    private int mLensType;
    // 视频是否是正面
    private boolean isFront = false;

    @Override
    protected void onResume() {
        super.onResume();

        createLensEngine();
        startLensEngine();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mLensEngine != null) {
            this.mLensEngine.release();
        }
        if (this.mAnalyzer != null) {
            this.mAnalyzer.stop();
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDynamicGesturesComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_dynamic_gestures;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("lensType", this.lensType);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("动态手势识别");

        if (savedInstanceState != null) {
            mLensType = savedInstanceState.getInt("lensType");
        }

        createHandAnalyzer();
    }

    /**
     * 创建手部分析仪
     */
    private void createHandAnalyzer() {
        // Create a  analyzer. You can create an analyzer using the provided customized face detection parameter: MLHandKeypointAnalyzerSetting
        MLGestureAnalyzerSetting setting = new MLGestureAnalyzerSetting.Factory().create();
        mAnalyzer = MLGestureAnalyzerFactory.getInstance().getGestureAnalyzer(setting);
        mAnalyzer.setTransactor(new HandAnalyzerTransactor(mOverlay));
    }

    /**
     * 创建镜头引擎
     */
    private void createLensEngine() {
        Context context = this.getApplicationContext();
        // Create LensEngine.
        mLensEngine = new LensEngine.Creator(context, mAnalyzer)
                .setLensType(this.mLensType)
                .applyDisplayDimension(640, 480)
                .applyFps(25.0f)
                .enableAutomaticFocus(true)
                .create();
    }

    /**
     * 启动镜头引擎
     */
    private void startLensEngine() {
        if (this.mLensEngine != null) {
            try {
                this.mPreview.start(this.mLensEngine, this.mOverlay);
            } catch (IOException e) {
                Timber.e("Failed to start lens engine." + e);
                this.mLensEngine.release();
                this.mLensEngine = null;
            }
        }
    }

    @OnClick({
            R.id.btn_dynamicgestures_switch,                                                        // 切换镜头按钮
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dynamicgestures_switch:                                                   // 切换镜头按钮
                switchCamera();
                break;
        }
    }

    /**
     * 切换相机
     */
    private void switchCamera() {
        isFront = !isFront;
        if (this.isFront) {
            mLensType = LensEngine.FRONT_LENS;
        } else {
            mLensType = LensEngine.BACK_LENS;
        }
        if (this.mLensEngine != null) {
            this.mLensEngine.close();
        }
        this.createLensEngine();
        this.startLensEngine();
    }

    /**
     * 识别结果处理类
     */
    private static class HandAnalyzerTransactor implements MLAnalyzer.MLTransactor<MLGesture> {
        private GraphicOverlay mGraphicOverlay;

        HandAnalyzerTransactor(GraphicOverlay ocrGraphicOverlay) {
            this.mGraphicOverlay = ocrGraphicOverlay;
        }

        /**
         * 处理分析器返回的结果.
         */
        @Override
        public void transactResult(MLAnalyzer.Result<MLGesture> result) {
            this.mGraphicOverlay.clear();

            SparseArray<MLGesture> handGestureSparseArray = result.getAnalyseList();
            List<MLGesture> list = new ArrayList<>();
            for (int i = 0; i < handGestureSparseArray.size(); i++) {
                list.add(handGestureSparseArray.valueAt(i));
            }
            HandGestureGraphic graphic = new HandGestureGraphic(this.mGraphicOverlay, list);
            this.mGraphicOverlay.add(graphic);
        }

        @Override
        public void destroy() {
            this.mGraphicOverlay.clear();
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