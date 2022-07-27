package com.zqw.mobile.grainfull.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baidu.aip.asrwakeup3.core.mini.AutoCheck;
import com.baidu.aip.asrwakeup3.core.recog.IStatus;
import com.baidu.aip.asrwakeup3.core.recog.MyRecognizer;
import com.baidu.aip.asrwakeup3.core.recog.listener.IRecogListener;
import com.baidu.aip.asrwakeup3.core.recog.listener.MessageStatusRecogListener;
import com.baidu.aip.asrwakeup3.uiasr.params.CommonRecogParams;
import com.baidu.aip.asrwakeup3.uiasr.params.OfflineRecogParams;
import com.baidu.aip.asrwakeup3.uiasr.params.OnlineRecogParams;
import com.baidu.speech.asr.SpeechConstant;
import com.blankj.utilcode.util.ActivityUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.grainfull.BuildConfig;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.dialog.IdentifyDialog;
import com.zqw.mobile.grainfull.di.component.DaggerBaiduVoiceRecogComponent;
import com.zqw.mobile.grainfull.mvp.contract.BaiduVoiceRecogContract;
import com.zqw.mobile.grainfull.mvp.presenter.BaiduVoiceRecogPresenter;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Description:百度AI - 短语音识别
 * <p>
 * Created on 2022/07/25 17:21
 *
 * @author 赤槿
 * module name is BaiduVoiceRecogActivity
 */
public class BaiduVoiceRecogActivity extends BaseActivity<BaiduVoiceRecogPresenter> implements BaiduVoiceRecogContract.View, IStatus {
    /*------------------------------------------控件信息------------------------------------------*/
    @BindView(R.id.txvi_voicerecog_error)
    TextView txviError;

    @BindView(R.id.btn_voicerecog_start)
    Button btnStart;
    /*------------------------------------------业务信息------------------------------------------*/
    // 弹出结果框
    private IdentifyDialog mResult;
    /**
     * Api的参数类，仅仅用于生成调用START的json字符串，本身与SDK的调用无关
     */
    private CommonRecogParams apiParams;

    /**
     * 识别控制器，使用MyRecognizer控制识别的流程
     */
    protected MyRecognizer myRecognizer;

    /**
     * 本Activity中是否需要调用离线命令词功能。根据此参数，判断是否需要调用SDK的ASR_KWS_LOAD_ENGINE事件
     */
    protected boolean enableOffline;

    /**
     * 控制UI按钮的状态
     */
    protected int status;

    /**
     * 销毁时需要释放识别资源。
     */
    @Override
    public void onDestroy() {

        // 如果之前调用过myRecognizer.loadOfflineEngine()， release()里会自动调用释放离线资源
        // 基于DEMO5.1 卸载离线资源(离线时使用) release()方法中封装了卸载离线资源的过程
        // 基于DEMO的5.2 退出事件管理器
        myRecognizer.release();

        super.onDestroy();

        if (mResult != null) {
            mResult.dismiss();
            mResult = null;
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBaiduVoiceRecogComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_baidu_voice_recog;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("语音识别");

        initRecog();
    }

    private void initRecog() {
        // 初始化Result弹框
        mResult = new IdentifyDialog(this);

        apiParams = new OnlineRecogParams();
        apiParams.initSamplePath(this);
        status = STATUS_NONE;

        // 基于DEMO集成第1.1, 1.2, 1.3 步骤 初始化EventManager类并注册自定义输出事件
        // DEMO集成步骤 1.2 新建一个回调类，识别引擎会回调这个类告知重要状态和识别结果
        IRecogListener listener = new MessageStatusRecogListener(handler);
        // DEMO集成步骤 1.1 1.3 初始化：new一个IRecogListener示例 & new 一个 MyRecognizer 示例,并注册输出事件
        myRecognizer = new MyRecognizer(this, listener);
        if (enableOffline) {
            // 基于DEMO集成1.4 加载离线资源步骤(离线时使用)。offlineParams是固定值，复制到您的代码里即可
            Map<String, Object> offlineParams = OfflineRecogParams.fetchOfflineParams();
            myRecognizer.loadOfflineEngine(offlineParams);
        }

    }

    @OnClick({
            R.id.btn_voicerecog_start,                                                              // 语音识别
            R.id.imvi_voicerecog_setting,                                                           // 设置
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_voicerecog_start:                                                         // 语音识别
                onBtn();
                break;
            case R.id.imvi_voicerecog_setting:                                                      // 设置
                ActivityUtils.startActivity(BaiduVoiceOnlineSettingActivity.class);
                break;
        }
    }

    private void onBtn() {
        switch (status) {
            case STATUS_NONE: // 初始状态
                start();
                status = STATUS_WAITING_READY;
                updateBtnTextByStatus();
                txviError.setText("");

                if(mResult != null){
                    mResult.setData("");
                }
                break;
            case STATUS_WAITING_READY: // 调用本类的start方法后，即输入START事件后，等待引擎准备完毕。
            case STATUS_READY: // 引擎准备完毕。
            case STATUS_SPEAKING: // 用户开始讲话
            case STATUS_FINISHED: // 一句话识别语音结束
            case STATUS_RECOGNITION: // 识别中
                stop();
                status = STATUS_STOPPED; // 引擎识别中
                updateBtnTextByStatus();
                break;
            case STATUS_LONG_SPEECH_FINISHED: // 长语音识别结束
            case STATUS_STOPPED: // 引擎识别中
                cancel();
                status = STATUS_NONE; // 识别结束，回到初始状态
                updateBtnTextByStatus();
                break;
            default:
                break;
        }
    }

    private void updateBtnTextByStatus() {
        switch (status) {
            case STATUS_NONE:
                btnStart.setText("开始录音");
                btnStart.setEnabled(true);

                break;
            case STATUS_WAITING_READY:
            case STATUS_READY:
            case STATUS_SPEAKING:
            case STATUS_RECOGNITION:
                btnStart.setText("停止录音");
                btnStart.setEnabled(true);

                break;
            case STATUS_LONG_SPEECH_FINISHED:
            case STATUS_STOPPED:
                btnStart.setText("取消整个识别过程");
                btnStart.setEnabled(true);

                break;
            default:
                break;
        }
    }

    /**
     * 开始录音，点击“开始”按钮后调用。
     * 基于DEMO集成2.1, 2.2 设置识别参数并发送开始事件
     */
    private void start() {
        // DEMO集成步骤2.1 拼接识别参数： 此处params可以打印出来，直接写到你的代码里去，最终的json一致即可。
        final Map<String, Object> params = fetchParams();
        if (BuildConfig.DEBUG) {
            params.put(SpeechConstant.APP_ID, getString(R.string.baidu_app_id_debug));
            params.put(SpeechConstant.APP_KEY, getString(R.string.baidu_map_api_key_debug));
            params.put(SpeechConstant.SECRET, getString(R.string.baidu_secret_key_debug));
        } else {
            params.put(SpeechConstant.APP_ID, getString(R.string.baidu_app_id));
            params.put(SpeechConstant.APP_KEY, getString(R.string.baidu_map_api_key));
            params.put(SpeechConstant.SECRET, getString(R.string.baidu_secret_key));
        }

        // params 也可以根据文档此处手动修改，参数会以json的格式在界面和logcat日志中打印
        Timber.i("设置的start输入参数：%s", params);
        // 复制此段可以自动检测常规错误
        (new AutoCheck(getApplicationContext(), new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainErrorMessage(); // autoCheck.obtainAllMessage();
//                        txtLog.append(message + "\n");
                        Timber.i("======" + message + "\n");

                        ; // 可以用下面一行替代，在logcat中查看代码
                        // Log.w("AutoCheckMessage", message);
                    }
                }
            }
        }, enableOffline)).checkAsr(params);

        // 这里打印出params， 填写至您自己的app中，直接调用下面这行代码即可。
        // DEMO集成步骤2.2 开始识别
        myRecognizer.start(params);
    }

    /**
     * 开始录音后，手动点击“停止”按钮。
     * SDK会识别不会再识别停止后的录音。
     * 基于DEMO集成4.1 发送停止事件 停止录音
     */
    private void stop() {
        myRecognizer.stop();
    }

    /**
     * 开始录音后，手动点击“取消”按钮。
     * SDK会取消本次识别，回到原始状态。
     * 基于DEMO集成4.2 发送取消事件 取消本次识别
     */
    private void cancel() {
        myRecognizer.cancel();
    }

    private Map<String, Object> fetchParams() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        //  上面的获取是为了生成下面的Map， 自己集成时可以忽略
        Map<String, Object> params = apiParams.fetch(sp);
        //  集成时不需要上面的代码，只需要params参数。
        return params;
    }

    private Handler handler = new Handler() {

        /*
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handleMsg(msg);
        }

    };

    private void handleMsg(Message msg) {
        if (msg.obj != null) {
            Timber.i("###" + msg.obj.toString() + "\n");
            txviError.append(msg.obj.toString() + "\n");
        }

        switch (msg.what) { // 处理MessageStatusRecogListener中的状态回调
            case STATUS_FINISHED:
                if (msg.arg2 == 1) {
                    // 识别成功
                    // ###识别结束，结果是“经过了几次对读者问题解决之后，我打算添加这一环节，非常有必要。”；说话结束到识别结束耗时【312ms】
                    Timber.i("####" + msg.obj.toString() + "\n");
                    // 将识别的结果做一次格式化，去掉(识别结束，结果是“)和(”；说话结束到识别结束耗时【312ms】)
                    String result = "";
                    try {
                        result = msg.obj.toString().substring(
                                9,
                                msg.obj.toString().lastIndexOf("”"));
                    } catch (Exception ex) {
                        result = msg.obj.toString();
                    }
                    // 弹出对话框，显示结果
                    mResult.show();
                    mResult.setAppend(result);
                }
                status = msg.what;
                updateBtnTextByStatus();
                break;
            case STATUS_NONE:
            case STATUS_READY:
            case STATUS_SPEAKING:
            case STATUS_RECOGNITION:
                status = msg.what;
                updateBtnTextByStatus();
                break;
            default:
                break;

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