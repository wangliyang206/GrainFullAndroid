package com.qiangxi.checkupdatelibrary.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;

import java.io.File;

import static android.app.Notification.VISIBILITY_SECRET;
import static android.app.PendingIntent.getActivity;
import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;

/**
 * 作者 任强强 on 2016/10/18 11:36.
 */

public class NotificationUtil extends ContextWrapper {
    private NotificationManager mManager;

    public static final String CHANNEL_ID = "default";
    private static final String CHANNEL_NAME = "Default Channel";
    private static final int notificationId = 0;

    public NotificationUtil(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        //是否绕过请勿打扰模式
        channel.canBypassDnd();
        //闪光灯
        channel.enableLights(true);
        //锁屏显示通知
        channel.setLockscreenVisibility(VISIBILITY_SECRET);
        //闪关灯的灯光颜色
        channel.setLightColor(Color.RED);
        //桌面launcher的消息角标
        channel.canShowBadge();
        //是否允许震动
        channel.enableVibration(true);
        //获取系统通知响铃声音的配置
        channel.getAudioAttributes();
        //获取通知取到组
        channel.getGroup();
        //设置可绕过  请勿打扰模式
        channel.setBypassDnd(true);
        //设置震动模式
        channel.setVibrationPattern(new long[]{100, 100, 200});
        //是否会有灯光
        channel.shouldShowLights();
        getManager().createNotificationChannel(channel);
    }

    private NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    /**
     * 展示下载成功通知
     *
     * @param context               上下文
     * @param file                  下载的apk文件
     * @param notificationIconResId 通知图标资源id
     * @param notificationTitle     通知标题
     * @param isCanClear            通知是否可被清除
     */
    public void showDownloadSuccessNotification(Context context, File file, int notificationIconResId, String notificationTitle, String notificationContent, boolean isCanClear) {
        Intent installIntent = new Intent();
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installIntent.setAction(Intent.ACTION_VIEW);
        Uri uri;
        //当前设备系统版本在7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider.FileProvider", file);
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        NotificationCompat.Builder builder = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            // 通知兼容安卓8.0以下版本
            builder = new NotificationCompat.Builder(context);
            builder.setPriority(PRIORITY_DEFAULT);
        }
        builder.setAutoCancel(false).setShowWhen(true).setSmallIcon(notificationIconResId).setContentTitle(notificationTitle).setContentText(notificationContent);
        PendingIntent pendingIntent = getActivity(context, 0, installIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();// 获取一个Notification
        notification.defaults = Notification.DEFAULT_SOUND;// 设置为默认的声音
        notification.flags = isCanClear ? Notification.FLAG_ONLY_ALERT_ONCE : Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_NO_CLEAR;
        getManager().notify(notificationId, notification);// 显示通知
    }

    /**
     * 展示实时下载进度通知
     *
     * @param context               上下文
     * @param currentProgress       当前进度
     * @param totalProgress         总进度
     * @param notificationIconResId 通知图标资源id
     * @param notificationTitle     通知标题
     * @param isCanClear            通知是否可被清除
     */
    public void showDownloadingNotification(Context context, int currentProgress, int totalProgress, int notificationIconResId, String notificationTitle, boolean isCanClear) {
        NotificationCompat.Builder builder = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            // 通知兼容安卓8.0以下版本
            builder = new NotificationCompat.Builder(context);
            builder.setPriority(PRIORITY_DEFAULT);
        }

        builder.setAutoCancel(false)
                .setShowWhen(false)
                .setSmallIcon(notificationIconResId)
                .setContentTitle(notificationTitle)
                .setProgress(totalProgress, currentProgress, false);
        // 获取一个Notification
        Notification notification = builder.build();
        // 设置为默认的声音
        notification.defaults = Notification.DEFAULT_SOUND;
        notification.flags = isCanClear ? Notification.FLAG_ONLY_ALERT_ONCE : Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_NO_CLEAR;
        // 显示通知
        getManager().notify(notificationId, notification);
    }

    /**
     * 展示下载失败通知
     *
     * @param context               上下文
     * @param notificationContent   通知内容,比如:下载失败,点击重新下载
     * @param intent                该intent用来重新下载应用
     * @param notificationIconResId 通知图标资源id
     * @param notificationTitle     通知标题
     * @param isCanClear            通知是否可被清除
     */
    public void showDownloadFailureNotification(Context context, Intent intent, int notificationIconResId, String notificationTitle, String notificationContent, boolean isCanClear) {
        NotificationCompat.Builder builder = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            // 通知兼容安卓8.0以下版本
            builder = new NotificationCompat.Builder(context);
            builder.setPriority(PRIORITY_DEFAULT);
        }
        builder.setAutoCancel(false)                                                                // 设置禁用点击信息后自动清除通知
                .setShowWhen(true)
                .setSmallIcon(notificationIconResId)
                .setContentTitle(notificationTitle)
                .setContentText(notificationContent);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();// 获取一个Notification
        notification.defaults = Notification.DEFAULT_SOUND;// 设置为默认的声音
        notification.flags = isCanClear ? Notification.FLAG_ONLY_ALERT_ONCE : Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_NO_CLEAR;
        getManager().notify(notificationId, notification);// 显示通知
    }
}
