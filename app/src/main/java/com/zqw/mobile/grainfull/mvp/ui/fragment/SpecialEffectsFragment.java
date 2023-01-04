package com.zqw.mobile.grainfull.mvp.ui.fragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerSpecialEffectsComponent;
import com.zqw.mobile.grainfull.mvp.contract.SpecialEffectsContract;
import com.zqw.mobile.grainfull.mvp.presenter.SpecialEffectsPresenter;
import com.zqw.mobile.grainfull.mvp.ui.activity.AudioWaveformActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.LevitationButtonActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.LotteryTurntableActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.SerialNumberActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.SpiderWebGradeActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.fragment
 * @ClassName: SpecialEffectsFragment
 * @Description: 特效
 * @Author: WLY
 * @CreateDate: 2022/7/14 16:46
 */
public class SpecialEffectsFragment extends BaseFragment<SpecialEffectsPresenter> implements SpecialEffectsContract.View, View.OnClickListener {
    /*------------------------------------------------控件信息------------------------------------------------*/

    @BindView(R.id.lila_fragmentspecialeffects_loading)
    View lilaLoading;                                                                               // Loading

    /*------------------------------------------------业务区域------------------------------------------------*/

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerSpecialEffectsComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_specialeffects, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({
            R.id.btn_fragmentspecialeffects_text_serialnumber,                                      // 仿序列号
            R.id.btn_fragmentspecialeffects_other_audiowaveform,                                    // 音频波形
            R.id.btn_fragmentspecialeffects_other_lottery,                                          // 抽奖转盘
            R.id.btn_fragmentspecialeffects_other_spiderwebgrade,                                   // 蜘蛛网等级
            R.id.btn_fragmentspecialeffects_other_levitationbutton,                                 // 悬浮按钮
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fragmentspecialeffects_text_serialnumber:                                 // 仿序列号
                ActivityUtils.startActivity(SerialNumberActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_other_audiowaveform:                               // 音频波形
                ActivityUtils.startActivity(AudioWaveformActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_other_lottery:                                     // 抽奖转盘
                ActivityUtils.startActivity(LotteryTurntableActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_other_spiderwebgrade:                              // 蜘蛛网等级
                ActivityUtils.startActivity(SpiderWebGradeActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_other_levitationbutton:                            // 悬浮按钮
                ActivityUtils.startActivity(LevitationButtonActivity.class);
                break;
        }
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {
//        if (lilaLoading != null){
//            lilaLoading.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void hideLoading() {
//        if (lilaLoading != null) {
//            lilaLoading.setVisibility(View.GONE);
//        }
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

    }

}
