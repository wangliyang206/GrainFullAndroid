package com.zqw.mobile.grainfull.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.baidu.idl.face.platform.FaceStatusNewEnum;
import com.baidu.idl.face.platform.model.ImageInfo;
import com.baidu.idl.face.platform.ui.FaceDetectActivity;
import com.baidu.idl.face.platform.ui.utils.IntentUtils;
import com.zqw.mobile.grainfull.app.dialog.TimeoutDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:百度AI - 人脸采集 - 人脸检测指数
 * <p>
 * Created on 2022/07/13 18:10
 *
 * @author 赤槿
 * module name is BaiduFaceDetectExpActivity
 */
public class BaiduFaceDetectExpActivity extends FaceDetectActivity implements TimeoutDialog.OnTimeoutDialogClickListener {

    private TimeoutDialog mTimeoutDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 添加至销毁列表
        BaiduFaceRecognitionActivity.addDestroyActivity(this, "BaiduFaceDetectExpActivity");
    }

    @Override
    public void onDetectCompletion(FaceStatusNewEnum status, String message,
                                   HashMap<String, ImageInfo> base64ImageCropMap,
                                   HashMap<String, ImageInfo> base64ImageSrcMap) {
        super.onDetectCompletion(status, message, base64ImageCropMap, base64ImageSrcMap);
        if (status == FaceStatusNewEnum.OK && mIsCompletion) {
            // 获取最优图片
            getBestImage(base64ImageCropMap, base64ImageSrcMap);
        } else if (status == FaceStatusNewEnum.DetectRemindCodeTimeout) {
            if (mViewBg != null) {
                mViewBg.setVisibility(View.VISIBLE);
            }
            showMessageDialog();
        }
    }

    /**
     * 获取最优图片
     * @param imageCropMap 抠图集合
     * @param imageSrcMap  原图集合
     */
    private void getBestImage(HashMap<String, ImageInfo> imageCropMap, HashMap<String, ImageInfo> imageSrcMap) {
        String bmpStr = null;
        // 将抠图集合中的图片按照质量降序排序，最终选取质量最优的一张抠图图片
        if (imageCropMap != null && imageCropMap.size() > 0) {
            List<Map.Entry<String, ImageInfo>> list1 = new ArrayList<>(imageCropMap.entrySet());
            Collections.sort(list1, (o1, o2) -> {
                String[] key1 = o1.getKey().split("_");
                String score1 = key1[2];
                String[] key2 = o2.getKey().split("_");
                String score2 = key2[2];
                // 降序排序
                return Float.valueOf(score2).compareTo(Float.valueOf(score1));
            });

            // 获取抠图中的加密或非加密的base64
//            int secType = mFaceConfig.getSecType();
//            String base64;
//            if (secType == 0) {
//                base64 = list1.get(0).getValue().getBase64();
//            } else {
//                base64 = list1.get(0).getValue().getSecBase64();
//            }
        }

        // 将原图集合中的图片按照质量降序排序，最终选取质量最优的一张原图图片
        if (imageSrcMap != null && imageSrcMap.size() > 0) {
            List<Map.Entry<String, ImageInfo>> list2 = new ArrayList<>(imageSrcMap.entrySet());
            Collections.sort(list2, (o1, o2) -> {
                String[] key1 = o1.getKey().split("_");
                String score1 = key1[2];
                String[] key2 = o2.getKey().split("_");
                String score2 = key2[2];
                // 降序排序
                return Float.valueOf(score2).compareTo(Float.valueOf(score1));
            });
            bmpStr = list2.get(0).getValue().getBase64();

            // 获取原图中的加密或非加密的base64
//            int secType = mFaceConfig.getSecType();
//            String base64;
//            if (secType == 0) {
//                base64 = list2.get(0).getValue().getBase64();
//            } else {
//                base64 = list2.get(0).getValue().getSecBase64();
//            }
        }

        // 页面跳转
        IntentUtils.getInstance().setBitmap(bmpStr);
        Intent intent = new Intent(this, BaiduFaceCollectionSuccessActivity.class);
        intent.putExtra("destroyType", "BaiduFaceDetectExpActivity");
        startActivity(intent);
    }

    private void showMessageDialog() {
        mTimeoutDialog = new TimeoutDialog(this);
        mTimeoutDialog.setDialogListener(this);
        mTimeoutDialog.setCanceledOnTouchOutside(false);
        mTimeoutDialog.setCancelable(false);
        mTimeoutDialog.show();
        onPause();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onRecollect() {
        if (mTimeoutDialog != null) {
            mTimeoutDialog.dismiss();
        }
        if (mViewBg != null) {
            mViewBg.setVisibility(View.GONE);
        }
        onResume();
    }

    @Override
    public void onReturn() {
        if (mTimeoutDialog != null) {
            mTimeoutDialog.dismiss();
        }
        finish();
    }
}