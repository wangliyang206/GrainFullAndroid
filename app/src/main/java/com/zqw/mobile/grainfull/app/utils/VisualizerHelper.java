package com.zqw.mobile.grainfull.app.utils;

import android.graphics.Color;
import android.media.audiofx.Visualizer;
import java.util.ArrayList;
import java.util.List;

/**
 * 可视化工具帮助程序
 */
@SuppressWarnings("ALL")
public class VisualizerHelper {
    //true = 镜像取值，能量高点在中间；false = 顺序取值，能量高点在左侧；
    public static boolean imageValue = true;
    public static boolean isFFT = true;
    public static final int ALPHA = 255;
    public static final int RED = 255;
    public static final int GREEN = 255;
    public static final int BLUE = 255;
    public static final int COLOR = Color.argb(ALPHA, RED - 55, GREEN - 55, BLUE - 55);
    //音频把柱(max1024)
    public static final int LUMP_COUNT = 256;
    //放大量
    public static final int LAGER_OFFSET = isFFT ? 3 : 0;


    private static VisualizerHelper helper;
    public static synchronized VisualizerHelper getInstance() {
        synchronized (VisualizerHelper.class) {
            if (helper == null) {
                helper = new VisualizerHelper();
            }
            return helper;
        }
    }

    private List<OnVisualizerEnergyCallBack> onEnergyCallBacks = new ArrayList<>();
    private final Visualizer.OnDataCaptureListener dataCaptureListener = new Visualizer.OnDataCaptureListener() {

        @Override
        public void onWaveFormDataCapture(Visualizer visualizer, final byte[] fft, int samplingRate) {
            if (!isFFT) {
                dispose(fft);
            }
        }

        @Override
        public void onFftDataCapture(Visualizer visualizer, final byte[] fft, int samplingRate) {
            if (isFFT) {
                dispose(fft);
            }
        }

    };

    public Visualizer.OnDataCaptureListener getDataCaptureListener() {
        return dataCaptureListener;
    }

    private void dispose(byte[] data) {
        float energy = 0f;
        byte[] newData = new byte[LUMP_COUNT];
        byte abs;
        if (imageValue) {
            /***************镜像取值，能量高点在中间***************/
            int total = LUMP_COUNT / 2;
            byte[] temp = new byte[total];
            for (int i = 0; i < total; i++) {
                abs = (byte) Math.abs(data[total - i]);
                temp[i] = abs < 0 ? 0 : abs;
                newData[i] = temp[i];
                energy += newData[i];
            }
            for (int i = 0; i < total; i++) {
                newData[total + i] = temp[total - 1 - i];
                energy += newData[i];
            }
        } else {
            /***************顺序取值，能量高点在左侧***************/
            for (int i = 0; i < LUMP_COUNT; i++) {
                abs = (byte) Math.abs(data[i]);
                newData[i] = abs < 0 ? (byte) LUMP_COUNT : abs;
                energy += newData[i];
            }

        }
        /**************************************************/
        for (int i = 0; i < onEnergyCallBacks.size(); i++) {
            onEnergyCallBacks.get(i).setWaveData(newData, energy);
        }
    }


    public void addCallBack(OnVisualizerEnergyCallBack onEnergyCallBack) {
        onEnergyCallBacks.add(onEnergyCallBack);
    }

    public void removeCallBack(OnVisualizerEnergyCallBack onEnergyCallBack) {
        onEnergyCallBacks.remove(onEnergyCallBack);
    }

    public interface OnVisualizerEnergyCallBack {

        void setWaveData(byte[] data, float totalEnergy);

    }
}
