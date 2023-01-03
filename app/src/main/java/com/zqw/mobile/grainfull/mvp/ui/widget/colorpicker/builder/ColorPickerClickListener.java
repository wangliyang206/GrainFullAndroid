package com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.builder;

import android.content.DialogInterface;

/**
 * 颜色选择器单击侦听器
 * @author Vondear
 * @date 2015/4/17 新增
 *       2018/6/11 11:36:40 整合修改
 */
public interface ColorPickerClickListener {
    void onClick(DialogInterface d, int lastSelectedColor, Integer[] allColors);
}
