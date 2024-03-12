package com.zqw.mobile.grainfull.mvp.ui.widget.trendchart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
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
 * @date 2023/3/23 16:51
 * @Description 一个简单多层叠柱状图
 */
public class SimpleMultipleColumnView<T> extends View {
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
    private SimpleMultipleColumnViewCoordinateAxisBean<T> yAxisBean;
    /**
     * x轴文字区域
     */
    private RectF xAxisRect = new RectF();
    /**
     * x轴模型
     */
    private SimpleMultipleColumnViewCoordinateAxisBean<T> xAxisBean;
    /**
     * 内容轴模型
     */
    private SimpleMultipleColumnViewCoordinateAxisBean<T> contentAxisBean;
    /**
     * 每个柱状条宽度
     */
    private float eachColumnWidth = ConvertUtils.dp2px(13);

    /**
     * 每个柱状条之间的间距（含X轴字符区）
     */
    private float xAxisBetweenDistance = 0;

    /////////////////////////////////////////

    private ValueAnimator valueAnimator;
    private Paint debugPaint = new Paint();
    private Paint paint = new Paint();

    {
        debugPaint.setAntiAlias(true);
        debugPaint.setStrokeWidth(1);
    }

    /////////////////////////////////////////

    public SimpleMultipleColumnView(Context context) {
        this(context, null);
    }

    public SimpleMultipleColumnView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleMultipleColumnView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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


            if (eachColumnWidth == 0) {
                xAxisBetweenDistance = ConvertUtils.dp2px(14);
                //每个柱状条之间的间距 = (X轴有效绘制区域-每个柱状条之间的间距*(每个柱状条宽度x柱状条数量-1)-x轴左右预留区域)/柱状条数量
                eachColumnWidth = (xAxisRect.width() - xAxisBetweenDistance * (xAxisBean.texts.size() - 1) - ConvertUtils.dp2px(xAxisLeftReservedArea) - ConvertUtils.dp2px(xAxisRightReservedArea)) / xAxisBean.texts.size();
            } else {
                //每个柱状条之间的间距 = (X轴有效绘制区域-x轴左右预留区域-(每个柱状条宽度x柱状条数量-1)x每个柱状条宽度)/柱状条数量
                xAxisBetweenDistance = (xAxisRect.width() - ConvertUtils.dp2px(xAxisLeftReservedArea) - ConvertUtils.dp2px(xAxisRightReservedArea) - (xAxisBean.texts.size() - 1) * eachColumnWidth) / xAxisBean.texts.size();
            }
            if (contentAxisBean.texts.size() == xAxisBean.texts.size()) {
                for (int i = 0; i < contentAxisBean.texts.size(); i++) {
                    if (i == 0) {
                        contentAxisBean.getRectF(i).left = xAxisRect.left;
                    } else {
                        contentAxisBean.getRectF(i).left = contentAxisBean.getRectF(i - 1).right + xAxisBetweenDistance;
                    }
                    contentAxisBean.getRectF(i).right = contentAxisBean.getRectF(i).left + eachColumnWidth;

                    contentAxisBean.getRectF(i).bottom = yAxisRect.bottom;
                    contentAxisBean.getRectF(i).top = yAxisRect.bottom - yAxisRect.height();

                }


            }
        }
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
            if (contentAxisBean != null) {
                //辅助绘制相关
                paint.setStrokeWidth(ConvertUtils.dp2px(1f));
                debugPaint.setStyle(Paint.Style.STROKE);
                debugPaint.setColor(0xff000000);
                for (int i = 0; i < contentAxisBean.texts.size(); i++) {
                    canvas.drawRect(contentAxisBean.getRectF(i), debugPaint);
                }
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
                if (i == 0) {
                    canvas.drawLine(yAxisRect.right, yPosition, vailRect.right, yPosition, paint);
                }
                //绘制y轴文字
                canvas.drawText(yAxisBean.getOnXyAxisTextRealization(i).getText(), yAxisRect.left + yAxisBean.getMaxTextSize(true) / 2, yPosition + yAxisTextOffset, yAxisBean.getPaint());
            }
            /*******************************************************Y轴区域绘制结束↑*******************************************************/

            /*******************************************************X轴区域绘制开始↓*******************************************************/


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

                for (int i = 0; i < contentAxisBean.texts.size(); i++) {
                    float height = contentAxisBean.getRectF(i).height() * columnHeightPercent;


                    if (contentAxisBean.texts.get(i).getTotal() == 0) {
                        paint.setStrokeWidth(ConvertUtils.dp2px(1));
                        paint.setStyle(Paint.Style.FILL);
                        paint.setColor(0xfff4f4f4);
                        canvas.drawRect(contentAxisBean.texts.get(i).bgRectF, paint);
                    } else {

                        contentAxisBean.texts.get(i).getLevel1().rectF.left = contentAxisBean.getRectF(i).left;
                        contentAxisBean.texts.get(i).getLevel1().rectF.right = contentAxisBean.getRectF(i).right;
                        contentAxisBean.texts.get(i).getLevel1().rectF.bottom = contentAxisBean.getRectF(i).bottom;
                        float level1 = contentAxisBean.getOnXyAxisTextRealization(i).getLevel1().value * height;
                        contentAxisBean.texts.get(i).getLevel1().rectF.top = contentAxisBean.getRectF(i).bottom - level1;

                        contentAxisBean.texts.get(i).getLevel2().rectF.left = contentAxisBean.getRectF(i).left;
                        contentAxisBean.texts.get(i).getLevel2().rectF.right = contentAxisBean.getRectF(i).right;
                        contentAxisBean.texts.get(i).getLevel2().rectF.bottom = contentAxisBean.texts.get(i).getLevel1().rectF.top;
                        int level2 = (int) (contentAxisBean.getOnXyAxisTextRealization(i).getLevel2().value * height);
                        contentAxisBean.texts.get(i).getLevel2().rectF.top = contentAxisBean.texts.get(i).getLevel2().rectF.bottom - level2;

                        contentAxisBean.texts.get(i).getLevel3().rectF.left = contentAxisBean.getRectF(i).left;
                        contentAxisBean.texts.get(i).getLevel3().rectF.right = contentAxisBean.getRectF(i).right;
                        contentAxisBean.texts.get(i).getLevel3().rectF.bottom = contentAxisBean.texts.get(i).getLevel2().rectF.top;
                        float level3 = contentAxisBean.getOnXyAxisTextRealization(i).getLevel3().value * height;
                        contentAxisBean.texts.get(i).getLevel3().rectF.top = contentAxisBean.texts.get(i).getLevel3().rectF.bottom - level3;

                        contentAxisBean.texts.get(i).getLevel4().rectF.left = contentAxisBean.getRectF(i).left;
                        contentAxisBean.texts.get(i).getLevel4().rectF.right = contentAxisBean.getRectF(i).right;
                        contentAxisBean.texts.get(i).getLevel4().rectF.bottom = contentAxisBean.texts.get(i).getLevel3().rectF.top;
                        float level4 = contentAxisBean.getOnXyAxisTextRealization(i).getLevel4().value * height;
                        contentAxisBean.texts.get(i).getLevel4().rectF.top = contentAxisBean.texts.get(i).getLevel4().rectF.bottom - level4;

                        contentAxisBean.texts.get(i).getLevel5().rectF.left = contentAxisBean.getRectF(i).left;
                        contentAxisBean.texts.get(i).getLevel5().rectF.right = contentAxisBean.getRectF(i).right;
                        contentAxisBean.texts.get(i).getLevel5().rectF.bottom = contentAxisBean.texts.get(i).getLevel4().rectF.top;
                        float level5 = contentAxisBean.getOnXyAxisTextRealization(i).getLevel5().value * height;

                        contentAxisBean.texts.get(i).getLevel5().rectF.top = contentAxisBean.texts.get(i).getLevel5().rectF.bottom - level5;


                        paint.setStrokeWidth(ConvertUtils.dp2px(1));
                        paint.setStyle(Paint.Style.FILL);
                        paint.setColor(0xffffb968);
                        canvas.drawRect(contentAxisBean.texts.get(i).getLevel1().rectF, paint);
                        paint.setColor(0xff44d7b9);
                        canvas.drawRect(contentAxisBean.texts.get(i).getLevel2().rectF, paint);
                        paint.setColor(0xff5f8fff);
                        canvas.drawRect(contentAxisBean.texts.get(i).getLevel3().rectF, paint);
                        paint.setColor(0xfffee25e);
                        canvas.drawRect(contentAxisBean.texts.get(i).getLevel4().rectF, paint);
                        paint.setColor(0xffffa092);
                        canvas.drawRect(contentAxisBean.texts.get(i).getLevel5().rectF, paint);
                    }
                }


            }
            /*******************************************************内容轴区域绘制结束↑*******************************************************/
        }
    }


    public void setData(SimpleMultipleColumnViewCoordinateAxisBean<T> yAxisBean, SimpleMultipleColumnViewCoordinateAxisBean<T> xAxisBean, SimpleMultipleColumnViewCoordinateAxisBean<T> contentAxisBean) {
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

    private void start() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(0f, 1f);
            valueAnimator.setInterpolator(new LinearInterpolator());
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


    public static class SimpleMultipleColumnViewCoordinateAxisBean<T> {
        /**
         * 最大最小值
         */
        private float max, min;

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
        public RectF getRectF(int position) {
            return texts.get(position).bgRectF;
        }

        /**
         * 获取文本实现者
         */
        public OnSimpleMultipleColumnViewXyAxisTextRealization<T> getOnXyAxisTextRealization(int position) {
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
        public void setCoordinateAxisTextData(List<OnSimpleMultipleColumnViewXyAxisTextRealization<T>> textData, boolean reverse) {
            if (reverse) {
                Collections.reverse(textData);
            }
            max = textData.get(0).getLevel1().value;
            min = max;
            for (int i = 0; i < textData.size(); i++) {
                max = Math.max(textData.get(i).getLevel1().value, max);
                min = Math.min(textData.get(i).getLevel1().value, min);
                max = Math.max(textData.get(i).getLevel2().value, max);
                min = Math.min(textData.get(i).getLevel2().value, min);
                max = Math.max(textData.get(i).getLevel3().value, max);
                min = Math.min(textData.get(i).getLevel3().value, min);
                max = Math.max(textData.get(i).getLevel4().value, max);
                min = Math.min(textData.get(i).getLevel4().value, min);
                max = Math.max(textData.get(i).getLevel5().value, max);
                min = Math.min(textData.get(i).getLevel5().value, min);
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
        static class TextBean<T> implements OnSimpleMultipleColumnViewXyAxisTextRealization<T> {
            /**
             * 矩阵
             */
            RectF bgRectF = new RectF();
            /**
             * 文本尺寸
             */
            private float[] size;
            /**
             * xy轴文本实现者
             */
            OnSimpleMultipleColumnViewXyAxisTextRealization<T> xyText;

            /**
             * 获取总值
             */
            float getTotal() {
                float total = 0f;
                if (xyText != null) {
                    total += getLevel1().value;
                    total += getLevel2().value;
                    total += getLevel3().value;
                    total += getLevel4().value;
                    total += getLevel5().value;
                }
                return total;
            }


            @Override
            public SimpleMultipleColumnViewChildBean getLevel1() {
                return xyText.getLevel1();
            }

            @Override
            public SimpleMultipleColumnViewChildBean getLevel2() {
                return xyText.getLevel2();
            }

            @Override
            public SimpleMultipleColumnViewChildBean getLevel3() {
                return xyText.getLevel3();
            }

            @Override
            public SimpleMultipleColumnViewChildBean getLevel4() {
                return xyText.getLevel4();
            }

            @Override
            public SimpleMultipleColumnViewChildBean getLevel5() {
                return xyText.getLevel5();
            }

            @Override
            public String getText() {
                //TODO nothing
                return null;
            }

            @Override
            public T getBean() {
                //TODO nothing
                return null;
            }
        }
    }

    public static class SimpleMultipleColumnViewChildBean {
        float value;
        RectF rectF = new RectF();

        public SimpleMultipleColumnViewChildBean(float value) {
            this.value = value;
        }
    }

    public interface OnSimpleMultipleColumnViewXyAxisTextRealization<T> extends OnTextRealization<T> {

        SimpleMultipleColumnViewChildBean getLevel1();

        SimpleMultipleColumnViewChildBean getLevel2();

        SimpleMultipleColumnViewChildBean getLevel3();

        SimpleMultipleColumnViewChildBean getLevel4();

        SimpleMultipleColumnViewChildBean getLevel5();
    }


}
