package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerLoadButtonComponent;
import com.zqw.mobile.grainfull.mvp.contract.LoadButtonContract;
import com.zqw.mobile.grainfull.mvp.presenter.LoadButtonPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.loadingbutton.LoadingButton;
import com.zqw.mobile.grainfull.mvp.ui.widget.textview.DrawableTextView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Description: 带加载效果的按钮
 * <p>
 * Created on 2023/06/30 12:14
 *
 * @author 赤槿
 * module name is LoadButtonActivity
 */
public class LoadButtonActivity extends BaseActivity<LoadButtonPresenter> implements LoadButtonContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.btn_loadbutton_ok)
    LoadingButton loadingBtn;
    @BindView(R.id.btn_loadbutton_cancel)
    Button btCancel;
    @BindView(R.id.btn_loadbutton_fail)
    Button btFail;
    @BindView(R.id.btn_loadbutton_complete)
    Button btComplete;

    @BindView(R.id.txvi_loadbutton_LoadingPosition)
    TextView tvLoadingPosition;
    @BindView(R.id.imvi_loadbutton_EndCompleteDrawableIcon)
    ImageView imEndCompleteDrawableIcon;
    @BindView(R.id.imvi_loadbutton_EndFailDrawableIcon)
    ImageView imEndFailDrawableIcon;

    @BindView(R.id.txvi_loadbutton_LoadingText)
    TextView tvLoadingText;
    @BindView(R.id.txvi_loadbutton_CompleteText)
    TextView tvCompleteText;
    @BindView(R.id.txvi_loadbutton_FailText)
    TextView tvFailText;
    /*------------------------------------------------业务区域------------------------------------------------*/

    private int itemIndexSelected;
    private String editTextString;


    private String loadingText = "Loading";
    private String completeText = "Success";
    private String failText = "Fail";

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoadButtonComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_load_button;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("加载效果的按钮");


        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "load_button_open");

        initLoadingButton();
        initView();
    }

    private void initView() {
        Switch swEnableShrink = findViewById(R.id.view_loadbutton_EnableShrink);
        Switch swEnableRestore = findViewById(R.id.view_loadbutton_EnableRestore);
        Switch swDisableClickOnLoading = findViewById(R.id.view_loadbutton_DisableOnLoading);
        final TextView tvRadiusValue = findViewById(R.id.txvi_loadbutton_RadiusValue);
        SeekBar sbRadius = findViewById(R.id.view_loadbutton_Radius);
        RadioGroup rgShrinkShape = findViewById(R.id.rdio_loadbutton_ShrinkShape);
        final TextView tvLoadingDrawableColorValue = findViewById(R.id.txvi_loadbutton_LoadingDrawableColorValue);
        SeekBar sbLoadingDrawableColor = findViewById(R.id.view_loadbutton_LoadingDrawableColor);
        final TextView tvLoadingStrokeWidthValue = findViewById(R.id.txvi_loadbutton_LoadingStrokeWidthValue);
        final TextView tvShrinkDurationValue = findViewById(R.id.txvi_loadbutton_ShrinkDurationValue);
        SeekBar sbShrinkDuration = findViewById(R.id.view_loadbutton_ShrinkDuration);
        SeekBar sbLoadingStrokeWidth = findViewById(R.id.view_loadbutton_LoadingStrokeWidth);
        tvLoadingStrokeWidthValue.setText(loadingBtn.getLoadingDrawable().getStrokeWidth() + "");
        final TextView tvLoadingEndDrawableSizeValue = findViewById(R.id.txvi_loadbutton_LoadingEndDrawableSizeValue);
        SeekBar sbLoadingEndDrawableSizeValue = findViewById(R.id.view_loadbutton_LoadingEndDrawableSizeValue);
        final TextView tvEndDrawableDurationValue = findViewById(R.id.txvi_loadbutton_EndDrawableDurationValue);
        SeekBar sbEndDrawableDuration = findViewById(R.id.view_loadbutton_EndDrawableDuration);

        swEnableShrink.setOnCheckedChangeListener((buttonView, isChecked) -> {
            loadingBtn.cancel();
            loadingBtn.setEnableShrink(isChecked);
            int defaultStrokeWidth = (int) (loadingBtn.getTextSize() * 0.14f);
            tvLoadingStrokeWidthValue.setText(defaultStrokeWidth + "");
            int loadingSize;
            if (isChecked) {
                loadingSize = (int) loadingBtn.getTextSize() * 2;
                loadingBtn.setLoadingEndDrawableSize(loadingSize);
                loadingBtn.getLoadingDrawable().setStrokeWidth(defaultStrokeWidth);


            } else {
                loadingSize = (int) loadingBtn.getTextSize();
                loadingBtn.setLoadingEndDrawableSize(loadingSize);
                loadingBtn.getLoadingDrawable().setStrokeWidth(loadingSize * 0.14f);
            }
            tvLoadingEndDrawableSizeValue.setText(loadingSize + "");
        });

        swEnableRestore.setOnCheckedChangeListener((buttonView, isChecked) -> {
            loadingBtn.cancel();
            loadingBtn.setEnableRestore(isChecked);
        });

        swDisableClickOnLoading.setOnCheckedChangeListener((buttonView, isChecked) -> loadingBtn.setDisableClickOnLoading(isChecked));


        tvRadiusValue.setText(35 + "");
        sbRadius.setMax(100);
        sbRadius.setProgress(35);
        sbRadius.setOnSeekBarChangeListener(new EmptyOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    loadingBtn.setRadius(progress);
                }
                tvRadiusValue.setText(String.valueOf(progress));
            }
        });

        rgShrinkShape.setOnCheckedChangeListener((group, checkedId) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (checkedId == R.id.radio_loadbutton_Default) {
                    loadingBtn.setShrinkShape(LoadingButton.ShrinkShape.DEFAULT);

                } else if (checkedId == R.id.radio_loadbutton_Oval) {
                    loadingBtn.setShrinkShape(LoadingButton.ShrinkShape.OVAL);
                }
            }
        });


        tvShrinkDurationValue.setText(String.valueOf(500));
        sbShrinkDuration.setMax(3000);
        sbShrinkDuration.setProgress(500);
        sbShrinkDuration.setOnSeekBarChangeListener(new EmptyOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                loadingBtn.setShrinkDuration(progress);
                tvShrinkDurationValue.setText(String.valueOf(progress));
            }
        });


        int loadingDrawableColorValue = loadingBtn.getLoadingDrawable().getColorSchemeColors()[0];
        tvLoadingDrawableColorValue.setText(Integer.toHexString(loadingDrawableColorValue));
        tvLoadingDrawableColorValue.setBackgroundColor(loadingDrawableColorValue);
        sbLoadingDrawableColor.setMax(0xffffff);
        sbLoadingDrawableColor.setProgress(loadingDrawableColorValue - 0xff000000);
        sbLoadingDrawableColor.setOnSeekBarChangeListener(new EmptyOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                loadingBtn.getLoadingDrawable().setColorSchemeColors(progress);
                tvLoadingDrawableColorValue.setText(Integer.toHexString(progress + 0xff000000));
                tvLoadingDrawableColorValue.setBackgroundColor(progress + 0xff000000);
            }
        });

        sbLoadingStrokeWidth.setMax(30);
        sbLoadingStrokeWidth.setProgress((int) loadingBtn.getLoadingDrawable().getStrokeWidth());
        sbLoadingStrokeWidth.setOnSeekBarChangeListener(new EmptyOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                loadingBtn.getLoadingDrawable().setStrokeWidth(progress);
                tvLoadingStrokeWidthValue.setText(String.valueOf(progress));
            }
        });


        tvLoadingEndDrawableSizeValue.setText(loadingBtn.getLoadingEndDrawableSize() + "");
        sbLoadingEndDrawableSizeValue.setMax(250);
        sbLoadingEndDrawableSizeValue.setProgress(loadingBtn.getLoadingEndDrawableSize());
        sbLoadingEndDrawableSizeValue.setOnSeekBarChangeListener(new EmptyOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                loadingBtn.setLoadingEndDrawableSize(progress);
                tvLoadingEndDrawableSizeValue.setText(String.valueOf(progress));
            }
        });


        tvEndDrawableDurationValue.setText(loadingBtn.getEndDrawableDuration() + "");
        sbEndDrawableDuration.setMax(6500);
        sbEndDrawableDuration.setProgress((int) loadingBtn.getEndDrawableDuration());
        sbEndDrawableDuration.setOnSeekBarChangeListener(new EmptyOnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                loadingBtn.setEndDrawableKeepDuration(progress);
                tvEndDrawableDurationValue.setText(String.valueOf(progress));
            }
        });


        tvLoadingPosition = findViewById(R.id.txvi_loadbutton_LoadingPosition);
        imEndCompleteDrawableIcon = findViewById(R.id.imvi_loadbutton_EndCompleteDrawableIcon);
        imEndFailDrawableIcon = findViewById(R.id.imvi_loadbutton_EndFailDrawableIcon);
        tvLoadingText = findViewById(R.id.txvi_loadbutton_LoadingText);
        tvCompleteText = findViewById(R.id.txvi_loadbutton_CompleteText);
        tvFailText = findViewById(R.id.txvi_loadbutton_FailText);
        btCancel = findViewById(R.id.btn_loadbutton_cancel);
        btFail = findViewById(R.id.btn_loadbutton_fail);
        btComplete = findViewById(R.id.btn_loadbutton_complete);

        tvLoadingText.setText("Loading");
        tvCompleteText.setText("Success");
        tvFailText.setText("Fail");
        imEndCompleteDrawableIcon.setImageResource(R.drawable.ic_successful);
        imEndFailDrawableIcon.setImageResource(R.drawable.ic_fail);
    }

    private void initLoadingButton() {
        loadingBtn.cancel();
        loadingBtn.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        loadingBtn.getLoadingDrawable().setStrokeWidth(loadingBtn.getTextSize() * 0.14f);
        loadingBtn.getLoadingDrawable().setColorSchemeColors(loadingBtn.getTextColors().getDefaultColor());
        loadingBtn.setEnableShrink(true)
                .setDisableClickOnLoading(true)
                .setShrinkDuration(450)
                .setLoadingPosition(DrawableTextView.POSITION.START)
                .setSuccessDrawable(R.drawable.ic_successful)
                .setFailDrawable(R.drawable.ic_fail)
                .setEndDrawableKeepDuration(900)
                .setEnableRestore(true)
                .setLoadingEndDrawableSize((int) (loadingBtn.getTextSize() * 2))
                .setOnStatusChangedListener(new LoadingButton.OnStatusChangedListener() {

                    @Override
                    public void onShrinking() {
                        Timber.d("LoadingButton - onShrinking");
                    }

                    @Override
                    public void onLoadingStart() {
                        Timber.d("LoadingButton - onLoadingStart");
                        loadingBtn.setText(loadingText);
                    }

                    @Override
                    public void onLoadingStop() {
                        Timber.d("LoadingButton - onLoadingStop");
                    }

                    @Override
                    public void onEndDrawableAppear(boolean isSuccess, LoadingButton.EndDrawable endDrawable) {
                        Timber.d("LoadingButton - onEndDrawableAppear");
                        if (isSuccess) {
                            loadingBtn.setText(completeText);
                        } else {
                            loadingBtn.setText(failText);
                        }
                    }

                    @Override
                    public void onCompleted(boolean isSuccess) {
                        Timber.d("LoadingButton - onCompleted isSuccess: %s", isSuccess);
                        showMessage(isSuccess ? "Success" : "Fail");
                    }

                    @Override
                    public void onRestored() {
                        Timber.d("LoadingButton - onRestored");
                    }

                    @Override
                    public void onCanceled() {
                        Timber.d("LoadingButton - onCanceled");
                        showMessage("onCanceled");
                    }
                });
    }

    @OnClick({
            R.id.btn_loadbutton_ok,                                                                        // 点击了按钮
            R.id.txvi_loadbutton_LoadingPosition,
            R.id.rela_loadbutton_EndCompleteDrawableIcon,
            R.id.rela_loadbutton_EndFailDrawableIcon,
            R.id.txvi_loadbutton_LoadingText,
            R.id.txvi_loadbutton_CompleteText,
            R.id.txvi_loadbutton_FailText,
            R.id.btn_loadbutton_cancel,
            R.id.btn_loadbutton_fail,
            R.id.btn_loadbutton_complete,
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_loadbutton_ok:
                loadingBtn.start();
                return;
            case R.id.btn_loadbutton_cancel:
                loadingBtn.cancel();
                return;
            case R.id.btn_loadbutton_fail:
                loadingBtn.complete(false);
                return;
            case R.id.btn_loadbutton_complete:
                loadingBtn.complete(true);
                return;
        }

        loadingBtn.cancel();
        switch (v.getId()) {

            case R.id.txvi_loadbutton_LoadingPosition:
                final List<String> items = Arrays.asList("START", "TOP", "END", "BOTTOM");
                final int curIndex = items.indexOf(tvLoadingPosition.getText().toString());

                showSelectDialog("LoadingPosition", (dialog, which) -> {
                    loadingBtn.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    loadingBtn.setLoadingPosition(itemIndexSelected);
                    tvLoadingPosition.setText(items.get(itemIndexSelected));
                    itemIndexSelected = 0;
                }, curIndex, items.toArray(new String[0]));


                break;
            case R.id.txvi_loadbutton_LoadingText:
                showEditDialog("SetLoadingText", (dialog, which) -> {
                    tvLoadingText.setText(editTextString);
                    loadingText = editTextString;
                    editTextString = "";
                });
                break;
            case R.id.txvi_loadbutton_CompleteText:
                showEditDialog("SetCompleteText", (dialog, which) -> {
                    tvCompleteText.setText(editTextString);
                    completeText = editTextString;
                    editTextString = "";
                });
                break;
            case R.id.txvi_loadbutton_FailText:
                showEditDialog("SetFailText", (dialog, which) -> {
                    tvFailText.setText(editTextString);
                    failText = editTextString;
                    editTextString = "";
                });
                break;
        }
    }

    /**
     * 弹出框
     */
    private void showSelectDialog(String title, DialogInterface.OnClickListener onConfirmClickListener,
                                  int checkIndex, String... items) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setSingleChoiceItems(items, checkIndex, (dialog, which) -> itemIndexSelected = which)
                .setNegativeButton("Confirm", onConfirmClickListener);
        builder.create().show();
    }

    /**
     * 弹出框
     */
    private void showEditDialog(String title, DialogInterface.OnClickListener onConfirmClickListener) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_text, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = view.findViewById(R.id.edit_dialogedittext_input);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editTextString = s.toString();
            }
        });

        builder.setTitle(title)
                .setView(view)
                .setNegativeButton("Confirm", onConfirmClickListener);
        builder.create().show();
    }

    static class EmptyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

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