package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.FaceStatusNewEnum;
import com.baidu.idl.face.platform.ILivenessStrategy;
import com.baidu.idl.face.platform.ILivenessStrategyCallback;
import com.baidu.idl.face.platform.ILivenessViewCallback;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.baidu.idl.face.platform.manager.TimeManager;
import com.baidu.idl.face.platform.model.FaceExtInfo;
import com.baidu.idl.face.platform.model.ImageInfo;
import com.baidu.idl.face.platform.stat.Ast;
import com.baidu.idl.face.platform.ui.FaceSDKResSettings;
import com.baidu.idl.face.platform.ui.task.MediaListener;
import com.baidu.idl.face.platform.ui.task.MediaPrepareTask;
import com.baidu.idl.face.platform.ui.utils.BrightnessUtils;
import com.baidu.idl.face.platform.ui.utils.CameraPreviewUtils;
import com.baidu.idl.face.platform.ui.utils.CameraUtils;
import com.baidu.idl.face.platform.ui.utils.VolumeUtils;
import com.baidu.idl.face.platform.ui.widget.FaceAuraColorView;
import com.baidu.idl.face.platform.ui.widget.FaceDetectRoundView;
import com.baidu.idl.face.platform.utils.APIUtils;
import com.baidu.idl.face.platform.utils.DensityUtils;
import com.baidu.idl.face.platform.utils.FileUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.dialog.TimeoutDialog;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.di.component.DaggerBaiduFaceLivenessVideoExpComponent;
import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceLivenessVideoExpContract;
import com.zqw.mobile.grainfull.mvp.presenter.BaiduFaceLivenessVideoExpPresenter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:百度AI - 人脸识别 - 面部活力体验视频(包含视频录制)
 * <p>
 * Created on 2022/07/12 17:53
 *
 * @author 赤槿
 * module name is BaiduFaceLivenessVideoExpActivity
 */
public class BaiduFaceLivenessVideoExpActivity extends BaseActivity<BaiduFaceLivenessVideoExpPresenter> implements BaiduFaceLivenessVideoExpContract.View,
        VolumeUtils.VolumeCallback,
        ILivenessStrategyCallback,
        ILivenessViewCallback,
        TextureView.SurfaceTextureListener, MediaListener,
        TimeoutDialog.OnTimeoutDialogClickListener {
    /*------------------------------------------控件信息------------------------------------------*/

    /*------------------------------------------业务逻辑------------------------------------------*/
    // 对话框
    private MaterialDialog mDialog;

    // 视频录制相关
    private static final String VIDEO_FILE_DIC = "/vrecord";
    private static final int START_RECORD = 0;   // 开始录制
    private static final int STOP_RECORD = 1;    // 结束录制
    private static final int RECORD_OPEN_ERROR = 2;

    // View
    protected View mRootView;
    protected FrameLayout mFrameLayout;
    private TextureView mTextureView;
    protected ImageView mCloseView;
    protected ImageView mSoundView;
    protected ImageView mSuccessView;
    protected TextView mTipsTopView;
    protected FaceDetectRoundView mFaceDetectRoundView;
    protected LinearLayout mImageLayout;
    protected LinearLayout mImageLayout2;
    private RelativeLayout mRelativeAddImageView;
    private ImageView mImageAnim;
    public View mViewBg;
    protected FaceAuraColorView mFaceAuraColorView;
    // 人脸信息
    protected FaceConfig mFaceConfig;
    protected ILivenessStrategy mILivenessStrategy;
    // 显示Size
    private Rect mPreviewRect = new Rect();
    protected int mDisplayWidth = 0;
    protected int mDisplayHeight = 0;
    // 状态标识
    protected volatile boolean mIsEnableSound = true;
    protected boolean mIsCompletion = false;
    private String currentSaveName = null;
    private String videoExName = null;
    // 相机
    protected Camera mCamera;
    protected Camera.Parameters mCameraParam;
    protected int mCameraId;
    protected int mPreviewWidth;
    protected int mPreviewHight;
    protected int mPreviewDegree;
    private MediaRecorder mMediaRecorder;
    // 监听系统音量广播
    protected BroadcastReceiver mVolumeReceiver;

    private Context mContext;
    private AnimationDrawable mAnimationDrawable;
    private LivenessTypeEnum mLivenessType = null;

    // 视频录制相关
    private MediaPrepareTask mMediaPrepareTask;
    private long mTimeVideoRecord = 0L;
    private Bitmap mBitmap;
    public volatile int mFrameStackCounter = 0;
    private DecodeThread mDecodeThread;
    private boolean mIsStartPreviewSuccess;

    private TimeoutDialog mTimeoutDialog;

    /**
     * 根据主题使用不同的颜色。
     * 如果想要纯透明，则需要重写此方法，返回值为 -1 即可。
     */
    public int useStatusBarColor() {
        return -1;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBaiduFaceLivenessVideoExpComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    protected void setForm() {
        setScreenBright();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_baidu_face_liveness_video_exp;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        // 初始化Loading对话框
        mDialog = new MaterialDialog.Builder(this).content(R.string.common_execute).progress(true, 0).cancelable(false).build();

        mContext = this;
        DisplayMetrics dm = new DisplayMetrics();
        Display display = this.getWindowManager().getDefaultDisplay();
        display.getMetrics(dm);
        mDisplayWidth = dm.widthPixels;
        mDisplayHeight = dm.heightPixels;

        FaceSDKResSettings.initializeResId();
        mFaceConfig = FaceSDKManager.getInstance().getFaceConfig();

        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int vol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        mIsEnableSound = vol > 0 ? mFaceConfig.isSound() : false;

        mRootView = this.findViewById(com.baidu.idl.face.platform.ui.R.id.liveness_root_layout);
        mFrameLayout = mRootView.findViewById(com.baidu.idl.face.platform.ui.R.id.liveness_surface_layout);

        mTextureView = new TextureView(this);

        int w = mDisplayWidth;
        int h = mDisplayHeight;

        FrameLayout.LayoutParams cameraFL = new FrameLayout.LayoutParams(
                (int) (w * FaceDetectRoundView.SURFACE_RATIO), (int) (h * FaceDetectRoundView.SURFACE_RATIO),
                Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        mTextureView.setLayoutParams(cameraFL);
        mFrameLayout.addView(mTextureView);

        mRootView.findViewById(com.baidu.idl.face.platform.ui.R.id.liveness_close).setOnClickListener(v -> onBackPressed());

        mFaceDetectRoundView = mRootView.findViewById(com.baidu.idl.face.platform.ui.R.id.liveness_face_round);
        mFaceDetectRoundView.setIsActiveLive(false);
        mFaceAuraColorView = mRootView.findViewById(com.baidu.idl.face.platform.ui.R.id.detect_aura);
        mCloseView = mRootView.findViewById(com.baidu.idl.face.platform.ui.R.id.liveness_close);
        mSoundView = mRootView.findViewById(com.baidu.idl.face.platform.ui.R.id.liveness_sound);
        mSoundView.setImageResource(mIsEnableSound ?
                com.baidu.idl.face.platform.ui.R.mipmap.icon_titlebar_voice2 : com.baidu.idl.face.platform.ui.R.mipmap.icon_titlebar_voice_close);
        mSoundView.setOnClickListener(v -> {
            mIsEnableSound = !mIsEnableSound;
            mSoundView.setImageResource(mIsEnableSound ?
                    com.baidu.idl.face.platform.ui.R.mipmap.icon_titlebar_voice2 : com.baidu.idl.face.platform.ui.R.mipmap.icon_titlebar_voice_close);
            if (mILivenessStrategy != null) {
                mILivenessStrategy.setLivenessStrategySoundEnable(mIsEnableSound);
            }
        });
        mTipsTopView = mRootView.findViewById(com.baidu.idl.face.platform.ui.R.id.liveness_top_tips);
        mSuccessView = mRootView.findViewById(com.baidu.idl.face.platform.ui.R.id.liveness_success_image);

        mImageLayout = mRootView.findViewById(com.baidu.idl.face.platform.ui.R.id.liveness_result_image_layout);
        mImageLayout2 = mRootView.findViewById(com.baidu.idl.face.platform.ui.R.id.liveness_result_image_layout2);
        mRelativeAddImageView = mRootView.findViewById(com.baidu.idl.face.platform.ui.R.id.relative_add_image_view);
        addImageView();
        mViewBg = findViewById(com.baidu.idl.face.platform.ui.R.id.view_live_bg);
        initData();

        // 添加至销毁列表
        BaiduFaceRecognitionActivity.addDestroyActivity(this, "BaiduFaceRecognitionActivity");
    }

    private void initData() {
        // 初始化录制时间（默认15s）
        mTimeVideoRecord = mFaceConfig.getRecordVideoTime();
        if (mTimeVideoRecord > 15 * 1000) {
            mTimeVideoRecord = 15 * 1000;
        }
    }

    /**
     * 动态加载ImageView
     */
    private void addImageView() {
        mFaceDetectRoundView.post(() -> {
            mImageAnim = new ImageView(BaiduFaceLivenessVideoExpActivity.this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup
                    .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.height = DensityUtils.dip2px(mContext, 110); // 设置图片的高度
            layoutParams.width = DensityUtils.dip2px(mContext, 87);   // 设置图片的宽度
            float halfHeight = mFaceDetectRoundView.getHeight() / 2;
            layoutParams.setMargins(0, (int) (halfHeight - (halfHeight * FaceDetectRoundView.HEIGHT_RATIO))
                    - layoutParams.height / 2, 0, 0);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            mImageAnim.setLayoutParams(layoutParams);
            mImageAnim.setScaleType(ImageView.ScaleType.FIT_XY);  // 使图片充满控件大小
            mRelativeAddImageView.addView(mImageAnim);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 删除视频
        deleteVideo();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mVolumeReceiver = VolumeUtils.registerVolumeReceiver(this, this);
        if (mFaceDetectRoundView != null) {
            mFaceDetectRoundView.setTipTopText("请将脸移入取景框");
        }
        currentSaveName = null;
        mRecordHandler.sendEmptyMessage(START_RECORD);
        mDecodeThread = new DecodeThread();
        mDecodeThread.start();
    }

    /**
     * 删除视频
     */
    private void deleteVideo() {
//        File file = new File(getFilesDir() + VIDEO_FILE_DIC);
        File file = new File(Constant.VIDEO_PATH + VIDEO_FILE_DIC);
        FileUtils.deleteDir(file);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart");
        if (mFrameStackCounter > 0) {
            mFrameStackCounter = 0;
        }
        if (mFaceAuraColorView.getVisibility() == View.VISIBLE) {
            mFaceAuraColorView.setVisibility(View.GONE);
        }
        updateOriIcon();
    }

    @Override
    public void onPause() {
        super.onPause();
        mRecordHandler.sendEmptyMessage(STOP_RECORD);
        if (mDecodeThread != null) {
            mDecodeThread.running = false;
            mDecodeThread.interrupt();
            mDecodeThread = null;
        }
        if (mMediaPrepareTask != null) {
            mMediaPrepareTask.cancel(true);
            mMediaPrepareTask = null;
        }
        if (mILivenessStrategy != null) {
            mILivenessStrategy.reset();
        }
        VolumeUtils.unRegisterVolumeReceiver(this, mVolumeReceiver);
        mVolumeReceiver = null;
        mFaceDetectRoundView.setProcessCount(0,
                mFaceConfig.getLivenessTypeList().size());
        mIsCompletion = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        mFaceAuraColorView.release();
        // 拷贝视频路径
        // FileUtils.copyFile(currentSaveName, "/sdcard" + videoExName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRecordHandler != null) {
            mRecordHandler.removeCallbacksAndMessages(null);
            mRecordHandler = null;
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
     * 设置屏幕亮度
     */
    private void setScreenBright() {
        BrightnessUtils.setBrightness(this, 255);
    }

    public Handler mRecordHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg != null) {
                switch (msg.what) {
                    case START_RECORD:
                        controlVideoRecord(false);
                        break;

                    case STOP_RECORD:
                        controlVideoRecord(true);
                        break;

                    case RECORD_OPEN_ERROR:
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                                "record open failed", Toast.LENGTH_SHORT).show());
                        break;

                    default:
                        break;
                }
            }
        }
    };

    public void controlVideoRecord(boolean isStop) {
        if (isStop) {
            if (mMediaRecorder != null) {
                try {
                    mMediaRecorder.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            releaseMediaRecorder();
            stopPreview();
        } else {
            if (mMediaPrepareTask == null) {
                mMediaPrepareTask = new MediaPrepareTask(this);
                mMediaPrepareTask.execute(false, null, null);
            }
        }
    }

    protected void stopPreview() {
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                CameraUtils.releaseCamera(mCamera);
                mCamera = null;
            }
        }

        if (mILivenessStrategy != null) {
            mILivenessStrategy = null;
        }
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        if (mFrameStackCounter > 0) {
            return;
        }

        if (mTextureView == null) {
            return;
        }

        mBitmap = mTextureView.getBitmap();
        if (mILivenessStrategy == null) {
            mILivenessStrategy = FaceSDKManager.getInstance().getLivenessStrategyModule(this);
            mILivenessStrategy.setPreviewDegree(mPreviewDegree);
            mILivenessStrategy.setLivenessStrategySoundEnable(mIsEnableSound);
            Rect detectRect = FaceDetectRoundView.getPreviewDetectRect(
                    mDisplayWidth, mPreviewHight, mPreviewWidth);
            mILivenessStrategy.setLivenessStrategyConfig(
                    mFaceConfig.getLivenessTypeList(), mFaceConfig.getLivenessColorTypeList(),
                    mPreviewRect, detectRect, this);
        }
        mFrameStackCounter++;
    }

    @Override
    public void onLivenessCompletion(final FaceStatusNewEnum status, final String message,
                                     HashMap<String, ImageInfo> base64ImageCropMap,
                                     HashMap<String, ImageInfo> base64ImageSrcMap,
                                     final int currentLivenessCount, float livenessScore) {
        runOnUiThread(() -> {
            if (mIsCompletion) {
                return;
            }

            onRefreshView(status, message, currentLivenessCount);

            if (status == FaceStatusNewEnum.OK
                    || status == FaceStatusNewEnum.AuraColorError
                    || status == FaceStatusNewEnum.AuraLivenessScoreError) {
                mIsCompletion = true;
                // saveAllImage(base64ImageCropMap, base64ImageSrcMap);
            }
            // 打点
            Ast.getInstance().faceHit("liveness");
        });
        runOnUiThread(() -> {
            if (status == FaceStatusNewEnum.OK && mIsCompletion) {
                // 获取最优图片
                getBestImage(base64ImageCropMap, base64ImageSrcMap, livenessScore);
            } else if (status == FaceStatusNewEnum.DetectRemindCodeTimeout) {
                if (mViewBg != null) {
                    mViewBg.setVisibility(View.VISIBLE);
                }
                showMessageDialog();
            } else if (status == FaceStatusNewEnum.AuraLivenessScoreError && mIsCompletion) {
                startFailureActivity(livenessScore, false);
            } else if (status == FaceStatusNewEnum.AuraColorError && mIsCompletion) {
                startFailureActivity(0f, true);
            }
        });
    }

    private void onRefreshView(FaceStatusNewEnum status, String message, int currentLivenessCount) {
        switch (status) {
            case OK:
            case FaceLivenessActionComplete:
            case DetectRemindCodeTooClose:
            case DetectRemindCodeTooFar:
            case DetectRemindCodeBeyondPreviewFrame:
            case DetectRemindCodeNoFaceDetected:
                // onRefreshTipsView(false, message);
                mFaceDetectRoundView.setTipTopText(message);
                mFaceDetectRoundView.setTipSecondText("");
                mFaceDetectRoundView.setProcessCount(currentLivenessCount,
                        mFaceConfig.getLivenessTypeList().size());
                if (mFaceAuraColorView.getVisibility() == View.VISIBLE) {
                    mFaceAuraColorView.setTipTopText(message);
                    mFaceAuraColorView.setTipSecondText("");
                }
                stopAnim();
                break;

            case FaceLivenessActionTypeLiveEye:
            case FaceLivenessActionTypeLiveMouth:
                mFaceDetectRoundView.setTipTopText(message);
                mFaceDetectRoundView.setTipSecondText("");
                mFaceDetectRoundView.setProcessCount(currentLivenessCount,
                        mFaceConfig.getLivenessTypeList().size());
                // onRefreshTipsView(false, message);
                // onRefreshSuccessView(false);
                break;

            case DetectRemindCodePitchOutofUpRange:
            case DetectRemindCodePitchOutofDownRange:
            case DetectRemindCodeYawOutofLeftRange:
            case DetectRemindCodeYawOutofRightRange:
                mFaceDetectRoundView.setTipTopText("请保持正脸");
                mFaceDetectRoundView.setTipSecondText(message);
                mFaceDetectRoundView.setProcessCount(currentLivenessCount,
                        mFaceConfig.getLivenessTypeList().size());
                if (mFaceAuraColorView.getVisibility() == View.VISIBLE) {
                    mFaceAuraColorView.setTipTopText("请保持正脸");
                    mFaceAuraColorView.setTipSecondText(message);
                }
                // onRefreshSuccessView(false);
                // onRefreshTipsView(true, message);
                break;

            case FaceLivenessActionCodeTimeout:    // 动作超时，播放教程动画
                mFaceDetectRoundView.setProcessCount(currentLivenessCount,
                        mFaceConfig.getLivenessTypeList().size());
                mFaceDetectRoundView.setIsShowShade(true);
                // 帧动画开启
                if (mRelativeAddImageView.getVisibility() == View.INVISIBLE) {
                    mRelativeAddImageView.setVisibility(View.VISIBLE);
                }
                loadAnimSource();
                // 监听帧动画时间
                int duration = 0;
                for (int i = 0; i < mAnimationDrawable.getNumberOfFrames(); i++) {
                    // 计算动画播放的时间
                    duration += mAnimationDrawable.getDuration(i);
                }
                TimeManager.getInstance().setActiveAnimTime(duration);
                break;

            case AuraStart:
                mFaceDetectRoundView.setTipTopText(message);
                mFaceDetectRoundView.setTipSecondText("");
                break;

            case AuraColorChange:
                mFaceDetectRoundView.setTipTopText(" ");
                if (mFaceAuraColorView.getVisibility() == View.VISIBLE) {
                    mFaceAuraColorView.setTipTopText(message);
                    mFaceAuraColorView.setTipSecondText("");
                }
                break;

            case AuraColorError:
                break;

            default:
                mFaceDetectRoundView.setTipTopText("请保持正脸");
                mFaceDetectRoundView.setTipSecondText(message);
                mFaceDetectRoundView.setProcessCount(currentLivenessCount,
                        mFaceConfig.getLivenessTypeList().size());
                if (mFaceAuraColorView.getVisibility() == View.VISIBLE) {
                    mFaceAuraColorView.setTipTopText("请保持正脸");
                    mFaceAuraColorView.setTipSecondText(message);
                }
                // onRefreshSuccessView(false);
                // onRefreshTipsView(false, message);
                break;
        }
    }

    private void stopAnim() {
        mFaceDetectRoundView.setIsShowShade(false);
        if (mAnimationDrawable != null) {
            mAnimationDrawable.stop();
            mAnimationDrawable = null;
        }
        if (mRelativeAddImageView.getVisibility() == View.VISIBLE) {
            mRelativeAddImageView.setVisibility(View.INVISIBLE);
        }
    }

    // 加载动画
    private void loadAnimSource() {
        if (mLivenessType != null) {
            switch (mLivenessType) {
                case Eye:
                    mImageAnim.setBackgroundResource(com.baidu.idl.face.platform.ui.R.drawable.anim_eye);
                    break;
                case Mouth:
                    mImageAnim.setBackgroundResource(com.baidu.idl.face.platform.ui.R.drawable.anim_mouth);
                    break;
                default:
                    break;
            }
            mAnimationDrawable = (AnimationDrawable) mImageAnim.getBackground();
            mAnimationDrawable.start();
        }
    }

    @Override
    public void setCurrentLiveType(LivenessTypeEnum liveType) {
        mLivenessType = liveType;
    }

    @Override
    public void viewReset() {
        mFaceDetectRoundView.setProcessCount(0, 1);
    }

    @Override
    public void animStop() {
        runOnUiThread(() -> stopAnim());
    }

    @Override
    public void setFaceInfo(FaceExtInfo faceInfo) {
        // TODO：传递FaceInfo信息，便于调试画人脸检测框和人脸检测区域（使用时，将注释放开）
        // if (mFaceDetectRoundView != null) {
        //     mFaceDetectRoundView.setFaceInfo(faceInfo);
        // }
    }

    @Override
    public void setBackgroundColor(final int currentColor, final int preColor) {
        runOnUiThread(() -> {
            if (mFaceAuraColorView == null) {
                return;
            }

            int preBag = -1;
            if (preColor == 0) {
                preBag = getResources().getColor(com.baidu.idl.face.platform.ui.R.color.aura_blue);
            } else if (preColor == 1) {
                preBag = getResources().getColor(com.baidu.idl.face.platform.ui.R.color.aura_green);
            } else if (preColor == 2) {
                preBag = getResources().getColor(com.baidu.idl.face.platform.ui.R.color.aura_red);
            }

            switch (currentColor) {
                case -1:
                    updateOriIcon();
                    mFaceAuraColorView.setVisibility(View.GONE);
                    break;
                case 0:
                    updateWhiteIcon();
                    mFaceAuraColorView.setVisibility(View.VISIBLE);
                    mFaceAuraColorView.start(getResources().getColor(com.baidu.idl.face.platform.ui.R.color.aura_blue));
                    mFaceAuraColorView.setColorBg(preBag);
                    break;
                case 1:
                    updateWhiteIcon();
                    mFaceAuraColorView.setVisibility(View.VISIBLE);
                    mFaceAuraColorView.start(getResources().getColor(com.baidu.idl.face.platform.ui.R.color.aura_green));
                    mFaceAuraColorView.setColorBg(preBag);
                    break;
                case 2:
                    updateWhiteIcon();
                    mFaceAuraColorView.setVisibility(View.VISIBLE);
                    mFaceAuraColorView.start(getResources().getColor(com.baidu.idl.face.platform.ui.R.color.aura_red));
                    mFaceAuraColorView.setColorBg(preBag);
                    break;
                case 3:
                    updateOriIcon();
                    mFaceAuraColorView.setVisibility(View.VISIBLE);
                    mFaceAuraColorView.start(getResources().getColor(com.baidu.idl.face.platform.ui.R.color.aura_default));
                    mFaceAuraColorView.setColorBg(preBag);
                    break;
                default:
                    updateOriIcon();
                    mFaceAuraColorView.setVisibility(View.GONE);
                    break;
            }
        });
    }

    private void updateWhiteIcon() {
        mSoundView.setImageResource(mIsEnableSound ?
                com.baidu.idl.face.platform.ui.R.mipmap.icon_titlebar_voice2_white : com.baidu.idl.face.platform.ui.R.mipmap.icon_titlebar_voice_close_white);
        mCloseView.setImageResource(com.baidu.idl.face.platform.ui.R.mipmap.icon_titlebar_close_white);
        mFaceAuraColorView.setTextColor(Color.WHITE);
    }

    @Override
    public void startRecordVideo(boolean isLostFaceId) {
        // 如果丢失faceId
        if (isLostFaceId) {
            releaseMediaRecorder();
            initMediaRecorder(mCameraParam);
        }

        if (mMediaPrepareTask != null) {
            mMediaPrepareTask.cancel(true);
            mMediaPrepareTask = null;
        }
        mMediaPrepareTask = new MediaPrepareTask(this);
        mMediaPrepareTask.execute(true, null, null);
    }

    private boolean initMediaRecorder(Camera.Parameters cameraParam) {
        Point point;
        // 如果当前设备在录制中预览分辨率发生改变，则将两种分辨率改成一致
        if (Build.MODEL.equals("M5 Note")) {
            point = new Point(mPreviewWidth, mPreviewHight);
        } else {
            point = CameraPreviewUtils.getBestVideoPreview(cameraParam);
        }
        // Use the same size for recording profile.
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);
        profile.videoFrameWidth = point.x;
        profile.videoFrameHeight = point.y;
        // BEGIN_INCLUDE (configure_media_recorder)
        mMediaRecorder = new MediaRecorder();
        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(profile);
        mMediaRecorder.setOrientationHint(270);
        // Step 4: Set output file
        mMediaRecorder.setOutputFile(getCurrentSaveName());
        // END_INCLUDE (configure_media_recorder)
        // Step 5: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
            mTextureView.setSurfaceTextureListener(this);
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    /**
     * 获取视频保存目录
     */
    protected String getCurrentSaveName() {
        if (TextUtils.isEmpty(currentSaveName)) {
//            File path = getFilesDir();
//            if (path == null) {
//                return null;
//            }
            File dir = new File(Constant.VIDEO_PATH + VIDEO_FILE_DIC);
//            File dir = new File(path.toString() + VIDEO_FILE_DIC);
            if (!dir.exists()) {
                dir.mkdir();
            }
            videoExName = "/" + TimeManager.getDate() + ".mp4";
            currentSaveName = dir + videoExName;
        }
        return currentSaveName;
    }

    @Override
    public int doInBackground(boolean isStart) {
        if (!isStart) {
            mIsStartPreviewSuccess = startPreview();
            return -1;
        } else {
            // if (startPreview()) {
            if (mIsStartPreviewSuccess) {
                try {
                    mMediaRecorder.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    if (mRecordHandler != null) {
                        mRecordHandler.sendEmptyMessage(RECORD_OPEN_ERROR);
                    }
                }
            } else {
                releaseMediaRecorder();
                return -1;
            }
            return 0;
        }
    }

    protected boolean startPreview() {

        if (mCamera == null) {
            try {
                mCamera = open();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mCamera == null) {
            return false;
        }

        if (mCameraParam == null) {
            mCameraParam = mCamera.getParameters();
        }

        mCameraParam.setPictureFormat(PixelFormat.JPEG);
        int degree = displayOrientation(this);
        mCamera.setDisplayOrientation(degree);
        // 设置后无效，camera.setDisplayOrientation方法有效
        mCameraParam.set("rotation", degree);
        mPreviewDegree = degree;

        Point point;
        // 如果当前设备在录制中预览分辨率发生改变，则将两种分辨率改成一致
        if (Build.MODEL.equals("M5 Note")) {
            point = CameraPreviewUtils.getBestVideoForSameSize(mCameraParam);
        } else {
            point = CameraPreviewUtils.getBestPreview(mCameraParam,
                    new Point(mDisplayWidth, mDisplayHeight));
        }

        mPreviewWidth = point.x;
        mPreviewHight = point.y;
        // Log.e(TAG, "x = " + mPreviewWidth + " y = " + mPreviewHight);
        // Preview 768,432

        if (mILivenessStrategy != null) {
            mILivenessStrategy.setPreviewDegree(degree);
        }

        mPreviewRect.set(0, 0, mPreviewHight, mPreviewWidth);


        mCameraParam.setPreviewSize(mPreviewWidth, mPreviewHight);
        mCamera.setParameters(mCameraParam);

        try {
            mCamera.setPreviewTexture(mTextureView.getSurfaceTexture());
            mCamera.startPreview();
        } catch (RuntimeException e) {
            e.printStackTrace();
            CameraUtils.releaseCamera(mCamera);
            mCamera = null;
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            CameraUtils.releaseCamera(mCamera);
            mCamera = null;
            return false;
        }
        return initMediaRecorder(mCameraParam);
    }


    private int displayOrientation(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
            default:
                degrees = 0;
                break;
        }
        int result = (0 - degrees + 360) % 360;
        if (APIUtils.hasGingerbread()) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(mCameraId, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;
            } else {
                result = (info.orientation - degrees + 360) % 360;
            }
        }
        return result;
    }

    private Camera open() {
        Camera camera;
        int numCameras = Camera.getNumberOfCameras();
        if (numCameras == 0) {
            return null;
        }

        int index = 0;
        while (index < numCameras) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(index, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                break;
            }
            index++;
        }

        if (index < numCameras) {
            camera = Camera.open(index);
            mCameraId = index;
        } else {
            camera = Camera.open(0);
            mCameraId = 0;
        }
        return camera;
    }

    @Override
    public void onPostExecute(Integer result) {
        if (result == 0) {
            if (mRecordHandler == null) {
                mRecordHandler = new Handler();
            }
            mRecordHandler.postDelayed(() -> releaseMediaRecorder(), mTimeVideoRecord + 1000);
        }
    }

    @Override
    public void volumeChanged() {
        try {
            AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            if (am != null) {
                int cv = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                mIsEnableSound = cv > 0;
                mSoundView.setImageResource(mIsEnableSound
                        ? com.baidu.idl.face.platform.ui.R.mipmap.icon_titlebar_voice2 : com.baidu.idl.face.platform.ui.R.mipmap.icon_titlebar_voice_close);
                if (mILivenessStrategy != null) {
                    mILivenessStrategy.setLivenessStrategySoundEnable(mIsEnableSound);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class DecodeThread extends Thread {
        private volatile boolean running = true;

        @Override
        public void run() {
            while (running) {
                if (mFrameStackCounter > 0) {
                    if (mIsCompletion) {
                        return;
                    }
                    if (!running) {
                        return;
                    }
                    if (mBitmap != null && mILivenessStrategy != null) {
                        mBitmap = FaceSDKManager.getInstance().scaleImage(mBitmap, mPreviewHight, mPreviewWidth);
                        mILivenessStrategy.livenessStrategy(mBitmap);
                        mBitmap.recycle();
                        mFrameStackCounter--;
                    }
                }
            }
        }
    }

    private void updateOriIcon() {
        mSoundView.setImageResource(mIsEnableSound ?
                com.baidu.idl.face.platform.ui.R.mipmap.icon_titlebar_voice2 : com.baidu.idl.face.platform.ui.R.mipmap.icon_titlebar_voice_close);
        mCloseView.setImageResource(com.baidu.idl.face.platform.ui.R.mipmap.icon_titlebar_close);
        mFaceAuraColorView.setTextColor(Color.BLACK);
    }


    /**
     * 获取最优图片
     *
     * @param imageCropMap 抠图集合
     * @param imageSrcMap  原图集合
     */
    private void getBestImage(HashMap<String, ImageInfo> imageCropMap, HashMap<String, ImageInfo> imageSrcMap, float livenessScore) {
        // 将抠图集合中的图片按照质量降序排序，最终选取质量最优的一张抠图图片
        if (imageCropMap != null && imageCropMap.size() > 0) {
            List<Map.Entry<String, ImageInfo>> list1 = new ArrayList<>(imageCropMap.entrySet());
            Collections.sort(list1, (o1, o2) -> {
                String[] key1 = o1.getKey().split("_");
                String score1 = key1[2];
                String[] key2 = o2.getKey().split("_");
                String score2 = key2[2];
                // 降序排序
                return Float.valueOf(score2).compareTo(Float.valueOf(score1));
            });

            // 获取抠图中的加密的base64
//            String base64 = list1.get(0).getValue().getSecBase64();
        }

        // 将原图集合中的图片按照质量降序排序，最终选取质量最优的一张原图图片
        if (imageSrcMap != null && imageSrcMap.size() > 0) {
            List<Map.Entry<String, ImageInfo>> list2 = new ArrayList<>(imageSrcMap.entrySet());
            Collections.sort(list2, (o1, o2) -> {
                String[] key1 = o1.getKey().split("_");
                String score1 = key1[2];
                String[] key2 = o2.getKey().split("_");
                String score2 = key2[2];
                // 降序排序
                return Float.valueOf(score2).compareTo(Float.valueOf(score1));
            });

            // 获取原图中的加密的base64
//            String base64 = list2.get(0).getValue().getSecBase64();
        }

        // 页面跳转
        Intent intent = new Intent(this, BaiduFaceCollectionSuccessActivity.class);
        intent.putExtra("livenessScore", livenessScore);
        intent.putExtra("SaveName", currentSaveName);
        startActivity(intent);
    }

    private void showMessageDialog() {
        mTimeoutDialog = new TimeoutDialog(this);
        mTimeoutDialog.setDialogListener(this);
        mTimeoutDialog.setCanceledOnTouchOutside(false);
        mTimeoutDialog.setCancelable(false);
        mTimeoutDialog.show();
        onPause();
    }

    private void startFailureActivity(float livenessScore, boolean isColorError) {
        // 页面跳转
        Intent intent = new Intent(this, BaiduFaceCollectFailureActivity.class);
        intent.putExtra("livenessScore", livenessScore);
        intent.putExtra("isColorError", isColorError);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onRecollect() {
        if (mTimeoutDialog != null) {
            mTimeoutDialog.dismiss();
        }
        if (mViewBg != null) {
            mViewBg.setVisibility(View.GONE);
        }
        if (mFrameStackCounter > 0) {
            mFrameStackCounter = 0;
        }
        onResume();
    }

    @Override
    public void onReturn() {
        if (mTimeoutDialog != null) {
            mTimeoutDialog.dismiss();
        }
        finish();
    }

    @Override
    public void showLoadingSubmit() {
        if (mDialog != null)
            mDialog.show();
    }

    @Override
    public void hideLoadingSubmit() {
        if (mDialog != null)
            mDialog.cancel();
    }
}