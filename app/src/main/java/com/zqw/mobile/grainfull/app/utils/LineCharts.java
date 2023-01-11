package com.zqw.mobile.grainfull.app.utils;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Hello Charts 图表 - 折线图
 * 创建者: Yeliheng
 * 时间: 2018/12/31 - 22:44
 */
public class LineCharts {
    private List<PointValue> values;
    private List<Line> lines;
    private LineChartData lineChartData;
    private List<Line> linesList;
    private List<PointValue> pointValueList;
    private List<PointValue> points;
    private int position = 0;
    private Axis axisY, axisX;

    public void makeCharts(final LineChartView lineChartView, float uT) {
        //实时添加新的点
        PointValue value1 = new PointValue(position * 5, uT);
        value1.setLabel("00:00");
        pointValueList.add(value1);
        float x = value1.getX();
        float y = uT;
        //根据新的点的集合画出新的线
        Line line = new Line(pointValueList);
        line.setColor(Color.RED);
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(true);//曲线是否平滑
        line.setHasPoints(false);//设置折线是否含点
        linesList.clear();
        linesList.add(line);
        lineChartData = initData(linesList);
        lineChartView.setLineChartData(lineChartData);
        //根据点的横坐实时变换坐标的视图范围
        Viewport port;
        if (x > 500) {
            port = initViewPort(x - 500, x, y);
        } else {
            port = initViewPort(0, 500, y);
        }
        lineChartView.setCurrentViewport(port);//当前窗口

        Viewport maxPort = initMaxViewPort(x, y);//更新最大窗口设置
        lineChartView.setMaximumViewport(maxPort);//最大窗口
        position++;
    }

    /**
     * 初始化视图
     */
    public void initView(LineChartView lineChartView) {
        pointValueList = new ArrayList<>();
        linesList = new ArrayList<>();
        //初始化坐标轴
        axisY = new Axis();
        axisX = new Axis();
        lineChartData = initData(null);
        lineChartView.setLineChartData(lineChartData);
        Viewport port = initViewPort(0, 50, 150);
        lineChartView.setCurrentViewportWithAnimation(port);
        lineChartView.setInteractive(false);
        lineChartView.setScrollEnabled(true);
        lineChartView.setValueTouchEnabled(true);
        lineChartView.setFocusableInTouchMode(true);
        lineChartView.setViewportCalculationEnabled(false);
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.startDataAnimation();
        points = new ArrayList<>();
    }


    /**
     * 初始化图表数据
     */

    protected LineChartData initData(List<Line> lines) {
        LineChartData data = new LineChartData(lines);
        data.setAxisYLeft(axisY);
        data.setAxisXBottom(axisX);
        return data;
    }

    /**
     * 最大显示区域
     */
    protected Viewport initMaxViewPort(float right, float top) {
        Viewport port = new Viewport();
        if (max > top) {
            port.top = max + 150;
            // Log.d("IF",max + "");
        } else {
            max = top;
            port.top = max + 150;
            // Log.d("ELSE",max + "");
        }
        port.bottom = 0;
        port.left = 0;
        port.right = right + 500;
        return port;
    }

    /**
     * 当前显示区域
     */

    float max = 150;//最大高度,判断max与top的值，实时刷新图表y轴

    protected Viewport initViewPort(float left, float right, float top) {
        Viewport port = new Viewport();
        if (max > top) {
            port.top = max + 150;
            // Log.d("IF",max + "");
        } else {
            max = top;
            port.top = max + 150;
            //Log.d("ELSE",max + "");
        }
        port.bottom = 0;
        port.left = left;
        port.right = right;
        return port;
    }
}
