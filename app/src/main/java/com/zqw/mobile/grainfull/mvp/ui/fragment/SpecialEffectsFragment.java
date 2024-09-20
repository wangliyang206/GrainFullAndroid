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
import com.zqw.mobile.grainfull.mvp.ui.activity.BottomSheetDialogActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.CardOverlapActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.ClockActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.CompassClockActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.DashboardActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.ElectronicClockActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.FlipClockActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.LayoutHomeActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.LevelSelectActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.LevitationButtonActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.LoadButtonActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.LocalVerificationCodeActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.LotteryTurntableActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.ProductDisplayActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.ProgressViewActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.RadarEffectActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.ScaleRulerActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.SerialNumberActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.SpiderWebGradeActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.StatisticalEffectsActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.TableFormatListActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.TemperatureActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.TiktokLikeActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.TrendChartActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.WaveEffectsActivity;

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
            R.id.btn_fragmentspecialeffects_layout_home,                                            // 模仿 - 首页
            R.id.btn_fragmentspecialeffects_layout_cardoverlap,                                     // 卡片重叠滑动 - 探探效果

            R.id.btn_fragmentspecialeffects_text_serialnumber,                                      // 仿序列号
            R.id.btn_fragmentspecialeffects_text_localcode,                                         // 本地验证码

            R.id.btn_fragmentspecialeffects_button_loadbutton,                                      // 加载按钮

            R.id.btn_fragmentspecialeffects_clock_clock,                                            // 时钟
            R.id.btn_fragmentspecialeffects_clock_flipclock,                                        // 翻转时钟
            R.id.btn_fragmentspecialeffects_clock_compassclock,                                     // 罗盘时钟
            R.id.btn_fragmentspecialeffects_clock_electronicclock,                                  // 电子时钟

            R.id.btn_fragmentspecialeffects_other_audiowaveform,                                    // 音频波形
            R.id.btn_fragmentspecialeffects_other_lottery,                                          // 抽奖转盘
            R.id.btn_fragmentspecialeffects_other_spiderwebgrade,                                   // 蜘蛛网等级
            R.id.btn_fragmentspecialeffects_other_progress,                                         // 进度条
            R.id.btn_fragmentspecialeffects_other_levitationbutton,                                 // 悬浮窗口/按钮
            R.id.btn_fragmentspecialeffects_other_dashboard,                                        // 仪表盘
            R.id.btn_fragmentspecialeffects_other_scaleruler,                                       // 刻度尺
            R.id.btn_fragmentspecialeffects_other_evolution,                                        // 进化图
            R.id.btn_fragmentspecialeffects_other_temperature,                                      // 升温效果
            R.id.btn_fragmentspecialeffects_other_productdisplay,                                   // 商品 - 横滑动
            R.id.btn_fragmentspecialeffects_other_radar,                                            // 雷达效果
            R.id.btn_fragmentspecialeffects_other_trendchart,                                       // 趋势图
            R.id.btn_fragmentspecialeffects_other_statisticaleffects,                               // 统计效果
            R.id.btn_fragmentspecialeffects_other_like,                                             // 点赞效果
            R.id.btn_fragmentspecialeffects_other_wave,                                             // 水波纹效果
            R.id.btn_fragmentspecialeffects_other_bottomsheet,                                      // 底部弹出框
            R.id.btn_fragmentspecialeffects_other_TableFormatList,                                  // 表格式列表
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fragmentspecialeffects_layout_home:                                       // 模仿 - 首页
                ActivityUtils.startActivity(LayoutHomeActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_layout_cardoverlap:                                // 卡片重叠滑动 - 探探效果
                ActivityUtils.startActivity(CardOverlapActivity.class);
                break;

            case R.id.btn_fragmentspecialeffects_text_serialnumber:                                 // 仿序列号
                ActivityUtils.startActivity(SerialNumberActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_text_localcode:                                    // 本地验证码
                ActivityUtils.startActivity(LocalVerificationCodeActivity.class);
                break;

            case R.id.btn_fragmentspecialeffects_button_loadbutton:                                 // 加载按钮
                ActivityUtils.startActivity(LoadButtonActivity.class);
                break;

            case R.id.btn_fragmentspecialeffects_clock_clock:                                       // 时钟
                ActivityUtils.startActivity(ClockActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_clock_flipclock:                                   // 翻转时钟
                ActivityUtils.startActivity(FlipClockActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_clock_compassclock:                                // 罗盘时钟
                ActivityUtils.startActivity(CompassClockActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_clock_electronicclock:                             // 电子时钟
                ActivityUtils.startActivity(ElectronicClockActivity.class);
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
            case R.id.btn_fragmentspecialeffects_other_progress:                                    // 进度条
                ActivityUtils.startActivity(ProgressViewActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_other_levitationbutton:                            // 悬浮窗口/按钮
                ActivityUtils.startActivity(LevitationButtonActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_other_dashboard:                                   // 仪表盘
                ActivityUtils.startActivity(DashboardActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_other_scaleruler:                                  // 刻度尺
                ActivityUtils.startActivity(ScaleRulerActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_other_evolution:                                   // 进化图
                ActivityUtils.startActivity(LevelSelectActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_other_temperature:                                 // 升温效果
                ActivityUtils.startActivity(TemperatureActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_other_productdisplay:                              // 商品 - 横滑动
                ActivityUtils.startActivity(ProductDisplayActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_other_radar:                                       // 雷达效果
                ActivityUtils.startActivity(RadarEffectActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_other_trendchart:                                  // 趋势图
                ActivityUtils.startActivity(TrendChartActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_other_statisticaleffects:                          // 统计效果
                ActivityUtils.startActivity(StatisticalEffectsActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_other_like:                                        // 点赞效果
                ActivityUtils.startActivity(TiktokLikeActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_other_wave:                                        // 水波纹效果
                ActivityUtils.startActivity(WaveEffectsActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_other_bottomsheet:                                 // 底部弹出框
                ActivityUtils.startActivity(BottomSheetDialogActivity.class);
                break;
            case R.id.btn_fragmentspecialeffects_other_TableFormatList:                             // 表格式列表
                ActivityUtils.startActivity(TableFormatListActivity.class);
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
