package com.zqw.mobile.grainfull.mvp.ui.widget.trendchart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.zqw.mobile.grainfull.app.utils.DrawViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael by Administrator
 * @date 2021/2/1 15:02
 * @Description 一个简单的半圆环状饼图
 */
@SuppressWarnings("ALL")
public class SimpleHalfPieChart extends View {
    /**
     * 动画持续时间
     */
    private static final int ANIMATION_DURATION = 500;
    /**
     * 中心点
     */
    private Point centerPoint = new Point();
    /**
     * 间隔角度
     */
    private int intervalAngle = 1;
    /**
     * 宽高比例
     */
    private float whPercent = 0.35f;
    /**
     * 内圆百分比
     */
    private float innerRadiusPercent = 0.35f;
    /**
     * 外圆百分比
     */
    private float outerRadiusPercent = 0.5f;
    /**
     * 背景色
     */
    private int backgroundColor = Color.parseColor("#FFFFFF");
    /**
     * 中心文字
     */
    private String centerText = "院校倾向";
    /**
     * 中心文字样式
     */
    private int centerTextStyle = Typeface.BOLD;
    /**
     * 中心文字颜色
     */
    private int centerTextColor = Color.parseColor("#212121");
    /**
     * 中心文字大小
     */
    private float centerTextSize = ConvertUtils.sp2px(16f);
    /**
     * 中心文字距底部的距离
     */
    private int distanceBetweenCenterTextAndBottom = ConvertUtils.dp2px(20);
    /**
     * 文字与指示线之间的距离
     */
    private int distanceBetweenTextAndLine = ConvertUtils.dp2px(5);
    /**
     * 上下文字之间的距离
     */
    private int distanceBetweenText = ConvertUtils.dp2px(6);
    /**
     * 延长线长度
     */
    private int lengthOfExtensionLine = ConvertUtils.dp2px(15);
    /**
     * 折线长度
     */
    private int lengthOfBrokenLine = ConvertUtils.dp2px(10);
    /**
     * 线宽
     */
    private int lineWidth = ConvertUtils.dp2px(1.5f);
    /**
     * 宽度
     */
    private int width;
    /**
     * 高度
     */
    private int height;
    /**
     * 数据集
     */
    private List<SimpleHalfPieChartBean> data = new ArrayList<>();
    /**
     * 储存已绘制的文字位置
     */
    private List<Rect> rectList = new ArrayList<>();
    /**
     * 画笔
     */
    private Paint textPaint = new Paint();
    private Paint drawPaint = new Paint();

    {
        drawPaint.setAntiAlias(true);
        textPaint.setAntiAlias(true);
    }

    /**
     * 实时渲染下标
     */
    private int realDrawPosition;
    /**
     * 动画
     */
    private ValueAnimator valueAnimator;
    /**
     * 动画监听器
     */
    private ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (data.size() > 0) {
                realDrawPosition = (int) animation.getAnimatedValue();
                invalidate();
            }
        }
    };

    public SimpleHalfPieChart(Context context) {
        this(context, null);
    }

    public SimpleHalfPieChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleHalfPieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (height == 0) {
            height = (int) (width * whPercent);
            setMeasuredDimension(width, height);
        }
        centerPoint.set(width / 2, height);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPaint.setColor(backgroundColor);
        //背景
        canvas.drawRect(0, 0, width, height, drawPaint);
        if (data.size() == 0) {
            return;
        }
        rectList.clear();
        //外圆衬底
        canvas.drawCircle(centerPoint.x, centerPoint.y, width / 2 * outerRadiusPercent, drawPaint);
        //各百分比圆环
        for (int i = 0; i < data.size(); i++) {
            if (realDrawPosition >= i) {
                drawPaint.setColor(data.get(i).percentColor);
                canvas.drawArc(centerPoint.x - width / 2 * outerRadiusPercent,
                        centerPoint.y - width / 2 * outerRadiusPercent,
                        centerPoint.x + width / 2 * outerRadiusPercent,
                        centerPoint.y + width / 2 * outerRadiusPercent,
                        data.get(i).startAngle,
                        data.get(i).sweepAngle,
                        true, drawPaint);
                drawPaint.setStrokeWidth(lineWidth);
                //计算延长线起始坐标
                Point start = DrawViewUtils.calculatePoint(centerPoint.x, centerPoint.y, (int) (width / 2 * outerRadiusPercent), data.get(i).startAngle + data.get(i).sweepAngle / 2);
                Point end = DrawViewUtils.calculatePoint(centerPoint.x, centerPoint.y, (int) (width / 2 * outerRadiusPercent) + lengthOfExtensionLine, data.get(i).startAngle + data.get(i).sweepAngle / 2);
                //折线结束坐标
                int lineEndX;
                int lineEndY;
                switch (DrawViewUtils.getQuadrantPositionByAngle(data.get(i).startAngle + data.get(i).sweepAngle / 2, 0)) {
                    case 1:
                        //计算折线结束坐标
                        lineEndX = end.x + lengthOfBrokenLine;
                        lineEndY = end.y;

                        textPaint.setTextAlign(Paint.Align.LEFT);

                        //计算上部文字位置
                        Rect rectTextRight = new Rect();
                        textPaint.setTextSize(data.get(i).textSize);
                        textPaint.setColor(data.get(i).textColor);
                        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, data.get(i).textStyle));
                        int whTextRight[] = DrawViewUtils.getTextWH(textPaint, data.get(i).text);
                        rectTextRight.left = lineEndX + distanceBetweenTextAndLine;
                        rectTextRight.right = rectTextRight.left + whTextRight[0];
                        rectTextRight.top = lineEndY - whTextRight[1] - distanceBetweenText / 2;
                        rectTextRight.bottom = rectTextRight.top + whTextRight[1];

                        //计算下部文字位置
                        Rect rectRatioRight = new Rect();
                        textPaint.setTextSize(data.get(i).ratioSize);
                        textPaint.setColor(data.get(i).ratioColor);
                        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, data.get(i).ratioStyle));
                        int whRatioRight[] = DrawViewUtils.getTextWH(textPaint, data.get(i).ratio);
                        rectRatioRight.left = lineEndX + distanceBetweenTextAndLine;
                        rectRatioRight.right = rectRatioRight.left + whRatioRight[0];
                        rectRatioRight.top = lineEndY + distanceBetweenText / 2;
                        rectRatioRight.bottom = rectRatioRight.top + whRatioRight[1];

                        if (!isInclude(rectTextRight) && !isInclude(rectRatioRight)) {
                            //延长线
                            canvas.drawLine(start.x, start.y, end.x, end.y, drawPaint);
                            //折线
                            canvas.drawLine(end.x, end.y, lineEndX, end.y, drawPaint);
                            //上部文字
                            canvas.drawText(data.get(i).text, rectTextRight.left, rectTextRight.top + whTextRight[1], textPaint);
                            //下部文字
                            canvas.drawText(data.get(i).ratio, rectRatioRight.left, rectRatioRight.top + whRatioRight[1], textPaint);
                        }

                        break;
                    case 2:
                        //计算折线结束坐标
                        lineEndX = end.x - lengthOfBrokenLine;
                        lineEndY = end.y;

                        textPaint.setTextAlign(Paint.Align.LEFT);

                        //计算上部文字位置
                        Rect rectTextLeft = new Rect();
                        textPaint.setTextSize(data.get(i).textSize);
                        textPaint.setColor(data.get(i).textColor);
                        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, data.get(i).textStyle));
                        int whTextLeft[] = DrawViewUtils.getTextWH(textPaint, data.get(i).text);
                        rectTextLeft.right = lineEndX - distanceBetweenTextAndLine;
                        rectTextLeft.left = rectTextLeft.right - whTextLeft[0];
                        rectTextLeft.top = lineEndY - whTextLeft[1] - distanceBetweenText / 2;
                        rectTextLeft.bottom = rectTextLeft.top + whTextLeft[1];

                        //计算下部文字位置
                        Rect rectRatioLeft = new Rect();
                        textPaint.setTextSize(data.get(i).ratioSize);
                        textPaint.setColor(data.get(i).ratioColor);
                        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, data.get(i).ratioStyle));
                        int whRatioLeft[] = DrawViewUtils.getTextWH(textPaint, data.get(i).ratio);
                        rectRatioLeft.right = lineEndX - distanceBetweenTextAndLine;
                        rectRatioLeft.left = rectRatioLeft.right - whRatioLeft[0];
                        rectRatioLeft.top = lineEndY + distanceBetweenText / 2;
                        rectRatioLeft.bottom = rectRatioLeft.top + whRatioLeft[1];

                        if (!isInclude(rectTextLeft) && !isInclude(rectRatioLeft)) {
                            //延长线
                            canvas.drawLine(start.x, start.y, end.x, end.y, drawPaint);
                            //折线
                            canvas.drawLine(end.x, end.y, lineEndX, lineEndY, drawPaint);
                            //上部文字
                            canvas.drawText(data.get(i).text, rectTextLeft.left, rectTextLeft.top + whTextLeft[1], textPaint);
                            //下部文字
                            canvas.drawText(data.get(i).ratio, rectRatioLeft.left, rectRatioLeft.top + whRatioLeft[1], textPaint);
                        }
                        break;
                    default:
                }
            }
        }
        drawPaint.setColor(backgroundColor);
        //内圆衬底
        canvas.drawCircle(centerPoint.x, centerPoint.y, width / 2 * innerRadiusPercent, drawPaint);
        //中心文字
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(centerTextSize);
        textPaint.setColor(centerTextColor);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, centerTextStyle));
        canvas.drawText(centerText, width / 2, height - distanceBetweenCenterTextAndBottom, textPaint);
    }

    /**
     * 即将绘制文字的碰撞检测
     */
    private boolean isInclude(Rect rect) {
        for (int i = 0; i < rectList.size(); i++) {
            if (DrawViewUtils.calculateOverlapArea(rectList.get(i), rect) > 0) {
                return true;
            }
        }
        rectList.add(rect);
        return false;
    }

    /**
     * 设置数据
     */
    public void setData(String text, List<SimpleHalfPieChartBean> data, boolean withAnimation) {
        centerText = text;
        this.data = data;

        int totalAngle = 180 - intervalAngle * data.size() + intervalAngle;
        int lastEnd = 180;
        for (int i = 0; i < data.size(); i++) {
            data.get(i).startAngle = lastEnd;
            data.get(i).sweepAngle = (int) (totalAngle * data.get(i).percent) + (i < data.size() - 1 ? intervalAngle : 0);
            lastEnd = lastEnd + intervalAngle + data.get(i).sweepAngle;
        }
        if (withAnimation) {
            startAnimation();
        } else {
            realDrawPosition = data.size() - 1;
            invalidate();
        }
    }

    /**
     * 开始动画
     */
    public void startAnimation() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        valueAnimator = ValueAnimator.ofInt(0, data.size() - 1);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setDuration(ANIMATION_DURATION);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(listener);
        valueAnimator.start();
    }

    public static class SimpleHalfPieChartBean {
        private String text;
        private float textSize = ConvertUtils.dp2px(12);
        private int textColor = Color.parseColor("#4A4A4A");
        private int textStyle = Typeface.NORMAL;
        private String ratio;
        private float ratioSize = ConvertUtils.dp2px(12);
        private int ratioColor = Color.parseColor("#4A4A4A");
        private int ratioStyle = Typeface.NORMAL;

        private float percent;
        private int percentColor;

        private int startAngle;
        private int sweepAngle;

        public SimpleHalfPieChartBean(String text, String ratio, float percent, int percentColor) {
            this.text = text;
            this.ratio = ratio;
            this.percent = percent;
            this.percentColor = percentColor;
        }
    }

}
