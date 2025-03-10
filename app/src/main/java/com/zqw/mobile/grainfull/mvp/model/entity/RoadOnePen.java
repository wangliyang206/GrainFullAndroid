package com.zqw.mobile.grainfull.mvp.model.entity;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

/**
 * 一笔画完
 */
public class RoadOnePen {
    private Long _no;
    private final int rows;
    private final int columns;
    private final List<Integer> roadList;

    public RoadOnePen(Long _no, int rows, int columns, List<Integer> roadList) {
        this._no = _no;
        this.rows = rows;
        this.columns = columns;
        this.roadList = roadList;
    }

    public RoadOnePen(int rows, int columns, @NonNull List<Integer> road) {
        this.rows = rows;
        this.columns = columns;
        this.roadList = road;
    }

    public RoadOnePen(int rows, int columns, Integer[] roads) {
        this.rows = rows;
        this.columns = columns;
        this.roadList = Arrays.asList(roads);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public List<Integer> getRoadList() {
        return roadList;
    }

    public String getRoadString() {
        return getListString(roadList);
    }

    public int getDifficulties() {
        return rows * columns - roadList.size();
    }

    public String getListString(List list) {
        StringBuilder roadString = new StringBuilder();
        if (list != null) {
            for (int p = 0; p < list.size(); p++) {
                roadString.append(list.get(p));
                if (p != list.size() - 1) {
                    roadString.append(",");
                }
            }
        }
        return roadString.toString();
    }

    public Long get_no() {
        return _no;
    }

    public void set_no(Long _no) {
        this._no = _no;
    }
}
