package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.common.MLException;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.faceverify.MLFaceTemplateResult;
import com.huawei.hms.mlsdk.faceverify.MLFaceVerificationAnalyzer;
import com.huawei.hms.mlsdk.faceverify.MLFaceVerificationAnalyzerFactory;
import com.huawei.hms.mlsdk.faceverify.MLFaceVerificationAnalyzerSetting;
import com.huawei.hms.mlsdk.faceverify.MLFaceVerificationResult;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.lcw.library.imagepicker.ImagePicker;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.BitmapUtils;
import com.zqw.mobile.grainfull.app.utils.GlideLoader;
import com.zqw.mobile.grainfull.di.component.DaggerFaceComparisonComponent;
import com.zqw.mobile.grainfull.mvp.contract.FaceComparisonContract;
import com.zqw.mobile.grainfull.mvp.presenter.FaceComparisonPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import timber.log.Timber;

/**
 * Description:人脸对比
 * <p>
 * Created on 2022/07/19 14:20
 *
 * @author 赤槿
 * module name is FaceComparisonActivity
 */
@RuntimePermissions
public class FaceComparisonActivity extends BaseActivity<FaceComparisonPresenter> implements FaceComparisonContract.View {
    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.imvi_facecomparison_leftcontent)
    ImageView imageLeft;

    @BindView(R.id.imvi_facecomparison_rightcontent)
    ImageView imageRight;

    @BindView(R.id.btn_facecomparison_compared)
    Button btnCompared;

    @BindView(R.id.txvi_facecomparison_result)
    TextView txviResult;
    /*------------------------------------------业务信息------------------------------------------*/

    // 模板图片
    private Bitmap templateBitmap;
    private Bitmap templateBitmapCopy;

    // 对比图片
    private Bitmap compareBitmap;
    private Bitmap compareBitmapCopy;

    /**
     * 图片对比操作对象
     */
    private MLFaceVerificationAnalyzer analyzer;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BitmapUtils.recycleBitmap(templateBitmap);
        BitmapUtils.recycleBitmap(templateBitmapCopy);
        BitmapUtils.recycleBitmap(compareBitmap);
        BitmapUtils.recycleBitmap(compareBitmapCopy);

        if (this.analyzer != null) {
            this.analyzer.stop();
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFaceComparisonComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_face_comparison;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("人脸对比");

        initAnalyzer();
    }

    private void initAnalyzer() {
        MLFaceVerificationAnalyzerSetting.Factory factory = new MLFaceVerificationAnalyzerSetting.Factory().setMaxFaceDetected(3);
        MLFaceVerificationAnalyzerSetting setting = factory.create();
        analyzer = MLFaceVerificationAnalyzerFactory
                .getInstance()
                .getFaceVerificationAnalyzer(setting);
    }

    @OnClick({
            R.id.cola_facecomparison_left,                                                          // 点击添加模板图片
            R.id.cola_facecomparison_right,                                                         // 点击添加对比图片
            R.id.btn_facecomparison_compared,                                                       // 对比按钮
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cola_facecomparison_left:                                                     // 点击添加模板图片
                BitmapUtils.recycleBitmap(templateBitmap);
                BitmapUtils.recycleBitmap(templateBitmapCopy);
                FaceComparisonActivityPermissionsDispatcher.getTemplateImageWithPermissionCheck(this);
                break;
            case R.id.cola_facecomparison_right:                                                    // 点击添加对比图片
                BitmapUtils.recycleBitmap(compareBitmap);
                BitmapUtils.recycleBitmap(compareBitmapCopy);
                FaceComparisonActivityPermissionsDispatcher.getComparePhotosWithPermissionCheck(this);
                break;
            case R.id.btn_facecomparison_compared:                                                  // 对比按钮
                onCompare();
                break;
        }
    }

    /**
     * 对比
     */
    private void onCompare() {
        final long startTime = System.currentTimeMillis();
        try {
            Task<List<MLFaceVerificationResult>> task = analyzer.asyncAnalyseFrame(MLFrame.fromBitmap(compareBitmap));
            final StringBuilder sb = new StringBuilder();
            sb.append("##获取人脸相似度");
            task.addOnSuccessListener(mlCompareList -> {
                long endTime = System.currentTimeMillis();
                sb.append("\n   处理时长[");
                sb.append(endTime - startTime);
                sb.append("毫秒]");

                if (mlCompareList.size() > 0) {
                    sb.append("成功!");
                } else {
                    sb.append("失败，对比图中未检测到人脸!");
                }

                for (MLFaceVerificationResult template : mlCompareList) {
                    Rect location = template.getFaceInfo().getFaceRect();
                    Canvas canvas = new Canvas(compareBitmapCopy);
                    Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    paint.setStyle(Paint.Style.STROKE);// Not Filled
                    paint.setStrokeWidth((location.right - location.left) / 50f);  // Line width
                    canvas.drawRect(location, paint);// framed
                    int id = template.getTemplateId();
                    float similarity = template.getSimilarity();
                    imageRight.setImageBitmap(compareBitmapCopy);
                    sb.append("\n   人脸位置[");
                    sb.append(location);
                    sb.append("]\n   ID[");
                    sb.append(id);
                    sb.append("]\n   相似度[");
                    sb.append(similarity);
                    sb.append("]\n");
                }
                sb.append("\n");
                txviResult.append(sb.toString());
            }).addOnFailureListener(e -> {
                long endTime = System.currentTimeMillis();
                sb.append("\n   处理时长[");
                sb.append(endTime - startTime);
                sb.append("毫秒]失败!");
                if (e instanceof MLException) {
                    MLException mlException = (MLException) e;
                    // Obtain error codes. Developers can process the error codes and display differentiated messages based on the error codes.
                    int errorCode = mlException.getErrCode();
                    // Obtain error information. Developers can quickly locate faults based on the error code.
                    String errorMessage = mlException.getMessage();
                    sb.append("\n   ErrorCode[");
                    sb.append(errorCode);
                    sb.append("]\n   Msg[");
                    sb.append(errorMessage);
                    sb.append("]");
                } else {
                    sb.append("\n   Error[");
                    sb.append(e.getMessage());
                    sb.append("]");
                }
                sb.append("\n");
                txviResult.append(sb.toString());
            });
        } catch (RuntimeException e) {
            Timber.e("设置包含人脸的图像进行比较。");
        }
    }

    /**
     * 加载模板图片
     */
    private void loadTemplatePic() {
        long startTime = System.currentTimeMillis();
        List<MLFaceTemplateResult> results = analyzer.setTemplateFace(MLFrame.fromBitmap(templateBitmap));
        long endTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("##设置模板图片\n   处理时长[");
        sb.append(endTime - startTime);
        sb.append("毫秒]");
        if (results.isEmpty()) {
            sb.append("失败，模板中无人脸!");
        } else {
            sb.append("成功!");
        }
        for (MLFaceTemplateResult template : results) {
            int id = template.getTemplateId();
            Rect location = template.getFaceInfo().getFaceRect();
            Canvas canvas = new Canvas(templateBitmapCopy);
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);// Not Filled
            paint.setStrokeWidth((location.right - location.left) / 50f);  // Line width
            canvas.drawRect(location, paint);// framed
            imageLeft.setImageBitmap(templateBitmapCopy);
            sb.append("\n   人脸位置[");
            sb.append(location);
            sb.append("]\n   ID[");
            sb.append(id);
            sb.append("]");
        }
        sb.append("\n");
        txviResult.setText(sb.toString());
    }

    /**
     * 选择模板图片
     */
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void getTemplateImage() {

        ImagePicker.getInstance()
                .setTitle("图片")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .filterGif(true)//设置是否过滤gif图片
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                .setSingleType(true)//设置图片视频不能同时选择
                //设置自定义图片加载器
                .setImageLoader(new GlideLoader(getApplicationContext()))
                .start(this, Constant.MAIN_AVATAR);
    }

    /**
     * 选择对比图片
     */
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void getComparePhotos() {

        ImagePicker.getInstance()
                .setTitle("图片")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .filterGif(true)//设置是否过滤gif图片
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                .setSingleType(true)//设置图片视频不能同时选择
                //设置自定义图片加载器
                .setImageLoader(new GlideLoader(getApplicationContext()))
                .start(this, Constant.REQUEST_SELECT_IMAGES_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ArrayList<String> mImage = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);

            // 模板图片
            if (requestCode == Constant.MAIN_AVATAR) {
                txviResult.setText("");

                templateBitmap = loadPic(mImage.get(0), imageLeft);
                templateBitmapCopy = templateBitmap.copy(Bitmap.Config.ARGB_8888, true);
                loadTemplatePic();
            }

            // 对比照片
            if (requestCode == Constant.REQUEST_SELECT_IMAGES_CODE) {
                // 显示图片
                compareBitmap = loadPic(mImage.get(0), imageRight);
                compareBitmapCopy = compareBitmap.copy(Bitmap.Config.ARGB_8888, true);
            }

            if (templateBitmap != null && compareBitmap != null) {
                btnCompared.setEnabled(true);
            }
        }
    }

    /**
     * 加载图片
     */
    private Bitmap loadPic(String val, ImageView view) {
        Bitmap pic = null;
        pic = BitmapUtils.loadFromPath(val, ((View) view.getParent()).getWidth(),
                ((View) view.getParent()).getHeight()).copy(Bitmap.Config.ARGB_8888, true);
        if (pic == null) {
            showMessage("请选择图片进行处理~");
        }
        view.setImageBitmap(pic);
        return pic;
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