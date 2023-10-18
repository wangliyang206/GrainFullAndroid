package com.zqw.mobile.grainfull.app.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import com.blankj.utilcode.util.Utils;
import com.zqw.mobile.grainfull.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import timber.log.Timber;

/**
 * 媒体存储(MediaStore) 工具类
 */
public class MediaStoreUtils {
    /**
     * 获取 文件扩展名
     *
     * @param name name
     * @return String Extension
     */
    public static String getExtension(String name) {
        int index = name.lastIndexOf(".");
        if (index > 0) {
            return name.substring(index + 1);
        }
        return "";
    }

    /**
     * 获取 文件MimeType
     *
     * @param name name
     * @return String MimeType
     */
    public static String getMimeType(String name) {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtension(name));
    }

    /**
     * 获取 Uri(根据mimeType)
     *
     * @param mimeType mimeType
     * @return Uri
     */
    public static Uri getContentUri(String mimeType) {
        Uri contentUri;
        if (mimeType.startsWith("image")) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if (mimeType.startsWith("video")) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if (mimeType.startsWith("audio")) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        } else {
            contentUri = MediaStore.Files.getContentUri("external");
        }
        return contentUri;
    }

    /**
     * 获取 显示名称(DISPLAY_NAME)
     */
    public static String getDisplayName(String mimeType) {
        String contentUri;
        if (mimeType.startsWith("image")) {
            contentUri = MediaStore.Images.Media.DISPLAY_NAME;
        } else if (mimeType.startsWith("video")) {
            contentUri = MediaStore.Video.Media.DISPLAY_NAME;
        } else if (mimeType.startsWith("audio")) {
            contentUri = MediaStore.Audio.Media.DISPLAY_NAME;
        } else {
            contentUri = MediaStore.Downloads.DISPLAY_NAME;
        }
        return contentUri;
    }

    /**
     * 获取_id
     */
    public static String getId(String mimeType) {
        String id;
        if (mimeType.startsWith("image")) {
            id = MediaStore.Images.ImageColumns._ID;
        } else if (mimeType.startsWith("video")) {
            id = MediaStore.Video.VideoColumns._ID;
        } else if (mimeType.startsWith("audio")) {
            id = MediaStore.Audio.AudioColumns._ID;
        } else {
            id = MediaStore.Downloads._ID;
        }
        return id;
    }

    /**
     * 获取Data
     */
    public static String getData(String mimeType) {
        String data;
        if (mimeType.startsWith("image")) {
            data = MediaStore.Images.Media.DATA;
        } else if (mimeType.startsWith("video")) {
            data = MediaStore.Video.Media.DATA;
        } else if (mimeType.startsWith("audio")) {
            data = MediaStore.Audio.Media.DATA;
        } else {
            data = MediaStore.Downloads.DATA;
        }
        return data;
    }

    /**
     * 创建文件
     */
    public void onCreateFile(Context context, int type, String path, String name) {
        // 获取到一个路径
        Uri uri = MediaStore.Files.getContentUri("external");
        // 创建一个ContentValues对象，用来给存储文件数据的数据库进行插入操作
        ContentValues contentValues = new ContentValues();
        if (type == 1) {
            // 设置存储路径致Downloads文件下
            contentValues.put(MediaStore.Downloads.RELATIVE_PATH, path);
        }
        // 设置文件名字
        contentValues.put(MediaStore.Downloads.DISPLAY_NAME, name);

        // 操作数据库
        context.getContentResolver().insert(uri, contentValues);
    }

    /**
     * 创建图片
     */
    public void onCreateImage(Context context, String path) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.ImageColumns.RELATIVE_PATH, path);
        // 设置文件名字
//        contentValues.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/*");
        // 操作数据库
        Uri imgUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        try {
//            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.meinv);
//            OutputStream outputStream = context.getContentResolver().openOutputStream(imgUri);
//            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
//            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除媒体文件
     */
    public static void deleteMedieFile(Context context, File file) {
        if (file.isFile()) {
            String filePath = file.getPath();
            if (filePath.endsWith(".mp4")) {
                int res = context.getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        MediaStore.Audio.Media.DATA + "= \"" + filePath + "\"",
                        null);
                if (res > 0) {
                    file.delete();
                    Timber.e("#####删除文件成功");
                } else {
                    Timber.e("#####删除文件失败");
                }
            } else if (filePath.endsWith(".jpg") || filePath.endsWith(".png") || filePath.endsWith(".bmp")) {
                int res = context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        MediaStore.Audio.Media.DATA + "= \"" + filePath + "\"",
                        null);
                if (res > 0) {
                    file.delete();
                    Timber.e("#####删除文件成功");
                } else {
                    Timber.e("#####删除文件失败");
                }
            } else {
                file.delete();
                Timber.e("#####删除文件成功");
            }
            //删除多媒体数据库中的数据
            return;
        }

    }

    /**
     * 创建一个图片
     */
    public static void createImage(Context context) {
        // 插入一个图片
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.ImageColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/GrainFull/Template");
        contentValues.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, "Zee.png");
        contentValues.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/png");
        Uri imgUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.role);
            OutputStream outputStream = context.getContentResolver().openOutputStream(imgUri);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 删除单个文件操作
     * 操作步骤：根据文件名称查询文件id，然后执行删除操作
     *
     * @param context  句柄
     * @param fileName 文件名称(不包含路径，例：zee.png)
     */
    public static void deleteIndividualFiles(Context context, String fileName) {
        // 查询文件
        String mimeType = getMimeType(fileName);
        Uri contentUri = getContentUri(mimeType);
        String select = getDisplayName(mimeType) + "=?";
        String[] arg = new String[]{fileName};
        Cursor cursor = context.getContentResolver().query(contentUri, null, select, arg, null);
        if (cursor != null && cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(getId(mimeType)));
            Uri queryUri = ContentUris.withAppendedId(contentUri, id);
            Timber.i("####queryUri=%s", queryUri);

            // 删除文件
            int value = context.getContentResolver().delete(queryUri, null, null);
            if (value > 0) {
                Timber.i("#####删除成功!");
            } else {
                Timber.e("#####删除失败!");
            }
            cursor.close();
        }
    }

    /**
     * 删除目录中的文件
     *
     * @param context  句柄
     * @param fileType 要删除的文件类型：image、video、audio
     * @param path     包含的目录路径(不包含文件，例：GrainFull/Template)
     */
    public static void deleteFilesInDir(Context context, String fileType, String path) {
        Uri externalContentUri = getContentUri(fileType);
        String select = getData(fileType) + " like " + "'%" + path + "%'";
        Cursor cursor = context.getContentResolver().query(externalContentUri, null, select, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(getId(fileType)));
                Uri queryUri = ContentUris.withAppendedId(externalContentUri, id);
                Timber.i("####queryUri=%s", queryUri);

                try {
                    // 删除文件
                    int value = context.getContentResolver().delete(queryUri, null, null);
                    if (value > 0) {
                        Timber.i("#####删除成功!");
                    } else {
                        Timber.e("#####删除失败!");
                    }
                } catch (Exception ex) {
                }
            }
            cursor.close();
        }
    }

    /**
     * 根据扩展名获取图片类型
     */
    private static Bitmap.CompressFormat getBitmapTypeByExtension(String val) {
        if (val.equals("png")) {
            return Bitmap.CompressFormat.PNG;
        } else if (val.equals("jpg") || val.equals("jpeg")) {
            return Bitmap.CompressFormat.JPEG;
        } else {
            return Bitmap.CompressFormat.WEBP;
        }

    }

    /**
     * 将Assets中的图片复制到本地SdCard中
     */
    public static void copyImageFromAssets(Context context, String assetsFilePath, String destFilePath) {
        try {
            String[] assets = Utils.getApp().getAssets().list(assetsFilePath);
            if (assets != null && assets.length > 0) {
                // Assets目录下有多个文件
                for (String fileName : assets) {
                    // 插入一个图片
                    ContentValues contentValues = new ContentValues();
//                    contentValues.put(MediaStore.Images.ImageColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/GrainFull/Template");
                    contentValues.put(MediaStore.Images.ImageColumns.RELATIVE_PATH, destFilePath);
                    contentValues.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, fileName);
                    contentValues.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/" + getExtension(fileName));
                    Uri imgUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    try {
//                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.role);
                        // 读取Assets中的图片
                        InputStream inputStream = context.getAssets().open(assetsFilePath + fileName);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        OutputStream outputStream = context.getContentResolver().openOutputStream(imgUri);
                        bitmap.compress(getBitmapTypeByExtension(getExtension(fileName)), 100, outputStream);
                        outputStream.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
