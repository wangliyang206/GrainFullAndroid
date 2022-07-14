/**
 * Copyright (C) 2017 Baidu Inc. All rights reserved.
 */
package com.baidu.idl.face.platform.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.FaceStatusNewEnum;
import com.baidu.idl.face.platform.IDetectStrategy;
import com.baidu.idl.face.platform.IDetectStrategyCallback;
import com.baidu.idl.face.platform.model.ImageInfo;
import com.baidu.idl.face.platform.ui.utils.BrightnessUtils;
import com.baidu.idl.face.platform.ui.utils.CameraUtils;
import com.baidu.idl.face.platform.ui.utils.VolumeUtils;
import com.baidu.idl.face.platform.ui.widget.FaceDetectRoundView;
import com.baidu.idl.face.platform.utils.APIUtils;
import com.baidu.idl.face.platform.utils.Base64Utils;
import com.baidu.idl.face.platform.ui.utils.CameraPreviewUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 人脸采集接口
 */
public class FaceDetectActivity extends Activity implements
        SurfaceHolder.Callback,
        Camera.PreviewCallback,
        Camera.ErrorCallback,
        VolumeUtils.VolumeCallback,
        IDetectStrategyCallback {

    public static final String TAG = FaceDetectActivity.class.getSimpleName();

    // View
    protected View mRootView;
    protected FrameLayout mFrameLayout;
    protected SurfaceView mSurfaceView;
    protected SurfaceHolder mSurfaceHolder;
    protected ImageView mCloseView;
    protected ImageView mSoundView;
    protected ImageView mSuccessView;
    protected TextView mTipsTopView;
    protected FaceDetectRoundView mFaceDetectRoundView;
    protected LinearLayout mImageLayout;
    protected LinearLayout mImageLayout2;
    public View mViewBg;
    // 人脸信息
    protected FaceConfig mFaceConfig;
    protected IDetectStrategy mIDetectStrategy;
    // 显示Size
    private Rect mPreviewRect = new Rect();
    protected int mDisplayWidth = 0;
    protected int mDisplayHeight = 0;
    protected int mSurfaceWidth = 0;
    protected int mSurfaceHeight = 0;
    protected Drawable mTipsIcon;
    // 状态标识
    protected volatile boolean mIsEnableSound = true;
    protected HashMap<String, String> mBase64ImageMap = new HashMap<String, String>();
    protected boolean mIsCreateSurface = false;
    protected volatile boolean mIsCompletion = false;
    // 相机
    protected Camera mCamera;
    protected Camera.Parameters mCameraParam;
    protected int mCameraId;
    protected int mPreviewWidth;
    protected int mPreviewHight;
    protected int mPreviewDegree;
    // 监听系统音量广播
    protected BroadcastReceiver mVolumeReceiver;
    // 是否弹窗
    protected boolean mHasShownTimeoutDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenBright();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_face_detect_v3100);
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

        mRootView = this.findViewById(R.id.detect_root_layout);
        mFrameLayout = (FrameLayout) mRootView.findViewById(R.id.detect_surface_layout);

        mSurfaceView = new SurfaceView(this);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setSizeFromLayout();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        int w = mDisplayWidth;
        int h = mDisplayHeight;

        // surfaceView使用屏幕分辨率的大小
//        FrameLayout.LayoutParams cameraFL = new FrameLayout.LayoutParams(
//                (int) (w * FaceDetectRoundView.SURFACE_RATIO), (int) (h * FaceDetectRoundView.SURFACE_RATIO),
//                Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        // surfaceView使用640*480的大小
        FrameLayout.LayoutParams cameraFL = new FrameLayout.LayoutParams(
                (int) (w * FaceDetectRoundView.SURFACE_RATIO * FaceDetectRoundView.RECT_RATIO),
                (int) (w * FaceDetectRoundView.SURFACE_RATIO * FaceDetectRoundView.RECT_RATIO * 640.0f / 480.0f),
                Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        mSurfaceView.setLayoutParams(cameraFL);
        mFrameLayout.addView(mSurfaceView);

        mRootView.findViewById(R.id.detect_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mFaceDetectRoundView = (FaceDetectRoundView) mRootView.findViewById(R.id.detect_face_round);
        mFaceDetectRoundView.setIsActiveLive(false);
        mCloseView = (ImageView) mRootView.findViewById(R.id.detect_close);
        mSoundView = (ImageView) mRootView.findViewById(R.id.detect_sound);
        mSoundView.setImageResource(mIsEnableSound ?
                R.mipmap.icon_titlebar_voice2 : R.drawable.collect_image_voice_selector);
        mSoundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsEnableSound = !mIsEnableSound;
                mSoundView.setImageResource(mIsEnableSound ?
                        R.mipmap.icon_titlebar_voice2 : R.drawable.collect_image_voice_selector);
                if (mIDetectStrategy != null) {
                    mIDetectStrategy.setDetectStrategySoundEnable(mIsEnableSound);
                }
            }
        });
        mTipsTopView = (TextView) mRootView.findViewById(R.id.detect_top_tips);
        mSuccessView = (ImageView) mRootView.findViewById(R.id.detect_success_image);

        mImageLayout = (LinearLayout) mRootView.findViewById(R.id.detect_result_image_layout);
        mImageLayout2 = (LinearLayout) mRootView.findViewById(R.id.detect_result_image_layout2);
        mViewBg = findViewById(R.id.view_bg);
        if (mBase64ImageMap != null) {
            mBase64ImageMap.clear();
        }
    }

    /**
     * 设置屏幕亮度
     */
    private void setScreenBright() {
        int currentBright = BrightnessUtils.getScreenBrightness(this);
        BrightnessUtils.setBrightness(this, currentBright + 100);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mHasShownTimeoutDialog) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mVolumeReceiver = VolumeUtils.registerVolumeReceiver(this, this);
            if (mFaceDetectRoundView != null) {
                mFaceDetectRoundView.setTipTopText("请将脸移入取景框");
            }
            startPreview();
        }
    }

    @Override
    public void onPause() {
        if (mIDetectStrategy != null) {
            mIDetectStrategy.reset();
        }
        super.onPause();
        VolumeUtils.unRegisterVolumeReceiver(this, mVolumeReceiver);
        mVolumeReceiver = null;
        mIsCompletion = false;
        stopPreview();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void volumeChanged() {
        try {
            AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            if (am != null) {
                int cv = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                mIsEnableSound = cv > 0;
                mSoundView.setImageResource(mIsEnableSound
                        ? R.mipmap.icon_titlebar_voice2 : R.mipmap.icon_titlebar_voice1);
                if (mIDetectStrategy != null) {
                    mIDetectStrategy.setDetectStrategySoundEnable(mIsEnableSound);
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

    protected void startPreview() {
        if (mSurfaceView != null && mSurfaceView.getHolder() != null) {
            mSurfaceHolder = mSurfaceView.getHolder();
            mSurfaceHolder.addCallback(this);
        }

        if (mCamera != null) {
            CameraUtils.releaseCamera(mCamera);
            mCamera = null;
        }

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
            return;
        }
        if (mCameraParam == null) {
            mCameraParam = mCamera.getParameters();
        }

        mCameraParam.setPictureFormat(PixelFormat.JPEG);

        // 获取前置摄像头预览角度，为90度
        int degree = displayOrientation(this);
        mCamera.setDisplayOrientation(degree);
        // 设置后无效，camera.setDisplayOrientation方法有效
        mCameraParam.set("rotation", degree);
        mPreviewDegree = degree;
        if (mIDetectStrategy != null) {
            mIDetectStrategy.setPreviewDegree(degree);
        }

        // 以屏幕分辨率为基准选取分辨率
//        Point point = CameraPreviewUtils.getBestPreview(mCameraParam,
//                new Point(mDisplayWidth, mDisplayHeight));
        // 以640 * 480为基准选取分辨率
        Point point = CameraPreviewUtils.getBestPreview(mCameraParam,
                new Point(640, 480));

        mPreviewWidth = point.x;
        mPreviewHight = point.y;
        // Preview 768,432
        mPreviewRect.set(0, 0, mPreviewHight, mPreviewWidth);

        mCameraParam.setPreviewSize(mPreviewWidth, mPreviewHight);
        mCamera.setParameters(mCameraParam);

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.stopPreview();
            mCamera.setErrorCallback(this);
            mCamera.setPreviewCallback(this);
            mCamera.startPreview();
        } catch (RuntimeException e) {
            e.printStackTrace();
            CameraUtils.releaseCamera(mCamera);
            mCamera = null;
        } catch (Exception e) {
            e.printStackTrace();
            CameraUtils.releaseCamera(mCamera);
            mCamera = null;
        }

    }

    protected void stopPreview() {
        if (mCamera != null) {
            try {
                mCamera.setErrorCallback(null);
                mCamera.setPreviewCallback(null);
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
        if (mSurfaceHolder != null) {
            mSurfaceHolder.removeCallback(this);
        }
        if (mIDetectStrategy != null) {
            mIDetectStrategy.reset();
            mIDetectStrategy = null;
        }
    }

    /**
     * 获取摄像头预览角度
     * @param context 当前上下文
     * @return
     */
    private int displayOrientation(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null) {
            return 90;
        }

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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsCreateSurface = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,
                               int format,
                               int width,
                               int height) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        if (holder.getSurface() == null) {
            return;
        }
        startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsCreateSurface = false;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        if (mIsCompletion) {
            return;
        }

        if (mIDetectStrategy == null && mFaceDetectRoundView != null && mFaceDetectRoundView.getRound() > 0) {
            mIDetectStrategy = FaceSDKManager.getInstance().getDetectStrategyModule();
            mIDetectStrategy.setPreviewDegree(mPreviewDegree);
            mIDetectStrategy.setDetectStrategySoundEnable(mIsEnableSound);

            Rect detectRect = FaceDetectRoundView.getPreviewDetectRect(mDisplayWidth, mPreviewHight, mPreviewWidth);
            mIDetectStrategy.setDetectStrategyConfig(mPreviewRect, detectRect, this);
        }
        if (mIDetectStrategy != null) {
            mIDetectStrategy.detectStrategy(data);
        }
    }

    @Override
    public void onError(int error, Camera camera) {
    }

    @Override
    public void onDetectCompletion(FaceStatusNewEnum status, String message,
                                   HashMap<String, ImageInfo> base64ImageCropMap,
                                   HashMap<String, ImageInfo> base64ImageSrcMap) {
        if (mIsCompletion) {
            return;
        }

        onRefreshView(status, message);

        if (status == FaceStatusNewEnum.OK) {
            mIsCompletion = true;
            // saveAllImage(base64ImageCropMap, base64ImageSrcMap);
        }
    }

    private void onRefreshView(FaceStatusNewEnum status, String message) {
        switch (status) {
            case OK:
                mFaceDetectRoundView.setTipTopText(message);
                // onRefreshSuccessView(true);
                // onRefreshTipsView(false, message);
                break;
            case DetectRemindCodePitchOutofUpRange:
            case DetectRemindCodePitchOutofDownRange:
            case DetectRemindCodeYawOutofLeftRange:
            case DetectRemindCodeYawOutofRightRange:
                mFaceDetectRoundView.setTipTopText(message);
                // onRefreshTipsView(true, message);
                // onRefreshSuccessView(false);
                break;
            default:
                mFaceDetectRoundView.setTipTopText(message);
                // onRefreshTipsView(false, message);
                // onRefreshSuccessView(false);
        }
    }

    private static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64Utils.decode(base64Data, Base64Utils.NO_WRAP);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
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
