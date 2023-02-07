package com.zqw.mobile.grainfull.mvp.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: OnePenError
 * @Description: 一笔画完 - 错误路线
 * @Author: WLY
 * @CreateDate: 2023/2/7 16:44
 */
@Entity
public class OnePenError {
    // 自增(数据库主键，自增)
    @Id(autoincrement = true)
    private Long _no;
    // 行
    private int rows;
    // 列
    private int columns;
    // 障碍数
    private String difficulties;
    // 起始位置
    private int startPosition;
    @Generated(hash = 1859240537)
    public OnePenError(Long _no, int rows, int columns, String difficulties,
            int startPosition) {
        this._no = _no;
        this.rows = rows;
        this.columns = columns;
        this.difficulties = difficulties;
        this.startPosition = startPosition;
    }

    public OnePenError(int rows, int columns, String difficulties, int startPosition) {
        this.rows = rows;
        this.columns = columns;
        this.difficulties = difficulties;
        this.startPosition = startPosition;
    }

    @Generated(hash = 1124368216)
    public OnePenError() {
    }
    public Long get_no() {
        return this._no;
    }
    public void set_no(Long _no) {
        this._no = _no;
    }
    public int getRows() {
        return this.rows;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }
    public int getColumns() {
        return this.columns;
    }
    public void setColumns(int columns) {
        this.columns = columns;
    }
    public String getDifficulties() {
        return this.difficulties;
    }
    public void setDifficulties(String difficulties) {
        this.difficulties = difficulties;
    }
    public int getStartPosition() {
        return this.startPosition;
    }
    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }
}
