package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerAircraftWarComponent;
import com.zqw.mobile.grainfull.mvp.contract.AircraftWarContract;
import com.zqw.mobile.grainfull.mvp.presenter.AircraftWarPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.aircraftwar.AircraftWar;

import butterknife.BindView;

/**
 * Description:飞机大战
 * <p>
 * Created on 2023/02/01 16:48
 *
 * @author 赤槿
 * module name is AircraftWarActivity
 */
public class AircraftWarActivity extends BaseActivity<AircraftWarPresenter> implements AircraftWarContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_aircraft_war)
    LinearLayout contentLayout;                                                                     // 主布局

    @BindView(R.id.view_aircraftwar_curtain)
    AircraftWar gameView;
    /*------------------------------------------------业务区域------------------------------------------------*/

    @Override
    protected void onPause() {
        super.onPause();
        if (gameView != null) {
            gameView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameView != null) {
            gameView.destroy();
        }
        gameView = null;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAircraftWarComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_aircraft_war;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("飞机大战");

        // 友盟统计 - 自定义事件
        MobclickAgent.onEvent(getApplicationContext(), "aircraft_war");

        //0:combatAircraft
        //1:explosion
        //2:yellowBullet
        //3:blueBullet
        //4:smallEnemyPlane
        //5:middleEnemyPlane
        //6:bigEnemyPlane
        //7:bombAward
        //8:bulletAward
        //9:pause1
        //10:pause2
        //11:bomb
        int[] bitmapIds = {
                R.drawable.plane,
                R.drawable.explosion,
                R.drawable.yellow_bullet,
                R.drawable.blue_bullet,
                R.drawable.small,
                R.drawable.middle,
                R.drawable.big,
                R.drawable.bomb_award,
                R.drawable.bullet_award,
                R.drawable.pause1,
                R.drawable.pause2,
                R.drawable.bomb
        };
        gameView.start(bitmapIds);
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