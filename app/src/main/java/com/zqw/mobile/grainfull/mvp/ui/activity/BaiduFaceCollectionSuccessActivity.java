package com.zqw.mobile.grainfull.mvp.ui.activity;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.utils.ArmsUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import com.zqw.mobile.grainfull.di.component.DaggerBaiduFaceCollectionSuccessComponent;
import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceCollectionSuccessContract;
import com.zqw.mobile.grainfull.mvp.presenter.BaiduFaceCollectionSuccessPresenter;
import com.zqw.mobile.grainfull.R;

import java.text.DecimalFormat;

import butterknife.BindView;
import timber.log.Timber;

/**
 * Description:百度AI - 人脸识别 采集成功
 * <p>
 * Created on 2022/07/12 18:16
 *
 * @author 赤槿
 * module name is BaiduFaceCollectionSuccessActivity
 */
public class BaiduFaceCollectionSuccessActivity extends BaseActivity<BaiduFaceCollectionSuccessPresenter> implements BaiduFaceCollectionSuccessContract.View {

    @BindView(R.id.text_score)
    TextView mTextScore;

    // 已保存的路径
    private String mSaveName;

    /**
     * 根据主题使用不同的颜色。
     * 如果想要纯透明，则需要重写此方法，返回值为 -1 即可。
     */
    public int useStatusBarColor() {
        return -1;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBaiduFaceCollectionSuccessComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_baidu_face_collection_success;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initData();

    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            float score = intent.getFloatExtra("livenessScore", 0f);
            DecimalFormat format = new DecimalFormat("0.0000");
            mSaveName = intent.getStringExtra("SaveName");
            mTextScore.setText("活体分数 " + format.format(score));
        }
    }

    // 回到首页
    public void onReturnHome(View v) {
        BaiduFaceRecognitionActivity.destroyActivity();
        finish();
    }

    /**
     * 播放
     */
    public void onPlay(View v) {
        Timber.i("####%s", mSaveName);
        if (FileUtils.isFile(mSaveName)) {
            Intent tostart = new Intent(Intent.ACTION_VIEW);
            tostart.setDataAndType(Uri.parse(mSaveName), "video/*");
            startActivity(tostart);
        } else {
            showMessage("没有发现文件！");
        }


    }

    // 重新采集
    public void onRecollect(View v) {
        finish();
    }

    public void onBack(View v) {
        finish();
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