package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.huawei.hms.image.vision.ImageVision;
import com.huawei.hms.image.vision.ImageVisionImpl;
import com.huawei.hms.image.vision.bean.ImageVisionResult;
import com.huawei.secure.android.common.util.LogsUtil;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.lcw.library.imagepicker.ImagePicker;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.app.utils.GlideLoader;
import com.zqw.mobile.grainfull.di.component.DaggerHmsPhotoFilterComponent;
import com.zqw.mobile.grainfull.mvp.contract.HmsPhotoFilterContract;
import com.zqw.mobile.grainfull.mvp.presenter.HmsPhotoFilterPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import timber.log.Timber;

/**
 * Description: 滤镜服务
 * <p>
 * Created on 2024/12/05 15:44
 *
 * @author 赤槿
 * module name is HmsPhotoFilterActivity
 */
@RuntimePermissions
public class HmsPhotoFilterActivity extends BaseActivity<HmsPhotoFilterPresenter> implements HmsPhotoFilterContract.View {
    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.rdg_hmsphotofilter_group)
    RadioGroup radioGroup;

    @BindView(R.id.imvi_hmsphotofilter_primary)
    ImageView imagePrimary;

    @BindView(R.id.imvi_hmsphotofilter_result)
    ImageView imageResult;

    /*------------------------------------------业务信息------------------------------------------*/
    // 对话框
    private MaterialDialog mDialog;
    private Bitmap bitmap;
    private ImageVisionImpl imageVisionFilterAPI;
    private int initCodeState = -2;
    private int stopCodeState = -2;
    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    // 请求结构
    private String string
            = "{\"projectId\":\"projectIdTest\",\"appId\":\"appIdTest\",\"authApiKey\":\"authApiKeyTest\",\"clientSecret\":\"clientSecretTest\",\"clientId\":\"clientIdTest\",\"token\":\"tokenTest\"}";

    @Override
    public void onDestroy() {
        if (mDialog != null) {
            this.mDialog.dismiss();
        }
        stopFilter();
        super.onDestroy();
        this.bitmap = null;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHmsPhotoFilterComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_hms_photo_filter;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("滤镜服务");

        // 初始化Loading对话框
        mDialog = new MaterialDialog.Builder(this).content(R.string.common_execute).progress(true, 0).build();

        initFilter();
    }

    /**
     * 初始化
     */
    private void initFilter() {
        imageVisionFilterAPI = ImageVision.getInstance(this);
        imageVisionFilterAPI.setVisionCallBack(new ImageVision.VisionCallBack() {
            @Override
            public void onSuccess(int successCode) {
                int initCode = imageVisionFilterAPI.init(getApplicationContext());
                initCodeState = initCode;
                stopCodeState = -2;
            }

            @Override
            public void onFailure(int errorCode) {
                showMessage("初始化失败！");
                Timber.e("ImageVisionAPI fail, errorCode: %s", errorCode);
            }
        });
    }

    @OnClick({
            R.id.imvi_hmsphotofilter_primary_icon,                                                  // 选择图片
            R.id.txvi_hmsphotofilter_primary_txt,
            R.id.imvi_hmsphotofilter_primary,
            R.id.btn_hmsphotofilter_submit,                                                         // 获取结果
    })
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imvi_hmsphotofilter_primary_icon:                                             // 选择图片
            case R.id.txvi_hmsphotofilter_primary_txt:
            case R.id.imvi_hmsphotofilter_primary:
                HmsPhotoFilterActivityPermissionsDispatcher.getCameraWithPermissionCheck(this);
                break;
            case R.id.btn_hmsphotofilter_submit:                                                    // 获取结果
                onSubmit();
                break;
        }
    }

    private void onSubmit() {
        if (initCodeState != 0 | stopCodeState == 0) {
            showMessage("服务尚未初始化。请在调用服务之前对其进行初始化。");
            return;
        }
        JSONObject authJson = null;
        try {
            authJson = new JSONObject(string);
        } catch (JSONException e) {
            Timber.e("filter exp%s", e.getMessage());
        }

        startFilter(getRadioGroup(), "1", "1", authJson);
    }

    /**
     * 停止滤镜
     */
    private void stopFilter() {
        if (null != imageVisionFilterAPI) {
            int stopCode = imageVisionFilterAPI.stop();
            bitmap = null;
            imageVisionFilterAPI = null;
            stopCodeState = stopCode;
        } else {
            showMessage("该服务尚未启用。");
            stopCodeState = 0;
        }
    }

    /**
     * 开始滤镜
     *
     * @param filterType 颜色映射的图片索引，索引范围[0,24]（0为原图）。
     * @param intensity  滤镜强度，取值范围[0,1.0]，默认为1.0。
     * @param compress   压缩率，取值范围（0,1.0]，默认为1.0。
     */
    private void startFilter(final String filterType, final String intensity, final String compress, final JSONObject authJson) {
        Runnable runnable = () -> {
            JSONObject jsonObject = new JSONObject();
            JSONObject taskJson = new JSONObject();
            try {
                taskJson.put("intensity", intensity);
                taskJson.put("filterType", filterType);
                taskJson.put("compressRate", compress);
                jsonObject.put("requestId", "1");
                jsonObject.put("taskJson", taskJson);
                jsonObject.put("authJson", authJson);
                Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(newBitmap);
                canvas.drawColor(0xFFFAFAFA);
                canvas.drawBitmap(bitmap, 0, 0, null);
                final ImageVisionResult visionResult = imageVisionFilterAPI.getColorFilter(jsonObject,
                        newBitmap);
                imageResult.post(() -> {
                    Bitmap image = visionResult.getImage();
                    imageResult.setImageBitmap(image);
//                        tv.setText(visionResult.getResponse().toString() + "resultCode:" + visionResult.getResultCode());
                });
            } catch (JSONException e) {
                LogsUtil.e(TAG, "JSONException: " + e.getMessage());
            }
        };
        executorService.execute(runnable);
    }

    /**
     * 获取 过滤器类型
     */
    private String getRadioGroup() {
        RadioButton button = findViewById(radioGroup.getCheckedRadioButtonId());
        if (button == null) {
            return "1";
        }
        return (String) button.getTag();
    }

    /**
     * 选择图片
     */
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void getCamera() {
        ImagePicker.getInstance()
                .setTitle("图片")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .filterGif(false)//设置是否过滤gif图片
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
            // 照片
            if (requestCode == Constant.REQUEST_SELECT_IMAGES_CODE) {
                bitmap = CommonUtils.getBitmapFromUri(data, this);
                imagePrimary.setImageBitmap(bitmap);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HmsPhotoFilterActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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