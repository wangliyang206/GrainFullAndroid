package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.dialog.PopupSelectList;
import com.zqw.mobile.grainfull.app.tts.SynthActivity;
import com.zqw.mobile.grainfull.app.utils.RxUtils;
import com.zqw.mobile.grainfull.di.component.DaggerTranslateComponent;
import com.zqw.mobile.grainfull.mvp.contract.TranslateContract;
import com.zqw.mobile.grainfull.mvp.model.entity.Translate;
import com.zqw.mobile.grainfull.mvp.presenter.TranslatePresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.loadingbutton.LoadingButton;
import com.zqw.mobile.grainfull.mvp.ui.widget.textview.DrawableTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Description: 翻译
 * <p>
 * Created on 2023/06/29 09:30
 *
 * @author 赤槿
 * module name is TranslateActivity
 */
public class TranslateActivity extends BaseActivity<TranslatePresenter> implements TranslateContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_translate)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.txvi_translate_before)
    TextView txviBefore;                                                                            // 原文
    @BindView(R.id.txvi_translate_after)
    TextView txviAfter;                                                                             // 译文

    @BindView(R.id.edit_translate_input)
    EditText editInput;                                                                             // 输入的中文
    @BindView(R.id.imvi_translate_cleanup)
    ImageView imviCleanUp;                                                                          // 清扫

    @BindView(R.id.txvi_translate_value)
    TextView txviValue;                                                                             // 结果
    @BindView(R.id.btn_translate_submit)
    LoadingButton btnSubmit;                                                                        // 翻译按钮

    /*------------------------------------------------业务区域------------------------------------------------*/
    private SynthActivity synthActivity;
    // 选择器
    private PopupSelectList mPopup;

    @Override
    protected void onDestroy() {
        KeyboardUtils.unregisterSoftInputChangedListener(getWindow());

        if (synthActivity != null) {
            synthActivity.onDestroy();
            synthActivity = null;
        }

        Translate.onDestroy();
        super.onDestroy();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTranslateComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_translate;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("翻译");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "translate_open");

        // 初始化语音播报
        synthActivity = new SynthActivity();
        synthActivity.initTTS(getApplicationContext());
        editInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                imviCleanUp.setVisibility(TextUtils.isEmpty(editInput.getText().toString()) ? View.GONE : View.VISIBLE);
            }
        });

        // 初始化数据源
        Translate.onInit();

        initButton();
    }

    /**
     * 初始化按钮
     */
    private void initButton() {
        btnSubmit.setEnableShrink(true)
                .setDisableClickOnLoading(true)
                .setShrinkDuration(450)
                .setLoadingPosition(DrawableTextView.POSITION.START)
                .setSuccessDrawable(R.drawable.ic_successful)
                .setFailDrawable(R.drawable.ic_fail)
                .setEndDrawableKeepDuration(900)
                .setEnableRestore(true)
                .setLoadingEndDrawableSize((int) (btnSubmit.getTextSize() * 2))
                .setOnStatusChangedListener(new LoadingButton.OnStatusChangedListener() {

                    @Override
                    public void onShrinking() {
                        Timber.d("LoadingButton - onShrinking");
                    }

                    @Override
                    public void onLoadingStart() {
                        Timber.d("LoadingButton - onLoadingStart");
                        btnSubmit.setText("Loading");
                    }

                    @Override
                    public void onLoadingStop() {
                        Timber.d("LoadingButton - onLoadingStop");
                    }

                    @Override
                    public void onEndDrawableAppear(boolean isSuccess, LoadingButton.EndDrawable endDrawable) {
                        Timber.d("LoadingButton - onEndDrawableAppear");
                        if (isSuccess) {
                            btnSubmit.setText("Success");
                        } else {
                            btnSubmit.setText("Fail");
                        }
                    }

                    @Override
                    public void onCompleted(boolean isSuccess) {
                        Timber.d("LoadingButton - onCompleted isSuccess: %s", isSuccess);
//                        showMessage(isSuccess ? "Success" : "Fail");
                    }

                    @Override
                    public void onRestored() {
                        Timber.d("LoadingButton - onRestored");
                    }

                    @Override
                    public void onCanceled() {
                        Timber.d("LoadingButton - onCanceled");
//                        showMessage("onCanceled");
                    }
                });
    }

    /**
     * 初始化选择器
     */
    private void initPop(boolean isAfter) {
        PopupSelectList.ItemClick itemClick = (position, info) -> {
            if (isAfter) {
                txviAfter.setText(info);
                txviAfter.setTag(Translate.getLanguageKey(true).get(position));
            } else {
                txviBefore.setText(info);
                txviBefore.setTag(Translate.getLanguageKey(false).get(position));
            }
        };
        mPopup = new PopupSelectList(this, "选择语言", Translate.getLanguageValue(isAfter), itemClick);
        mPopup.showAtLocation(contentLayout, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 翻译结果
     */
    @Override
    public void loadContent(boolean isSucc, String value) {
        // 判断是否翻译成功
        if (isSucc) {
            // 翻译成功，加载结果
            txviValue.setText(value);
            // 告诉按钮成功了。
            btnSubmit.complete(true);
        } else {
            // 告诉按钮失败了。
            btnSubmit.complete(false);
            showMessage(value);
        }
    }

    @OnClick({
            R.id.txvi_translate_before,                                                             // 输入的内容是什么样的？
            R.id.txvi_translate_after,                                                              // 想翻译成什么样的？
            R.id.txvi_translate_interchange,                                                        // 互换
            R.id.btn_translate_submit,                                                              // 翻译
            R.id.imvi_translate_play,                                                               // 播放
            R.id.imvi_translate_copy,                                                               // 复制
            R.id.imvi_translate_cleanup,                                                            // 清除
    })
    @Override
    public void onClick(View v) {
        hideInput();
        String mValue = txviValue.getText().toString();
        switch (v.getId()) {
            case R.id.txvi_translate_before:                                                        // 输入的内容是什么样的？
                initPop(false);
                break;
            case R.id.txvi_translate_after:                                                         // 想翻译成什么样的？
                initPop(true);
                break;
            case R.id.txvi_translate_interchange:                                                   // 互换
                String mBefore = txviBefore.getText().toString();
                String mBeforeTag = txviBefore.getTag().toString();
                String mAfter = txviAfter.getText().toString();
                String mAfterTag = txviAfter.getTag().toString();

                txviBefore.setText(mAfter);
                txviBefore.setTag(mAfterTag);
                txviAfter.setText(mBefore);
                txviAfter.setTag(mBeforeTag);
                break;
            case R.id.btn_translate_submit:                                                         // 翻译
                String str = editInput.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    showMessage("请输入要翻译的文字！");
                    return;
                }

                // 开启效果
                if (btnSubmit != null)
                    btnSubmit.start();

                // 延迟一秒后执行
                RxUtils.startDelayed(1, this, () -> {
                    // 执行翻译
                    if (mPresenter != null) {
                        mPresenter.translate(str.trim(), txviBefore.getTag().toString(), txviAfter.getTag().toString());
                    }
                });
                break;
            case R.id.imvi_translate_play:                                                          // 播放
                if (!TextUtils.isEmpty(mValue)) {
                    List<Pair<String, String>> texts = new ArrayList<>();
                    texts.add(new Pair<>(mValue, "a1"));

                    if (synthActivity != null) {
                        synthActivity.batchSpeak(texts);
                    }
                }
                break;
            case R.id.imvi_translate_copy:                                                          // 复制
                if (!TextUtils.isEmpty(mValue)) {
                    ClipboardUtils.copyText(mValue);
                    showMessage("复制成功！");
                }
                break;
            case R.id.imvi_translate_cleanup:                                                       // 清除
                editInput.setText("");
                break;
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideInput() {
        KeyboardUtils.hideSoftInput(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideInput();
        return super.onTouchEvent(event);
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