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
 * 噪音等级弹窗
 *
 * @author 赤槿
 */
public class NoiseLevelDialog extends AlertDialog implements View.OnClickListener {

    public NoiseLevelDialog(Context context) {
        super(context, R.style.weicomeDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_noiselevel_dialog);
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
        findViewById(R.id.view_popnoiselevel_layout).setOnClickListener(this);
        // 确定
        findViewById(R.id.txvi_popnoiselevel_yes).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txvi_popnoiselevel_yes) {
            // 关闭当前弹出框
            this.dismiss();
        }
    }

}
