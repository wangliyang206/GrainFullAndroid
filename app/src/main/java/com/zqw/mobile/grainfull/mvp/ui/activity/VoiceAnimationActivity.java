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
import com.zqw.mobile.grainfull.di.component.DaggerVoiceAnimationComponent;
import com.zqw.mobile.grainfull.mvp.contract.VoiceAnimationContract;
import com.zqw.mobile.grainfull.mvp.presenter.VoiceAnimationPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.GradientTextView;
import com.zqw.mobile.grainfull.mvp.ui.widget.SpeechAnimationView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description: 语音动画视图
 * <p>
 * Created on 2024/12/23 12:38
 *
 * @author 赤槿
 * module name is VoiceAnimationActivity
 */
public class VoiceAnimationActivity extends BaseActivity<VoiceAnimationPresenter> implements VoiceAnimationContract.View {

    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.view_voiceanimation_speechAnimation)
    SpeechAnimationView speechAnimationView;

    @BindView(R.id.txvi_voiceanimation_hear)
    GradientTextView viewHear;
    @BindView(R.id.txvi_voiceanimation_waitfor)
    GradientTextView viewWaitfor;
    @BindView(R.id.txvi_voiceanimation_connect)
    GradientTextView viewConnect;
    @BindView(R.id.txvi_voiceanimation_speech)
    GradientTextView viewSpeech;

    /*------------------------------------------业务信息------------------------------------------*/

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerVoiceAnimationComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_voice_animation;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("语音动画视图");

    }

    @OnClick({
            R.id.txvi_voiceanimation_hear,                                                          // 听
            R.id.txvi_voiceanimation_waitfor,                                                       // 等待
            R.id.txvi_voiceanimation_connect,                                                       // 连接
            R.id.txvi_voiceanimation_speech,                                                        // 讲话
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txvi_voiceanimation_hear:
                speechAnimationView.setAnimationType(SpeechAnimationView.AnimationType.LISTENING);
                setViewText(viewHear);
                break;
            case R.id.txvi_voiceanimation_waitfor:
                speechAnimationView.setAnimationType(SpeechAnimationView.AnimationType.WAITING);
                setViewText(viewWaitfor);
                break;
            case R.id.txvi_voiceanimation_connect:
                speechAnimationView.setAnimationType(SpeechAnimationView.AnimationType.Connecting);
                setViewText(viewConnect);
                break;
            case R.id.txvi_voiceanimation_speech:
                speechAnimationView.setAnimationType(SpeechAnimationView.AnimationType.ROBOT_SPEAKING);
                setViewText(viewSpeech);
                break;
        }
    }

    private void setViewText(GradientTextView viewText) {
        // 未选中
        setSelectedTextColor(viewHear, false);
        viewHear.setBackgroundResource(0);
        setSelectedTextColor(viewWaitfor, false);
        viewWaitfor.setBackgroundResource(0);
        setSelectedTextColor(viewConnect, false);
        viewConnect.setBackgroundResource(0);
        setSelectedTextColor(viewSpeech, false);
        viewSpeech.setBackgroundResource(0);

        // 选中
        setSelectedTextColor(viewText, true);
        viewText.setBackgroundResource(R.drawable.bg_function_selector_selected);
    }

    /**
     * 设置所选文本颜色
     */
    private void setSelectedTextColor(GradientTextView gradientTextView, boolean selected) {
        if (null != gradientTextView) {
            if (selected) {
                gradientTextView.setStartColor(0xFF4CA9F8);
                gradientTextView.setEndColor(0xFF4DCFE1);
            } else {
                gradientTextView.setStartColor(0);
                gradientTextView.setEndColor(0);
            }
        }
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