package com.zqw.mobile.grainfull.app.service;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import static com.baidu.location.BDLocation.BDLOCATION_GCJ02_TO_BD09LL;

/**
 * 百度定位服务
 *
 * @author baidu
 */
public class LocationService {
    private LocationClient client = null;
    private LocationClientOption mOption, DIYoption;
    private Object objLock = new Object();

    /**
     * 定位频率(1分钟)
     */
    public static final int SPAN_MAP = 1 * 60 * 1000;

    /**
     * 普通界面定位频率(20分钟)
     */
    public static final int SPAN_NORMAL = 20 * 60 * 1000;

    /***
     * 构造方法
     *
     * @param locationContext 上下文(句柄)
     */
    public LocationService(Context locationContext) {
        synchronized (objLock) {
            if (client == null) {
                client = new LocationClient(locationContext);
                client.setLocOption(getDefaultLocationClientOption());
            }
        }
    }

    /***
     * 注册监听，监听定位结果回调
     *
     * @param listener 定位结果回调
     * @return
     */
    public boolean registerListener(BDAbstractLocationListener listener) {
        boolean isSuccess = false;
        if (listener != null) {
            client.registerLocationListener(listener);
            isSuccess = true;
        }
        return isSuccess;
    }

    /**
     * 注销掉监听
     *
     * @param listener
     */
    public void unregisterListener(BDAbstractLocationListener listener) {
        if (listener != null) {
            client.unRegisterLocationListener(listener);
        }
    }

    /***
     * 配置定位信息
     *
     * @param option 定位信息
     * @return isSuccessSetOption
     */
    public boolean setLocationOption(LocationClientOption option) {
        boolean isSuccess = false;
        if (option != null) {
            if (client.isStarted())
                client.stop();
            DIYoption = option;
            client.setLocOption(option);
        }
        return isSuccess;
    }

    /**
     * 对外提供定位配置信息
     *
     * @return
     */
    public LocationClientOption getOption() {
        return DIYoption;
    }

    /***
     * 默认配置信息
     *
     * @return DefaultLocationClientOption
     */
    public LocationClientOption getDefaultLocationClientOption() {
        if (mOption == null) {
            mOption = new LocationClientOption();
            mOption.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            mOption.setCoorType(BDLOCATION_GCJ02_TO_BD09LL);//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
            // 1000 = 1秒，60000 = 1分钟；600000 = 10分钟；
            mOption.setScanSpan(SPAN_MAP);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
            mOption.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
            mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S/1次频率输出GPS结果
            mOption.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            mOption.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
            mOption.setOpenGps(true);//可选，默认false,设置是否使用gps
            mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        }
        return mOption;
    }

    /**
     * 重新设置定位间隔
     */
    public LocationClientOption setScanSpan(int val) {
        if (mOption != null)
            mOption.setScanSpan(val);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        return mOption;
    }

    /**
     * 开启定位
     */
    public void start() {
        synchronized (objLock) {
            if (client != null && !client.isStarted()) {
                client.start();
            }
        }
    }

    /**
     * 关闭定位
     */
    public void stop() {
        synchronized (objLock) {
            if (client != null && client.isStarted()) {
                client.stop();
            }
        }
    }

    /**
     * 开启定位
     */
    public void restart() {
        synchronized (objLock) {
            if (client != null && !client.isStarted()) {
                client.restart();
            }
        }
    }

    /**
     * 获取热点状态
     *
     * @return
     */
    public boolean requestHotSpotState() {

        return client.requestHotSpotState();

    }

}
