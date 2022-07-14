package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huawei.hms.mlplugin.card.bcr.MLBcrCapture;
import com.huawei.hms.mlplugin.card.bcr.MLBcrCaptureConfig;
import com.huawei.hms.mlplugin.card.bcr.MLBcrCaptureFactory;
import com.huawei.hms.mlplugin.card.bcr.MLBcrCaptureResult;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerIdentifyBankCardsComponent;
import com.zqw.mobile.grainfull.mvp.contract.IdentifyBankCardsContract;
import com.zqw.mobile.grainfull.mvp.presenter.IdentifyBankCardsPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;


/**
 * ================================================
 * Description:识别银行卡
 * <p>
 * Created by MVPArmsTemplate on 06/23/2022 19:34
 * ================================================
 */
public class IdentifyBankCardsActivity extends BaseActivity<IdentifyBankCardsPresenter> implements IdentifyBankCardsContract.View {
    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.edit_identifybankcards_input)
    EditText editInput;

    @BindView(R.id.txvi_identifybankcards_tips)
    TextView txviTips;

    @BindView(R.id.imvi_identifybankcards_image)
    ImageView image;
    /*------------------------------------------业务信息------------------------------------------*/

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerIdentifyBankCardsComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_identify_bank_cards;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 设置标题
        setTitle("识别银行卡");

    }

    @OnClick({
            R.id.imvi_identifybankcards_camera,                                                     // 识别银行卡
    })
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imvi_identifybankcards_camera:                                                // 识别银行卡
                startCaptureActivity(callback);
                break;
        }
    }

    /**
     * 启动识别功能
     */
    private void startCaptureActivity(MLBcrCapture.Callback callback) {
        MLBcrCaptureConfig config = new MLBcrCaptureConfig.Factory()
                // 设置银行卡识别期望返回的结果类型。
                // MLBcrCaptureConfig.RESULT_NUM_ONLY：仅识别卡号。
                // MLBcrCaptureConfig.RESULT_SIMPLE：仅识别卡号、有效期信息。
                // MLBcrCaptureConfig.RESULT_ALL：识别卡号、有效期、发卡行、发卡组织和卡类别等信息。
                .setResultType(MLBcrCaptureConfig.RESULT_ALL)
                // 设置识别界面横竖屏，支持三种模式：
                // MLBcrCaptureConfig.ORIENTATION_AUTO: 自适应模式，由物理感应器决定显示方向。
                // MLBcrCaptureConfig.ORIENTATION_LANDSCAPE: 横屏模式。
                // MLBcrCaptureConfig.ORIENTATION_PORTRAIT: 竖屏模式。
                .setOrientation(MLBcrCaptureConfig.ORIENTATION_AUTO)
                .create();
        MLBcrCapture bcrCapture = MLBcrCaptureFactory.getInstance().getBcrCapture(config);
        bcrCapture.captureFrame(this, callback);
    }

    /**
     * 使用银行卡预处理插件识别视频流银行卡。
     * 创建识别结果回调函数，处理卡片的识别结果。
     */
    private final MLBcrCapture.Callback callback = new MLBcrCapture.Callback() {
        @Override
        public void onSuccess(MLBcrCaptureResult bankCardResult) {
            // 识别成功处理。
            if (bankCardResult == null) {
                showMessage("识别失败");
                return;
            }

            // 识别出的卡号
            editInput.setText(bankCardResult.getNumber());
            // 银行卡图片
            Bitmap bitmap = bankCardResult.getOriginalBitmap();
            image.setImageBitmap(bitmap);
            // 银行卡其它参数
            txviTips.setText(formatIdCardResult(bankCardResult));
        }

        @Override
        public void onCanceled() {
            // 用户取消处理。
            showMessage("用户取消处理");
        }

        // 识别不到任何文字信息或识别过程发生系统异常的回调方法。
        // retCode：错误码。
        // bitmap：检测失败的卡证图片。
        @Override
        public void onFailure(int retCode, Bitmap bitmap) {
            // 识别失败处理。
            showMessage("识别失败处理，retCode=" + retCode + "；bitmap=" + bitmap);
        }

        @Override
        public void onDenied() {
            // 相机不支持等场景处理。
            showMessage("相机不支持等场景处理");
        }
    };

    /**
     * 获取银行卡其它参数
     */
    private String formatIdCardResult(MLBcrCaptureResult bankCardResult) {
        StringBuilder resultBuilder = new StringBuilder();

//        resultBuilder.append("银行卡号：");
//        resultBuilder.append(bankCardResult.getNumber());
//        resultBuilder.append("\r\n");

        resultBuilder.append("发卡行：");
        resultBuilder.append(bankCardResult.getIssuer());
        resultBuilder.append("\r\n");

        resultBuilder.append("有效期: ");
        resultBuilder.append(bankCardResult.getExpire());
        resultBuilder.append("\r\n");

        resultBuilder.append("卡类型: ");
        resultBuilder.append(bankCardResult.getType());
        resultBuilder.append("\r\n");

        resultBuilder.append("组织: ");
        resultBuilder.append(bankCardResult.getOrganization());
//        resultBuilder.append("\r\n");

        Timber.i("front result: %s", resultBuilder.toString());
        return resultBuilder.toString();
    }

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
