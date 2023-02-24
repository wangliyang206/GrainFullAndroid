package com.zqw.mobile.grainfull.mvp.ui.widget.card;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.widget.card
 * @ClassName: CardRotateFunc
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/2/24 11:35
 */
public class CardRotateFunc extends BaseFuncImpl {
    public float execute(float inParam) {
        if (inParam > this.getInParamMax()) {
            return this.getOutParamMin();
        } else if (inParam < this.getInParamMin()) {
            return this.getOutParamMax();
        } else {
            float rate = (this.getOutParamMin() - this.getOutParamMax()) / (this.getInParamMax() - this.getInParamMin());
            return this.getOutParamMax() + inParam * rate;
        }
    }
}