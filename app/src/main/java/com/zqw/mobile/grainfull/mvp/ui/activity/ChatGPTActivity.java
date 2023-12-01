package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.bumptech.glide.Glide;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.dialog.PopupChatGptMore;
import com.zqw.mobile.grainfull.app.global.AccountManager;
import com.zqw.mobile.grainfull.di.component.DaggerChatGPTComponent;
import com.zqw.mobile.grainfull.mvp.contract.ChatGPTContract;
import com.zqw.mobile.grainfull.mvp.model.entity.ChatToken;
import com.zqw.mobile.grainfull.mvp.presenter.ChatGPTPresenter;
import com.zqw.mobile.grainfull.mvp.ui.widget.AudioRecorderButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import timber.log.Timber;

/**
 * Description:
 * <p>
 * Created on 2023/11/23 15:49
 *
 * @author 赤槿
 * module name is ChatGPTActivity
 */
@RuntimePermissions
public class ChatGPTActivity extends BaseActivity<ChatGPTPresenter> implements ChatGPTContract.View {
    /*--------------------------------控件信息--------------------------------*/
    @BindView(R.id.radio_chatgpt_group)
    RadioGroup mRadioGroup;                                                                         // 切换版本
    @BindView(R.id.radio_chatgpt_x)
    RadioButton radioMinVersion;                                                                    // 小版本
    @BindView(R.id.radio_chatgpt_d)
    RadioButton radioMaxVersion;                                                                    // 大版本

    @BindView(R.id.imvi_chatgpt_more)
    ImageView imviMore;                                                                             // 更多

    @BindView(R.id.view_chatgpt_scrollView)
    NestedScrollView mScrollView;                                                                   // 外层 - 滑动布局
    @BindView(R.id.lila_chatgpt_chatlayout)
    LinearLayout lilaChatLayout;                                                                    // 消息总布局
    @BindView(R.id.edit_chatgpt_input)
    EditText editInput;                                                                             // 文字-输入框
    @BindView(R.id.view_chatgpt_voice)
    AudioRecorderButton viewVoice;                                                                  // 语音-按住说话
    @BindView(R.id.imvi_chatgpt_switch)
    ImageView imviVoiceOrText;                                                                      // 文字与语音-切换按钮
    @BindView(R.id.imvi_chatgpt_send)
    ImageView imviSend;                                                                             // 发送文字按钮


    // 接收的消息
    private TextView txviReceiveMsg;
    private ImageView imviReceiveMsg;
    /*--------------------------------业务信息--------------------------------*/
    @Inject
    AccountManager mAccountManager;

    // 额度
    private PopupChatGptMore mPopup;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mAccountManager = null;
        this.mPopup = null;
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
        lilaChatLayout.setOnTouchListener((v, event) -> {
            hideInput();
            return false;
        });

        editInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    imviSend.setVisibility(View.VISIBLE);
                    imviVoiceOrText.setVisibility(View.GONE);
                } else {
                    imviSend.setVisibility(View.GONE);
                    imviVoiceOrText.setVisibility(View.VISIBLE);
                }
            }
        });

        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_chatgpt_x) {
                mAccountManager.setChatGptVersion(false);
                radioMinVersion.setBackgroundResource(R.drawable.financial_order_selector);
                radioMaxVersion.setBackground(null);
            } else {
                mAccountManager.setChatGptVersion(true);
                radioMinVersion.setBackground(null);
                radioMaxVersion.setBackgroundResource(R.drawable.financial_order_selector);
            }
        });

        // 添加一条消息
        addLeftMsg("你好，我是Ai小助手，需要帮助吗？");

        // 初始化业务部分
        if (mPresenter != null) {
            mPresenter.initPresenter(mAccountManager.getChatGptSk());
        }
    }

    @OnClick({
            R.id.imvi_chatgpt_more,                                                                 // 更多
            R.id.imvi_chatgpt_switch,                                                               // 文字与语音-切换按钮
            R.id.imvi_chatgpt_send,                                                                 // 发送文字按钮
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvi_chatgpt_more:                                                            // 更多
                if (mPopup != null) {
                    mPopup.showAtLocation(v, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.imvi_chatgpt_switch:                                                          // 文字与语音-切换按钮
                if (editInput.isShown()) {
                    // 当前显示的是键盘，如果输入框中有文字，则执行发送事情，如果无内容，则执行切换事件
                    imviVoiceOrText.setImageResource(R.mipmap.icon_chat_softkeyboard);
                    editInput.setVisibility(View.GONE);
                    viewVoice.setVisibility(View.VISIBLE);
                    ChatGPTActivityPermissionsDispatcher.addVudioWithPermissionCheck(this);
                } else {
                    editInput.setVisibility(View.VISIBLE);
                    viewVoice.setVisibility(View.GONE);
                    imviVoiceOrText.setImageResource(R.mipmap.icon_chat_voice);
                }
                break;
            case R.id.imvi_chatgpt_send:                                                            // 发送文字按钮
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
            addLeftMsg("正在输入中...");

            if (mPresenter != null) {
                mPresenter.chatCreate(message);
            }
        }
    }

    /**
     * 添加右侧消息(显示我的消息)
     */
    private void addRightMsg(String text) {
        // 控制布局
        editInput.getText().clear();

        // 添加聊天布局
        LinearLayout viewRightMsg = LayoutInflater.from(this).inflate(R.layout.chat_right_textview, null).findViewById(R.id.chat_right_layout);
        TextView textView = viewRightMsg.findViewById(R.id.txvi_chatrightlayout_chat);
        textView.setText(text);
        if (viewRightMsg.getParent() != null) {
            ((ViewGroup) viewRightMsg.getParent()).removeView(viewRightMsg);
        }

        lilaChatLayout.addView(viewRightMsg);
    }

    /**
     * 添加左侧消息(显示对方消息)
     */
    public void addLeftMsg(String text) {
        LinearLayout viewLeftMsg = LayoutInflater.from(this).inflate(R.layout.chat_left_textview, null).findViewById(R.id.chat_left_layout);

        imviReceiveMsg = viewLeftMsg.findViewById(R.id.imvi_chatleftlayout_chat);
        txviReceiveMsg = viewLeftMsg.findViewById(R.id.txvi_chatleftlayout_chat);
        txviReceiveMsg.setText(text);
        if (viewLeftMsg.getParent() != null) {
            ((ViewGroup) viewLeftMsg.getParent()).removeView(viewLeftMsg);
        }

        lilaChatLayout.addView(viewLeftMsg);
    }

    /**
     * 获取API令牌额度
     */
    @Override
    public void loadTokenBalance(ChatToken mChatToken) {
        if (mPopup == null) {
            // 第一次进来，需要初始化
            mPopup = new PopupChatGptMore(getApplicationContext(), mChatToken.getTotal(), mChatToken.getUsed(), mChatToken.getRemaining(), mAccountManager.getChatGptSk(), sk -> {
                if (mPresenter != null) {
                    mPresenter.initPresenter(sk);
                }
            });

            imviMore.setVisibility(View.VISIBLE);
        } else {
            // 如果“已弹出”，直接刷新数据
            if (mPopup.isShowing()) {
                mPopup.onUpdate(mChatToken.getTotal(), mChatToken.getUsed(), mChatToken.getRemaining());
            }
        }
    }

    @Override
    public void loadSk() {
        // 如果“已弹出”，直接刷新数据
        if (mPopup.isShowing()) {
            mPopup.onUpdate(mAccountManager.getChatGptSk());
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ChatGPTActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 选择拍摄
     */
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void addVudio() {
        // 长按事件
        viewVoice.setOnLongClickListener(v -> {
            viewVoice.onStart();
            return false;
        });

        // 结束事件
        viewVoice.setOnAudioFinishRecorderListener((seconds, filePath) -> {
            Timber.i("##### filePath =%s", filePath);
        });
    }
}