/**
 * Copyright (C) 2016 Baidu Inc. All rights reserved.
 */
package com.baidu.idl.face.platform.ui.utils;

import android.graphics.Point;
import android.hardware.Camera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * 相机配置
 */
public final class CameraPreviewUtils {

    private static final String TAG = CameraPreviewUtils.class.getSimpleName();
    private static final int MIN_PREVIEW_PIXELS = 640 * 480;
    private static final int MAX_PREVIEW_PIXELS = 1920 * 1080;

    public static Point getBestPreview(Camera.Parameters parameters, Point screenResolution) {

        List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes();
        if (rawSupportedSizes == null) {
            Camera.Size defaultSize = parameters.getPreviewSize();
            return new Point(640, 480);
            // return new Point(defaultSize.width, defaultSize.height);
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
            } else if (realHeight > realWidth) {  // 过滤其横屏分辨率
                it.remove();
                continue;
            } else if (realHeight % 2 != 0 || realWidth % 2 != 0) {
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
            Camera.Size defaultSize = parameters.getPreviewSize();
            // return new Point(defaultSize.width, defaultSize.height);
            return new Point(640, 480);
        }
    }
}
