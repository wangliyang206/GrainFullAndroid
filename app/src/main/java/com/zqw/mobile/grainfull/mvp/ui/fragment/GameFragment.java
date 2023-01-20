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
import com.zqw.mobile.grainfull.di.component.DaggerGameComponent;
import com.zqw.mobile.grainfull.mvp.contract.GameContract;
import com.zqw.mobile.grainfull.mvp.presenter.GamePresenter;
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
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fragmentgame_common_klotski:                                              // 华容道
                ActivityUtils.startActivity(klotskiGameActivity.class);
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
