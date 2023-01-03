package com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.renderer;


import com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.ColorCircle;

import java.util.List;

/**
 * 彩色轮渲染器
 * @author vondear
 * @date 2018/6/11 11:36:40 整合修改
 */
public interface ColorWheelRenderer {
    float GAP_PERCENTAGE = 0.025f;

    void draw();

    ColorWheelRenderOption getRenderOption();

    void initWith(ColorWheelRenderOption colorWheelRenderOption);

    List<ColorCircle> getColorCircleList();
}
