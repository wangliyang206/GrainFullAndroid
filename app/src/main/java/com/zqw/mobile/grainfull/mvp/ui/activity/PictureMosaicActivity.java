package com.zqw.mobile.grainfull.mvp.ui.activity;

import android.Manifest;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import com.lcw.library.imagepicker.ImagePicker;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.GlideLoader;
import com.zqw.mobile.grainfull.di.component.DaggerPictureMosaicComponent;
import com.zqw.mobile.grainfull.mvp.contract.PictureMosaicContract;
import com.zqw.mobile.grainfull.mvp.presenter.PictureMosaicPresenter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.ui.widget.LoupeView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Description: 图片拼接
 * <p>
 * Created on 2023/02/09 15:00
 *
 * @author 赤槿
 * module name is PictureMosaicActivity
 */
@RuntimePermissions
public class PictureMosaicActivity extends BaseActivity<PictureMosaicPresenter> implements PictureMosaicContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_picture_mosaic)
    LinearLayout contentLayout;                                                                     // 主布局

//    @BindView(R.id.view_magnifieractivity_content)
//    LoupeView viewLoupeView;

    /*------------------------------------------------业务区域------------------------------------------------*/
    // 最大张图
    private int mMaxCount;

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
        DaggerPictureMosaicComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_picture_mosaic;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("图片拼接");

        // 友盟统计 - 自定义事件
//        MobclickAgent.onEvent(getApplicationContext(), "picture_mosaic");
    }

    @OnClick({
            R.id.view_picturemosaic_open,                                                           // 选择图片
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_picturemosaic_open:                                                      // 选择图片
                PictureMosaicActivityPermissionsDispatcher.addAvatarWithPermissionCheck(this);
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
                .setMaxCount(9)//设置最大选择图片数目(默认为1，单选)
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
                // 显示图片(方式一)
//                mImageLoader.loadImage(this,
//                        ImageConfigImpl.builder().url(mImg.get(0))
//                                .placeholder(R.mipmap.mis_default_error)
//                                .errorPic(R.mipmap.mis_default_error)
//                                .imageView(viewLoupeView).build());

                // 显示图片(方式二)
//                viewLoupeView.setImageBitmap(BitmapFactory.decodeFile(mImg.get(0)));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MagnifierActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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