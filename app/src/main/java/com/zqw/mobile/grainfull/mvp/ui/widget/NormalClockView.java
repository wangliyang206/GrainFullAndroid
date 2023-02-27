package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.widget
 * @ClassName: DialView
 * @Description: 普通时钟
 * @Author: WLY
 * @CreateDate: 2023/2/21 17:18
 */
public class NormalClockView extends View {
    // 是否可以绘制
    private boolean drawable = true;
    // 最小宽/高的一半长度
    private int halfMinLength;
    // 时针刻度线画笔
    private Paint paintKd30;
    // 时针数字画笔
    private Paint paintKd30Text;
    // 秒针刻度线画笔
    private Paint paintKdSecond;
    // 时针画笔
    private Paint paintHour;
    // 指针圆心画笔
    private Paint paintCircleBar;
    // 分针画笔
    private Paint paintMinute;
    // 秒针画笔
    private Paint paintSecond;
    // 时针旋转角度
    private float angleHour;
    // 分针旋转角度
    private float angleMinute;
    // 秒针旋转角度
    private float angleSecond;
    // 当前秒
    private int cuurSecond;
    // 当前分
    private int cuurMinute;
    // 当前时
    private int cuurHour;
    private Calendar mCalendar;
    // 上午/下午
    private boolean isMorning = true;
    private String[] strKedu = {"3", "2", "1", "12", "11", "10", "9", "8", "7", "6", "5", "4"};

    public NormalClockView(Context context) {
        this(context, null);
    }

    public NormalClockView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public NormalClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化画笔
        initPaint();
        // 初始化时间
        initTime();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        halfMinLength = Math.min(width, height) / 2;
        System.out.println(halfMinLength);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 表盘刻度绘制
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) {
                // 获取刻度路径
                float[] dialKdPaths = getDialPaths(halfMinLength, halfMinLength, halfMinLength - 8, halfMinLength * 5 / 6, -i * 6);
                canvas.drawLines(dialKdPaths, paintKd30);
                float[] dialPathsStr = getDialPaths(halfMinLength, halfMinLength, halfMinLength - 8, halfMinLength * 3 / 4, -i * 6);
                canvas.drawText(strKedu[i / 5], dialPathsStr[2] - 16, dialPathsStr[3] + 14, paintKd30Text);
                continue;
            }
            float[] dialKdPaths = getDialPaths(halfMinLength, halfMinLength, halfMinLength - 8, halfMinLength * 7 / 8, -i * 6);
            canvas.drawLines(dialKdPaths, paintKdSecond);
        }
        // 指针绘制
        // 时针绘制
        canvas.save();
        // 保存之前内容
        canvas.rotate(angleHour, halfMinLength, halfMinLength);
        // 旋转的是画布,从而得到指针旋转的效果
        canvas.drawLine(halfMinLength, halfMinLength, halfMinLength, halfMinLength * 3 / 4, paintHour);
        canvas.restore();
        // 恢复
        // 绘制分针
        canvas.save();
        canvas.rotate(angleMinute, halfMinLength, halfMinLength);
        // 旋转的是画布,从而得到指针旋转的效果
        canvas.drawLine(halfMinLength, halfMinLength, halfMinLength, halfMinLength / 2, paintMinute);
        paintCircleBar.setColor(Color.rgb(75, 75, 75));
        paintCircleBar.setShadowLayer(4, 4, 8, Color.argb(70, 40, 40, 40));
        canvas.drawCircle(halfMinLength, halfMinLength, 24, paintCircleBar);
        canvas.restore();
        // 绘制秒针
        canvas.save();
        canvas.rotate(angleSecond, halfMinLength, halfMinLength);
        // 旋转的是画布,从而得到指针旋转的效果
        canvas.drawLine(halfMinLength, halfMinLength + 40, halfMinLength, halfMinLength / 4 - 20, paintSecond);
        paintCircleBar.setColor(Color.rgb(178, 34, 34));
        paintCircleBar.setShadowLayer(4, 4, 8, Color.argb(50, 80, 0, 0));
        canvas.drawCircle(halfMinLength, halfMinLength, 12, paintCircleBar);
        canvas.restore();
    }

    /**
     * 初始化时,分,秒
     */
    private void initTime() {
        mCalendar = Calendar.getInstance();
        cuurHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        cuurMinute = mCalendar.get(Calendar.MINUTE);
        cuurSecond = mCalendar.get(Calendar.SECOND);
        if (cuurHour >= 12) {
            cuurHour = cuurHour - 12;
            isMorning = false;
        } else {
            isMorning = true;
        }
        angleSecond = cuurSecond * 6f;
        angleMinute = cuurMinute * 6f;
        angleHour = cuurHour * 6f * 5f;
    }

    /**
     * 更新时分秒针的角度,开始绘制
     */
    public void startRun() {
        new Thread(() -> {
            while (drawable) {
                try {
                    // 睡1s
                    Thread.sleep(1000);
                    // 更新秒针角度
                    updataAngleSecond();
                    // 更新分针角度
                    updataAngleMinute();
                    // 更新时针角度
                    updataAngleHour();
                    if (mListener != null) {
                        mListener.onClockMonitor(mCalendar.get(Calendar.AM_PM) == Calendar.PM, cuurHour, cuurMinute, cuurSecond);
                    }
                    // 重新绘制
                    postInvalidate();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 更新时针角度
     */
    private void updataAngleHour() {
        // 更新时针角度
        angleHour = angleHour + (30f / 3600);
        if (angleHour >= 360) {
            angleHour = 0;
            cuurHour = 0;
        }
    }

    /**
     * 更新分针角度
     */
    private void updataAngleMinute() {
        // 更新分针角度
        angleMinute = angleMinute + 0.1f;
        if (angleMinute >= 360) {
            angleMinute = 0;
            cuurMinute = 0;
            cuurHour += 1;
        }
    }

    /**
     * 更新秒针角度
     */
    private void updataAngleSecond() {
        // 更新秒针角度
        angleSecond = angleSecond + 6;
        cuurSecond += 1;
        if (angleSecond >= 360) {
            angleSecond = 0;
            cuurSecond = 0;
            cuurMinute += 1;
            // 一分钟同步一次本地时间
            mCalendar = Calendar.getInstance();
            cuurHour = mCalendar.get(Calendar.HOUR_OF_DAY);
            cuurMinute = mCalendar.get(Calendar.MINUTE);
            cuurSecond = mCalendar.get(Calendar.SECOND);
            if (cuurHour >= 12) {
                cuurHour = cuurHour - 12;
                isMorning = false;
            } else {
                isMorning = true;
            }
            angleSecond = cuurSecond * 6f;
            angleMinute = cuurMinute * 6f;
            angleHour = cuurHour * 6f * 5f;
        }
    }

    /**
     * 停止绘制
     */
    public void stopDrawing() {
        drawable = false;
    }

    /**
     * 通过改变角度值,获取不同角度方向的外圆一点到圆心连线过内圆一点的路径坐标集合
     *
     * @param x0          圆心x
     * @param y0          圆心y
     * @param outRadius   外圆半径
     * @param innerRadius 内圆半径
     * @param angle       角度
     * @return 返回
     */
    private float[] getDialPaths(int x0, int y0,
                                 int outRadius, int innerRadius, int angle) {
        float[] paths = new float[4];
        paths[0] = (float) (x0 + outRadius * Math.cos(angle * Math.PI / 180));
        paths[1] = (float) (y0 + outRadius * Math.sin(angle * Math.PI / 180));
        paths[2] = (float) (x0 + innerRadius * Math.cos(angle * Math.PI / 180));
        paths[3] = (float) (y0 + innerRadius * Math.sin(angle * Math.PI / 180));
        return paths;
    }

    /**
     * 初始化画笔参数
     */
    private void initPaint() {
        paintKd30 = new Paint();
        paintKd30.setStrokeWidth(8);
        paintKd30.setColor(Color.rgb(75, 75, 75));
        paintKd30.setAntiAlias(true);
        paintKd30.setDither(true);
        paintKd30.setStrokeCap(Paint.Cap.ROUND);
        paintKd30Text = new Paint();
        // 左对齐
        paintKd30Text.setTextAlign(Paint.Align.LEFT);
        // 设置宽度
        paintKd30Text.setStrokeWidth(6);
        // 文字大小
        paintKd30Text.setTextSize(40);
        // 加粗
        paintKd30Text.setTypeface(Typeface.DEFAULT_BOLD);
        // 画笔颜色
        paintKd30Text.setColor(Color.rgb(75, 75, 75));
        // 抗锯齿
        paintKd30Text.setAntiAlias(true);
        // 抖动
        paintKd30Text.setDither(true);
        // 笔尖圆角
        paintKd30Text.setStrokeCap(Paint.Cap.ROUND);
        // 阴影
        paintKd30Text.setShadowLayer(4, 2, 4, Color.argb(60, 90, 90, 90));
        paintKdSecond = new Paint();
        paintKdSecond.setStrokeWidth(6);
        paintKdSecond.setColor(Color.rgb(75, 75, 75));
        paintKdSecond.setAntiAlias(true);
        paintKdSecond.setDither(true);
        paintKdSecond.setStrokeCap(Paint.Cap.ROUND);
        paintKdSecond.setShadowLayer(4, 5, 10, Color.argb(50, 80, 80, 80));
        paintHour = new Paint();
        paintHour.setStrokeWidth(30);
        paintHour.setColor(Color.rgb(75, 75, 75));
        paintHour.setAntiAlias(true);
        paintHour.setDither(true);
        paintHour.setStrokeCap(Paint.Cap.ROUND);
        paintHour.setShadowLayer(4, 5, 10, Color.argb(50, 80, 80, 80));
        paintCircleBar = new Paint();
        paintCircleBar.setStrokeWidth(6);
        paintCircleBar.setColor(Color.rgb(178, 34, 34));
        paintCircleBar.setAntiAlias(true);
        paintCircleBar.setDither(true);
        paintCircleBar.setStrokeCap(Paint.Cap.ROUND);
        paintCircleBar.setShadowLayer(4, 5, 10, Color.argb(100, 80, 80, 80));
        paintMinute = new Paint();
        paintMinute.setStrokeWidth(30);
        paintMinute.setColor(Color.rgb(75, 75, 75));
        paintMinute.setAntiAlias(true);
        paintMinute.setDither(true);
        paintMinute.setStrokeCap(Paint.Cap.ROUND);
        paintMinute.setShadowLayer(4, 5, 10, Color.rgb(80, 80, 80));
        paintSecond = new Paint();
        paintSecond.setStrokeWidth(6);
        paintSecond.setColor(Color.rgb(180, 30, 30));
        paintSecond.setAntiAlias(true);
        paintSecond.setDither(true);
        paintSecond.setStrokeCap(Paint.Cap.ROUND);
        paintSecond.setShadowLayer(4, 2, 10, Color.argb(100, 90, 90, 90));
    }

    private onClockMonitorListener mListener;

    public interface onClockMonitorListener {
        void onClockMonitor(boolean isNoon, int hour, int minute, int second);
    }

    public void setOnClockMonitorListener(onClockMonitorListener listener) {
        this.mListener = listener;
    }
}
