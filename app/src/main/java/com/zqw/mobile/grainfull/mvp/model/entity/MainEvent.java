package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * 包名： com.zqw.mobile.recycling.model
 * 对象名： MainEvent
 * 描述：首页监听
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2018/3/9 17:52
 */

public class MainEvent {
    public MainEvent() {
    }

    public MainEvent(int code) {
        this.code = code;
    }

    public MainEvent(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public MainEvent(int code, int position) {
        this.code = code;
        this.position = position;
    }


    public MainEvent(int code, int type, int position) {
        this.code = code;
        this.type = type;
        this.position = position;
    }

    /**
     * 刷新回收订单首页
     */
    public MainEvent(int code, String dayRecycle, String monthRecycle, String totalRecovery, String unitRecycle) {
        this.code = code;
        this.dayRecycle = dayRecycle;
        this.monthRecycle = monthRecycle;
        this.totalRecovery = totalRecovery;
        this.unitRecycle = unitRecycle;
    }

    /**
     * 0 = 登录成功；
     * 1 = 打开筛选；
     * 2 = 打开侧滑；
     * 3 = 切换城市；
     * 6 = 签到-切换类型；
     * 7 = 订单-切换类型；
     */
    private int code = 0;
    private String msg = "";
    private int position = 0;
    private int type = 0;


    // 当日回收量(重量)
    private String dayRecycle;
    // 当月回收量(重量)
    private String monthRecycle;
    // 总回收量
    private String totalRecovery;
    // 单位
    private String unitRecycle;

    public String getDayRecycle() {
        return dayRecycle;
    }

    public void setDayRecycle(String dayRecycle) {
        this.dayRecycle = dayRecycle;
    }

    public String getMonthRecycle() {
        return monthRecycle;
    }

    public void setMonthRecycle(String monthRecycle) {
        this.monthRecycle = monthRecycle;
    }

    public String getTotalRecovery() {
        return totalRecovery;
    }

    public void setTotalRecovery(String totalRecovery) {
        this.totalRecovery = totalRecovery;
    }

    public String getUnitRecycle() {
        return unitRecycle;
    }

    public void setUnitRecycle(String unitRecycle) {
        this.unitRecycle = unitRecycle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
