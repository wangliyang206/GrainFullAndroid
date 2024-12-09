package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.huawei.hms.image.vision.crop.CropLayoutView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;
import com.lcw.library.imagepicker.ImagePicker;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.GlideLoader;
import com.zqw.mobile.grainfull.di.component.DaggerHmsCropImageComponent;
import com.zqw.mobile.grainfull.mvp.contract.HmsCropImageContract;
import com.zqw.mobile.grainfull.mvp.presenter.HmsCropImagePresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.GridRadioGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Description: 华为 - 裁剪图像
 * <p>
 * Created on 2024/12/09 10:18
 *
 * @author 赤槿
 * module name is HmsCropImageActivity
 */
@RuntimePermissions
public class HmsCropImageActivity extends BaseActivity<HmsCropImagePresenter> implements HmsCropImageContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.cropImageView)
    CropLayoutView cropLayoutView;                                                                  // 显示图片

    @BindView(R.id.ragr_hmscropimage_shape)
    RadioGroup radioShape;
    @BindView(R.id.ragr_hmscropimage_proportion)
    GridRadioGroup radioProportion;

    /*------------------------------------------------业务区域------------------------------------------------*/
    // 加载图片对象
    @Inject
    ImageLoader mImageLoader;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.mImageLoader = null;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHmsCropImageComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_hms_crop_image;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("裁剪图像");

        cropLayoutView.setAutoZoomEnabled(true);
        cropLayoutView.setCropShape(CropLayoutView.CropShape.RECTANGLE);
        radioShape.setOnCheckedChangeListener((group, checkedId) -> {
            if (group.getCheckedRadioButtonId() == R.id.radio_hmscropimage_rectangle) {
                cropLayoutView.setCropShape(CropLayoutView.CropShape.RECTANGLE);
            } else {
                cropLayoutView.setCropShape(CropLayoutView.CropShape.OVAL);
            }

        });

        radioProportion.setOnCheckedChangeListener((group, checkedId) -> {
            if (group.getCheckedRadioButtonId() == R.id.radio_hmscropimage_anyratio) {
                // 任意比率
                cropLayoutView.setFixedAspectRatio(false);
            } else if (group.getCheckedRadioButtonId() == R.id.radio_hmscropimage_one) {
                // 1:1
                cropLayoutView.setAspectRatio(1, 1);
            } else if (group.getCheckedRadioButtonId() == R.id.radio_hmscropimage_two) {
                // 9:16
                cropLayoutView.setAspectRatio(9, 16);
            } else if (group.getCheckedRadioButtonId() == R.id.radio_hmscropimage_three) {
                // 4:3
                cropLayoutView.setAspectRatio(4, 3);
            } else if (group.getCheckedRadioButtonId() == R.id.radio_hmscropimage_four) {
                // 16:9
                cropLayoutView.setAspectRatio(16, 9);
            }
        });
    }

    @OnClick({
            R.id.view_hmscropimage_open,                                                            // 选择图片
            R.id.btn_crop_image,                                                                    // 裁剪图片
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_hmscropimage_open:                                                       // 选择图片
                HmsCropImageActivityPermissionsDispatcher.addAvatarWithPermissionCheck(this);
                break;
            case R.id.btn_crop_image:                                                               // 裁剪图片
                onSaveImage();
                break;
        }
    }


    /**
     * 将图片进行保存
     */
    private void onSaveImage() {
        Bitmap croppedImage = cropLayoutView.getCroppedImage();
        if (croppedImage == null) {
            showMessage("未检测到结果图片！");
            return;
        }

        // 保存图片
        Runnable runnable = () -> {
            // 生成文件路径及名称
            String path = Constant.IMAGE_PATH + TimeUtils.getNowString(new SimpleDateFormat("yyyyMMdd_HHmmss")) + ".png";
            // 保存图片
            ImageUtils.save(croppedImage, path, Bitmap.CompressFormat.PNG);

            runOnUiThread(() -> {
                // 弹出成功提示
                showMessage("剪裁成功！路径：" + path);
            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    /**
     * 添加照片
     */
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void addAvatar() {

        ImagePicker.getInstance()
                .setTitle("图片")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .filterGif(true)//设置是否过滤gif图片
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                .setSingleType(true)//设置图片视频不能同时选择
                .setImageLoader(new GlideLoader(this))//设置自定义图片加载器
                .start(this, Constant.REQUEST_SELECT_IMAGES_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.REQUEST_SELECT_IMAGES_CODE) {
                ArrayList<String> mImg = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
                Bitmap mBitmap = BitmapFactory.decodeFile(mImg.get(0));

                cropLayoutView.setImageBitmap(mBitmap);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HmsCropImageActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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