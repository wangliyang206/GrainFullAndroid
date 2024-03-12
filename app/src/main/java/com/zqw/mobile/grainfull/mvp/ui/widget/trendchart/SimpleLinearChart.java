package com.zqw.mobile.grainfull.mvp.ui.widget.trendchart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Shader;
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
 * @date 2021/1/29 9:47
 * @Description 一个简单的线性图表
 */
@SuppressWarnings("ALL")
public class SimpleLinearChart extends View {
    /**
     * 动画持续时间
     */
    private static final int ANIMATION_DURATION = 500;
    /**
     * View宽度
     */
    private int width;
    /**
     * View高度
     */
    private int height;
    /**
     * 宽高比例
     */
    private float whPercent = 0.5f;
    /**
     * 坐标系原点
     */
    private Point originPoint = new Point();
    /**
     * 每个坐标点之间的距离
     */
    private int eachWidth;
    /**
     * 顶部预留的空白区域
     */
    private int topAreaOffset;
    /**
     * 坐标点与坐标点注释之间的间距
     */
    private int betweenDataTextAndPoint = ConvertUtils.dp2px(10);
    /**
     * X轴与文字之间的间距
     */
    private int betweenRemartTextAndXAxis = ConvertUtils.dp2px(15);
    /**
     * Y轴最大值
     */
    private int yMax;
    /**
     * Y轴最小值
     */
    private int yMin;
    /**
     * X轴线宽
     */
    private int xAxisWidth = 1;
    /**
     * 坐标线宽
     */
    private int pointLineWidth = 3;
    /**
     * Y轴每百分之一所占的距离
     */
    private float onePercentDistanceForYAxis;
    /**
     * 坐标系中Y轴最低的那个点到X轴坐标之间的留空距离所占有效坐标区域的百分比，避免坐标点画到X轴上
     */
    private float emptyAxisAreaPercent = 0.5f;
    /**
     * X轴标签区域高度
     */
    private int xAxisLabelHeight = ConvertUtils.dp2px(25);
    /**
     * 有效坐标区域（高度-X轴标签区域高度-X轴-顶部预留的空白区域）
     */
    private int effectiveAxisArea;
    /**
     * 是否绘制X轴
     */
    private boolean isDrawXAxis = true;
    /**
     * 第一个点是否贴边从Y轴开始绘制
     */
    private boolean isClingYAxisForFirst = false;
    /**
     * 坐标点半径
     */
    private int pointRadius = ConvertUtils.dp2px(3.5f);
    /**
     * 背景色
     */
    private int backgroundColor = Color.parseColor("#FFFFFF");
    /**
     * 坐标线色值
     */
    private int pointColor = Color.parseColor("#FF2678E3");
    /**
     * 坐标线色值
     */
    private int lineColor = Color.parseColor("#FF2678E3");
    /**
     * X轴色值
     */
    private int xAxisColor = Color.parseColor("#666666");
    /**
     * 遮罩开始色值
     */
    private int maskStartColor = Color.parseColor("#882678E3");
    /**
     * 遮罩中间色值
     */
    private int maskMiddleColor = Color.parseColor("#552678E3");
    /**
     * 遮罩后部色值
     */
    private int maskEndColor = Color.parseColor("#002678E3");
    /**
     * 遮罩开始色值起始区域
     */
    private float maskStartArea = 0.0f;
    /**
     * 遮罩中间色值起始区域
     */
    private float maskMiddleArea = 0.1f;
    /**
     * 遮罩后部色值起始区域
     */
    private float maskEndArea = 1.0f;
    /**
     * 线宽
     */
    private float lineWidth = ConvertUtils.dp2px(1.3f);
    /**
     * 画笔
     */
    Paint xAxisPaint = new Paint();
    Paint pointPaint = new Paint();
    Paint linePaint = new Paint();
    Paint maskPaint = new Paint();
    Paint textPaint = new Paint();

    {
        xAxisPaint.setAntiAlias(true);
        pointPaint.setAntiAlias(true);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        maskPaint.setAntiAlias(true);
        textPaint.setAntiAlias(true);
    }

    /**
     * 路径
     */
    private Path path = new Path();
    /**
     * 实时渲染坐标点下标（从左向右）
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
    /**
     * 数据集
     */
    private List<SimpleLinearChartBean> data = new ArrayList<>();
    /**
     * 计算后的贝塞尔坐标集
     */
    List<PointF> bezierPoints = new ArrayList<>();

    public SimpleLinearChart(Context context) {
        this(context, null);
    }

    public SimpleLinearChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleLinearChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        if (height == 0) {
            height = (int) (width * whPercent);
            setMeasuredDimension(width, height);
        }
        maskPaint.setShader(new LinearGradient(0, 0, 0, height, new int[]{maskStartColor, maskMiddleColor, maskEndColor}, new float[]{maskStartArea, maskMiddleArea, maskEndArea}, Shader.TileMode.CLAMP));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //背景
        pointPaint.setColor(backgroundColor);
        canvas.drawRect(0, 0, width, height, pointPaint);
        //X轴
        if (isDrawXAxis) {
            xAxisPaint.setColor(xAxisColor);
            xAxisPaint.setStrokeWidth(xAxisWidth);
            canvas.drawLine(0, height - xAxisLabelHeight, width, height - xAxisLabelHeight, xAxisPaint);
        }
        //坐标点+坐标线+遮罩
        for (int i = 0; i < data.size(); i++) {
            if (realDrawPosition >= i) {
                //坐标点
                pointPaint.setColor(pointColor);
                canvas.drawCircle(data.get(i).x, data.get(i).y, pointRadius, pointPaint);
            }
        }
        bezierPoints.clear();
        for (int i = 0; i < data.size(); i++) {
            if (realDrawPosition >= i) {
                bezierPoints.add(new PointF(data.get(i).x, data.get(i).y));
            }
        }
        if (bezierPoints.size() > 0) {
            //曲线轨迹
            path.reset();
            path.moveTo(bezierPoints.get(0).x, bezierPoints.get(0).y);
            DrawViewUtils.calculateBezier3(bezierPoints, 1f, path);
            linePaint.setColor(lineColor);
            linePaint.setStrokeWidth(lineWidth);
            canvas.drawPath(path, linePaint);
            //三阶贝塞尔需3个点才是完美的轨迹,两个点绘制出的遮罩不美观，故此处限制大于2
            if (bezierPoints.size() > 2) {
                //遮罩
                path.reset();
                path.moveTo(originPoint.x, originPoint.y);
                path.lineTo(bezierPoints.get(0).x, bezierPoints.get(0).y);
                DrawViewUtils.calculateBezier3(bezierPoints, 1f, path);
                path.lineTo(bezierPoints.get(bezierPoints.size() - 1).x, originPoint.y);
                path.lineTo(originPoint.x, originPoint.y);
                path.close();
                canvas.drawPath(path, maskPaint);
            }
        }


        //数据注释文字
        for (int i = 0; i < data.size(); i++) {
            if (realDrawPosition >= i) {
                textPaint.setColor(data.get(i).dataTextColor);
                textPaint.setTextSize(data.get(i).dataTextSize);
                int[] wh = DrawViewUtils.getTextWH(textPaint, data.get(i).dataText);
                if (i == 0) {
                    textPaint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText(data.get(i).dataText, isClingYAxisForFirst ? 0 : data.get(i).x - wh[0] / 2, data.get(i).y - betweenDataTextAndPoint, textPaint);
                } else if (i == realDrawPosition) {
                    textPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(data.get(i).dataText, isClingYAxisForFirst ? data.get(i).x : data.get(i).x + wh[0] / 2, data.get(i).y - betweenDataTextAndPoint, textPaint);
                } else {
                    textPaint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText(data.get(i).dataText, data.get(i).x, data.get(i).y - betweenDataTextAndPoint, textPaint);
                }
            }
        }
        //X轴区域文字
        for (int i = 0; i < data.size(); i++) {
            textPaint.setColor(data.get(i).remarkTextColor);
            textPaint.setTextSize(data.get(i).remarkTextSize);
            int[] wh = DrawViewUtils.getTextWH(textPaint, data.get(i).remarkText);
            if (i == 0) {
                textPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(data.get(i).remarkText, isClingYAxisForFirst ? 0 : data.get(i).x - wh[0] / 2, originPoint.y + xAxisWidth + betweenRemartTextAndXAxis, textPaint);
            } else if (i == data.size() - 1) {
                textPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(data.get(i).remarkText, isClingYAxisForFirst ? data.get(i).x : data.get(i).x + wh[0] / 2, originPoint.y + xAxisWidth + betweenRemartTextAndXAxis, textPaint);
            } else {
                textPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(data.get(i).remarkText, data.get(i).x, originPoint.y + xAxisWidth + betweenRemartTextAndXAxis, textPaint);
            }
        }

    }

    /**
     * 设置数据
     */
    public void setData(List<SimpleLinearChartBean> list, int yMin, int yMax, boolean withAnimation) {
        if (list.size() == 0) {
            return;
        }
        this.yMin = yMin;
        this.yMax = yMax;
        this.data = list;
        eachWidth = width / data.size();
        if (isClingYAxisForFirst) {
            //当贴边绘制时再次加上【每格宽度+当前每格宽度/（格数-1）】
            eachWidth = eachWidth + eachWidth / (data.size() - 1);
        }
        for (int i = 0; i < data.size(); i++) {
            textPaint.setTextSize(data.get(i).dataTextSize);
            //取数据集中最大文字高度+与坐标点之间的距离
            topAreaOffset = Math.max(topAreaOffset, DrawViewUtils.getTextWH(textPaint, data.get(i).dataText)[1] + betweenDataTextAndPoint);
        }
        effectiveAxisArea = height - xAxisLabelHeight - (isDrawXAxis ? 1 : 0) - topAreaOffset - xAxisWidth;
        originPoint.set(isClingYAxisForFirst ? 0 : eachWidth / 2, topAreaOffset + effectiveAxisArea + xAxisWidth);
        //Y轴差值
        int discrepancy = yMax - yMin;
        //底部留空区域
        float bottomEmptyArea = emptyAxisAreaPercent * effectiveAxisArea;
        effectiveAxisArea = height - xAxisLabelHeight - (isDrawXAxis ? 1 : 0) - topAreaOffset - xAxisWidth;
        originPoint.set(isClingYAxisForFirst ? 0 : eachWidth / 2, topAreaOffset + effectiveAxisArea + xAxisWidth);

        //每百分之1所占的距离
        onePercentDistanceForYAxis = (effectiveAxisArea - bottomEmptyArea) / discrepancy;
        for (int i = 0; i < data.size(); i++) {
            //计算每个点的Y值原点-(具体数值-最小Y值x每一格所占的长度)-底部留空距离
            data.get(i).y = (int) (originPoint.y - (int) ((data.get(i).data - yMin) * onePercentDistanceForYAxis) - bottomEmptyArea);
            data.get(i).x = (isClingYAxisForFirst ? 0 : eachWidth / 2) + i * eachWidth;
        }
        if (withAnimation) {
            startAnimation();
        } else {
            realDrawPosition = data.size();
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

    /**
     * 是否绘制X轴
     */
    public void setDrawXAxis(boolean drawXAxis) {
        isDrawXAxis = drawXAxis;
        invalidate();
    }

    /**
     * 第一个点是否贴边从Y轴开始绘制
     */
    public void setClingYAxisForFirst(boolean clingYAxisForFirst) {
        isClingYAxisForFirst = clingYAxisForFirst;
        invalidate();
    }

    public static class SimpleLinearChartBean {
        private int data;
        private String remarkText;
        private String dataText;
        private float dataTextSize = ConvertUtils.dp2px(12);
        private int dataTextColor = Color.parseColor("#2678E3");
        private float remarkTextSize = ConvertUtils.dp2px(11);
        private int remarkTextColor = Color.parseColor("#757575");
        private int x;
        private int y;


        public SimpleLinearChartBean(int data, String remarkText) {
            this.data = data;
            this.remarkText = remarkText;
        }

        public SimpleLinearChartBean(int data, String remarkText, String dataText) {
            this.data = data;
            this.remarkText = remarkText;
            this.dataText = dataText;
        }

        public SimpleLinearChartBean(int data, String remarkText, String dataText, float dataTextSize, int dataTextColor, float remarkTextSize, int remarkTextColor) {
            this.data = data;
            this.remarkText = remarkText;
            this.dataText = dataText;
            this.dataTextSize = dataTextSize;
            this.dataTextColor = dataTextColor;
            this.remarkTextSize = remarkTextSize;
            this.remarkTextColor = remarkTextColor;
        }

        public int getData() {
            return data;
        }

        public String getRemarkText() {
            return remarkText;
        }


    }
}
