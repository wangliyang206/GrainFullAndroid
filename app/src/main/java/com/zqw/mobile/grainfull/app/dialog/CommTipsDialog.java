package com.zqw.mobile.grainfull.app.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.zqw.mobile.grainfull.R;

/**
 * 提示弹框
 *
 * @author 赤槿
 */
public class CommTipsDialog extends AlertDialog implements View.OnClickListener {
    public interface ItemClick {
        void itemClick(boolean isVal);
    }

    // 回调
    private CommTipsDialog.ItemClick popupClick;

    // 标题和内容
    private String title, content;
    // 是否隐藏取消按钮
    private boolean hideCancel;

    public CommTipsDialog(Context context, String title, String content, CommTipsDialog.ItemClick popupClick) {
        super(context, R.style.weicomeDialog);
        this.popupClick = popupClick;
        this.title = title;
        this.content = content;
    }

    public CommTipsDialog(Context context, String title, String content, boolean hideCancel, CommTipsDialog.ItemClick popupClick) {
        super(context, R.style.weicomeDialog);
        this.popupClick = popupClick;
        this.title = title;
        this.content = content;
        this.hideCancel = hideCancel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_commtips_dialog);
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
        View line = findViewById(R.id.view_popcommtips_line);
        // 取消
        TextView cancel = findViewById(R.id.txvi_popcommtips_no);
        // 确定
        TextView ok = findViewById(R.id.txvi_popcommtips_yes);
        // 标题
        TextView mTitle = findViewById(R.id.txvi_popcommtips_title);
        // 内容
        TextView mContent = findViewById(R.id.txvi_popcommtips_content);

        mTitle.setText(title);
        mContent.setText(content);

        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);

        if (hideCancel) {
            cancel.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        } else {
            cancel.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txvi_popcommtips_no:
                if (popupClick != null) {
                    popupClick.itemClick(false);
                }
                break;
            case R.id.txvi_popcommtips_yes:
                if (popupClick != null) {
                    popupClick.itemClick(true);
                }
                break;
        }
        // 关闭当前弹出框
        this.dismiss();
    }

}
