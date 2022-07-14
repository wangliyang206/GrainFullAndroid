package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ImageUtils;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.text.MLLocalTextSetting;
import com.huawei.hms.mlsdk.text.MLText;
import com.huawei.hms.mlsdk.text.MLTextAnalyzer;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.lcw.library.imagepicker.ImagePicker;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.dialog.IdentifyDialog;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.app.utils.GlideLoader;
import com.zqw.mobile.grainfull.di.component.DaggerImageExtractionTextComponent;
import com.zqw.mobile.grainfull.mvp.contract.ImageExtractionTextContract;
import com.zqw.mobile.grainfull.mvp.presenter.ImageExtractionTextPresenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import timber.log.Timber;

/**
 * ================================================
 * Description:图片提取文字
 * <p>
 * Created by MVPArmsTemplate on 2022/07/08 16:11
 * ================================================
 */
@RuntimePermissions
public class ImageExtractionTextActivity extends BaseActivity<ImageExtractionTextPresenter> implements ImageExtractionTextContract.View {
    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.radio_imageextractiontext_group)
    RadioGroup radioGroup;

    @BindView(R.id.image_imageextractiontext_img)
    ImageView image;

    @BindView(R.id.btn_imageextractiontext_identify)
    Button btnIdentify;

    /*------------------------------------------业务信息------------------------------------------*/
    @Inject
    ImageLoader mImageLoader;
    // 用于临时保存图片地址
    private ArrayList<String> mImagePaths = new ArrayList<>();
    // 识别操作对象
    private MLTextAnalyzer analyzer;
    // 弹出结果框
    private IdentifyDialog mResult;
    // 对话框
    private MaterialDialog mDialog;

    @Override
    public void onDestroy() {
        if (mDialog != null) {
            this.mDialog.dismiss();
        }
        super.onDestroy();

        if (CommonUtils.isNotEmpty(mImagePaths)) {
            this.mImagePaths.clear();
            this.mImagePaths = null;
        }

        if (this.analyzer != null) {
            try {
                this.analyzer.stop();
            } catch (IOException e) {
                Timber.e("Stop failed: %s", e.getMessage());
            }
        }

        if (mResult != null) {
            mResult.dismiss();
            mResult = null;
        }

        this.mImageLoader = null;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerImageExtractionTextComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_image_extraction_text;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 设置标题
        setTitle("图片提取文字");

        initDialog();
    }

    /**
     * 初始化Dialog
     */
    private void initDialog() {
        // 初始化Loading对话框
        mDialog = new MaterialDialog.Builder(this).content(R.string.common_execute).progress(true, 0).build();
        // 初始化Result弹框
        mResult = new IdentifyDialog(this);
    }

    @OnClick({
            R.id.lila_imageextractiontext_tips,                                                     // 选择图片
            R.id.btn_imageextractiontext_identify,                                                  // 识别
    })
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.lila_imageextractiontext_tips:                                                // 选择图片
                ImageExtractionTextActivityPermissionsDispatcher.getCameraWithPermissionCheck(this);
                break;
            case R.id.btn_imageextractiontext_identify:                                             // 识别
                // 开始识别
                localAnalyzer(mImagePaths.get(0));
                break;
        }
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
                .filterGif(true)//设置是否过滤gif图片
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                .setSingleType(true)//设置图片视频不能同时选择
                .setImagePaths(mImagePaths)
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
                // 清空以前图片
                mImagePaths.clear();
                // 接收当前选中的图
                mImagePaths.addAll(data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES));
                // 显示图片
                mImageLoader.loadImage(this,
                        ImageConfigImpl.builder().url(mImagePaths.get(0))
                                .placeholder(R.mipmap.mis_default_error)
                                .errorPic(R.mipmap.mis_default_error)
                                .imageView(image).build());

                btnIdentify.setEnabled(true);
            }
        }
    }

    /**
     * 设备上的文本识别
     */
    private void localAnalyzer(String path) {
        mDialog.show();

        // Create the text analyzer MLTextAnalyzer to recognize characters in images. You can set MLLocalTextSetting to
        // specify languages that can be recognized.
        // If you do not set the languages, only Romance languages can be recognized by default.
        // Use default parameter settings to configure the on-device text analyzer. Only Romance languages can be
        // recognized.
        // analyzer = MLAnalyzerFactory.getInstance().getLocalTextAnalyzer();
        // Use the customized parameter MLLocalTextSetting to configure the text analyzer on the device.
        MLLocalTextSetting setting = new MLLocalTextSetting.Factory()
                .setOCRMode(MLLocalTextSetting.OCR_DETECT_MODE)
                .setLanguage(radioGroup.getCheckedRadioButtonId() == R.id.radio_imageextractiontext_zh ? "zh" : "en")
                .create();
        this.analyzer = MLAnalyzerFactory.getInstance()
                .getLocalTextAnalyzer(setting);
        // Create an MLFrame by using android.graphics.Bitmap.
        // 本地测试
//        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.text_image);
//        MLFrame frame = MLFrame.fromBitmap(bitmap);
        // 动态添加图片
        MLFrame frame = MLFrame.fromBitmap(ImageUtils.getBitmap(path));
        Task<MLText> task = this.analyzer.asyncAnalyseFrame(frame);
        task.addOnSuccessListener(text -> {
            // Recognition success.
            mDialog.dismiss();
            displaySuccess(text);
        }).addOnFailureListener(e -> {
            // Recognition failure.
            mDialog.dismiss();
            showMessage("提取失败！");
        });
    }

    private void displaySuccess(MLText mlText) {
        String result = "";
        List<MLText.Block> blocks = mlText.getBlocks();
        for (MLText.Block block : blocks) {
            for (MLText.TextLine line : block.getContents()) {
                result += line.getStringValue() + "\n";
            }
        }

        if (TextUtils.isEmpty(result)) {
            showMessage("未获得识别结果！");
        } else {
            mResult.show();
            mResult.setData(result);
        }
    }

    @Override
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