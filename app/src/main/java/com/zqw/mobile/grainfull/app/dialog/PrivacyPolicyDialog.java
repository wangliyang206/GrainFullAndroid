package com.zqw.mobile.grainfull.app.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.MyClickableSpan;

/**
 * 隐私政策弹框
 *
 * @author 赤槿
 */
public class PrivacyPolicyDialog extends AlertDialog implements View.OnClickListener {
    public interface ItemClick {
        void itemClick(boolean isVal);
    }

    private ItemClick popupClick;

    public PrivacyPolicyDialog(Context context, ItemClick popupClick) {
        super(context, R.style.weicomeDialog);
        this.popupClick = popupClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_privacypolicy_dialog);
        getWindow().setGravity(Gravity.CENTER);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(p);

        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        // 取消
        findViewById(R.id.txvi_popprivacypolicydialog_no).setOnClickListener(this);
        // 确定
        findViewById(R.id.txvi_popprivacypolicydialog_yes).setOnClickListener(this);
        TextView txviContent = findViewById(R.id.txvi_popprivacypolicydialog_content);
        SpannableString agreement = new SpannableString(getContext().getString(R.string.privacy_policy_tips));
        agreement.setSpan(new MyClickableSpan("《服务协议》"), 22, 28, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        agreement.setSpan(new MyClickableSpan("《隐私政策》"), 29, 34, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        txviContent.setText(agreement);
        txviContent.setMovementMethod(LinkMovementMethod.getInstance());

        TextView txviTips = findViewById(R.id.txvi_popprivacypolicydialog_tips);
        SpannableString agreementTips = new SpannableString(getContext().getString(R.string.privacy_policy_tips_x));
        agreementTips.setSpan(new MyClickableSpan("《服务协议》"), 14, 20, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        agreementTips.setSpan(new MyClickableSpan("《隐私政策》"), 21, 27, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        txviTips.setText(agreementTips);
        txviTips.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txvi_popprivacypolicydialog_no:
                if (popupClick != null) {
                    popupClick.itemClick(false);
                }
                break;
            case R.id.txvi_popprivacypolicydialog_yes:
                if (popupClick != null) {
                    popupClick.itemClick(true);
                }
                break;
        }
        // 关闭当前弹出框
        this.dismiss();
    }

}
