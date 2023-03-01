package com.zqw.mobile.grainfull.mvp.ui.fragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.TimeUtils;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.fragment
 * @ClassName: CompressPicScaleFragment
 * @Description: 按比例压缩图片
 * @Author: WLY
 * @CreateDate: 2022/7/14 16:46
 */
@RuntimePermissions
public class CompressPicScaleFragment extends BaseFragment<PictureCompressionPresenter> implements PictureCompressionContract.View, View.OnClickListener {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.fragment_compresspicscale)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.view_compresspicscale_compression)
    LinearLayout btnCompression;                                                                    // 压缩按钮
    @BindView(R.id.imvi_compresspicscale_image)
    ImageView imviContent;                                                                          // 显示图片
    @BindView(R.id.lila_compresspicscale_data)
    LinearLayout lilaDataLayout;                                                                    // 数据总布局

    @BindView(R.id.txvi_compresspicscale_before_size)
    TextView txviBeforeSize;                                                                        // 压缩前 - 图片尺寸
    @BindView(R.id.txvi_compresspicscale_before_howbig)
    TextView txviBeforeHowBig;                                                                      // 压缩前 - 图片大小

    @BindView(R.id.edit_compresspicscale_width)
    EditText editWidth;                                                                             // 压缩 - 图片宽度
    @BindView(R.id.edit_compresspicscale_height)
    EditText editHeight;                                                                            // 压缩 - 图片高度
    @BindView(R.id.txvi_compresspicscale_path)
    TextView txviPath;                                                                              // 压缩后保存的路径
    /*------------------------------------------------业务区域------------------------------------------------*/
    // 加载图片对象
    @Inject
    ImageLoader mImageLoader;

    // 对话框
    private MaterialDialog mDialog;
    // 选中的图片
    private ArrayList<String> mImage;
    // 获取图片尺寸
    private int[] imageSize;

    public static CompressPicScaleFragment instantiate() {
        return new CompressPicScaleFragment();
    }

    @Override
    public void onDestroy() {
        KeyboardUtils.unregisterSoftInputChangedListener(getActivity().getWindow());
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
        return inflater.inflate(R.layout.fragment_compresspicscale, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 初始化Loading对话框
        mDialog = new MaterialDialog.Builder(getContext()).content(R.string.common_execute).progress(true, 0).build();

    }

    @OnClick({
            R.id.view_compresspicscale_open,                                                        // 添加图片
            R.id.view_compresspicscale_compression,                                                 // 压缩图片
    })
    @Override
    public void onClick(View v) {
        hideInput();
        switch (v.getId()) {
            case R.id.view_compresspicscale_open:                                                   // 添加图片
                CompressPicScaleFragmentPermissionsDispatcher.addAvatarWithPermissionCheck(this);
                break;
            case R.id.view_compresspicscale_compression:                                            // 压缩图片
                onCompression();
                break;
        }
    }

    /**
     * 图片按比例压缩
     */
    private void onCompression() {
        // 检查是否有选择图片
        if (CommonUtils.isNotEmpty(mImage)) {
            // 检查输入的值是否有效
            if (checkInput()) {
                // 打开Loading效果
                if (mDialog != null)
                    mDialog.show();
                new Thread(() -> {
                    int mWidth = Integer.parseInt(editWidth.getText().toString());
                    int mHeight = Integer.parseInt(editHeight.getText().toString());

                    // 缩放
                    Bitmap mBitmap = ImageUtils.compressByScale(BitmapFactory.decodeFile(mImage.get(0)), mWidth, mHeight);
                    // 生成文件路径及名称
                    String path = Constant.IMAGE_PATH + TimeUtils.getNowString(new SimpleDateFormat("yyyyMMdd_HHmmss")) + ".png";
                    // 保存图片
                    ImageUtils.save(mBitmap, path, Bitmap.CompressFormat.PNG);

                    getActivity().runOnUiThread(() -> {
                        if (mDialog != null)
                            mDialog.cancel();
                        // 压缩成功后调用，返回压缩后的图片文件
                        // 显示图片路径
                        txviPath.setText("压缩后保存路径：" + path);
                    });
                }).start();
            }
        } else {
            showMessage("请先选择图片！");
        }

    }

    /**
     * 输入检查
     */
    private boolean checkInput() {
        String mWidth = editWidth.getText().toString();
        String mHeight = editHeight.getText().toString();

        if (TextUtils.isEmpty(mWidth)) {
            showMessage("请输入调整压缩指数的图片宽度！");
            return false;
        }

        int mWidthNum = Integer.parseInt(mWidth);
        if (mWidthNum <= 0) {
            showMessage("图片宽度不能小于0！");
            return false;
        }

        if (TextUtils.isEmpty(mHeight)) {
            showMessage("请输入调整压缩指数的图片高度！");
            return false;
        }

        int mHeightNum = Integer.parseInt(mHeight);
        if (mHeightNum <= 0) {
            showMessage("图片高度不能小于0！");
            return false;
        }
        return true;
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
                .start(getActivity(), Constant.MAIN_AVATAR);
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
        imageSize = ImageUtils.getSize(mImage.get(0));
        // 获取图片大小
//                String fileSize = FileUtils.getSize(mImage.get(0));
        long fileLength = FileUtils.getLength(mImage.get(0));
        String fileSize = Formatter.formatFileSize(getContext(), fileLength);

        txviBeforeSize.setText(imageSize[0] + "*" + imageSize[1]);
        txviBeforeHowBig.setText(fileSize);

        editWidth.removeTextChangedListener(mWidthWatcher);
        editHeight.removeTextChangedListener(mHeightWatcher);
        editWidth.setText(String.valueOf(imageSize[0]));
        editHeight.setText(String.valueOf(imageSize[1]));
        editWidth.addTextChangedListener(mWidthWatcher);
        editHeight.addTextChangedListener(mHeightWatcher);
        txviPath.setText("压缩后保存路径：");
    }

    /**
     * 宽度输入监听
     */
    private final TextWatcher mWidthWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String mWidth = editWidth.getText().toString();
            if (!TextUtils.isEmpty(mWidth)) {
                // 有值，转换成int类型
                int mNum = Integer.parseInt(mWidth);
                // 计算宽度比例
                double mWidthScale = CommonUtils.div(imageSize[0], mNum, 2);
                // 计算高度
                int mValue = (int) (imageSize[1] / mWidthScale);
                editHeight.removeTextChangedListener(mHeightWatcher);
                editHeight.setText(String.valueOf(mValue));
                editHeight.addTextChangedListener(mHeightWatcher);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 高度输入监听
     */
    private final TextWatcher mHeightWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String mHeight = editHeight.getText().toString();
            if (!TextUtils.isEmpty(mHeight)) {
                // 有值，转换成int类型
                int mNum = Integer.parseInt(mHeight);
                // 计算高度比例
                double mHeightScale = CommonUtils.div(imageSize[1], mNum, 2);
                // 计算宽度
                int mValue = (int) (imageSize[0] / mHeightScale);
                editWidth.removeTextChangedListener(mWidthWatcher);
                editWidth.setText(String.valueOf(mValue));
                editWidth.addTextChangedListener(mWidthWatcher);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CompressPicScaleFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 隐藏软键盘
     */
    private void hideInput() {
        KeyboardUtils.hideSoftInput(getActivity());
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
