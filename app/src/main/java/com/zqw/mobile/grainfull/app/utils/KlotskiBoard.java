package com.zqw.mobile.grainfull.app.utils;

import java.util.Random;

/**
 * 华容道游戏中使用
 * 用来保存块数据的类
 * 注意：x对应的是col,y对应的row
 */
public class KlotskiBoard {
    private int[][] array = null;
    private int row = 0;
    private int col = 0;

    // 四个方向
    private final int[][] dir = {
            {0, 1},//下
            {1, 0},//右
            {0, -1},//上
            {-1, 0}//左
    };

    private void createIntegerArray(int row, int col) {
        array = new int[row][col];
        int idx = 0;
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                array[i][j] = idx++;
    }

    /**
     * 移动块的位置
     *
     * @param srcX
     * @param srcY
     * @param xOffset
     * @param yOffset
     * @return 新的位置，错误返回new KlotskiPoint(-1,-1);
     */
    private KlotskiPoint move(int srcX, int srcY, int xOffset, int yOffset) {
        int x = srcX + xOffset;
        int y = srcY + yOffset;
        if (x < 0 || y < 0 || x >= col || y >= row)
            return new KlotskiPoint(-1, -1);

        int temp = array[y][x];
        array[y][x] = array[srcY][srcX];
        array[srcY][srcX] = temp;

        return new KlotskiPoint(x, y);
    }

    /**
     * 得到下一个可以移动的位置
     *
     * @param src
     */
    private KlotskiPoint getNextPoint(KlotskiPoint src) {
        Random rd = new Random();
        int idx = rd.nextInt(4);//产生0~3的随机数
        int xOffset = dir[idx][0];
        int yOffset = dir[idx][1];
        KlotskiPoint newPoint = move(src.getX(), src.getY(), xOffset, yOffset);
        if (newPoint.getX() != -1 && newPoint.getY() != -1) {
            return newPoint;
        }
        return getNextPoint(src);
    }

    /**
     * 生成拼图数据
     *
     * @param row
     * @param col
     */
    public int[][] createRandomBoard(int row, int col) {
        if (row < 2 || col < 2)
            throw new IllegalArgumentException("行和列都不能小于2");
        this.row = row;
        this.col = col;
        createIntegerArray(row, col);
        int count = 0;
        KlotskiPoint tempPoint = new KlotskiPoint(col - 1, row - 1);
        Random rd = new Random();
        int num = rd.nextInt(100) + 20;//产生20~119的随机数
        while (count < num) {
            tempPoint = getNextPoint(tempPoint);
            count++;
        }
        return array;
    }

    /**
     * 判断是否拼图成功
     *
     * @param arr
     */
    public boolean isSuccess(int[][] arr) {
        int idx = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length && idx < row * col - 1; j++) {
                if (arr[idx / row][idx % col] > arr[(idx + 1) / row][(idx + 1) % col]) {
                    return false;
                }
                idx++;
            }
        }
        return true;
    }
}
