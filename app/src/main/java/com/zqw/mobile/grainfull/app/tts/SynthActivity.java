package com.zqw.mobile.grainfull.app.tts;

import static com.zqw.mobile.grainfull.app.tts.MainHandlerConstant.PRINT;
import static com.zqw.mobile.grainfull.app.tts.MainHandlerConstant.UI_CHANGE_INPUT_TEXT_SELECTION;
import static com.zqw.mobile.grainfull.app.tts.MainHandlerConstant.UI_CHANGE_SYNTHES_TEXT_SELECTION;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Pair;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.zqw.mobile.grainfull.BuildConfig;
import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.global.Constant;
import com.zqw.mobile.grainfull.app.tts.control.InitConfig;
import com.zqw.mobile.grainfull.app.tts.control.MySyntherizer;
import com.zqw.mobile.grainfull.app.tts.listener.FileSaveListener;
import com.zqw.mobile.grainfull.app.tts.util.AutoCheck;
import com.zqw.mobile.grainfull.app.tts.util.IOfflineResourceConst;
import com.zqw.mobile.grainfull.app.tts.util.OfflineResource;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * 合成demo。
 * 离在线合成SDK含在线和离线模型，纯离线合成SDK额外含有纯离线功能。
 * <p>
 * <p>
 * TtsMode.MIX 离在线合成根据网络状况优先走在线，在线时访问服务器失败后转为离线。
 * TtsMode.ONLINE 一直在线合成
 * TtsMode.OFFLINE 纯离线合成，一直离线。需要纯离线SDK
 */
public class SynthActivity {
// ================== 完整版初始化参数设置开始 ==========================
    /**
     * 发布时请替换成自己申请的appId appKey 和 secretKey。注意如果需要离线合成功能,请在您申请的应用中填写包名。
     * 本demo的包名是com.baidu.tts.sample，定义在build.gradle中。
     */
    protected String appId;

    protected String appKey;

    protected String secretKey;

    protected String sn; // 纯离线合成SDK授权码；离在线合成SDK没有此参数

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； TtsMode.OFFLINE 纯离线合成，需要纯离线SDK
    protected TtsMode ttsMode = IOfflineResourceConst.DEFAULT_SDK_TTS_MODE;

    protected boolean isOnlineSDK = TtsMode.ONLINE.equals(IOfflineResourceConst.DEFAULT_SDK_TTS_MODE);


    // 离线发音选择，VOICE_FEMALE即为离线女声发音。
    // assets目录下bd_etts_common_speech_m15_mand_eng_high_am-mix_vXXXXXXX.dat为离线男声模型文件；
    // assets目录下bd_etts_common_speech_f7_mand_eng_high_am-mix_vXXXXX.dat为离线女声模型文件;
    // assets目录下bd_etts_common_speech_yyjw_mand_eng_high_am-mix_vXXXXX.dat 为度逍遥模型文件;
    // assets目录下bd_etts_common_speech_as_mand_eng_high_am_vXXXX.dat 为度丫丫模型文件;
    // 在线合成sdk下面的参数不生效
    protected String offlineVoice = OfflineResource.VOICE_MALE;

    // ===============初始化参数设置完毕，更多合成参数请至getParams()方法中设置 =================
    // 主控制类，所有合成控制方法从这个类开始
    protected MySyntherizer synthesizer;


    // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>, 其它发音人见文档
    public static String SPEAKER = "0";
    // 设置合成的音量，0-15 ，默认 5
    public static String VOLUME = "10";
    // 设置合成的语速，0-15 ，默认 5
    public static String SPEED = "5";
    // 设置合成的语调，0-15 ，默认 5
    public static String PITCH = "5";
    // 句柄
    private Context mContext;
    private Handler mainHandler;

    /**
     * 初始化语音合成
     */
    public void initTTS(Context context) {
        this.mContext = context;
        Timber.i("###赤槿###initTTS()");
        mainHandler = new Handler() {
            /*
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                handle(msg);
            }

        };

        if (BuildConfig.DEBUG) {
            appId = mContext.getString(R.string.baidu_app_id_debug);
            appKey = mContext.getString(R.string.baidu_map_api_key_debug);
            secretKey = mContext.getString(R.string.baidu_secret_key_debug);
        } else {
            appId = mContext.getString(R.string.baidu_app_id);
            appKey = mContext.getString(R.string.baidu_map_api_key);
            secretKey = mContext.getString(R.string.baidu_secret_key);
        }

        // 初始化TTS引擎
        initialTts();
    }

    /**
     * 初始化引擎，需要的参数均在InitConfig类里
     * <p>
     * DEMO中提供了3个SpeechSynthesizerListener的实现
     * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
     * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
     */
    private void initialTts() {
        LoggerProxy.printable(true); // 日志打印在logcat中
//        // 设置初始化参数
//        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
//        SpeechSynthesizerListener listener = new UiMessageListener(mainHandler);
//        InitConfig config = getInitConfig(listener);
//        synthesizer = new NonBlockSyntherizer(mContext, config, mainHandler); // 此处可以改为MySyntherizer 了解调用过程


        // 保存录音文件
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        SpeechSynthesizerListener listener = new FileSaveListener(mainHandler, Constant.AUDIO_PATH);

        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = getInitConfig(listener);
        synthesizer = new MySyntherizer(mContext, initConfig, mainHandler); // 此处可以改为MySyntherizer 了解调用过程
    }

    /**
     * 合成并播放
     * speak 实际上是调用 synthesize后，获取音频流，然后播放。
     * 获取音频流的方式见SaveFileActivity及FileSaveListener
     * 需要合成的文本text的长度不能超过1024个GBK字节。
     */
    public void speak(String val) {
        // 合成前可以修改参数：
        // Map<String, String> params = getParams();
        // params.put(SpeechSynthesizer.PARAM_SPEAKER, "3"); // 设置为度逍遥
        // synthesizer.setParams(params);
        int result = synthesizer.speak(val);
    }

    /**
     * 合成但是不播放，
     * 音频流保存为文件的方法可以参见SaveFileActivity及FileSaveListener
     */
    public void synthesize(String text) {
        int result = synthesizer.synthesize(text);
    }

    /**
     * 批量播放
     */
    public void batchSpeak(List<Pair<String, String>> texts) {
//        List<Pair<String, String>> texts = new ArrayList<>();
//        texts.add(new Pair<>("开始批量播放，", "a0"));
//        texts.add(new Pair<>("123456，", "a1"));
//        texts.add(new Pair<>("欢迎使用百度语音，，，", "a2"));
//        texts.add(new Pair<>("重(chong2)量这个是多音字示例", "a3"));
        int result = synthesizer.batchSpeak(texts);
    }

    /**
     * 赤槿说明：切换离线资源
     */
    private void switchOffTheOfflineResource() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Holo_Light_Dialog);
        builder.setTitle("引擎空闲时切换");
        final Map<String, String> map = new LinkedHashMap<>(4);
        map.put("离线女声", OfflineResource.VOICE_FEMALE);
        map.put("离线男声", OfflineResource.VOICE_MALE);
        map.put("离线度逍遥", OfflineResource.VOICE_DUXY);
        map.put("离线度丫丫", OfflineResource.VOICE_DUYY);
        final String[] keysTemp = new String[4];
        final String[] keys = map.keySet().toArray(keysTemp);
        builder.setItems(keys, (dialog, which) -> loadModel(map.get(keys[which])));
        builder.show();
    }

    /**
     * 在线合成sdk，这个方法不会被调用。
     * <p>
     * 切换离线发音。注意需要添加额外的判断：引擎在合成时该方法不能调用
     */
    private void loadModel(String mode) {
        offlineVoice = mode;
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        toPrint("切换离线语音：" + offlineResource.getModelFilename());
        int result = synthesizer.loadModel(offlineResource.getModelFilename(), offlineResource.getTextFilename());
    }

    /**
     * 暂停播放。仅调用speak后生效
     */
    public void pause() {
        int result = synthesizer.pause();
    }


    /**
     * 继续播放。仅调用speak后生效，调用pause生效
     */
    public void resume() {
        int result = synthesizer.resume();
    }

    /**
     * 停止合成引擎。即停止播放，合成，清空内部合成队列。
     */
    public void stop() {
        int result = synthesizer.stop();
    }

    public void onDestroy() {
        if (synthesizer != null) {
            synthesizer.release();
            synthesizer = null;
        }
    }

    protected InitConfig getInitConfig(SpeechSynthesizerListener listener) {
        Map<String, String> params = getParams();
        // 添加你自己的参数
        InitConfig initConfig;
        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        if (sn == null) {
            initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);
        } else {
            initConfig = new InitConfig(appId, appKey, secretKey, sn, ttsMode, params, listener);
        }
        // 如果您集成中出错，请将下面一段代码放在和demo中相同的位置，并复制InitConfig 和 AutoCheck到您的项目中
        // 上线时请删除AutoCheck的调用
        AutoCheck.getInstance(mContext).check(initConfig, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainDebugMessage();
                        toPrint(message); // 可以用下面一行替代，在logcat中查看代码
                        // Log.w("AutoCheckMessage", message);
                    }
                }
            }

        });
        return initConfig;
    }

    /**
     * 重新设置合成参数
     */
    public void setParams() {
        Map<String, String> params = new HashMap<>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>, 其它发音人见文档
        params.put(SpeechSynthesizer.PARAM_SPEAKER, SPEAKER);
        // 设置合成的音量，0-15 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, VOLUME);
        // 设置合成的语速，0-15 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, SPEED);
        // 设置合成的语调，0-15 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, PITCH);
        synthesizer.setParams(params);
    }

    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return 合成参数Map
     */
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>, 其它发音人见文档
        params.put(SpeechSynthesizer.PARAM_SPEAKER, SPEAKER);
        // 设置合成的音量，0-15 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, VOLUME);
        // 设置合成的语速，0-15 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, SPEED);
        // 设置合成的语调，0-15 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, PITCH);
        if (!isOnlineSDK) {
            // 在线SDK版本没有此参数。

            /*
            params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
            // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
            // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
            // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
            // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
            // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
            // params.put(SpeechSynthesizer.PARAM_MIX_MODE_TIMEOUT, SpeechSynthesizer.PARAM_MIX_TIMEOUT_TWO_SECOND);
            // 离在线模式，强制在线优先。在线请求后超时2秒后，转为离线合成。
            */
            // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
            OfflineResource offlineResource = createOfflineResource(offlineVoice);
            // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
            params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
            params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, offlineResource.getModelFilename());
        }
        return params;
    }

    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(mContext, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
            toPrint("【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }


    protected void handle(Message msg) {
        int what = msg.what;
        switch (what) {
            case PRINT:
                print(msg);
                break;
            case UI_CHANGE_INPUT_TEXT_SELECTION:
//                if (msg.arg1 <= mInput.getText().length()) {
//                    mInput.setSelection(0, msg.arg1);
//                }
                break;
            case UI_CHANGE_SYNTHES_TEXT_SELECTION:
//                SpannableString colorfulText = new SpannableString(mInput.getText().toString());
//                if (msg.arg1 <= colorfulText.toString().length()) {
//                    colorfulText.setSpan(new ForegroundColorSpan(Color.GRAY), 0, msg.arg1, Spannable
//                            .SPAN_EXCLUSIVE_EXCLUSIVE);
//                    mInput.setText(colorfulText);
//                }
                break;
            default:
                break;
        }
    }

    private void toPrint(String str) {
        Message msg = Message.obtain();
        msg.obj = str;
        mainHandler.sendMessage(msg);
    }

    private void print(Message msg) {
        String message = (String) msg.obj;
        if (message != null) {
            scrollLog(message);
        }
    }

    private void scrollLog(String message) {
        Spannable colorMessage = new SpannableString(message + "\n");
        colorMessage.setSpan(new ForegroundColorSpan(0xff0000ff), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mShowText.append(colorMessage);
//        Layout layout = mShowText.getLayout();
//        if (layout != null) {
//            int scrollAmount = layout.getLineTop(mShowText.getLineCount()) - mShowText.getHeight();
//            if (scrollAmount > 0) {
//                mShowText.scrollTo(0, scrollAmount + mShowText.getCompoundPaddingBottom());
//            } else {
//                mShowText.scrollTo(0, 0);
//            }
//        }
    }
}
