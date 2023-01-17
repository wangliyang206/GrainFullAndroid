package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerChineseToPinyinComponent;
import com.zqw.mobile.grainfull.mvp.contract.ChineseToPinyinContract;
import com.zqw.mobile.grainfull.mvp.presenter.ChineseToPinyinPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:中文转拼音
 * <p>
 * Created on 2023/01/11 11:03
 *
 * @author 赤槿
 * module name is ChineseToPinyinActivity
 */
public class ChineseToPinyinActivity extends BaseActivity<ChineseToPinyinPresenter> implements ChineseToPinyinContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_chinese_to_pinyin)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.edit_chinesetopinyin_text)
    EditText editInput;                                                                             // 输入的中文

    @BindView(R.id.radio_chinesetopinyin_group)
    RadioGroup radioGroup;                                                                          // 是否全拼音

    @BindView(R.id.chbo_chinesetopinyin_symbols)
    CheckBox mCheckBox;                                                                             // 是否显示音标

    @BindView(R.id.txvi_chinesetopinyin_result)
    TextView txviResult;                                                                            // 结果

    /*------------------------------------------------业务区域------------------------------------------------*/

    @Override
    protected void onDestroy() {
        KeyboardUtils.unregisterSoftInputChangedListener(getWindow());
        super.onDestroy();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerChineseToPinyinComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_chinese_to_pinyin;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("中文转拼音");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "chinese_to_pinyin_open");

        // 监听
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_chinesetopinyin_all) {
                // 全拼音
                mCheckBox.setVisibility(View.VISIBLE);
            } else {
                // 首字母
                mCheckBox.setVisibility(View.GONE);
            }
        });
    }

    @OnClick({
            R.id.btn_chinesetopinyin_convert,                                                       // 转换
            R.id.txvi_chinesetopinyin_result,                                                       // 结果
    })
    @Override
    public void onClick(View v) {
        hideInput();
        switch (v.getId()) {
            case R.id.btn_chinesetopinyin_convert:                                                  // 转换
                String mInput = editInput.getText().toString();
                if (TextUtils.isEmpty(mInput)) {
                    showMessage("请输入汉字！");
                    return;
                }

                // 友盟统计 - 自定义事件
                MobclickAgent.onEvent(getApplicationContext(), "chinese_to_pinyin");

                txviResult.setVisibility(View.VISIBLE);

                try {
                    if (radioGroup.getCheckedRadioButtonId() == R.id.radio_chinesetopinyin_all) {
                        // 全拼音
                        if (mCheckBox.isChecked()) {
                            // 有音标
                            mInput = PinyinHelper.convertToPinyinString(mInput, " ");
                        } else {
                            // 无音标
                            mInput = PinyinHelper.convertToPinyinString(mInput, " ", PinyinFormat.WITHOUT_TONE);
                        }
                    } else {
                        // 首字母
                        mInput = PinyinHelper.getShortPinyin(mInput);
                    }

                    txviResult.setText(mInput);
                } catch (PinyinException e) {
                    e.printStackTrace();
                    txviResult.setText("翻译失败！");
                }
                break;
            case R.id.txvi_chinesetopinyin_result:                                                  // 结果
                String mResult = txviResult.getText().toString();
                if (!TextUtils.isEmpty(mResult)) {
                    ClipboardUtils.copyText(mResult);
                    showMessage("复制成功！");
                }
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