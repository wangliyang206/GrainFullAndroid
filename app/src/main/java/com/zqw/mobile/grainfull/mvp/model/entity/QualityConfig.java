package com.zqw.mobile.grainfull.mvp.model.entity;

import org.json.JSONObject;

public class QualityConfig {
    // 光照
    private float minIllum;  // 最大
    private float maxIllum;  // 最小
    // 模糊
    private float blur;
    // 遮挡
    private float leftEyeOcclusion;       // 左眼
    private float rightEyeOcclusion;      // 右眼
    private float noseOcclusion;          // 鼻子
    private float mouseOcclusion;         // 嘴巴
    private float leftContourOcclusion;   // 左脸颊
    private float rightContourOcclusion;  // 右脸颊
    private float chinOcclusion;          // 下巴
    // 姿态角
    private int pitch;   // 上下角
    private int yaw;     // 左右角
    private int roll;    // 旋转角

    public float getMinIllum() {
        return minIllum;
    }

    public void setMinIllum(float minIllum) {
        this.minIllum = minIllum;
    }

    public float getMaxIllum() {
        return maxIllum;
    }

    public void setMaxIllum(float maxIllum) {
        this.maxIllum = maxIllum;
    }

    public float getBlur() {
        return blur;
    }

    public void setBlur(float blur) {
        this.blur = blur;
    }

    public float getLeftEyeOcclusion() {
        return leftEyeOcclusion;
    }

    public void setLeftEyeOcclusion(float leftEyeOcclusion) {
        this.leftEyeOcclusion = leftEyeOcclusion;
    }

    public float getRightEyeOcclusion() {
        return rightEyeOcclusion;
    }

    public void setRightEyeOcclusion(float rightEyeOcclusion) {
        this.rightEyeOcclusion = rightEyeOcclusion;
    }

    public float getNoseOcclusion() {
        return noseOcclusion;
    }

    public void setNoseOcclusion(float noseOcclusion) {
        this.noseOcclusion = noseOcclusion;
    }

    public float getMouseOcclusion() {
        return mouseOcclusion;
    }

    public void setMouseOcclusion(float mouseOcclusion) {
        this.mouseOcclusion = mouseOcclusion;
    }

    public float getLeftContourOcclusion() {
        return leftContourOcclusion;
    }

    public void setLeftContourOcclusion(float leftContourOcclusion) {
        this.leftContourOcclusion = leftContourOcclusion;
    }

    public float getRightContourOcclusion() {
        return rightContourOcclusion;
    }

    public void setRightContourOcclusion(float rightContourOcclusion) {
        this.rightContourOcclusion = rightContourOcclusion;
    }

    public float getChinOcclusion() {
        return chinOcclusion;
    }

    public void setChinOcclusion(float chinOcclusion) {
        this.chinOcclusion = chinOcclusion;
    }

    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public int getYaw() {
        return yaw;
    }

    public void setYaw(int yaw) {
        this.yaw = yaw;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    /**
     * 解析json文件的内容
     * @param jsonObject  json数据
     */
    public void parseFromJSONObject(JSONObject jsonObject) {
        minIllum = (float) jsonObject.optDouble("minIllum");
        maxIllum = (float) jsonObject.optDouble("maxIllum");
        blur = (float) jsonObject.optDouble("blur");
        leftEyeOcclusion = (float) jsonObject.optDouble("leftEyeOcclusion");
        rightEyeOcclusion = (float) jsonObject.optDouble("rightEyeOcclusion");
        noseOcclusion = (float) jsonObject.optDouble("noseOcclusion");
        mouseOcclusion = (float) jsonObject.optDouble("mouseOcclusion");
        leftContourOcclusion = (float) jsonObject.optDouble("leftContourOcclusion");
        rightContourOcclusion = (float) jsonObject.optDouble("rightContourOcclusion");
        chinOcclusion = (float) jsonObject.optDouble("chinOcclusion");
        pitch = jsonObject.optInt("pitch");
        yaw = jsonObject.optInt("yaw");
        roll = jsonObject.optInt("roll");
    }
}
