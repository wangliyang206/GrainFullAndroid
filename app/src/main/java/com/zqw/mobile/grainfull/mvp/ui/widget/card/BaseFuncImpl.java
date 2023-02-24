package com.zqw.mobile.grainfull.mvp.ui.widget.card;

import org.jetbrains.annotations.NotNull;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.widget.card
 * @ClassName: BaseFuncImpl
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/2/24 11:33
 */
public class BaseFuncImpl implements IFunc {
    private float outParamMax;
    private float outParamMin;
    private float inParamMin;
    private float initValue;
    private float inParamMax;

    public float getOutParamMax() {
        return this.outParamMax;
    }

    public void setOutParamMax(float var1) {
        this.outParamMax = var1;
    }

    public float getOutParamMin() {
        return this.outParamMin;
    }

    public void setOutParamMin(float var1) {
        this.outParamMin = var1;
    }

    public float getInParamMin() {
        return this.inParamMin;
    }

    public void setInParamMin(float var1) {
        this.inParamMin = var1;
    }

    public float getInitValue() {
        return this.initValue;
    }

    public void setInitValue(float var1) {
        this.initValue = var1;
    }

    public float getInParamMax() {
        return this.inParamMax;
    }

    public void setInParamMax(float var1) {
        this.inParamMax = var1;
    }

    public float execute(float inParam) {
        return 0.0F;
    }

    @NotNull
    public String toString() {
        return "BaseFuncImpl(initValue=" + this.getInitValue() + ", inParamMax=" + this.getInParamMax() + ", inParamMin=" + this.getInParamMin() + ')';
    }

    public BaseFuncImpl() {
    }

    public BaseFuncImpl(float initValue, float inParamMax) {
        this.setInitValue(initValue);
        this.setInParamMax(inParamMax);
    }
}
