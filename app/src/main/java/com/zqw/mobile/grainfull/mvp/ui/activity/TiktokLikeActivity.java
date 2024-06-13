package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerTiktokLikeComponent;
import com.zqw.mobile.grainfull.mvp.contract.TiktokLikeContract;
import com.zqw.mobile.grainfull.mvp.presenter.TiktokLikePresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.DYLikeLayout;
import com.zqw.mobile.grainfull.mvp.ui.widget.DYLoadingView;
import com.zqw.mobile.grainfull.mvp.ui.widget.likebutton.DYLikeView;
import com.zqw.mobile.grainfull.mvp.ui.widget.toutiao.ArticleRl;

import butterknife.BindView;

/**
 * Description:点赞效果
 * <p>
 * Created on 2024/03/12 15:39
 *
 * @author 赤槿
 * module name is TiktokLikeActivity
 */
public class TiktokLikeActivity extends BaseActivity<TiktokLikePresenter> implements TiktokLikeContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.loading_view)
    DYLoadingView dyLoadingView;

    @BindView(R.id.view_tiktoklike_article)
    ArticleRl articleRl;
    @BindView(R.id.btn_tiktoklike_click)
    View btnTouTiaoClick;


    @BindView(R.id.dy_like_layout)
    DYLikeLayout dyLikeLayout;
    @BindView(R.id.dy_like_button)
    DYLikeView dyLikeButton;
    /*------------------------------------------------业务区域------------------------------------------------*/


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTiktokLikeComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_tiktok_like;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("点赞效果");


        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "tiktok_like_open");
        loadTouTiao();
        loadDouYin();
    }

    private void loadTouTiao(){
        btnTouTiaoClick.setOnTouchListener(new View.OnTouchListener() {
            int x;
            int y;
            long lastDownTime;
            Runnable mLongPressed = new Runnable() {
                @Override
                public void run() {
                    articleRl.setVisibility(View.VISIBLE);
                    articleRl.setThumb(x, y);
                    articleRl.postDelayed(this, 50);
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    lastDownTime = System.currentTimeMillis();
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    articleRl.postDelayed(mLongPressed, 100);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (System.currentTimeMillis() - lastDownTime < 100) {//判断为单击事件
                        articleRl.setVisibility(View.VISIBLE);
                        articleRl.setThumb(x, y);
                        articleRl.removeCallbacks(mLongPressed);
                    } else {//判断为长按事件后松开
                        articleRl.removeCallbacks(mLongPressed);
                    }
                }
                return true;
            }
        });
    }

    private void loadDouYin(){
        dyLikeLayout.setLikeClickCallBack(new DYLikeLayout.LikeClickCallBack() {
            @Override
            public void onLikeListener() {
                //多击监听
                if (!dyLikeButton.isLiked()) {
                    dyLikeButton.performClick();
                }
            }

            @Override
            public void onSingleListener() {
                //单击监听

            }
        });
    }

    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {
        dyLoadingView.setVisibility(View.VISIBLE);
        dyLoadingView.start();
    }

    @Override
    public void hideLoading() {
        dyLoadingView.stop();
        dyLoadingView.setVisibility(View.GONE);
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