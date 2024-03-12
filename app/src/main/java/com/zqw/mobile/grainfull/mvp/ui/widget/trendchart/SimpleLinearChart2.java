package com.zqw.mobile.grainfull.mvp.ui.widget.trendchart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
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
 * @author Michael by Administrator
 * @date 2021/1/29 9:47
 * @Description 一个简单的线性图表
 */
@SuppressWarnings("ALL")
public class SimpleLinearChart2<T> extends View {
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
    private int xAxisLeftReservedArea = 0;
    /**
     * x轴右侧预留区域
     */
    private int xAxisRightReservedArea = 0;
    /**
     * y轴模型
     */
    private SimpleLinearChart2CoordinateAxisBean<T> yAxisBean;
    /**
     * x轴文字区域
     */
    private RectF xAxisRect = new RectF();
    /**
     * x轴模型
     */
    private SimpleLinearChart2CoordinateAxisBean<T> xAxisBean;
    /**
     * 内容轴模型
     */
    private List<SimpleLinearChart2CoordinateAxisBean<T>> contentAxisBean;
    /**
     * 每个柱状条宽度
     */
    private float eachColumnWidth = ConvertUtils.dp2px(13);
//    /**
//     * 标注区域
//     */
//    private RectF remarkRect = new RectF();
//    /**
//     * 标注区域高度
//     */
//    private int remarkArea = 48;

    /**
     * 第一个点是否贴边从Y轴开始绘制
     */
    private boolean isClingYAxisForFirst = true;


    /////////////////////////////////////////

    private ValueAnimator valueAnimator;
    private Paint debugPaint = new Paint();
    private Paint paint = new Paint();


    {
        debugPaint.setAntiAlias(true);
        debugPaint.setStrokeWidth(1);
    }


    RectF rectF = new RectF();
    /////////////////////////////////////////

    public SimpleLinearChart2(Context context) {
        this(context, null);
    }

    public SimpleLinearChart2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleLinearChart2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (height == 0) {
            height = ConvertUtils.dp2px(200) + getPaddingTop() + getPaddingBottom();

        }
        setMeasuredDimension(width, height);

        vailRect.left = getPaddingStart() | getPaddingLeft();
        vailRect.top = getPaddingTop() + ConvertUtils.dp2px(vailReservedArea[0]);
        vailRect.right = width - (getPaddingRight() | getPaddingEnd());
        vailRect.bottom = height - getPaddingBottom() - ConvertUtils.dp2px(vailReservedArea[1]);

//        remarkRect.left = vailRect.left;
//        remarkRect.bottom = vailRect.bottom;
//        remarkRect.top = remarkRect.bottom - DensityUtil.dp2px(remarkArea);
//        remarkRect.right = vailRect.right;


        if (xAxisBean != null && yAxisBean != null) {

            yAxisRect.right = yAxisBean.getMaxTextSize(true);

            xAxisRect.left = yAxisRect.right + ConvertUtils.dp2px(xAxisLeftReservedArea);
            xAxisRect.right = vailRect.right - ConvertUtils.dp2px(xAxisRightReservedArea);
//            xAxisRect.bottom = remarkRect.top;
            xAxisRect.bottom = vailRect.bottom;
            xAxisRect.top = xAxisRect.bottom - xAxisBean.getMaxTextSize(false);

            yAxisRect.left = vailRect.left;
            yAxisRect.top = vailRect.top + ConvertUtils.dp2px(yAxisReservedArea);
            yAxisRect.bottom = xAxisRect.top;

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
            debugPaint.setStyle(Paint.Style.FILL);
            debugPaint.setColor(0xaaff80f0);
            //remark轴有效区域
//            canvas.drawRect(remarkRect, debugPaint);
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
                //遮罩
                for (SimpleLinearChart2CoordinateAxisBean<T> contentAxisBean : contentAxisBean) {
                    float maxHeight = 0;
                    for (int i = 0; i < contentAxisBean.texts.size(); i++) {
                        if (i == 0) {
                            contentAxisBean.getRectF(i).left = xAxisRect.left;
                        } else {
                            contentAxisBean.getRectF(i).left = contentAxisBean.getRectF(i - 1).right + xAxisBetweenDistance;
                        }
                        contentAxisBean.getRectF(i).right = contentAxisBean.getRectF(i).left + eachColumnWidth;
                        contentAxisBean.getRectF(i).top = xAxisRect.top - contentAxisBean.getOnXyAxisTextRealization(i).getNumber() * eachCoordinatePercent * value;
                        contentAxisBean.getRectF(i).bottom = xAxisRect.top;
                        if (isClingYAxisForFirst) {
                            if (i == contentAxisBean.texts.size() - 1) {
                                contentAxisBean.bezierPoints.get(i).set(contentAxisBean.getRectF(i).left + contentAxisBean.getRectF(i).width() / 2, contentAxisBean.getRectF(i).top);
                            } else {
                                contentAxisBean.bezierPoints.get(i).set(contentAxisBean.getRectF(i).left, contentAxisBean.getRectF(i).top);
                            }
                        } else {
                            contentAxisBean.bezierPoints.get(i).set(contentAxisBean.getRectF(i).left + contentAxisBean.getRectF(i).width() / 2, contentAxisBean.getRectF(i).top);
                        }
                        if (DEBUG) {
                            //辅助绘制相关
                            paint.setStrokeWidth(ConvertUtils.dp2px(1f));
                            debugPaint.setStyle(Paint.Style.STROKE);
                            debugPaint.setColor(0xff000000);
                            canvas.drawRect(contentAxisBean.getRectF(i), debugPaint);
                        }

                        maxHeight = Math.max(maxHeight, contentAxisBean.getRectF(i).height());
                    }
                    if (contentAxisBean.checked) {
                        contentAxisBean.path.reset();
                        if (isClingYAxisForFirst) {
                            contentAxisBean.path.moveTo(xAxisRect.left, xAxisRect.top);
                            contentAxisBean.path.lineTo(xAxisRect.left, contentAxisBean.bezierPoints.get(0).y);
                            contentAxisBean.path.lineTo(contentAxisBean.bezierPoints.get(0).x, contentAxisBean.bezierPoints.get(0).y);
                        } else {
                            contentAxisBean.path.moveTo(xAxisRect.left + contentAxisBean.getRectF(0).width() / 2, xAxisRect.top);
                            contentAxisBean.path.lineTo(xAxisRect.left + contentAxisBean.getRectF(0).width() / 2, contentAxisBean.bezierPoints.get(0).y);
                            contentAxisBean.path.lineTo(contentAxisBean.bezierPoints.get(0).x, contentAxisBean.bezierPoints.get(0).y);
                        }
                        DrawViewUtils.calculateBezier3(contentAxisBean.bezierPoints, 1f, contentAxisBean.path);
                        contentAxisBean.path.lineTo(contentAxisBean.bezierPoints.get(contentAxisBean.bezierPoints.size() - 1).x, xAxisRect.top);
                        contentAxisBean.path.lineTo(xAxisRect.left + contentAxisBean.getRectF(0).width() / 2, xAxisRect.top);
                        contentAxisBean.path.close();

                        contentAxisBean.initMaskPaint(
                                xAxisRect.right,
                                xAxisRect.top - maxHeight,
                                xAxisRect.right,
                                xAxisRect.top);
                        canvas.drawPath(contentAxisBean.path, contentAxisBean.maskPaint);
                    }
                }

                //曲线轨迹
                for (SimpleLinearChart2CoordinateAxisBean<T> contentAxisBean : contentAxisBean) {
                    //三阶贝塞尔需3个点才是完美的轨迹,两个点绘制出的遮罩不美观，故此处限制大于2
                    if (contentAxisBean.bezierPoints.size() > 0) {
                        contentAxisBean.path.reset();
                        contentAxisBean.path.moveTo(contentAxisBean.bezierPoints.get(0).x, contentAxisBean.bezierPoints.get(0).y);
                        DrawViewUtils.calculateBezier3(contentAxisBean.bezierPoints, 1f, contentAxisBean.path);
                        contentAxisBean.path.moveTo(contentAxisBean.bezierPoints.get(0).x, contentAxisBean.bezierPoints.get(0).y);
                        contentAxisBean.path.close();
                        contentAxisBean.paint.setColor(contentAxisBean.checked ? 0xff5f8fff : 0xffdfe9ff);
                        contentAxisBean.paint.setStyle(Paint.Style.STROKE);
                        contentAxisBean.paint.setStrokeWidth(ConvertUtils.dp2px(2f));
                        canvas.drawPath(contentAxisBean.path, contentAxisBean.paint);

                    }
                }

            }
            /*******************************************************内容轴区域绘制结束↑*******************************************************/

            /*******************************************************注释轴区域绘制结束↑*******************************************************/
            for (SimpleLinearChart2CoordinateAxisBean<T> contentAxisBean : contentAxisBean) {

            }
            /*******************************************************注释区域绘制结束↓*******************************************************/
        }
    }


    public void setData(SimpleLinearChart2CoordinateAxisBean<T> yAxisBean, SimpleLinearChart2CoordinateAxisBean<T> xAxisBean, List<SimpleLinearChart2CoordinateAxisBean<T>> contentAxisBean) {
        this.yAxisBean = yAxisBean;
        this.xAxisBean = xAxisBean;
        this.contentAxisBean = contentAxisBean;
        start();
    }


    public void setEachColumnWidth(float eachColumnWidth) {
        this.eachColumnWidth = eachColumnWidth;
        start();
    }

    public void setCheckedLine(int position) {
        if (position > 0 && position < contentAxisBean.size()) {
            for (int i = 0; i < contentAxisBean.size(); i++) {
                contentAxisBean.get(i).setChecked(false);
            }
            contentAxisBean.get(position).setChecked(true);
            invalidate();
        }
    }

    private float value = 0f;

    private void start() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(0f, 1f);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    value = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
        }
        valueAnimator.cancel();
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setDuration(1000);
        valueAnimator.start();
    }


    public static class SimpleLinearChart2CoordinateAxisBean<T> {
        /**
         * 最大最小值
         */
        private int max, min;
        /**
         * 计算后的贝塞尔坐标集
         */
        private List<PointF> bezierPoints = new ArrayList<>();

        private String remark;
        private boolean checked;

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }


        private List<TextBean<T>> texts = new ArrayList<>();
        private float textSize = ConvertUtils.sp2px(10f);
        private int color = 0xff999999;
        private Paint paint = new Paint();
        private Paint maskPaint = new Paint();
        private Path path = new Path();

        {
            paint.setTextSize(textSize);
            paint.setColor(color);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            maskPaint.setAntiAlias(true);
        }


        public Paint getPaint() {
            paint.setTextAlign(Paint.Align.CENTER);
            return paint;
        }

        public void initMaskPaint(float startX, float startY, float endX, float endY) {
            maskPaint.setShader(
                    new LinearGradient(startX, startY, endX, endY,
                            new int[]{Color.parseColor("#dbe4fd"), Color.parseColor("#e9effe"), Color.parseColor("#f6f8fe")},
                            new float[]{0.0f, 0.5f, 1.0f}, Shader.TileMode.CLAMP)
            );
        }

        /**
         * 获取矩阵
         */
        public RectF getRectF(int position) {
            return texts.get(position).rectF;
        }

        /**
         * 获取文本实现者
         */
        public OnSimpleLinearChart2XyAxisTextRealization<T> getOnXyAxisTextRealization(int position) {
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
        public void setCoordinateAxisTextData(List<OnSimpleLinearChart2XyAxisTextRealization<T>> textData, boolean reverse) {
            if (reverse) {
                Collections.reverse(textData);
            }
            bezierPoints.clear();
            for (int i = 0; i < textData.size(); i++) {
                if (i == 0) {
                    max = textData.get(i).getNumber();
                    min = textData.get(i).getNumber();
                } else {
                    max = Math.max(max, textData.get(i).getNumber());
                    min = Math.min(min, textData.get(i).getNumber());
                }
                TextBean<T> textBean = new TextBean<>();
                textBean.xyText = textData.get(i);
                textBean.size = DrawViewUtils.getTextWHF(getPaint(), textBean.xyText.getText());
                texts.add(textBean);
                bezierPoints.add(new PointF());
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
            RectF rectF = new RectF();
            /**
             * 文本尺寸
             */
            private float[] size;
            /**
             * xy轴文本实现者
             */
            OnSimpleLinearChart2XyAxisTextRealization<T> xyText;
        }
    }


    /**
     * xy轴文本实现者
     */
    public interface OnSimpleLinearChart2XyAxisTextRealization<T> extends OnTextRealization<T> {

        String getRemark();

        int getNumber();

    }

}
