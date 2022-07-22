package com.zqw.mobile.grainfull.mvp.ui.activity;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.utils.ArmsUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.di.component.DaggerBaiduFaceActivationComponent;
import com.zqw.mobile.grainfull.mvp.contract.BaiduFaceActivationContract;
import com.zqw.mobile.grainfull.mvp.presenter.BaiduFaceActivationPresenter;
import com.zqw.mobile.grainfull.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:百度人脸识别激活
 * <p>
 * Created on 2022/07/22 18:29
 *
 * @author 赤槿
 * module name is BaiduFaceActivationActivity
 */
public class BaiduFaceActivationActivity extends BaseActivity<BaiduFaceActivationPresenter> implements BaiduFaceActivationContract.View {
    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.edit_faceactivation_one)
    EditText editOne;
    @BindView(R.id.view_faceactivation_one)
    View viewOne;

    @BindView(R.id.edit_faceactivation_two)
    EditText editTwo;
    @BindView(R.id.view_faceactivation_two)
    View viewTwo;

    @BindView(R.id.edit_faceactivation_three)
    EditText editThree;
    @BindView(R.id.view_faceactivation_three)
    View viewThree;

    @BindView(R.id.edit_faceactivation_four)
    EditText editFour;
    @BindView(R.id.view_faceactivation_four)
    View viewFour;

    @BindView(R.id.txvi_faceactivation_error)
    TextView txviError;
    @BindView(R.id.btn_faceactivation_activation)
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
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(com.jess.arms.R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBaiduFaceActivationComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_baidu_face_activation;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("百度人脸识别激活");

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
        editOne.setTransformationMethod(new SerialNumberActivity.AllCapTransformationMethod(true));
        editTwo.setTransformationMethod(new SerialNumberActivity.AllCapTransformationMethod(true));
        editThree.setTransformationMethod(new SerialNumberActivity.AllCapTransformationMethod(true));
        editFour.setTransformationMethod(new SerialNumberActivity.AllCapTransformationMethod(true));
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
            R.id.btn_faceactivation_activation,                                                     // 在线激活
            R.id.txvi_faceactivation_issue,                                                         // 遇到的问题
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_faceactivation_activation:                                                // 在线激活
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
            case R.id.txvi_faceactivation_issue:                                                    // 遇到的问题
                showMessage("暂未开放！");
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