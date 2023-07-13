package com.zqw.mobile.grainfull.mvp.ui.fragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerLayoutHomeComponent;
import com.zqw.mobile.grainfull.mvp.contract.LayoutHomeContract;
import com.zqw.mobile.grainfull.mvp.presenter.LayoutHomePresenter;

import butterknife.BindView;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.ui.fragment
 * @ClassName: LayoutOtherFragment
 * @Description: 其它 - 首页 - 其它
 * @Author: WLY
 * @CreateDate: 2023/7/14 16:46
 */
public class LayoutOtherFragment extends BaseFragment<LayoutHomePresenter> implements LayoutHomeContract.View, View.OnClickListener {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.fragment_layouthome_other)
    LinearLayout contentLayout;                                                                     // 主布局
    @BindView(R.id.txvi_layouthomeother_tips)
    TextView txviTips;                                                                              // 提示
    /*------------------------------------------------业务区域------------------------------------------------*/
    // 类型：1论坛，2选车，3新车特卖，4我的
    private int type;

    public static LayoutOtherFragment instantiate(int position) {
        LayoutOtherFragment fragment = new LayoutOtherFragment();
        fragment.type = position;
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerLayoutHomeComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layouthome_other, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        if (type == 1) {
            txviTips.setText("论坛");
        } else if (type == 2) {
            txviTips.setText("选车");
        } else if (type == 3) {
            txviTips.setText("新车特卖");
        } else if (type == 4) {
            txviTips.setText("我的");
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setData(@Nullable Object data) {

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

    }

}
