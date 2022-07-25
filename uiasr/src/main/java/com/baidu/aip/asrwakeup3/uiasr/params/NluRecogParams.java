package com.baidu.aip.asrwakeup3.uiasr.params;

import android.content.SharedPreferences;
import com.baidu.speech.asr.SpeechConstant;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by fujiayi on 2017/6/24.
 */

public class NluRecogParams extends CommonRecogParams {


    private static final String TAG = "NluRecogParams";

    public NluRecogParams() {
        super();
        stringParams.addAll(Arrays.asList(SpeechConstant.NLU, SpeechConstant.ASR_PUNCTUATION_MODE));

        intParams.addAll(Arrays.asList(SpeechConstant.DECODER, SpeechConstant.PROP));

        boolParams.addAll(Arrays.asList("_nlu_online"));
        boolParams.addAll(Arrays.asList(SpeechConstant.DISABLE_PUNCTUATION));

        // copyOfflineResource(context);
    }

    public Map<String, Object> fetch(SharedPreferences sp) {

        Map<String, Object> map = super.fetch(sp);
        if (sp.getBoolean("_grammar", false)) {
            Map<String, Object> offlineParams = OfflineRecogParams.fetchOfflineParams();
            map.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH,
                    offlineParams.get(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH));

        }
        if (sp.getBoolean("_slot_data", false)) {
            map.putAll(OfflineRecogParams.fetchSlotDataParam());
        }
        return map;

    }


}
