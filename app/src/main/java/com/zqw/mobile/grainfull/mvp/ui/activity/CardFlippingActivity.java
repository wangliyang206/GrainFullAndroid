package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerCardFlippingComponent;
import com.zqw.mobile.grainfull.mvp.contract.CardFlippingContract;
import com.zqw.mobile.grainfull.mvp.model.entity.CardFlipping;
import com.zqw.mobile.grainfull.mvp.presenter.CardFlippingPresenter;
import com.zqw.mobile.grainfull.mvp.ui.adapter.CardFlippingAdapter;
import com.zqw.mobile.grainfull.mvp.ui.widget.anim.FlipCardAnimation;

import javax.inject.Inject;

import butterknife.BindView;
import tyrantgit.explosionfield.ExplosionField;

/**
 * Description:卡牌消消乐
 * <p>
 * Created on 2023/07/10 17:00
 *
 * @author 赤槿
 * module name is CardFlippingActivity
 */
public class CardFlippingActivity extends BaseActivity<CardFlippingPresenter> implements CardFlippingContract.View, DefaultAdapter.OnRecyclerViewItemClickListener {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_card_flipping)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.txvi_cardflipping_steps)
    TextView txviSteps;                                                                             // 步数
    @BindView(R.id.revi_cardflipping_content)
    RecyclerView mRecyclerView;                                                                     // 卡牌
    /*------------------------------------------------业务区域------------------------------------------------*/
    @Inject
    GridLayoutManager mLayoutManager;

    @Inject
    CardFlippingAdapter mAdapter;                                                                   // 内容适配器
    // 翻转中
    private boolean isFlipping;
    // 当前操作步数
    private int steps = 0;
    // 上一次数据
    private CardFlipping lastInfo;
    private View lastView;
    // 爆炸散落动画对象
    private ExplosionField mExplosionField;

    @Override
    protected void onDestroy() {
        // super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(mRecyclerView);
        super.onDestroy();
        this.mRecyclerView = null;
        if (mExplosionField != null) {
            this.mExplosionField.clear();
            this.mExplosionField = null;
        }

    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCardFlippingComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_card_flipping;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("卡牌消消乐");

        // 先初始化载入ExplosionField
        mExplosionField = ExplosionField.attach2Window(this);
        initRecyclerView();

        if (mPresenter != null) {
            mPresenter.init();
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    /**
     * 列表点击事件
     */
    @Override
    public void onItemClick(@NonNull View view, int viewType, @NonNull Object data, int position) {
        // 拿到点击item数据
        CardFlipping info = (CardFlipping) data;
        // 非翻转中 并且 是反面时才允许翻牌
        if (!isFlipping && !info.isDisplayFront()) {
            startAnimationFront(view, info);
        }
    }

    /**
     * 翻转动画 - 由反面转为正面
     */
    private void startAnimationFront(View view, CardFlipping info) {
        int width = view.getWidth() / 2;
        int height = view.getHeight() / 2;
        FlipCardAnimation animationToFront = new FlipCardAnimation(0, 180, width, height);
        animationToFront.setInterpolator(new AnticipateOvershootInterpolator());
        animationToFront.setDuration(1500);
        animationToFront.setFillAfter(false);
        animationToFront.setRepeatCount(0);
        animationToFront.FlipDirection(false);
        animationToFront.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isFlipping = true;
                steps++;
                // 显示当前步数
                txviSteps.setText(String.valueOf(steps));
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isFlipping = false;
                // 判断掀开的卡片是否满足两张(不满足时还可以继续掀牌)
                if (lastView != null) {
                    // 判断是否一样
                    if (info.getSign() == lastInfo.getSign()) {
                        // 两次标志一样，将消除两张卡牌
                        info.setDisplayFront(true);
                        info.setDisappear(true);
                        lastInfo.setDisappear(true);
                        mExplosionField.explode(lastView);
                        mExplosionField.explode(view);
                        lastInfo = null;
                        lastView = null;
                        // 检查是否完成，完成后弹出成绩
                        if(mAdapter.isSucc()){
                            // 已完成
                            showMessage("恭喜您，已完成！");
                        }
                    } else {
                        // 两次标志不一样，将两张卡牌翻转
                        startAnimationBack(lastView, lastInfo);
                        startAnimationBack(view, info);
                    }
                    return;
                }

                // 还不满足，可以继续掀卡牌
                info.setDisplayFront(true);
                lastInfo = info;
                lastView = view;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                ((FlipCardAnimation) animation).setCanContentChange();
            }
        });
        animationToFront.setOnContentChangeListener(() -> {
            ImageView imviBg = view.findViewById(R.id.imvi_cardflippingitem_bg);
            ImageView imviContent = view.findViewById(R.id.imvi_cardflippingitem_content);
            imviBg.setImageResource(info.getImageBg());
            imviContent.setVisibility(View.VISIBLE);
            imviContent.setImageResource(info.getImageContent());
        });
        view.startAnimation(animationToFront);
    }

    /**
     * 翻转动画 - 由正面转为反面
     */
    private void startAnimationBack(View view, CardFlipping info) {
        int width = view.getWidth() / 2;
        int height = view.getHeight() / 2;
        FlipCardAnimation animationToBack = new FlipCardAnimation(0, -180, width, height);
        animationToBack.setInterpolator(new AnticipateOvershootInterpolator());
        animationToBack.setDuration(1500);
        animationToBack.setFillAfter(false);
        animationToBack.setRepeatCount(0);
        animationToBack.FlipDirection(false);
        animationToBack.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isFlipping = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isFlipping = false;
                info.setDisplayFront(false);
                info.setDisappear(false);
                lastInfo = null;
                lastView = null;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                ((FlipCardAnimation) animation).setCanContentChange();
            }
        });
        animationToBack.setOnContentChangeListener(() -> {
            ImageView imviBg = view.findViewById(R.id.imvi_cardflippingitem_bg);
            ImageView imviContent = view.findViewById(R.id.imvi_cardflippingitem_content);
            imviBg.setImageResource(R.mipmap.tw_card);
            imviContent.setVisibility(View.GONE);
        });
        view.startAnimation(animationToBack);
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