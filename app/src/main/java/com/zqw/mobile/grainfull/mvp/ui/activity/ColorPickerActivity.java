package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ClipboardUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.ColorsUtil;
import com.zqw.mobile.grainfull.di.component.DaggerColorPickerComponent;
import com.zqw.mobile.grainfull.mvp.contract.ColorPickerContract;
import com.zqw.mobile.grainfull.mvp.presenter.ColorPickerPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.ColorPickerView;
import com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.OnColorSelectedListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:取色板
 * <p>
 * Created on 2023/01/03 17:48
 *
 * @author 赤槿
 * module name is ColorPickerActivity
 */
public class ColorPickerActivity extends BaseActivity<ColorPickerPresenter> implements ColorPickerContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_color_picker)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.color_picker_view)
    ColorPickerView colorPickerView;

    @BindView(R.id.view_colorpicker_bg)
    View viewBg;

    @BindView(R.id.txvi_colorpicker_color)
    TextView txviColor;
    /*------------------------------------------------业务区域------------------------------------------------*/

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerColorPickerComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_color_picker;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("取色板");

        colorPickerView.addOnColorChangedListener(selectedColor -> {
            // 颜色变化时的手柄
            viewBg.setBackgroundColor(selectedColor);
            txviColor.setText(ColorsUtil.getHexString(selectedColor, false));
        });
        colorPickerView.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {

            }
        });
    }

    @OnClick({
            R.id.btn_colorpicker_copy,                                                              // 复制按钮
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_colorpicker_copy:                                                         // 复制按钮
                String mColor = txviColor.getText().toString();
                if (!TextUtils.isEmpty(mColor)) {
                    ClipboardUtils.copyText(mColor);
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