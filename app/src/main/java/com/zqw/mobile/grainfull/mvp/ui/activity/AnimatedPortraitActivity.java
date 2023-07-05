package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.lcw.library.imagepicker.ImagePicker;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.GlideLoader;
import com.zqw.mobile.grainfull.app.utils.RxUtils;
import com.zqw.mobile.grainfull.di.component.DaggerAnimatedPortraitComponent;
import com.zqw.mobile.grainfull.mvp.contract.AnimatedPortraitContract;
import com.zqw.mobile.grainfull.mvp.presenter.AnimatedPortraitPresenter;

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
 * Description:人像动漫化
 * <p>
 * Created on 2023/07/04 10:14
 *
 * @author 赤槿
 * module name is AnimatedPortraitActivity
 */
@RuntimePermissions
public class AnimatedPortraitActivity extends BaseActivity<AnimatedPortraitPresenter> implements AnimatedPortraitContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_animated_portrait)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.imvi_animatedportrait_originalcontent)
    ImageView imviiOriginal;                                                                        // 优化前

    @BindView(R.id.imvi_animatedportrait_effectcontent)
    ImageView imviEffect;                                                                           // 优化后
    @BindView(R.id.imvi_animatedportrait_save)
    ImageView imviSave;                                                                             // 保存图片
    /*------------------------------------------------业务区域------------------------------------------------*/
    // 加载图片对象
    @Inject
    ImageLoader mImageLoader;
    // 合成的动漫图片数据源
    private Bitmap mBitmap;

    // 对话框
    private MaterialDialog mDialog;

    @Override
    protected void onDestroy() {
        if (mDialog != null) {
            this.mDialog.dismiss();
        }
        super.onDestroy();
        this.mDialog = null;
        this.mImageLoader = null;
        this.mBitmap = null;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAnimatedPortraitComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_animated_portrait;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("人像动漫化");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "animated_portrait_open");

        // 初始化Loading对话框
        mDialog = new MaterialDialog.Builder(this).content(R.string.common_execute).progress(true, 0).cancelable(false).build();

        if (mPresenter != null) {
            mPresenter.getBaiduToken();
        }
    }

    @OnClick({
            R.id.cola_animatedportrait_original,                                                    // 添加人像图片
            R.id.imvi_animatedportrait_save,                                                        // 保存图片
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cola_animatedportrait_original:                                               // 添加人像图片
                AnimatedPortraitActivityPermissionsDispatcher.getPortraitImageWithPermissionCheck(this);
                break;
            case R.id.imvi_animatedportrait_save:                                                   // 保存图片
                onSaveImage();
                break;
        }
    }

    /**
     * 将图片进行保存
     */
    private void onSaveImage() {
        // 保存图片
        Runnable runnable = () -> {
            // 生成文件路径及名称
            String path = Constant.IMAGE_PATH + TimeUtils.getNowString(new SimpleDateFormat("yyyyMMdd_HHmmss")) + ".png";
            // 保存图片
            ImageUtils.save(mBitmap, path, Bitmap.CompressFormat.PNG);

            runOnUiThread(() -> {
                // 弹出成功提示
                showMessage("图片保存成功！路径：" + path);
            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * 选择人像图片
     */
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void getPortraitImage() {

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

            // 对比照片
            if (requestCode == Constant.REQUEST_SELECT_IMAGES_CODE) {
                // 显示图片
                mImageLoader.loadImage(getApplicationContext(), ImageConfigImpl.builder().url(mImage.get(0))
                        .errorPic(R.mipmap.mis_default_error)
                        .placeholder(R.mipmap.mis_default_error)
                        .imageView(imviiOriginal).build());

                // 显示loading
                showLoadingSubmit();
                // 图片压缩
                compressImage(data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES));
            }
        }
    }

    /**
     * 压缩图片
     *
     * @param mImg 图片地址
     */
    private void compressImage(ArrayList<String> mImg) {
        Luban.with(this)
                .load(mImg)
                .ignoreBy(2048)
                .setTargetDir(Constant.IMAGE_PATH)
                .filter(path1 -> !(TextUtils.isEmpty(path1) || path1.toLowerCase().endsWith(".gif")))
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // 压缩成功后调用，返回压缩后的图片文件
                        // 显示图片
                        uploadData(file.getAbsolutePath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 当压缩过程出现问题时调用
                        // 显示图片
                        uploadData(mImg);
                    }
                }).launch();
    }

    /**
     * 压缩成功 》 加载图片 》 单张添加
     */
    private void uploadData(String path) {
        onCreatingPictures(path);
    }

    /**
     * 加载图片
     */
    private void uploadData(ArrayList<String> path) {
        onCreatingPictures(path.get(0));
    }

    /**
     * 制作图片
     */
    private void onCreatingPictures(String path) {
        // 延迟一秒调用
        RxUtils.startDelayed(1, this, () -> {
            // 开始优化
            if (mPresenter != null) {
                mPresenter.onStartOptimization(path);
            }
        });
    }

    public Activity getActivity() {
        return this;
    }

    /**
     * 提交时显示loading框
     */
    @Override
    public void showLoadingSubmit() {
        if (mDialog != null)
            mDialog.show();
    }

    /**
     * 提交后隐藏loading框
     */
    @Override
    public void hideLoadingSubmit() {
        if (mDialog != null)
            mDialog.dismiss();
    }

    /**
     * 加载优化后的效果
     */
    @Override
    public void loadImage(String image) {
        // 第一步，先转成图片格式
        byte[] mByte = EncodeUtils.base64Decode(image);
        mBitmap = BitmapFactory.decodeByteArray(mByte, 0, mByte.length);
        // 第二步，显示
        imviEffect.setImageBitmap(mBitmap);
        imviSave.setVisibility(View.VISIBLE);
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