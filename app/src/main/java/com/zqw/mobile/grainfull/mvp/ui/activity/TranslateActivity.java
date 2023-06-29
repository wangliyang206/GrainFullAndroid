package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ClipboardUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.dialog.PopupSelectList;
import com.zqw.mobile.grainfull.app.tts.SynthActivity;
import com.zqw.mobile.grainfull.di.component.DaggerTranslateComponent;
import com.zqw.mobile.grainfull.mvp.contract.TranslateContract;
import com.zqw.mobile.grainfull.mvp.model.entity.Translate;
import com.zqw.mobile.grainfull.mvp.presenter.TranslatePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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

    @BindView(R.id.txvi_translate_value)
    TextView txviValue;                                                                             // 结果

    /*------------------------------------------------业务区域------------------------------------------------*/
    private SynthActivity synthActivity;
    // 选择器
    private PopupSelectList mPopup;

    @Override
    protected void onDestroy() {
        if (synthActivity != null) {
            synthActivity.onDestroy();
            synthActivity = null;
        }

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

        // 初始化语音播报
        synthActivity = new SynthActivity();
        synthActivity.initTTS(getApplicationContext(), true);

        // 初始化数据源
        Translate.init();
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
    public void loadContent(String value) {
        txviValue.setText(value);
    }

    @OnClick({
            R.id.txvi_translate_before,                                                             // 输入的内容是什么样的？
            R.id.txvi_translate_after,                                                              // 想翻译成什么样的？
            R.id.txvi_translate_interchange,                                                        // 互换
            R.id.imvi_translate_ok,                                                                 // 翻译
            R.id.imvi_translate_play,                                                               // 播放
            R.id.imvi_translate_copy,                                                               // 复制
    })
    @Override
    public void onClick(View v) {
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
            case R.id.imvi_translate_ok:                                                            // 翻译
                String str = editInput.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    showMessage("请输入要翻译的文字！");
                    return;
                }

                // 执行翻译
                if (mPresenter != null) {
                    mPresenter.translate(str, txviBefore.getTag().toString(), txviAfter.getTag().toString());
                }
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