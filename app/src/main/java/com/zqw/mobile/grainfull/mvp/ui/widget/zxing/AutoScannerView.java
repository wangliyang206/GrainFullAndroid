package com.zqw.mobile.grainfull.mvp.ui.widget.zxing;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.baidu.idl.face.platform.utils.DensityUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.mvp.ui.widget.zxing.camera.CameraManager;

/**
 * Created by yangxixi on 16/11/22.
 * <p>
 * 自动上下扫描
 */

public class AutoScannerView extends View {
    private Paint maskPaint;
    private Paint linePaint;
    private Paint traAnglePaint;
    private Paint textPaint;
    private CameraManager cameraManager;

    private final int maskColor = Color.parseColor("#60000000");                          //蒙在摄像头上面区域的半透明颜色
    private final int triAngleColor = Color.parseColor("#76EE00");                        //边角的颜色
    private final int lineColor = Color.parseColor("#FF0000");                            //中间线的颜色
    private final int textColor = Color.parseColor("#CCCCCC");                            //文字的颜色
    private final int triAngleLength = DensityUtils.dip2px(getContext(), 20);                    //每个角的点距离
    private final int triAngleWidth = DensityUtils.dip2px(getContext(), 4);                      //每个角的点宽度
    private final int textMarinTop = DensityUtils.dip2px(getContext(), 30);                      //文字距离识别框的距离
    private int lineOffsetCount = 0;

    public AutoScannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskPaint.setColor(maskColor);

        traAnglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //获取布局中的属性值
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoScannerView);
        int angleColor = a.getColor(R.styleable.AutoScannerView_triAngleColor, triAngleColor);
        traAnglePaint.setColor(angleColor);
        traAnglePaint.setStrokeWidth(triAngleWidth);
        traAnglePaint.setStyle(Paint.Style.STROKE);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(lineColor);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextSize(DensityUtils.dip2px(getContext(), 14));
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
        invalidate();//重新进入可能不刷新，所以调用一次。
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (cameraManager == null)
            return;
        Rect frame = cameraManager.getFramingRect();
        Rect previewFrame = cameraManager.getFramingRectInPreview();
        if (frame == null || previewFrame == null) {
            return;
        }

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // 除了中间的识别区域，其他区域都将蒙上一层半透明的图层
        canvas.drawRect(0, 0, width, frame.top, maskPaint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, maskPaint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, maskPaint);
        canvas.drawRect(0, frame.bottom + 1, width, height, maskPaint);

        String text = "将二维码放入框内，即可自动扫描";
        canvas.drawText(text, (width - textPaint.measureText(text)) / 2, frame.bottom + textMarinTop, textPaint);

        // 四个角落的三角
        Path leftTopPath = new Path();
        leftTopPath.moveTo(frame.left + triAngleLength, frame.top + triAngleWidth / 2);
        leftTopPath.lineTo(frame.left + triAngleWidth / 2, frame.top + triAngleWidth / 2);
        leftTopPath.lineTo(frame.left + triAngleWidth / 2, frame.top + triAngleLength);
        canvas.drawPath(leftTopPath, traAnglePaint);

        Path rightTopPath = new Path();
        rightTopPath.moveTo(frame.right - triAngleLength, frame.top + triAngleWidth / 2);
        rightTopPath.lineTo(frame.right - triAngleWidth / 2, frame.top + triAngleWidth / 2);
        rightTopPath.lineTo(frame.right - triAngleWidth / 2, frame.top + triAngleLength);
        canvas.drawPath(rightTopPath, traAnglePaint);

        Path leftBottomPath = new Path();
        leftBottomPath.moveTo(frame.left + triAngleWidth / 2, frame.bottom - triAngleLength);
        leftBottomPath.lineTo(frame.left + triAngleWidth / 2, frame.bottom - triAngleWidth / 2);
        leftBottomPath.lineTo(frame.left + triAngleLength, frame.bottom - triAngleWidth / 2);
        canvas.drawPath(leftBottomPath, traAnglePaint);

        Path rightBottomPath = new Path();
        rightBottomPath.moveTo(frame.right - triAngleLength, frame.bottom - triAngleWidth / 2);
        rightBottomPath.lineTo(frame.right - triAngleWidth / 2, frame.bottom - triAngleWidth / 2);
        rightBottomPath.lineTo(frame.right - triAngleWidth / 2, frame.bottom - triAngleLength);
        canvas.drawPath(rightBottomPath, traAnglePaint);

        //循环划线，从上到下
        if (lineOffsetCount > frame.bottom - frame.top - DensityUtils.dip2px(getContext(), 10)) {
            lineOffsetCount = 0;
        } else {
            lineOffsetCount = lineOffsetCount + 6;
//            canvas.drawLine(frame.left, frame.top + lineOffsetCount, frame.right, frame.top + lineOffsetCount, linePaint);    //画一条红色的线
            Rect lineRect = new Rect();
            lineRect.left = frame.left;
            lineRect.top = frame.top + lineOffsetCount;
            lineRect.right = frame.right;
            lineRect.bottom = frame.top + DensityUtils.dip2px(getContext(), 10) + lineOffsetCount;
            canvas.drawBitmap(((BitmapDrawable) (getResources().getDrawable(R.drawable.qr_scan_line))).getBitmap(), null, lineRect, linePaint);
        }
        postInvalidateDelayed(10L, frame.left, frame.top, frame.right, frame.bottom);
    }
}
