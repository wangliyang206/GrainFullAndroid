package com.jess.arms.http.imageloader.glide;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.util.Util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.jess.arms.http.imageloader.glide
 * @ClassName: GlideRoundTransform
 * @Description: Glide设置成圆角图片，以及解决刷新图片闪烁的问题
 * @Author: WLY
 * @CreateDate: 2023/7/19 14:45
 */
public class GlideRoundTransform extends BitmapTransformation {
    private final float radius;
    private final String ID = "com.bumptech.glide.transformations.FillSpace";
    private final byte[] ID_ByTES = ID.getBytes(CHARSET);

    public GlideRoundTransform(int dp) {
        this.radius = dp;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap toTransform) {
        if (toTransform == null) {
            return null;
        }
        Bitmap result = pool.get(toTransform.getWidth(), toTransform.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            return Bitmap.createBitmap(toTransform.getWidth(), toTransform.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new BitmapShader(toTransform, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        RectF rectF = new RectF(0f, 0f, toTransform.getWidth(), toTransform.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        //左上角、右上角圆角
        RectF rectRound = new RectF(0f, 100f, toTransform.getWidth(), toTransform.getHeight());
        canvas.drawRect(rectRound, paint);
        return result;
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof GlideRoundTransform) {
            GlideRoundTransform other = (GlideRoundTransform) o;
            return radius == other.radius;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(ID.hashCode(), Util.hashCode(radius));

    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_ByTES);
        byte[] radiusData = ByteBuffer.allocate(4).putInt((int) radius).array();
        messageDigest.update(radiusData);
    }
}
