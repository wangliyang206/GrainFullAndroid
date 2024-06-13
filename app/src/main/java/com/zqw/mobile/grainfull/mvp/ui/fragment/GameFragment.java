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
import com.umeng.analytics.MobclickAgent;
import com.unity3d.player.UnityPlayerActivity;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerGameComponent;
import com.zqw.mobile.grainfull.mvp.contract.GameContract;
import com.zqw.mobile.grainfull.mvp.presenter.GamePresenter;
import com.zqw.mobile.grainfull.mvp.ui.activity.ARFaceChangingActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.ARPortalActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.AircraftWarActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.CardFlippingActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.ElfinPlayerActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.OneLineToEndActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.WhacAMoleActivity;
import com.zqw.mobile.grainfull.mvp.ui.activity.klotskiGameActivity;

import butterknife.OnClick;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.fragment
 * @ClassName: GameFragment
 * @Description: 游戏
 * @Author: WLY
 * @CreateDate: 2023/1/20 17:36
 */
public class GameFragment extends BaseFragment<GamePresenter> implements GameContract.View, View.OnClickListener {
    /*------------------------------------------------控件信息------------------------------------------------*/

    /*------------------------------------------------业务区域------------------------------------------------*/

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerGameComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({
            R.id.btn_fragmentgame_common_klotski,                                                   // 华容道
            R.id.btn_fragmentgame_common_aircraftwar,                                               // 飞机大战
            R.id.btn_fragmentgame_common_whacamole,                                                 // 打地鼠
            R.id.btn_fragmentgame_common_onepen,                                                    // 一笔画完
            R.id.btn_fragmentgame_common_cardflipping,                                              // 卡牌消消乐

            R.id.btn_fragmentgame_unity_stickpin,                                                   // 见缝插针
            R.id.btn_fragmentgame_unity_elfin,                                                      // 小精灵3D模型
            R.id.btn_fragmentgame_unity_facecapture,                                                // 脸谱变脸
            R.id.btn_fragmentgame_unity_portal,                                                     // 传送门
    })
    @Override
    public void onClick(View v) {
        Bundle mBundle = new Bundle();
        switch (v.getId()) {
            case R.id.btn_fragmentgame_common_klotski:                                              // 华容道
                ActivityUtils.startActivity(klotskiGameActivity.class);
                break;
            case R.id.btn_fragmentgame_common_aircraftwar:                                          // 飞机大战
                ActivityUtils.startActivity(AircraftWarActivity.class);
                break;
            case R.id.btn_fragmentgame_common_whacamole:                                            // 打地鼠
                ActivityUtils.startActivity(WhacAMoleActivity.class);
                break;
            case R.id.btn_fragmentgame_common_onepen:                                               // 一笔画完
                ActivityUtils.startActivity(OneLineToEndActivity.class);
                break;
            case R.id.btn_fragmentgame_common_cardflipping:                                         // 卡牌消消乐
                ActivityUtils.startActivity(CardFlippingActivity.class);
                break;

            case R.id.btn_fragmentgame_unity_stickpin:                                              // 见缝插针
                mBundle.putInt("layout", 4);

                // 友盟统计 - 自定义事件
                MobclickAgent.onEvent(getContext(), "unity_stickpin_open");

                ActivityUtils.startActivity(mBundle, UnityPlayerActivity.class);
                break;
            case R.id.btn_fragmentgame_unity_elfin:                                                 // 小精灵3D模型
                ActivityUtils.startActivity(ElfinPlayerActivity.class);
                break;
            case R.id.btn_fragmentgame_unity_facecapture:                                           // 脸谱变脸
                ActivityUtils.startActivity(ARFaceChangingActivity.class);
                break;
            case R.id.btn_fragmentgame_unity_portal:                                                // 传送门
                ActivityUtils.startActivity(ARPortalActivity.class);
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
