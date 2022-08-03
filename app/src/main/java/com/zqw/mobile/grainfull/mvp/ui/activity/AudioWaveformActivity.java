package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerAudioWaveformComponent;
import com.zqw.mobile.grainfull.mvp.contract.AudioWaveformContract;
import com.zqw.mobile.grainfull.mvp.presenter.AudioWaveformPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.VisualizeView;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:音频波形 效果
 * <p>
 * Created on 2022/08/03 14:42
 *
 * @author 赤槿
 * module name is AudioWaveformActivity
 */
public class AudioWaveformActivity extends BaseActivity<AudioWaveformPresenter> implements AudioWaveformContract.View {
    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.vivi_audiowaveform_graphics)
    VisualizeView visualizerView;

    /*------------------------------------------业务信息------------------------------------------*/
    // 模式
    private int MODE = VisualizeView.SINGLE;
    // 控制模式
    private int index = 1;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAudioWaveformComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_audio_waveform;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("音频波形");

        // 设置颜色
        visualizerView.setColor(android.R.color.black);
    }

    @OnClick({
            R.id.btn_audiowaveform_model,                                                           // 切换模式
            R.id.btn_audiowaveform_play,                                                            // 播放
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_audiowaveform_model:                                                      // 切换模式
                switch (index) {
                    case 1:
                        visualizerView.setColor(R.color.colorPrimary);
                        MODE = VisualizeView.CIRCLE;
                        index++;
                        break;
                    case 2:
                        visualizerView.setColor(R.color.colorPrimary);
                        MODE = VisualizeView.NET;
                        index++;
                        break;
                    case 3:
                        MODE = VisualizeView.REFLECT;
                        index++;
                        break;
                    case 4:
                        MODE = VisualizeView.WAVE;
                        index++;
                        break;
                    case 5:
                        visualizerView.setColor(android.R.color.black);
                        MODE = VisualizeView.GRAIN;
                        index++;
                        break;
                    case 6:
                        visualizerView.setColor(android.R.color.black);
                        MODE = VisualizeView.SINGLE;
                        index = 1;
                        break;
                }

                visualizerView.setMode(MODE);
                break;
            case R.id.btn_audiowaveform_play:                                                       // 播放
                if (mPresenter != null) {
                    mPresenter.onPlay();
                }
                break;
        }
    }

    @Override
    public void loadView() {
        // 设置数据源
        visualizerView.setData(getData());
    }

    /**
     * 生成数据源
     */
    private float[] getData() {
        int length = 513;
        float[] model = new float[length];

        for (int i = 0; i < length; i++) {
            model[i] = (float) nextDouble(0.0, 150.0000000);
        }
        return model;
    }

    /**
     * 生成max到min范围的浮点数
     */
    public double nextDouble(final double min, final double max) {
        return min + ((max - min) * new Random().nextDouble());
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