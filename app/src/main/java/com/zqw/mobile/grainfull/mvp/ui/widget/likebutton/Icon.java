package com.zqw.mobile.grainfull.mvp.ui.widget.likebutton;

import androidx.annotation.DrawableRes;

public class Icon {

    private int onIconResourceId;
    private int offIconResourceId;

    public Icon(@DrawableRes int onIconResourceId, @DrawableRes int offIconResourceId) {
        this.onIconResourceId = onIconResourceId;
        this.offIconResourceId = offIconResourceId;
    }

    public int getOffIconResourceId() {
        return offIconResourceId;
    }

    public void setOffIconResourceId(@DrawableRes int offIconResourceId) {
        this.offIconResourceId = offIconResourceId;
    }

    public int getOnIconResourceId() {
        return onIconResourceId;
    }

    public void setOnIconResourceId(@DrawableRes int onIconResourceId) {
        this.onIconResourceId = onIconResourceId;
    }
}
