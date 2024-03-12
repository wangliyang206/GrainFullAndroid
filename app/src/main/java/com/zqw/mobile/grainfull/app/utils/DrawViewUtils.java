package com.zqw.mobile.grainfull.app.utils;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;

import androidx.annotation.ColorRes;
import androidx.annotation.FloatRange;

import com.blankj.utilcode.util.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael by Administrator
 * @date 2020/12/22 14:43
 * @Description 自绘时用到的一些常用方法
 */
@SuppressWarnings("ALL")
public class DrawViewUtils {


    /**
     * 计算多边形面积的函数  (以原点为基准点,分割为多个三角形)
     * 定理：任意多边形的面积可由任意一点与多边形上依次两点连线构成的三角形矢量面积求和得出。矢量面积=三角形两边矢量的叉乘。
     * https://blog.csdn.net/xxdddail/article/details/48973269
     *
     * @param list 坐标点集合
     * @return 面积
     */
    public static float calculateArea(List<Point> list) {
        float iArea = 0;
        for (int i = 0; i < list.size(); i++) {
            iArea = iArea + (list.get(i).x * list.get((i + 1) % list.size()).y - list.get((i + 1) % list.size()).x * list.get(i).y);
        }
        return (float) Math.abs(0.5 * iArea);
    }

    /**
     * 根据三角函数计算扇形坐标点
     *
     * @param startAngle 起始角度
     * @param endAngle   结束角度
     * @param radiu      半径
     * @return
     */
    public static PointF getFanCenterPointF(float startAngle, float endAngle, float radiu) {
        float halfAngle = startAngle + endAngle / 2;
        //Math.toRadians 根据角度获取正切弧
        float radians = (float) Math.toRadians(halfAngle);
        //根据圆半径长度计算起点的x y坐标
        float x = (float) (radiu * Math.cos(radians));
        float y = (float) (radiu * Math.sin(radians));
        return new PointF(x, y);
    }

    /**
     * Math.atan2(x,y)  计算点相当于y轴的相对角度
     * Math.atan2(y,x)  计算点相当于x轴的相对角度
     *
     * @param x1 坐标原点
     * @param y1 坐标原点
     * @param x2 触摸点
     * @param y2 触摸点
     * @return 角度
     */
    public static double calculateAngle(float x1, float y1, float x2, float y2) {
        float x = x2 - x1;
        float y = y2 - y1;
        double k = Math.atan2(Math.abs(x), Math.abs(y));
        double angle = Math.abs(k * (180 / Math.PI));
        if (x < 0 && y < 0) {  //左上角
            angle = 360 - angle;
        } else if (x > 0 && y < 0) {  //右上角
            angle += 0;
        } else if (x > 0 && y > 0) {  // 右下角
            angle = 180 - angle;
        } else if (x < 0 && y > 0) {   //左下角
            angle += 180;
        }
        return angle;
    }

    /**
     * 计算坐标系中两点的长度
     */
    public static double calculateLength(float x1, float y1, float x2, float y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    /**
     * 实时改变指定的颜色透明度
     *
     * @param alpha 透明度
     * @param color 色值ID
     * @return 加入透明度后的色值
     */
    public static int getColorWithAlpha(@FloatRange(from = 0f, to = 1f) float alpha, @ColorRes int color) {
        return Math.min(255, Math.max(0, (int) (alpha * 255))) << 24 + 0x00ffffff & color;
    }

    /**
     * 计算指定区域文本位置
     *
     * @param s     文本
     * @param rect  要显示文本的区域
     * @param paint 画笔
     * @param align 对齐方式
     * @return 坐标数组，0位X  1位Y
     */
    public static float[] calculateTextCenterPosition(String s, Rect rect, Paint paint, Paint.Align align) {
        TextPaint textPaint = new TextPaint(paint);
        Paint.FontMetricsInt fm = textPaint.getFontMetricsInt();
        float[] textPosition = new float[2];
        if (Paint.Align.LEFT.name().equals(align.name())) {
            textPosition[0] = rect.left + paint.measureText(s) / 2;
        } else if (Paint.Align.CENTER.name().equals(align.name())) {
            textPosition[0] = rect.right / 2;
        } else if (Paint.Align.RIGHT.name().equals(align.name())) {
            textPosition[0] = rect.right - paint.measureText(s) / 2;
        }
        textPosition[1] = (rect.top + rect.bottom) / 2 - (fm.bottom + fm.top) / 2;
        return textPosition;
    }

    /**
     * 将3点钟方向的角度系转换成12点钟方向的角度系
     *
     * @param angle 待转角度
     * @return 转换后的角度
     */
    public static float convertAngleFrom3To12(float angle) {
        if (angle < 90) {
            return 270 + angle;
        } else {
            return angle - 90;
        }
    }

    /**
     * 计算圆弧上的某一点
     *
     * @param centerX 中心点X轴坐标
     * @param centerY 中心点Y轴坐标
     * @param radius  半径
     * @param angle   角度
     * @return 坐标点
     */
    public static Point calculatePoint(int centerX, int centerY, int radius, float angle) {
        Point point = new Point();
        point.x = (int) (centerX + radius * Math.cos((angle) * Math.PI / 180));
        point.y = (int) (centerY + radius * Math.sin((angle) * Math.PI / 180));
        return point;
    }

    /**
     * 获取文字高度
     *
     * @param paint 画笔
     * @param str   文字
     * @return 文字高度
     */
    public static int getTextHeight(Paint paint, String str) {
        return str.matches("^[\u4e00-\u9fa5]*") ? -ConvertUtils.dp2px(1.2f) /*纯中文偏移量1.2*/ : -ConvertUtils.dp2px(3);
    }

    /**
     * 获取文字宽高
     *
     * @param paint 画笔
     * @param str   文字
     * @return 一个包含文字尺寸的数组
     */
    public static int[] getTextWH(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        int w = rect.width();
        int h = rect.height();
        return new int[]{w, h};
    }

    /**
     * 获取文字宽高
     *
     * @param paint 画笔
     * @param str   文字
     * @return 一个包含文字尺寸的数组
     */
    public static float[] getTextWHF(Paint paint, String str) {
        float w = paint.measureText(str);
        float h = paint.descent() - paint.ascent();
        return new float[]{w, h};
    }

    /**
     * 平滑贝塞尔曲线轨迹
     *
     * @param lineSmoothness 平滑度 0-1 越小越平滑
     * @param pathMeasure    轨迹测量器
     * @param path           轨迹
     * @param entry          各顶点坐标集
     * @return 如果返为true, 说明测量成功，可以绘制path,反之，测量失败
     * 请使用{@link DrawViewUtils#calculateBezier3}
     */
    @Deprecated
    public static boolean calculateBesselSmoothPath(@FloatRange(from = 0f, to = 1f) float lineSmoothness, PathMeasure pathMeasure, Path path, List<Point> entry) {
        if (entry.size() < 2) {
            return false;
        }
        path.reset();
        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        float nextPointX;
        float nextPointY;

        final int lineSize = entry.size();
        for (int valueIndex = 0; valueIndex < lineSize; ++valueIndex) {
            if (Float.isNaN(currentPointX)) {
                Point point = entry.get(valueIndex);
                currentPointX = point.x;
                currentPointY = point.y;
            }
            if (Float.isNaN(previousPointX)) {
                //是否是第一个点
                if (valueIndex > 0) {
                    Point point = entry.get(valueIndex - 1);
                    previousPointX = point.x;
                    previousPointY = point.y;
                } else {
                    //是的话就用当前点表示上一个点
                    previousPointX = currentPointX;
                    previousPointY = currentPointY;
                }
            }

            if (Float.isNaN(prePreviousPointX)) {
                //是否是前两个点
                if (valueIndex > 1) {
                    Point point = entry.get(valueIndex - 2);
                    prePreviousPointX = point.x;
                    prePreviousPointY = point.y;
                } else {
                    //是的话就用当前点表示上上个点
                    prePreviousPointX = previousPointX;
                    prePreviousPointY = previousPointY;
                }
            }

            // 判断是不是最后一个点了
            if (valueIndex < lineSize - 1) {
                Point point = entry.get(valueIndex + 1);
                nextPointX = point.x;
                nextPointY = point.y;
            } else {
                //是的话就用当前点表示下一个点
                nextPointX = currentPointX;
                nextPointY = currentPointY;
            }

            if (valueIndex == 0) {
                // 将Path移动到开始点
                path.moveTo(currentPointX, currentPointY);
            } else {
                // 求出控制点坐标
                final float firstDiffX = (currentPointX - prePreviousPointX);
                final float firstDiffY = (currentPointY - prePreviousPointY);
                final float secondDiffX = (nextPointX - previousPointX);
                final float secondDiffY = (nextPointY - previousPointY);
                final float firstControlPointX = previousPointX + (lineSmoothness * firstDiffX);
                final float firstControlPointY = previousPointY + (lineSmoothness * firstDiffY);
                final float secondControlPointX = currentPointX - (lineSmoothness * secondDiffX);
                final float secondControlPointY = currentPointY - (lineSmoothness * secondDiffY);
                //画出曲线
                path.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                        currentPointX, currentPointY);
            }

            // 更新值,
            prePreviousPointX = previousPointX;
            prePreviousPointY = previousPointY;
            previousPointX = currentPointX;
            previousPointY = currentPointY;
            currentPointX = nextPointX;
            currentPointY = nextPointY;
        }
        pathMeasure.setPath(path, false);
        float distance = pathMeasure.getLength() * 1f;
        return pathMeasure.getSegment(0, distance, path, true);
    }

    /**
     * 计算三阶贝塞尔曲线
     *
     * @param mappedPoints 坐标点
     * @param sharpenRatio 平滑度
     * @param path         path
     * @return 计算后的点位。
     */
    public static List<PointF> calculateBezier3(List<PointF> mappedPoints, float sharpenRatio, Path path) {
        List<PointF> list = new ArrayList<>();
        if (mappedPoints.size() < 3) {
//            throw new IllegalArgumentException("The size of mappedPoints must not less than 3!");
        }

        PointF pMidOfLm = new PointF();
        PointF pMidOfMr = new PointF();

        PointF cache = null;

        for (int i = 0; i <= mappedPoints.size() - 3; i++) {
            PointF pL = mappedPoints.get(i);
            PointF pM = mappedPoints.get(i + 1);
            PointF pR = mappedPoints.get(i + 2);

            pMidOfLm.x = (pL.x + pM.x) / 2.0f;
            pMidOfLm.y = (pL.y + pM.y) / 2.0f;

            pMidOfMr.x = (pM.x + pR.x) / 2.0f;
            pMidOfMr.y = (pM.y + pR.y) / 2.0f;

            float lengthOfLm = (float) Math.hypot(pM.x - pL.x, pM.y - pL.y);
            float lengthOfMr = (float) Math.hypot(pR.x - pM.x, pR.y - pM.y);

            float ratio = (lengthOfLm / (lengthOfLm + lengthOfMr)) * sharpenRatio;
            float oneMinusRatio = (1 - ratio) * sharpenRatio;

            float dx = pMidOfLm.x - pMidOfMr.x;
            float dy = pMidOfLm.y - pMidOfMr.y;

            PointF cLeft = new PointF();
            cLeft.x = pM.x + dx * ratio;
            cLeft.y = pM.y + dy * ratio;

            PointF cRight = new PointF();
            cRight.x = pM.x + -dx * oneMinusRatio;
            cRight.y = pM.y + -dy * oneMinusRatio;

            if (i == 0) {
                PointF pMidOfLCLeft = new PointF((pL.x + cLeft.x) / 2.0f, (pL.y + cLeft.y) / 2.0f);
                PointF pMidOfCLeftM = new PointF((cLeft.x + pM.x) / 2.0f, (cLeft.y + pM.y) / 2.0f);

                float length1 = (float) Math.hypot(cLeft.x - pL.x, cLeft.y - pL.y);
                float length2 = (float) Math.hypot(pM.x - cLeft.x, pM.y - cLeft.y);

                ratio = (length1 / (length1 + length2)) * sharpenRatio;
                PointF first = new PointF();
                first.x = cLeft.x + (pMidOfLCLeft.x - pMidOfCLeftM.x) * ratio;
                first.y = cLeft.y + (pMidOfLCLeft.y - pMidOfCLeftM.y) * ratio;
                if (path != null) {
                    path.cubicTo(first.x, first.y, cLeft.x, cLeft.y, pM.x, pM.y);
                }
                list.add(first);
                list.add(cLeft);
                list.add(pM);
            } else {
                if (path != null) {
                    path.cubicTo(cache.x, cache.y, cLeft.x, cLeft.y, pM.x, pM.y);
                }
                list.add(cache);
                list.add(cLeft);
                list.add(pM);
            }

            cache = cRight;

            if (i == mappedPoints.size() - 3) {
                PointF pMidOfMCRight = new PointF((pM.x + cRight.x) / 2.0f, (pM.y + cRight.y) / 2.0f);
                PointF pMidOfCRightR = new PointF((pR.x + cRight.x) / 2.0f, (pR.y + cRight.y) / 2.0f);

                float length1 = (float) Math.hypot(cRight.x - pM.x, cRight.y - pM.y);
                float length2 = (float) Math.hypot(pR.x - cRight.x, pR.y - cRight.y);
                ratio = (length2 / (length1 + length2)) * sharpenRatio;

                PointF last = new PointF();
                last.x = cRight.x + (pMidOfCRightR.x - pMidOfMCRight.x) * ratio;
                last.y = cRight.y + (pMidOfCRightR.y - pMidOfMCRight.y) * ratio;
                if (path != null) {
                    path.cubicTo(cRight.x, cRight.y, last.x, last.y, pR.x, pR.y);
                }
                list.add(cRight);
                list.add(last);
                list.add(pR);
            }
        }
        return list;
    }

    /**
     * 通过角度获取象限位置
     *
     * @param angle           角度
     * @param defaultQuadrant 默认象限
     * @return 象限
     */
    public static int getQuadrantPositionByAngle(int angle, int defaultQuadrant) {
        if (90 > angle && 0 < angle) {
            return 4;
        } else if (180 > angle && 90 < angle) {
            return 3;
        } else if (270 > angle && 180 < angle) {
            return 2;
        } else if (360 > angle && 270 < angle) {
            return 1;
        }
        return defaultQuadrant;
    }

    /**
     * 获取两个矩形的相交面积
     *
     * @param rect1 矩形
     * @param rect2 矩形
     * @return 面积
     */
    public static double calculateOverlapArea(Rect rect1, Rect rect2) {
        if (rect1 == null || rect2 == null) {
            return -1;
        }
        double p1X = rect1.left, p1Y = rect1.top + rect1.height();
        double p2X = p1X + rect1.width(), p2Y = p1Y + rect1.height();
        double p3X = rect2.left, p3Y = rect2.top + rect2.height();
        double p4X = p3X + rect2.width(), p4Y = p3Y + rect2.height();

        if (p1X > p4X || p2X < p3X || p1Y > p4Y || p2Y < p3Y) {
            return 0;
        }
        double length = Math.min(p2X, p4X) - Math.max(p1X, p3X);
        double width = Math.min(p2Y, p4Y) - Math.max(p1Y, p3Y);
        return length * width;
    }

    /**
     * 获取两个矩形的相交面积
     *
     * @param rect1 矩形
     * @param rect2 矩形
     * @return 面积
     */
    public static double calculateOverlapAreaF(RectF rect1, RectF rect2) {
        if (rect1 == null || rect2 == null) {
            return -1;
        }
        double p1X = rect1.left, p1Y = rect1.top + rect1.height();
        double p2X = p1X + rect1.width(), p2Y = p1Y + rect1.height();
        double p3X = rect2.left, p3Y = rect2.top + rect2.height();
        double p4X = p3X + rect2.width(), p4Y = p3Y + rect2.height();

        if (p1X > p4X || p2X < p3X || p1Y > p4Y || p2Y < p3Y) {
            return 0;
        }
        double length = Math.min(p2X, p4X) - Math.max(p1X, p3X);
        double width = Math.min(p2Y, p4Y) - Math.max(p1Y, p3Y);
        return length * width;
    }


    /**
     * 任意三角形获取对边长度
     *
     * @param a      边长
     * @param b      边长
     * @param degree 角度
     * @return
     */
    public static Double getLengthOfSide(double a, double b, double degree) {
        return Math.sqrt(b * b + a * a - 2 * a * b * Math.cos(Math.toRadians(degree)));
    }

}
