package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
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
import com.zqw.mobile.grainfull.app.utils.ChineseNumUtils;
import com.zqw.mobile.grainfull.di.component.DaggerDigitalToChineseComponent;
import com.zqw.mobile.grainfull.mvp.contract.DigitalToChineseContract;
import com.zqw.mobile.grainfull.mvp.presenter.DigitalToChinesePresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:数字转中文
 * <p>
 * Created on 2023/01/10 16:38
 *
 * @author 赤槿
 * module name is DigitalToChineseActivity
 */
public class DigitalToChineseActivity extends BaseActivity<DigitalToChinesePresenter> implements DigitalToChineseContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_digital_to_chinese)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.edit_digitaltochinese_text)
    EditText editInput;                                                                             // 输入的数字
    @BindView(R.id.lila_digitaltochinese_result)
    LinearLayout lilaResult;                                                                        // 总布局，显示结果
    @BindView(R.id.txvi_digitaltochinese_lowercase)
    TextView txviLowercase;                                                                         // 简体小写
    @BindView(R.id.txvi_digitaltochinese_capital)
    TextView txviCapital;                                                                           // 简体大写
    @BindView(R.id.txvi_digitaltochinese_capitalamount)
    TextView txviCapitalAmount;                                                                     // 大写金额

    /*------------------------------------------------业务区域------------------------------------------------*/

    @Override
    protected void onDestroy() {
        KeyboardUtils.unregisterSoftInputChangedListener(getWindow());
        super.onDestroy();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDigitalToChineseComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_digital_to_chinese;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("数字转中文");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "digital_to_chinese");

        // 增加输入监听
        editInput.addTextChangedListener(mTextWatcher);
    }

    @OnClick({
            R.id.lila_digitaltochinese_lowercase,                                                   // 复制 简体小写
            R.id.lila_digitaltochinese_capital,                                                     // 复制 简体大写
            R.id.lila_digitaltochinese_capitalamount,                                               // 复制 金额
    })
    @Override
    public void onClick(View v) {
        hideInput();
        switch (v.getId()) {
            case R.id.lila_digitaltochinese_lowercase:                                              // 复制 简体小写
                String mLowercase = txviLowercase.getText().toString();
                if (!TextUtils.isEmpty(mLowercase)) {
                    ClipboardUtils.copyText(mLowercase);
                    showMessage("复制成功！");
                }
                break;
            case R.id.lila_digitaltochinese_capital:                                                // 复制 简体大写
                String mCapital = txviCapital.getText().toString();
                if (!TextUtils.isEmpty(mCapital)) {
                    ClipboardUtils.copyText(mCapital);
                    showMessage("复制成功！");
                }
                break;
            case R.id.lila_digitaltochinese_capitalamount:                                          // 复制 金额
                String mAmount = txviCapitalAmount.getText().toString();
                if (!TextUtils.isEmpty(mAmount)) {
                    ClipboardUtils.copyText(mAmount);
                    showMessage("复制成功！");
                }
                break;
        }
    }

    /**
     * 输入监听
     */
    private final TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s)) {
                // 没有数字，则隐藏布局
                lilaResult.setVisibility(View.GONE);
            } else {
                // 判断第一位如果是“.”的话，则禁止输入
                String str = s.subSequence(0, 1).toString();
                if (str.equalsIgnoreCase(".")) {
                    editInput.removeTextChangedListener(mTextWatcher);
                    editInput.setText("");
                    editInput.addTextChangedListener(mTextWatcher);
                } else {
                    // 有数字，显示布局
                    lilaResult.setVisibility(View.VISIBLE);

                    // 简体小写
                    txviLowercase.setText(ChineseNumUtils.numberToChinese(false, s.toString()));
                    // 简体大写
                    txviCapital.setText(ChineseNumUtils.numberToChinese(true, s.toString()));
                    // 金额
                    String capitalAmount = ChineseNumUtils.convert(Double.parseDouble(s.toString()));
                    if (TextUtils.isEmpty(capitalAmount)) {
                        showMessage("数字太大，不能完成转换！");
                        txviCapitalAmount.setText("");
                    } else {
                        txviCapitalAmount.setText(capitalAmount);
                    }

                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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