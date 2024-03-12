package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.blankj.utilcode.util.ConvertUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.DrawViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael by Administrator
 * @date 2021/1/28 9:41
 * @Description 一个简单的五边形图
 */
@SuppressWarnings("ALL")
public class SimplePentagonView extends View {
    /**
     * 动画持续时间
     */
    private static final int ANIMATION_DURATION = 500;
    /**
     * 整体背景
     */
    private LinearGradient fullBack;
    /**
     * 五星前景
     */
    private LinearGradient pentagonForeground;
    /**
     * 宽高比例
     */
    private float whPercent = 0.5f;
    /**
     * 半径比例
     */
    private float radiusPercent = 0.6f;

    /**
     * 文字与图片之间的距离
     */
    private int betweenTextAndImageDistance = ConvertUtils.dp2px(3);
    /**
     * 文字与网格线之间的距离
     */
    private int betweenWebAndTextDistance = ConvertUtils.dp2px(6);

    /**
     * 内部的多边形坐标集（包含最外一层）
     */
    private List<Point> pointList = new ArrayList<>();

    /**
     * 前景多边形坐标集
     */
    private List<Point> foregroundList = new ArrayList<>();
    /**
     * 数据模型
     */
    private List<SimpleSpiderViewBean> data = new ArrayList<>();

    {
        data.add(new SimpleSpiderViewBean(R.mipmap.icon_sx, "升学目标", 0f));
        data.add(new SimpleSpiderViewBean(R.mipmap.icon_xysp, "学业水平", 0f));
        data.add(new SimpleSpiderViewBean(R.mipmap.icon_xljk, "心理健康", 0f));
        data.add(new SimpleSpiderViewBean(R.mipmap.icon_yy, "应用达人", 0f));
        data.add(new SimpleSpiderViewBean(R.mipmap.icon_zwrz, "自我认知", 0f));


    }

    private int total = 888;
    private int number;

    private String title = "生涯学分";

    private String desc = "超过95%的同学";

    /**
     * 中心点
     */
    private Point center = new Point(0, 0);
    /**
     * 两个顶点之间的夹角
     */
    private int angle = 360 / data.size();


    /**
     * 半径
     */
    private int radius;
    /**
     * 小圆点半径
     */
    private static int circleRadius = ConvertUtils.dp2px(3f);
    /**
     * 五星背景色
     */
    private int pentagonbackgroundColor = Color.parseColor("#1AFFFFFF");
    /**
     * 五星骨架色
     */
    private int pentagonSpiderColor = Color.parseColor("#0DFFFFFF");

    /**
     * 进度
     */
    private float progress;

    /**
     * 画笔
     */
    private Paint topPointtPaint = new Paint();
    private Paint backgroundPaint = new Paint();
    private Paint ringPaint = new Paint();
    private Paint pentagonBackgroundPaint = new Paint();
    private Paint paint = new Paint();
    private Path path = new Path();
    private Paint pentagonForegroundPaint = new Paint();
    private Paint spiderPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint textCenterPaint = new Paint();

    {
        topPointtPaint.setStrokeWidth(1);
        topPointtPaint.setAntiAlias(true);
        ringPaint.setStrokeWidth(1);
        ringPaint.setAntiAlias(true);
        textCenterPaint.setStrokeWidth(1);
        textCenterPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(1);
        textPaint.setAntiAlias(true);
        backgroundPaint.setStrokeWidth(1);
        backgroundPaint.setAntiAlias(true);
        pentagonBackgroundPaint.setStrokeWidth(1);
        pentagonBackgroundPaint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        pentagonForegroundPaint.setStrokeWidth(1);
        pentagonForegroundPaint.setAntiAlias(true);
        spiderPaint.setStrokeWidth(3);
        spiderPaint.setAntiAlias(true);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                List<SimpleSpiderViewBean> data = new ArrayList<>();
                data.add(new SimpleSpiderViewBean(R.mipmap.icon_sx, "升学目标", 0.9f));
                data.add(new SimpleSpiderViewBean(R.mipmap.icon_xysp, "学业水平", 0.9f));
                data.add(new SimpleSpiderViewBean(R.mipmap.icon_xljk, "心理健康", 0.8f));
                data.add(new SimpleSpiderViewBean(R.mipmap.icon_yy, "应用达人", 0.6f));
                data.add(new SimpleSpiderViewBean(R.mipmap.icon_zwrz, "自我认知", 1.0f));
                setData(data, true);
            }
        });
    }


    public SimplePentagonView(Context context) {
        this(context, null);
    }

    public SimplePentagonView(Context context, @androidx.annotation.Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimplePentagonView(Context context, @androidx.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator.removeAllListeners();
            valueAnimator.removeAllUpdateListeners();
        }
        if (progressAnimator != null) {
            progressAnimator.cancel();
            progressAnimator.removeAllListeners();
            progressAnimator.removeAllUpdateListeners();
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
            int[] size = data.get(i).getSize(backgroundPaint);
            maxSize = Math.max(size[0], size[1]);
        }
        radius = Math.min(width, height) / 2 - betweenTextAndImageDistance / 2 + betweenWebAndTextDistance / 2 - maxSize;
        radius = (int) (radius * radiusPercent);
        center.set(width / 2, height / 2 + getPaddingTop() - getPaddingBottom());
        pointList.clear();
       List<Point> points = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            pointList.add(DrawViewUtils.calculatePoint(center.x, center.y, radius, getAngleForEach(i)));
        }
        fullBack = new LinearGradient(0f, height, width, 0, new int[]{0xff2c80ff, 0xff0043ff}, new float[]{0, 1.0f}, Shader.TileMode.MIRROR);
        pentagonForeground = new LinearGradient(0f, 0f, width, height, 0xfff0f9f, 0x35ffffff, Shader.TileMode.MIRROR);
        if (ringBitmap == null) {
            ringBitmap = getBitmap(R.mipmap.icon_ring, 0.75f);
        }
    }

    Bitmap ringBitmap;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*************************************整体渐变背景*****************************************/
        backgroundPaint.setShader(fullBack);
        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);
        backgroundPaint.setShader(null);
        /*************************************月牙环*****************************************/
        if (ringBitmap != null) {
            canvas.drawBitmap(ringBitmap, center.x - ringBitmap.getWidth() / 2, center.y - ringBitmap.getHeight() / 2, ringPaint);
        }
        /*************************************五星骨架*****************************************/
        for (int i = 0; i < data.size(); i++) {
            Point point = DrawViewUtils.calculatePoint(center.x, center.y, radius, getAngleForEach(i));
            spiderPaint.setColor(pentagonSpiderColor);
            canvas.drawLine(center.x, center.y, point.x, point.y, spiderPaint);
        }
        /*************************************五星背景*****************************************/
        path.reset();
        path.moveTo(pointList.get(0).x, pointList.get(0).y);
        for (int i = 0; i < pointList.size(); i++) {
            path.lineTo(pointList.get(i).x, pointList.get(i).y);
        }
        path.close();
        pentagonBackgroundPaint.setColor(pentagonbackgroundColor);
        canvas.drawPath(path, pentagonBackgroundPaint);

        /*************************************五星前景*****************************************/
        if (foregroundList != null && foregroundList.size() > 0) {
            pentagonForegroundPaint.setShader(pentagonForeground);
            path.reset();
            path.moveTo(foregroundList.get(0).x, foregroundList.get(0).y);
            for (int i = 0; i < pointList.size(); i++) {
                path.lineTo(foregroundList.get(i).x, foregroundList.get(i).y);
            }
            path.close();
            canvas.drawPath(path, pentagonForegroundPaint);
            pentagonForegroundPaint.setShader(null);
        }

        /*************************************五星顶点图标和文字*****************************************/

        int offset = ConvertUtils.dp2px(2);
        for (int i = 0; i < data.size() * progress; i++) {
            topPointtPaint.setTextSize(data.get(i).textSize);
            topPointtPaint.setColor(data.get(i).textColor);
            topPointtPaint.setTypeface(Typeface.create(Typeface.DEFAULT, data.get(i).textStyle));
            Bitmap bitmap = getBitmap(data.get(i).iconRes, 0.6f);
            if (bitmap != null) {
                if (getScreenPercent() > 1f) {
                    offset = (int) (ConvertUtils.dp2px(1.5f) + Math.min(1f - getScreenPercent(), 1f) * getBitMapSize(bitmap));
                }
            }
            Point point = DrawViewUtils.calculatePoint(center.x, center.y, radius, getAngleForEach(i));
            if (i == 0) {
                canvas.drawText(data.get(i).text, point.x + betweenWebAndTextDistance + betweenTextAndImageDistance + getBitMapSize(bitmap), point.y, topPointtPaint);
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, point.x + betweenWebAndTextDistance, point.y - data.get(i).getSize(topPointtPaint)[1] + offset, topPointtPaint);
                }
            } else if (i == 1) {
                canvas.drawText(data.get(i).text, point.x + betweenWebAndTextDistance + betweenTextAndImageDistance + getBitMapSize(bitmap) - ConvertUtils.dp2px(30), point.y + betweenWebAndTextDistance + ConvertUtils.dp2px(10), topPointtPaint);
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, point.x + betweenWebAndTextDistance - ConvertUtils.dp2px(30), point.y - data.get(i).getSize(topPointtPaint)[1] + betweenWebAndTextDistance + ConvertUtils.dp2px(10) + offset, topPointtPaint);
                }
            } else if (i == 2) {
                canvas.drawText(data.get(i).text, point.x - data.get(i).getSize(topPointtPaint)[0] - betweenWebAndTextDistance + ConvertUtils.dp2px(30), point.y + betweenWebAndTextDistance + ConvertUtils.dp2px(10), topPointtPaint);
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, point.x - data.get(i).getSize(topPointtPaint)[0] - betweenWebAndTextDistance - betweenTextAndImageDistance - getBitMapSize(bitmap) + ConvertUtils.dp2px(30), point.y - data.get(i).getSize(topPointtPaint)[1] + betweenWebAndTextDistance + ConvertUtils.dp2px(10) + offset, topPointtPaint);
                }
            } else if (i == 3) {
                canvas.drawText(data.get(i).text, point.x - betweenWebAndTextDistance - data.get(i).getSize(topPointtPaint)[0], point.y, topPointtPaint);
                canvas.drawBitmap(bitmap, point.x - betweenWebAndTextDistance - betweenTextAndImageDistance - data.get(i).getSize(topPointtPaint)[0] - getBitMapSize(bitmap), point.y - data.get(i).getSize(topPointtPaint)[1] + offset, topPointtPaint);
            } else if (i == 4) {
                canvas.drawText(data.get(i).text, point.x + betweenTextAndImageDistance - getBitMapSize(bitmap) - data.get(i).getSize(topPointtPaint)[0] / 4, point.y - betweenWebAndTextDistance, topPointtPaint);
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, point.x + betweenTextAndImageDistance - getBitMapSize(bitmap) - data.get(i).getSize(topPointtPaint)[0] / 4 - getBitMapSize(bitmap), point.y - data.get(i).getSize(topPointtPaint)[1] - betweenWebAndTextDistance + offset, topPointtPaint);
                }
            }
        }

        /*************************************中间文字*****************************************/
        textCenterPaint.setTextSize(ConvertUtils.sp2px(36));
        textCenterPaint.setColor(Color.parseColor("#ffffff"));
        textCenterPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textCenterPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(number), center.x, center.y - DrawViewUtils.getTextWH(paint, String.valueOf(number))[1], textCenterPaint);

        textCenterPaint.setTextSize(ConvertUtils.sp2px(12));
//        textCenterPaint.setColor(Color.argb(Math.min(1f * progress * 5, 1f), 1f, 1f, 1f));
        textCenterPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        textCenterPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(title, center.x, center.y + ConvertUtils.dp2px(20), textCenterPaint);

        textCenterPaint.setTextSize(ConvertUtils.sp2px(12));
//        textCenterPaint.setColor(Color.argb(1f * progress, 1f, 1f, 1f));
        textCenterPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        textCenterPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(desc, center.x, center.y + ConvertUtils.dp2px(40), textCenterPaint);

    }

    private Bitmap getBitmap(int res, float scale) {
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(res)).getBitmap();
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return dstbmp;
    }

    private int getBitMapSize(Bitmap bitmap) {
        if (bitmap != null) {
            return Math.max(bitmap.getWidth(), bitmap.getHeight());
        }
        return 0;
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

            int distance = 0;
            if (foregroundList.size() > 0) {
                for (int i = 0; i < foregroundList.size(); i++) {
                    distance = (int) Math.max(distance, DrawViewUtils.calculateLength(center.x, center.y, foregroundList.get(i).x, foregroundList.get(i).y));
                }
                pentagonForeground = new LinearGradient(distance, 0f, distance * 2, distance * 2, 0xfff0f9f, 0x35ffffff, Shader.TileMode.MIRROR);
            }
            invalidate();
        }
    }

    /**
     * 动画
     */
    private ValueAnimator valueAnimator, progressAnimator;
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
    /**
     * 动画监听器
     */
    private ValueAnimator.AnimatorUpdateListener progressListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (data.size() > 0) {
                progress = (float) animation.getAnimatedValue();
                number = (int) (progress * total);
                invalidate();
            }
        }
    };

    /**
     * 开始动画
     */
    public void startAnimation() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (progressAnimator != null) {
            progressAnimator.cancel();
        }
        valueAnimator = ValueAnimator.ofFloat(0, 1f);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setDuration(ANIMATION_DURATION);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                progress = 0;
                number = 0;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                int distance = 0;
                if (foregroundList.size() > 0) {
                    for (int i = 0; i < foregroundList.size(); i++) {
                        distance = (int) Math.max(distance, DrawViewUtils.calculateLength(center.x, center.y, foregroundList.get(i).x, foregroundList.get(i).y));
                    }
                    pentagonForeground = new LinearGradient(distance, 0f, distance * 2, distance * 2, 0xfff0f9f, 0x35ffffff, Shader.TileMode.MIRROR);
                }
                startProgressAnimation();
            }
        });
        valueAnimator.addUpdateListener(listener);
        valueAnimator.start();
    }

    /**
     * 开始动画
     */
    public void startProgressAnimation() {
        if (progressAnimator != null) {
            progressAnimator.cancel();
        }
        progressAnimator = ValueAnimator.ofFloat(0, 1f);
        progressAnimator.setRepeatMode(ValueAnimator.RESTART);
        progressAnimator.setDuration(ANIMATION_DURATION * 2);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.addUpdateListener(progressListener);
        progressAnimator.start();
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

    private float getScreenPercent() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        float density = dm.density;
        float screenWidth = dm.widthPixels * density + 0.5f;        // 屏幕宽（px，如：480px）
        float screenHeight = dm.heightPixels * density + 0.5f;        // 屏幕高（px，如：800px）

        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;
        return screenWidth / screenHeight;
    }

    /**
     * 数据模型
     */
    public static class SimpleSpiderViewBean {
        private String text;
        private float textSize = ConvertUtils.sp2px(14);
        private int textColor = Color.parseColor("#FFFFFF");
        private int textStyle = Typeface.NORMAL;
        private int iconRes;

        private float percent;


        public SimpleSpiderViewBean(int iconRes, String text, float percent) {
            this.text = text;
            this.percent = percent;
            this.iconRes = iconRes;
        }

        public SimpleSpiderViewBean(String text, float textSize, int textColor, int textStyle, String remark, float remarkSize, int remarkColor, int remarkStyle, float percent) {
            this.text = text;
            this.textSize = textSize;
            this.textColor = textColor;
            this.textStyle = textStyle;
            this.percent = percent;
        }

        public String getText() {
            return text;
        }


        public float getPercent() {
            return percent;
        }

        private int[] getSize(Paint paint) {

            paint.setTextSize(textSize);
            paint.setColor(textColor);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, textStyle));
            int[] textSize = DrawViewUtils.getTextWH(paint, text);
            return textSize;
        }
    }
}
