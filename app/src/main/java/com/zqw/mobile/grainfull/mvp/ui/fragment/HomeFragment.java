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
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduFaceActivationActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduFaceRecognitionActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduSpeechSynthesisActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.BaiduVoiceRecogActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.DynamicGesturesActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.FaceComparisonActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.IdentifyBankCardsActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.IdentifyIdCardActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.ImageExtractionTextActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.LivenessDetectionActivity;
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
 * ??????(??????)
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View, View.OnClickListener {
    /*------------------------------------------------????????????------------------------------------------------*/
    @BindView(R.id.imvi_fragmenthome_head)
    CircleImageView imviHead;                                                                       // ??????

    @BindView(R.id.lila_fragmenthome_search)
    LinearLayout lilaSearch;                                                                        // ??????
    @BindView(R.id.vstv_fragmenthome_view)
    VerticalScrollTextView txviSearchHot;                                                           // ?????????????????????

    @BindView(R.id.lila_fragmenthome_nottargeted)
    LinearLayout lilaNotTargeted;                                                                   // ??????????????????
    @BindView(R.id.lila_fragmenthome_loading)
    View lilaLoading;                                                                               // Loading
    /*------------------------------------------------????????????------------------------------------------------*/
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

        // ???????????????
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
     * ???????????????
     */
    private void initView() {
        if (mPresenter != null) {
            // ?????????
            mPresenter.initHome();
        }
    }

    /**
     * ????????????
     */
    private void onRefresh() {
        // ??????????????????
        if (mPresenter != null) {
            mPresenter.getHome();
        }
    }

    /**
     * ????????????
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
            R.id.imvi_fragmenthome_head,                                                            // ????????????
            R.id.btn_home_manual,                                                                   // ????????????-????????????
            R.id.btn_home_load,                                                                     // ????????????-????????????
            R.id.lila_fragmenthome_search,                                                          // ????????????
            R.id.btn_fragmenthome_bankcards,                                                        // ???????????????
            R.id.btn_fragmenthome_idcard,                                                           // ???????????????
            R.id.btn_fragmenthome_imagetotext,                                                      // ??????????????????
            R.id.btn_fragmenthome_gesture,                                                          // ??????????????????
            R.id.btn_fragmenthome_livenessdetection,                                                // ????????????
            R.id.btn_fragmenthome_facerecognition,                                                  // ??????????????????
            R.id.btn_fragmenthome_facecomparison,                                                   // ????????????
            R.id.btn_fragmenthome_voicerecog,                                                       // ????????????
            R.id.btn_fragmenthome_speechsynthesis,                                                  // ????????????
    })
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.imvi_fragmenthome_head:                                                       // ????????????
                // ???????????????????????????
                EventBus.getDefault().post(new MainEvent(EventBusTags.OPEN_MENU_TAG), EventBusTags.HOME_TAG);
                break;
            case R.id.btn_home_manual:                                                              // ????????????-????????????
                showMessage("???????????????");
                break;
            case R.id.btn_home_load:                                                                // ????????????-????????????
                // ?????????????????????
                initView();
                break;
            case R.id.lila_fragmenthome_search:                                                     // ????????????
                break;
            case R.id.btn_fragmenthome_bankcards:                                                   // ???????????????
                ActivityUtils.startActivity(IdentifyBankCardsActivity.class);
                break;
            case R.id.btn_fragmenthome_idcard:                                                      // ???????????????
                ActivityUtils.startActivity(IdentifyIdCardActivity.class);
                break;
            case R.id.btn_fragmenthome_imagetotext:                                                 // ??????????????????
                ActivityUtils.startActivity(ImageExtractionTextActivity.class);
                break;
            case R.id.btn_fragmenthome_gesture:                                                     // ??????????????????
                ActivityUtils.startActivity(DynamicGesturesActivity.class);
                break;
            case R.id.btn_fragmenthome_livenessdetection:                                           // ????????????
                ActivityUtils.startActivity(LivenessDetectionActivity.class);
                break;
            case R.id.btn_fragmenthome_facerecognition:                                             // ??????????????????
                ActivityUtils.startActivity(BaiduFaceRecognitionActivity.class);
                break;
            case R.id.btn_fragmenthome_facecomparison:                                              // ????????????
                ActivityUtils.startActivity(FaceComparisonActivity.class);
                break;
            case R.id.btn_fragmenthome_voicerecog:                                                  // ????????????
                ActivityUtils.startActivity(BaiduVoiceRecogActivity.class);
                break;
            case R.id.btn_fragmenthome_speechsynthesis:                                             // ????????????
                ActivityUtils.startActivity(BaiduSpeechSynthesisActivity.class);
                break;
        }
    }

    /**
     * ??????????????????    ??????
     */
    @Subscriber(tag = EventBusTags.HOME_TAG, mode = ThreadMode.POST)
    private void eventBusEvent(MainEvent mainEvent) {
        // ????????????
        if (mainEvent.getCode() == EventBusTags.SWITCH_CITIES_TAG) {
            // ????????????????????????
            if (lilaNotTargeted != null)
                lilaNotTargeted.setVisibility(View.GONE);
            // ??????????????????
            onDoubleClick();
        }
    }

    /**
     * ???????????????
     */
    @Override
    public void homeSearchHot(List<String> list) {
        txviSearchHot.setDataSource(list);
    }

    /**
     * ????????????
     */
    @Override
    public void homemPositioningFailure() {
        // ????????????
        if (lilaNotTargeted != null)
            lilaNotTargeted.setVisibility(View.VISIBLE);
    }

    /**
     * ????????????
     */
    @Override
    public void homeSetLocateAdd(String str) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {
        // ????????????????????????
        if (lilaNotTargeted != null)
            lilaNotTargeted.setVisibility(View.GONE);
        // ??????Loading
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
     * ????????????
     */
    public void onDoubleClick() {
        onRefresh();
    }

}
