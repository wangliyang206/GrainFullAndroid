package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;

import com.zqw.mobile.grainfull.app.utils.KlotskiBoard;
import com.zqw.mobile.grainfull.app.utils.KlotskiPoint;

import timber.log.Timber;

/**
 * 华容道
 */
public class KlotskiView extends AppCompatImageView {
    private Paint paint;
    // 尺寸宽度
    private int sizeWidth;
    // 尺寸高度
    private int sizeHeight;
    private int tileWidth;
    private int tileHeight;
    private Bitmap[] bitmapTiles;
    private int[][] dataTiles;
    private KlotskiBoard tilesBoard;
    // 列
    private int COL = 3;
    // 行
    private int ROW = 3;
    private final int[][] dir = {
            {-1, 0},//左
            {0, -1},//上
            {1, 0},//右
            {0, 1}//下
    };
    // 是否成功
    private boolean isSuccess;
    // 步数
    private int steps = 0;

    // 游戏是否开启
    private boolean isStartGame;
    // 当前的图片
    private Bitmap bitmap;
    // 监听
    private OnClickChangeListener mListener;

    public KlotskiView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public KlotskiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public KlotskiView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    /**
     * 开始游戏
     *
     * @param mLevel 等级：1代表9块(横竖各三个)，2代表16块(横竖各四个)，3代表25块(横竖各五个)
     */
    public void startGame(int mLevel) {
        Timber.i("#####=startGame");
        // 初始化 关卡
        tilesBoard = new KlotskiBoard();
        switch (mLevel) {
            case 2:
                // 第二关，横竖各四块，共16块
                ROW = 4;
                COL = 4;
                break;
            case 3:
                // 第三关，横竖各五块，共25块
                ROW = 5;
                COL = 5;
                break;
            default:
                // 第一关，横竖各三块，共9块
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

        // 是否开始游戏，此字段不可以放到初始化前面，切换关卡时会报错
        isStartGame = true;
        // 游戏是否完成
        isSuccess = false;
        // 复位当前步数
        steps = 0;
        dataTiles = tilesBoard.createRandomBoard(ROW, COL);
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

        // 判断是否开启游戏
        if (isStartGame) {
            // 游戏开启
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    int idx = dataTiles[i][j];
                    if (idx == ROW * COL - 1 && !isSuccess)
                        continue;
                    canvas.drawBitmap(bitmapTiles[idx], j * tileWidth, i * tileHeight, paint);
                }
            }
        }

        // 判断是否已经完成游戏(完成后显示原图)
        if (isSuccess) {
            RectF dst = new RectF(0, 0, getWidth(), getHeight());
            canvas.drawBitmap(bitmap, null, dst, paint);
        }
    }

    /**
     * 将屏幕上的点转换成,对应拼图块的索引
     *
     * @param x
     * @param y
     */
    private KlotskiPoint xyToIndex(int x, int y) {
        int extraX = x % tileWidth > 0 ? 1 : 0;
        int extraY = x % tileWidth > 0 ? 1 : 0;
        int col = x / tileWidth + extraX;
        int row = y / tileHeight + extraY;

        return new KlotskiPoint(col - 1, row - 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isStartGame && event.getAction() == MotionEvent.ACTION_DOWN) {
            KlotskiPoint point = xyToIndex((int) event.getX(), (int) event.getY());

            for (int i = 0; i < dir.length; i++) {
                int newX = point.getX() + dir[i][0];
                int newY = point.getY() + dir[i][1];

                if (newX >= 0 && newX < COL && newY >= 0 && newY < ROW) {
                    if (dataTiles[newY][newX] == COL * ROW - 1) {
                        steps++;
                        int temp = dataTiles[point.getY()][point.getX()];
                        dataTiles[point.getY()][point.getX()] = dataTiles[newY][newX];
                        dataTiles[newY][newX] = temp;
                        invalidate();
                        if (tilesBoard.isSuccess(dataTiles)) {
                            isSuccess = true;
                            isStartGame = false;
                        }

                        if (mListener != null) {
                            mListener.onChange(isSuccess, steps);
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 注册监听
     */
    public void setOnClickChangeListener(OnClickChangeListener mListener) {
        this.mListener = mListener;
    }

    public interface OnClickChangeListener {
        void onChange(boolean isSuccess, int steps);
    }
}
