package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huawei.hms.mlplugin.card.icr.cn.MLCnIcrCapture;
import com.huawei.hms.mlplugin.card.icr.cn.MLCnIcrCaptureConfig;
import com.huawei.hms.mlplugin.card.icr.cn.MLCnIcrCaptureFactory;
import com.huawei.hms.mlplugin.card.icr.cn.MLCnIcrCaptureResult;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerIdentifyIdCardComponent;
import com.zqw.mobile.grainfull.mvp.contract.IdentifyIdCardContract;
import com.zqw.mobile.grainfull.mvp.presenter.IdentifyIdCardPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;


/**
 * ================================================
 * Description:识别身份证
 * <p>
 * Created by MVPArmsTemplate on 06/24/2022 11:00
 * ================================================
 */
public class IdentifyIdCardActivity extends BaseActivity<IdentifyIdCardPresenter> implements IdentifyIdCardContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.imgvi_identifyidcard_idcardfont)
    ImageView imviFont;                                                                             // 身份证头像面
    @BindView(R.id.imgvi_identifyidcard_idcardback)
    ImageView imviBack;                                                                             // 身份证国徽面

    @BindView(R.id.txvi_identifyidcard_idcard)
    TextView txviIdCard;                                                                            // 身份证号
    @BindView(R.id.txvi_identifyidcard_name)
    TextView txviName;                                                                              // 姓名
    @BindView(R.id.txvi_identifyidcard_sex)
    TextView txviSex;                                                                               // 性别
    @BindView(R.id.txvi_identifyidcard_validityperiod)
    TextView txviValidityPeriod;                                                                    // 有效期

    /*------------------------------------------------业务区域------------------------------------------------*/
    // 当前识别的是身份证正面吗
    private boolean isFront;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerIdentifyIdCardComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_identify_id_card;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 设置标题
        setTitle("识别身份证");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "identify_id_card_open");
    }

    @OnClick({
            R.id.rela_identifyidcard_idcardfont,                                                    // 身份证头像面
            R.id.rela_identifyidcard_idcardback,                                                    // 身份证国徽面
    })
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rela_identifyidcard_idcardfont:                                               // 身份证头像面
                startCaptureActivity(this.idCallBack, true);
                break;
            case R.id.rela_identifyidcard_idcardback:                                               // 身份证国徽面
                startCaptureActivity(this.idCallBack, false);
                break;
        }
    }

    /**
     * 设置识别参数，调用识别器抓包接口进行识别，通过回调函数返回识别结果。
     *
     * @param callback 身份证分析回调
     * @param isFront  是否是身份证正面
     */
    private void startCaptureActivity(MLCnIcrCapture.CallBack callback, boolean isFront) {
        this.isFront = isFront;
        MLCnIcrCaptureConfig config = new MLCnIcrCaptureConfig.Factory()
                .setFront(isFront)
                .create();
        MLCnIcrCapture icrCapture = MLCnIcrCaptureFactory.getInstance().getIcrCapture(config);
        icrCapture.capture(callback, this);
    }

    /**
     * 使用中文二代身份证预处理插件识别视频流身份证。
     * 创建识别结果回调函数，处理身份证的识别结果。
     */
    private final MLCnIcrCapture.CallBack idCallBack = new MLCnIcrCapture.CallBack() {

        @Override
        public void onSuccess(MLCnIcrCaptureResult idCardResult) {
            Timber.i("识别身份证回调 记录成功");
            if (idCardResult == null) {
                Timber.i("识别身份证回调 记录成功 idCardResult is null");
                return;
            }
            Bitmap bitmap = idCardResult.cardBitmap;
            if (isFront) {
                // 显示识别结果(人像面)
                imviFont.setImageBitmap(bitmap);
                txviIdCard.setText(idCardResult.idNum);
                txviName.setText(idCardResult.name);
                txviSex.setText(idCardResult.sex);
                // 友盟统计 - 自定义事件
                MobclickAgent.onEvent(getApplicationContext(), "identify_id_card");
            } else {
                // 显示识别结果(国徽面)
                imviBack.setImageBitmap(bitmap);
                txviValidityPeriod.setText(idCardResult.validDate);
            }
        }

        @Override
        public void onCanceled() {
            // 用户取消处理。
            showMessage("用户取消处理");
        }

        @Override
        public void onFailure(int retCode, Bitmap bitmap) {
            // 识别失败处理。
            showMessage("识别失败处理，retCode=" + retCode + "；bitmap=" + bitmap);
        }

        // Camera unavailable processing, the reason that the camera is unavailable is generally that the user has not been granted camera permissions.
        @Override
        public void onDenied() {
            // 相机不支持等场景处理。
            showMessage("相机不支持等场景处理");
        }
    };

    @Override
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
