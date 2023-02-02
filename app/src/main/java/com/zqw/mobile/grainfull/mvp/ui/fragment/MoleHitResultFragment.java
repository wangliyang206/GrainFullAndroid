package com.zqw.mobile.grainfull.mvp.ui.fragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.BackgroundMusic;
import com.zqw.mobile.grainfull.app.utils.SoundPoolManager;
import com.zqw.mobile.grainfull.di.component.DaggerMoleHitComponent;
import com.zqw.mobile.grainfull.mvp.contract.MoleHitContract;
import com.zqw.mobile.grainfull.mvp.presenter.MoleHitPresenter;
import com.zqw.mobile.grainfull.mvp.ui.activity.WhacAMoleActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description : 打地鼠游戏结算页
 */
public class MoleHitResultFragment extends BaseFragment<MoleHitPresenter> implements MoleHitContract.View, View.OnClickListener {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.fragment_molehit_result)
    FrameLayout contentLayout;                                                                      // 主布局
    @BindView(R.id.txvi_molehitresult_rate)
    TextView mGameRateView;
    @BindView(R.id.txvi_molehitresult_pride_text)
    TextView mPrideTextView;

    /*------------------------------------------------业务区域------------------------------------------------*/
    private SoundPoolManager mSoundManager;
    private BackgroundMusic mBgMusicManager;
    private boolean playOnce;


    @Override
    public void onResume() {
        super.onResume();

        if (!playOnce) {
            mGameRateView.postDelayed(() -> {
                mSoundManager.play(0);
                mBgMusicManager.playBackgroundMusic("win.mp3", false);
            }, 750);
            playOnce = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != mBgMusicManager) {
            mBgMusicManager.stopBackgroundMusic();
            mBgMusicManager.end();
        }
    }

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
        return inflater.inflate(R.layout.fragment_molehit_result, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 初始化声音效果
        initSoundEffect();
        // 加载积分
        doLoadData();
    }

    /**
     * 初始化声音效果
     */
    private void initSoundEffect() {

        mSoundManager = new SoundPoolManager();
        int[] effect = new int[]{
                R.raw.amazing
        };
        mSoundManager.init(mContext, effect);

        mBgMusicManager = new BackgroundMusic(mContext);
    }

    /**
     * 加载积分
     */
    private void doLoadData() {
        int score = ((WhacAMoleActivity) mContext).getScore();
        contentLayout.postDelayed(() -> {
            try {
                String ratingText;
                if (score > 20) {
                    ratingText = "游戏达人";
                    mPrideTextView.setText("你看起来很厉害的样子 ✲(✿◕ฺ ∀◕✿)✲");
                } else if (score > 10) {
                    ratingText = "新手玩家";
                    mPrideTextView.setText("你已经很棒了 ✲(✿◕ฺ ∀◕✿)✲");
                } else {
                    ratingText = "小白一个";
                    mPrideTextView.setText("再接再厉哦，加油！ ✲(✿◕ฺ ∀◕✿)✲");
                }
                mGameRateView.setText(ratingText);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, 500);
    }

    @OnClick({
            R.id.btn_molehitresult_start,                                                                   // 再来一局
    })
    @Override
    public void onClick(View v) {
        ((WhacAMoleActivity) mContext).changePage(1);
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
