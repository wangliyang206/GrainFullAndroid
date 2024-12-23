package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;

/**
 * 渐变文本视图
 */
public class GradientTextView extends androidx.appcompat.widget.AppCompatTextView {

    private Paint paint;

    private int mStartColor = 0;//0xFF4CA9F8;
    private int mEndColor = 0;//0xFF4DCFE1;

    public GradientTextView(Context context) {
        super(context);
        init();
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        setLayerType(LAYER_TYPE_SOFTWARE, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mStartColor != mEndColor) {
            int[] colors = {mStartColor, mEndColor}; // 渐变颜色
            LinearGradient linearGradient = new LinearGradient(0, 0, getWidth(), 0, colors, null, Shader.TileMode.CLAMP);
            paint.setShader(linearGradient);
        } else {
            paint.setColor(getCurrentTextColor());
            paint.setShader(null);
        }
        paint.setTextSize(getTextSize());
        paint.setTypeface(getTypeface());

        String text = getText().toString();
        // 获取文本的边界
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);

        // 计算中心位置
        float x = (getWidth() / 2) - (rect.width() / 2);
        float y = (getHeight() / 2) - (rect.height() / 2);

        // 绘制文本
        canvas.drawText(text, x, y + rect.height(), paint);
    }

    public void setStartColor(int startColor) {
        mStartColor = startColor;
    }

    public void setEndColor(int endColor) {
        this.mEndColor = endColor;
    }
}
