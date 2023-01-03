package com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.builder;


import com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.ColorPickerView;
import com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.renderer.ColorWheelRenderer;
import com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.renderer.FlowerColorWheelRenderer;
import com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.renderer.SimpleColorWheelRenderer;

/**
 * 彩色轮渲染器生成器
 * @author vondear
 * @date 2018/6/11 11:36:40 整合修改
 */
public class ColorWheelRendererBuilder {
    public static ColorWheelRenderer getRenderer(ColorPickerView.WHEEL_TYPE wheelType) {
        switch (wheelType) {
            case CIRCLE:
                return new SimpleColorWheelRenderer();
            case FLOWER:
                return new FlowerColorWheelRenderer();
                default:
                    break;
        }
        throw new IllegalArgumentException("wrong WHEEL_TYPE");
    }
}