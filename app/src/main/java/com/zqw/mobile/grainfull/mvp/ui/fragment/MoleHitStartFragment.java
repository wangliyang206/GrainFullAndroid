package com.zqw.mobile.grainfull.mvp.ui.fragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerMoleHitComponent;
import com.zqw.mobile.grainfull.mvp.contract.MoleHitContract;
import com.zqw.mobile.grainfull.mvp.presenter.MoleHitPresenter;
import com.zqw.mobile.grainfull.mvp.ui.activity.WhacAMoleActivity;

import butterknife.BindView;

/**
 * Description : 打地鼠游戏开始页
 */
public class MoleHitStartFragment extends BaseFragment<MoleHitPresenter> implements MoleHitContract.View, View.OnClickListener {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.id_start_next)
    View mNextView;

    /*------------------------------------------------业务区域------------------------------------------------*/

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerMoleHitComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_start, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 初始化
        mNextView.setSelected(true);
        mNextView.setOnClickListener(v -> ((WhacAMoleActivity) mContext).changePage(1));
    }

    @Override
    public void onClick(View v) {

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
