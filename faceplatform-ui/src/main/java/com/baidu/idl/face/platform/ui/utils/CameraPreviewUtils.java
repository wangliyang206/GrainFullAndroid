/**
 * Copyright (C) 2016 Baidu Inc. All rights reserved.
 */
package com.baidu.idl.face.platform.ui.utils;

import android.graphics.Point;
import android.hardware.Camera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 相机配置
 */
public final class CameraPreviewUtils {

    private static final String TAG = CameraPreviewUtils.class.getSimpleName();
    private static final int MIN_PREVIEW_PIXELS = 640 * 480;
    private static final int MAX_PREVIEW_PIXELS = 1920 * 1080;

    /**
     * 选取最优的Camera分辨率（与屏幕分辨率宽高比最接近）
     * @param parameters          Camera参数
     * @param screenResolution    屏幕分辨率
     * @return
     */
    public static Point getBestPreview(Camera.Parameters parameters, Point screenResolution) {

        List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes();
        if (rawSupportedSizes == null) {
            return new Point(640, 480);
        }

        List<Camera.Size> supportedPictureSizes = new ArrayList<Camera.Size>(rawSupportedSizes);
        Collections.sort(supportedPictureSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });

        final double screenAspectRatio = (screenResolution.x > screenResolution.y) ?
                ((double) screenResolution.x / (double) screenResolution.y) :
                ((double) screenResolution.y / (double) screenResolution.x);

        Camera.Size selectedSize = null;
        double selectedMinus = -1;
        double selectedPreviewSize = 0;
        Iterator<Camera.Size> it = supportedPictureSizes.iterator();
        while (it.hasNext()) {
            Camera.Size supportedPreviewSize = it.next();
            int realWidth = supportedPreviewSize.width;
            int realHeight = supportedPreviewSize.height;
//            Log.e(TAG, "preview size " + realWidth + " " + realHeight);
            if (realWidth * realHeight < MIN_PREVIEW_PIXELS) {
                it.remove();
                continue;
            } else if (realWidth * realHeight > MAX_PREVIEW_PIXELS) {
                it.remove();
                continue;
            } else if (realHeight % 4 != 0 || realWidth % 4 != 0) {
                it.remove();
                continue;
            } else {
                double aRatio = (supportedPreviewSize.width > supportedPreviewSize.height) ?
                        ((double) supportedPreviewSize.width / (double) supportedPreviewSize.height) :
                        ((double) supportedPreviewSize.height / (double) supportedPreviewSize.width);
                double minus = Math.abs(aRatio - screenAspectRatio);

                boolean selectedFlag = false;
                if ((selectedMinus == -1 && minus <= 0.25f)
                        || (selectedMinus >= minus && minus <= 0.25f)) {
                    selectedFlag = true;
                }
                if (selectedFlag) {
                    selectedMinus = minus;
                    selectedSize = supportedPreviewSize;
                    selectedPreviewSize = realWidth * realHeight;
                }
            }
        }

        if (selectedSize != null) {
            Camera.Size preview = selectedSize;
            return new Point(preview.width, preview.height);
        } else {
            return new Point(640, 480);
        }
    }

    /**
     * 选取最优视频录制分辨率（针对魅蓝Note5，保证与Camera分辨率相同）
     * @param parameters Camera参数
     * @return
     */
    public static Point getBestVideoForSameSize(Camera.Parameters parameters) {

        List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes();  // camera分辨率
        List<Camera.Size> videoSupportedSizes = parameters.getSupportedVideoSizes();  // video分辨率

        List<Camera.Size> sameSizes = new ArrayList<>();

        if (videoSupportedSizes == null || rawSupportedSizes == null) {
            return new Point(640, 480);
        }

        // 合并两个集合
        videoSupportedSizes.addAll(rawSupportedSizes);

        HashMap<Camera.Size, Integer> mapList = new HashMap<>();
        for (Camera.Size size : videoSupportedSizes) {
            int count = 0;
            if (mapList.get(size) != null) {
                count = mapList.get(size) + 1;
            }
            mapList.put(size, count);
        }
        // 找出重复分辨率
        for (Camera.Size key : mapList.keySet()) {
            if (mapList.get(key) != null && mapList.get(key) > 0) {
                sameSizes.add(key);
            }
        }

        // 从低到高排序
        Collections.sort(sameSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return 1;
                }
                if (bPixels > aPixels) {
                    return -1;
                }
                return 0;
            }
        });

        Iterator<Camera.Size> it = sameSizes.iterator();
        while (it.hasNext()) {
            Camera.Size supportedPreviewSize = it.next();
            int realWidth = supportedPreviewSize.width;
            int realHeight = supportedPreviewSize.height;
            if (realWidth * realHeight < MIN_PREVIEW_PIXELS) {
                it.remove();
                continue;
            } else if (realWidth * realHeight > MAX_PREVIEW_PIXELS) {
                it.remove();
                continue;
            } else if (realHeight % 4 != 0 || realWidth % 4 != 0) {
                it.remove();
                continue;
            } else {
                return new Point(sameSizes.get(0).width, sameSizes.get(0).height);
            }
        }

        return new Point(640, 480);
    }

    public static Point getBestVideoPreview(Camera.Parameters parameters) {

        List<Camera.Size> rawSupportedSizes = parameters.getSupportedVideoSizes();
        if (rawSupportedSizes == null) {
            return new Point(640, 480);
        }

        List<Camera.Size> supportedPictureSizes = new ArrayList<Camera.Size>(rawSupportedSizes);
        Collections.sort(supportedPictureSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });

        for (int i = 0; i < supportedPictureSizes.size(); i++) {
            if (supportedPictureSizes.get(i).width != 640 || supportedPictureSizes.get(i).height != 480) {
                continue;
            }
            // 选取相对较低的合适分辨率
            return new Point(supportedPictureSizes.get(supportedPictureSizes.size() - 4).width,
                    supportedPictureSizes.get(supportedPictureSizes.size() - 4).height);
        }
        return new Point(640, 480);
    }
}
