package com.zqw.mobile.grainfull.mvp.ui.widget.verifycodeview;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zqw.mobile.grainfull.R;


public class VerifyCodeView extends RelativeLayout {
    private EditText editText;
    private TextView[] textViews;

    private View[] lineViews;
    private static int MAX = 6;
    private String inputContent;
    private Context context;

    public VerifyCodeView(Context context) {
        this(context, null);
    }

    public VerifyCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerifyCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        View.inflate(context, R.layout.view_verify_code, this);

        textViews = new TextView[MAX];
        textViews[0] = (TextView) findViewById(R.id.tv_0);
        textViews[1] = (TextView) findViewById(R.id.tv_1);
        textViews[2] = (TextView) findViewById(R.id.tv_2);
        textViews[3] = (TextView) findViewById(R.id.tv_3);
        textViews[4] = (TextView) findViewById(R.id.tv_4);
        textViews[5] = (TextView) findViewById(R.id.tv_5);

        lineViews = new View[MAX];
        lineViews[0] = findViewById(R.id.view_line_0);
        lineViews[1] = findViewById(R.id.view_line_1);
        lineViews[2] = findViewById(R.id.view_line_2);
        lineViews[3] = findViewById(R.id.view_line_3);
        lineViews[4] = findViewById(R.id.view_line_4);
        lineViews[5] = findViewById(R.id.view_line_5);

        editText = (EditText) findViewById(R.id.edit_text_view);
        editText.setCursorVisible(false);//隐藏光标
        setEditTextListener();
    }

    private void setEditTextListener() {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputContent = editText.getText().toString();

                if (inputCompleteListener != null) {
                    if (inputContent.length() >= MAX) {
                        inputCompleteListener.inputComplete();
                    } else {
                        inputCompleteListener.invalidContent();
                    }
                }

                // 设置内容
                for (int i = 0; i < MAX; i++) {
                    if (i < inputContent.length()) {
                        textViews[i].setText(String.valueOf(inputContent.charAt(i)));
                    } else {
                        textViews[i].setText("");
                    }
                }

                // 设置焦点
                for (int i = 0; i < MAX; i++) {
                    if (i < inputContent.length()) {
                        // 计算并设置下一个item也变更
                        if (i + 1 < MAX) {
                            lineViews[i + 1].setBackground(ActivityCompat.getDrawable(context, R.mipmap.ic_login_select));
                        }
                    } else {
                        if (i != inputContent.length())
                            lineViews[i].setBackground(ActivityCompat.getDrawable(context, R.mipmap.ic_login_notselect));
                    }
                }

            }
        });
    }


    private InputCompleteListener inputCompleteListener;

    public void setInputCompleteListener(InputCompleteListener inputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener;
    }

    public interface InputCompleteListener {

        void inputComplete();

        void invalidContent();
    }

    public String getEditContent() {
        return inputContent;
    }

}