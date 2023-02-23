package com.zqw.mobile.grainfull.mvp.ui.widget.electimeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;

import com.zqw.mobile.grainfull.R;

/**
 * 电子时钟 - 单数控件
 */
public class ElecTimeNumView extends View {
    /**
     * 骨架画笔
     */
    Paint unitPaint;

    /**
     * 控件宽高,width不等于height/2
     */
    int width, height;

    /**
     * 骨架宽高
     */
    int unitW, unitH;

    /**
     * 默认骨架颜色
     */
    @ColorInt
    public static int unitBgColor = Color.parseColor("#e8e8e8");
//    public static int unitBgColor = Color.DKGRAY;

    /**
     * 选中的骨架颜色
     */
    @ColorInt
//    public static int unitSelectedColor = Color.parseColor("#e8e8e8");
    public static int unitSelectedColor = Color.DKGRAY;

    /**
     * 当前数字
     */
    int curNum;

    /**
     * 每个数字对应的颜色数组枚举
     */
    enum NumColors {

        NUM_0(new int[]{unitSelectedColor, unitSelectedColor, unitSelectedColor, unitSelectedColor, unitSelectedColor, unitSelectedColor, unitBgColor}),
        NUM_1(new int[]{unitBgColor, unitSelectedColor, unitSelectedColor, unitBgColor, unitBgColor, unitBgColor, unitBgColor}),
        NUM_2(new int[]{unitSelectedColor, unitSelectedColor, unitBgColor, unitSelectedColor, unitSelectedColor, unitBgColor, unitSelectedColor}),
        NUM_3(new int[]{unitSelectedColor, unitSelectedColor, unitSelectedColor, unitSelectedColor, unitBgColor, unitBgColor, unitSelectedColor}),
        NUM_4(new int[]{unitBgColor, unitSelectedColor, unitSelectedColor, unitBgColor, unitBgColor, unitSelectedColor, unitSelectedColor}),
        NUM_5(new int[]{unitSelectedColor, unitBgColor, unitSelectedColor, unitSelectedColor, unitBgColor, unitSelectedColor, unitSelectedColor}),
        NUM_6(new int[]{unitSelectedColor, unitBgColor, unitSelectedColor, unitSelectedColor, unitSelectedColor, unitSelectedColor, unitSelectedColor}),
        NUM_7(new int[]{unitSelectedColor, unitSelectedColor, unitSelectedColor, unitBgColor, unitBgColor, unitBgColor, unitBgColor}),
        NUM_8(new int[]{unitSelectedColor, unitSelectedColor, unitSelectedColor, unitSelectedColor, unitSelectedColor, unitSelectedColor, unitSelectedColor}),
        NUM_9(new int[]{unitSelectedColor, unitSelectedColor, unitSelectedColor, unitSelectedColor, unitBgColor, unitSelectedColor, unitSelectedColor});

        /**
         * 数字对应的颜色数组
         */
        int colors[];

        NumColors(int[] colors) {
            this.colors = colors;
        }

        public static NumColors getTargetNumColors(int num) {
            switch (num) {
                case 0:
                    return NUM_0;
                case 1:
                    return NUM_1;
                case 2:
                    return NUM_2;
                case 3:
                    return NUM_3;
                case 4:
                    return NUM_4;
                case 5:
                    return NUM_5;
                case 6:
                    return NUM_6;
                case 7:
                    return NUM_7;
                case 8:
                    return NUM_8;
                case 9:
                    return NUM_9;
                default:
                    return NUM_0;
            }
        }
    }


    public ElecTimeNumView(Context context) {
        this(context, null);
    }

    public ElecTimeNumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        unitW = height - width;
        unitH = 2 * width - height;
    }

    private void init(Context context) {

        unitPaint = new Paint();
        unitPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        unitPaint.setAntiAlias(true);
        unitPaint.setStrokeWidth(1F);
        unitPaint.setColor(unitBgColor);
        unitPaint.setAntiAlias(true);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (context == null || attrs == null) {
            return;
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ElecTimeNumView);

        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.ElecTimeNumView_num) {
                curNum = typedArray.getInt(attr, 0);
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawNum(canvas, curNum);

    }

    /**
     * 绘制数字
     *
     * @param canvas
     * @param num    数字
     */
    private void drawNum(Canvas canvas, int num) {
        //绘制第1个
        unitPaint.setColor(NumColors.getTargetNumColors(num).colors[0]);
        canvas.translate(unitH / 2, 0);
        drawUnit(canvas, unitW, unitH);

        //绘制第2个
        unitPaint.setColor(NumColors.getTargetNumColors(num).colors[1]);
        canvas.translate(unitW + unitH / 2, 0);
        canvas.rotate(90);
        canvas.translate(unitH / 2, 0);
        drawUnit(canvas, unitW, unitH);

        //绘制第3个
        unitPaint.setColor(NumColors.getTargetNumColors(num).colors[2]);
        canvas.translate(unitW, 0);
        drawUnit(canvas, unitW, unitH);

        //绘制第4个
        unitPaint.setColor(NumColors.getTargetNumColors(num).colors[3]);
        canvas.translate(unitW + unitH / 2, 0);
        canvas.rotate(90);
        canvas.translate(unitH / 2, 0);
        drawUnit(canvas, unitW, unitH);

        //绘制第5个
        unitPaint.setColor(NumColors.getTargetNumColors(num).colors[4]);
        canvas.translate(unitW + unitH / 2, 0);
        canvas.rotate(90);
        canvas.translate(unitH / 2, 0);
        drawUnit(canvas, unitW, unitH);

        //绘制第6个
        unitPaint.setColor(NumColors.getTargetNumColors(num).colors[5]);
        canvas.translate(unitW, 0);
        drawUnit(canvas, unitW, unitH);

        //绘制第7个
        unitPaint.setColor(NumColors.getTargetNumColors(num).colors[6]);
        canvas.rotate(90);
        canvas.translate(unitH / 2, -unitH / 2);
        drawUnit(canvas, unitW, unitH);
    }

    /**
     * 绘制骨架单元
     *
     * @param w 宽
     * @param h 高
     */
    private void drawUnit(Canvas canvas, int w, int h) {
        if (mPath == null) {
            mPath = new Path();
            // 间隔，相当于padding
            int d = 5;
            mPath.moveTo(d, h / 2);
            mPath.lineTo(h / 2 + d, 0);
            mPath.lineTo(w - h / 2 - d, 0);
            mPath.lineTo(w - d, h / 2);
            mPath.lineTo(w - h / 2 - d, h);
            mPath.lineTo(h / 2 + d, h);
            mPath.lineTo(d, h / 2);
            mPath.close();
        }


        //绘制网格
        if (isDrawGrid) {
            canvas.save();
            canvas.clipPath(mPath);
            canvas.drawPath(mPath, unitPaint);
            unitPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
            canvas.drawPath(getGridPath(w, h), unitPaint);
            unitPaint.setXfermode(null);
            canvas.restore();
        } else {
            canvas.drawPath(mPath, unitPaint);
        }
    }

    boolean isDrawGrid = true;

    Path mPath;
    Path gridPath;

    float grideWidth;

    Path getGridPath(int w, int h) {
        if (gridPath != null) {
            return gridPath;
        }
        gridPath = new Path();
        grideWidth = h / 3F;
        for (int i = 1; i < w / grideWidth; i++) {
            gridPath.moveTo(0, grideWidth * i);
            gridPath.lineTo(w, grideWidth * i);
        }
        for (int j = 1; j < w / grideWidth; j++) {
            gridPath.moveTo(grideWidth * j, 0);
            gridPath.lineTo(grideWidth * j, h);
        }
        return gridPath;
    }

    /**
     * 设置当前数字
     *
     * @param num
     * @return
     */
    public ElecTimeNumView setCurNum(int num) {
        if (num >= 0 && num <= 9 && num != curNum) {
            curNum = num;
            postInvalidate();
        }
        return this;
    }
}
