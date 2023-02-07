package com.zqw.mobile.grainfull.mvp.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: OnePenSaved
 * @Description: 一笔画完 - 保存路线
 * @Author: WLY
 * @CreateDate: 2023/2/7 16:50
 */
@Entity
public class OnePenSaved {
    // 自增(数据库主键，自增)
    @Id(autoincrement = true)
    private Long _no;
    // 行
    private int rows;
    // 列
    private int columns;
    // 障碍数
    private int difficulties;
    // 路线
    private String road;
    @Generated(hash = 1550164264)
    public OnePenSaved(Long _no, int rows, int columns, int difficulties,
            String road) {
        this._no = _no;
        this.rows = rows;
        this.columns = columns;
        this.difficulties = difficulties;
        this.road = road;
    }

    public OnePenSaved(int rows, int columns, int difficulties, String road) {
        this.rows = rows;
        this.columns = columns;
        this.difficulties = difficulties;
        this.road = road;
    }

    @Generated(hash = 293928238)
    public OnePenSaved() {
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
    public int getDifficulties() {
        return this.difficulties;
    }
    public void setDifficulties(int difficulties) {
        this.difficulties = difficulties;
    }
    public String getRoad() {
        return this.road;
    }
    public void setRoad(String road) {
        this.road = road;
    }
}