package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.ConvertUtils;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.app.utils.VisualizerHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 音频可视化控件 - 扩散环
 */
public class DiffusionRingView extends View implements VisualizerHelper.OnVisualizerEnergyCallBack {
    private int radius = ConvertUtils.dp2px(100);
    private Point centerPoint = new Point();
    private Paint paint = new Paint();
    private int threshold = 500;
    private int startRadius = 0;

    {
        paint.setStrokeWidth(ConvertUtils.dp2px(1));
        paint.setAntiAlias(true);
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public void setStartRadius(int startRadius) {
        this.startRadius = startRadius;

    }

    private List<DiffusionRingViewBean> list = new ArrayList<>();

    public DiffusionRingView(Context context) {
        super(context);
    }

    public DiffusionRingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DiffusionRingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerPoint.set(w / 2, h / 2);
    }

    @Override
    public void setWaveData(byte[] data, float totalEnergy) {
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).enable) {
                list.remove(i);
                break;
            }
        }
        //能量最高点的位置百分比
        float positionPercent = totalEnergy * 1.0f / (VisualizerHelper.imageValue ? 4000 : 2000);
        if (totalEnergy > (VisualizerHelper.imageValue ? 2000 : 1000) + threshold) {
            DiffusionRingViewBean attachmentRingViewBean = new DiffusionRingViewBean();
            attachmentRingViewBean.alpha = VisualizerHelper.ALPHA;
            attachmentRingViewBean.radius = radius + startRadius;
            attachmentRingViewBean.angle = positionPercent * 360;
            list.add(attachmentRingViewBean);
        }

        invalidate();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(centerPoint.x, centerPoint.y, radius + startRadius, paint);


        for (int i = 0; i < list.size(); i++) {
            list.get(i).alpha -= 5;
            list.get(i).radius += 5;
            if (list.get(i).alpha <= 0) {
                list.get(i).enable = false;
            }
            if (list.get(i).enable) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.argb(list.get(i).alpha, VisualizerHelper.RED, VisualizerHelper.GREEN, VisualizerHelper.BLUE));
                canvas.drawArc(centerPoint.x - list.get(i).radius, centerPoint.y - list.get(i).radius,
                        centerPoint.x + list.get(i).radius, centerPoint.y + list.get(i).radius,
                        0, 360, false, paint);

                CommonUtils.calcPoint(centerPoint.x, centerPoint.y, list.get(i).radius, list.get(i).angle, list.get(i).point);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.argb(list.get(i).alpha, VisualizerHelper.RED, VisualizerHelper.GREEN, VisualizerHelper.BLUE));
                canvas.drawArc(list.get(i).point.x - 10, list.get(i).point.y - 10,
                        list.get(i).point.x + 10, list.get(i).point.y + 10,
                        0, 360, true, paint);
            }
        }


    }


    private static class DiffusionRingViewBean {
        private int radius;
        private int alpha;
        private boolean enable = true;
        private float angle;
        private Point point = new Point();
    }
}
