package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.dialog.CommTipsDialog;
import com.zqw.mobile.grainfull.app.utils.ThreadUtil;
import com.zqw.mobile.grainfull.di.component.DaggerOneLineToEndComponent;
import com.zqw.mobile.grainfull.mvp.contract.OneLineToEndContract;
import com.zqw.mobile.grainfull.mvp.model.entity.RoadOnePen;
import com.zqw.mobile.grainfull.mvp.presenter.OneLineToEndPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.onepen.FinishWithOneStroke;

import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

/**
 * Description: 一笔画完
 * <p>
 * Created on 2023/02/01 14:15
 *
 * @author 赤槿
 * module name is OneLineToEndActivity
 */
public class OneLineToEndActivity extends BaseActivity<OneLineToEndPresenter> implements OneLineToEndContract.View, FinishWithOneStroke.yibiListener {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_one_line_to_end)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.view_onelinetoend_content)
    FinishWithOneStroke viewContent;
    /*------------------------------------------------业务区域------------------------------------------------*/
    // 是否正在帮助
    private boolean mIsHelping = false;
    // 首次通过
    private boolean firstPassed = false;
    // 行数、列数、障碍物
    private int initRows = 5, initColums = 5, initDifficulties = 4;

    @Override
    protected void onDestroy() {
        ThreadUtil.getInstance().removeRunable("getRoadToQueue");
        super.onDestroy();
    }

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

        initGirdRoad(initRows, initColums, initDifficulties);
    }

    @Override
    public void loadView(RoadOnePen road) {
        viewContent.initGrid(road, this);
    }

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

    @Override
    public void initGirdRoad(int initRows, int initColums, int initDifficulties) {
        Timber.i("#### initGirdRoad");
        firstPassed = false;
        if (mPresenter != null) {
            mPresenter.initGirdRoad(initRows, initColums, initDifficulties);
        }
    }

    /**
     * 停止获取道路
     */
    @Override
    public boolean stopGettingRoad() {
        Timber.i("#### stopGettingRoad");
        return false;
    }

    /**
     * 保存状态
     */
    @Override
    public void saveYibi(RoadOnePen road, List<Integer> passedPositions) {
        Timber.i("#### saveYibi");
    }

    /**
     * 通关
     */
    @Override
    public void passed(RoadOnePen road) {
        Timber.i("#### passed = 通过");
        if (road == null) return;
        if (!firstPassed) {
            firstPassed = true;
            if (mPresenter != null) {
                mPresenter.insertPassedYibi(road);
            }

            CommTipsDialog mDialog = new CommTipsDialog(this, "温馨提示", "恭喜通过！是否继续下一关？", isVal -> {
                if (isVal) {
                    initGirdRoad(initRows, initColums, initDifficulties);
                }
            });
            mDialog.show();
        }
    }

    /**
     * 设置正在帮助
     */
    @Override
    public void setIsHelping(boolean isHelping) {
        Timber.i("#### setIsHelping=%s", isHelping);
        runOnUiThread(() -> {
            if (isHelping) {
//                helpButton.setBackgroundResource(R.drawable.ic_helping);
            } else {
//                helpButton.setBackgroundResource(R.drawable.ic_help);
            }
            this.mIsHelping = isHelping;
        });
    }

    @Override
    public boolean isHelping() {
        return mIsHelping;
    }
}