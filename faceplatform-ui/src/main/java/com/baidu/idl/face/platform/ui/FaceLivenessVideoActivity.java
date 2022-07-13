/**
 * Copyright (C) 2017 Baidu Inc. All rights reserved.
 */
package com.baidu.idl.face.platform.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.baidu.idl.face.platform.ui.task.MediaListener;
import com.baidu.idl.face.platform.ui.task.MediaPrepareTask;
import com.baidu.idl.face.platform.ui.utils.BrightnessUtils;
import com.baidu.idl.face.platform.ui.utils.CameraPreviewUtils;
import com.baidu.idl.face.platform.ui.utils.CameraUtils;
import com.baidu.idl.face.platform.ui.utils.VolumeUtils;
import com.baidu.idl.face.platform.ui.widget.FaceAuraColorView;
import com.baidu.idl.face.platform.ui.widget.FaceDetectRoundView;
import com.baidu.idl.face.platform.utils.APIUtils;
import com.baidu.idl.face.platform.utils.Base64Utils;
import com.baidu.idl.face.platform.utils.DensityUtils;
import com.baidu.idl.face.platform.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活体检测接口
 */
public class FaceLivenessVideoActivity extends Activity implements
        VolumeUtils.VolumeCallback,
        ILivenessStrategyCallback,
        ILivenessViewCallback,
        TextureView.SurfaceTextureListener, MediaListener {

    public static final String TAG = FaceLivenessVideoActivity.class.getSimpleName();
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "record open failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;

                    default:
                        break;
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenBright();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_face_liveness_v3100);
        mContext = FaceLivenessVideoActivity.this;
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

        mRootView = this.findViewById(R.id.liveness_root_layout);
        mFrameLayout = (FrameLayout) mRootView.findViewById(R.id.liveness_surface_layout);

        mTextureView = new TextureView(this);

        int w = mDisplayWidth;
        int h = mDisplayHeight;

        FrameLayout.LayoutParams cameraFL = new FrameLayout.LayoutParams(
                (int) (w * FaceDetectRoundView.SURFACE_RATIO), (int) (h * FaceDetectRoundView.SURFACE_RATIO),
                Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        mTextureView.setLayoutParams(cameraFL);
        mFrameLayout.addView(mTextureView);

        mRootView.findViewById(R.id.liveness_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mFaceDetectRoundView = (FaceDetectRoundView) mRootView.findViewById(R.id.liveness_face_round);
        mFaceDetectRoundView.setIsActiveLive(false);
        mFaceAuraColorView = (FaceAuraColorView) mRootView.findViewById(R.id.detect_aura);
        mCloseView = (ImageView) mRootView.findViewById(R.id.liveness_close);
        mSoundView = (ImageView) mRootView.findViewById(R.id.liveness_sound);
        mSoundView.setImageResource(mIsEnableSound ?
                R.mipmap.icon_titlebar_voice2 : R.mipmap.icon_titlebar_voice_close);
        mSoundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsEnableSound = !mIsEnableSound;
                mSoundView.setImageResource(mIsEnableSound ?
                        R.mipmap.icon_titlebar_voice2 : R.mipmap.icon_titlebar_voice_close);
                if (mILivenessStrategy != null) {
                    mILivenessStrategy.setLivenessStrategySoundEnable(mIsEnableSound);
                }
            }
        });
        mTipsTopView = (TextView) mRootView.findViewById(R.id.liveness_top_tips);
        mSuccessView = (ImageView) mRootView.findViewById(R.id.liveness_success_image);

        mImageLayout = (LinearLayout) mRootView.findViewById(R.id.liveness_result_image_layout);
        mImageLayout2 = (LinearLayout) mRootView.findViewById(R.id.liveness_result_image_layout2);
        mRelativeAddImageView = (RelativeLayout) mRootView.findViewById(R.id.relative_add_image_view);
        addImageView();
        mViewBg = findViewById(R.id.view_live_bg);
        initData();
    }

    private void initData() {
        // 初始化录制时间（默认15s）
        mTimeVideoRecord = mFaceConfig.getRecordVideoTime();
        if (mTimeVideoRecord > 15 * 1000) {
            mTimeVideoRecord = 15 * 1000;
        }
    }

    /**
     * 设置屏幕亮度
     */
    private void setScreenBright() {
        BrightnessUtils.setBrightness(this, 255);
    }

    /**
     * 动态加载ImageView
     */
    private void addImageView() {
        mFaceDetectRoundView.post(new Runnable() {
            @Override
            public void run() {
                mImageAnim = new ImageView(FaceLivenessVideoActivity.this);
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
            }
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

    @Override
    public void volumeChanged() {
        try {
            AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            if (am != null) {
                int cv = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                mIsEnableSound = cv > 0;
                mSoundView.setImageResource(mIsEnableSound
                        ? R.mipmap.icon_titlebar_voice2 : R.mipmap.icon_titlebar_voice_close);
                if (mILivenessStrategy != null) {
                    mILivenessStrategy.setLivenessStrategySoundEnable(mIsEnableSound);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    @Override
    public void onPostExecute(Integer result) {
        if (result == 0) {
            if (mRecordHandler == null) {
                mRecordHandler = new Handler();
            }
            mRecordHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    releaseMediaRecorder();
                }
            }, mTimeVideoRecord + 1000);
        }
    }

//    class MediaPrepareTask extends AsyncTask<Boolean, Void, Integer> {
//
//        @Override
//        protected Integer doInBackground(Boolean... starts) {
//            if (isCancelled()) {
//                return -1;
//            }
//
//            if (!starts[0]) {
//                mIsStartPreviewSuccess = startPreview();
//                return -1;
//            } else {
//                // if (startPreview()) {
//                if (mIsStartPreviewSuccess) {
//                    try {
//                        mMediaRecorder.start();
//                    } catch (Exception e) {
//                        System.err.println(e.getMessage());
//                        if (mRecordHandler != null) {
//                            mRecordHandler.sendEmptyMessage(RECORD_OPEN_ERROR);
//                        }
//                    }
//                } else {
//                    releaseMediaRecorder();
//                    return -1;
//                }
//                return 0;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Integer result) {
//            if (result == 0) {
//                if (mRecordHandler == null) {
//                    mRecordHandler = new Handler();
//                }
//                mRecordHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        releaseMediaRecorder();
//                    }
//                }, mTimeVideoRecord + 1000);
//            }
//        }
//    }

    @Override
    public void onLivenessCompletion(final FaceStatusNewEnum status, final String message,
                                     HashMap<String, ImageInfo> base64ImageCropMap,
                                     HashMap<String, ImageInfo> base64ImageSrcMap,
                                     final int currentLivenessCount, float livenessScore) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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

    // 加载动画
    private void loadAnimSource() {
        if (mLivenessType != null) {
            switch (mLivenessType) {
                case Eye:
                    mImageAnim.setBackgroundResource(R.drawable.anim_eye);
                    break;
                case Mouth:
                    mImageAnim.setBackgroundResource(R.drawable.anim_mouth);
                    break;
                default:
                    break;
            }
            mAnimationDrawable = (AnimationDrawable) mImageAnim.getBackground();
            mAnimationDrawable.start();
        }
    }

    private static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64Utils.decode(base64Data, Base64Utils.NO_WRAP);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
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
    public void setCurrentLiveType(LivenessTypeEnum liveType) {
        mLivenessType = liveType;
    }

    @Override
    public void viewReset() {
        mFaceDetectRoundView.setProcessCount(0, 1);
    }

    @Override
    public void animStop() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stopAnim();
            }
        });
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mFaceAuraColorView == null) {
                    return;
                }

                int preBag = -1;
                if (preColor == 0) {
                    preBag = getResources().getColor(R.color.aura_blue);
                } else if (preColor == 1) {
                    preBag = getResources().getColor(R.color.aura_green);
                } else if (preColor == 2) {
                    preBag = getResources().getColor(R.color.aura_red);
                }

                switch (currentColor) {
                    case -1:
                        updateOriIcon();
                        mFaceAuraColorView.setVisibility(View.GONE);
                        break;
                    case 0:
                        updateWhiteIcon();
                        mFaceAuraColorView.setVisibility(View.VISIBLE);
                        mFaceAuraColorView.start(getResources().getColor(R.color.aura_blue));
                        mFaceAuraColorView.setColorBg(preBag);
                        break;
                    case 1:
                        updateWhiteIcon();
                        mFaceAuraColorView.setVisibility(View.VISIBLE);
                        mFaceAuraColorView.start(getResources().getColor(R.color.aura_green));
                        mFaceAuraColorView.setColorBg(preBag);
                        break;
                    case 2:
                        updateWhiteIcon();
                        mFaceAuraColorView.setVisibility(View.VISIBLE);
                        mFaceAuraColorView.start(getResources().getColor(R.color.aura_red));
                        mFaceAuraColorView.setColorBg(preBag);
                        break;
                    case 3:
                        updateOriIcon();
                        mFaceAuraColorView.setVisibility(View.VISIBLE);
                        mFaceAuraColorView.start(getResources().getColor(R.color.aura_default));
                        mFaceAuraColorView.setColorBg(preBag);
                        break;
                    default:
                        updateOriIcon();
                        mFaceAuraColorView.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    private void updateWhiteIcon() {
        mSoundView.setImageResource(mIsEnableSound ?
                R.mipmap.icon_titlebar_voice2_white : R.mipmap.icon_titlebar_voice_close_white);
        mCloseView.setImageResource(R.mipmap.icon_titlebar_close_white);
        mFaceAuraColorView.setTextColor(Color.WHITE);
    }

    private void updateOriIcon() {
        mSoundView.setImageResource(mIsEnableSound ?
                R.mipmap.icon_titlebar_voice2 : R.mipmap.icon_titlebar_voice_close);
        mCloseView.setImageResource(R.mipmap.icon_titlebar_close);
        mFaceAuraColorView.setTextColor(Color.BLACK);
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

    /**
     * 获取视频保存目录
     */
    protected String getCurrentSaveName() {
        if (TextUtils.isEmpty(currentSaveName)) {
            // File path = FileUtils.getSDRootFile();
            File path = getFilesDir();
            if (path == null) {
                return null;
            }
            File dir = new File(path.toString() + VIDEO_FILE_DIC);
            if (!dir.exists()) {
                dir.mkdir();
            }
            videoExName = "/" + TimeManager.getDate() + ".mp4";
            currentSaveName = dir + videoExName;
        }
        return currentSaveName;
    }

    /**
     * 删除视频
     */
    private void deleteVideo() {
        File file = new File(getFilesDir() + VIDEO_FILE_DIC);
        FileUtils.deleteDir(file);
    }

    // ----------------------------------------供调试用----------------------------------------------
    private void saveAllImage(HashMap<String, ImageInfo> imageCropMap, HashMap<String, ImageInfo> imageSrcMap) {
        if (imageCropMap != null && imageCropMap.size() > 0) {
            List<Map.Entry<String, ImageInfo>> list1 = new ArrayList<>(imageCropMap.entrySet());
            Collections.sort(list1, new Comparator<Map.Entry<String, ImageInfo>>() {

                @Override
                public int compare(Map.Entry<String, ImageInfo> o1,
                                   Map.Entry<String, ImageInfo> o2) {
                    String[] key1 = o1.getKey().split("_");
                    String score1 = key1[2];
                    String[] key2 = o2.getKey().split("_");
                    String score2 = key2[2];
                    // 降序排序
                    return Float.valueOf(score2).compareTo(Float.valueOf(score1));
                }
            });
            setImageView1(list1);
        }

        if (imageSrcMap != null && imageSrcMap.size() > 0) {
            List<Map.Entry<String, ImageInfo>> list2 = new ArrayList<>(imageSrcMap.entrySet());
            Collections.sort(list2, new Comparator<Map.Entry<String, ImageInfo>>() {

                @Override
                public int compare(Map.Entry<String, ImageInfo> o1,
                                   Map.Entry<String, ImageInfo> o2) {
                    String[] key1 = o1.getKey().split("_");
                    String score1 = key1[2];
                    String[] key2 = o2.getKey().split("_");
                    String score2 = key2[2];
                    // 降序排序
                    return Float.valueOf(score2).compareTo(Float.valueOf(score1));
                }
            });
            setImageView2(list2);
        }
    }

    private void setImageView1(List<Map.Entry<String, ImageInfo>> list) {
        Bitmap bmp = null;
        mImageLayout.removeAllViews();
        for (Map.Entry<String, ImageInfo> entry : list) {
            bmp = base64ToBitmap(entry.getValue().getBase64());
            ImageView iv = new ImageView(this);
            iv.setImageBitmap(bmp);
            mImageLayout.addView(iv, new LinearLayout.LayoutParams(300, 300));
        }
    }

    private void setImageView2(List<Map.Entry<String, ImageInfo>> list) {
        Bitmap bmp = null;
        mImageLayout2.removeAllViews();
        for (Map.Entry<String, ImageInfo> entry : list) {
            bmp = base64ToBitmap(entry.getValue().getBase64());
            ImageView iv = new ImageView(this);
            iv.setImageBitmap(bmp);
            mImageLayout2.addView(iv, new LinearLayout.LayoutParams(300, 300));
        }
    }
}
