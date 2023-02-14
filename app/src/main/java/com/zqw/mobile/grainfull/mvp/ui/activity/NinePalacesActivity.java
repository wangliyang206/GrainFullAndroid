package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.lcw.library.imagepicker.ImagePicker;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.GlideLoader;
import com.zqw.mobile.grainfull.di.component.DaggerNinePalacesComponent;
import com.zqw.mobile.grainfull.mvp.contract.NinePalacesContract;
import com.zqw.mobile.grainfull.mvp.presenter.NinePalacesPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.NinePalacesView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Description:九宫格切图
 * <p>
 * Created on 2023/02/13 16:37
 *
 * @author 赤槿
 * module name is NinePalacesActivity
 */
@RuntimePermissions
public class NinePalacesActivity extends BaseActivity<NinePalacesPresenter> implements NinePalacesContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_nine_palaces)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.view_ninepalaces_image)
    NinePalacesView npviImage;

    @BindView(R.id.radio_ninepalaces_group)
    RadioGroup radioGroup;

    /*------------------------------------------------业务区域------------------------------------------------*/
    // 类型：2代表4块；3代表9块(默认)；4代表16块；5代表25块；
    private int type = 3;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerNinePalacesComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_nine_palaces;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("九宫格切图");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "nine_palaces");

        // 排列监听
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (group.getCheckedRadioButtonId() == R.id.radio_ninepalaces_two) {
                type = 2;
            } else if (group.getCheckedRadioButtonId() == R.id.radio_ninepalaces_three) {
                type = 3;
            } else if (group.getCheckedRadioButtonId() == R.id.radio_ninepalaces_four) {
                type = 4;
            } else if (group.getCheckedRadioButtonId() == R.id.radio_ninepalaces_five) {
                type = 5;
            }
            npviImage.startGame(type);
        });
    }

    @OnClick({
            R.id.view_ninepalaces_open,                                                             // 添加图片
            R.id.view_ninepalaces_cropping,                                                         // 裁剪图片
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_ninepalaces_open:                                                        // 添加图片
                NinePalacesActivityPermissionsDispatcher.addAvatarWithPermissionCheck(this);
                break;
            case R.id.view_ninepalaces_cropping:                                                    // 裁剪图片
                if (npviImage != null) {
                    String path = Constant.IMAGE_PATH + TimeUtils.getNowString(new SimpleDateFormat("yyyyMMdd_HHmmss"));
                    Bitmap[] mBitmap = npviImage.getBitmapTiles();

                    new Thread(() -> {
                        int index = 1;
                        for (Bitmap info : mBitmap) {
                            // 生成文件路径及名称
                            String name = path + "-" + index + ".png";
                            // 保存图片
                            ImageUtils.save(info, name, Bitmap.CompressFormat.PNG);
                            index++;
                        }

                        runOnUiThread(() -> {
                            // 弹出成功提示
                            showMessage("图片保存成功！路径：" + path);
                        });
                    }).start();
                }
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
                npviImage.setImageBitmap(BitmapFactory.decodeFile(mImg.get(0)));
                npviImage.startGame(type);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        NinePalacesActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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