package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * Created by chawei on 2018/4/29.
 */

public class CoinInfo {

    /**
     * name         string 名称
     * priceLast    string	最新价
     * riseRate24	string	24小时涨幅,负值前面有负号
     * vol24	    string	24小时成交量
     * close	    string	收盘价
     * open	        string	开盘价
     * bid	        string	买一价
     * ask	        string	卖一价
     * mountPercent	string	量比
     */
    public String name;
    public String priceLast;
    public String riseRate24;
    public String vol24;
    public String close;
    public String open;
    public String bid;
    public String ask;
    public String amountPercent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriceLast() {
        return priceLast;
    }

    public void setPriceLast(String priceLast) {
        this.priceLast = priceLast;
    }

    public String getRiseRate24() {
        return riseRate24;
    }

    public void setRiseRate24(String riseRate24) {
        this.riseRate24 = riseRate24;
    }

    public String getVol24() {
        return vol24;
    }

    public void setVol24(String vol24) {
        this.vol24 = vol24;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getAmountPercent() {
        return amountPercent;
    }

    public void setAmountPercent(String amountPercent) {
        this.amountPercent = amountPercent;
    }
}
