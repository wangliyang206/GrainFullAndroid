package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;
import com.lcw.library.imagepicker.ImagePicker;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.app.utils.GlideLoader;
import com.zqw.mobile.grainfull.di.component.DaggerPictureCompressionComponent;
import com.zqw.mobile.grainfull.mvp.contract.PictureCompressionContract;
import com.zqw.mobile.grainfull.mvp.presenter.PictureCompressionPresenter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Description:图片压缩
 * <p>
 * Created on 2023/02/15 13:51
 *
 * @author 赤槿
 * module name is PictureCompressionActivity
 */
@RuntimePermissions
public class PictureCompressionActivity extends BaseActivity<PictureCompressionPresenter> implements PictureCompressionContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_picture_compression)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.radio_picturecompression_group)
    RadioGroup radioGroup;                                                                          // 类型
    @BindView(R.id.txvi_picturecompression_tips)
    TextView txviTips;                                                                              // 提示

    @BindView(R.id.view_picturecompression_compression)
    LinearLayout btnCompression;                                                                    // 压缩按钮
    @BindView(R.id.imvi_picturecompression_image)
    ImageView imviContent;                                                                          // 显示图片
    @BindView(R.id.lila_picturecompression_data)
    LinearLayout lilaDataLayout;                                                                    // 数据总布局

    @BindView(R.id.txvi_picturecompression_before_size)
    TextView txviBeforeSize;                                                                        // 压缩前 - 图片尺寸
    @BindView(R.id.txvi_picturecompression_after_size)
    TextView txviAfterSize;                                                                         // 压缩后 - 图片尺寸
    @BindView(R.id.txvi_picturecompression_before_howbig)
    TextView txviBeforeHowBig;                                                                      // 压缩前 - 图片大小
    @BindView(R.id.txvi_picturecompression_after_howbig)
    TextView txviAfterHowBig;                                                                       // 压缩后 - 图片大小
    @BindView(R.id.txvi_picturecompression_path)
    TextView txviPath;                                                                              // 压缩后保存的路径
    /*------------------------------------------------业务区域------------------------------------------------*/
    // 加载图片对象
    @Inject
    ImageLoader mImageLoader;

    // 对话框
    private MaterialDialog mDialog;

    // 选中的图片
    private ArrayList<String> mImage;

    @Override
    protected void onDestroy() {
        this.mImage = null;
        if (mDialog != null) {
            this.mDialog.dismiss();
        }
        super.onDestroy();

        this.mDialog = null;
        this.mImageLoader = null;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPictureCompressionComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_picture_compression;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("图片压缩");

        // 初始化Loading对话框
        mDialog = new MaterialDialog.Builder(this).content(R.string.common_execute).progress(true, 0).build();

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (group.getCheckedRadioButtonId() == R.id.radio_picturecompression_size) {
                txviTips.setText("说明：低于500kB的照片不支持压缩。");
            } else {
                txviTips.setText("说明：将图片尺寸压缩20%。");
            }
        });

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "picture_compression");
    }

    @OnClick({
            R.id.view_picturecompression_open,                                                      // 添加图片
            R.id.view_picturecompression_compression,                                               // 压缩图片
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_picturecompression_open:                                                 // 添加图片
                PictureCompressionActivityPermissionsDispatcher.addAvatarWithPermissionCheck(this);
                break;
            case R.id.view_picturecompression_compression:                                          // 压缩图片
                onCompression();
                break;
        }
    }

    /**
     * 图片压缩
     */
    private void onCompression() {
        if (CommonUtils.isNotEmpty(mImage)) {
            if (radioGroup.getCheckedRadioButtonId() == R.id.radio_picturecompression_size) {
                // 按大小
                compressImage(mImage);
            } else {
                // 按比例
                if (mDialog != null)
                    mDialog.show();
                new Thread(() -> {
                    // 缩放
                    Bitmap mBitmap = ImageUtils.compressByScale(BitmapFactory.decodeFile(mImage.get(0)), 0.8f, 0.8f);
                    // 生成文件路径及名称
                    String path = Constant.IMAGE_PATH + TimeUtils.getNowString(new SimpleDateFormat("yyyyMMdd_HHmmss")) + ".png";
                    // 保存图片
                    ImageUtils.save(mBitmap, path, Bitmap.CompressFormat.PNG);

                    runOnUiThread(() -> {
                        if (mDialog != null)
                            mDialog.cancel();
                        // 压缩成功后调用，返回压缩后的图片文件
                        // 显示图片路径
                        txviPath.setText("压缩后保存路径：" + path);

                        // 获取图片尺寸
                        int[] imageSize = ImageUtils.getSize(path);
                        // 获取图片大小
//                        String fileSize = FileUtils.getSize(path);
                        long fileLength = FileUtils.getLength(path);
                        String fileSize = Formatter.formatFileSize(getApplicationContext(), fileLength);
                        txviAfterSize.setText(imageSize[0] + "*" + imageSize[1]);
                        txviAfterHowBig.setText(fileSize);
                    });
                }).start();

            }
        } else {
            showMessage("请先选择图片！");
        }

    }


    /**
     * 按大小压缩图片
     */
    private void compressImage(ArrayList<String> mImg) {
        Luban.with(this)
                // 传入原图
                .load(mImg)
                // 不压缩的阈值，单位为K
                .ignoreBy(500)
                // 缓存压缩图片路径
                .setTargetDir(Constant.IMAGE_PATH)
                // 设置开启压缩条件
                .filter(path1 -> !(TextUtils.isEmpty(path1) || path1.toLowerCase().endsWith(".gif")))
                // 压缩回调接口
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // 压缩开始前调用，可以在方法内启动 loading UI
                        if (mDialog != null)
                            mDialog.show();
                    }

                    @Override
                    public void onSuccess(File file) {
                        if (mDialog != null)
                            mDialog.cancel();
                        // 压缩成功后调用，返回压缩后的图片文件
                        // 显示图片路径
                        txviPath.setText("压缩后保存路径：" + file.getAbsolutePath());

                        // 获取图片尺寸
                        int[] imageSize = ImageUtils.getSize(file.getAbsolutePath());
                        // 获取图片大小
//                        String fileSize = FileUtils.getSize(file.getAbsolutePath());
                        long fileLength = FileUtils.getLength(file.getAbsolutePath());
                        String fileSize = Formatter.formatFileSize(getApplicationContext(), fileLength);
                        txviAfterSize.setText(imageSize[0] + "*" + imageSize[1]);
                        txviAfterHowBig.setText(fileSize);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mDialog != null)
                            mDialog.cancel();
                        // 当压缩过程出现问题时调用
//                        showMessage("当前压缩过程出现问题，e=" + e.getMessage());
                        txviPath.setText("当前压缩过程出现问题，e=" + e.getMessage());
                    }
                }).launch();
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
                // 返回的参数
                mImage = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);

                // 显示图片
                btnCompression.setVisibility(View.VISIBLE);
                imviContent.setVisibility(View.VISIBLE);
                lilaDataLayout.setVisibility(View.VISIBLE);
                imviContent.setImageBitmap(BitmapFactory.decodeFile(mImage.get(0)));
                // 获取图片尺寸
                int[] imageSize = ImageUtils.getSize(mImage.get(0));
                // 获取图片大小
//                String fileSize = FileUtils.getSize(mImage.get(0));
                long fileLength = FileUtils.getLength(mImage.get(0));
                String fileSize = Formatter.formatFileSize(this, fileLength);

                txviBeforeSize.setText(imageSize[0] + "*" + imageSize[1]);
                txviBeforeHowBig.setText(fileSize);

                txviAfterSize.setText("");
                txviAfterHowBig.setText("");
                txviPath.setText("压缩后保存路径：");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PictureCompressionActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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