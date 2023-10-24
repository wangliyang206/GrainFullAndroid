package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.Utils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.unity3d.player.UnityPlayerActivity;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.MediaStoreUtils;
import com.zqw.mobile.grainfull.di.component.DaggerElfinPlayerComponent;
import com.zqw.mobile.grainfull.mvp.contract.ElfinPlayerContract;
import com.zqw.mobile.grainfull.mvp.presenter.ElfinPlayerPresenter;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Description: 小精灵 - unity - 入口
 * <p>
 * Created on 2023/10/13 15:54
 *
 * @author 赤槿
 * module name is ElfinPlayerActivity
 */
@RuntimePermissions
public class ElfinPlayerActivity extends BaseActivity<ElfinPlayerPresenter> implements ElfinPlayerContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_elfin_player)
    LinearLayout contentLayout;                                                                     // 总布局

    @BindView(R.id.txvi_elfinplayer_path)
    TextView txviPath;
    @BindView(R.id.imvi_elfinplayer_pic)
    ImageView imviPic;
    /*------------------------------------------------业务区域------------------------------------------------*/
    // 加载图片对象
    @Inject
    ImageLoader mImageLoader;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerElfinPlayerComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mImageLoader = null;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_elfin_player;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("小精灵模块库");

        ElfinPlayerActivityPermissionsDispatcher.loadDataWithPermissionCheck(this);
    }

    /**
     * 申请权限成功后的逻辑
     */
    @NeedsPermission({
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    })
    public void loadData() {
        // 统一流程：先删除模板目录中的所有文件，然后将Assets中的模板拷贝到SDCARD/Pictures/GrainFull/Template中

        // 判断当前android为10时采用分区模式
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            // 分区模式

            // 清空模板
            MediaStoreUtils.deleteFilesInDir(this, "image", Constant.TEMPLATE_PATH);
            // 将Assets中的模板保存到SDCARD中
            MediaStoreUtils.copyImageFromAssets(this, "template/", Environment.DIRECTORY_PICTURES + "/GrainFull/Template");

        } else {
            // 旧式外部存储
            // 清空模板
            FileUtils.deleteFilesInDir(Constant.TEMPLATE_PATH);
            // 将Assets中的模板保存到SDCARD中
            ResourceUtils.copyFileFromAssets("template", Constant.TEMPLATE_PATH);
        }

        try {
            String[] assets = Utils.getApp().getAssets().list("template");
            if (assets != null && assets.length > 0) {
                // 第三步，显示：路径和图片
                txviPath.setText("路径：" + Constant.TEMPLATE_PATH + assets[0]);
                // 显示图片
                mImageLoader.loadImage(getApplicationContext(), ImageConfigImpl.builder().url(Constant.TEMPLATE_PATH + assets[0])
                        .errorPic(R.mipmap.mis_default_error)
                        .placeholder(R.mipmap.mis_default_error)
                        .imageView(imviPic).build());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick({
            R.id.btn_elfinplayer_select,                                                            // 查看
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_elfinplayer_select:                                                       // 查看
                Bundle mBundle = new Bundle();
                // layout用来控制切换的界面
                // 0代表使用模板一(AR识别，3D精灵库，多精灵模板)
                // 1代表使用模板二(AR识别，3D精灵库，只有一个精灵“小鬼”，使用安卓本地模板)
                // 2代表使用模板三(见缝插针游戏)
                mBundle.putInt("layout", 0);
                ActivityUtils.startActivity(mBundle, UnityPlayerActivity.class);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ElfinPlayerActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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