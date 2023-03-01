package com.zqw.mobile.grainfull.mvp.ui.fragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.Manifest;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;
import com.lcw.library.imagepicker.ImagePicker;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.app.utils.GlideLoader;
import com.zqw.mobile.grainfull.di.component.DaggerPictureCompressionComponent;
import com.zqw.mobile.grainfull.mvp.contract.PictureCompressionContract;
import com.zqw.mobile.grainfull.mvp.presenter.PictureCompressionPresenter;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.fragment
 * @ClassName: CompressPicSizeFragment
 * @Description: 按大小压缩图片
 * @Author: WLY
 * @CreateDate: 2022/7/14 16:46
 */
@RuntimePermissions
public class CompressPicSizeFragment extends BaseFragment<PictureCompressionPresenter> implements PictureCompressionContract.View, View.OnClickListener {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.fragment_compresspicsize)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.txvi_compresspicsize_tips)
    TextView txviTips;                                                                              // 提示

    @BindView(R.id.view_compresspicsize_compression)
    LinearLayout btnCompression;                                                                    // 压缩按钮
    @BindView(R.id.imvi_compresspicsize_image)
    ImageView imviContent;                                                                          // 显示图片
    @BindView(R.id.lila_compresspicsize_data)
    LinearLayout lilaDataLayout;                                                                    // 数据总布局

    @BindView(R.id.txvi_compresspicsize_before_size)
    TextView txviBeforeSize;                                                                        // 压缩前 - 图片尺寸
    @BindView(R.id.txvi_compresspicsize_after_size)
    TextView txviAfterSize;                                                                         // 压缩后 - 图片尺寸
    @BindView(R.id.txvi_compresspicsize_before_howbig)
    TextView txviBeforeHowBig;                                                                      // 压缩前 - 图片大小
    @BindView(R.id.txvi_compresspicsize_after_howbig)
    TextView txviAfterHowBig;                                                                       // 压缩后 - 图片大小
    @BindView(R.id.txvi_compresspicsize_path)
    TextView txviPath;                                                                              // 压缩后保存的路径
    /*------------------------------------------------业务区域------------------------------------------------*/
    // 加载图片对象
    @Inject
    ImageLoader mImageLoader;

    // 对话框
    private MaterialDialog mDialog;

    // 选中的图片
    private ArrayList<String> mImage;

    public static CompressPicSizeFragment instantiate() {
        return new CompressPicSizeFragment();
    }

    @Override
    public void onDestroy() {
        this.mImage = null;
        if (mDialog != null) {
            this.mDialog.dismiss();
        }
        super.onDestroy();

        this.mDialog = null;
        this.mImageLoader = null;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerPictureCompressionComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compresspicsize, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 初始化Loading对话框
        mDialog = new MaterialDialog.Builder(getContext()).content(R.string.common_execute).progress(true, 0).build();

    }

    @OnClick({
            R.id.view_compresspicsize_open,                                                         // 添加图片
            R.id.view_compresspicsize_compression,                                                  // 压缩图片
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_compresspicsize_open:                                                    // 添加图片
                CompressPicSizeFragmentPermissionsDispatcher.addAvatarWithPermissionCheck(this);
                break;
            case R.id.view_compresspicsize_compression:                                             // 压缩图片
                onCompression();
                break;
        }
    }

    /**
     * 图片压缩
     */
    private void onCompression() {
        if (CommonUtils.isNotEmpty(mImage)) {
            // 按大小
            compressImage(mImage);
        } else {
            showMessage("请先选择图片！");
        }

    }

    /**
     * 按大小压缩图片
     */
    private void compressImage(ArrayList<String> mImg) {
        Luban.with(getContext())
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
                        String fileSize = Formatter.formatFileSize(getContext(), fileLength);
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
                .setImageLoader(new GlideLoader(getContext()))//设置自定义图片加载器
                .start(getActivity(), Constant.REQUEST_SELECT_IMAGES_CODE);
    }

    /**
     * 选择图片的返回结果
     */
    public void onResult(ArrayList<String> mList) {
        // 返回的参数
        this.mImage = mList;

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
        String fileSize = Formatter.formatFileSize(getContext(), fileLength);

        txviBeforeSize.setText(imageSize[0] + "*" + imageSize[1]);
        txviBeforeHowBig.setText(fileSize);

        txviAfterSize.setText("");
        txviAfterHowBig.setText("");
        txviPath.setText("压缩后保存路径：");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CompressPicSizeFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {
//        if (lilaLoading != null){
//            lilaLoading.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void hideLoading() {
//        if (lilaLoading != null) {
//            lilaLoading.setVisibility(View.GONE);
//        }
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

    }

}
