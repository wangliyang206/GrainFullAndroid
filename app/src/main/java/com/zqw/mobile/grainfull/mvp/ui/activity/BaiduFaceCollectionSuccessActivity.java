package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.ui.utils.IntentUtils;
import com.baidu.idl.face.platform.ui.widget.CircleImageView;
import com.baidu.idl.face.platform.utils.Base64Utils;
import com.baidu.idl.face.platform.utils.BitmapUtils;
import com.baidu.idl.face.platform.utils.DensityUtils;
import com.baidu.idl.face.platform.utils.FileUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerBaiduFaceCollectionSuccessComponent;
import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceCollectionSuccessContract;
import com.zqw.mobile.grainfull.mvp.presenter.BaiduFaceCollectionSuccessPresenter;

import java.io.File;

import butterknife.BindView;

/**
 * Description:百度AI - 人脸识别 采集成功
 * <p>
 * Created on 2022/07/12 18:16
 *
 * @author 赤槿
 * module name is BaiduFaceCollectionSuccessActivity
 */
public class BaiduFaceCollectionSuccessActivity extends BaseActivity<BaiduFaceCollectionSuccessPresenter> implements BaiduFaceCollectionSuccessContract.View {

    @BindView(R.id.circle_head)
    CircleImageView mCircleHead;

    @BindView(R.id.image_circle)
    ImageView mImageCircle;

    @BindView(R.id.image_star)
    ImageView mImageStar;

    protected String mDestroyType;

    /**
     * 根据主题使用不同的颜色。
     * 如果想要纯透明，则需要重写此方法，返回值为 -1 即可。
     */
    public int useStatusBarColor() {
        return -1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IntentUtils.getInstance().release();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBaiduFaceCollectionSuccessComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_baidu_face_collection_success;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initData();

    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mDestroyType = intent.getStringExtra("destroyType");
            String bmpStr = IntentUtils.getInstance().getBitmap();
            if (TextUtils.isEmpty(bmpStr)) {
                return;
            }
            Bitmap bmp = base64ToBitmap(bmpStr);
            saveImage(bmp);
            bmp = FaceSDKManager.getInstance().scaleImage(bmp,
                    DensityUtils.dip2px(getApplicationContext(), 97),
                    DensityUtils.dip2px(getApplicationContext(), 97));
            mCircleHead.setImageBitmap(bmp);
        }

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mImageCircle.setVisibility(View.VISIBLE);
//                mImageStar.setVisibility(View.VISIBLE);
//            }
//        }, 500);
    }

    // 回到首页
    public void onReturnHome(View v) {
        if ("BaiduFaceLivenessExpActivity".equals(mDestroyType)) {
            BaiduFaceRecognitionActivity.destroyActivity("BaiduFaceLivenessExpActivity");
        }
        if ("BaiduFaceDetectExpActivity".equals(mDestroyType)) {
            BaiduFaceRecognitionActivity.destroyActivity("BaiduFaceDetectExpActivity");
        }
        finish();
    }

    // 重新采集
    public void onRecollect(View v) {
        finish();
    }

    private Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64Utils.decode(base64Data, Base64Utils.NO_WRAP);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void saveImage(final Bitmap bitmap) {
        // 保存图片
        Runnable runnable = () -> {

            try {
                String path = getFrameSavePath("1");
                if (path == null) {
                    return;
                }
                BitmapUtils.saveBitmap(new File(path), bitmap);
                bitmap.recycle();
            } catch (Exception e) {
                System.err.print(e.getMessage());
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * 获取抽帧图片保存目录
     */
    private String getFrameSavePath(String fileName) {
        File path = FileUtils.getSDRootFile();
        if (path == null) {
            return null;
        }
        File dir = new File(path.toString() + "/image");
        if (!dir.exists()) {
            dir.mkdir();
        }
        String imagePath = dir + "/" + fileName + ".jpg";
        return imagePath;
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