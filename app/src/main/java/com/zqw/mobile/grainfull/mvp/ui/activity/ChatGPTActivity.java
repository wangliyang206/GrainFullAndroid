package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.bumptech.glide.Glide;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.di.component.DaggerChatGPTComponent;
import com.zqw.mobile.grainfull.mvp.contract.ChatGPTContract;
import com.zqw.mobile.grainfull.mvp.presenter.ChatGPTPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:
 * <p>
 * Created on 2023/11/23 15:49
 *
 * @author 赤槿
 * module name is ChatGPTActivity
 */
public class ChatGPTActivity extends BaseActivity<ChatGPTPresenter> implements ChatGPTContract.View {
    /*--------------------------------控件信息--------------------------------*/
    @BindView(R.id.view_chatgpt_scrollView)
    NestedScrollView mScrollView;                                                                   // 外层 - 滑动布局
    @BindView(R.id.lila_chatgpt_chatlayout)
    LinearLayout lilaChatLayout;                                                                    // 消息总布局
    @BindView(R.id.edit_chatgpt_input)
    EditText editInput;                                                                             // 消息输入框
    @BindView(R.id.btn_chatgpt_send)
    Button btnSend;                                                                                 // 发送消息按钮

    // 接收的消息
    private TextView txviReceiveMsg;
    private ImageView imviReceiveMsg;
    /*--------------------------------业务信息--------------------------------*/

    /**
     * 将状态栏改为浅色、深色模式(状态栏 icon 和字体，false = 浅色，true = 深色)
     */
    public boolean useLightStatusBar() {
        return false;
    }

    /**
     * 根据主题使用不同的颜色。
     * 如果想要纯透明，则需要重写此方法，返回值为 -1 即可。
     */
    public int useStatusBarColor() {
        return getResources().getColor(R.color.colorPrimary);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerChatGPTComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_chat_gpt;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("ChatGPT");

        // 添加一条消息
        addLeftMsg("你好，我是Ai小助手，需要帮助吗？", R.color.c_f2f3f5);
    }

    @OnClick({
            R.id.btn_chatgpt_send,                                                                  // 发送消息按钮
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chatgpt_send:                                                             // 发送消息按钮
                onSend();
                break;
        }
    }

    /**
     * 发送消息
     */
    private void onSend() {
        String message = editInput.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            showMessage("请输入内容！");
        } else {
            // 隐藏软键盘
            hideInput();
            // 在界面上显示“我”发出的消息
            addRightMsg(message);
            // 在界面上显示一条提示“对方，正在输入中……”
            addLeftMsg("正在输入中...", R.color.c_f2f3f5);

            if (mPresenter != null) {
                mPresenter.chatCreate(1, message);
            }
        }
    }

    /**
     * 添加右侧消息(显示我的消息)
     */
    private void addRightMsg(String text) {
        // 控制布局
        editInput.getText().clear();
        btnSend.setEnabled(false);
        btnSend.setText("别急");

        // 添加聊天布局
        LinearLayout mLinearLayoutAdd = LayoutInflater.from(this).inflate(R.layout.chat_right_textview, null).findViewById(R.id.chat_right_layout);
        mLinearLayoutAdd.setBackground(ContextCompat.getDrawable(this, R.color.c_f7f8fa));
        TextView textView = mLinearLayoutAdd.findViewById(R.id.txvi_chatrightlayout_chat);
        textView.setText(text);
        if (mLinearLayoutAdd.getParent() != null) {
            ((ViewGroup) mLinearLayoutAdd.getParent()).removeView(mLinearLayoutAdd);
        }

        lilaChatLayout.addView(mLinearLayoutAdd);
    }

    /**
     * 添加左侧消息(显示对方消息)
     */
    private void addLeftMsg(String text, int color) {
        LinearLayout mLinearLayoutAdd = LayoutInflater.from(this).inflate(R.layout.chat_left_textview, null).findViewById(R.id.chat_left_layout);

        mLinearLayoutAdd.setBackground(ContextCompat.getDrawable(this, color));
        imviReceiveMsg = mLinearLayoutAdd.findViewById(R.id.imvi_chatleftlayout_chat);
        txviReceiveMsg = mLinearLayoutAdd.findViewById(R.id.txvi_chatleftlayout_chat);
        txviReceiveMsg.setText(text);
        if (mLinearLayoutAdd.getParent() != null) {
            ((ViewGroup) mLinearLayoutAdd.getParent()).removeView(mLinearLayoutAdd);
        }

        lilaChatLayout.addView(mLinearLayoutAdd);
    }

    /**
     * 加载聊天消息
     */
    @Override
    public void onLoadMessage(StringBuffer info) {
        runOnUiThread(() -> {
            imviReceiveMsg.setVisibility(View.GONE);
            txviReceiveMsg.setVisibility(View.VISIBLE);

            // response返回拼接
            txviReceiveMsg.setText(info.toString());

            mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        });
    }

    /**
     * 加载图片消息
     */
    @Override
    public void onLoadImages(String url) {
        imviReceiveMsg.setVisibility(View.VISIBLE);
        txviReceiveMsg.setVisibility(View.GONE);
        Glide.with(imviReceiveMsg).load(url).into(imviReceiveMsg);

        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    /**
     * 完成一次会话
     */
    @Override
    public void onSucc() {
        runOnUiThread(() -> {
            mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            btnSend.setEnabled(true);
            btnSend.setText("发送");
        });
    }

    /**
     * 隐藏软键盘
     */
    private void hideInput() {
        KeyboardUtils.hideSoftInput(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideInput();
        return super.onTouchEvent(event);
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