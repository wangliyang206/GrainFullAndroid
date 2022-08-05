package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.UriUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.dialog.AudioDialog;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.di.component.DaggerAudioConversionComponent;
import com.zqw.mobile.grainfull.mvp.contract.AudioConversionContract;
import com.zqw.mobile.grainfull.mvp.presenter.AudioConversionPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:音频转换
 * <p>
 * Created on 2022/08/05 14:46
 *
 * @author 赤槿
 * module name is AudioConversionActivity
 */
public class AudioConversionActivity extends BaseActivity<AudioConversionPresenter> implements AudioConversionContract.View {
    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.activity_audio_conversion)
    LinearLayout contentLayout;

    @BindView(R.id.edit_audioconversion_ac)
    EditText editAC;

    @BindView(R.id.edit_audioconversion_ar)
    EditText editAR;

    /*------------------------------------------业务信息------------------------------------------*/
    // 结果
    private AudioDialog mAudioDialog;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAudioDialog != null) {
            mAudioDialog.dismiss();
            mAudioDialog = null;
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAudioConversionComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_audio_conversion;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("音频转换");

        mAudioDialog = new AudioDialog(this);
    }

    @OnClick({
            R.id.btn_audioconversion_select,                                                        // 选择音频文件
    })
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_audioconversion_select:                                                   // 选择音频文件
                if (inputCheck()) {
                    onOpenFile();
                }
                break;
        }
    }

    /**
     * 校验输入内容
     */
    private boolean inputCheck() {
        if (TextUtils.isEmpty(editAC.getText())) {
            showMessage("请输入声道个数");
            return false;
        }

        if (TextUtils.isEmpty(editAR.getText())) {
            showMessage("请输入采样率");
            return false;
        }

        return true;
    }

    /**
     * 打开文件
     */
    private void onOpenFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // 选择音频/视频 （mp4 3gp 是android支持的视频格式）
        intent.setType("audio/*;video/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, Constant.REQUEST_SELECT_IMAGES_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.REQUEST_SELECT_IMAGES_CODE) {
                // 获取文件路径
                Uri uri = data.getData();

                // 弹出Dialog，可以播放与保存。
                if (mAudioDialog != null) {
                    mAudioDialog.setPlayPath(UriUtils.uri2File(uri).getPath(), editAC.getText().toString(), editAR.getText().toString());
                    mAudioDialog.showAtLocation(contentLayout, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
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