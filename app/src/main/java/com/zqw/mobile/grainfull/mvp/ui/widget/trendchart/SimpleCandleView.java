package com.zqw.mobile.grainfull.mvp.ui.widget.trendchart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.zqw.mobile.grainfull.app.utils.DrawViewUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Michael by 61642
 * @date 2023/3/22 14:42
 * @Description 一个简单的蜡烛图
 */
public class SimpleCandleView<T> extends View {
    private final boolean DEBUG = false;

    /**
     * 宽高
     */
    private int height, width;

    /**
     * 有效绘制区域
     */
    private RectF vailRect = new RectF();
    /**
     * 有效绘制预留区域
     */
    private int[] vailReservedArea = {5/*顶部*/, 5/*底部*/};

    /**
     * y轴文字区域
     */
    private RectF yAxisRect = new RectF();
    /**
     * y轴预留区域
     */
    private int yAxisReservedArea = 5/*顶部*/;
    /**
     * x轴左侧预留区域
     */
    private int xAxisLeftReservedArea = 10;
    /**
     * x轴右侧预留区域
     */
    private int xAxisRightReservedArea = 0;
    /**
     * y轴模型
     */
    private SimpleCandleViewCoordinateAxisBean<T> yAxisBean;
    /**
     * x轴文字区域
     */
    private RectF xAxisRect = new RectF();
    /**
     * x轴模型
     */
    private SimpleCandleViewCoordinateAxisBean<T> xAxisBean;
    /**
     * 内容轴模型
     */
    private SimpleCandleViewCoordinateAxisBean<T> contentAxisBean;
    /**
     * 每个柱状条宽度
     */
    private float eachColumnWidth = ConvertUtils.dp2px(13);


    /////////////////////////////////////////
    private OnSimpleCandleViewCallBack<T> onSimpleCandleViewCallBack;

    public void setOnSimpleCandleViewCallBack(OnSimpleCandleViewCallBack<T> onSimpleCandleViewCallBack) {
        this.onSimpleCandleViewCallBack = onSimpleCandleViewCallBack;
    }

    private ValueAnimator valueAnimator;
    private Paint debugPaint = new Paint();
    private Paint paint = new Paint();

    {
        debugPaint.setAntiAlias(true);
        debugPaint.setStrokeWidth(1);
    }

    RectF rectF = new RectF();
    /////////////////////////////////////////

    public SimpleCandleView(Context context) {
        this(context, null);
    }

    public SimpleCandleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleCandleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (height == 0) {
            height = ConvertUtils.dp2px(150) + getPaddingTop() + getPaddingBottom();

        }
        setMeasuredDimension(width, height);
        vailRect.left = getPaddingStart() | getPaddingLeft();
        vailRect.top = getPaddingTop() + ConvertUtils.dp2px(vailReservedArea[0]);
        vailRect.right = width - (getPaddingRight() | getPaddingEnd());
        vailRect.bottom = height - getPaddingBottom() - ConvertUtils.dp2px(vailReservedArea[1]);


        if (xAxisBean != null && yAxisBean != null) {
            yAxisRect.right = yAxisBean.getMaxTextSize(true);

            xAxisRect.left = yAxisRect.right + ConvertUtils.dp2px(xAxisLeftReservedArea);

            yAxisRect.left = vailRect.left;
            yAxisRect.top = vailRect.top + ConvertUtils.dp2px(yAxisReservedArea);
            yAxisRect.bottom = xAxisRect.top;

            xAxisRect.right = vailRect.right - ConvertUtils.dp2px(xAxisRightReservedArea);
            xAxisRect.bottom = vailRect.bottom;
            xAxisRect.top = xAxisRect.bottom - xAxisBean.getMaxTextSize(false);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < contentAxisBean.texts.size(); i++) {
                    contentAxisBean.getRectF(i).touch = false;
                }
                return true;
            case MotionEvent.ACTION_UP:
                for (int i = 0; i < contentAxisBean.texts.size(); i++) {
                    if (contentAxisBean.getRectF(i).contains(event.getX(), event.getY())) {
                        contentAxisBean.getRectF(i).touch = !contentAxisBean.getRectF(i).touch;
                        if (onSimpleCandleViewCallBack != null) {
                            onSimpleCandleViewCallBack.onItemClick(contentAxisBean.getOnXyAxisTextRealization(i).getBean());
                        }
                        break;
                    }
                }
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0xffffffff);
        if (DEBUG) {
            //辅助绘制相关
            debugPaint.setStyle(Paint.Style.STROKE);
            debugPaint.setColor(0xff000000);
            //view有效区域
            canvas.drawRect(vailRect, debugPaint);
            if (yAxisBean != null) {
                debugPaint.setStyle(Paint.Style.FILL);
                debugPaint.setColor(0xffff0000);
                //y轴有效区域
                canvas.drawRect(yAxisRect, debugPaint);
            }
            if (xAxisBean != null) {
                debugPaint.setStyle(Paint.Style.FILL);
                debugPaint.setColor(0xff00ff00);
                //x轴有效区域
                canvas.drawRect(xAxisRect, debugPaint);
            }
        }
        if (yAxisBean != null && xAxisBean != null) {
            /*******************************************************Y轴区域绘制开始↓*******************************************************/
            //y轴延长线
            float extendedLine = ConvertUtils.dp2px(5);
            //绘制y轴线
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(ConvertUtils.dp2px(1));
            paint.setColor(0xffcccccc);
            canvas.drawLine(yAxisRect.right, yAxisRect.top - extendedLine, yAxisRect.right, xAxisRect.top, paint);
            //y轴绘制区域均分
            float eachBetweenHeight = yAxisRect.height() / (yAxisBean.texts.size() - 1);
            for (int i = 0; i < yAxisBean.texts.size(); i++) {
                float yPosition;
                paint.setStrokeWidth(ConvertUtils.dp2px(1));
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(0xffcccccc);
                //Y轴文字偏移量，第0个贴底，其余的跟Y轴均分线对其
                float yAxisTextOffset;
                if (i == 0) {
                    yPosition = yAxisRect.bottom;
                    yAxisTextOffset = 0;
                } else {
                    yPosition = yAxisRect.bottom - eachBetweenHeight * i;
                    yAxisTextOffset = yAxisBean.getCurrentTextSize(i, false) / 2 - extendedLine;
                }

                //绘制y轴等分背景线
                paint.setStrokeWidth(ConvertUtils.dp2px(1));
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(0xff999999);
                canvas.drawLine(yAxisRect.right, yPosition, vailRect.right, yPosition, paint);
                //绘制y轴文字
                canvas.drawText(yAxisBean.getOnXyAxisTextRealization(i).getText(), yAxisRect.left + yAxisBean.getMaxTextSize(true) / 2, yPosition + yAxisTextOffset, yAxisBean.getPaint());
            }
            /*******************************************************Y轴区域绘制结束↑*******************************************************/

            /*******************************************************X轴区域绘制开始↓*******************************************************/

            //每个柱状条之间的间距（含X轴字符区）
            float xAxisBetweenDistance = 0;
            if (eachColumnWidth == 0) {
                xAxisBetweenDistance = ConvertUtils.dp2px(14);
                //每个柱状条之间的间距 = (X轴有效绘制区域-每个柱状条之间的间距*(每个柱状条宽度x柱状条数量-1)-x轴左右预留区域)/柱状条数量
                eachColumnWidth = (xAxisRect.width() - xAxisBetweenDistance * (xAxisBean.texts.size() - 1) - ConvertUtils.dp2px(xAxisLeftReservedArea) - ConvertUtils.dp2px(xAxisRightReservedArea)) / xAxisBean.texts.size();
            } else {
                //每个柱状条之间的间距 = (X轴有效绘制区域-x轴左右预留区域-(每个柱状条宽度x柱状条数量-1)x每个柱状条宽度)/柱状条数量
                xAxisBetweenDistance = (xAxisRect.width() - ConvertUtils.dp2px(xAxisLeftReservedArea) - ConvertUtils.dp2px(xAxisRightReservedArea) - (xAxisBean.texts.size() - 1) * eachColumnWidth) / xAxisBean.texts.size();
            }
            for (int i = 0; i < xAxisBean.texts.size(); i++) {
                if (i == 0) {
                    xAxisBean.getRectF(i).left = xAxisRect.left;
                } else {
                    xAxisBean.getRectF(i).left = xAxisBean.getRectF(i - 1).right + xAxisBetweenDistance;
                }
                xAxisBean.getRectF(i).right = xAxisBean.getRectF(i).left + eachColumnWidth;
                xAxisBean.getRectF(i).top = xAxisRect.top;
                xAxisBean.getRectF(i).bottom = xAxisRect.bottom;
                //绘制x轴文字
                canvas.drawText(xAxisBean.getOnXyAxisTextRealization(i).getText(),
                        xAxisBean.getRectF(i).left + xAxisBean.getRectF(i).width() / 2,
                        xAxisBean.getRectF(i).top + xAxisBean.getRectF(i).height() / 2 + xAxisBean.getMaxTextSize(false) / 2 - ConvertUtils.dp2px(3)/*Y轴偏移量*/,
                        yAxisBean.getPaint());
            }
            /*******************************************************X轴区域绘制结束↑*******************************************************/

            /*******************************************************内容轴区域绘制开始↓*******************************************************/
            if (contentAxisBean != null) {
                //实际坐标轴区域高度除以最大值与最小值的差值可得出该差值的一个值所占用有效绘制区域的百分比
                float eachCoordinatePercent = yAxisBean.max == 0 ? 0 : yAxisRect.height() / (yAxisBean.max - yAxisBean.min);
                if (contentAxisBean.texts.size() == xAxisBean.texts.size()) {
                    for (int ii = 0; ii < contentAxisBean.texts.size(); ii++) {
                        int i = (int) (ii * columnHeightPercent);
                        if (i == 0) {
                            contentAxisBean.getRectF(i).left = xAxisRect.left;
                        } else {
                            contentAxisBean.getRectF(i).left = contentAxisBean.getRectF(i - 1).right + xAxisBetweenDistance;
                        }
                        contentAxisBean.getRectF(i).right = contentAxisBean.getRectF(i).left + eachColumnWidth;

                        contentAxisBean.getRectF(i).topHigh = xAxisRect.top - contentAxisBean.getOnXyAxisTextRealization(i).getTopLevelHigh() * eachCoordinatePercent;
                        contentAxisBean.getRectF(i).top = xAxisRect.top - contentAxisBean.getOnXyAxisTextRealization(i).getTopLevelLow() * eachCoordinatePercent;
                        contentAxisBean.getRectF(i).bottom = xAxisRect.top - contentAxisBean.getOnXyAxisTextRealization(i).getBottomLevelHigh() * eachCoordinatePercent;
                        contentAxisBean.getRectF(i).bottomLow = xAxisRect.top - contentAxisBean.getOnXyAxisTextRealization(i).getBottomLevelLow() * eachCoordinatePercent;

                        //空心柱Y轴中点
                        float center = contentAxisBean.getRectF(i).top + contentAxisBean.getRectF(i).height() / 2;

                        rectF.left = contentAxisBean.getRectF(i).left;
                        rectF.top = center - contentAxisBean.getRectF(i).height() / 2 * columnHeightPercent;
                        rectF.right = contentAxisBean.getRectF(i).right;
                        rectF.bottom = center + contentAxisBean.getRectF(i).height() / 2 * columnHeightPercent;


                        if (contentAxisBean.getRectF(i).touch) {
                            paint.setStyle(Paint.Style.FILL);
                            paint.setStrokeWidth(ConvertUtils.dp2px(3));
                        } else {
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(ConvertUtils.dp2px(1.5f));
                        }
                        paint.setColor(0xffff951b);
                        //绘制空心轴
                        canvas.drawRoundRect(rectF, ConvertUtils.dp2px(2), ConvertUtils.dp2px(2), paint);

                        float x = contentAxisBean.getRectF(i).left + contentAxisBean.getRectF(i).width() / 2;
                        //绘制空心轴延长线上半部分
                        canvas.drawLine(x, rectF.top, x, rectF.top - contentAxisBean.getRectF(i).topHeight() * columnHeightPercent, paint);
                        //绘制空心轴延长线下半部分
                        canvas.drawLine(x, rectF.bottom, x, rectF.bottom + contentAxisBean.getRectF(i).bottomHeight() * columnHeightPercent, paint);
                    }
                }
            }
            /*******************************************************内容轴区域绘制结束↑*******************************************************/
        }
    }


    public void setData(SimpleCandleViewCoordinateAxisBean<T> yAxisBean, SimpleCandleViewCoordinateAxisBean<T> xAxisBean, SimpleCandleViewCoordinateAxisBean<T> contentAxisBean) {
        this.yAxisBean = yAxisBean;
        this.xAxisBean = xAxisBean;
        this.contentAxisBean = contentAxisBean;
        start();
    }


    public void setEachColumnWidth(float eachColumnWidth) {
        this.eachColumnWidth = eachColumnWidth;
        start();
    }

    private float columnHeightPercent = 0f;
    private boolean line;

    private void start() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(0f, 1f);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    line = true;
                    invalidate();
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    line = false;
                }
            });
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    columnHeightPercent = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
        }
        valueAnimator.cancel();
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setDuration(1000);
        valueAnimator.start();
    }


    public static class SimpleCandleViewCoordinateAxisBean<T> {
        /**
         * 最大最小值
         */
        private int max, min;

        private List<TextBean<T>> texts = new ArrayList<>();
        private float textSize = ConvertUtils.sp2px(10f);
        private int color = 0xff999999;
        private Paint paint = new Paint();

        {
            paint.setTextSize(textSize);
            paint.setColor(color);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
        }

        public Paint getPaint() {
            paint.setTextAlign(Paint.Align.CENTER);
            return paint;
        }

        /**
         * 获取矩阵
         */
        public TextBean.AxisRectF getRectF(int position) {
            return texts.get(position).rectF;
        }

        /**
         * 获取文本实现者
         */
        public OnSimpleCandleViewXyAxisTextRealization<T> getOnXyAxisTextRealization(int position) {
            return texts.get(position).xyText;
        }

        /**
         * 获取指定文本最大宽高尺寸
         */
        public float getCurrentTextSize(int i, boolean width) {
            float size = 0;
            if (texts.size() > 0 && i >= 0 && i < texts.size()) {
                size = Math.max(size, width ? texts.get(i).size[0] : texts.get(i).size[1]);
            }
            return size;
        }

        /**
         * 获取文本最大宽高尺寸
         */
        public float getMaxTextSize(boolean width) {
            float size = 0;
            for (int i = 0; i < texts.size(); i++) {
                size = Math.max(size, getCurrentTextSize(i, width));
            }
            return size;
        }

        /**
         * 设置坐标轴文本数据
         */
        public void setCoordinateAxisTextData(List<OnSimpleCandleViewXyAxisTextRealization<T>> textData, boolean reverse) {
            if (reverse) {
                Collections.reverse(textData);
            }
            for (int i = 0; i < textData.size(); i++) {
                if (i == 0) {
                    max = textData.get(i).getTopLevelHigh();
                    min = textData.get(i).getBottomLevelLow();
                } else {
                    max = Math.max(max, textData.get(i).getTopLevelHigh());
                    min = Math.min(min, textData.get(i).getBottomLevelLow());
                }
                TextBean<T> textBean = new TextBean<>();
                textBean.xyText = textData.get(i);
                textBean.size = DrawViewUtils.getTextWHF(getPaint(), textBean.xyText.getText());
                texts.add(textBean);
            }
        }

        public void setTextSize(float textSize) {
            this.textSize = textSize;
            paint.setTextSize(textSize);
        }

        public void setColor(int color) {
            this.color = color;
            paint.setColor(color);
        }

        /**
         * 文本
         */
        static class TextBean<T> {
            /**
             * 矩阵
             */
            AxisRectF rectF = new AxisRectF();
            /**
             * 文本尺寸
             */
            private float[] size;
            /**
             * xy轴文本实现者
             */
            OnSimpleCandleViewXyAxisTextRealization<T> xyText;

            /**
             * 点位
             */
            static final class AxisRectF {
                private boolean touch;
                /**
                 * 左边
                 */
                public float left;
                /**
                 * 顶边高点
                 */
                public float topHigh;
                /**
                 * 顶边低点
                 */
                public float top;
                /**
                 * 右边
                 */
                public float right;
                /**
                 * 底边高点
                 */
                public float bottom;
                /**
                 * 底边低点
                 */
                public float bottomLow;

                public final float width() {
                    return right - left;
                }

                public final float height() {
                    return bottom - top;
                }

                public final float topHeight() {
                    return top - topHigh;
                }

                public final float bottomHeight() {
                    return bottomLow - bottom;
                }

                public boolean contains(float x, float y) {
                    return left < right && top < bottom && x >= left && x < right && y >= top && y < bottom;
                }
            }
        }
    }


    /**
     * xy轴文本实现者
     */
    public interface OnSimpleCandleViewXyAxisTextRealization<T> extends OnTextRealization<T> {

        int getTopLevelHigh();

        int getTopLevelLow();

        int getBottomLevelHigh();

        int getBottomLevelLow();

    }

    public interface OnSimpleCandleViewCallBack<T> {
        void onItemClick(T t);
    }


}
