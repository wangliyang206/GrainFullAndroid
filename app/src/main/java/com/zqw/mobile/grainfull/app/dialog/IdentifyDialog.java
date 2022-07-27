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

import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DeviceUtils;
import com.zqw.mobile.grainfull.R;

/**
 * 图片中文字识别结果弹框
 *
 * @author 赤槿
 */
public class IdentifyDialog extends AlertDialog implements View.OnClickListener {

    // 内容对象
    private TextView mContent;

    public IdentifyDialog(Context context) {
        super(context, R.style.weicomeDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_identify_dialog);
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
        // 内容
        mContent = findViewById(R.id.txvi_popidentify_content);

        findViewById(R.id.view_popidentify_layout).setOnClickListener(this);
        findViewById(R.id.btn_popidentify_close).setOnClickListener(this);
        findViewById(R.id.imvi_popidentify_copy).setOnClickListener(this);
    }

    /**
     * 设置结果内容
     */
    public void setData(String content) {
        if (mContent != null) {
            mContent.setText(content);
        }
    }

    /**
     * 追加内容
     */
    public void setAppend(String val) {
        if (mContent != null) {
            mContent.append(val);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_popidentify_layout:
                break;
            case R.id.btn_popidentify_close:
                // 关闭当前弹出框
                this.dismiss();
                break;
            case R.id.imvi_popidentify_copy:
                // 复制到剪贴板
                DeviceUtils.copyTextToBoard(getContext(), mContent.getText().toString());
                ArmsUtils.makeText(getContext(), "复制成功！");
                break;
        }
    }

}
