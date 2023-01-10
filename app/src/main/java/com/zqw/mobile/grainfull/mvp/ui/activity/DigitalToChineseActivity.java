package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.ChineseNumUtils;
import com.zqw.mobile.grainfull.di.component.DaggerDigitalToChineseComponent;
import com.zqw.mobile.grainfull.mvp.contract.DigitalToChineseContract;
import com.zqw.mobile.grainfull.mvp.presenter.DigitalToChinesePresenter;

import butterknife.BindView;

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
    TextView txviLowercase;                                                                         // 小写
    @BindView(R.id.txvi_digitaltochinese_capital)
    TextView txviCapital;                                                                           // 大写
    @BindView(R.id.txvi_digitaltochinese_amount)
    TextView txviAmount;                                                                            // 金额

    /*------------------------------------------------业务区域------------------------------------------------*/

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

        editInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    // 没有数字，则隐藏布局
                    lilaResult.setVisibility(View.GONE);
                } else {
                    // 有数字，显示布局
                    lilaResult.setVisibility(View.VISIBLE);
                    txviAmount.setText(ChineseNumUtils.upperRMB(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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