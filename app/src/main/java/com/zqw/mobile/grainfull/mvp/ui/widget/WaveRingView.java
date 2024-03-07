package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.zqw.mobile.grainfull.app.utils.CommonUtils;
import com.zqw.mobile.grainfull.app.utils.VisualizerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 音频可视化控件 - 上帝之环
 */
@SuppressWarnings("ALL")
public class WaveRingView extends View implements VisualizerHelper.OnVisualizerEnergyCallBack {
    private int randomAngle;
    private int powerPosition;
    private Point point = new Point();
    private int x;
    private Random random = new Random();
    private float powerPercent;
    private int radius = ConvertUtils.dp2px(100);
    private Point centerPoint = new Point();
    private Paint paint = new Paint();
    private String text = "THE RING OF GOD";
    private float degress;
    /**
     * 是否旋转
     */
    private boolean isRotate = true;
    /**
     * 是否随机角度
     */
    private boolean isRandom = false;
    /**
     * 是否绘制基线
     */
    private boolean isBase = false;
    /**
     * 是否绘制波纹
     */
    private boolean isWave = true;
    /**
     * 是否绘制能量点
     */
    private boolean isPoint = true;
    /**
     * 是否均衡能量强度
     */
    private boolean isPowerOffset = true;
    /**
     * 能量点扩散
     */
    private boolean isSpread = true;
    /**
     * 是否绘制文字
     */
    private boolean isDrawText = true;
    /**
     * 文字位移
     */
    private boolean isMove = true;
    /**
     * 文字随机位移
     */
    private boolean isRandomMove = false;
    /**
     * 能量阈值
     */
    private int scope = 1;
    /**
     * 能量取值
     */
    private int value = 150;
    /**
     * 能量点与圆环距离
     */
    private int distance = 30;
    /**
     * 能量点扩散速度
     */
    private int speed = 3;
    /**
     * 文字位移阈值
     */
    private int moveValue = 1000;

    {
        paint.setAntiAlias(true);
    }

    public void setRandom(boolean random) {
        if (random) {
            isRotate = !random;
        }
        isRandom = random;

    }

    public void setRotate(boolean rotate) {
        if (rotate) {
            isRandom = !rotate;
        }
        isRotate = rotate;
    }

    public void setBase(boolean base) {
        isBase = base;
    }

    public void setWave(boolean wave) {
        isWave = wave;
    }

    public void setPowerOffset(boolean powerOffset) {
        isPowerOffset = powerOffset;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }


    public void setValue(int value) {
        this.value = value;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setPoint(boolean point) {
        isPoint = point;
    }

    public void setSpread(boolean spread) {
        isSpread = spread;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDrawText(boolean drawText) {
        isDrawText = drawText;
    }

    public void setMove(boolean move) {
        isMove = move;
    }

    public void setRandomMove(boolean randomMove) {
        isRandomMove = randomMove;
    }

    public void setMoveValue(int moveValue) {
        this.moveValue = moveValue;
    }

    private List<WaveRingViewBean> list = new ArrayList<>();
    private List<Float> lastRadius = new ArrayList<>();

    public WaveRingView(Context context) {
        super(context);
    }

    public WaveRingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WaveRingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerPoint.set(w / 2, h / 2);
        sweepGradient = new SweepGradient(centerPoint.x, centerPoint.y, new int[]{Color.GREEN, Color.RED, Color.BLUE, Color.GREEN}, null);
    }

    private SweepGradient sweepGradient;

    @Override
    public void setWaveData(byte[] data, float totalEnergy) {
        int total = data.length - value;
        //圆点基准位置半径
        float basePoint = radius + value - distance;
        if (list.size() > 0) {
            if (lastRadius.size() == 0 || list.size() != lastRadius.size()) {
                lastRadius.clear();
                for (int i = 0; i < total; i++) {
                    lastRadius.add(basePoint);
                }
            }
            for (int i = 0; i < lastRadius.size(); i++) {
                if (i < list.size()) {
                    if (list.get(i).radius < lastRadius.get(i)) {
                        lastRadius.set(i, list.get(i).radius);
                    } else {
                        if (isSpread) {
                            lastRadius.set(i, lastRadius.get(i).floatValue() + speed);
                        } else {
                            lastRadius.set(i, list.get(i).radius);
                        }
                    }
                }

            }
        }

        list.clear();
        //圆周长
        float totalLength = 3.14f * 2 * radius;
        //每个角度所占用的长度
        float eachWidthByAngle = totalLength / 360;
        //每个能量所占用的长度
        float eachWidthByDataLength = totalLength / total;
        for (int i = 0; i < total; i++) {
            //每个能量在圆环上的角度位置
            float positionAngle = i * 1.0f / total * 360;
            //每个能量的强度百分比
            powerPercent = isPowerOffset ? 0f : data[i] * 1.0f / VisualizerHelper.LUMP_COUNT;
            WaveRingViewBean waveRingViewBean = new WaveRingViewBean();
            waveRingViewBean.angle = positionAngle + (isRotate ? degress : getRandomAngle());
            waveRingViewBean.powerPercent = data[i] * 1.0f / VisualizerHelper.LUMP_COUNT;
            if (data[i] > scope) {
                waveRingViewBean.radius = basePoint - data[i] - powerPercent * data[i] - (ConvertUtils.dp2px(distance) * powerPercent);
                CommonUtils.calcPoint(centerPoint.x, centerPoint.y, (int) (radius + data[i]) + value, waveRingViewBean.angle, waveRingViewBean.inner);
                CommonUtils.calcPoint(centerPoint.x, centerPoint.y, (int) (radius - data[i] - powerPercent * data[i]) + value, waveRingViewBean.angle, waveRingViewBean.outter);
                CommonUtils.calcPoint(centerPoint.x, centerPoint.y, radius + value, waveRingViewBean.angle, waveRingViewBean.center);
            } else {
                waveRingViewBean.radius = basePoint;
                CommonUtils.calcPoint(centerPoint.x, centerPoint.y, radius + value, waveRingViewBean.angle, waveRingViewBean.inner);
                CommonUtils.calcPoint(centerPoint.x, centerPoint.y, radius + value, waveRingViewBean.angle, waveRingViewBean.outter);
                CommonUtils.calcPoint(centerPoint.x, centerPoint.y, radius + value, waveRingViewBean.angle, waveRingViewBean.center);
            }
            list.add(waveRingViewBean);
        }
        powerPosition = -1;
        if (isMove) {
            if (totalEnergy > moveValue) {
                powerPosition = isRandomMove ? CommonUtils.randomInt(random, 1, list.size()) : getStrongPowerPosition(data);
            }
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setShader(sweepGradient);
        paint.setColor(Color.argb((int) (VisualizerHelper.ALPHA * 0.85f), VisualizerHelper.RED, VisualizerHelper.GREEN, VisualizerHelper.BLUE));
        paint.setStrokeWidth(ConvertUtils.dp2px(2));

        for (int i = 0; i < list.size(); i++) {
            if (isBase) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(ConvertUtils.dp2px(1f));
                canvas.drawLine(list.get(i).inner.x, list.get(i).inner.y, list.get(i).outter.x, list.get(i).outter.y, paint);
            }

            if (isPoint) {
                paint.setStyle(Paint.Style.FILL);
                if (i < lastRadius.size()) {
                    CommonUtils.calcPoint(centerPoint.x, centerPoint.y, (int) lastRadius.get(i).floatValue(), list.get(i).angle, list.get(i).point);
                    canvas.drawArc(
                            list.get(i).point.x - ConvertUtils.dp2px(2),
                            list.get(i).point.y - ConvertUtils.dp2px(2),
                            list.get(i).point.x + ConvertUtils.dp2px(2),
                            list.get(i).point.y + ConvertUtils.dp2px(2),
                            0, 360, true, paint
                    );
                }
            }

            if (isWave) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(ConvertUtils.dp2px(1.8f));
                if (i == 0) {
                    canvas.drawLine(list.get(list.size() - 1).center.x, list.get(list.size() - 1).center.y, list.get(0).outter.x, list.get(0).outter.y, paint);
                    canvas.drawLine(list.get(0).outter.x, list.get(0).outter.y, list.get(1).center.x, list.get(1).center.y, paint);
                    canvas.drawLine(list.get(1).center.x, list.get(1).center.y, list.get(0).inner.x, list.get(0).inner.y, paint);
                    canvas.drawLine(list.get(0).inner.x, list.get(0).inner.y, list.get(list.size() - 1).center.x, list.get(list.size() - 1).center.y, paint);
                } else if (i == list.size() - 1) {
                    canvas.drawLine(list.get(list.size() - 2).center.x, list.get(list.size() - 2).center.y, list.get(list.size() - 1).outter.x, list.get(list.size() - 1).outter.y, paint);
                    canvas.drawLine(list.get(list.size() - 1).outter.x, list.get(list.size() - 1).outter.y, list.get(0).center.x, list.get(0).center.y, paint);
                    canvas.drawLine(list.get(0).center.x, list.get(0).center.y, list.get(list.size() - 1).inner.x, list.get(list.size() - 1).inner.y, paint);
                    canvas.drawLine(list.get(list.size() - 1).inner.x, list.get(list.size() - 1).inner.y, list.get(list.size() - 2).center.x, list.get(list.size() - 2).center.y, paint);
                } else {
                    canvas.drawLine(list.get(i - 1).center.x, list.get(i - 1).center.y, list.get(i).outter.x, list.get(i).outter.y, paint);
                    canvas.drawLine(list.get(i).outter.x, list.get(i).outter.y, list.get(i + 1).center.x, list.get(i + 1).center.y, paint);
                    canvas.drawLine(list.get(i + 1).center.x, list.get(i + 1).center.y, list.get(i).inner.x, list.get(i).inner.y, paint);
                    canvas.drawLine(list.get(i).inner.x, list.get(i).inner.y, list.get(i - 1).center.x, list.get(i - 1).center.y, paint);
                }
            }
        }


        if (isDrawText) {
            paint.setShader(null);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            paint.setTextSize(ConvertUtils.dp2px(20));
            paint.setStrokeWidth(ConvertUtils.dp2px(1f));
            paint.setTextAlign(Paint.Align.CENTER);
            point.set(centerPoint.x, centerPoint.y);
            if (isMove) {
                if (powerPosition > -1 && powerPosition < list.size()) {
                    CommonUtils.calcPoint(centerPoint.x, centerPoint.y, (int) (150 * 1.0f * list.get(powerPosition).powerPercent), list.get(powerPosition).angle, point);
                    canvas.drawText(text, point.x, point.y, paint);
                }
            }
            canvas.drawText(text, centerPoint.x, centerPoint.y, paint);
        }
        if (isRotate) {
            if (degress >= 360) {
                degress = 0;
            } else {
                degress += 0.3f;
            }
        }

        x++;
        if (x > 300) {
            randomAngle = CommonUtils.randomInt(random, 1, 4);
            x = 0;
        }
    }

    /**
     * 获取最强能量位置
     */
    private int getStrongPowerPosition(byte[] data) {
        int power = 0;
        int position = -1;
        for (int i = 0; i < list.size(); i++) {
            if (data[i] > power) {
                position = i;
            }
        }
        return position;
    }

    /**
     * 获取随机角度
     */
    private float getRandomAngle() {
        if (isRandom) {
            switch (randomAngle) {
                case 1:
                    return 90;
                case 2:
                    return 180;
                case 3:
                    return 270;
                case 4:
                    return 360;
                default:
                    return 0;
            }
        }
        return degress;
    }

    private static class WaveRingViewBean {
        private Point point = new Point();
        private Point inner = new Point();
        private Point center = new Point();
        private Point outter = new Point();
        private float angle;
        private float radius;
        private float powerPercent;
    }
}
