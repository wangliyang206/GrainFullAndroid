package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: CardFlipping
 * @Description: 卡牌消消乐
 * @Author: WLY
 * @CreateDate: 2023/7/10 17:23
 */
public class CardFlipping {
    public CardFlipping() {
    }

    public CardFlipping(int id, int sign, int imageBg, int imageContent, boolean isDisplayFront, boolean isDisappear) {
        this.id = id;
        this.sign = sign;
        this.imageBg = imageBg;
        this.imageContent = imageContent;
        this.isDisplayFront = isDisplayFront;
        this.isDisappear = isDisappear;
    }

    // id
    private int id;
    // 标志
    private int sign;
    // 背景
    private int imageBg;
    // 图片路径(url/path)
    private int imageContent;

    // 是否显示正面
    private boolean isDisplayFront;
    // 是否消失
    private boolean isDisappear;

    public int getImageBg() {
        return imageBg;
    }

    public void setImageBg(int imageBg) {
        this.imageBg = imageBg;
    }

    public boolean isDisplayFront() {
        return isDisplayFront;
    }

    public void setDisplayFront(boolean displayFront) {
        isDisplayFront = displayFront;
    }

    public boolean isDisappear() {
        return isDisappear;
    }

    public void setDisappear(boolean disappear) {
        isDisappear = disappear;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public int getImageContent() {
        return imageContent;
    }

    public void setImageContent(int imageContent) {
        this.imageContent = imageContent;
    }
}
