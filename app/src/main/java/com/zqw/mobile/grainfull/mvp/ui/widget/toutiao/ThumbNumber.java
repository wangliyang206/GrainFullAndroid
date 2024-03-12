package com.zqw.mobile.grainfull.mvp.ui.widget.toutiao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.zqw.mobile.grainfull.R;

/**
 * 拇指数字
 */
class ThumbNumber extends View {
    private Paint textPaint;
    private Paint textPaintStroke;
    private int number;
    public static final int TEXT_SIZE = 100;
    public static final int STROKE_WIDTH = 10;
    private Bitmap mTalk1;
    private Bitmap mTalk2;
    private Bitmap mTalk3;
    private Bitmap bitmapTalk;
    private int textWidth;
    private int imageWidth;

    public ThumbNumber(Context context) {
        this(context, null);
    }

    public ThumbNumber(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThumbNumber(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        //文字画笔
        textPaint = new Paint();
        //字号
        textPaint.setTextSize(TEXT_SIZE);
        //文本对齐
        textPaint.setTextAlign(Paint.Align.LEFT);
        //边框
        textPaint.setStrokeWidth(STROKE_WIDTH);
        //字体填充
        textPaint.setStyle(Paint.Style.FILL);
        //字体加粗
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        //文本x轴倾斜
        textPaint.setTextSkewX(-0.3f);
        //色号渐变
        LinearGradient mLinearGradient = new LinearGradient(0, 0, 0, 90f,
                new int[]{0xFFFF9641, 0xFFFF9641, 0xFFFF9641, 0xFFFF9641, 0xFFff0000, 0xFFff0000},
                null, Shader.TileMode.CLAMP);
        textPaint.setShader(mLinearGradient);
        //描边画笔
        textPaintStroke = new Paint();
        //边框颜色
        textPaintStroke.setColor(Color.BLACK);
        //边框字号
        textPaintStroke.setTextSize(TEXT_SIZE);
        //对齐
        textPaintStroke.setTextAlign(Paint.Align.LEFT);
        //边框宽度
        textPaintStroke.setStrokeWidth(4);
        //边框样式
        textPaintStroke.setStyle(Paint.Style.STROKE);
        //加粗
        textPaintStroke.setTypeface(Typeface.DEFAULT_BOLD);
        //x轴倾斜
        textPaintStroke.setTextSkewX(-0.3f);


        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inScaled = true;
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        mTalk1 = BitmapFactory.decodeResource(getResources(), R.drawable.multi_digg_word_level_1, opt).copy(Bitmap.Config.ARGB_8888, true);
        mTalk1.setDensity(getResources().getDisplayMetrics().densityDpi);
        mTalk2 = BitmapFactory.decodeResource(getResources(), R.drawable.multi_digg_word_level_2, opt).copy(Bitmap.Config.ARGB_8888, true);
        mTalk2.setDensity(getResources().getDisplayMetrics().densityDpi);
        mTalk3 = BitmapFactory.decodeResource(getResources(), R.drawable.multi_digg_word_level_3, opt).copy(Bitmap.Config.ARGB_8888, true);
        mTalk3.setDensity(getResources().getDisplayMetrics().densityDpi);
        bitmapTalk = mTalk1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        textWidth = (int) textPaint.measureText(number + "");
        imageWidth = bitmapTalk.getWidth();
    }

    Rect dst = new Rect();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(number + "", 40, 90, textPaint);
        canvas.drawText(number + "", 40, 90, textPaintStroke);

        dst.left = 60 + textWidth;
        dst.top = 40;
        dst.right = 60 + textWidth + imageWidth;
        dst.bottom = 115;
        canvas.drawBitmap(bitmapTalk, null, dst, textPaint);

    }

    public void setNumber(int number) {
        this.number = number;
        if (number < 20) {
            bitmapTalk = mTalk1;
        } else if (number < 40) {
            bitmapTalk = mTalk2;
        } else {
            bitmapTalk = mTalk3;
        }
        requestLayout();
        invalidate();
    }
}