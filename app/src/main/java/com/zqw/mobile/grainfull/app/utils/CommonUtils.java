package com.zqw.mobile.grainfull.app.utils;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.jess.arms.cj.colorful.Colorful;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 包名： com.cj.mobile.aptitude.app.utils
 * 对象名： CommonUtils
 * 描述：第三方没有的工具这里都有
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2018/6/8 16:15
 */

public class CommonUtils {

    /**
     * 倾斜到像素
     */
    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    /**
     * 调整绘图大小
     */
    public static Drawable resizeDrawable(Context context, Drawable drawable, int width, int height) {
        Bitmap bitmap = getBitmap(drawable, width, height);
        return new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, width, height, true));
    }

    public static Bitmap getBitmap(Drawable drawable, int width, int height) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawableCompat) {
            return getBitmap((VectorDrawableCompat) drawable, width, height);
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable, width, height);
        } else {
            throw new IllegalArgumentException("Unsupported drawable type");
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    private static Bitmap getBitmap(VectorDrawableCompat vectorDrawable, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    /**
     * 映射从范围到范围的值
     */
    public static double mapValueFromRangeToRange(double value, double fromLow, double fromHigh, double toLow, double toHigh) {
        return toLow + ((value - fromLow) / (fromHigh - fromLow) * (toHigh - toLow));
    }

    /**
     * 夹紧
     */
    public static double clamp(double value, double low, double high) {
        return Math.min(Math.max(value, low), high);
    }

    /**
     * 获取缩放位图
     *
     * @param r
     * @param res
     * @param resources
     * @return
     */
    public static Bitmap getScaleBitmap(int r, int res, Resources resources) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeResource(resources, res, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > r || height > r) {
            // 缩放
            scaleWidth = ((float) width) / r;
            scaleHeight = ((float) height) / r;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        opts.inDither = true;
        WeakReference<Bitmap> weak = new WeakReference<>(BitmapFactory.decodeResource(resources, res, opts));
        return Bitmap.createScaledBitmap(weak.get(), r, r, true);
    }

    /**
     * 计算贝塞尔平滑路径
     */
    public static boolean calculateBesselSmoothPath(float lineSmoothness, int offset, boolean forceClosed, PathMeasure pathMeasure, Path path, List<Point> entry) {
        if (entry.size() < 2) {
            return false;
        }
        path.reset();
        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        float nextPointX;
        float nextPointY;

        final int lineSize = entry.size();
        for (int valueIndex = 0; valueIndex < lineSize; ++valueIndex) {
            if (Float.isNaN(currentPointX)) {
                Point point = entry.get(valueIndex);
                currentPointX = point.x;
                currentPointY = point.y;
            }
            if (Float.isNaN(previousPointX)) {
                //是否是第一个点
                if (valueIndex > 0) {
                    Point point = entry.get(valueIndex - 1);
                    previousPointX = point.x;
                    previousPointY = point.y;
                } else {
                    //是的话就用当前点表示上一个点
                    previousPointX = currentPointX;
                    previousPointY = currentPointY;
                }
            }

            if (Float.isNaN(prePreviousPointX)) {
                //是否是前两个点
                if (valueIndex > 1) {
                    Point point = entry.get(valueIndex - 2);
                    prePreviousPointX = point.x;
                    prePreviousPointY = point.y;
                } else {
                    //是的话就用当前点表示上上个点
                    prePreviousPointX = previousPointX;
                    prePreviousPointY = previousPointY;
                }
            }

            // 判断是不是最后一个点了
            if (valueIndex < lineSize - 1) {
                Point point = entry.get(valueIndex + 1);
                nextPointX = point.x;
                nextPointY = point.y;
            } else {
                //是的话就用当前点表示下一个点
                nextPointX = currentPointX;
                nextPointY = currentPointY;
            }

            if (valueIndex == 0) {
                // 将Path移动到开始点
                path.moveTo(currentPointX, currentPointY);
            } else {
                // 求出控制点坐标
                final float firstDiffX = (currentPointX - prePreviousPointX);
                final float firstDiffY = (currentPointY - prePreviousPointY);
                final float secondDiffX = (nextPointX - previousPointX);
                final float secondDiffY = (nextPointY - previousPointY);
                final float firstControlPointX = previousPointX + (lineSmoothness * firstDiffX) + offset;
                final float firstControlPointY = previousPointY + (lineSmoothness * firstDiffY) + offset;
                final float secondControlPointX = currentPointX - (lineSmoothness * secondDiffX) - offset;
                final float secondControlPointY = currentPointY - (lineSmoothness * secondDiffY) - offset;
                //画出曲线
                path.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                        currentPointX, currentPointY);
            }

            // 更新值,
            prePreviousPointX = previousPointX;
            prePreviousPointY = previousPointY;
            previousPointX = currentPointX;
            previousPointY = currentPointY;
            currentPointX = nextPointX;
            currentPointY = nextPointY;
        }
        pathMeasure.setPath(path, forceClosed);
        float distance = pathMeasure.getLength() * 1f;
        return pathMeasure.getSegment(0, distance, path, true);
    }

    /**
     * 计算圆弧上的某一点
     */
    public static Point calcPoint(int centerX, int centerY, int radius, float angle, Point point) {
        point.x = (int) (centerX + radius * Math.cos((angle) * Math.PI / 180));
        point.y = (int) (centerY + radius * Math.sin((angle) * Math.PI / 180));
        return point;
    }

    public static int randomInt(Random random, int min, int max) {
        if ((max - min + 1) == 0) {
            return (min + max) / 2;
        }
        return random.nextInt(max) % (max - min + 1) + min;
    }

    public static void hideBottomUIMenu(View v) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            v.setSystemUiVisibility(uiOptions);
        }

    }

    /**
     * 获取状态栏高度，单位px
     */
    public static int getHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 生成随机数
     *
     * @param n 0~n之间的整数
     */
    public static int getRandomNum(int n) {
        return (int) ((Math.random() * n));
    }

    /**
     * 通过秒换算成时分秒
     *
     * @param isWhole 小时/分钟为零时是否显示
     * @param second  秒
     */
    public static String timeConversion(boolean isWhole, int second) {
        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        if (isWhole) {
            return h + "时" + d + "分" + s + "秒";
        } else {
            if (h == 0) {
                // 判断分钟是否为零
                if (d == 0) {
                    return s + "秒";
                } else {
                    return d + "分" + s + "秒";
                }
            } else {
                return h + "时" + d + "分" + s + "秒";
            }
        }
    }

    /**
     * 判断设备是否支持霍尔传感器
     */
    public static Boolean isMagneticSensorAvailable(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        return magneticSensor != null;
    }

    /**
     * List深复制
     */
    public static <E> List<E> deepCopy(List<E> src) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            @SuppressWarnings("unchecked")
            List<E> dest = (List<E>) in.readObject();
            return dest;
        } catch (Exception e) {
            return new ArrayList<E>();
        }
    }

    /**
     * 给夜间模式增加一个动画,颜色渐变
     *
     * @param newTheme
     */
    public static void animChangeColor(Context mContext, Colorful colorful, final int newTheme) {
        final View rootView = ((Activity) mContext).getWindow().getDecorView();
        rootView.setDrawingCacheEnabled(true);
        rootView.buildDrawingCache(true);

        final Bitmap localBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        if (null != localBitmap && rootView instanceof ViewGroup) {
            final View tmpView = new View(mContext);
            tmpView.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), localBitmap));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) rootView).addView(tmpView, params);
            tmpView.animate().alpha(0).setDuration(400).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    colorful.setTheme(newTheme);
                    System.gc();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    ((ViewGroup) rootView).removeView(tmpView);
                    localBitmap.recycle();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        }
    }

    public static boolean customRegular(String str, String regular) {
        return TextUtils.isEmpty(regular) ? false : Pattern.matches(regular, str);
    }

    /**
     * 防止按钮快速重复点击
     *
     * @return 双击 = true，单击 = false；
     */
    public static boolean isDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0L < timeD && timeD < 800L) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }

    /**
     * 格式化金钱格式
     */
    public static String formatMoney(double val) {
        // 格式化小数
        DecimalFormat df = new DecimalFormat("0.00");
        // 与上一行代码的区别是：#表示如果不存在则显示为空，0表示如果没有则该位补0.
//        DecimalFormat df = new DecimalFormat("#0.00");
        // 将数据转换成以3位逗号隔开的字符串，并保留两位小数
//        DecimalFormat df = new DecimalFormat("#,###.00");
        // 不四舍五入
        df.setRoundingMode(RoundingMode.FLOOR);
        // 返回的是String类型
        return df.format(val);
    }

    /**
     * 补零
     */
    public static String format0Right(String str) {
        try {
            if (str.length() <= 1) {
                str = "0" + str;
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return str;
    }

    /**
     * 平面翻转
     *
     * @param img1 需要设置动画的控件
     * @param type 旋转方向(ROTATE_DECREASE = 逆时针；ROTATE_INCREASE = 顺时针)
     */
    public static void rotationTriangle(ImageView img1, boolean type) {
        AnimatorSet animationSet = new AnimatorSet();
        ObjectAnimator scaleAnimation = null;
        if (type) {
            scaleAnimation = ObjectAnimator.ofFloat(img1, "rotationX", 0f, 180f);
        } else {
            scaleAnimation = ObjectAnimator.ofFloat(img1, "rotationX", 180f, 0f);
        }
        scaleAnimation.setDuration(800);
        animationSet.play(scaleAnimation);
        animationSet.start();
    }

    /**
     * 获取拼音的首字母（大写）
     *
     * @param pinyin
     * @return
     */
    public static String getFirstLetter(final String pinyin) {
        if (TextUtils.isEmpty(pinyin)) return "定位";
        String c = pinyin.substring(0, 1);
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c).matches()) {
            return c.toUpperCase();
        } else if ("0".equals(c)) {
            return "定位";
        } else if ("1".equals(c)) {
            return "热门";
        }
        return "定位";
    }

    public static boolean isNotEmpty(List<?> list) {
        return list != null && list.size() > 0;
    }

    /**
     * 获取assets的images目录中图片数量
     */
    public static int getAssetListCount(Context mContext) {
        int imgCount = 0;
        AssetManager assetManager = mContext.getAssets();
        try {
            // -2 代表 减去android-logo-shine和android-logo-mask
            imgCount = Objects.requireNonNull(assetManager.list("images")).length - 2;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imgCount;
    }

    /**
     * 验证是否为浮点数
     *
     * @param input
     * @return
     */
    public static boolean checkContainsFloat(String input) {
        Pattern p = Pattern.compile("\\d+(\\.\\d+)?");
        if (input == null)
            return false;
        return p.matcher(input).matches();
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    private static final int DEF_DIV_SCALE = 10; //这个类不能实例化

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 数字格式化对象
     */
    public static String round(double val) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(val);
    }

    // 最后一次点击时间
    private static long lastClickTime;

    /**
     * 按两次Back在退出程序
     *
     * @param context 句柄
     */
    public static void exitSys(Context context) {
        if ((System.currentTimeMillis() - lastClickTime) > 2000) {
            ArmsUtils.makeText(context, ArmsUtils.getString(context, R.string.common_exit));
            lastClickTime = System.currentTimeMillis();
        } else {
            ArmsUtils.killAll();
//            context.finish();
//            /*当前是退出APP后结束进程。如果不这样做，那么在APP结束后需求手动将EventBus中所注册的监听全部清除以免APP在次启动后重复注册监听*/
//            Process.killProcess(Process.myPid());
//            返回到桌面
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            context.startActivity(intent);
        }
    }

    /**
     * 如果当前是否非空，则返回当前值
     *
     * @param cs
     * @return
     */
    public static String isEmptyReturnStr(CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return "";
        }
        String val = cs.toString();
        return ((val != null && val.length() > 0 && (!val.equalsIgnoreCase("null"))) ? val : "");
    }

    /***
     * Sim卡序列号
     */
    public static String getSimSerialNumber(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            String ret = tm.getSimSerialNumber();
            if (TextUtils.isEmpty(ret))
                return "";
            else
                return ret;
        } catch (Exception ex) {
            return "";
        }
    }

    /***
     * 拿到电话号码(此接口不会100%的获取手机号码，原因是手机号码是映射到sim卡中的。要想100%获取手机号码只能通过靠对接运营商接口获得)
     */
    public static String getPhoneNumber(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            String ret = tm.getLine1Number();
            if (TextUtils.isEmpty(ret))
                return "";
            else
                return ret;
        } catch (Exception ex) {
            return "";
        }
    }
}
