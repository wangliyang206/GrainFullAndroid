package com.zqw.mobile.grainfull.app.tts.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Auth {
    private static volatile Auth ourInstance;

    private String appId;

    private String appKey;

    private String secretKey;

    private String sn; // 收费纯离线版本需要序列号，在线版本不需要

    private Auth(Context context) {
        Properties prop = load(context);
        String applicationId = getProperty(prop, "applicationId");

        if (!context.getPackageName().equals(applicationId)) {
            throw new AuthCheckException("包名不一致，请在app/build.gradle 里 修改defaultConfig.applicationId。\n\n"
                    + "auth.properties里写的包名是：'" + applicationId
                    + "' ; 实际app的包名是：'" + context.getPackageName() + "'");
        }

        appId = getProperty(prop, "appId");
        appKey = getProperty(prop, "appKey");
        secretKey = getProperty(prop, "secretKey");
        sn = prop.getProperty("sn"); //  收费纯离线版本需要序列号，纯在线版本不需要
    }

    public static Auth getInstance(Context context) {
        if (ourInstance == null) {
            synchronized (Auth.class) {
                ourInstance = new Auth(context);
            }
        }
        return ourInstance;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getSn() {
        return sn;
    }

    public boolean hasSn() {
        return sn != null;
    }

    private String getProperty(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new AuthCheckException("在 assets/auth.properties里没有设置 " + key);
        }
        return value.trim();
    }

    private Properties load(Context context) {
        try {
            InputStream is = context.getAssets().open("auth.properties");
            Properties prop = new Properties();
            prop.load(is);
            is.close();
            return prop;
        } catch (IOException e) {
            e.printStackTrace();
            throw new AuthCheckException(e);
        }
    }

    public static class AuthCheckException extends RuntimeException {
        public AuthCheckException(String message) {
            super(message);
        }

        public AuthCheckException(Throwable cause) {
            super(cause);
        }
    }
}
