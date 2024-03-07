package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.zqw.mobile.grainfull.R;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.widget
 * @ClassName: CheckView
 * @Description: 生成本地手机验证码
 * <p>
 * XML使用：
 * <com.zqw.mobile.grainfull.mvp.ui.widget.CheckView
 * android:id="@+id/checkView"
 * android:layout_width="200dp"
 * android:layout_height="50dp"
 * kongqw:bg_color="#FFFFFF00"
 * kongqw:line_num="10"
 * kongqw:point_num="100"
 * kongqw:text_length="6"
 * kongqw:text_size="30dp" />
 * 代码使用：
 * // 生成验证码
 * checkView.invaliChenkCode();
 * <p/>
 * @Author: WLY
 * @CreateDate: 2024/3/4 15:43
 */
public class CheckView extends View {
    Context mContext;
    String mCheckCode = null;
    Paint mTempPaint = new Paint();
    private int mPointNum;
    private int mLineNum;
    private int mTextLength;
    private float mTextSize;
    //    private int mTextColor;
    private int mBgColor;

    public CheckView(Context context) {
        this(context,null);
    }

    // 验证码
    public CheckView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        init(context, attrs);
    }

    public CheckView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        mContext = context;

        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.CheckView);
        // 获取随机点的个数
        mPointNum = a.getInteger(R.styleable.CheckView_point_num, 0);
        // 获取随机线的条数
        mLineNum = a.getInteger(R.styleable.CheckView_line_num, 0);
        // 获取验证码长度
        mTextLength = a.getInteger(R.styleable.CheckView_text_length, 4);
        // 获取验证码字体大小
        mTextSize = a.getDimension(R.styleable.CheckView_text_size, 30);
        // 获取验证码字体颜色
//        mTextColor = a.getColor(R.styleable.CheckView_text_color, 0XFFFFFFFF);
        // 获取背景颜色
        mBgColor = a.getColor(R.styleable.CheckView_bg_color, 0XFFFFFFFF);
        a.recycle();

        mTempPaint.setAntiAlias(true);
        mTempPaint.setTextSize(mTextSize);
        mTempPaint.setStrokeWidth(3);
//        Log.d("CheckView", "point_num = " + mPointNum);
//        Log.d("CheckView", "line_num = " + mLineNum);
//        Log.d("CheckView", "text_length = " + mTextLength);
//        Log.d("CheckView", "text_color = " + mTextColor);
//        Log.d("CheckView", "text_size = " + mTextSize);
//        Log.d("CheckView", "bg_color = " + mBgColor);
    }

    public void onDraw(Canvas canvas) {
        // 生成验证码
        mCheckCode = makeCheckCode();
        // 设置二维码背景色
        canvas.drawColor(mBgColor);

        final int height = getHeight();
        // 获得CheckView控件的高度
        final int width = getWidth();
        // 获得CheckView控件的宽度
        int dx = width / mTextLength / 2;

        char[] checkNum = mCheckCode.toCharArray();
        for (int i = 0; i < mTextLength; i++) {
            // 绘制验证控件上的文本
            canvas.drawText("" + checkNum[i], dx, getPositon(height), mTempPaint);
            dx += width / (mTextLength + 1);
        }
        int[] line;
        for (int i = 0; i < mLineNum; i++) {
            // 划线
            line = getLine(height, width);
            canvas.drawLine(line[0], line[1], line[2], line[3], mTempPaint);
        }
        // 绘制小圆点
        int[] point;
        for (int i = 0; i < mPointNum; i++) {
            // 画点
            point = getPoint(height, width);
            canvas.drawCircle(point[0], point[1], 1, mTempPaint);
        }
    }

    /**
     * 生成新的验证码
     */
    public void invaliChenkCode() {
        invalidate();
    }

    /**
     * 获取验证码
     */
    public String getCheckCode() {
        return mCheckCode;
    }

    /**
     * 产生随机验证码
     */
    public String makeCheckCode() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mTextLength; i++) {
            int temp = (int) (Math.random() * 10);
            sb.append(temp);
        }
        return sb.toString();
    }

    /**
     * 计算验证码的绘制y点位置
     *
     * @param height 传入CheckView的高度值
     */
    public int getPositon(int height) {
        int tempPositoin = (int) (Math.random() * height);
        if (tempPositoin < 50) {
            tempPositoin += 50;
        }
        return tempPositoin;
    }


    /**
     * 随机产生划线的起始点坐标和结束点坐标
     *
     * @param height 传入CheckView的高度值
     * @param width  传入CheckView的宽度值
     * @return 起始点坐标和结束点坐标
     */
    public static int[] getLine(int height, int width) {
        int[] tempCheckNum = {0, 0, 0, 0};
        for (int i = 0; i < 4; i += 2) {
            tempCheckNum[i] = (int) (Math.random() * width);
            tempCheckNum[i + 1] = (int) (Math.random() * height);
        }
        return tempCheckNum;
    }

    /**
     * 随机产生点的圆心点坐标
     *
     * @param height 传入CheckView的高度值
     * @param width  传入CheckView的宽度值
     */
    public static int[] getPoint(int height, int width) {
        int[] tempCheckNum = {0, 0, 0, 0};
        tempCheckNum[0] = (int) (Math.random() * width);
        tempCheckNum[1] = (int) (Math.random() * height);
        return tempCheckNum;
    }
}
