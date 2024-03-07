package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.zqw.mobile.grainfull.app.utils.VisualizerHelper;
import com.zqw.mobile.grainfull.mvp.model.entity.HorizontalEnergyViewBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 音频可视化控件 -横向能量条
 */
public class HorizontalEnergyView extends View implements VisualizerHelper.OnVisualizerEnergyCallBack {
    public boolean isCircle = true;
    private int clearance = ConvertUtils.dp2px(5);
    private int count = 20;
    private List<HorizontalEnergyViewBean> list = new ArrayList<>();
    private Paint backgroundPaint = new Paint();
    private Paint paint = new Paint();

    public void setCircle(boolean circle) {
        isCircle = circle;
        requestLayout();
    }

    public void setCount(int count) {
        this.count = count;
        requestLayout();
    }

    public HorizontalEnergyView(Context context) {
        super(context);
        init();
    }

    public HorizontalEnergyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalEnergyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backgroundPaint.setStrokeWidth(1.5f);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setColor(Color.WHITE);

        paint.setAntiAlias(true);
        paint.setAntiAlias(true);
    }


    @Override
    public void setWaveData(byte[] data, float totalEnergy) {

        int total = (int) (totalEnergy / (VisualizerHelper.isFFT ? 10 : 100) / 10);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).enable = i < total;

        }
        invalidate();
    }


    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        list.clear();
        int width = ((r - l) - (clearance * count - 1)) / count;
        for (int i = 0; i < count; i++) {
            HorizontalEnergyViewBean bean = new HorizontalEnergyViewBean();

            bean.rect.left = i * (width + clearance);
            bean.rect.right = bean.rect.left + width;
            bean.rect.top = (b - t) / 2 - (bean.rect.right - bean.rect.left) / 2;
            bean.rect.bottom = (b - t) / 2 + (bean.rect.right - bean.rect.left) / 2;
            list.add(bean);

        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < list.size(); i++) {
            if (isCircle) {
                canvas.drawArc(list.get(i).rect, 0, 360, false, backgroundPaint);
            } else {
                canvas.drawRect(list.get(i).rect, backgroundPaint);
            }
            if (list.get(i).enable) {
                paint.setColor(Color.WHITE);
                if (isCircle) {
                    canvas.drawArc(list.get(i).rect, 0, 360, false, paint);
                } else {
                    canvas.drawRect(list.get(i).rect, paint);
                }
            }
        }

    }
}
