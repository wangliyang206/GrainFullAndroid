package com.zqw.mobile.grainfull.mvp.ui.widget.aircraftwar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.zqw.mobile.grainfull.mvp.model.entity.AutoSprite;
import com.zqw.mobile.grainfull.mvp.ui.widget.aircraftwar.AircraftWar;

/**
 * 奖品
 */
public class Award extends AutoSprite {
    public static int STATUS_DOWN1 = 1;
    public static int STATUS_UP2 = 2;
    public static int STATUS_DOWN3 = 3;

    private int status = STATUS_DOWN1;

    public Award(Bitmap bitmap) {
        super(bitmap);
        setSpeed(7);
    }

    @Override
    protected void afterDraw(Canvas canvas, Paint paint, AircraftWar gameView) {
        //在afterDraw中不调用super.afterDraw方法
        if (!isDestroyed()) {
            //在绘制一定次数后要改变方向或速度
            int canvasHeight = canvas.getHeight();
            if (status != STATUS_DOWN3) {
                float maxY = getY() + getHeight();
                if (status == STATUS_DOWN1) {
                    //第一次向下
                    if (maxY >= canvasHeight * 0.25) {
                        //当第一次下降到临界值时改变方向，向上
                        setSpeed(-5);
                        status = STATUS_UP2;
                    }
                } else if (status == STATUS_UP2) {
                    //第二次向上
                    if (maxY + this.getSpeed() <= 0) {
                        //第二次上升到临界值时改变方向，向下
                        setSpeed(13);
                        status = STATUS_DOWN3;
                    }
                }
            }
            if (status == STATUS_DOWN3) {
                if (getY() >= canvasHeight) {
                    destroy();
                }
            }
        }
    }
}