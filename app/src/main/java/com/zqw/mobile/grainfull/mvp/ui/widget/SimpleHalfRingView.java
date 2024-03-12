package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.zqw.mobile.grainfull.app.utils.DrawViewUtils;

/**
 * @author Michael by Administrator
 * @date 2021/12/16 09:33
 * @Description 一个简单的半圆环指示器（xml中支持paddingStart和paddingEnd）
 */
public class SimpleHalfRingView extends View {
    /**
     * 动画持续时间
     */
    private static final int ANIMATION_DURATION = 1000;
    /**
     * 是否绘制指示线动画
     */
    private boolean pointAnimation = false;
    /**
     * 中心点
     */
    private Point centerPoint = new Point();
    /**
     * 宽高比例
     */
    private float whPercent = 0.5f;
    /**
     * 圆环大小比例
     */
    private float ringPercent = 0.4f;
    /**
     * 宽度
     */
    private int width;
    /**
     * 高度
     */
    private int height;
    /**
     * 圆环半径
     */
    private float radius;
    /**
     * 圆环线宽
     */
    private int ringStrokeWidth = ConvertUtils.dp2px(18);
    /**
     * 指示点半径
     */
    int pointRadius = ConvertUtils.dp2px(3);
    /**
     * 指示点与圆环的距离
     */
    int betweenPointAndArc = ConvertUtils.dp2px(2);
    /**
     * 指示文字与指示文字之间的距离
     */
    private int distanceBetweenTextAndMidLine = ConvertUtils.dp2px(5);
    /**
     * 文字绘制区域与指示线之间的距离
     */
    private int distanceBetweenTextRectAndLine = ConvertUtils.dp2px(10);
    /**
     * 文字与指示线之间的距离
     */
    private int distanceBetweenLineAndPoint = ConvertUtils.dp2px(2);
    /**
     * 数字与百分号之间的距离
     */
    private int distanceBetweenNumberAndPercent = ConvertUtils.dp2px(3);
    /**
     * 任务数字与单位之间的距离
     */
    private int distanceBetweenTaskNumberAndSuffix = ConvertUtils.dp2px(3);
    /**
     * 与指示点连接的横线长度
     */
    private int lineLength = ConvertUtils.dp2px(30);
    /**
     * 圆环背景前景矩阵
     */
    private RectF ringBackgroundRect = new RectF(), ringForegroundRect = new RectF();
    /**
     * 中心虚线小圆环矩阵
     */
    private RectF pointRect = new RectF();
    /**
     * 圆环绘制开始、结束角度
     */
    private int startAngle = 120, endAngle = 300;
    /**
     * 圆环前景色
     */
    private int forceColor = 0xffffffff;
    /**
     * 圆环背景色
     */
    private int ringBackColor = 0x4dffffff;

    /**
     * 动画实时进度百分比
     */
    private float currentPercent = 0f;
    /**
     * 总百分比，由外部传入，与{@link #total} 相除所得的百分比
     */
    private float totalPercent = 0.9f;
    /**
     * 中心数字，此数字为总数
     */
    private int total = 888;
    /**
     * 画笔
     */
    private Paint textPaint = new Paint();
    private Paint drawPaint = new Paint();

    {
        drawPaint.setAntiAlias(true);
        textPaint.setAntiAlias(true);
    }

    public SimpleHalfRingView(Context context) {
        this(context, null);
    }

    public SimpleHalfRingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleHalfRingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(888, 0.5f, true);
            }
        });
    }

    public SimpleHalfRingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
        centerPoint.set(width / 2, height / 2);
        radius = Math.min(width * ringPercent, height * ringPercent);
        ringBackgroundRect.set(
                centerPoint.x - radius,
                centerPoint.y - radius,
                centerPoint.x + radius,
                centerPoint.y + radius
        );
        ringForegroundRect.set(
                centerPoint.x - radius,
                centerPoint.y - radius,
                centerPoint.x + radius,
                centerPoint.y + radius
        );
        pointRect.set(
                centerPoint.x - radius + (ringStrokeWidth >> 1) + ConvertUtils.dp2px(5),
                centerPoint.y - radius + (ringStrokeWidth >> 1) + ConvertUtils.dp2px(5),
                centerPoint.x + radius - (ringStrokeWidth >> 1) - ConvertUtils.dp2px(5),
                centerPoint.y + radius - (ringStrokeWidth >> 1) - ConvertUtils.dp2px(5)
        );
    }


    DashPathEffect pointDashPathEffect = new DashPathEffect(new float[]{ConvertUtils.dp2px(2), 10}, 0);
    DashPathEffect lineDashPathEffect = new DashPathEffect(new float[]{3, 5}, 0);


    private Rect rect = new Rect();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /************************************背景透明**************************************/
        setBackgroundColor(Color.TRANSPARENT);
        /************************************圆环背景**************************************/
        drawPaint.setColor(ringBackColor);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeWidth(ringStrokeWidth);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(ringBackgroundRect, startAngle, endAngle, false, drawPaint);
        drawPaint.setStrokeCap(Paint.Cap.BUTT);
        /************************************内圈圆点**************************************/
        drawPaint.setColor(ringBackColor);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeWidth(ConvertUtils.dp2px(1));
        drawPaint.setPathEffect(pointDashPathEffect);
        canvas.drawArc(pointRect, startAngle, endAngle, false, drawPaint);
        drawPaint.setPathEffect(null);
        /************************************圆环前景**************************************/
        drawPaint.setColor(forceColor);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeWidth(ringStrokeWidth);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(ringBackgroundRect, startAngle, getSweepAngle(getProgress()), false, drawPaint);
        drawPaint.setStrokeCap(Paint.Cap.BUTT);
        /************************************中间文字**************************************/
        int offset = ConvertUtils.dp2px(5);

        String numberStr = String.valueOf((int) (currentPercent * total));
        textPaint.setTextSize(ConvertUtils.dp2px(36));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        int[] numberSize = DrawViewUtils.getTextWH(textPaint, numberStr);
        canvas.drawText(numberStr, centerPoint.x - (numberSize[0] >> 1) - offset, centerPoint.y + ConvertUtils.dp2px(2), textPaint);


        String suffixStr = "个";
        textPaint.setTextSize(ConvertUtils.dp2px(18));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(suffixStr, centerPoint.x - (numberSize[0] >> 1) + numberSize[0] + ConvertUtils.dp2px(2) + distanceBetweenTaskNumberAndSuffix - offset, centerPoint.y, textPaint);

        String taskStr = "任务";
        textPaint.setTextSize(ConvertUtils.dp2px(14));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        int[] taskSize = DrawViewUtils.getTextWH(textPaint, taskStr);
        canvas.drawText(taskStr, centerPoint.x - ((taskSize[1] + offset) >> 1), centerPoint.y + (numberSize[1] >> 1) + taskSize[1], textPaint);
        /**************************************************************************指示区域****************************************************************************/

        /************************************左半球指示文字**************************************/
        String strLeftNumber = getProgressStr(true) + "";
        textPaint.setColor(forceColor);
        textPaint.setTextSize(ConvertUtils.sp2px(22f));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        int[] strLeftNumberSize = DrawViewUtils.getTextWH(textPaint, strLeftNumber);

        String strLeftPercent = "%";
        textPaint.setColor(forceColor);
        textPaint.setTextSize(ConvertUtils.sp2px(12f));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        int[] strLeftPercentSize = DrawViewUtils.getTextWH(textPaint, strLeftPercent);

        String strLeftDown = "已完成";
        textPaint.setColor(forceColor);
        textPaint.setTextSize(ConvertUtils.sp2px(12f));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        int[] strLeftDownSize = DrawViewUtils.getTextWH(textPaint, strLeftDown);


        rect.left = getPaddingStart();
        rect.top = centerPoint.y - distanceBetweenTextAndMidLine - strLeftNumberSize[1];
        rect.right = rect.left + strLeftNumberSize[0] + strLeftPercentSize[0] + distanceBetweenNumberAndPercent;
        rect.bottom = centerPoint.y + distanceBetweenTextAndMidLine + strLeftDownSize[1];

        textPaint.setTextSize(ConvertUtils.sp2px(22f));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(strLeftNumber, rect.left, ((rect.bottom + rect.top) >> 1) - distanceBetweenTextAndMidLine, textPaint);
        textPaint.setTextSize(ConvertUtils.sp2px(12f));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(strLeftPercent, rect.right - strLeftPercentSize[0], ((rect.bottom + rect.top) >> 1) - distanceBetweenTextAndMidLine, textPaint);
        textPaint.setTextSize(ConvertUtils.sp2px(12f));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(strLeftDown, rect.left + (rect.width() >> 1) - (strLeftDownSize[0] >> 1), ((rect.bottom + rect.top) >> 1) + distanceBetweenTextAndMidLine, textPaint);
        /************************************左半球指示点**************************************/

        float leftAngle;

        if (pointAnimation) {
            leftAngle = startAngle + getSweepAngle(Math.min(getProgress(), 0.5f)) * 0.5f;
        } else {
            leftAngle = startAngle + getSweepAngle((float) (totalPercent >= 0.5f ? 0.5 : totalPercent)) * 0.5f;
        }
        Point leftPoint = DrawViewUtils.calculatePoint(centerPoint.x, centerPoint.y, (int) (radius + ringStrokeWidth + pointRadius + betweenPointAndArc), leftAngle);
        drawPaint.setColor(forceColor);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeWidth(ConvertUtils.dp2px(2));
        canvas.drawCircle(leftPoint.x, leftPoint.y, pointRadius, drawPaint);
        /************************************左半球指示线**************************************/
        drawPaint.setColor(forceColor);
        drawPaint.setStyle(Paint.Style.FILL);
        drawPaint.setStrokeWidth(ConvertUtils.dp2px(2));
        drawPaint.setPathEffect(lineDashPathEffect);
        int startLeftX = leftPoint.x - pointRadius - distanceBetweenLineAndPoint;
        int startY = leftPoint.y;
        int endLeftX = leftPoint.x - pointRadius - distanceBetweenLineAndPoint - lineLength;
        int endLeftY = leftPoint.y;
        canvas.drawLine(startLeftX, startY, endLeftX, endLeftY, drawPaint);
        canvas.drawLine(endLeftX, endLeftY, rect.right + distanceBetweenTextRectAndLine, rect.top + (rect.height() >> 1), drawPaint);
        drawPaint.setPathEffect(null);
        /**************************************************************************右半球****************************************************************************/
        /************************************右半球指示文字**************************************/
        String strRightPercent = "%";
        textPaint.setColor(forceColor);
        textPaint.setTextSize(ConvertUtils.sp2px(12f));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        int[] strRightPercentSize = DrawViewUtils.getTextWH(textPaint, strRightPercent);


        String strRightNumber = getProgressStr(false) + "";
        textPaint.setColor(forceColor);
        textPaint.setTextSize(ConvertUtils.sp2px(22f));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        int[] strRightNumberSize = DrawViewUtils.getTextWH(textPaint, strRightNumber);


        String strRightDown = "未完成";
        textPaint.setColor(forceColor);
        textPaint.setTextSize(ConvertUtils.sp2px(12f));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        int[] strRightDownSize = DrawViewUtils.getTextWH(textPaint, strRightDown);

        rect.right = getWidth() - getPaddingEnd();
        rect.left = rect.right - strRightPercentSize[0] - distanceBetweenNumberAndPercent - strRightNumberSize[0];
        rect.top = centerPoint.y - distanceBetweenTextAndMidLine - strRightNumberSize[1];
        rect.bottom = centerPoint.y + distanceBetweenTextAndMidLine + strRightDownSize[1];

        textPaint.setTextSize(ConvertUtils.sp2px(12f));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(strRightPercent, rect.right - strRightPercentSize[0], ((rect.bottom + rect.top) >> 1) - distanceBetweenTextAndMidLine, textPaint);
        textPaint.setTextSize(ConvertUtils.sp2px(22f));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(strRightNumber, rect.right - strRightPercentSize[0] - strRightNumberSize[0] - distanceBetweenNumberAndPercent, ((rect.bottom + rect.top) >> 1) - distanceBetweenTextAndMidLine, textPaint);
        textPaint.setTextSize(ConvertUtils.sp2px(12f));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(strRightDown, rect.left + (rect.width() >> 1) - (strLeftDownSize[0] >> 1), ((rect.bottom + rect.top) >> 1) + distanceBetweenTextAndMidLine, textPaint);


        /************************************右半球指示点**************************************/

        Point rightPoint;
        if (pointAnimation) {
            float progress = (float) (Math.min(currentPercent, 0.5f) * totalPercent);
            float angle = getSweepAngle(progress) * 0.5f;
            rightPoint = DrawViewUtils.calculatePoint(centerPoint.x, centerPoint.y, (int) (radius + ringStrokeWidth + pointRadius + betweenPointAndArc), startAngle + 180 + 45 + angle);
        } else {
            float progress = (float) (Math.min(totalPercent, 0.5f) * totalPercent);
            float angle = getSweepAngle(progress) * 0.5f;
            rightPoint = DrawViewUtils.calculatePoint(centerPoint.x, centerPoint.y, (int) (radius + ringStrokeWidth + pointRadius + betweenPointAndArc), startAngle + 180 + 45 + angle);
        }
//        rightPoint = DrawViewUtils.calculatePoint(centerPoint.x, centerPoint.y, (int) (radius + ringStrokeWidth + pointRadius + betweenPointAndArc), leftAngle + 180);
        drawPaint.setColor(forceColor);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeWidth(ConvertUtils.dp2px(2));
        canvas.drawCircle(rightPoint.x, rightPoint.y, pointRadius, drawPaint);
        /************************************左半球指示线**************************************/
        drawPaint.setColor(forceColor);
        drawPaint.setStyle(Paint.Style.FILL);
        drawPaint.setStrokeWidth(ConvertUtils.dp2px(2));
        drawPaint.setPathEffect(lineDashPathEffect);
        int startRightLeftX = rightPoint.x + pointRadius + distanceBetweenLineAndPoint;
        int startRightY = rightPoint.y;
        int endRightX = rightPoint.x + pointRadius + distanceBetweenLineAndPoint + lineLength;
        int endRightY = rightPoint.y;
        canvas.drawLine(startRightLeftX, startRightY, endRightX, endRightY, drawPaint);
        canvas.drawLine(endRightX, endRightY, rect.left - distanceBetweenTextRectAndLine, rect.top + (rect.height() >> 1), drawPaint);
        drawPaint.setPathEffect(null);
    }

    /**
     * 按进度转换为角度
     *
     * @param progress
     * @return
     */
    private int getSweepAngle(float progress) {
        return (int) (progress * endAngle);
    }

    /**
     * 进度转文本
     *
     * @param isLeft 是否位于左侧
     * @return 0-100
     */
    private int getProgressStr(boolean isLeft) {
        return isLeft ? (int) (1f * totalPercent * 100) : (int) (1f * (1f - totalPercent) * 100);
    }

    /**
     * 获取实时进度
     *
     * @return 0f-1f
     */
    private float getProgress() {
        return (float) (currentPercent * totalPercent);
    }


    /**
     * 开始动画
     */
    public void startAnimation() {
        clear();
        valueAnimator = ValueAnimator.ofFloat(0, 1f);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setDuration(ANIMATION_DURATION);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(listener);
        valueAnimator.start();
    }

    /**
     * 打扫卫生
     */
    public void clear() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator.removeAllListeners();
            valueAnimator.removeAllUpdateListeners();
        }
    }

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
            currentPercent = (float) animation.getAnimatedValue();
            if (onSimpleHalfRingViewCallBack != null) {
                onSimpleHalfRingViewCallBack.onProgressChanged(getProgress(), total, true);
            }
            invalidate();
        }
    };

    public void setData(int total, float totalPercent, boolean animation) {
        pointAnimation = animation;
        currentPercent = 1f;
        this.total = total;
        this.totalPercent = totalPercent;
        if (animation) {
            startAnimation();
        } else {
            requestLayout();
            if (onSimpleHalfRingViewCallBack != null) {
                onSimpleHalfRingViewCallBack.onProgressChanged(getProgress(), total, false);
            }
        }
    }

    private OnSimpleHalfRingViewCallBack onSimpleHalfRingViewCallBack;

    public void setOnSimpleHalfRingViewCallBack(OnSimpleHalfRingViewCallBack onSimpleHalfRingViewCallBack) {
        this.onSimpleHalfRingViewCallBack = onSimpleHalfRingViewCallBack;
    }

    public interface OnSimpleHalfRingViewCallBack {
        /**
         * @param percent       当前进度（百分比）
         * @param total         总数
         * @param fromAnimation 该变化是否来自动画触发
         */
        void onProgressChanged(float percent, int total, boolean fromAnimation);
    }
}