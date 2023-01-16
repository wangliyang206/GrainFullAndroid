package com.zqw.mobile.grainfull.app.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.zqw.mobile.grainfull.R;

/**
 * 不同意隐私政策弹框
 *
 * @author 赤槿
 */
public class NotPrivacyPolicyDialog extends AlertDialog implements View.OnClickListener {
    public interface ItemClick {
        void itemClick(boolean isVal);
    }

    private ItemClick popupClick;

    public NotPrivacyPolicyDialog(Context context, ItemClick popupClick) {
        super(context, R.style.weicomeDialog);
        this.popupClick = popupClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_notprivacypolicy_dialog);
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
