package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.unity3d.player.UnityPlayerActivity;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.di.component.DaggerElfinPlayerComponent;
import com.zqw.mobile.grainfull.mvp.contract.ElfinPlayerContract;
import com.zqw.mobile.grainfull.mvp.presenter.ElfinPlayerPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description: 小精灵 - unity - 入口
 * <p>
 * Created on 2023/10/13 15:54
 *
 * @author 赤槿
 * module name is ElfinPlayerActivity
 */
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

        // 流程步骤：
        // 第一步，清空模板，重新保存模板到本地。
        FileUtils.delete(Constant.TEMPLATE_PATH);
        // 第二步，显示模板路径。
        ResourceUtils.copyFileFromAssets("template/template.bmp", Constant.TEMPLATE_PATH + "template.bmp");
        // 第三步，显示：路径和图片
        txviPath.setText("路径：" + Constant.TEMPLATE_PATH + "template.bmp");
        // 显示图片
        mImageLoader.loadImage(getApplicationContext(), ImageConfigImpl.builder().url(Constant.TEMPLATE_PATH + "template.bmp")
                .errorPic(R.mipmap.mis_default_error)
                .placeholder(R.mipmap.mis_default_error)
                .imageView(imviPic).build());
    }

    @OnClick({
            R.id.btn_elfinplayer_select,                                                            // 查看
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_elfinplayer_select:                                                       // 查看
                Bundle mBundle = new Bundle();
                mBundle.putInt("layout", 1);
                ActivityUtils.startActivity(mBundle, UnityPlayerActivity.class);
                break;
        }
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