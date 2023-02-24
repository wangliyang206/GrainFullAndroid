package com.zqw.mobile.grainfull.mvp.ui.widget.card;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.widget.card
 * @ClassName: CardShadowDistanceFunc
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/2/24 11:36
 */
public class CardShadowDistanceFunc extends BaseFuncImpl {
    public float execute(float inParam) {
        if (inParam < this.getInParamMin()) {
            return 10.0F;
        } else if (inParam > this.getInParamMax()) {
            return this.getOutParamMin();
        } else {
            return inParam >= 0.0F && inParam < 90.0F ? 10.0F + inParam * (this.getOutParamMax() - 10.0F) / 90.0F : this.getOutParamMax() + (inParam - 90.0F) * (this.getOutParamMin() - this.getOutParamMax()) / 90.0F;
        }
    }
}
