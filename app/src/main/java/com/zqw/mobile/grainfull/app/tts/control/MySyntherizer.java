package com.zqw.mobile.grainfull.app.tts.control;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;

import com.baidu.tts.client.SpeechSynthesizeBag;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.zqw.mobile.grainfull.app.tts.MainHandlerConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 该类是对SpeechSynthesizer的封装
 * <p>
 * Created by fujiayi on 2017/5/24.
 */

public class MySyntherizer implements MainHandlerConstant {

    protected SpeechSynthesizer mSpeechSynthesizer;
    protected Context context;
    protected Handler mainHandler;

    private static final String TAG = "NonBlockSyntherizer";

    protected static volatile boolean isInitied = false;

    public MySyntherizer(Context context, InitConfig initConfig, Handler mainHandler) {
        this(context, mainHandler);
        init(initConfig);
    }

    protected MySyntherizer(Context context, Handler mainHandler) {
        if (isInitied) {
            // SpeechSynthesizer.getInstance() 不要连续调用
            throw new RuntimeException("MySynthesizer 对象里面 SpeechSynthesizer还未释放，请勿新建一个新对象。" +
                    "如果需要新建，请先调用之前MySynthesizer对象的release()方法。");
        }
        Log.i("MySyntherizer", "MySyntherizer new called");
        this.context = context;
        this.mainHandler = mainHandler;
        isInitied = true;
    }

    /**
     * 注意该方法需要在新线程中调用。且该线程不能结束。详细请参见NonBlockSyntherizer的实现
     *
     * @param config 配置
     * @return 是否初始化成功
     */
    protected boolean init(InitConfig config) {

        sendToUiThread("初始化开始");
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();

        mSpeechSynthesizer.setContext(context);
        Log.i("MySyntherizer", "包名:" + context.getPackageName());

        SpeechSynthesizerListener listener = config.getListener();

        // listener = new SwitchSpeakerListener(mainHandler,context,this); // 测试播放过程中切换发音人逻辑
        mSpeechSynthesizer.setSpeechSynthesizerListener(listener);


        // 请替换为语音开发者平台上注册应用得到的App ID ,AppKey ，Secret Key ，填写在SynthActivity的开始位置
        mSpeechSynthesizer.setAppId(config.getAppId());
        mSpeechSynthesizer.setApiKey(config.getAppKey(), config.getSecretKey());

        setParams(config.getParams());
        // 初始化tts
        int result = mSpeechSynthesizer.initTts(config.getTtsMode());
        if (result != 0) {
            sendToUiThread("【error】initTts 初始化失败 + errorCode：" + result);
            return false;
        }

        // 设置播放的音频流类型，具体参数和组合见AudioAttributes,https://source.android.google.cn/devices/audio/attributes
        // mSpeechSynthesizer.setAudioAttributes(AudioAttributes.USAGE_MEDIA,AudioAttributes.CONTENT_TYPE_MUSIC);

        // 此时可以调用 speak和synthesize方法
        sendToUiThread(INIT_SUCCESS, "合成引擎初始化成功");
        return true;
    }

    /**
     * 合成并播放
     *
     * @param text 小于1024 GBK字节，即512个汉字或者字母数字
     * @return =0表示成功
     */
    public int speak(String text) {
        if (!isInitied) {
            throw new RuntimeException("TTS 还未初始化");
        }
        Log.i(TAG, "speak text:" + text);
        return mSpeechSynthesizer.speak(text);
    }

    /**
     * 合成并播放
     *
     * @param text        小于1024 GBK字节，即512个汉字或者字母数字
     * @param utteranceId 用于listener的回调，默认"0"
     * @return =0表示成功
     */
    public int speak(String text, String utteranceId) {
        if (!isInitied) {
            throw new RuntimeException("TTS 还未初始化");
        }
        return mSpeechSynthesizer.speak(text, utteranceId);
    }

    /**
     * 只合成不播放
     *
     * @param text 合成的文本
     * @return =0表示成功
     */
    public int synthesize(String text) {
        if (!isInitied) {
            // SpeechSynthesizer.getInstance() 不要连续调用
            throw new RuntimeException("TTS 还未初始化");
        }
        return mSpeechSynthesizer.synthesize(text);
    }

    public int synthesize(String text, String utteranceId) {
        if (!isInitied) {
            // SpeechSynthesizer.getInstance() 不要连续调用
            throw new RuntimeException("TTS 还未初始化");
        }
        return mSpeechSynthesizer.synthesize(text, utteranceId);
    }

    public int batchSpeak(List<Pair<String, String>> texts) {
        if (!isInitied) {
            throw new RuntimeException("TTS 还未初始化");
        }
        List<SpeechSynthesizeBag> bags = new ArrayList<SpeechSynthesizeBag>();
        for (Pair<String, String> pair : texts) {
            SpeechSynthesizeBag speechSynthesizeBag = new SpeechSynthesizeBag();
            speechSynthesizeBag.setText(pair.first);
            if (pair.second != null) {
                speechSynthesizeBag.setUtteranceId(pair.second);
            }
            bags.add(speechSynthesizeBag);

        }
        return mSpeechSynthesizer.batchSpeak(bags);
    }

    public void setParams(Map<String, String> params) {
        if (params != null) {
            for (Map.Entry<String, String> e : params.entrySet()) {
                mSpeechSynthesizer.setParam(e.getKey(), e.getValue());
            }
        }
    }

    public int pause() {
        return mSpeechSynthesizer.pause();
    }

    public int resume() {
        return mSpeechSynthesizer.resume();
    }

    public int stop() {
        return mSpeechSynthesizer.stop();
    }

    /**
     * 引擎在合成时该方法不能调用！！！
     * 注意 只有 TtsMode.MIX 才可以切换离线发音
     *
     * @return
     */
    public int loadModel(String modelFilename, String textFilename) {
        int res = mSpeechSynthesizer.loadModel(modelFilename, textFilename);
        sendToUiThread("切换离线发音人成功。");
        return res;
    }

    /**
     * 设置播放音量，默认已经是最大声音
     * 0.0f为最小音量，1.0f为最大音量
     *
     * @param leftVolume  [0-1] 默认1.0f
     * @param rightVolume [0-1] 默认1.0f
     */
    public void setStereoVolume(float leftVolume, float rightVolume) {
        mSpeechSynthesizer.setStereoVolume(leftVolume, rightVolume);
    }

    public void release() {
        Log.i("MySyntherizer", "MySyntherizer release called");
        if (!isInitied) {
            // 这里报错是因为连续两次 new MySyntherizer。
            // 必须第一次new 之后，调用release方法
            throw new RuntimeException("TTS 还未初始化");
        }
        mSpeechSynthesizer.stop();
        mSpeechSynthesizer.release();
        mSpeechSynthesizer = null;
        isInitied = false;
    }


    protected void sendToUiThread(String message) {
        sendToUiThread(PRINT, message);
    }

    protected void sendToUiThread(int action, String message) {
        Log.i(TAG, message);
        if (mainHandler == null) { // 可以不依赖mainHandler
            return;
        }
        Message msg = Message.obtain();
        msg.what = action;
        msg.obj = message + "\n";
        mainHandler.sendMessage(msg);
    }
}
