package com.zqw.mobile.grainfull.app.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zqw.mobile.grainfull.R;

/**
 * 包名： com.zqw.mobile.grainfull.app.dialog
 * 对象名： PopupFastGptIntro
 * 描述：FastGPT应用介绍
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2023/12/25 14:48
 */

public class PopupFastGptIntro extends PopupWindow implements View.OnClickListener {

    public PopupFastGptIntro(Context context) {
        super(context);

        // 初始化下拉列表
        View view = LayoutInflater.from(context).inflate(R.layout.pop_fastgptintro_layout, null);
        setContentView(view);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);

        // 设置背景色
        setBackgroundDrawable(new ColorDrawable());

        // 让popwin获取焦点
        setFocusable(true);
        // 点击弹出窗口区域之外的任意区域，则该窗口关闭
        setOutsideTouchable(true);

        view.findViewById(R.id.lila_popfastgptintro_top).setOnClickListener(this);
        view.findViewById(R.id.btn_popfastgptintro_ok).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.lila_popfastgptintro_top) {
            // 点击布局不关闭
        } else {
            // 关闭
            this.dismiss();
        }
    }

}
