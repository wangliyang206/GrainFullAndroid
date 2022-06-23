package com.jess.arms.cj;

/**
 * 包名： com.cj.mobile.common.model
 * 对象名： ClientInfo
 * 描述：APP设备信息
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/3/27 13:57
 */

public class ClientInfo {

    /* 手机号（有则填写） */
    private String cell;

    /* 设备ID */
    private String deviceid;

    /* SIM 卡序列号 */
    private String simid;

    /* 操作系统名称：android、iphone、ipad */
    private String os;

    /* 操作系统版本 */
    private String osver;

    /* 客户端版本号 */
    private String vercode;

    /* 客户端版本名称 */
    private String vername;

    /* 主屏像素(高) */
    private String ppiheight;

    /* 主屏像素(宽) */
    private String ppiwidth;

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getSimid() {
        return simid;
    }

    public void setSimid(String simid) {
        this.simid = simid;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsver() {
        return osver;
    }

    public void setOsver(String osver) {
        this.osver = osver;
    }

    public String getVercode() {
        return vercode;
    }

    public void setVercode(String vercode) {
        this.vercode = vercode;
    }

    public String getVername() {
        return vername;
    }

    public void setVername(String vername) {
        this.vername = vername;
    }

    public String getPpiheight() {
        return ppiheight;
    }

    public void setPpiheight(String ppiheight) {
        this.ppiheight = ppiheight;
    }

    public String getPpiwidth() {
        return ppiwidth;
    }

    public void setPpiwidth(String ppiwidth) {
        this.ppiwidth = ppiwidth;
    }
}
