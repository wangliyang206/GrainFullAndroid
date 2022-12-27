package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baidu.idl.face.platform.utils.DensityUtils;

/**
 * 转盘 - 外层闪烁灯
 */

public class RotateLayout extends RelativeLayout {
    private Context context;
    private static final String TAG = "LuckPanLayout";
    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint yellowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int radius;
    private int CircleX, CircleY;
    private Canvas canvas;
    private boolean isYellow = false;
    private int delayTime = 500;
    private RotateView rotatePan;
    private ImageView startBtn;

    private int screenWidth, screeHeight;
    private int MinValue;
    private static final String START_BTN_TAG = "startbtn";
    public static final int DEFAULT_TIME_PERIOD = 500;

    public RotateLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public RotateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public RotateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int i) {
        this.context = context;
        backgroundPaint.setColor(Color.rgb(255, 92, 93));
        whitePaint.setColor(Color.WHITE);
        yellowPaint.setColor(Color.YELLOW);
        screeHeight = getResources().getDisplayMetrics().heightPixels;
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        //外围的圆点旋转起来，其实是view的定时更新
        post(new Runnable() {
            @Override
            public void run() {
                startLuckLight();
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //就是让两个view居中，没有其他的操作
        int centerX = (right - left) / 2;
        int centerY = (bottom - top) / 2;
        boolean panReady = false;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof RotateView) {
                rotatePan = (RotateView) child;
                int panWidth = child.getWidth();
                int panHeight = child.getHeight();
                child.layout(centerX - panWidth / 2, centerY - panHeight / 2, centerX + panWidth / 2, centerY + panHeight / 2);
                panReady = true;
            } else if (child instanceof ImageView) {
//                if(TextUtils.equals((String) child.getTag(),START_BTN_TAG)){
                startBtn = (ImageView) child;
                int btnWidth = child.getWidth();
                int btnHeight = child.getHeight();
                child.layout(centerX - btnWidth / 2, centerY - btnHeight / 2, centerX + btnWidth / 2, centerY + btnHeight / 2);
//                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        MinValue = Math.min(screenWidth, screeHeight);
        MinValue -= DensityUtils.dip2px(context, 20) * 2;
        Log.d(TAG, "screenWidth = " + screenWidth + "screenHeight = " + screeHeight + "MinValue = " + MinValue);
        setMeasuredDimension(MinValue, MinValue);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;

        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;
        int MinValue = Math.min(width, height);

        radius = MinValue / 2;
        CircleX = getWidth() / 2;
        CircleY = getHeight() / 2;
        //画背景
        canvas.drawCircle(CircleX, CircleY, radius, backgroundPaint);
        //画圆圈，主要功能也就在这里
        drawSmallCircle(isYellow);
    }

    private void drawSmallCircle(boolean FirstYellow) {
        //位置要在内部，每次的半径相应的变短一点
        int pointDistance = radius - DensityUtils.dip2px(context, 10);
        //每次增加20度，也就是能够得到18个点
        for (int i = 0; i <= 360; i += 20) {
            //每次获取到圆心的位置，由i控制位置
            int x = (int) (pointDistance * Math.sin(i * Math.PI / 180)) + CircleX;
            int y = (int) (pointDistance * Math.cos(i * Math.PI / 180)) + CircleY;

            //两个不同颜色圆
            if (FirstYellow)
                canvas.drawCircle(x, y, DensityUtils.dip2px(context, 6), yellowPaint);
            else
                canvas.drawCircle(x, y, DensityUtils.dip2px(context, 6), whitePaint);
            FirstYellow = !FirstYellow;
        }
    }

    //改变boolean 来实现转圈。而不是动画转圈
    private void startLuckLight() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                isYellow = !isYellow;
                invalidate();
                postDelayed(this, delayTime);
            }
        }, delayTime);
    }
}
