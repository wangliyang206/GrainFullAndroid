package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.ColorsUtil;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.di.component.DaggerColorCodeConversionComponent;
import com.zqw.mobile.grainfull.mvp.contract.ColorCodeConversionContract;
import com.zqw.mobile.grainfull.mvp.presenter.ColorCodeConversionPresenter;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

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
    @BindView(R.id.edit_colorcodeconversion_computer)
    EditText editComputer;                                                                          // 显示的计算机颜色

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

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "color_code_conversion");

        setCurrentColor(0, isAlpha(mDefaultColor), mDefaultColor);
    }

    /**
     * 是否透明
     */
    private boolean isAlpha(int mColor) {
        // 00是完全透明， ff是完全不透明(255)
        return Color.alpha(mColor) != 255;
    }

    /**
     * 设置当前颜色
     *
     * @param type    0代表第一次进来时加载默认颜色；1代表除了HEX不动其余需要重新赋值；2代表除了RGB不动其余需要重新赋值；3代表除了CMYK不动其余需要重新赋值；4代表除了HSV不动其余需要重新赋值；5代表除了计算机代码其余需要重新赋值；
     * @param isAlpha 是否有透明度
     * @param mColor  颜色值
     */
    private void setCurrentColor(int type, boolean isAlpha, int mColor) {
        // 去除监听
        editHex.removeTextChangedListener(mHex);
        editRgb.removeTextChangedListener(mRgb);
        editCmyk.removeTextChangedListener(mCmyk);
        editHsv.removeTextChangedListener(mHsv);
        editComputer.removeTextChangedListener(mComputer);

        // 显示背景颜色
        viewCard.setCardBackgroundColor(mColor);

        if (type != 1) {
            // 显示十六进制的颜色
            editHex.setText(ColorsUtil.getHexString(mColor, isAlpha));
        }

        // 显示 RGB 颜色
        int a = Color.alpha(mColor);
        int r = Color.red(mColor);
        int g = Color.green(mColor);
        int b = Color.blue(mColor);
        if (type != 2) {
            editRgb.setText(r + "," + g + "," + b);
        }

        // 显示 CMYK 颜色
        if (type != 3) {
            if ((r == 0) && (g == 0) && (b == 0)) {
                editCmyk.setText(0 + "," + 0 + "," + 0 + "," + 1);
            } else {
                float[] mCmyk = ColorsUtil.getCMYK(r, g, b);
                editCmyk.setText(format(mCmyk[0]) + "," + format(mCmyk[1]) + "," + format(mCmyk[2]) + "," + format(mCmyk[3]));
            }
        }

        // 显示 HSV 颜色
        if (type != 4) {
            float[] hsv = new float[3];
            Color.RGBToHSV(r, g, b, hsv);
            editHsv.setText(
                    mDecimalFormat.format(hsv[0])
                            + "," +
                            mDecimalFormat.format(hsv[1])
                            + "," +
                            mDecimalFormat.format(hsv[2]));
        }

        // 显示 计算机颜色代码
        if (type != 5) {
            editComputer.setText("0x" + Integer.toHexString(mColor));
        }

        // 增加监听
        // HEX 监听
        editHex.addTextChangedListener(mHex);
        // RGB 监听
        editRgb.addTextChangedListener(mRgb);
        // CMYK 监听
        editCmyk.addTextChangedListener(mCmyk);
        // HSV 监听
        editHsv.addTextChangedListener(mHsv);
        // 计算机颜色代码 监听
        editComputer.addTextChangedListener(mComputer);
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
            R.id.btn_colorcodeconversion_copycomputer,                                              // 复制计算机颜色代码
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
            case R.id.btn_colorcodeconversion_copycomputer:                                         // 复制计算机颜色代码
                String mComputerColor = editComputer.getText().toString();
                if (!TextUtils.isEmpty(mComputerColor)) {
                    ClipboardUtils.copyText(mComputerColor);
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

    /**
     * HEX 监听
     */
    private final TextWatcher mHex = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                int mColor = Color.parseColor(s.toString());
                setCurrentColor(1, isAlpha(mColor), mColor);
            } catch (Exception ex) {
                // 位数不足时会闪退，这时不用管
                Timber.e("####mHex-onTextChanged-ex=%s", ex.getMessage());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * RGB 监听
     */
    private final TextWatcher mRgb = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                String val = s.toString();
                if (TextUtils.isEmpty(val)) {
                    setCurrentColor(2, false, Color.parseColor("#FFFFFF"));
                } else {
                    String[] mSplit = val.split(",");
                    String[] rgb = new String[3];

                    if (mSplit.length == 3) {
                        rgb[0] = mSplit[0];
                        rgb[1] = mSplit[1];
                        rgb[2] = mSplit[2];
                    } else if (mSplit.length == 2) {
                        rgb[0] = mSplit[0];
                        rgb[1] = mSplit[1];
                        rgb[2] = "0";
                    } else {
                        rgb[0] = mSplit[0];
                        rgb[1] = "0";
                        rgb[2] = "0";
                    }


                    setCurrentColor(2, false, Color.rgb(
                            Integer.parseInt(rgb[0]),
                            Integer.parseInt(rgb[1]),
                            Integer.parseInt(rgb[2])
                    ));
                }

            } catch (Exception ex) {
                // 这时不用管
                Timber.e("####mRgb-onTextChanged-ex=%s", ex.getMessage());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * CMYK 监听
     */
    private final TextWatcher mCmyk = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                String val = s.toString();
                // 判断文本框内是否有内容，如果没有则无需操作
                if (!TextUtils.isEmpty(val)) {
                    // 有内容，将内容格式化成四份。
                    String[] mSplit = val.split(",");
                    // 不够四份，则不做处理
                    if (mSplit.length == 4) {
                        // 将cmyk转化成rgb
                        int[] mRgb = ColorsUtil.getRGB(Float.parseFloat(mSplit[0]), Float.parseFloat(mSplit[1]), Float.parseFloat(mSplit[2]), Float.parseFloat(mSplit[3]));
                        // 显示
                        setCurrentColor(3, false, Color.rgb(mRgb[0], mRgb[1], mRgb[2]));
                    }
                }

            } catch (Exception ex) {
                // 这时不用管
                Timber.e("####mCmyk-onTextChanged-ex=%s", ex.getMessage());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * HSV 监听
     */
    private final TextWatcher mHsv = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                String val = s.toString();
                // 判断文本框内是否有内容，如果没有则无需操作
                if (!TextUtils.isEmpty(val)) {
                    // 有内容，将内容格式化成三份。
                    String[] mSplit = val.split(",");
                    float[] mHsv = new float[3];
                    mHsv[0] = Float.parseFloat(mSplit[0]);
                    mHsv[1] = Float.parseFloat(mSplit[1]);
                    mHsv[2] = Float.parseFloat(mSplit[2]);

                    // 不够三份，则不做处理
                    if (mSplit.length == 3) {
                        // 显示
                        setCurrentColor(4, false, Color.HSVToColor(mHsv));
                    }
                }

            } catch (Exception ex) {
                // 这时不用管
                Timber.e("####mHsv-onTextChanged-ex=%s", ex.getMessage());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 计算机颜色代码 监听
     */
    private final TextWatcher mComputer = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
//                0x330000FF
                // 转大写，然后截取字符串，0X
                String str = s.toString().toUpperCase().replace("0X", "");
                // 截取字符串，33
                String val = str.substring(0, 2);
                // 获取透明度
                int mAlpha = Integer.parseInt(val, 16);
                // 截取字符串，0000FF
                val = str.substring(2);

                // 颜色值#，转成 int;
                int mColor = Color.parseColor("#" + val);
                int r = Color.red(mColor);
                int g = Color.green(mColor);
                int b = Color.blue(mColor);
                setCurrentColor(5, mAlpha != 255, Color.argb(mAlpha, r, g, b));
            } catch (Exception ex) {
                // 位数不足时会闪退，这时不用管
                Timber.e("####mComputer-onTextChanged-ex=%s", ex.getMessage());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}