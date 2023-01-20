package com.zqw.mobile.grainfull.app.utils;

/**
 * 华容道view中使用
 */
public class KlotskiPoint {
    public KlotskiPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private int x=0;
    private int y=0;
}
