package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerLevelSelectComponent;
import com.zqw.mobile.grainfull.mvp.contract.LevelSelectContract;
import com.zqw.mobile.grainfull.mvp.presenter.LevelSelectPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.LevelSelectView;

import butterknife.BindView;

/**
 * Description:进化图
 * <p>
 * Created on 2023/02/22 15:43
 *
 * @author 赤槿
 * module name is LevelSelectActivity
 */
public class LevelSelectActivity extends BaseActivity<LevelSelectPresenter> implements LevelSelectContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_level_select)
    LinearLayout contentLayout;                                                                     // 总布局

    @BindView(R.id.view_levelselect_select)
    LevelSelectView viewLevel;
    @BindView(R.id.imvi_levelselect_pic)
    ImageView imviPic;

    /*------------------------------------------------业务区域------------------------------------------------*/


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLevelSelectComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_level_select;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("进化图");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "level_select");
        viewLevel.setOnChangeListener(index -> {
            switch (index){
                case 0:
                    imviPic.setImageResource(R.mipmap.icon_egg);
                    break;
                case 1:
                    imviPic.setImageResource(R.mipmap.icon_tadpole);
                    break;
                case 2:
                    imviPic.setImageResource(R.mipmap.icon_youngfrog);
                    break;
                case 3:
                    imviPic.setImageResource(R.mipmap.icon_frog);
                    break;
            }
        });
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