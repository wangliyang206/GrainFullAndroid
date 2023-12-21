package com.zqw.mobile.grainfull.app.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.Utils;
import com.zqw.mobile.grainfull.app.global.Constant;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
        Uri contentUri = null;
        if (mimeType.startsWith("image")) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if (mimeType.startsWith("video")) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if (mimeType.startsWith("audio")) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        } else if (mimeType.startsWith("download")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
            }
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
     * 根据扩展名获取图片类型
     */
    private static Bitmap.CompressFormat getBitmapTypeByExtension(String val) {
        if (val.equals("png")) {
            return Bitmap.CompressFormat.PNG;
        } else if (val.equals("jpg") || val.equals("jpeg")) {
            return Bitmap.CompressFormat.JPEG;
        } else if (val.equals("webp")) {
            return Bitmap.CompressFormat.WEBP;
        } else {
            return Bitmap.CompressFormat.JPEG;
        }
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
     * 在Download目录下创建文件(缺点：文件名称如果使用同一个时，手动删除后，则无法创建)
     *
     * @param context  句柄
     * @param fileName 文件名称(包含：123.text)
     * @return 操作流对象
     * @throws FileNotFoundException
     */
    public static OutputStream createFile(Context context, String fileName) throws FileNotFoundException {
        Uri contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        String path = Environment.DIRECTORY_DOWNLOADS + File.separator + Constant.APP_CATALOGUE + "/";

        OutputStream outputStream = null;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, path);
        try {
            Uri uri = context.getContentResolver().insert(contentUri, contentValues);
            if (uri != null) {
                Timber.i("##### file=%s", uri.getPath());
                outputStream = context.getContentResolver().openOutputStream(uri);
            } else {
                Timber.i("##### file=null");
            }
        } catch (Exception ex) {
            Timber.e("##### onCreateFileByAudio ERROR=%s", ex.getMessage());
        }
        return outputStream;
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
     * 将项目中的图片(MipMap)保存到本地(SdCard/Pictures)
     */
    public static void saveImage(Context context, String destFilePath, String fileName, int id) {
        // 插入一个图片
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            // 分区模式
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.ImageColumns.RELATIVE_PATH, destFilePath);
            contentValues.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpg");
            Uri imgUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
                OutputStream outputStream = context.getContentResolver().openOutputStream(imgUri);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            // 旧式外部存储
            try {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
                String jpegName = destFilePath + fileName;
                FileOutputStream fout = new FileOutputStream(jpegName);
                BufferedOutputStream bos = new BufferedOutputStream(fout);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
     * @param path     包含的目录路径(不包含文件名称及后缀，例：GrainFull/Template)
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
     * 将Assets中的图片复制到本地SdCard/Pictures中
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

                        // 保存图片到本地
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

    /**
     * 以流形式保存文件
     *
     * @param outputStream 目标路径
     * @param inputStream  往文件中输入的内容
     */
    public static void saveFile(OutputStream outputStream, InputStream inputStream) {
        if (outputStream == null) {
            return;
        }

        OutputStream os = null;
        try {
            os = new BufferedOutputStream(outputStream);
            byte data[] = new byte[8192];
            int len;
            while ((len = inputStream.read(data, 0, 8192)) != -1) {
                os.write(data, 0, len);
            }
        } catch (IOException e) {
            Timber.i("##### tts save error");
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
                outputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取文件
     */
    public static Uri getDownloadFileUri(Context context, String fileName) {
        Uri mResult = null;
        try {
            Uri contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;

            String select = MediaStore.MediaColumns.DISPLAY_NAME + "=?";
            String[] arg = new String[]{fileName.trim()};
            Cursor cursor = context.getContentResolver().query(contentUri, null, select, arg, null);

            // 查询List结果
            if (cursor != null) {
                Timber.i("####cursor=%s", cursor.getCount());
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
                    String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));
                    mResult = ContentUris.withAppendedId(contentUri, id);
                    Timber.i("####id=%s", id);
                    Timber.i("####displayName=%s", displayName);
                    Timber.i("####uri=%s", mResult);
                }
            } else {
                Timber.i("####cursor=null");
            }
        } catch (Exception ex) {
            Timber.e("####getDownloadFile Error=%s", ex.getMessage());
        }

        return mResult;
    }

    /**
     * 读取文件
     */
    public static InputStream getDownloadFile(Context context, String fileName) {
        InputStream mInputStream = null;
        try {
            Uri uri = getDownloadFileUri(context, fileName);

            // 查询List结果
            if (uri != null) {
                mInputStream = context.getContentResolver().openInputStream(uri);

                if (mInputStream == null)
                    Timber.i("#### 未匹配");
            } else {
                Timber.i("####cursor=null");
            }
        } catch (Exception ex) {
            Timber.e("####getDownloadFile Error=%s", ex.getMessage());
        }

        return mInputStream;
    }

    /**
     * 删除Download中文件
     */
    public static void delDownloadFile(Context context, String fileName) {
        try {
            Uri contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;

            String select = MediaStore.MediaColumns.DISPLAY_NAME + "=?";
            String[] arg = new String[]{fileName.trim()};
            Cursor cursor = context.getContentResolver().query(contentUri, null, select, arg, null);

            // 查询List结果
            if (cursor != null) {
                Timber.i("####cursor=%s", cursor.getCount());
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
                    String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));
                    Uri uri = ContentUris.withAppendedId(contentUri, id);
                    Timber.i("####id=%s", id);
                    Timber.i("####displayName=%s", displayName);
                    Timber.i("####uri=%s", uri);

                    // 用于删除MediaStore对象所创建的文件
                    context.getContentResolver().delete(uri, null, null);
                }
            } else {
                Timber.i("####cursor=null");
            }

            // 如果存在，则删除(这句话用于删除媒体对象创建的文件)
            FileUtils.delete(Constant.AUDIO_PATH + fileName);
        } catch (Exception ex) {
            Timber.e("####delDownloadFile Error=%s", ex.getMessage());
        }
    }

    /**
     * 获取Download中文件大小
     */
    public static int getDownloadFileSize(Context context, String fileName) {
        try {
            return getDownloadFile(context, fileName).available();
        } catch (Exception ex) {
            Timber.e("####delDownloadFile Error=%s", ex.getMessage());
        }

        return 0;
    }
}
