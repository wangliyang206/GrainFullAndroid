package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ScrollerCompat;

import com.baidu.idl.face.platform.utils.DensityUtils;
import com.zqw.mobile.grainfull.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 转盘 - 完整品
 */

public class RotateLayoutView extends View {
    private Context context;
    private int panNum = 6;
    private Paint dPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint yellowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int InitAngle = 0;
    private int radius = 0;
    private int verPanRadius;
    private int diffRadius;
    private List<Integer> images = new ArrayList<>();
    private List<String> strs = new ArrayList<>();

    private List<Bitmap> bitmaps = new ArrayList<>();
    private ScrollerCompat scroller;
    private int screenWidth, screeHeight;
    private int ringLength = 16;
    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect mRect;
    private boolean isYellow = false;
    private int delayTime = 500;
    private int pos;

    public RotateLayoutView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public RotateLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public RotateLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int s) {
        this.context = context;
        screeHeight = getResources().getDisplayMetrics().heightPixels;
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        scroller = ScrollerCompat.create(context);
        //外围的圆点旋转起来，其实是view的定时更新
        post(new Runnable() {
            @Override
            public void run() {
                startLuckLight();
            }
        });
    }

    private void initDate() {
        //数据不能错误
        if (strs.size() != images.size() || strs.size() == 0 || images.size() == 0) {
            return;
        }
        panNum = strs.size();
        //获取到角度
        InitAngle = 360 / panNum;
        //获取到角度
        verPanRadius = 360 / panNum;
        //初始角度的一半
        diffRadius = verPanRadius / 2;
        //两个扇形的颜色
        dPaint.setColor(Color.rgb(255, 133, 132));
        //两个扇形的颜色
        sPaint.setColor(Color.rgb(254, 104, 105));
        //背景
        backgroundPaint.setColor(Color.rgb(255, 92, 93));
        //圆圈画笔
        whitePaint.setColor(Color.WHITE);
        yellowPaint.setColor(Color.YELLOW);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(DensityUtils.dip2px(context, 16));
        setClickable(true);
        for (int i = 0; i < panNum; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), images.get(i));
            bitmaps.add(bitmap);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int MinValue = Math.min(screenWidth, screeHeight);
        //设置边距，获取到view的宽度
        MinValue -= DensityUtils.dip2px(context, 30) * 2;
        setMeasuredDimension(MinValue, MinValue);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //内边距
        final int paddingLeft = getPaddingLeft() + DensityUtils.dip2px(context, ringLength);
        final int paddingRight = getPaddingRight() - DensityUtils.dip2px(context, ringLength);
        final int paddingTop = getPaddingTop() + DensityUtils.dip2px(context, ringLength);
        final int paddingBottom = getPaddingBottom() - DensityUtils.dip2px(context, ringLength);
        //view的宽高
        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;
        int MinValue = Math.min(width, height);
        //获取到圆的半径
        radius = MinValue / 2;
        //获取到view的矩形
        RectF rectF = new RectF(getPaddingLeft() + DensityUtils.dip2px(context, ringLength), getPaddingTop() + DensityUtils.dip2px(context, ringLength), width - DensityUtils.dip2px(context, ringLength), height - DensityUtils.dip2px(context, ringLength));
        //角度的起始是顺时针的，%4==0,如果为4,则就是90度，数学的第三象限开始，否则在第四象限
        int angle = (panNum % 4 == 0) ? InitAngle : InitAngle - diffRadius;
        Log.d("angle", String.valueOf(angle));
        //大圆
        canvas.drawCircle(MinValue / 2, MinValue / 2, radius, backgroundPaint);

        //绘制圆弧，两个不同颜色的圆弧
        for (int i = 0; i < panNum; i++) {
            if (i % 2 == 0) {
                canvas.drawArc(rectF, angle, verPanRadius, true, dPaint);
            } else {
                canvas.drawArc(rectF, angle, verPanRadius, true, sPaint);
            }
            angle += verPanRadius;
        }

        //绘制图片
        for (int i = 0; i < panNum; i++) {
            drawIcon(width / 2, height / 2, radius, (panNum % 4 == 0) ? InitAngle + diffRadius : InitAngle, i, canvas);
            InitAngle += verPanRadius;
        }

        //绘制文字
        for (int i = 0; i < panNum; i++) {
            drawText((panNum % 4 == 0) ? InitAngle + diffRadius + (diffRadius * 3 / 4) : InitAngle + diffRadius, strs.get(i), 2 * radius, textPaint, canvas, rectF);
            InitAngle += verPanRadius;
        }

        //绘制中间的图
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.node);
        mRect = new Rect(MinValue / 2 - bitmap.getWidth() / 2, MinValue / 2 - bitmap.getHeight() / 2, MinValue / 2 + bitmap.getWidth() / 2, MinValue / 2 + bitmap.getHeight() / 2);
        canvas.drawBitmap(bitmap, null, mRect, null);
        drawSmallCircle(canvas, isYellow, MinValue);
    }

    //文字绘制，path路径
    private void drawText(float startAngle, String string, int mRadius, Paint mTextPaint, Canvas mCanvas, RectF mRange) {
        Path path = new Path();
        path.addArc(mRange, startAngle, verPanRadius);
        float textWidth = mTextPaint.measureText(string);

        //圆弧的水平偏移，保证在转盘内部
        float hOffset = (panNum % 4 == 0) ? ((float) (mRadius * Math.PI / panNum / 2))
                : ((float) (mRadius * Math.PI / panNum / 2 - textWidth / 2));
        //圆弧的垂直偏移，保证在转盘内部
        float vOffset = mRadius / 2 / 6;
        mCanvas.drawTextOnPath(string, path, hOffset, vOffset, mTextPaint);
    }

    private void drawIcon(int xx, int yy, int mRadius, float startAngle, int i, Canvas mCanvas) {

        int imgWidth = mRadius / 4;

        float angle = (float) Math.toRadians(verPanRadius + startAngle);

        //确定图片在圆弧中 中心点的位置
        float x = (float) (xx + (mRadius / 2 + mRadius / 12 - DensityUtils.dip2px(context, ringLength)) * Math.cos(angle));
        float y = (float) (yy + (mRadius / 2 + mRadius / 12 - DensityUtils.dip2px(context, ringLength)) * Math.sin(angle));

        // 确定绘制图片的位置，前后偏移，得到图片的位置
        RectF rect = new RectF(x - imgWidth * 2 / 3, y - imgWidth * 2 / 3,
                x + imgWidth * 2 / 3, y + imgWidth * 2 / 3);
//        mCanvas.drawRect(rect,textPaint);
        Bitmap bitmap = bitmaps.get(i);
        //绘制
        mCanvas.drawBitmap(bitmap, null, rect, null);
    }

    private void drawSmallCircle(Canvas canvas, boolean FirstYellow, int minValue) {
        //位置要在内部，每次的半径相应的变短一点
        int pointDistance = radius - DensityUtils.dip2px(context, 9);
        //每次增加20度，也就是能够得到18个点
        for (int i = 0; i <= 360; i += 20) {
            //每次获取到圆心的位置，由i控制位置
            int x = (int) (pointDistance * Math.sin(i * Math.PI / 180)) + minValue / 2;
            int y = (int) (pointDistance * Math.cos(i * Math.PI / 180)) + minValue / 2;

            //两个不同颜色圆
            if (FirstYellow)
                canvas.drawCircle(x, y, DensityUtils.dip2px(context, 4), yellowPaint);
            else
                canvas.drawCircle(x, y, DensityUtils.dip2px(context, 4), whitePaint);
            FirstYellow = !FirstYellow;
        }
    }

    /**
     * 点击事件，点击中心就开始动画
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                //点击了中间位置
                if (x > mRect.left && x < mRect.right && y > mRect.top && y < mRect.bottom) {
                    startAnimation(pos);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return super.onTouchEvent(event);
    }

    private static final long ONE_WHEEL_TIME = 500;

    //旋转的动画
    public void startAnimation(int pos) {

        //Rotate lap. 随机的圈数，>4圈，修改可以控制旋转的圈数和时长
        int lap = (int) (Math.random() * 2) + 4;

        //Rotate angle.
        int angle = 0;
        if (pos < 0) {
            //随机角度
            angle = (int) (Math.random() * 360);
        } else {
            //控制某个角度，由具体的位置，计算出具体的角度值
            int initPos = queryPosition();
            if (pos > initPos) {
                angle = (pos - initPos) * verPanRadius;
                lap -= 1;
                angle = 360 - angle;
            } else if (pos < initPos) {
                angle = (initPos - pos) * verPanRadius;
            } else {
                //nothing to do.
            }
        }

        //All of the rotate angle.
        int increaseDegree = lap * 360 + angle;
        long time = (lap + angle / 360) * ONE_WHEEL_TIME;
        int DesRotate = increaseDegree + InitAngle;

        //TODO 为了每次都能旋转到转盘的中间位置
        int offRotate = DesRotate % 360 % verPanRadius;
        DesRotate -= offRotate;
        DesRotate += diffRadius;
        //属性动画
        ValueAnimator animtor = ValueAnimator.ofInt(InitAngle, DesRotate);
        animtor.setInterpolator(new AccelerateDecelerateInterpolator());
        animtor.setDuration(time);
        animtor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int updateValue = (int) animation.getAnimatedValue();
                //每次绘制的初始值改变，最终会停止到之前设置的角度数值
                InitAngle = (updateValue % 360 + 360) % 360;
                //重绘制
                ViewCompat.postInvalidateOnAnimation(RotateLayoutView.this);
            }
        });
        //动画监听，获取到具体的停留位置，这里处理随机位置，其实提前是知道的
        animtor.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                int pos = InitAngle / 60;
                if (pos >= 0 && pos <= 3) {
                    pos = 3 - pos;
                } else {
                    pos = (6 - pos) + 3;
                }

                mOnCallBackPosition.getStopPosition(pos);
                //可以回调出去，得到值
//                Toast.makeText(context, strs.get(pos), Toast.LENGTH_SHORT).show();

            }
        });
        animtor.start();
    }

    //由具体的位置，获取到角度
    private int queryPosition() {
        InitAngle = (InitAngle % 360 + 360) % 360;
        int pos = InitAngle / verPanRadius;
        if (panNum == 4) pos++;
        return calcumAngle(pos);
    }

    private int calcumAngle(int pos) {
        if (pos >= 0 && pos <= panNum / 2) {
            pos = panNum / 2 - pos;
        } else {
            pos = (panNum - pos) + panNum / 2;
        }
        return pos;
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

    /**
     * 设置初始值，图片
     *
     * @param icon
     */
    public void setImageIcon(List<Integer> icon) {
        this.images.addAll(icon);
        initDate();
    }

    /**
     * 设置初始值 名字
     *
     * @param name
     */
    public void setStrName(List<String> name) {
        this.strs.addAll(name);
        initDate();
    }

    //回调传值
    public void setOnCallBackPosition(int pos, onCallBackPosition mOnCallBackPosition) {
        this.mOnCallBackPosition = mOnCallBackPosition;
        this.pos = pos;
    }

    private onCallBackPosition mOnCallBackPosition;

    public interface onCallBackPosition {
        void getStopPosition(int pos);
    }

}
