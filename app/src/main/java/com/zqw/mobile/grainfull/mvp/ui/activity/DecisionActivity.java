package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.dialog.PopupSelectList;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.di.component.DaggerDecisionComponent;
import com.zqw.mobile.grainfull.mvp.contract.DecisionContract;
import com.zqw.mobile.grainfull.mvp.presenter.DecisionPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.TurntableView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:做个决定
 * <p>
 * Created on 2022/12/27 16:16
 *
 * @author 赤槿
 * module name is DecisionActivity
 */
public class DecisionActivity extends BaseActivity<DecisionPresenter> implements DecisionContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_decision)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.btn_decisionactivity_title)
    Button txviTitle;

    @BindView(R.id.rovi_decisionactivity_turntable)
    TurntableView mTurntable;

    @BindView(R.id.txvi_decisionactivity_result)
    TextView txviResult;
    /*------------------------------------------------业务区域------------------------------------------------*/
    // 转盘中的文字
    private List<String> names = new ArrayList<>();
    // 选择器
    private PopupSelectList mPopup;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDecisionComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_decision;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("做个决定");

        // 加载默认数据
        names = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dinnerFoodName)));

        initPop();
        changeColors();
        changeDatas();

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "decision_open");
    }

    /**
     * 初始化选择器
     */
    private void initPop() {
        List<String> mList = new ArrayList<>();
        mList.add("今晚吃什么？");
        mList.add("早上吃什么？");
        mList.add("下午做什么？");
        mList.add("掷个骰子");
        mList.add("约她出去吗？");
        mList.add("打什么游戏？");

        PopupSelectList.ItemClick itemClick = (position, info) -> {
            // 显示内容
            txviTitle.setText(info);

            if (position == 0) {
                names = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dinnerFoodName)));
            } else if (position == 1) {
                names = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.breakfastFoodName)));
            } else if (position == 2) {
                names = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.afternoonName)));
            } else if (position == 3) {
                names = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.diceName)));
            } else if (position == 4) {
                names = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.appointmentName)));
            } else if (position == 5) {
                names = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.gameName)));
            }

            changeColors();
            changeDatas();
            txviResult.setText("");
        };
        mPopup = new PopupSelectList(this, "转盘", mList, itemClick);
    }

    /**
     * 变更扇形颜色
     */
    private void changeColors() {
        int num = names.size();
        ArrayList<Integer> colors = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            if (num % 2 == 0) {
                // 两个颜色
                if (i % 2 == 0) {
                    colors.add(getResources().getColor(R.color.rmb_light_color));
                } else {
                    colors.add(getResources().getColor(R.color.rmb_green_color));
                }
            } else {
                // 三个颜色
                if (i == num - 1) {
                    // 最后一个颜色
                    colors.add(getResources().getColor(R.color.rmb_dark_green_color));
//                    colors.add(Color.parseColor("#eed1bd"));
                } else {
                    if (i % 2 == 0) {
                        colors.add(getResources().getColor(R.color.rmb_light_color));
                    } else {
                        colors.add(getResources().getColor(R.color.rmb_green_color));
                    }
                }

            }
        }

        mTurntable.setBackColor(colors);
    }

    /**
     * 变更数据
     */
    private void changeDatas() {
        int num = names.size();
        mTurntable.setDatas(num, names);
    }

    @OnClick({
            R.id.btn_decisionactivity_title,                                                        // 选择转盘
            R.id.btn_decisionactivity_edit,                                                         // 编辑转盘
            R.id.imvi_decisionactivity_start,                                                       // 转盘 - 开始按钮
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_decisionactivity_title:                                                   // 选择转盘
                if (mPopup != null) {
                    mPopup.showAtLocation(contentLayout, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.btn_decisionactivity_edit:                                                    // 编辑转盘
                ActivityUtils.startActivityForResult(this, EditTurntableActivity.class, Constant.MAIN_BASICINFO);
                break;
            case R.id.imvi_decisionactivity_start:                                                  // 转盘 - 开始按钮

                // 友盟统计 - 自定义事件
                MobclickAgent.onEvent(getApplicationContext(), "decision");

                // 以下为随即抽奖
                mTurntable.startRotate(new TurntableView.ITurntableListener() {
                    @Override
                    public void onStart() {
//                        showMessage("开始抽奖");
                        txviResult.setText("");
                    }

                    @Override
                    public void onEnd(int position, String name) {
                        txviResult.setText(name);
                    }
                });

                // 以下为指定抽奖
//                mTurntable.startRotate(3,new TurntableView.ITurntableListener() {
//                    @Override
//                    public void onStart() {
//                        showMessage("开始抽奖");
//                    }
//
//                    @Override
//                    public void onEnd(int position, String name) {
//                        showMessage("抽中抽奖:" + name);
//                    }
//                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.MAIN_BASICINFO && resultCode == RESULT_OK) {
            names.clear();
            names.addAll(data.getStringArrayListExtra("Turntable"));
            txviTitle.setText(data.getStringExtra("title"));
            changeColors();
            changeDatas();
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
}