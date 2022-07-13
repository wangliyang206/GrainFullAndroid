package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerBaiduFaceCollectFailureComponent;
import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceCollectFailureContract;
import com.zqw.mobile.grainfull.mvp.presenter.BaiduFaceCollectFailurePresenter;

import java.text.DecimalFormat;

import butterknife.BindView;

/**
 * Description:采集请求失败页面
 * <p>
 * Created on 2022/07/12 18:19
 *
 * @author 赤槿
 * module name is BaiduFaceCollectFailureActivity
 */
public class BaiduFaceCollectFailureActivity extends BaseActivity<BaiduFaceCollectFailurePresenter> implements BaiduFaceCollectFailureContract.View {
    @BindView(R.id.text_err_message)
    TextView mTextErrMessage;
    @BindView(R.id.text_err_tips)
    TextView mTextErrTips;
    @BindView(R.id.text_score)
    TextView mTextScore;

    /**
     * 根据主题使用不同的颜色。
     * 如果想要纯透明，则需要重写此方法，返回值为 -1 即可。
     */
    public int useStatusBarColor() {
        return -1;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBaiduFaceCollectFailureComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_baidu_face_collect_failure;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initData();

    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            float livenessScore = intent.getFloatExtra("livenessScore", 0);
            boolean isColorError = intent.getBooleanExtra("isColorError", false);
            judgeError(livenessScore, isColorError);
        }
    }

    private void judgeError(float livenessScore, boolean isColorError) {
        if (isColorError) {
            mTextErrMessage.setText(R.string.collect_failure);
            mTextErrTips.setText(R.string.collect_tips_1);
        } else {
            mTextErrMessage.setText(R.string.collect_failure);
            mTextErrTips.setText("请确保是本人操作且正脸采集");
            DecimalFormat format = new DecimalFormat("0.0000");
            mTextScore.setText("活体分数：" + format.format(livenessScore));
        }
    }

    public void onReturnHomeFailure(View v) {
        BaiduFaceRecognitionActivity.destroyActivity();
        finish();
    }

    public void onRecollectFailure(View v) {
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