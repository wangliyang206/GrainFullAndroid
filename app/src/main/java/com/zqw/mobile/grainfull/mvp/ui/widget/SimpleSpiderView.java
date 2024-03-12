package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
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
 * @date 2021/1/28 9:41
 * @Description 一个简单的蜘蛛网状统计图
 */
@SuppressWarnings("ALL")
public class SimpleSpiderView extends View {
    /**
     * 动画持续时间
     */
    private static final int ANIMATION_DURATION = 500;
    /**
     * 内部的多边形数（包含最外一层）
     */
    private static final int SPODER_ROUND_COUNT = 5;
    /**
     * 宽高比例
     */
    private float whPercent = 0.5f;
    /**
     * 背景
     */
    private LinearGradient background;
    /**
     * 文字与文字之间的距离
     */
    private int betweenTextDistance = ConvertUtils.dp2px(3);
    /**
     * 文字与网格线之间的距离
     */
    private int betweenWebAndTextDistance = ConvertUtils.dp2px(10);
    /**
     * 每个多边形之间的距离
     */
    private int betweenSpiderRoundDistance;

    /**
     * 内部的多边形坐标集（包含最外一层）
     */
    private List<List<Point>> pointList = new ArrayList<>();

    /**
     * 前景多边形坐标集
     */
    private List<Point> foregroundList = new ArrayList<>();
    /**
     * 数据模型
     */
    private List<SimpleSpiderViewBean> data = new ArrayList<>();

    {
        data.add(new SimpleSpiderViewBean("", "目标规划", 0f));
        data.add(new SimpleSpiderViewBean("", "自我认知", 0f));
        data.add(new SimpleSpiderViewBean("", "学习状态", 0f));
        data.add(new SimpleSpiderViewBean("", "心理健康", 0f));
        data.add(new SimpleSpiderViewBean("", "生涯学习", 0f));


    }

    /**
     * 中心点
     */
    private Point center = new Point(0, 0);
    /**
     * 两个顶点之间的夹角
     */
    private int angle = 360 / data.size();

    /**
     * 背景网格线色值
     */
    private int backgroundLineColor = Color.parseColor("#E6FFFFFF");
    /**
     * 前景网格线色值
     */
    private int foregroundLineColor = Color.parseColor("#66FFFFFF");
    /**
     * 前景小圆点色值
     */
    private int foregroundCircleColor = Color.parseColor("#FFC148");
    /**
     * 半径
     */
    private int radius;
    /**
     * 小圆点半径
     */
    private static int circleRadius = ConvertUtils.dp2px(3f);
    /**
     * 画笔
     */
    private Paint textPaint = new Paint();
    private Paint backgroundPaint = new Paint();
    private Paint foregroundPaint = new Paint();
    private Paint spiderPaint = new Paint();

    {
        textPaint.setStrokeWidth(1);
        textPaint.setAntiAlias(true);
        backgroundPaint.setStrokeWidth(1);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(backgroundLineColor);
        backgroundPaint.setPathEffect(new DashPathEffect(new float[]{3f, 6f}, 0));
        foregroundPaint.setStrokeWidth(1);
        foregroundPaint.setAntiAlias(true);
        spiderPaint.setStrokeWidth(3);
        spiderPaint.setAntiAlias(true);
        spiderPaint.setColor(foregroundCircleColor);
    }

    /**
     * 指示路径
     */
    private Path path = new Path();
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
                foregroundList.clear();
                float percent = (float) animation.getAnimatedValue();
                for (int i = 0; i < data.size(); i++) {
                    foregroundList.add(DrawViewUtils.calculatePoint(center.x, center.y, (int) (radius * percent * data.get(i).percent), getAngleForEach(i)));
                }
                invalidate();
            }
        }
    };

    public SimpleSpiderView(Context context) {
        this(context, null);
    }

    public SimpleSpiderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleSpiderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator.removeAllUpdateListeners();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (height == 0) {
            height = (int) (width * whPercent);
            setMeasuredDimension(width, height);
        }
        int maxSize = 0;
        for (int i = 0; i < data.size(); i++) {
            int[][] size = data.get(i).getSize(textPaint);
            //比较各点的文字宽高取得最大宽或高尺寸
            maxSize = Math.max(Math.max(size[0][0], size[0][1]), Math.max(size[1][0], size[1][1]));
        }
        radius = Math.min(width, height) / 2 - betweenTextDistance / 2 + betweenWebAndTextDistance / 2 - maxSize;
        center.set(width / 2, height / 2);
        betweenSpiderRoundDistance = radius / SPODER_ROUND_COUNT;
        //计算内部每个多边形顶点坐标
        pointList.clear();
        for (int i = 0; i < SPODER_ROUND_COUNT; i++) {
            List<Point> points = new ArrayList<>();
            for (int j = 0; j < data.size(); j++) {
                points.add(DrawViewUtils.calculatePoint(center.x, center.y, betweenSpiderRoundDistance * (i + 1), getAngleForEach(j)));
            }
            pointList.add(points);
        }
        background = new LinearGradient(0f, 0f, width, height, 0xff2678e3, 0xff3ad4d0, Shader.TileMode.MIRROR);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*************************************背景*****************************************/
        textPaint.setShader(background);
        canvas.drawRect(0, 0, getWidth(), getHeight(), textPaint);
        textPaint.setShader(null);
        /*************************************背景网格*****************************************/
        //骨架
        for (int i = 0; i < data.size(); i++) {
            Point point = DrawViewUtils.calculatePoint(center.x, center.y, radius, getAngleForEach(i));
            canvas.drawLine(center.x, center.y, point.x, point.y, backgroundPaint);
        }
        //多边形背景
        for (int i = 0; i < pointList.size(); i++) {
            for (int j = 0; j < pointList.get(i).size(); j++) {
                if (j == pointList.get(i).size() - 1) {
                    canvas.drawLine(pointList.get(i).get(j).x, pointList.get(i).get(j).y, pointList.get(i).get(0).x, pointList.get(i).get(0).y, backgroundPaint);
                } else {
                    canvas.drawLine(pointList.get(i).get(j).x, pointList.get(i).get(j).y, pointList.get(i).get(j + 1).x, pointList.get(i).get(j + 1).y, backgroundPaint);
                }
            }
        }
        /*************************************前景*****************************************/
        //多边形
        path.reset();
        if (foregroundList.size() > 0) {
            for (int i = 0; i < foregroundList.size(); i++) {
                if (i == 0) {
                    path.moveTo(foregroundList.get(i).x, foregroundList.get(i).y);
                } else if (i == foregroundList.size() - 1) {
                    path.lineTo(foregroundList.get(i).x, foregroundList.get(i).y);
                    path.close();
                } else {
                    path.lineTo(foregroundList.get(i).x, foregroundList.get(i).y);
                }
            }
        }
        foregroundPaint.setColor(foregroundLineColor);
        canvas.drawPath(path, foregroundPaint);
        //线
        if (foregroundList.size() > 0) {
            for (int i = 0; i < foregroundList.size(); i++) {
                if (i == foregroundList.size() - 1) {
                    canvas.drawLine(foregroundList.get(i).x, foregroundList.get(i).y, foregroundList.get(0).x, foregroundList.get(0).y, spiderPaint);
                } else {
                    canvas.drawLine(foregroundList.get(i).x, foregroundList.get(i).y, foregroundList.get(i + 1).x, foregroundList.get(i + 1).y, spiderPaint);
                }
            }
        }

        if (foregroundList.size() > 0) {
            for (int i = 0; i < foregroundList.size(); i++) {
                //圆点
                foregroundPaint.setColor(foregroundCircleColor);
                canvas.drawCircle(foregroundList.get(i).x, foregroundList.get(i).y, circleRadius, foregroundPaint);
                //小白点
                foregroundPaint.setColor(Color.WHITE);
                canvas.drawCircle(foregroundList.get(i).x, foregroundList.get(i).y, circleRadius / 2, foregroundPaint);
            }
        }
        /*************************************文字*****************************************/
        for (int i = 0; i < data.size(); i++) {
            int[][] textSize = data.get(i).getSize(textPaint);
            int maxSize = Math.max(Math.max(textSize[0][0], textSize[0][1]), Math.max(textSize[1][0], textSize[1][1]));
            if (getAngleForEach(i) == 0) {
                textPaint.setTextSize(data.get(i).textSize);
                textPaint.setColor(data.get(i).textColor);
                textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, data.get(i).textStyle));
                canvas.drawText(data.get(i).text, center.x + radius + betweenWebAndTextDistance + maxSize - textSize[0][0] / 2, getHeight() / 2 - betweenTextDistance / 2 - textSize[0][1] / 2, textPaint);

                textPaint.setTextSize(data.get(i).remarkSize);
                textPaint.setColor(data.get(i).remarkColor);
                textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, data.get(i).remarkStyle));
                canvas.drawText(data.get(i).remark, center.x + radius + betweenWebAndTextDistance + maxSize / 2, getHeight() / 2 + betweenTextDistance / 2 + textSize[0][1] / 2, textPaint);
            } else if (getAngleForEach(i) == 90) {
                textPaint.setTextSize(data.get(i).textSize);
                textPaint.setColor(data.get(i).textColor);
                textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, data.get(i).textStyle));
                canvas.drawText(data.get(i).text, center.x - textSize[0][0] / 2, center.y + radius + betweenWebAndTextDistance + textSize[0][1] / 2, textPaint);

                textPaint.setTextSize(data.get(i).remarkSize);
                textPaint.setColor(data.get(i).remarkColor);
                textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, data.get(i).remarkStyle));
                canvas.drawText(data.get(i).remark, center.x - textSize[1][0] / 2, center.y + radius + betweenWebAndTextDistance + textSize[0][1] + betweenTextDistance + textSize[1][1] / 2, textPaint);

            } else if (getAngleForEach(i) == 180) {
                textPaint.setTextSize(data.get(i).textSize);
                textPaint.setColor(data.get(i).textColor);
                textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, data.get(i).textStyle));
                canvas.drawText(data.get(i).text, center.x - radius - betweenWebAndTextDistance - maxSize - textSize[0][0] / 2, getHeight() / 2 - betweenTextDistance / 2 - textSize[0][1] / 2, textPaint);

                textPaint.setTextSize(data.get(i).remarkSize);
                textPaint.setColor(data.get(i).remarkColor);
                textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, data.get(i).remarkStyle));
                canvas.drawText(data.get(i).remark, center.x - radius - betweenWebAndTextDistance - maxSize - maxSize / 2, getHeight() / 2 + betweenTextDistance / 2 + textSize[0][1] / 2, textPaint);
            } else if (getAngleForEach(i) == 270) {
                textPaint.setTextSize(data.get(i).textSize);
                textPaint.setColor(data.get(i).textColor);
                textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, data.get(i).textStyle));
                canvas.drawText(data.get(i).text, center.x - textSize[0][0] / 2, getHeight() / 2 - (radius + textSize[0][1] / 2 + betweenTextDistance + betweenWebAndTextDistance), textPaint);

                textPaint.setTextSize(data.get(i).remarkSize);
                textPaint.setColor(data.get(i).remarkColor);
                textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, data.get(i).remarkStyle));
                canvas.drawText(data.get(i).remark, center.x - textSize[1][0] / 2, getHeight() / 2 - (radius + textSize[1][1] / 2 + betweenWebAndTextDistance - textSize[1][1]), textPaint);
            } else {
                switch (DrawViewUtils.getQuadrantPositionByAngle(getAngleForEach(i), 1)) {
                    case 1:
                    case 4:
                        textPaint.setTextSize(data.get(i).textSize);
                        textPaint.setColor(data.get(i).textColor);
                        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, data.get(i).textStyle));
                        Point point1 = DrawViewUtils.calculatePoint(center.x, center.y, radius, getAngleForEach(i));
                        canvas.drawText(data.get(i).text, point1.x + maxSize / 2 + betweenWebAndTextDistance - textSize[0][0] / 2, point1.y - betweenTextDistance / 2 - textSize[0][1] / 2, textPaint);

                        textPaint.setTextSize(data.get(i).remarkSize);
                        textPaint.setColor(data.get(i).remarkColor);
                        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, data.get(i).remarkStyle));
                        canvas.drawText(data.get(i).remark, point1.x + betweenWebAndTextDistance, point1.y + betweenTextDistance / 2 + textSize[0][1] / 2, textPaint);
                        break;
                    case 2:
                    case 3:
                        textPaint.setTextSize(data.get(i).textSize);
                        textPaint.setColor(data.get(i).textColor);
                        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, data.get(i).textStyle));
                        Point point3 = DrawViewUtils.calculatePoint(center.x, center.y, radius, getAngleForEach(i));
                        canvas.drawText(data.get(i).text, point3.x + maxSize / 2 - betweenWebAndTextDistance - maxSize - textSize[0][0] / 2, point3.y - betweenTextDistance / 2 - textSize[0][1] / 2, textPaint);

                        textPaint.setTextSize(data.get(i).remarkSize);
                        textPaint.setColor(data.get(i).remarkColor);
                        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, data.get(i).remarkStyle));
                        canvas.drawText(data.get(i).remark, point3.x - betweenWebAndTextDistance - maxSize, point3.y + betweenTextDistance / 2 + textSize[0][1] / 2, textPaint);

                        break;
                    default:
                }
            }
        }
    }

    /**
     * 设置数据
     */
    public void setData(List<SimpleSpiderViewBean> list, boolean withAnimation) {
        if (data.size() == 0) {
            return;
        }
        this.data = list;
        angle = 360 / data.size();
        if (withAnimation) {
            startAnimation();
        } else {
            foregroundList.clear();
            float percent = 1f;
            for (int i = 0; i < data.size(); i++) {
                foregroundList.add(DrawViewUtils.calculatePoint(center.x, center.y, (int) (radius * percent * data.get(i).percent), getAngleForEach(i)));
            }
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
        valueAnimator = ValueAnimator.ofFloat(0, 1f);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setDuration(ANIMATION_DURATION);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(listener);
        valueAnimator.start();
    }


    /**
     * 计算背景点
     */
    private Point[] calcBackgroundPoint(int i, int radius) {
        Point[] points = new Point[2];
        if (i == 0) {
            points[0] = DrawViewUtils.calculatePoint(center.x, center.y, radius, getAngleForEach(i));
            points[1] = DrawViewUtils.calculatePoint(center.x, center.y, radius, getAngleForEach(data.size() - 1));
        } else if (i == data.size() - 1) {
            points[0] = DrawViewUtils.calculatePoint(center.x, center.y, radius, getAngleForEach(data.size()));
            points[1] = DrawViewUtils.calculatePoint(center.x, center.y, radius, angle - getAngleForZero());
        } else {
            points[0] = DrawViewUtils.calculatePoint(center.x, center.y, radius, getAngleForEach(i));
            points[1] = DrawViewUtils.calculatePoint(center.x, center.y, radius, getAngleForEach(i + 1));
        }
        return points;
    }

    /**
     * 计算角度与0度间的夹角
     */
    private int getAngleForZero() {
        return 90 - angle;
    }

    /**
     * 计算每个顶点的角度
     */
    private int getAngleForEach(int i) {
        return i * angle - getAngleForZero();
    }

    /**
     * 数据模型
     */
    public static class SimpleSpiderViewBean {
        private String text;
        private float textSize = ConvertUtils.dp2px(16);
        private int textColor = Color.parseColor("#FFFFFF");
        private int textStyle = Typeface.BOLD;
        private String remark;
        private float remarkSize = ConvertUtils.dp2px(12);
        private int remarkColor = Color.parseColor("#FFFFFF");
        private int remarkStyle = Typeface.NORMAL;

        private float percent;


        public SimpleSpiderViewBean(String text, String remark, float percent) {
            this.text = text;
            this.remark = remark;
            this.percent = percent;
        }

        public SimpleSpiderViewBean(String text, float textSize, int textColor, int textStyle, String remark, float remarkSize, int remarkColor, int remarkStyle, float percent) {
            this.text = text;
            this.textSize = textSize;
            this.textColor = textColor;
            this.textStyle = textStyle;
            this.remark = remark;
            this.remarkSize = remarkSize;
            this.remarkColor = remarkColor;
            this.remarkStyle = remarkStyle;
            this.percent = percent;
        }

        public String getText() {
            return text;
        }

        public String getRemark() {
            return remark;
        }

        public float getPercent() {
            return percent;
        }

        private int[][] getSize(Paint paint) {

            int size[][] = new int[2][2];
            paint.setTextSize(textSize);
            paint.setColor(textColor);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, textStyle));
            int[] textSize = DrawViewUtils.getTextWH(paint, text);
            paint.setTextSize(remarkSize);
            paint.setColor(remarkColor);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, remarkStyle));
            int[] remarkSize = DrawViewUtils.getTextWH(paint, remark);
            size[0] = textSize;
            size[1] = remarkSize;
            return size;
        }
    }
}
