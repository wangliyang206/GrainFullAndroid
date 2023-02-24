package com.zqw.mobile.grainfull.mvp.ui.widget.card;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.widget.card
 * @ClassName: CardShadowSizeFunc
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/2/24 11:39
 */
public class CardShadowSizeFunc extends BaseFuncImpl {
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
