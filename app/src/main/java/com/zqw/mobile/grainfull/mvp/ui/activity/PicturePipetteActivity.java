package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.lcw.library.imagepicker.ImagePicker;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.ColorsUtil;
import com.zqw.mobile.grainfull.app.utils.GlideLoader;
import com.zqw.mobile.grainfull.di.component.DaggerPicturePipetteComponent;
import com.zqw.mobile.grainfull.mvp.contract.PicturePipetteContract;
import com.zqw.mobile.grainfull.mvp.presenter.PicturePipettePresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.ImageColorPickerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Description:吸管工具(图片中提取颜色)
 * <p>
 * Created on 2023/01/05 11:12
 *
 * @author 赤槿
 * module name is PicturePipetteActivity
 */
@RuntimePermissions
public class PicturePipetteActivity extends BaseActivity<PicturePipettePresenter> implements PicturePipetteContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_picture_pipette)
    LinearLayout contentLayout;                                                                     // 主布局
    @BindView(R.id.view_picturepipette_image)
    ImageColorPickerView viewImage;                                                                 // 提取颜色的容器

    @BindView(R.id.view_picturepipette_bg)
    View viewBg;                                                                                    // 显示选中的颜色
    @BindView(R.id.imvi_picturepipette_tips)
    ImageView imviTips;                                                                             // 添加图片的提示
    @BindView(R.id.btn_picturepipette_copy)
    ImageView imviCopy;                                                                             // 复制按钮
    @BindView(R.id.txvi_picturepipette_color)
    TextView txviColor;                                                                             // 显示颜色值(十六进制)
    @BindView(R.id.txvi_picturepipette_rgb)
    TextView txviRgbColor;                                                                          // 显示颜色值(RGB)
    /*------------------------------------------------业务区域------------------------------------------------*/

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPicturePipetteComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_picture_pipette;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("吸管工具");

        viewImage.setOnColorChangedListenner(color -> {
            // 显示背景色
            viewBg.setBackgroundColor(color);
            // 显示十六进制颜色
            txviColor.setText(ColorsUtil.getHexString(color, false));
            // 显示RGB颜色
            txviRgbColor.setText("RGB " + Color.red(color) + "," + Color.green(color) + "," + Color.blue(color));

            // 控制特殊控件的颜色
            if (ColorUtils.isLightColor(color)) {
                // 亮色
                txviColor.setTextColor(getResources().getColor(R.color.colorpicker_txt_color));
                txviRgbColor.setTextColor(getResources().getColor(R.color.colorpicker_txt_color));
                imviCopy.setImageResource(R.mipmap.icon_copy);
                imviTips.setImageResource(R.mipmap.icon_add_picture);
            } else {
                // 暗色
                txviColor.setTextColor(Color.WHITE);
                txviRgbColor.setTextColor(Color.WHITE);
                imviCopy.setImageResource(R.mipmap.icon_copy_white);
                imviTips.setImageResource(R.mipmap.icon_add_picture_white);
            }

        });
    }

    @OnClick({
            R.id.btn_picturepipette_copy,                                                           // 复制
            R.id.cola_picturepipette_select,                                                        // 选择图片

            R.id.btn_picturepipette_up,                                                             // 上
            R.id.btn_picturepipette_down,                                                           // 下
            R.id.btn_picturepipette_left,                                                           // 左
            R.id.btn_picturepipette_right,                                                          // 右
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_picturepipette_copy:                                                      // 复制
                String mColor = txviColor.getText().toString();
                if (!TextUtils.isEmpty(mColor)) {
                    ClipboardUtils.copyText(mColor);
                    showMessage("复制成功！");
                }
                break;
            case R.id.cola_picturepipette_select:                                                   // 选择图片
                PicturePipetteActivityPermissionsDispatcher.addAvatarWithPermissionCheck(this);
                break;

            case R.id.btn_picturepipette_up:                                                        // 上

                break;
            case R.id.btn_picturepipette_down:                                                      // 下
                break;
            case R.id.btn_picturepipette_left:                                                      // 左
                viewImage.setHorizontalMovePoint(true);
                break;
            case R.id.btn_picturepipette_right:                                                     // 右
                viewImage.setHorizontalMovePoint(false);
                break;
        }
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
                ArrayList<String> mImg = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);

                // 显示图片
                viewImage.setImageBitmap(BitmapFactory.decodeFile(mImg.get(0)));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PicturePipetteActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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