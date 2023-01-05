package com.zqw.mobile.grainfull.mvp.ui.activity;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.utils.ArmsUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import com.zqw.mobile.grainfull.app.utils.ColorsUtil;
import com.zqw.mobile.grainfull.di.component.DaggerPicturePipetteComponent;
import com.zqw.mobile.grainfull.mvp.contract.PicturePipetteContract;
import com.zqw.mobile.grainfull.mvp.presenter.PicturePipettePresenter;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.ColorPickerPreference;
import com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.ColorPickerView;
import com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.ImageColorPickerView;
import com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.builder.ColorPickerClickListener;
import com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.builder.ColorPickerDialogBuilder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:图片吸管(图片中提取颜色)
 * <p>
 * Created on 2023/01/05 11:12
 *
 * @author 赤槿
 * module name is PicturePipetteActivity
 */
public class PicturePipetteActivity extends BaseActivity<PicturePipettePresenter> implements PicturePipetteContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_picture_pipette)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.view_picturepipette_image)
    ImageColorPickerView viewImage;

    @BindView(R.id.txvi_picturepipette_show)
    TextView txviShow;
    /*------------------------------------------------业务区域------------------------------------------------*/

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPicturePipetteComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_picture_pipette;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("图片吸管");

        viewImage.setOnColorChangedListenner(new ImageColorPickerView.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                txviShow.setText(ColorsUtil.getHexString(color, false));
                txviShow.setBackgroundColor(color);
            }
        });
    }

//    @OnClick({
//            R.id.btn_picturepipette_click,                                                          // 点击
//    })
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btn_picturepipette_click:                                                     // 点击
//
//                break;
//        }
//    }

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