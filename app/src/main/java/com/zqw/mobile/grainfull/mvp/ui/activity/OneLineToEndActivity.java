package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.dialog.CommTipsDialog;
import com.zqw.mobile.grainfull.app.dialog.PopupOneLineToEnd;
import com.zqw.mobile.grainfull.app.service.OneLineToEndMusicService;
import com.zqw.mobile.grainfull.app.utils.EventBusTags;
import com.zqw.mobile.grainfull.app.utils.ThreadUtil;
import com.zqw.mobile.grainfull.di.component.DaggerOneLineToEndComponent;
import com.zqw.mobile.grainfull.mvp.contract.OneLineToEndContract;
import com.zqw.mobile.grainfull.mvp.model.entity.MainEvent;
import com.zqw.mobile.grainfull.mvp.model.entity.RoadOnePen;
import com.zqw.mobile.grainfull.mvp.presenter.OneLineToEndPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.onepen.FinishWithOneStroke;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
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

    @BindView(R.id.txvi_onelinetoend_level)
    TextView txviLevel;                                                                             // 关卡

    @BindView(R.id.view_onelinetoend_content)
    FinishWithOneStroke viewContent;

    @BindView(R.id.btn_onelinetoend_help)
    ImageButton btnHelp;

    /*------------------------------------------------业务区域------------------------------------------------*/
    public final static String key_toPlayMusic = "key_toPlayMusic";
    // 播放音乐
    public boolean toPlayMusic = true;
    // 是否正在帮助
    private boolean mIsHelping = false;
    // 首次通过
    private boolean firstPassed = false;
    // 行数
    private final int initRows = 5;
    // 列数
    private final int initColums = 5;
    // 障碍物
    private final int initDifficulties = 4;

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, OneLineToEndMusicService.class);
        intent.putExtra(key_toPlayMusic, toPlayMusic ? 0 : 2);
        startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(this, OneLineToEndMusicService.class);
        intent.putExtra(key_toPlayMusic, 1);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, OneLineToEndMusicService.class));
        ThreadUtil.getInstance().removeRunable("initGirdRoad");
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

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "one_line_to_end");

        initGirdRoad(initRows, initColums, initDifficulties);
    }

    /**
     * 清空数据库事件    回调
     */
    @Subscriber(tag = EventBusTags.HOME_TAG, mode = ThreadMode.POST)
    private void eventBusEvent(MainEvent mainEvent) {
        if (mainEvent.getCode() == EventBusTags.ONE_LINE_TO_END_CLEAR) {
            if (mPresenter != null) {
                mPresenter.clearPassedData();
            }

            showLevel("1");
        }
    }

    /**
     * 显示关卡
     */
    @Override
    public void showLevel(String tips) {
        txviLevel.setText(tips);
    }

    /**
     * 已生成关卡，加载游戏
     */
    @Override
    public void loadGame(RoadOnePen road) {
        viewContent.initGrid(road, this);
    }

    /**
     * 收集统计信息
     */
    @Override
    public void showCollection(int rows, int columns, int difficulties, int passed) {
        PopupOneLineToEnd mPopup = new PopupOneLineToEnd(getApplicationContext(), rows, columns, difficulties, passed);
        mPopup.showAtLocation(contentLayout, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @OnClick({
            R.id.btn_onelinetoend_help,                                                             // 帮助
            R.id.btn_onelinetoend_collection,                                                       // 收集
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_onelinetoend_help:                                                        // 帮助
                if (!mIsHelping) {
                    viewContent.getHelp();
                } else {
                    runOnUiThread(() -> viewContent.refreshGrid());
                }
                break;
            case R.id.btn_onelinetoend_collection:                                                  // 收集
                if (mPresenter != null) {
                    mPresenter.getCollection();
                }
                break;
        }
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
                } else {
                    // 取消后需要让游戏继续走下去
                    firstPassed = false;
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
                btnHelp.setBackgroundResource(R.drawable.ic_helping);
            } else {
                btnHelp.setBackgroundResource(R.drawable.ic_help);
            }
            this.mIsHelping = isHelping;
        });
    }

    @Override
    public boolean isHelping() {
        return mIsHelping;
    }
}