package com.zqw.mobile.grainfull.mvp.ui.widget.card;

public interface IFunc {
    float getInitValue();

    void setInitValue(float var1);

    float getInParamMax();

    void setInParamMax(float var1);

    float getInParamMin();

    void setInParamMin(float var1);

    float getOutParamMax();

    void setOutParamMax(float var1);

    float getOutParamMin();

    void setOutParamMin(float var1);

    float execute(float var1);
}
