package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.zqw.mobile.grainfull.app.utils.KlotskiBoard;
import com.zqw.mobile.grainfull.app.utils.KlotskiPoint;

import java.io.IOException;
import java.io.InputStream;

import timber.log.Timber;

/**
 * 华容道
 */
public class KlotskiView extends AppCompatImageView {
    private Context context;
    private Bitmap back;
    private Paint paint;
    private int tileWidth;
    private int tileHeight;
    private Bitmap[] bitmapTiles;
    private int[][] dataTiles;
    private KlotskiBoard tilesBoard;
    private final int COL = 3;
    private final int ROW = 3;
    private final int[][] dir = {
            {-1, 0},//左
            {0, -1},//上
            {1, 0},//右
            {0, 1}//下
    };
    private boolean isSuccess;
    private int steps = 0;

    // 游戏是否开启
    private boolean isStartGame;
    // 当前的图片
    private Bitmap bitmap;

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
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);

        BitmapDrawable drawable = (BitmapDrawable) getDrawable();
        bitmap = drawable.getBitmap();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Timber.i("#####=onMeasure");
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        back = Bitmap.createScaledBitmap(bitmap, sizeWidth, sizeHeight, true);
        tileWidth = back.getWidth() / COL;
        tileHeight = back.getHeight() / ROW;
        bitmapTiles = new Bitmap[COL * ROW];
        int idx = 0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                bitmapTiles[idx++] = Bitmap.createBitmap(back, j * tileWidth, i * tileHeight, tileWidth, tileHeight);
            }
        }
    }

    /**
     * 开始游戏
     */
    public void startGame() {
        isStartGame = true;
        tilesBoard = new KlotskiBoard();
        dataTiles = tilesBoard.createRandomBoard(ROW, COL);
        isSuccess = false;
        steps = 0;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);

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
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
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
                            invalidate();
                            String successText = String.format("恭喜你拼图成功，移动了%d次", steps);
                            new AlertDialog.Builder(context)
                                    .setTitle("拼图成功")
                                    .setCancelable(false)
                                    .setMessage(successText)
                                    .setPositiveButton("重新开始", (dialog, which) -> startGame())
                                    .setNegativeButton("退出游戏", (dialog, which) -> System.exit(0))
                                    .create()
                                    .show();
                        }
                    }
                }
            }
        }
        return true;
    }

}
