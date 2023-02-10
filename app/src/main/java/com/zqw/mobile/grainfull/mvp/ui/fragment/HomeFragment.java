package com.zqw.mobile.grainfull.mvp.ui.fragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.EventBusTags;
import com.zqw.mobile.grainfull.di.component.DaggerHomeComponent;
import com.zqw.mobile.grainfull.mvp.contract.HomeContract;
import com.zqw.mobile.grainfull.mvp.model.entity.MainEvent;
import com.zqw.mobile.grainfull.mvp.presenter.HomePresenter;
import com.zqw.mobile.grainfull.mvp.ui.activity.AudioConversionActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduFaceRecognitionActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduSpeechSynthesisActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduVoiceRecogActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.ChineseToPinyinActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.ColorCodeConversionActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.ColorPickerActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.DecisionActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.DigitalToChineseActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.DynamicGesturesActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.EquipmentInfoActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.FaceComparisonActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.HandheldBulletScreenActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.IdentifyBankCardsActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.IdentifyIdCardActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.ImageExtractionTextActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.LivenessDetectionActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.MagnifierActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.MetalDetectorActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.NoiseMeasurementActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.PictureMosaicActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.PicturePipetteActivity;
import com.zqw.mobile.grainfull.mvp.ui.widget.VerticalScrollTextView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 首页(业务)
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View, View.OnClickListener {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.imvi_fragmenthome_head)
    CircleImageView imviHead;                                                                       // 头像

    @BindView(R.id.lila_fragmenthome_search)
    LinearLayout lilaSearch;                                                                        // 搜索
    @BindView(R.id.vstv_fragmenthome_view)
    VerticalScrollTextView txviSearchHot;                                                           // 搜索热门关键字

    @BindView(R.id.lila_fragmenthome_nottargeted)
    LinearLayout lilaNotTargeted;                                                                   // 定位失败界面
    @BindView(R.id.lila_fragmenthome_loading)
    View lilaLoading;                                                                               // Loading
    /*------------------------------------------------业务区域------------------------------------------------*/
    @Inject
    ImageLoader mImageLoader;

    @Override
    public void onStart() {
        super.onStart();
        txviSearchHot.startPlay();
    }

    @Override
    public void onResume() {
        super.onResume();

        // 初始化首页
        initView();
    }

    @Override
    public void onPause() {
        super.onPause();
        txviSearchHot.stopPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mImageLoader = null;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 初始化控件
     */
    private void initView() {
        if (mPresenter != null) {
            // 初始化
            mPresenter.initHome();
        }
    }

    /**
     * 刷新数据
     */
    private void onRefresh() {
        // 获取首页数据
        if (mPresenter != null) {
            mPresenter.getHome();
        }
    }

    /**
     * 设置头像
     */
    @Override
    public void homeSetAvatar(String url) {
        if (TextUtils.isEmpty(url)) {
            mImageLoader.loadImage(getContext(),
                    ImageConfigImpl
                            .builder()
                            .fallback(R.drawable.profile6)
                            .placeholder(R.mipmap.ic_home_head)
                            .imageView(imviHead).build());
        } else {
            mImageLoader.loadImage(getContext(),
                    ImageConfigImpl
                            .builder()
                            .url(url)
                            .fallback(R.mipmap.ic_home_head)
                            .placeholder(R.mipmap.ic_home_head)
                            .imageView(imviHead).build());
        }
    }

    @OnClick({
            R.id.lila_fragmenthome_loading,                                                         // Loading
            R.id.imvi_fragmenthome_head,                                                            // 点击头像
            R.id.btn_home_manual,                                                                   // 定位失败-手动定位
            R.id.btn_home_load,                                                                     // 定位失败-重新加载
            R.id.lila_fragmenthome_search,                                                          // 拜访搜索

            R.id.btn_fragmenthome_bankcards,                                                        // 识别银行卡
            R.id.btn_fragmenthome_idcard,                                                           // 识别身份证
            R.id.btn_fragmenthome_imagetotext,                                                      // 图片提取文字
            R.id.btn_fragmenthome_gesture,                                                          // 动态手势识别
            R.id.btn_fragmenthome_livenessdetection,                                                // 活体检测
            R.id.btn_fragmenthome_facecomparison,                                                   // 人脸对比

            R.id.btn_fragmenthome_facerecognition,                                                  // 人脸采集
            R.id.btn_fragmenthome_voicerecog,                                                       // 语音识别
            R.id.btn_fragmenthome_speechsynthesis,                                                  // 语音合成

            R.id.btn_fragmenthome_color_colorpicker,                                                // 取色板
            R.id.btn_fragmenthome_color_picturepipette,                                             // 吸管工具
            R.id.btn_fragmenthome_color_codeconversion,                                             // 颜色码转换

            R.id.btn_fragmenthome_text_digital,                                                     // 数字转中文
            R.id.btn_fragmenthome_text_chinesetopinyin,                                             // 中文转拼音
            R.id.btn_fragmenthome_text_bulletchat,                                                  // 手持弹幕

            R.id.btn_fragmenthome_image_picturemosaic,                                              // 图片拼接

            R.id.btn_fragmenthome_equipment_info,                                                   // 查看设备信息
            R.id.btn_fragmenthome_equipment_metaldetector,                                          // 金属探测仪
            R.id.btn_fragmenthome_equipment_noisemeasurement,                                       // 噪音测量

            R.id.btn_fragmenthome_audioconversion,                                                  // 音频转换
            R.id.btn_fragmenthome_other_decision,                                                   // 做个决定
            R.id.btn_fragmenthome_other_magnifier,                                                  // 放大镜
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvi_fragmenthome_head:                                                       // 点击头像
                // 点击头像，打开侧滑
                EventBus.getDefault().post(new MainEvent(EventBusTags.OPEN_MENU_TAG), EventBusTags.HOME_TAG);
                break;
            case R.id.btn_home_manual:                                                              // 定位失败-手动定位
                showMessage("暂未开放！");
                break;
            case R.id.btn_home_load:                                                                // 定位失败-重新加载
                // 初始化首页内容
                initView();
                break;
            case R.id.lila_fragmenthome_search:                                                     // 拜访搜索
                break;

            case R.id.btn_fragmenthome_bankcards:                                                   // 识别银行卡
                ActivityUtils.startActivity(IdentifyBankCardsActivity.class);
                break;
            case R.id.btn_fragmenthome_idcard:                                                      // 识别身份证
                ActivityUtils.startActivity(IdentifyIdCardActivity.class);
                break;
            case R.id.btn_fragmenthome_imagetotext:                                                 // 图片提取文字
                ActivityUtils.startActivity(ImageExtractionTextActivity.class);
                break;
            case R.id.btn_fragmenthome_gesture:                                                     // 动态手势识别
                ActivityUtils.startActivity(DynamicGesturesActivity.class);
                break;
            case R.id.btn_fragmenthome_livenessdetection:                                           // 活体检测
                ActivityUtils.startActivity(LivenessDetectionActivity.class);
                break;
            case R.id.btn_fragmenthome_facecomparison:                                              // 人脸对比
                ActivityUtils.startActivity(FaceComparisonActivity.class);
                break;

            case R.id.btn_fragmenthome_facerecognition:                                             // 人脸采集
                ActivityUtils.startActivity(BaiduFaceRecognitionActivity.class);
                break;
            case R.id.btn_fragmenthome_voicerecog:                                                  // 语音识别
                ActivityUtils.startActivity(BaiduVoiceRecogActivity.class);
                break;
            case R.id.btn_fragmenthome_speechsynthesis:                                             // 语音合成
                ActivityUtils.startActivity(BaiduSpeechSynthesisActivity.class);
                break;

            case R.id.btn_fragmenthome_color_colorpicker:                                           // 取色板
                ActivityUtils.startActivity(ColorPickerActivity.class);
                break;
            case R.id.btn_fragmenthome_color_picturepipette:                                        // 吸管工具
                ActivityUtils.startActivity(PicturePipetteActivity.class);
                break;
            case R.id.btn_fragmenthome_color_codeconversion:                                        // 颜色码转换
                ActivityUtils.startActivity(ColorCodeConversionActivity.class);
                break;

            case R.id.btn_fragmenthome_text_digital:                                                // 数字转中文
                ActivityUtils.startActivity(DigitalToChineseActivity.class);
                break;
            case R.id.btn_fragmenthome_text_chinesetopinyin:                                        // 中文转拼音
                ActivityUtils.startActivity(ChineseToPinyinActivity.class);
                break;
            case R.id.btn_fragmenthome_text_bulletchat:                                             // 手持弹幕
                ActivityUtils.startActivity(HandheldBulletScreenActivity.class);
                break;

            case R.id.btn_fragmenthome_image_picturemosaic:                                         // 图片拼接
                ActivityUtils.startActivity(PictureMosaicActivity.class);
                break;

            case R.id.btn_fragmenthome_equipment_info:                                              // 查看设备信息
                ActivityUtils.startActivity(EquipmentInfoActivity.class);
                break;
            case R.id.btn_fragmenthome_equipment_metaldetector:                                     // 金属探测仪
                ActivityUtils.startActivity(MetalDetectorActivity.class);
                break;
            case R.id.btn_fragmenthome_equipment_noisemeasurement:                                  // 噪音测量
                ActivityUtils.startActivity(NoiseMeasurementActivity.class);
                break;

            case R.id.btn_fragmenthome_audioconversion:                                             // 音频转换
                ActivityUtils.startActivity(AudioConversionActivity.class);
                break;
            case R.id.btn_fragmenthome_other_decision:                                              // 做个决定
                ActivityUtils.startActivity(DecisionActivity.class);
                break;
            case R.id.btn_fragmenthome_other_magnifier:                                             // 放大镜
                ActivityUtils.startActivity(MagnifierActivity.class);
                break;

        }
    }

    /**
     * 首页特殊事件    回调
     */
    @Subscriber(tag = EventBusTags.HOME_TAG, mode = ThreadMode.POST)
    private void eventBusEvent(MainEvent mainEvent) {
        // 切换区域
        if (mainEvent.getCode() == EventBusTags.SWITCH_CITIES_TAG) {
            // 隐藏定位失败界面
            if (lilaNotTargeted != null)
                lilaNotTargeted.setVisibility(View.GONE);
            // 重新请求数据
            onDoubleClick();
        }
    }

    /**
     * 热门关键字
     */
    @Override
    public void homeSearchHot(List<String> list) {
        txviSearchHot.setDataSource(list);
    }

    /**
     * 定位失败
     */
    @Override
    public void homemPositioningFailure() {
        // 显示布局
        if (lilaNotTargeted != null)
            lilaNotTargeted.setVisibility(View.VISIBLE);
    }

    /**
     * 设置城市
     */
    @Override
    public void homeSetLocateAdd(String str) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {
        // 隐藏定位失败界面
        if (lilaNotTargeted != null)
            lilaNotTargeted.setVisibility(View.GONE);
        // 加载Loading
//        if (lilaLoading != null)
//            lilaLoading.setVisibility(View.VISIBLE);

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

    /**
     * 双击刷新
     */
    public void onDoubleClick() {
        onRefresh();
    }

}
