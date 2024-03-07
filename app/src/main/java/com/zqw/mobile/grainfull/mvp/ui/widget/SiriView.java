package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.app.utils.VisualizerHelper;
import com.zqw.mobile.grainfull.mvp.model.entity.SiriViewBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 音频可视化控件 - 仿siri那个智障倾听时的效果
 */
public class SiriView extends View implements VisualizerHelper.OnVisualizerEnergyCallBack {

    private int defultY;
    private Paint paint = new Paint();
    private List<SiriViewBean> attribute = new ArrayList<>();
    private List<Point> pointFList = new ArrayList<>();
    private PathMeasure pathMeasure = new PathMeasure();


    public SiriView(Context context) {
        this(context, null);
    }

    public SiriView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SiriView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        pointFList.clear();
        for (int i = 0; i < 10; i++) {
            pointFList.add(new Point());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        defultY = h / 2;
        attribute.clear();
        attribute.add(createScope(200, 109, 106, 160, 0, 0.1f, 0.5f));
        attribute.add(createScope(200, 144, 109, 131, 0.1f, 0.2f, 0.6f));
        attribute.add(createScope(200, 140, 147, 94, 0.2f, 0.3f, 0.7f));
        attribute.add(createScope(200, 117, 157, 145, 0.1f, 0.4f, 0.8f));
        attribute.add(createScope(200, 54, 102, 126, 0, 0.5f, 0.9f));
    }

    private SiriViewBean createScope(int a, int r, int g, int b, float heightPercent, float leftDistance, float rightDistance) {
        SiriViewBean siriViewBean = new SiriViewBean();
        siriViewBean.color = Color.argb(a, r, g, b);
        siriViewBean.path = new Path();
        siriViewBean.heightPercent = heightPercent;
        siriViewBean.leftDistance = leftDistance;
        siriViewBean.rightDistance = rightDistance;
        return siriViewBean;
    }

    @Override
    public void setWaveData(byte[] data, float totalEnergy) {
        for (int i = 0; i < attribute.size(); i++) {
            pointFList.get(0).x = 0;
            pointFList.get(0).y = defultY;
            pointFList.get(1).x = (int) (attribute.get(i).leftDistance * getWidth()) + Math.abs(getData(data, i));
            pointFList.get(1).y = defultY;
            pointFList.get(2).x = (int) ((attribute.get(i).rightDistance + attribute.get(i).leftDistance) / 2 * getWidth());
            pointFList.get(2).y = (int) (defultY - defultY * attribute.get(i).heightPercent - Math.abs(getData(data, i)));
            pointFList.get(3).x = (int) (attribute.get(i).rightDistance * getWidth()) - Math.abs(getData(data, i));
            pointFList.get(3).y = defultY;
            pointFList.get(4).x = getWidth();
            pointFList.get(4).y = defultY;
            pointFList.get(5).x = (int) (attribute.get(i).rightDistance * getWidth()) - Math.abs(getData(data, i));
            pointFList.get(5).y = defultY;
            pointFList.get(6).x = (int) ((attribute.get(i).rightDistance + attribute.get(i).leftDistance) / 2 * getWidth());
            pointFList.get(6).y = (int) (defultY + defultY * attribute.get(i).heightPercent + Math.abs(getData(data, i)));
            pointFList.get(7).x = (int) (attribute.get(i).leftDistance * getWidth()) + Math.abs(getData(data, i));
            pointFList.get(7).y = defultY;
            pointFList.get(8).x = 0;
            pointFList.get(8).y = defultY;
            CommonUtils.calculateBesselSmoothPath(0.1f, 10, true, pathMeasure, attribute.get(i).path, pointFList);
        }
    }

    private byte getData(byte[] data, int index) {
//        if (true){
//            return 100;
//        }
        if (index == 0) {
            return data[0];
        } else if (index == 1) {
            return data[2];
        } else if (index == 2) {
            return data[4];
        } else if (index == 3) {
            return data[6];
        } else if (index == 4) {
            return data[8];
        } else {
            return data[0];
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < attribute.size(); i++) {
            if (
                    i == 0
                            ||
                            i == 1
                            ||
                            i == 2
                            ||
                            i == 3
                            ||
                            i == 4
            ) {
                paint.setColor(attribute.get(i).color);
                canvas.drawPath(attribute.get(i).path, paint);
            }
        }
        invalidate();
    }
}
