package com.zqw.mobile.grainfull.mvp.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: OnePenPassed
 * @Description: 一笔画完 - 保存关卡
 * @Author: WLY
 * @CreateDate: 2023/2/7 16:48
 */
@Entity
public class OnePenLevel {
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
    // 是否通关
    private boolean passed;
    @Generated(hash = 1645520868)
    public OnePenLevel(Long _no, int rows, int columns, int difficulties,
            String road, boolean passed) {
        this._no = _no;
        this.rows = rows;
        this.columns = columns;
        this.difficulties = difficulties;
        this.road = road;
        this.passed = passed;
    }

    public OnePenLevel(int rows, int columns, int difficulties, String road, boolean passed) {
        this.rows = rows;
        this.columns = columns;
        this.difficulties = difficulties;
        this.road = road;
        this.passed = passed;
    }

    @Generated(hash = 1843741959)
    public OnePenLevel() {
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
    public boolean getPassed() {
        return this.passed;
    }
    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
