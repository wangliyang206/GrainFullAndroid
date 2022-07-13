package com.baidu.idl.face.platform.ui.utils;

import android.content.Context;
import android.util.Log;

import com.baidu.idl.face.platform.FaceSDKManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * 数据加密请求
 * Created by v_liujialu01 on 2020/4/15.
 */

public class SecRequest {
    private static final String TAG = SecRequest.class.getSimpleName();

    private static final String REQUEST_URL = "http://10.138.32.176:8518/api/v3/person/verify_sec?appid=7758258";

//    JSONObject jsonObject = new JSONObject();
//    jsonObject.put("image_type", "BASE64");                           // 图片类型
//    jsonObject.put("image", secBase64);                               // 图片加密字符串
//    jsonObject.put("id_card_number", "101111111111111111");           // 身份证号
//    jsonObject.put("name", URLDecoder.decode("张三", "UTF-8"));       // 姓名
//    jsonObject.put("quality_control", "NONE");                        // 质量控制
//    jsonObject.put("liveness_control", "NONE");                       // 活体控制
//    jsonObject.put("risk_identify", true);                            // 是否开启风控人证
//    jsonObject.put("zid", LH.gzfi(FaceDetectActivity.this, null, 5001, null));   // zid
//    jsonObject.put("ip", "172.30.154.173");                           // ip
//    jsonObject.put("phone", "13000000000");                           // 手机号
//    jsonObject.put("image_sec", true);                                // 是否进行图片加密
//    jsonObject.put("app", "Android");                                 // 应用类型
//    jsonObject.put("ev", "smrz");                                     // 用途

    public static void sendMessage(final Context context, final String secBase64, final int secType) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("image_type", "BASE64");
                    jsonObject.put("image", secBase64);
                    jsonObject.put("id_card_number", "101111111111111111");
                    jsonObject.put("name", URLDecoder.decode("张三", "UTF-8"));
                    jsonObject.put("quality_control", "NONE");
                    jsonObject.put("liveness_control", "NONE");
                    // TODO：是否开启风控认证，但如果sceType为false时，该参数必须传入false
                    jsonObject.put("risk_identify", false);
                    jsonObject.put("zid", FaceSDKManager.getInstance().getZid(context));
                    jsonObject.put("ip", "172.30.154.173");
                    jsonObject.put("phone", "13000000000");
                    if (secType == 0) {
                        jsonObject.put("image_sec", false);
                    } else if (secType == 1) {
                        jsonObject.put("image_sec", true);
                    }
                    jsonObject.put("app", "Android");
                    jsonObject.put("ev", "smrz");

                    requestPost(jsonObject.toString(), REQUEST_URL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private static String requestPost(String paramStr, String requestUrl) {
        HttpURLConnection conn = null; // 连接对象
        int responseCode = -1;
        String resultData = null;

        OutputStream outputStream = null;
        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;
        try {
            URL url = new URL(requestUrl);
            conn = (HttpURLConnection) url.openConnection();
            System.setProperty("sun.net.client.defaultConnectTimeout", "8000");
            System.setProperty("sun.net.client.defaultReadTimeout", "8000");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();
            outputStream = conn.getOutputStream();
            outputStream.write(paramStr.getBytes());
            outputStream.flush();
            outputStream.close();
            responseCode = conn.getResponseCode();
            Log.e(TAG, "request code " + responseCode);
            if (HttpURLConnection.HTTP_OK == responseCode) {
                inputStream = conn.getInputStream();
                byte[] buffer = new byte[1024];
                baos = new ByteArrayOutputStream();
                int len = -1;
                while ((len = inputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                byte[] b = baos.toByteArray();
                resultData = new String(b, "utf-8");
                baos.flush();
                Log.e(TAG, "request data " + resultData);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "IOException " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (baos != null) {
                    baos.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultData;
    }
}
