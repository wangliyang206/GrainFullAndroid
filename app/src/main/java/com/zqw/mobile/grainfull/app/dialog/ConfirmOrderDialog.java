package com.zqw.mobile.grainfull.app.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jess.arms.widget.etoast2.Toast;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;

/**
 * 调拨单(确认)操作
 * 此界面有两个功能使用：调拨单确认订单、按重量调拨、出库单过磅重量
 */
public class ConfirmOrderDialog extends AlertDialog {
    /**
     * 输入框
     */
    private EditText mWeight;

    public interface ConfirmOrder {
        void onOver(String count);
    }

    /**
     * 回调
     */
    private ConfirmOrder confirmOrder;

    /**
     * 单位
     */
    private String unit = "(KG)";

    /**
     * 默认重量
     */
    private String defaultWeight = "";

    public ConfirmOrderDialog(Context context, String defaultWeight, String unit, ConfirmOrder confirmOrder) {
        super(context, R.style.quick_option_dialog);
        this.confirmOrder = confirmOrder;
        this.unit = unit;
        this.defaultWeight = defaultWeight;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_order_layout);
        getWindow().setGravity(Gravity.CENTER);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(p);

        //解决dilaog中EditText无法弹出输入的问题
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        initView();
    }

    private void initView() {
        TextView mUnit = findViewById(R.id.txvi_dialogconfirmorder_unit);
        mWeight = findViewById(R.id.txvi_dialogconfirmorder_weight);
        mUnit.setText(unit);

        // 设置默认值
        if (!TextUtils.isEmpty(defaultWeight)) {
            mWeight.setText(defaultWeight);
        }

        findViewById(R.id.txvi_popsignindialog_yes).setOnClickListener(v -> {
            String weightStr = mWeight.getText().toString().trim();
            if (TextUtils.isEmpty(weightStr)) {
                Toast.makeText(getContext(), "请输入重量！").show();
                return;
            }

            if (!CommonUtils.customRegular(weightStr, Constant.regular)) {
                Toast.makeText(getContext(), "请输入有效的重量！").show();
                return;
            }

            if (confirmOrder != null) {
                confirmOrder.onOver(weightStr);
            }

            ConfirmOrderDialog.this.dismiss();
        });

        findViewById(R.id.txvi_popsignindialog_no).setOnClickListener(v -> ConfirmOrderDialog.this.dismiss());
    }
}
