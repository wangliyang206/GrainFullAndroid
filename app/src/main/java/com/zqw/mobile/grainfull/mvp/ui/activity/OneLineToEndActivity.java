package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerOneLineToEndComponent;
import com.zqw.mobile.grainfull.mvp.contract.OneLineToEndContract;
import com.zqw.mobile.grainfull.mvp.presenter.OneLineToEndPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.onepen.FinishWithOneStroke;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Description: 一笔画完
 * <p>
 * Created on 2023/02/01 14:15
 *
 * @author 赤槿
 * module name is OneLineToEndActivity
 */
public class OneLineToEndActivity extends BaseActivity<OneLineToEndPresenter> implements OneLineToEndContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_one_line_to_end)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.view_onelinetoend_content)
    FinishWithOneStroke viewContent;
    /*------------------------------------------------业务区域------------------------------------------------*/
    // 行数、列数、障碍物
    private int rows = 5, columns = 5, difficulties = 4;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerOneLineToEndComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_one_line_to_end;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("一笔画完");

//        initGirdRoad(rows, columns, difficulties);
    }


//    public void initGirdRoad(final int initRows, final int initColums, final int initDifficulties) {
//        ThreadUtil.getInstance().addRunableToSingleThead("initGirdRoad", () -> {
//            Bean_Road aRoad = null;
//            if (passPassed) {
//                getNoPassedRoad = true;
//                long starttime = System.currentTimeMillis();
//                boolean t = false;
//                while (getNoPassedRoad) {
//                    //先从数据库中找
//                    Cursor cursor = getMySql().getSavedYibi(initRows, initColums, initDifficulties);
//                    if (cursor.getCount() > 0) {
//                        boolean sqlGot = false;
//                        cursor.moveToPosition(random.nextInt(cursor.getCount()));
//                        while (cursor.getPosition() != cursor.getCount()) {
//                            String[] roadpositions = cursor.getString(4).split("[,]");
//                            aRoad = new Bean_Road(initRows, initColums, ValueUtil.getIntListFromStrs(roadpositions));
//                            if (!getMySql().checkPassedYibi(aRoad)) {
//                                sqlGot = true;
//                                getNoPassedRoad = false;
//                                break;
//                            }
//                            cursor.moveToNext();
//                        }
//                        if (!sqlGot) {
//                            while (cursor.moveToPrevious()) {
//                                String[] roadpositions = cursor.getString(4).split("[,]");
//                                aRoad = new Bean_Road(initRows, initColums, ValueUtil.getIntListFromStrs(roadpositions));
//                                if (!getMySql().checkPassedYibi(aRoad)) {
//                                    sqlGot = true;
//                                    getNoPassedRoad = false;
//                                    break;
//                                }
//                            }
//                        }
//                        if (sqlGot) break;
//                    }
//                    cursor.close();
//
//
//                    //找不到再从队列拿或者直接生成
//                    if (!ValueUtil.roadQueue.isEmpty()) {
//                        aRoad = ValueUtil.roadQueue.poll();
//                    } else {
//                        aRoad = _findRoadUtil.getAppointedRoad(initRows, initColums, initDifficulties, passPassed);
//                    }
//                    if (aRoad != null) {
//                        if (!getMySql().checkPassedYibi(aRoad)) {
//                            getNoPassedRoad = false;
//                            break;
//                        }
//                    }
//                    if (!t && System.currentTimeMillis() - starttime >= 3000) {
//                        showToast("或许你已经全通关当前所设置的难度了,你可以选择其它难度");
//                        t = true;
//                    }
//                }
//            } else {
//                //先从数据库中随机找
//                Cursor cursor = getMySql().getSavedYibi(initRows, initColums, initDifficulties);
//                if (cursor.getCount() > 0) {
//                    cursor.moveToPosition(random.nextInt(cursor.getCount()));
//                    String[] roadpositions = cursor.getString(4).split("[,]");
//                    aRoad = new Bean_Road(rows, columns, ValueUtil.getIntListFromStrs(roadpositions));
//                }
//                cursor.close();
//
//                //找不到再从队列拿或直接生成
//                if (aRoad == null) {
//                    if (!ValueUtil.roadQueue.isEmpty()) {
//                        aRoad = ValueUtil.roadQueue.poll();
//                    } else {
//                        aRoad = _findRoadUtil.getAppointedRoad(initRows, initColums, initDifficulties, passPassed);
//                    }
//                }
//            }
//
//            final Bean_Road road = aRoad;
//
//            final boolean ispassed;
//            if (road != null) {
//                ispassed = getMySql().checkPassedYibi(road);
//            } else {
//                ispassed = false;
//            }
//            if (road != null && (road.getRows() != initRows || road.getColumns() != initColums || road.getDifficulties() != initDifficulties) && getNoPassedRoad) {
//                initGirdRoad(initRows, initColums, initDifficulties);
//                return;
//            }
//
//            if (passPassed && ispassed && getNoPassedRoad) {
//                initGirdRoad(initRows, initColums, initDifficulties);
//                return;
//            }
//
//            runOnUiThread(() -> {
//                if (road != null) {
//                    checkPassedView(road);
//                    if (isCreatedHint) {
//                        createdHint.setVisibility(View.VISIBLE);
//                        AnimUtil.doScale(createdHint, 0, 1, 0, 1, null, true);
//                    }
//                    rows = initRows;
//                    columns = initColums;
//                    difficulties = initDifficulties;
//                    grid_yibi.initGrid(road, RandomRoadFragment.this);
//                    saveYibi(road, new ArrayList<>());
//                    setting.setText("点击设置\n" + rows + "*" + columns + " | " + difficulties);
//                } else {
//                    showToast("取消构图");
//                    if (rows != initRows || initColums != columns || difficulties != initDifficulties)
//                        ValueUtil.findRoadToQueue(getContext(), rows, columns, difficulties, passPassed);
//                }
//            });
//        });
//    }

    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}