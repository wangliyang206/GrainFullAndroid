package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerWhacAMoleComponent;
import com.zqw.mobile.grainfull.mvp.contract.WhacAMoleContract;
import com.zqw.mobile.grainfull.mvp.presenter.WhacAMolePresenter;
import com.zqw.mobile.grainfull.mvp.ui.fragment.MoleHitResultFragment;
import com.zqw.mobile.grainfull.mvp.ui.fragment.MoleHitMainFragment;
import com.zqw.mobile.grainfull.mvp.ui.fragment.MoleHitStartFragment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Description: 打地鼠(鼹鼠撞击)
 * <p>
 * Created on 2023/02/02 09:20
 *
 * @author 赤槿
 * module name is WhacAMoleActivity
 */
public class WhacAMoleActivity extends BaseActivity<WhacAMolePresenter> implements WhacAMoleContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_whac_amole)
    LinearLayout contentLayout;                                                                     // 主布局

    /*------------------------------------------------业务区域------------------------------------------------*/
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private MoleHitStartFragment mStartFragment;
    private MoleHitMainFragment mGameFragment;
    private MoleHitResultFragment mResultFragment;
    private int score;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerWhacAMoleComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_whac_amole;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("打地鼠");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "mole_hit");

        initPage();
        changePage(0);
    }

    private void initPage() {
        mFragmentList.clear();
        mStartFragment = new MoleHitStartFragment();
        mGameFragment = new MoleHitMainFragment();
        mResultFragment = new MoleHitResultFragment();
        mFragmentList.add(mStartFragment);
        mFragmentList.add(mGameFragment);
        mFragmentList.add(mResultFragment);
    }

    /**
     * 更改界面
     */
    public void changePage(int index) {
        try {
            Fragment fragment = mFragmentList.get(index);
            if (null != fragment) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.id_game_root, fragment);
                fragmentTransaction.commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 告知游戏分数
     *
     * @param score 分数
     */
    public void postScore(int score) {
        try {
            this.score = score;
            changePage(2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 对外提供积分
     */
    public int getScore() {
        return score;
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