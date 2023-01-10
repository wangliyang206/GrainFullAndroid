package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.blankj.utilcode.util.ClipboardUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.ColorsUtil;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.di.component.DaggerColorCodeConversionComponent;
import com.zqw.mobile.grainfull.mvp.contract.ColorCodeConversionContract;
import com.zqw.mobile.grainfull.mvp.presenter.ColorCodeConversionPresenter;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:颜色码转换
 * <p>
 * Created on 2023/01/09 16:19
 *
 * @author 赤槿
 * module name is ColorCodeConversionActivity
 */
public class ColorCodeConversionActivity extends BaseActivity<ColorCodeConversionPresenter> implements ColorCodeConversionContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_color_code_conversion)
    LinearLayout contentLayout;                                                                     // 主布局
    @BindView(R.id.view_colorcodeconversion_card)
    CardView viewCard;                                                                              // 颜色背景

    @BindView(R.id.edit_colorcodeconversion_hex)
    EditText editHex;                                                                               // 显示的HEX颜色
    @BindView(R.id.edit_colorcodeconversion_rgb)
    EditText editRgb;                                                                               // 显示的RGB颜色
    @BindView(R.id.edit_colorcodeconversion_cmyk)
    EditText editCmyk;                                                                              // 显示的CMYK颜色
    @BindView(R.id.edit_colorcodeconversion_hsv)
    EditText editHsv;                                                                               // 显示的HSV颜色

    /*------------------------------------------------业务区域------------------------------------------------*/
    // 格式化对像
    private DecimalFormat mDecimalFormat = new DecimalFormat("##0.000");
    // 默认颜色值
    private int mDefaultColor = Color.parseColor("#F79A10");

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mDecimalFormat = null;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerColorCodeConversionComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_color_code_conversion;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("颜色码转换");

        setCurrentColor(mDefaultColor);

    }

    /**
     * 设置当前颜色
     */
    private void setCurrentColor(int mColor) {
        // 显示背景颜色
        viewCard.setCardBackgroundColor(mColor);
        // 显示十六进制的颜色
        editHex.setText(ColorsUtil.getHexString(mColor, false));

        // 显示 RGB 颜色
        int a = Color.alpha(mColor);
        int r = Color.red(mColor);
        int g = Color.green(mColor);
        int b = Color.blue(mColor);
        editRgb.setText(r + "," + g + "," + b);

        // 显示 CMYK 颜色
        float[] mCmyk = ColorsUtil.getCMYK(r, g, b);
        editCmyk.setText(format(mCmyk[0]) + "," + format(mCmyk[1]) + "," + format(mCmyk[2]) + "," + format(mCmyk[3]));

        // 显示 HSV 颜色
        float[] hsv = new float[3];
        Color.RGBToHSV(r, g, b, hsv);
        editHsv.setText(
                mDecimalFormat.format(hsv[0])
                        + "," +
                        mDecimalFormat.format(hsv[1])
                        + "," +
                        mDecimalFormat.format(hsv[2]));

    }

    /**
     * 取小数点后两位
     */
    private int format(float num) {
        int mVal = 0;
        String str = CommonUtils.formatMoney(num);
        str = str.substring(str.indexOf(".") + 1);
        mVal = Integer.parseInt(str);
        return mVal;
    }

    @OnClick({
            R.id.btn_colorcodeconversion_copyhex,                                                   // 复制HEX
            R.id.btn_colorcodeconversion_copyrgb,                                                   // 复制RGB
            R.id.btn_colorcodeconversion_copycmyk,                                                  // 复制CMYK
            R.id.btn_colorcodeconversion_copyhsv,                                                   // 复制HSV
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_colorcodeconversion_copyhex:                                              // 复制HEX
                String mHexColor = editHex.getText().toString();
                if (!TextUtils.isEmpty(mHexColor)) {
                    ClipboardUtils.copyText(mHexColor);
                    showMessage("复制成功！");
                }
                break;
            case R.id.btn_colorcodeconversion_copyrgb:                                              // 复制RGB
                String mRgbColor = editRgb.getText().toString();
                if (!TextUtils.isEmpty(mRgbColor)) {
                    ClipboardUtils.copyText(mRgbColor);
                    showMessage("复制成功！");
                }
                break;
            case R.id.btn_colorcodeconversion_copycmyk:                                             // 复制CMYK
                String mCmykColor = editCmyk.getText().toString();
                if (!TextUtils.isEmpty(mCmykColor)) {
                    ClipboardUtils.copyText(mCmykColor);
                    showMessage("复制成功！");
                }
                break;
            case R.id.btn_colorcodeconversion_copyhsv:                                              // 复制HSV
                String mHsvColor = editHsv.getText().toString();
                if (!TextUtils.isEmpty(mHsvColor)) {
                    ClipboardUtils.copyText(mHsvColor);
                    showMessage("复制成功！");
                }
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