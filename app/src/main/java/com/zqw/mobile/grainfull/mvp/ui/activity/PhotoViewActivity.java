package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

/**
 * 包名： com.zqw.mobile.aptitude.mvp.ui.activity
 * 对象名： PhotoViewActivity
 * 描述：图片查看器
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2018/8/16 17:18
 */

public class PhotoViewActivity extends BaseActivity {

    @BindView(R.id.view_photoview_image)
    PhotoView photoView;

    /**
     * 将状态栏改为浅色、深色模式(状态栏 icon 和字体，false = 浅色，true = 深色)
     */
    public boolean useLightStatusBar() {
        return false;
    }

    /**
     * 根据主题使用不同的颜色。
     * 如果想要纯透明，则需要重写此方法，返回值为 -1 即可。
     */
    public int useStatusBarColor() {
        return -1;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_photo_view;
    }

    @OnClick({
            R.id.view_photoview_image,                                                              // 点击图片关闭
            R.id.view_photoview_save,                                                               // 保存图片
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_photoview_image:                                                         // 点击图片关闭
                onBackPressed();
                break;
            case R.id.view_photoview_save:                                                          // 保存图片
                // 保存图片
                Runnable runnable = () -> {
                    // 生成文件路径及名称
                    String path = Constant.IMAGE_PATH + TimeUtils.getNowString(new SimpleDateFormat("yyyyMMdd_HHmmss")) + ".png";
                    // 保存图片
                    try {
                        Bitmap bitmap = Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(getIntent().getStringExtra(Constant.IMAGE_URL))
                                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .get();

                        if (bitmap != null) {
                            ImageUtils.save(bitmap, path, Bitmap.CompressFormat.PNG);

                            runOnUiThread(() -> {
                                // 弹出成功提示
                                showMessage("图片保存成功！路径：" + path);
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
                break;
        }
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        String url = getIntent().getStringExtra(Constant.IMAGE_URL);
        Glide.with(getApplicationContext()).load(url).into(photoView);
    }

    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }
}
