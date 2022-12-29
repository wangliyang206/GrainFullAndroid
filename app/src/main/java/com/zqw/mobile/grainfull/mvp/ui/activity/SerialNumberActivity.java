package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ReplacementTransformationMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.di.component.DaggerSerialNumberComponent;
import com.zqw.mobile.grainfull.mvp.contract.SerialNumberContract;
import com.zqw.mobile.grainfull.mvp.presenter.SerialNumberPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:仿序列号(错误时带震颤效果)
 * <p>
 * Created on 2022/07/14 17:11
 *
 * @author 赤槿
 * module name is SerialNumberActivity
 */
public class SerialNumberActivity extends BaseActivity<SerialNumberPresenter> implements SerialNumberContract.View {
    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.edit_serialnumber_one)
    EditText editOne;
    @BindView(R.id.view_serialnumber_one)
    View viewOne;

    @BindView(R.id.edit_serialnumber_two)
    EditText editTwo;
    @BindView(R.id.view_serialnumber_two)
    View viewTwo;

    @BindView(R.id.edit_serialnumber_three)
    EditText editThree;
    @BindView(R.id.view_serialnumber_three)
    View viewThree;

    @BindView(R.id.edit_serialnumber_four)
    EditText editFour;
    @BindView(R.id.view_serialnumber_four)
    View viewFour;

    @BindView(R.id.txvi_serialnumber_error)
    TextView txviError;
    @BindView(R.id.btn_serialnumber_activation)
    Button btnActivation;
    /*------------------------------------------业务信息------------------------------------------*/

    /**
     * 将状态栏改为浅色、深色模式(状态栏 icon 和字体，false = 浅色，true = 深色)
     */
    public boolean useLightStatusBar() {
        return false;
    }

    /**
     * 根据主题使用不同的颜色。
     * 如果想要纯透明，则需要重写此方法，返回值为 -1 即可。
     */
    public int useStatusBarColor() {
        return getResources().getColor(R.color.colorPrimary);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSerialNumberComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_serial_number;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("仿序列号");

        initView();
        initActivation();
    }

    private void initView() {
        editTwo.setFocusable(false);
        editTwo.setFocusableInTouchMode(false);
        editTwo.requestFocus();

        editThree.setFocusable(false);
        editThree.setFocusableInTouchMode(false);
        editThree.requestFocus();

        editFour.setFocusable(false);
        editFour.setFocusableInTouchMode(false);
        editFour.requestFocus();
    }

    /**
     * 激活
     */
    private void initActivation() {
        editOne.setTransformationMethod(new AllCapTransformationMethod(true));
        editTwo.setTransformationMethod(new AllCapTransformationMethod(true));
        editThree.setTransformationMethod(new AllCapTransformationMethod(true));
        editFour.setTransformationMethod(new AllCapTransformationMethod(true));
        editOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editOne.length() == 0) {
                    viewOne.setBackgroundColor(Color.parseColor("#666666"));
                } else if (editOne.length() == 4) {
                    viewOne.setBackgroundColor(Color.parseColor("#666666"));
                    editTwo.setFocusable(true);
                    editTwo.setFocusableInTouchMode(true);
                    editTwo.requestFocus();
                    editTwo.setText(editTwo.getText().toString().trim() + " ");
                    editTwo.setSelection(editTwo.getText().length());
                } else if (editOne.length() < 4) {
                    viewOne.setBackgroundColor(getResources().getColor(R.color.btn_normal_color));
                }
            }
        });
        editTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editTwo.length() == 0) {
                    editOne.setFocusable(true);
                    editOne.setFocusableInTouchMode(true);
                    editOne.requestFocus();
                    viewTwo.setBackgroundColor(Color.parseColor("#666666"));
                } else if (editTwo.getText().toString().trim().length() == 4) {
                    viewTwo.setBackgroundColor(Color.parseColor("#666666"));
                    editThree.setFocusable(true);
                    editThree.setFocusableInTouchMode(true);
                    editThree.requestFocus();
                    editThree.setText(editThree.getText().toString().trim() + " ");
                    editThree.setSelection(editThree.getText().length());
                } else if (editTwo.getText().toString().trim().length() < 4) {
                    viewTwo.setBackgroundColor(getResources().getColor(R.color.btn_normal_color));
                }


            }
        });
        editThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editThree.length() == 0) {
                    editTwo.setFocusable(true);
                    editTwo.setFocusableInTouchMode(true);
                    editTwo.requestFocus();
                    viewThree.setBackgroundColor(Color.parseColor("#666666"));

                } else if (editThree.getText().toString().trim().length() == 4) {
                    viewThree.setBackgroundColor(Color.parseColor("#666666"));
                    editFour.setFocusable(true);
                    editFour.setFocusableInTouchMode(true);
                    editFour.requestFocus();
                    editFour.setText(editFour.getText().toString().trim() + " ");
                    editFour.setSelection(editFour.getText().length());
                } else if (editThree.getText().toString().trim().length() < 4) {
                    viewThree.setBackgroundColor(getResources().getColor(R.color.btn_normal_color));
                }
            }
        });
        editFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editFour.length() == 0) {
                    editThree.setFocusable(true);
                    editThree.setFocusableInTouchMode(true);
                    editThree.requestFocus();
                    viewFour.setBackgroundColor(Color.parseColor("#666666"));
                } else if (editFour.getText().toString().trim().length() == 4) {
                    viewFour.setBackgroundColor(Color.parseColor("#666666"));
                    btnActivation.setEnabled(true);
                } else if (editFour.getText().toString().trim().length() < 4) {
                    viewFour.setBackgroundColor(getResources().getColor(R.color.btn_normal_color));
                    btnActivation.setEnabled(false);
                }
            }
        });
    }

    @OnClick({
            R.id.btn_serialnumber_activation,                                                       // 激活
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_serialnumber_activation:                                                  // 激活
                if (!CommonUtils.isDoubleClick()) {
                    // 最终拼接的序列号
                    String end = "";

                    if (editOne.getText().toString().trim().length() == 4 &&
                            editTwo.getText().toString().trim().length() == 4
                            && editThree.getText().toString().trim().length() == 4
                            && editFour.getText().toString().trim().length() == 4) {
                        String et_one = editOne.getText().toString().trim();
                        String et_two = editTwo.getText().toString().trim();
                        String et_three = editThree.getText().toString().trim();
                        String et_four = editFour.getText().toString().trim();
                        end = et_one + "-" + et_two + "-" + et_three + "-" + et_four;
                    }

                    if (end.equalsIgnoreCase("AAAA-BBBB-CCCC-DDDD")) {
                        // 序列号正确
                        txviError.setText("");
                        showMessage("恭喜您，已激活！");
                    } else {
                        // 序列号不正确
                        txviError.setText("序列号有误，请重新输入");
                        initShake();
                    }

                }
                break;
        }
    }

    /**
     * 为组件设置一个抖动效果
     */
    private void initShake() {
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        editOne.startAnimation(shake);
        editTwo.startAnimation(shake);
        editThree.startAnimation(shake);
        editFour.startAnimation(shake);
        // 改变view的颜色
        viewOne.setBackgroundColor(Color.parseColor("#F34B56"));
        viewTwo.setBackgroundColor(Color.parseColor("#FF0033"));
        viewThree.setBackgroundColor(Color.parseColor("#FF0033"));
        viewFour.setBackgroundColor(Color.parseColor("#FF0033"));

    }

    /**
     * 转大写
     */
    public static class AllCapTransformationMethod extends ReplacementTransformationMethod {

        private char[] lower = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
                'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        private char[] upper = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
                'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        private boolean allUpper = false;

        public AllCapTransformationMethod(boolean needUpper) {
            this.allUpper = needUpper;
        }

        @Override
        protected char[] getOriginal() {
            if (allUpper) {
                return lower;
            } else {
                return upper;
            }
        }

        @Override
        protected char[] getReplacement() {
            if (allUpper) {
                return upper;
            } else {
                return lower;
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