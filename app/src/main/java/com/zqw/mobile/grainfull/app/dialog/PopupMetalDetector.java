package com.zqw.mobile.grainfull.app.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zqw.mobile.grainfull.R;

/**
 * 包名： com.zqw.mobile.grainfull.app.dialog
 * 对象名： PopupMetalDetector
 * 描述：设置报警阈值
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2023/1/12 14:48
 */

public class PopupMetalDetector extends PopupWindow implements View.OnClickListener {
    // 输入对象
    private final EditText editInput;
    // 事件回调
    private final ItemClick itemClick;

    public PopupMetalDetector(Context context, ItemClick itemClick) {
        super(context);
        this.itemClick = itemClick;

        // 初始化下拉列表
        View view = LayoutInflater.from(context).inflate(R.layout.pop_metal_detector_layout, null);
        setContentView(view);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);

        // 设置背景色
        setBackgroundDrawable(new ColorDrawable());

        // 让popwin获取焦点
        setFocusable(true);
        // 点击弹出窗口区域之外的任意区域，则该窗口关闭
        setOutsideTouchable(true);

        editInput = view.findViewById(R.id.edit_popmetaldetector_input);

        view.findViewById(R.id.lila_popmetaldetector_top).setOnClickListener(this);
        view.findViewById(R.id.btn_popmetaldetector_confim).setOnClickListener(this);

    }

    /**
     * 对外提供，设置阈值
     */
    public void setContent(String content) {
        if (editInput != null) {
            editInput.setText(content);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_popmetaldetector_confim) {
            if (itemClick != null) {
                itemClick.itemClick(true, editInput.getText().toString());
            }
        } else {
            if (itemClick != null) {
                itemClick.itemClick(false, "");
            }
        }
        this.dismiss();
    }

    public interface ItemClick {
        void itemClick(boolean isSucc, String val);
    }
}
