package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;

import timber.log.Timber;

/**
 * 多格切图 - 添加图片后自动将一张图片切成多张小图
 * 例：4格、9格、16格、25格、36格、49格、64格
 */
public class NinePalacesView extends AppCompatImageView {
    private Paint paint;
    // 尺寸宽度
    private int sizeWidth;
    // 尺寸高度
    private int sizeHeight;
    private int tileWidth;
    private int tileHeight;
    private Bitmap[] bitmapTiles;
    private int[][] dataTiles;
    // 列
    private int COL = 3;
    // 行
    private int ROW = 3;
    // 当前的图片
    private Bitmap bitmap;

    public NinePalacesView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public NinePalacesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public NinePalacesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, 0);
    }

    /**
     * 初始化
     */
    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        Timber.i("#####=init");
        paint = new Paint();
        paint.setAntiAlias(true);

        // 第一次加载默认图片
        BitmapDrawable drawable = (BitmapDrawable) getDrawable();
        bitmap = drawable.getBitmap();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        Timber.i("#####=setImageResource");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Timber.i("#####=onMeasure");
        // 拿到屏幕尺寸
        sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 重新设定View尺寸，1:1正方形
        ViewGroup.LayoutParams mLayoutParams = getLayoutParams();
        mLayoutParams.width = sizeWidth;
        mLayoutParams.height = sizeWidth;
        setLayoutParams(mLayoutParams);

        // 加载图片
        startGame(1);
    }

    /**
     * 对外提供已分割好的小图
     */
    public Bitmap[] getBitmapTiles() {
        return bitmapTiles;
    }

    /**
     * 开始游戏
     *
     * @param type 类型：2代表4块；3代表9块(默认)；4代表16块；5代表25块；
     */
    public void startGame(int type) {
        Timber.i("#####=startGame");
        switch (type) {
            case 2:
                // 横竖各二块，共4块
                ROW = 2;
                COL = 2;
                break;
            case 4:
                // 横竖各四块，共16块
                ROW = 4;
                COL = 4;
                break;
            case 5:
                // 横竖各五块，共25块
                ROW = 5;
                COL = 5;
                break;
            default:
                // 横竖各三块，共9块
                ROW = 3;
                COL = 3;
                break;
        }

        // 获取图片
        BitmapDrawable drawable = (BitmapDrawable) getDrawable();
        bitmap = drawable.getBitmap();
        // 将图片进行数据拆分
        Bitmap back = Bitmap.createScaledBitmap(bitmap, sizeWidth, sizeHeight, true);
        tileWidth = back.getWidth() / COL;
        tileHeight = back.getHeight() / ROW;
        bitmapTiles = new Bitmap[COL * ROW];
        int idx = 0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                bitmapTiles[idx++] = Bitmap.createBitmap(back, j * tileWidth, i * tileHeight, tileWidth, tileHeight);
            }
        }

        dataTiles = new int[ROW][COL];
        int mIndex = 0;
        for (int i = 0; i < ROW; i++)
            for (int j = 0; j < COL; j++)
                dataTiles[i][j] = mIndex++;

        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Timber.i("#####=onSizeChanged");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Timber.i("#####=onDraw");
        canvas.drawColor(Color.GRAY);

        // 绘制图片
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                int idx = dataTiles[i][j];

                // 显示图片
                canvas.drawBitmap(bitmapTiles[idx], j * tileWidth, i * tileHeight, paint);
            }
        }

        // 绘制分割线
        for (int i = 0; i < ROW - 1; i++) {
            // 竖线
            Paint mVertical = new Paint();
            mVertical.setAntiAlias(true);
            mVertical.setStyle(Paint.Style.STROKE);
            // 设置颜色
            mVertical.setColor(Color.WHITE);
            mVertical.setStrokeWidth(10);
            PathEffect effectsVertical = new DashPathEffect(new float[]{0, 10, 35, 2}, 1);

            Path pathVertical = new Path();
            float mX = (i + 1) * tileWidth;
            float maxY = ROW * tileWidth;
            // 移动画笔 - 竖线
            pathVertical.moveTo(mX, 0);
            // 绘制虚线 - 竖线
            pathVertical.lineTo(mX, maxY);

            mVertical.setPathEffect(effectsVertical);
            canvas.drawPath(pathVertical, mVertical);


            // 横线
            Paint mHorizontal = new Paint();
            mHorizontal.setAntiAlias(true);
            mHorizontal.setStyle(Paint.Style.STROKE);
            // 设置颜色
            mHorizontal.setColor(Color.WHITE);
            mHorizontal.setStrokeWidth(10);
            PathEffect effectsHorizontal = new DashPathEffect(new float[]{0, 10, 35, 2}, 1);

            Path pathHorizontal = new Path();
            float mY = (i + 1) * tileWidth;
            float maxX = COL * tileHeight;
            // 移动画笔 - 横线
            pathHorizontal.moveTo(0, mY);
            // 绘制虚线 - 横线
            pathHorizontal.lineTo(maxX, mY);

            mHorizontal.setPathEffect(effectsHorizontal);
            canvas.drawPath(pathHorizontal, mHorizontal);
        }

    }
}
