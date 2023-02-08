package com.zqw.mobile.grainfull.app.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.EventBusTags;
import com.zqw.mobile.grainfull.mvp.model.entity.MainEvent;

import org.simple.eventbus.EventBus;

/**
 * 包名： com.zqw.mobile.grainfull.app.dialog
 * 对象名： PopupOneLineToEnd
 * 描述：一笔画完 -
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2023/1/12 14:48
 */

public class PopupOneLineToEnd extends PopupWindow implements View.OnClickListener {
    TextView txviRows;
    TextView txviColumns;
    TextView txviDifficulties;
    TextView txviPassed;

    public PopupOneLineToEnd(Context context, int rows, int columns, int difficulties, int passed) {
        super(context);

        // 初始化下拉列表
        View view = LayoutInflater.from(context).inflate(R.layout.pop_onelinetoend_layout, null);
        setContentView(view);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);

        // 设置背景色
        setBackgroundDrawable(new ColorDrawable());

        // 让popwin获取焦点
        setFocusable(true);
        // 点击弹出窗口区域之外的任意区域，则该窗口关闭
        setOutsideTouchable(true);

        txviRows = view.findViewById(R.id.txvi_poponelinetoend_rows);
        txviColumns = view.findViewById(R.id.txvi_poponelinetoend_columns);
        txviDifficulties = view.findViewById(R.id.txvi_poponelinetoend_difficulties);
        txviPassed = view.findViewById(R.id.txvi_poponelinetoend_passed);

        view.findViewById(R.id.lila_poponelinetoend_top).setOnClickListener(this);
        view.findViewById(R.id.btn_poponelinetoend_clear).setOnClickListener(this);

        txviRows.setText(String.valueOf(rows));
        txviColumns.setText(String.valueOf(columns));
        txviDifficulties.setText(String.valueOf(difficulties));
        txviPassed.setText(String.valueOf(passed));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_poponelinetoend_clear) {
            // 清空
            txviPassed.setText("0");

            // 发送清空通知
            EventBus.getDefault().post(new MainEvent(EventBusTags.ONE_LINE_TO_END_CLEAR), EventBusTags.HOME_TAG);
        } else {
            // 关闭
            this.dismiss();
        }
    }
}
